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
 * @author Kristof Heirwegh
 */
public class GetVectorCapabilitiesRequest implements CommandRequest {

	private static final long serialVersionUID = 1L;

	// neutral & independent strings that accidentally contain the same value as their geotools counterparts...
	public static final String PROPERTY_WFS_CAPABILITIESURL = "WFSDataStoreFactory:GET_CAPABILITIES_URL";

	public static final String PROPERTY_WFS_USERNAME = "WFSDataStoreFactory:USERNAME";

	public static final String PROPERTY_WFS_PASSWORD = "WFSDataStoreFactory:PASSWORD";

	public static final String PROPERTY_DATABASE_DBTYPE = "dbtype";

	public static final String PROPERTY_DATABASE_HOST = "host";

	public static final String PROPERTY_DATABASE_PORT = "port";

	public static final String PROPERTY_DATABASE_NAMESPACE = "namespace";

	public static final String PROPERTY_DATABASE_DATABASE = "database";

	public static final String PROPERTY_DATABASE_USER = "user";

	public static final String PROPERTY_DATABASE_PASSWD = "passwd";

	public static final String COMMAND = "command.discovery.GetVectorCapabilities";

	private Map<String, String> connectionProperties = new LinkedHashMap<String, String>();

	public Map<String, String> getConnectionProperties() {
		return connectionProperties;
	}

	public void setConnectionProperties(Map<String, String> connectionProperties) {
		this.connectionProperties = connectionProperties;
	}

}
