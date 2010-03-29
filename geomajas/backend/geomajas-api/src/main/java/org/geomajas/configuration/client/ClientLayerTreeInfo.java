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
package org.geomajas.configuration.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * Configuration of a layer tree.
 * 
 * @author Joachim Van der Auwera
 */
public class ClientLayerTreeInfo implements Serializable {

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
	 * @param id id
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
	 * @param treeNode root node
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
	 * @param tools list of tools
	 */
	public void setTools(List<ClientToolInfo> tools) {
		this.tools = tools;
	}
}
