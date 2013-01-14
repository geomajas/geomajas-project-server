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
package org.geomajas.configuration.client;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.IsInfo;

/**
 * Representation of a node in the layer tree widget.
 * 
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class ClientLayerTreeNodeInfo implements IsInfo {

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
