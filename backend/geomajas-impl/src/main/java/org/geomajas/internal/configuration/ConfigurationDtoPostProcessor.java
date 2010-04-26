/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.internal.configuration;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.Layer;
import org.geomajas.layer.LayerException;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

/**
 * Post-processes configuration DTOs. Generally responsible for any behaviour that would violate the DTO contract
 * (especially for GWT) if it would be added to the configuration objects themselves , such as hooking up client
 * configurations to their server layers.
 * 
 * @author Jan De Moerloose
 */
@Component
public class ConfigurationDtoPostProcessor {

	private static final double METER_PER_INCH = 0.0254;

	@Autowired
	private DtoConverterService converterService;

	@Autowired
	private GeoService geoService;

	@Autowired(required = false)
	protected Map<String, ClientApplicationInfo> applicationMap = new LinkedHashMap<String, ClientApplicationInfo>();

	@Autowired(required = false)
	protected Map<String, NamedStyleInfo> namedStyleMap = new LinkedHashMap<String, NamedStyleInfo>();

	@Autowired(required = false)
	protected Map<String, Layer<?>> layerMap = new LinkedHashMap<String, Layer<?>>();

	public ConfigurationDtoPostProcessor() {

	}

	@PostConstruct
	public void processConfiguration() throws BeansException {
		for (ClientApplicationInfo application : applicationMap.values()) {
			try {
				postProcess(application);
			} catch (LayerException e) {
				throw new BeanInitializationException("Could not post process configuration", e);
			}
		}
		for (NamedStyleInfo style : namedStyleMap.values()) {
			postProcess(style);
		}
	}

	private ClientApplicationInfo postProcess(ClientApplicationInfo client) throws LayerException {
		// initialize maps
		for (ClientMapInfo map : client.getMaps()) {
			map.setUnitLength(getUnitLength(map.getCrs(), map.getInitialBounds()));
			map.setPixelLength(METER_PER_INCH / map.getUnitLength() / client.getScreenDpi());
			for (ClientLayerInfo layer : map.getLayers()) {
				Layer<?> serverLayer = layerMap.get(layer.getServerLayerId());
				if (serverLayer == null) {
					throw new LayerException(ExceptionCode.LAYER_NOT_FOUND, layer.getServerLayerId());
				}
				layer.setLayerInfo(serverLayer.getLayerInfo());
				layer
						.setMaxExtent(getClientMaxExtent(map.getCrs(), layer.getCrs(), layer.getLayerInfo()
								.getMaxExtent()));
				if (layer instanceof ClientVectorLayerInfo) {
					postProcess((ClientVectorLayerInfo) layer);
				}
			}
		}
		return client;
	}

	private ClientVectorLayerInfo postProcess(ClientVectorLayerInfo layer) throws LayerException {
		// copy feature info from server if not explicitly defined
		if (layer.getFeatureInfo() == null) {
			VectorLayerInfo serverInfo = (VectorLayerInfo) layer.getLayerInfo();
			layer.setFeatureInfo(serverInfo.getFeatureInfo());
		}
		return layer;
	}

	private NamedStyleInfo postProcess(NamedStyleInfo client) {
		// index styles
		int i = 0;
		for (FeatureStyleInfo style : client.getFeatureStyles()) {
			style.setIndex(i++);
			style.setStyleId(client.getName() + "-" + style.getIndex());
		}
		return client;
	}

	private double getUnitLength(String mapCrsKey, Bbox mapBounds) throws LayerException {
		try {
			if (null == mapBounds) {
				throw new LayerException(ExceptionCode.MAP_MAX_EXTENT_MISSING);
			}
			CoordinateReferenceSystem crs = CRS.decode(mapCrsKey);
			// GeodeticCalculator calculator = new GeodeticCalculator(crs);
			// calculator.setStartingPosition(new DirectPosition2D(crs, mapBounds.getX(), mapBounds.getY()));
			// calculator.setDestinationPosition(new DirectPosition2D(crs, mapBounds.getMaxX(), mapBounds.getY()));
			// double distance = calculator.getOrthodromicDistance();
			// return distance / mapBounds.getWidth();
			Coordinate c1 = new Coordinate(0, 0);
			Coordinate c2 = new Coordinate(1, 0);
			double distance = JTS.orthodromicDistance(c1, c2, crs);
			return distance;
		} catch (FactoryException e) {
			throw new LayerException(e, ExceptionCode.TRANSFORMER_CREATE_LAYER_TO_MAP_FAILED);
		} catch (TransformException e) {
			throw new LayerException(e, ExceptionCode.TRANSFORMER_CREATE_LAYER_TO_MAP_FAILED);
		}
	}

	public Bbox getClientMaxExtent(String mapCrsKey, String layerCrsKey, Bbox serverBbox) throws LayerException {
		if (mapCrsKey.equals(layerCrsKey)) {
			return serverBbox;
		}
		try {
			CoordinateReferenceSystem mapCrs = CRS.decode(mapCrsKey);
			CoordinateReferenceSystem layerCrs = CRS.decode(layerCrsKey);
			Envelope serverEnvelope = converterService.toInternal(serverBbox);
			MathTransform transformer = geoService.findMathTransform(layerCrs, mapCrs);
			return converterService.toDto(JTS.transform(serverEnvelope, transformer));
		} catch (FactoryException e) {
			throw new LayerException(e, ExceptionCode.TRANSFORMER_CREATE_LAYER_TO_MAP_FAILED);
		} catch (TransformException e) {
			throw new LayerException(e, ExceptionCode.TRANSFORMER_CREATE_LAYER_TO_MAP_FAILED);
		} catch (GeomajasException e) {
			throw new LayerException(e, ExceptionCode.TRANSFORMER_CREATE_LAYER_TO_MAP_FAILED);
		}
	}

}
