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

package org.geomajas.gwt.client.samples.base;

import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * <p>
 * Definition of a single node in the tree that is shown in the left side of the sample overview.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class SampleTreeNode extends TreeNode {

	private SamplePanel samplePanel;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * When creating leaf nodes, use this constructor.
	 * 
	 * @param samplePanel
	 *            The actual sample.
	 * @param nodeId
	 *            This node's identifier.
	 * @param parentNodeId
	 *            The parent node's identifier. If this is a valid value, this node will be attached to that parent.
	 */
	public SampleTreeNode(SamplePanel samplePanel, String nodeId, String parentNodeId) {
		super(samplePanel.title);
		setIcon(samplePanel.icon);
		this.samplePanel = samplePanel;
		setNodeId(nodeId);
		setParentID(parentNodeId);
	}

	/**
	 * When creating internal nodes, use this constructor. Note that no <code>SamplePanel</code> is required, since it's
	 * an internal panel. It will simply open up a folder with more samples...yey.
	 * 
	 * @param title
	 *            The tree node's title.
	 * @param icon
	 *            The tree node's icon.
	 * @param nodeId
	 *            This node's identifier.
	 * @param parentNodeId
	 *            The parent node's identifier. If this is a valid value, this node will be attached to that parent.
	 */
	public SampleTreeNode(String title, String icon, String nodeId, String parentNodeId) {
		super();
		setName(title);
		setIcon(icon);
		setNodeId(nodeId);
		setParentID(parentNodeId);
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	public SamplePanel getSamplePanel() {
		return samplePanel;
	}

	public void setNodeId(String value) {
		setAttribute("nodeId", value);
	}

	public String getNodeId() {
		return getAttribute("nodeId");
	}
}
