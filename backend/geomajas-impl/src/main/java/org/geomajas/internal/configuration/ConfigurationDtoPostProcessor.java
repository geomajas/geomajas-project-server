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
import org.geomajas.configuration.LayerInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.configuration.client.ScaleUnit;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.Layer;
import org.geomajas.layer.LayerException;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.GeodeticCalculator;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Post-processes configuration DTOs. Generally responsible for any behaviour that would violate the DTO contract
 * (especially for GWT) if it would be added to the configuration objects themselves, such as hooking up client
 * configurations to their server layers.
 * 
 * @author Jan De Moerloose
 */
@Component
public class ConfigurationDtoPostProcessor {

	private static final double METER_PER_INCH = 0.0254;

	private final Logger log = LoggerFactory.getLogger(ConfigurationDtoPostProcessor.class);

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
			// result should be m = (m/inch) / (number/inch)
			map.setPixelLength(METER_PER_INCH / client.getScreenDpi());
			for (ClientLayerInfo layer : map.getLayers()) {
				String layerId = layer.getServerLayerId();
				Layer<?> serverLayer = layerMap.get(layerId);
				if (serverLayer == null) {
					throw new LayerException(ExceptionCode.LAYER_NOT_FOUND, layerId);
				}
				LayerInfo layerInfo = serverLayer.getLayerInfo();
				layer.setLayerInfo(layerInfo);
				layer.setMaxExtent(getClientMaxExtent(map.getCrs(), layer.getCrs(), layerInfo.getMaxExtent(), layerId));
				if (layer.getScaleUnit() == ScaleUnit.NORMAL) {
					// force pixel per unit (expected by client)
					layer.setScaleUnit(ScaleUnit.PIXEL_PER_UNIT);
					// result should be pix/map unit = number * (m/map unit) / (m/pix)
					layer.setMaximumScale(new ScaleInfo(layer.getMaximumScale().getValue() * map.getUnitLength()
							/ map.getPixelLength()));
					layer.setMinimumScale(new ScaleInfo(layer.getMinimumScale().getValue() * map.getUnitLength()
							/ map.getPixelLength()));
				}
				log.debug("Layer " + layer.getId() + " has scale range : " + layer.getMinimumScale().getValue() + ","
						+ layer.getMaximumScale().getValue());
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
			CoordinateReferenceSystem crs = geoService.getCrs(mapCrsKey);
			GeodeticCalculator calculator = new GeodeticCalculator(crs);
			Coordinate center = new Coordinate(0.5 * (mapBounds.getX() + mapBounds.getMaxX()),
					0.5 * (mapBounds.getY() + mapBounds.getMaxY()));
			calculator.setStartingPosition(new DirectPosition2D(crs, center.getX(), center.getY()));
			calculator.setDestinationPosition(new DirectPosition2D(crs, center.getX() + 1, center.getY()));
			return calculator.getOrthodromicDistance();
		} catch (TransformException e) {
			throw new LayerException(e, ExceptionCode.TRANSFORMER_CREATE_LAYER_TO_MAP_FAILED);
		}
	}

	public Bbox getClientMaxExtent(String mapCrsKey, String layerCrsKey, Bbox serverBbox, String layer)
			throws LayerException {
		if (mapCrsKey.equals(layerCrsKey)) {
			return serverBbox;
		}
		try {
			CoordinateReferenceSystem mapCrs = geoService.getCrs(mapCrsKey);
			CoordinateReferenceSystem layerCrs = geoService.getCrs(layerCrsKey);
			Envelope serverEnvelope = converterService.toInternal(serverBbox);
			MathTransform transformer = geoService.findMathTransform(layerCrs, mapCrs);
			Bbox res = converterService.toDto(JTS.transform(serverEnvelope, transformer));
			if (Double.isNaN(res.getX()) || Double.isNaN(res.getY()) || Double.isNaN(res.getWidth())
					|| Double.isNaN(res.getHeight())) {
				throw new LayerException(ExceptionCode.LAYER_EXTENT_CANNOT_CONVERT, layer, mapCrsKey);
			}
			return res;
		} catch (TransformException e) {
			throw new LayerException(e, ExceptionCode.TRANSFORMER_CREATE_LAYER_TO_MAP_FAILED);
		} catch (GeomajasException e) {
			throw new LayerException(e, ExceptionCode.TRANSFORMER_CREATE_LAYER_TO_MAP_FAILED);
		}
	}

}
