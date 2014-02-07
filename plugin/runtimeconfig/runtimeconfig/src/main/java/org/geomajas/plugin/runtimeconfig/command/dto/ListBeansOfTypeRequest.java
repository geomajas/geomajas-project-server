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
package org.geomajas.plugin.runtimeconfig.command.dto;

import org.geomajas.command.CommandRequest;
import org.geomajas.plugin.runtimeconfig.dto.bean.BeanTypeInfo;

/**
 * Request object for {@link org.geomajas.configurator.command.configurator.ListBeansOfTypeCommand}.
 * 
 * @author Jan De Moerloose
 */
public class ListBeansOfTypeRequest implements CommandRequest {

	private static final long serialVersionUID = 100L;

	private String pluginName;

	private BeanTypeInfo type;

	public String getPluginName() {
		return pluginName;
	}

	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}

	public BeanTypeInfo getType() {
		return type;
	}

	public void setType(BeanTypeInfo type) {
		this.type = type;
	}
}
