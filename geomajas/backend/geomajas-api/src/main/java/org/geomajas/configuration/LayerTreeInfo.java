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
 * Configuration of a layer tree.

 * @author Joachim Van der Auwera
 */
public class LayerTreeInfo implements Serializable {

	private static final long serialVersionUID = 151L;
	private String id;
	private LayerTreeNodeInfo treeNode;
	private List<ToolInfo> tools;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LayerTreeNodeInfo getTreeNode() {
		return treeNode;
	}

	public void setTreeNode(LayerTreeNodeInfo treeNode) {
		this.treeNode = treeNode;
	}

	public List<ToolInfo> getTools() {
		if (null == tools) {
			tools = new ArrayList<ToolInfo>();
		}
		return tools;
	}

	public void setTools(List<ToolInfo> tools) {
		this.tools = tools;
	}
}
