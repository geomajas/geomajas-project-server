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

import java.beans.PropertyDescriptor;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.global.ExceptionCode;
import org.geomajas.layer.Layer;
import org.geomajas.layer.LayerException;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.GeodeticCalculator;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Post-processes configuration, mainly for setting ids and doing some coordinate transformations.
 * 
 * @author Jan De Moerloose
 * 
 */
@Component
public class ConfigurationBeanPostProcessor implements BeanPostProcessor {

	private static final double METER_PER_INCH = 0.0254;

	@Autowired
	private DtoConverterService converterService;

	@Autowired
	private GeoService geoService;

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		try {
			if (bean instanceof ClientApplicationInfo) {
				return postProcess((ClientApplicationInfo) bean);
			} else if (bean instanceof ClientVectorLayerInfo) {
				return postProcess((ClientVectorLayerInfo) bean);
			} else if (bean instanceof NamedStyleInfo) {
				return postProcess((NamedStyleInfo) bean);
			}
			return bean;
		} catch (LayerException e) {
			throw new BeanInitializationException("Could not post process configuration", e);
		}
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof Layer) {
			// force the layer info to have the same id as the layer (so clients know this) !!!
			Layer layer = (Layer) bean;
			assignId(layer.getLayerInfo(), beanName, true);
		} else {
			// assign the spring id unless the configuration explicitly defines the id and/or name property
			assignId(bean, beanName, false);
		}
		return bean;
	}

	private void assignId(Object bean, String beanName, boolean override) {
		try {
			PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(bean.getClass(), "id");
			if (descriptor != null) {
				if (override || (descriptor.getReadMethod().invoke(bean, null) == null)) {
					descriptor.getWriteMethod().invoke(bean, beanName);
				}
			}
		} catch (Exception be) {
			// ignore if no id property
		}
		try {
			PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(bean.getClass(), "name");
			if (descriptor != null) {
				if (override || (descriptor.getReadMethod().invoke(bean, null) == null)) {
					descriptor.getWriteMethod().invoke(bean, beanName);
				}
			}
		} catch (Exception be) {
			// ignore if no name property
		}
	}

	private ClientApplicationInfo postProcess(ClientApplicationInfo client) throws LayerException {
		// initialize maps
		for (ClientMapInfo map : client.getMaps()) {
			map.setUnitLength(getUnitLength(map.getCrs(), map.getInitialBounds()));
			map.setPixelLength(METER_PER_INCH / map.getUnitLength() / client.getScreenDpi());
			for (ClientLayerInfo layer : map.getLayers()) {
				layer
						.setMaxExtent(getClientMaxExtent(map.getCrs(), layer.getCrs(), layer.getLayerInfo()
								.getMaxExtent()));
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

	private NamedStyleInfo postProcess(NamedStyleInfo client) throws LayerException {
		// index styles
		int i = 0;
		for (FeatureStyleInfo style : client.getFeatureStyles()) {
			style.setIndex(i++);
		}
		return client;
	}

	private double getUnitLength(String mapCrsKey, Bbox mapBounds) throws LayerException {
		try {
			if (null == mapBounds) {
				throw new LayerException(ExceptionCode.MAP_MAX_EXTENT_MISSING);
			}
			CoordinateReferenceSystem crs = CRS.decode(mapCrsKey);
			GeodeticCalculator calculator = new GeodeticCalculator(crs);
			calculator.setStartingPosition(new DirectPosition2D(crs, mapBounds.getX(), mapBounds.getY()));
			calculator.setDestinationPosition(new DirectPosition2D(crs, mapBounds.getMaxX(), mapBounds.getY()));
			double distance = calculator.getOrthodromicDistance();
			return distance / mapBounds.getWidth();
		} catch (FactoryException e) {
			throw new LayerException(e, ExceptionCode.LAYER_CRS_INIT_PROBLEM);
		} catch (TransformException e) {
			throw new LayerException(e, ExceptionCode.LAYER_CRS_INIT_PROBLEM);
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
			throw new LayerException(e, ExceptionCode.LAYER_CRS_INIT_PROBLEM);
		} catch (TransformException e) {
			throw new LayerException(e, ExceptionCode.LAYER_CRS_INIT_PROBLEM);
		}
	}

}
