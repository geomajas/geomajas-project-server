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
package org.geomajas.plugin.deskmanager.client.gwt.manager.common;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.plugin.deskmanager.domain.dto.LayerTreeNodeDto;

import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.TransferImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * Contains two layertreepanels which are used to make a selection (target) of some layers (source).
 * 
 * @author Kristof Heirwegh
 */
public class LayerSelectPanel extends HLayout {

	private static final String HELP_TEXT = "<b>Beide lijsten:</b><br />"
			+ "- Gebruik \"Drag &amp; Drop\" om items toe te voegen of te verwijderen, "
			+ "of selecteer een item en gebruik een van de pijltjes om het item toe te voegen of te verwijderen.<br />"
			+ "<b>Lijst Geselecteerde lagen:</b><br />" + "- Versleep items om de volgorde te wijzigen.<br />"
			+ "- Gebruik het contextmenu om mappen toe te voegen of te verwijderen.<br />"
			+ "- Gebruik de \"Roll-over\"-actie (knopje rechts van een item) om het item te wijzigen.";

	private static final String HELP_ICON = "osgeo/help.png";

	private LayerTreeNodeDto source;

	private LayerTreeNodeDto target;

	private LayerTreeGrid left;

	private LayerTreeGrid right;

	private Tree leftTree;

	private Tree rightTree;

	private boolean allowNonPublicLayers;

	private boolean allowFolders;

	public LayerSelectPanel() {
		super(10);

		left = new LayerTreeGrid("Beschikbare lagen", false);
		right = new LayerTreeGrid("Geselecteerde lagen", true);
		right.setSourceTreeGrid(left);

		TransferImgButton add = new TransferImgButton(TransferImgButton.RIGHT);
		add.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				right.transferSelectedData(left);
			}
		});

		TransferImgButton remove = new TransferImgButton(TransferImgButton.LEFT);
		remove.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				left.transferSelectedData(right);
			}
		});

		Img help = new Img(HELP_ICON, 24, 24);
		help.setTooltip(HELP_TEXT);
		help.setHoverWidth(350);
		help.setShowDisabled(false);
		help.setShowDown(false);

		VLayout buttons = new VLayout(10);
		buttons.addMember(add);
		buttons.addMember(remove);
		buttons.addMember(new LayoutSpacer());
		buttons.addMember(help);

		addMember(left);
		addMember(buttons);
		addMember(right);
	}

	public void clearValues() {
		source = null;
		target = null;
		leftTree = null;
		rightTree = null;
		left.setData((Tree) null);
		right.setData((Tree) null);
	}

	public void setValues(LayerTreeNodeDto source, LayerTreeNodeDto target, boolean allowNonPublicLayers,
			boolean allowFolders) {
		if (target == null) {
			throw new IllegalArgumentException("Target is niet gezet ??");
		}

		this.source = source.clone();
		this.target = target;
		this.allowNonPublicLayers = allowNonPublicLayers;
		this.allowFolders = allowFolders;

		// -- create trees
		buildTrees();

		// -- fill grids
		left.setData(leftTree);
		leftTree.openAll();

		right.setData(rightTree);
		right.setAllowFolders(allowFolders);
		rightTree.openAll();
	}

	public LayerTreeNodeDto getValues() {
		// updating domainobjects from tree
		// -- remove old references (easier to just clear everything than trying to update)
		if (target == null) {
			throw new RuntimeException("Waarde is niet gezet ??");
		}

		target.getChildren().clear();
		for (TreeNode node : rightTree.getAllNodes()) {
			LayerTreeNode ltn = (LayerTreeNode) node;
			if (!ltn.getNode().isLeaf()) {
				ltn.getNode().getChildren().clear();
			}
		}

		// -- wire nodes together
		for (TreeNode node : rightTree.getAllNodes()) {
			LayerTreeNodeDto dnode = ((LayerTreeNode) node).getNode();
			LayerTreeNode parent = (LayerTreeNode) rightTree.getParent(node);
			if (parent != null) {
				dnode.setParentNode(parent.getNode());
				parent.getNode().getChildren().add(dnode);
			} else {
				dnode.setParentNode(null);
			}
		}

		return ((LayerTreeNode) rightTree.getRoot()).getNode();
	}

	// ----------------------------------------------------------

	/**
	 * TODO.
	 * 
	 * @author Jan De Moerloose
	 *
	 */
	public static class LayerTreeNode extends TreeNode {

		public static final String FLD_NAME = "name";

		public static final String FLD_PUBLIC = "public";

		private final LayerTreeNodeDto node;

		public LayerTreeNode(LayerTreeNodeDto node) {
			if (node == null) {
				throw new IllegalArgumentException("Was expecting a node!");
			}
			this.node = node;
			setName(node.getName());
			setIsFolder(!node.isLeaf());
		}

		public LayerTreeNodeDto getNode() {
			return node;
		}
	}

	private void buildTrees() {
		leftTree = new Tree();
		leftTree.setIdField(LayerTreeNode.FLD_NAME);
		leftTree.setModelType(TreeModelType.CHILDREN);
		leftTree.setRoot(toTreeNode(source));
		leftTree.setSeparateFolders(true);
		leftTree.setSortFoldersBeforeLeaves(true);
		rightTree = new Tree();
		rightTree.setModelType(TreeModelType.CHILDREN);
		rightTree.setRoot(toTreeNode(target));
		rightTree.setSeparateFolders(true);
		rightTree.setSortFoldersBeforeLeaves(true);
		filterSourceTree();
	}

	private LayerTreeNode toTreeNode(LayerTreeNodeDto node) {
		LayerTreeNode tn = new LayerTreeNode(node);
		if (!node.isLeaf()) {
			List<LayerTreeNode> children = new ArrayList<LayerSelectPanel.LayerTreeNode>();
			for (LayerTreeNodeDto ltn : node.getChildren()) {
				if (ltn != null) {
					children.add(toTreeNode(ltn));
				}
			}
			tn.setChildren(children.toArray(new LayerTreeNode[children.size()]));
			tn.setAttribute(LayerTreeNode.FLD_PUBLIC, true); // no such thing as non-public folders
		} else {
			tn.setAttribute(LayerTreeNode.FLD_PUBLIC, node.isPublicLayer());
		}
		return tn;
	}

	private void filterSourceTree() {
		for (TreeNode node : rightTree.getAllNodes()) {
			TreeNode lefty = leftTree.findById(node.getName());
			if (lefty != null) {
				if (!leftTree.isLeaf(lefty)) {
					leftTree.addList(leftTree.getChildren(lefty), leftTree.getRoot());
				}
				leftTree.remove(lefty);
			}
		}
		if (!allowNonPublicLayers) {
			for (TreeNode node : leftTree.getAllNodes()) {
				if (leftTree.isLeaf(node) && !((LayerTreeNode) node).getNode().isPublicLayer()) {
					leftTree.remove(node);
				}
			}
		}
		if (!allowFolders) {
			for (TreeNode node : leftTree.getAllNodes()) {
				if (!leftTree.isLeaf(node) && !node.equals(leftTree.getRoot())) {
					leftTree.addList(leftTree.getChildren(node), leftTree.getRoot());
					leftTree.remove(node);
				}
			}
		}
	}
}
