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
package org.geomajas.configuration.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.Parameter;

/**
 * Representation of a tool.
 * 
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class ClientToolInfo implements Serializable {

	private static final long serialVersionUID = 151L;

	@NotNull
	private String id;
	
	private String toolId;

	private List<Parameter> parameters = new ArrayList<Parameter>();

	/**
	 * Get the list of parameters for this tool.
	 * 
	 * @return list of {@link Parameter}
	 */
	public List<Parameter> getParameters() {
		return parameters;
	}

	/**
	 * Set the list of parameters for this tool.
	 * 
	 * @param value
	 *            list of {@link Parameter}
	 */
	public void setParameters(List<Parameter> value) {
		parameters = value;
	}

	/**
	 * Get the id to fetch the {@link org.geomajas.gwt.client.action.ToolbarBaseAction}.
	 * 
	 * @return tool id
	 */
	public String getId() {
		return null == toolId ? id : toolId;
	}

	/**
	 * Set the id unique for the tool.
	 * 
	 * @param value
	 *            unique tool id
	 */
	public void setId(String value) {
		this.id = value;
	}

	/**
	 * Set the id used for creating the tool in {@link org.geomajas.gwt.client.action.toolbar.ToolbarRegistry}.
	 * 
	 * @param toolId tool id
	 * @since 1.10.0
	 */
	public void setToolId(String toolId) {
		this.toolId = toolId;
	}
	
	/**
	 * Get the id used for creating the tool in {@link org.geomajas.gwt.client.action.toolbar.ToolbarRegistry}.
	 * 
	 * @return tool id
	 * @since 1.10.0
	 */
	public String getToolId() {
		return toolId;
	}

}
