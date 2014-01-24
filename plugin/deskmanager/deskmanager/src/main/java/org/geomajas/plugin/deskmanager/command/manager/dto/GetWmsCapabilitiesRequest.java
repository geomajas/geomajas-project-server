/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
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
public class GetWmsCapabilitiesRequest implements CommandRequest {

	private static final long serialVersionUID = 1L;

	public static final String COMMAND = "command.deskmanager.manager.GetWmsCapabilities";

	public static final String GET_CAPABILITIES_URL = "GetCapabilitiesURL";

	/**
	 * @see org.geomajas.plugin.deskmanager.service.manager.DiscoveryService for the names
	 */
	private Map<String, String> connectionProperties = new LinkedHashMap<String, String>();

	public Map<String, String> getConnectionProperties() {
		return connectionProperties;
	}

	public void setConnectionProperties(Map<String, String> connectionProperties) {
		this.connectionProperties = connectionProperties;
	}

}
