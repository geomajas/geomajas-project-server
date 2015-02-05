/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
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
