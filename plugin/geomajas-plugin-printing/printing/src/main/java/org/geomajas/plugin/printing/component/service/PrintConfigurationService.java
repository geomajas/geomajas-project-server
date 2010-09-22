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

import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;

/**
 * Provides safe access to print configuration data.
 * TODO: printing still needs map info from server, should client pass it in map component info ?
 * 
 * @author Jan De Moerloose
 */
public interface PrintConfigurationService {

	/**
	 * Get information about the map with specified mapId in the application with the specified applicationId.
	 * 
	 * @param mapId
	 *            map Id
	 * @param applicationId
	 *            application id
	 * @return {@link ClientMapInfo}
	 */
	ClientMapInfo getMapInfo(String mapId, String applicationId);

	/**
	 * Get information about the specified raster layer.
	 * 
	 * @param layerId
	 *            layer id
	 * @return {@link RasterLayerInfo}
	 */
	RasterLayerInfo getRasterLayerInfo(String layerId);

	/**
	 * Get information about specified vector layer.
	 * 
	 * @param layerId
	 *            layer id
	 * @return {@link VectorLayerInfo}
	 */
	VectorLayerInfo getVectorLayerInfo(String layerId);

}
