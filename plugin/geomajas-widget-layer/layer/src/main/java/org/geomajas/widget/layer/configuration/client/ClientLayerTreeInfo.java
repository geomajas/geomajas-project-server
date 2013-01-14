/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.widget.layer.configuration.client;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.client.ClientToolInfo;
import org.geomajas.configuration.client.ClientWidgetInfo;

/**
 * Configuration of a layer tree.
 * <p>
 * This configuration object implements the <code>ClientWidgetInfo</code> interface, so that it can be easily retrieved
 * like any other widget configuration object.
 * </p>
 * 
 * @author Joachim Van der Auwera
 * @author Kristof Heirwegh
 * @author Oliver May
 * @since 1.0.0
 */
@Api(allMethods = true)
public class ClientLayerTreeInfo implements ClientWidgetInfo {

	private static final long serialVersionUID = 100L;

	/**
	 * Use this identifier in your configuration files (beans).
	 */
	public static final String IDENTIFIER = "Glt.LayerTreeWithLegendInfo";

	@NotNull
	private String id;

	@NotNull
	private ClientAbstractNodeInfo treeNode;

	private List<ClientToolInfo> tools = new ArrayList<ClientToolInfo>();

	private int iconSize = 18;
	
	/**
	 * Get the unique id of this layer tree.
	 * 
	 * @return the unique id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the unique id of this layer tree (auto-copied from Spring context).
	 * 
	 * @param id
	 *            id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get the root node this layer tree. The root is just a container for the client nodes, and should never be 
	 * displayed.
	 * 
	 * @return the root node
	 */
	public ClientAbstractNodeInfo getTreeNode() {
		return treeNode;
	}

	/**
	 * Set the root node. The root is just a container for the client nodes, and should never be displayed.
	 * 
	 * @param treeNode
	 *            root node
	 */
	public void setTreeNode(ClientAbstractNodeInfo treeNode) {
		this.treeNode = treeNode;
	}

	/**
	 * Get the list of tools for this layer tree.
	 * 
	 * @return the root node
	 */
	public List<ClientToolInfo> getTools() {
		return tools;
	}

	/**
	 * Set the list of tools for this layer tree.
	 * 
	 * @param tools
	 *            list of tools
	 */
	public void setTools(List<ClientToolInfo> tools) {
		this.tools = tools;
	}
	
	/**
	 * The size of the legend icons in pixels, this is always square (so only
	 * one parameter).
	 * <p>
	 * The default value is 18
	 * </p>
	 * @return the icon size.
	 */
	public int getIconSize() {
		return iconSize;
	}

	/**
	 * The size of the legend icons in pixels, this is always square (so only
	 * one parameter).
	 * <p>
	 * The default value is 18
	 * </p>
	 * @param iconSize the icon size.
	 */
	public void setIconSize(int iconSize) {
		this.iconSize = iconSize;
	}
	
}