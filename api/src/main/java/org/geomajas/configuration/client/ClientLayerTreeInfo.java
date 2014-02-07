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
package org.geomajas.configuration.client;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.geomajas.annotation.Api;

/**
 * Configuration of a layer tree.
 * <p>
 * Since version 1.10.0 this configuration object implements the <code>ClientWidgetInfo</code> interface, so that it can
 * be easily retrieved like any other widget configuration object.
 * </p>
 * 
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class ClientLayerTreeInfo implements ClientWidgetInfo {

	private static final long serialVersionUID = 151L;

	@NotNull
	private String id;

	@NotNull
	private ClientLayerTreeNodeInfo treeNode;

	private List<ClientToolInfo> tools = new ArrayList<ClientToolInfo>();

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
	 * Get the root node this layer tree.
	 * 
	 * @return the root node
	 */
	public ClientLayerTreeNodeInfo getTreeNode() {
		return treeNode;
	}

	/**
	 * Set the root node.
	 * 
	 * @param treeNode
	 *            root node
	 */
	public void setTreeNode(ClientLayerTreeNodeInfo treeNode) {
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
}