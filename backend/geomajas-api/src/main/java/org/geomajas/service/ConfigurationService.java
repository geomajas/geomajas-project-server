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
package org.geomajas.service;

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.global.Api;
import org.geomajas.layer.Layer;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.VectorLayer;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Utility service which is used to get some global data.
 *
 * @author Joachim Van der Auwera
 */
@Api(allMethods = true)
public interface ConfigurationService {

	/**
	 * Get the vector layer with given id.
	 *
	 * @param id layer id
	 * @return layer
	 */
	VectorLayer getVectorLayer(String id);

	/**
	 * Get the raster layer with given id.
	 *
	 * @param id layer id
	 * @return layer
	 */
	RasterLayer getRasterLayer(String id);

	/**
	 * Get the vector layer with given id.
	 *
	 * @param id vector layer id
	 * @return vector layer
	 */
	Layer<?> getLayer(String id);

	/**
	 * Get information about the map with specified mapId in the application with the specified applicationId.
	 *
	 * @param mapId map Id
	 * @param applicationId  application id
	 * @return {@link org.geomajas.configuration.client.ClientMapInfo}
	 */
	ClientMapInfo getMap(String mapId, String applicationId);

	/**
	 * Get the {@link org.opengis.referencing.crs.CoordinateReferenceSystem} with given code.
	 *
	 * @param crs Coordinate reference system code. (EPSG:xxxx)
	 * @return {@link org.opengis.referencing.crs.CoordinateReferenceSystem}
	 * @throws LayerException CRS code not found
	 */
	CoordinateReferenceSystem getCrs(String crs) throws LayerException;
}
