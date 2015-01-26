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
package org.geomajas.command.dto;

import org.geomajas.command.CommandRequest;

import java.util.Arrays;

/**
 * Request object for {@link org.geomajas.command.configuration.RefreshConfigurationCommand}.
 * 
 * @author Jan De Moerloose
 */
public class RefreshConfigurationRequest implements CommandRequest {

	private static final long serialVersionUID = 160L;

	/**
	 * Command name for this request.
	 *
	 * @since 1.9.0
	 * */
	public static final String COMMAND = "command.configuration.Refresh";

	private String[] configLocations;

	/**
	 * Get the new array of resource locations in classpath notation.
	 * 
	 * @return array
	 */
	public String[] getConfigLocations() {
		return configLocations;
	}

	/**
	 * Set the new array of resource locations in classpath notation, e.g.
	 * ["path/to/applicationContext.xml","path/to/layer1.xml",...]
	 * 
	 * @param configLocations
	 *            array of classpath resources
	 */
	public void setConfigLocations(String[] configLocations) {
		this.configLocations = configLocations;
	}

	@Override
	public String toString() {
		return "RefreshConfigurationRequest{" +
				"configLocations=" + (configLocations == null ? null : Arrays.asList(configLocations)) +
				'}';
	}
}
