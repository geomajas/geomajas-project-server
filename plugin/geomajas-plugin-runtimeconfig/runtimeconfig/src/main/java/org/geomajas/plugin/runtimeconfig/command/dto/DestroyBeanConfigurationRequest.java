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

import java.util.List;

import org.geomajas.command.CommandRequest;

/**
 * Request object for {@link org.geomajas.configurator.command.configurator.DestroyBeanConfigurationCommand}.
 * 
 * @author Jan De Moerloose
 */
public class DestroyBeanConfigurationRequest implements CommandRequest {

	private static final long serialVersionUID = 100L;

	private List<String> beanNames;

	public List<String> getBeanNames() {
		return beanNames;
	}

	public void setBeanNames(List<String> beanNames) {
		this.beanNames = beanNames;
	}

}
