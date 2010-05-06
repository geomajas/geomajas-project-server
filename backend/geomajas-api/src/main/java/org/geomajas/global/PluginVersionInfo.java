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

package org.geomajas.global;

/**
 * Version information for a plug-in. Used both to define the plug-in itself and for dependencies.
 *
 * @author Joachim Van der Auwera
 */
@Api(allMethods = true)
public class PluginVersionInfo {

	private String name;
	private String version;

	/**
	 * Get the plug-in's name.
	 *
	 * @return plug-in name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the plug-in's name.
	 *
	 * @param name plug-in name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the plug-in's version.
	 *
	 * @return version for the plug-in
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Set the plg-in's version.
	 *
	 * @param version version for the plug-in
	 */
	public void setVersion(String version) {
		this.version = version;
	}
}
