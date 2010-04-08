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

import org.geomajas.global.Api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * Representation of a node in the layer tree widget.
 * 
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 */
@Api(allMethods = true)
public class ClientLayerTreeNodeInfo implements Serializable {

	private static final long serialVersionUID = 151L;

	@NotNull
	private String label;

	private List<ClientLayerTreeNodeInfo> treeNodes = new ArrayList<ClientLayerTreeNodeInfo>();

	private List<ClientLayerInfo> layers = new ArrayList<ClientLayerInfo>();

	@NotNull
	private boolean expanded;

	/**
	 * Create client layer tree node info instance. 
	 */
	public ClientLayerTreeNodeInfo() {
	}

	/**
	 * Get the tree node label.
	 * 
	 * @return label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Set the tree node label.
	 * 
	 * @param label
	 *            label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Get the list of sub-nodes of this node.
	 * 
	 * @return the list of sub-nodes
	 */
	public List<ClientLayerTreeNodeInfo> getTreeNodes() {
		return treeNodes;
	}

	/**
	 * Set the list of sub-nodes of this node.
	 * 
	 * @param treeNodes
	 *            list of sub-nodes
	 */
	public void setTreeNodes(List<ClientLayerTreeNodeInfo> treeNodes) {
		this.treeNodes = treeNodes;
	}

	/**
	 * Get the list of layers that are part this node.
	 * 
	 * @return list of layers
	 */
	public List<ClientLayerInfo> getLayers() {
		return layers;
	}

	/**
	 * Set the list of layers that are part this node.
	 * 
	 * @param layers
	 *            list of layers
	 */
	public void setLayers(List<ClientLayerInfo> layers) {
		this.layers = layers;
	}

	/**
	 * Is the node expanded?
	 * 
	 * @return true when node is expanded
	 */
	public boolean isExpanded() {
		return expanded;
	}

	/**
	 * Set the "expanded" state for the node.
	 * 
	 * @param expanded
	 *            true when node is expanded
	 */
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
}
