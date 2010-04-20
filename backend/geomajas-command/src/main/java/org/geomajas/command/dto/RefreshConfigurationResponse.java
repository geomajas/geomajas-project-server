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
package org.geomajas.command.dto;

import org.geomajas.command.CommandResponse;

/**
 * Response object for {@link org.geomajas.command.configuration.RefreshConfigurationCommand}.
 * 
 * @author Jan De Moerloose
 * 
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

}
