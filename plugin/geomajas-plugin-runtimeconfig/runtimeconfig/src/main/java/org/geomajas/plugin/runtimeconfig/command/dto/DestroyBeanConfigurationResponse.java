/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.runtimeconfig.command.dto;

import org.geomajas.command.CommandResponse;

/**
 * Response object for {@link org.geomajas.configurator.command.configurator.DestroyBeanConfigurationCommand}.
 * 
 * @author Jan De Moerloose
 */
public class DestroyBeanConfigurationResponse extends CommandResponse {

	private static final long serialVersionUID = 100L;

	private String name;

	
	public String getName() {
		return name;
	}

	
	public void setName(String name) {
		this.name = name;
	}
	
	
}
