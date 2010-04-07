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

package org.geomajas.internal.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.layer.Layer;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.VectorLayer;
import org.geomajas.service.ConfigurationService;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Container class which contains runtime information about the parameters and other information for Geomajas. Values
 * are injected using Spring.
 * 
 * @author Joachim Van der Auwera
 */
@Component
public class ConfigurationServiceImpl implements ConfigurationService {

	@Autowired(required = false)
	protected Map<String, Layer<?>> layerMap = new LinkedHashMap<String, Layer<?>>();

	@Autowired(required = false)
	protected Map<String, VectorLayer> vectorLayerMap = new LinkedHashMap<String, VectorLayer>();

	@Autowired(required = false)
	protected Map<String, RasterLayer> rasterLayerMap = new LinkedHashMap<String, RasterLayer>();

	@Autowired(required = false)
	protected Map<String, ClientApplicationInfo> applicationMap = new LinkedHashMap<String, ClientApplicationInfo>();

	public VectorLayer getVectorLayer(String id) {
		return id == null ? null : vectorLayerMap.get(id);
	}

	public RasterLayer getRasterLayer(String id) {
		return id == null ? null : rasterLayerMap.get(id);
	}

	public Layer<?> getLayer(String id) {
		return id == null ? null : layerMap.get(id);
	}

	public ClientMapInfo getMap(String mapId, String applicationId) {
		if (null == mapId || null == applicationId) {
			return null;
		}
		ClientApplicationInfo application = applicationMap.get(applicationId);
		if (application != null) {
			for (ClientMapInfo map : application.getMaps()) {
				if (mapId.equals(map.getId())) {
					return map;
				}
			}
		}
		return null;
	}

	public CoordinateReferenceSystem getCrs(String crs) throws LayerException {
		try {
			return CRS.decode(crs);
		} catch (NoSuchAuthorityCodeException e) {
			throw new LayerException(e, ExceptionCode.CRS_DECODE_FAILURE_FOR_MAP, crs);
		} catch (FactoryException e) {
			throw new LayerException(e, ExceptionCode.LAYER_CRS_PROBLEMATIC, crs);
		}
	}

}
