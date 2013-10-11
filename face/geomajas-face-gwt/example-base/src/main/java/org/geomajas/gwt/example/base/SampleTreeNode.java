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

package org.geomajas.gwt.example.base;

import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * <p>
 * Definition of a single node in the tree that is shown in the left side of the sample overview.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class SampleTreeNode extends TreeNode {

	private SamplePanelFactory factory;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * When creating internal nodes, use this constructor. Note that no {@link SamplePanel} is required, since it's
	 * an internal panel. It will open up a folder with more samples...yey.
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
		this(title, icon, nodeId, parentNodeId, null);
	}

	/**
	 * When creating internal nodes, use this constructor. Note that no {@link SamplePanel} is required, since it's
	 * an internal panel. It will open up a folder with more samples...yey.
	 * 
	 * @param title
	 *            The tree node's title.
	 * @param icon
	 *            The tree node's icon.
	 * @param nodeId
	 *            This node's identifier.
	 * @param parentNodeId
	 *            The parent node's identifier. If this is a valid value, this node will be attached to that parent.
	 * @param factory
	 *            A factory for creating the correct SamplePanel instance.
	 */
	public SampleTreeNode(String title, String icon, String nodeId, String parentNodeId, SamplePanelFactory factory) {
		super();
		setName(title);
		setIcon(icon);
		setNodeId(nodeId);
		setParentID(parentNodeId);
		this.factory = factory;
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	public void setNodeId(String value) {
		setAttribute("nodeId", value);
	}

	public String getNodeId() {
		return getAttribute("nodeId");
	}

	public SamplePanelFactory getFactory() {
		return factory;
	}
}
