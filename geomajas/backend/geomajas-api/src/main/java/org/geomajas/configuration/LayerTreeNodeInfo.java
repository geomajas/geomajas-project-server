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
package org.geomajas.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a node in the layer tree widget.
 *
 * @author Pieter De Graef
 * @author Joachim Van der Auwera  
 */
public class LayerTreeNodeInfo implements Serializable {

	private static final long serialVersionUID = 151L;

	private String label;

	private List<LayerTreeNodeInfo> treeNodes;

	private List<String> layerIds;

	private String expanded;

	public LayerTreeNodeInfo() {
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<LayerTreeNodeInfo> getTreeNodes() {
		if (null == treeNodes) {
			treeNodes = new ArrayList<LayerTreeNodeInfo>();
		}
		return treeNodes;
	}

	public void setTreeNodes(List<LayerTreeNodeInfo> treeNodes) {
		this.treeNodes = treeNodes;
	}

	/**
	 * Get list of layers (by id) which should be in the layertree.
	 *
	 * @return list of layer ids
	 */
	public List<String> getLayerIds() {
		if (null == layerIds) {
			layerIds = new ArrayList<String>();
		}
		return layerIds;
	}

	/**
	 * Set list of layers (by id) which should be displayed in the layertree.
	 *
	 * @param layerIds list of layer ids
	 */
	public void setLayerIds(List<String> layerIds) {
		this.layerIds = layerIds;
	}

	public String getExpanded() {
		return expanded;
	}

	public void setExpanded(String expanded) {
		this.expanded = expanded;
	}
}
