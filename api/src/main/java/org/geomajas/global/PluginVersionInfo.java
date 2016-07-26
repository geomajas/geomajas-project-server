/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.global;

import javax.validation.constraints.NotNull;

import org.geomajas.annotation.Api;

/**
 * Version information for a plug-in. Used both to define the plug-in itself and for dependencies.
 *
 * @author Joachim Van der Auwera
 * @since 1.7.0
 */
@Api(allMethods = true)
public class PluginVersionInfo {

	@NotNull
	private String name;

	@NotNull
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
	
	/**
	 * {@inheritDoc}
	 * @since 1.10.0
	 */
	public String toString() {
		return "PluginVersionInfo(name=" + this.getName() + ", version=" + this.getVersion() + ")";
	}

}
