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
package org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.common;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;

import com.google.gwt.user.client.Timer;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * @author Kristof Heirwegh
 */
public class GroupTreeGrid extends TreeGrid {

	private static final String ROOTNODE_ID = "__ROOT__";

	private static final String GROUP_ID = "groupId";

	private static final String PARENT_ID = "parentId";

	private static final String NAME = "name";

	private static final String GROUP = "group";

	private Tree treeData;

	private GroupTreeNode rootNode;

	private List<TerritoryDto> allGroups;

	public GroupTreeGrid() {
		super();
		setWidth100();
		setHeight100();
		setShowRoot(false);
		setCanSort(false); // this messes things up
		setCanDrag(false);
		setCanReorderRecords(false);
		setCanAcceptDroppedRecords(false);
		setCanDragRecordsOut(false);
		setShowAllRecords(true);
		setSelectionAppearance(SelectionAppearance.CHECKBOX);
		setShowPartialSelection(true);
		setCascadeSelection(true);
		setShowHeader(false);

		rootNode = new GroupTreeNode(ROOTNODE_ID, "", "root", true);
		treeData = new Tree();
		treeData.setModelType(TreeModelType.PARENT);
		treeData.setNameProperty(NAME);
		treeData.setIdField(GROUP_ID);
		treeData.setOpenProperty("isOpen");
		treeData.setParentIdField("parentId");
		treeData.setRoot(rootNode);
		setData(treeData);
	}

	public void setGroups(List<TerritoryDto> groups) {
		this.allGroups = groups;
	}

	public void setValues(List<TerritoryDto> groups) {
		buildTree();
		if (groups != null) {
			List<TreeNode> selected = new ArrayList<TreeNode>();
			for (TerritoryDto gd : groups) {
				selected.add(treeData.findById("" + gd.getId()));
			}
			if (selected.size() > 0) {
				selectRecords(selected.toArray(new TreeNode[selected.size()]), true);
			}
		}
	}

	public List<TerritoryDto> getValues() {
		List<TerritoryDto> vals = new ArrayList<TerritoryDto>();
		for (ListGridRecord lgr : getSelectedRecords(true)) {
			TerritoryDto g = ((GroupTreeNode) lgr).getGroup();
			if (g != null) {
				vals.add(g);
			}
		}
		return vals;
	}

	// -------------------------------------------------

	private void buildTree() {
		// this happens when command is still loading
		if (allGroups == null) {
			new Timer() {

				public void run() {
					buildTree();
				}
			} .schedule(2000); // don't hammer
			return;
		}

		// clear
		treeData.removeList(treeData.getAllNodes(rootNode));

		// build the tree -- Expects data to be ordered correctly!

		GroupTreeNode currentNode = null;
		for (TerritoryDto g : allGroups) {
			if (currentNode == null || !currentNode.getGroupId().equals(g.getCategory().getId())) {
				currentNode = new GroupTreeNode(g.getCategory().getId(), rootNode.getGroupId(), g.getCategory()
						.getDescription(), false);
				treeData.add(currentNode, rootNode);
			}
			treeData.add(new GroupTreeNode(g, currentNode.getParentId()), currentNode);
		}

	}

	// -------------------------------------------------

	/**
	 * TODO.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public static class GroupTreeNode extends TreeNode {

		public GroupTreeNode(TerritoryDto group, String parentId) {
			this("" + group.getId(), parentId, group.getName(), false);
			setAttribute(GROUP, group);
		}

		public GroupTreeNode(String groupId, String parentId, String name, boolean isOpen) {
			super();
			setAttribute(GROUP_ID, groupId);
			setAttribute(PARENT_ID, parentId);
			setAttribute(NAME, name);
			setAttribute("isOpen", isOpen);
		}

		// -------------------------------------------------
		public TerritoryDto getGroup() {
			return (TerritoryDto) getAttributeAsObject(GROUP);
		}

		public String getGroupId() {
			return getAttribute(GROUP_ID);
		}

		public String getParentId() {
			return getAttribute(PARENT_ID);
		}
	}
}
