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
package org.geomajas.widget.layer.configuration.client;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.IsInfo;

/**
 * Representation of a node in the layer tree widget.
 * 
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 * @author Oliver May
 * @since 1.0.0
 */
@Api(allMethods = true)
public abstract class ClientAbstractNodeInfo implements IsInfo {

	private static final long serialVersionUID = 100L;

	private List<ClientAbstractNodeInfo> treeNodes = new ArrayList<ClientAbstractNodeInfo>();

	/**
	 * Create client layer tree node info instance.
	 */
	public ClientAbstractNodeInfo() {
	}

	/**
	 * Get the list of sub-nodes of this node.
	 * 
	 * @return the list of sub-nodes
	 */
	public List<ClientAbstractNodeInfo> getTreeNodes() {
		return treeNodes;
	}

	/**
	 * Set the list of sub-nodes of this node.
	 * 
	 * @param treeNodes
	 *            list of sub-nodes
	 */
	public void setTreeNodes(List<ClientAbstractNodeInfo> treeNodes) {
		this.treeNodes = treeNodes;
	}

}
