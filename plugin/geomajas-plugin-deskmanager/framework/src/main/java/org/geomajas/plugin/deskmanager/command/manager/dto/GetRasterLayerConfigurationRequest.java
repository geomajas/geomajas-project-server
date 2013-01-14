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
package org.geomajas.plugin.deskmanager.command.manager.dto;

import java.util.LinkedHashMap;
import java.util.Map;

import org.geomajas.command.CommandRequest;

/**
 * @author Jan De Moerloose
 */
public class GetRasterLayerConfigurationRequest implements CommandRequest {

	private static final long serialVersionUID = 1L;

	public static final String COMMAND = "command.discovery.GetRasterLayerConfiguration";

	private RasterCapabilitiesInfo rasterCapabilitiesInfo;

	private Map<String, String> connectionProperties = new LinkedHashMap<String, String>();

	public Map<String, String> getConnectionProperties() {
		return connectionProperties;
	}

	public void setConnectionProperties(Map<String, String> connectionProperties) {
		this.connectionProperties = connectionProperties;
	}
	
	public RasterCapabilitiesInfo getRasterCapabilitiesInfo() {
		return rasterCapabilitiesInfo;
	}

	
	public void setRasterCapabilitiesInfo(RasterCapabilitiesInfo rasterCapabilitiesInfo) {
		this.rasterCapabilitiesInfo = rasterCapabilitiesInfo;
	}

	
}
