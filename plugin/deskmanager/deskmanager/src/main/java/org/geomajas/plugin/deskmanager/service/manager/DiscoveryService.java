/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.service.manager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.geomajas.plugin.deskmanager.DeskmanagerException;
import org.geomajas.plugin.deskmanager.command.manager.dto.RasterCapabilitiesInfo;
import org.geomajas.plugin.deskmanager.command.manager.dto.DynamicRasterLayerConfiguration;
import org.geomajas.plugin.deskmanager.command.manager.dto.VectorCapabilitiesInfo;
import org.geomajas.plugin.deskmanager.command.manager.dto.DynamicVectorLayerConfiguration;
import org.geomajas.plugin.deskmanager.domain.dto.DynamicLayerConfiguration;

/**
 * @author Kristof Heirwegh
 */
public interface DiscoveryService {

	Map<String, Object> createBeanLayerDefinitionParameters(DynamicLayerConfiguration lc) throws Exception;

	List<VectorCapabilitiesInfo> getVectorCapabilities(Map<String, String> connectionProperties) throws 
			IOException, DeskmanagerException;

	DynamicVectorLayerConfiguration getVectorLayerConfiguration(Map<String, String> connectionProperties, 
			String layerName)
			throws IOException, DeskmanagerException;

	/**
	 * Get list of Raster Capabilities for each layer, for each CRS that is available on the server.
	 * 
	 * @param connectionProperties the connection parameters
	 * @return the list of layers
	 * @throws Exception
	 */
	List<RasterCapabilitiesInfo> getRasterCapabilities(Map<String, String> connectionProperties) throws Exception;

	DynamicRasterLayerConfiguration getRasterLayerConfiguration(Map<String, String> connectionProperties,
		RasterCapabilitiesInfo rasterCapabilitiesInfo)
			throws IOException, DeskmanagerException;
}
