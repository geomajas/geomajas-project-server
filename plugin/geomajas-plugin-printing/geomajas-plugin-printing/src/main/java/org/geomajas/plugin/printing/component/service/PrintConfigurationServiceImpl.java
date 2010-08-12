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
package org.geomajas.plugin.printing.component.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.VectorLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Trimmed down configuration service for printing.
 * 
 * @author Jan De Moerloose
 *
 */
@Component("printConfigurationService")
public class PrintConfigurationServiceImpl implements PrintConfigurationService {

	@Autowired(required = false)
	protected Map<String, ClientApplicationInfo> applicationMap = new LinkedHashMap<String, ClientApplicationInfo>();

	@Autowired(required = false)
	protected Map<String, VectorLayer> vectorLayerMap = new LinkedHashMap<String, VectorLayer>();

	@Autowired(required = false)
	protected Map<String, RasterLayer> rasterLayerMap = new LinkedHashMap<String, RasterLayer>();

	public ClientMapInfo getMapInfo(String mapId, String applicationId) {
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

	public RasterLayerInfo getRasterLayerInfo(String layerId) {
		return layerId == null ? null : rasterLayerMap.get(layerId).getLayerInfo();
	}

	public VectorLayerInfo getVectorLayerInfo(String layerId) {
		return layerId == null ? null : vectorLayerMap.get(layerId).getLayerInfo();
	}

}
