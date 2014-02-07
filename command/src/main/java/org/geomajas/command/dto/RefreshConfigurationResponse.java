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
package org.geomajas.command.dto;

import org.geomajas.command.CommandResponse;

import java.util.Arrays;

/**
 * Response object for {@link org.geomajas.command.configuration.RefreshConfigurationCommand}.
 * 
 * @author Jan De Moerloose
 */
public class RefreshConfigurationResponse extends CommandResponse {

	private static final long serialVersionUID = 160L;

	private String[] applicationNames;

	/**
	 * Returns an array of client applications in the newly loaded context.
	 * 
	 * @return array of application names
	 */
	public String[] getApplicationNames() {
		return applicationNames;
	}

	/**
	 * Sets an array of client applications in the newly loaded context.
	 * 
	 * @param applicationNames
	 *            array of application names
	 */
	public void setApplicationNames(String[] applicationNames) {
		this.applicationNames = applicationNames;
	}

	@Override
	public String toString() {
		return "RefreshConfigurationResponse{" +
				"applicationNames=" + (applicationNames == null ? null : Arrays.asList(applicationNames)) +
				'}';
	}
}
