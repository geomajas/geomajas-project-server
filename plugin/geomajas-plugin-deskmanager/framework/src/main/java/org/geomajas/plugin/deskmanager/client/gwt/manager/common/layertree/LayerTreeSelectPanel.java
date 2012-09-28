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
package org.geomajas.plugin.deskmanager.client.gwt.manager.common.layertree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.plugin.deskmanager.client.gwt.common.DeskmanagerIcon;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.util.GeodeskDtoUtil;
import org.geomajas.plugin.deskmanager.domain.dto.BaseGeodeskDto;
import org.geomajas.plugin.deskmanager.domain.dto.LayerDto;
import org.geomajas.widget.layer.configuration.client.ClientAbstractNodeInfo;
import org.geomajas.widget.layer.configuration.client.ClientBranchNodeInfo;
import org.geomajas.widget.layer.configuration.client.ClientLayerNodeInfo;
import org.geomajas.widget.layer.configuration.client.ClientLayerTreeInfo;

import com.google.gwt.core.client.GWT;
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
public class LayerTreeSelectPanel extends HLayout {
	
	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);
	
	private LayerTreeGrid left;

	private LayerTreeGrid right;

	private Tree leftTree;

	private Tree rightTree;

	private boolean allowNonPublicLayers;

	private BaseGeodeskDto geodesk;

	protected Map<String, LayerDto> layers = new HashMap<String, LayerDto>();

	private ClientBranchNodeInfo sourceRootNode;

	private ClientBranchNodeInfo targetRootNode;

	public LayerTreeSelectPanel() {
		super(10);

		left = new LayerTreeGrid(MESSAGES.layerSelectAvailableLayers(), false);
		right = new LayerTreeGrid(MESSAGES.layerSelectSelectedLayers(), true);
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

		Img help = new Img(DeskmanagerIcon.HELP_ICON, 24, 24);
		help.setTooltip(MESSAGES.layerSelectPanelHelpText());
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
		leftTree = null;
		rightTree = null;
		left.setData((Tree) null);
		right.setData((Tree) null);
	}

	public void setValues(BaseGeodeskDto geodesk) {
		this.geodesk = geodesk;
		this.allowNonPublicLayers = geodesk.isPublic();

		layers.clear();
		for (LayerDto layer : geodesk.getMainMapLayers()) {
			layers.put(layer.getClientLayerIdReference(), layer);
		}

		// -- create trees
		buildTrees();

		// -- fill grids
		left.setData(leftTree);
		leftTree.openAll();

		right.setData(rightTree);
		rightTree.openAll();
	}

	private ClientAbstractNodeInfo fromTreeNode(Tree tree, LayerTreeNode treeNode) {
		ClientAbstractNodeInfo nodeInfo = treeNode.getNode();
		
		if (tree.hasChildren(treeNode)) {
			for (TreeNode node : tree.getChildren(treeNode)) {
				nodeInfo.getTreeNodes().add(fromTreeNode(tree, (LayerTreeNode) node));
			}
		}
		return nodeInfo;
	}
	
	public ClientLayerTreeInfo getValues() {
		// updating domainobjects from tree
		// -- remove old references (easier to just clear everything than trying to update)
		if (targetRootNode == null) {
			throw new RuntimeException("Value has not been set ??");
		}

		ClientAbstractNodeInfo rootNode = fromTreeNode(rightTree, (LayerTreeNode) rightTree.getRoot());
		
		ClientLayerTreeInfo treeInfo = new ClientLayerTreeInfo();
		treeInfo.setTreeNode(rootNode);
		
		return treeInfo;
		
	}

	// ----------------------------------------------------------

	private void buildTrees() {
		// create flat tree for the left side.
		sourceRootNode = new ClientBranchNodeInfo();
		sourceRootNode.setLabel("ROOT");

		for (LayerDto layer : geodesk.getMainMapLayers()) {
			ClientLayerNodeInfo layerNode = new ClientLayerNodeInfo();
			layerNode.setLayerId(layer.getClientLayerIdReference());
			sourceRootNode.getTreeNodes().add(layerNode);
		}

		ClientLayerTreeInfo clientLayerTreeInfo = (ClientLayerTreeInfo) GeodeskDtoUtil.getMainMapClientWidgetInfo(
				geodesk).get(ClientLayerTreeInfo.IDENTIFIER);
		targetRootNode = new ClientBranchNodeInfo();
		targetRootNode.setLabel("ROOT");
		targetRootNode.setExpanded(true);
		if (clientLayerTreeInfo != null) {
			// FIXME: dangerous cast, ClientLayerTreeInfo rootnode should always be branch!
			targetRootNode = (ClientBranchNodeInfo) clientLayerTreeInfo.getTreeNode();
		}

		leftTree = new Tree();
		leftTree.setIdField(LayerTreeNode.FLD_NAME);
		leftTree.setModelType(TreeModelType.CHILDREN);
		leftTree.setRoot(toTreeNode(sourceRootNode));

		rightTree = new Tree();
		rightTree.setModelType(TreeModelType.CHILDREN);
		rightTree.setRoot(toTreeNode(targetRootNode));
		rightTree.setSeparateFolders(true);
		rightTree.setSortFoldersBeforeLeaves(true);
		filterSourceTree();
	}

	private LayerTreeNode toTreeNode(ClientAbstractNodeInfo node) {
		LayerTreeNode tn = null;
		if (node instanceof ClientBranchNodeInfo) {
			tn = new LayerTreeNode((ClientBranchNodeInfo) node);
			List<LayerTreeNode> children = new ArrayList<LayerTreeNode>();
			for (ClientAbstractNodeInfo ltn : node.getTreeNodes()) {
				if (ltn != null) {
					children.add(toTreeNode(ltn));
				}
			}
			tn.setChildren(children.toArray(new LayerTreeNode[children.size()]));
			tn.setAttribute(LayerTreeNode.FLD_PUBLIC, true); // no such thing as non-public folders
		} else if (node instanceof ClientLayerNodeInfo) {
			LayerDto layerDto = layers.get(((ClientLayerNodeInfo) node).getLayerId());
			tn = new LayerTreeNode(node, layerDto);
			tn.setAttribute(LayerTreeNode.FLD_PUBLIC, layerDto.getLayerModel().isPublic());
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

				if (leftTree.isLeaf(node) && !((LayerTreeNode) node).getLayer().getLayerModel().isPublic()) {
					leftTree.remove(node);
				}
			}
		}
	}
}
