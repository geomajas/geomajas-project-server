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
package org.geomajas.configuration.client;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.IsInfo;
import org.geomajas.configuration.Parameter;

/**
 * Representation of a tool.
 * 
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class ClientToolInfo implements IsInfo {

	private static final long serialVersionUID = 151L;

	@NotNull
	private String id;
	
	private String toolId;
	
	private String description;
	
	private String title;
	
	private String icon;

	private List<ClientToolInfo> tools = new ArrayList<ClientToolInfo>();
	
	private List<Parameter> parameters = new ArrayList<Parameter>();

	/**
	 * Get the list of tools this tool should list (in a drop-down panel for instance).
	 * 
	 * @return list of {@link ClientToolInfo}
	 * @since 1.10.0
	 */
	public List<ClientToolInfo> getTools() {
		return tools;
	}

	/**
	 * Set the list of tools this tool should list (in a drop-down panel for instance).
	 * 
	 * @param tools
	 * 			list of {@link ClientToolInfo}
	 * @since 1.10.0
	 */
	public void setTools(List<ClientToolInfo> tools) {
		this.tools = tools;
	}

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
	 * Get the unique id of this tool.
	 * 
	 * @return tool id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the unique id for the tool.
	 * 
	 * @param value
	 *            unique tool id
	 */
	public void setId(String value) {
		this.id = value;
	}

	/**
	 * Set the toolId used for fetching the {@link org.geomajas.gwt.client.action.ToolbarBaseAction} 
	 * from {@link org.geomajas.gwt.client.action.toolbar.ToolbarRegistry}.
	 * 
	 * @param toolId tool id
	 * @since 1.10.0
	 */
	public void setToolId(String toolId) {
		this.toolId = toolId;
	}
	
	/**
	 * <p>Get the toolId used for fetching the {@link org.geomajas.gwt.client.action.ToolbarBaseAction} 
	 * from {@link org.geomajas.gwt.client.action.toolbar.ToolbarRegistry}.</p>
	 * <p>If toolId == null, the id is returned.</p>
	 * @return tool id
	 * @since 1.10.0
	 */
	public String getToolId() {
		return toolId == null ? id : toolId;
	}

	/**
	 * Get the description of the tool.
	 * 
	 * @return the description
	 * @since 1.10.0
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the description of the tool.
	 * 
	 * @param description the description to set
	 * @since 1.10.0
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get the title of the tool.
	 * 
	 * @return the title
	 * @since 1.10.0
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set the title of the tool.
	 * 
	 * @param title the title to set
	 * @since 1.10.0
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Get the url of the icon. 
	 * 
	 * @return the icon
	 * @since 1.10.0
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * Set the url of the icon.
	 * 
	 * @param icon the icon to set
	 * @since 1.10.0
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

}
