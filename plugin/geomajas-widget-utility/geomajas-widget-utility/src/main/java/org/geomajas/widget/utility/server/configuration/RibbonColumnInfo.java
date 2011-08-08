/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.widget.utility.server.configuration;

import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.Parameter;
import org.geomajas.configuration.client.ClientToolInfo;
import org.geomajas.configuration.client.ClientUserDataInfo;

/**
 * Ribbon column configuration object. Determines the contents for a single column within a ribbon group.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class RibbonColumnInfo implements ClientUserDataInfo {

	private static final long serialVersionUID = 100L;

	private String type;

	private List<ClientToolInfo> tools;

	private List<Parameter> parameters;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<ClientToolInfo> getTools() {
		return tools;
	}

	public void setTools(List<ClientToolInfo> tools) {
		this.tools = tools;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}
}