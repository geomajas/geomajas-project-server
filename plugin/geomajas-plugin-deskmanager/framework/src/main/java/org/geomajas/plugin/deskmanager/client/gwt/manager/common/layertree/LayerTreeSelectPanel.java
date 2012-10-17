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
 * @author Oliver May
 */
public class LayerTreeSelectPanel extends HLayout {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	private LayerTreeGrid sourceGrid;

	private LayerTreeGrid targetGrid;

	private Tree sourceTree;

	private Tree targetTree;

	private boolean allowNonPublicLayers;

	private BaseGeodeskDto geodesk;

	protected Map<String, LayerDto> layers = new HashMap<String, LayerDto>();

	private ClientBranchNodeInfo sourceRootNode;

	private ClientBranchNodeInfo targetRootNode;

	public LayerTreeSelectPanel() {
		super(10);

		sourceGrid = new LayerTreeGrid(MESSAGES.layerSelectAvailableLayers(), false);
		targetGrid = new LayerTreeGrid(MESSAGES.layerSelectSelectedLayers(), true);

		TransferImgButton add = new TransferImgButton(TransferImgButton.RIGHT);
		add.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				targetGrid.transferSelectedData(sourceGrid);
			}
		});

		TransferImgButton remove = new TransferImgButton(TransferImgButton.LEFT);
		remove.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				sourceGrid.transferSelectedData(targetGrid);
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

		addMember(sourceGrid);
		addMember(buttons);
		addMember(targetGrid);
	}

	public void clearValues() {
		sourceTree = null;
		targetTree = null;
		sourceGrid.setData((Tree) null);
		targetGrid.setData((Tree) null);
	}

	public void setValues(BaseGeodeskDto geodesk) {
		this.geodesk = geodesk;
		this.allowNonPublicLayers = geodesk.isPublic();

		layers.clear();
		for (LayerDto layer : GeodeskDtoUtil.getMainMapLayers(geodesk)) {
			layers.put(layer.getClientLayerIdReference(), layer);
		}

		// -- create trees
		buildTrees();

		// -- fill grids
		sourceGrid.setData(sourceTree);
		sourceTree.openAll();

		targetGrid.setData(targetTree);
		targetTree.openAll();
	}

	/**
	 * Recursively convert from smartgwt LayerTree into WidgetInfo layertree.
	 * 
	 * @param treeNode
	 *            the treeNode to convert.
	 * @return the converted treeNode.
	 */
	private ClientAbstractNodeInfo fromTreeNode(LayerTreeNode treeNode) {
		if (treeNode.getNode() instanceof ClientBranchNodeInfo) {
			// Create folder
			ClientBranchNodeInfo branch = new ClientBranchNodeInfo();
			branch.setLabel(((ClientBranchNodeInfo) treeNode.getNode()).getLabel());
			branch.setExpanded(true);

			// Add children
			for (TreeNode node : targetTree.getChildren(treeNode)) {
				branch.getTreeNodes().add(fromTreeNode((LayerTreeNode) node));
			}
			return branch;
		} else if (treeNode.getNode() instanceof ClientLayerNodeInfo) {
			// Is leaf
			ClientLayerNodeInfo layer = new ClientLayerNodeInfo();
			layer.setLayerId(((ClientLayerNodeInfo) treeNode.getNode()).getLayerId());
			return layer;
		} else {
			throw new RuntimeException("Wrong type of treeNode in tree!");
		}
	}

	public ClientLayerTreeInfo getValues() {
		if (targetRootNode == null) {
			throw new RuntimeException("Value has not been set ??");
		}

		targetRootNode.getTreeNodes().clear();
		ClientAbstractNodeInfo rootNode = fromTreeNode((LayerTreeNode) targetTree.getRoot());

		ClientLayerTreeInfo treeInfo = new ClientLayerTreeInfo();
		treeInfo.setTreeNode(rootNode);

		return treeInfo;
	}

	// ----------------------------------------------------------

	private void buildTrees() {
		// create flat tree for the sourceGrid side.
		sourceRootNode = new ClientBranchNodeInfo();
		sourceRootNode.setLabel("ROOT");

		for (LayerDto layer : layers.values()) {
			ClientLayerNodeInfo layerNode = new ClientLayerNodeInfo();
			layerNode.setLayerId(layer.getClientLayerIdReference());
			sourceRootNode.getTreeNodes().add(layerNode);
		}

		ClientLayerTreeInfo clientLayerTreeInfo = (ClientLayerTreeInfo) GeodeskDtoUtil.getMainMapClientWidgetInfo(
				geodesk).get(ClientLayerTreeInfo.IDENTIFIER);
		if (clientLayerTreeInfo != null) {
			// FIXME: dangerous cast, ClientLayerTreeInfo rootnode should always be branch!
			targetRootNode = (ClientBranchNodeInfo) clientLayerTreeInfo.getTreeNode();
		} else {
			targetRootNode = new ClientBranchNodeInfo();
			targetRootNode.setLabel("ROOT");
			targetRootNode.setExpanded(true);
		}

		sourceTree = new Tree();
		sourceTree.setIdField(LayerTreeNode.FLD_NAME);
		sourceTree.setModelType(TreeModelType.CHILDREN);
		sourceTree.setRoot(toTreeNode(sourceRootNode));

		targetTree = new Tree();
		targetTree.setIdField(LayerTreeNode.FLD_NAME);
		targetTree.setModelType(TreeModelType.CHILDREN);
		targetTree.setRoot(toTreeNode(targetRootNode));
		targetTree.setSortFoldersBeforeLeaves(false);
		filterSourceTree();
	}

	private LayerTreeNode toTreeNode(ClientAbstractNodeInfo node) {
		LayerTreeNode tn = null;
		if (node instanceof ClientBranchNodeInfo) {
			tn = new LayerTreeNode((ClientBranchNodeInfo) node);
			List<LayerTreeNode> children = new ArrayList<LayerTreeNode>();
			for (ClientAbstractNodeInfo ltn : node.getTreeNodes()) {
				if (ltn != null) {
					LayerTreeNode ltnn = toTreeNode(ltn);
					if (ltnn != null) {
						children.add(ltnn);
					}
				}
			}
			tn.setChildren(children.toArray(new LayerTreeNode[children.size()]));
			tn.setAttribute(LayerTreeNode.FLD_PUBLIC, true); // no such thing as non-public folders
		} else if (node instanceof ClientLayerNodeInfo) {
			LayerDto layerDto = layers.get(((ClientLayerNodeInfo) node).getLayerId());
			//Don't add if layerModel is null (layer is orphin)!
			if (layerDto == null || layerDto.getLayerModel() == null) {
				return null;
			}
			tn = new LayerTreeNode(node, layerDto);
			tn.setAttribute(LayerTreeNode.FLD_PUBLIC, layerDto.getLayerModel().isPublic());
		}
		return tn;
	}

	private void filterSourceTree() {
		for (TreeNode node : targetTree.getAllNodes()) {
			TreeNode lefty = sourceTree.findById(node.getName());
			if (lefty != null) {
				if (!sourceTree.isLeaf(lefty)) {
					sourceTree.addList(sourceTree.getChildren(lefty), sourceTree.getRoot());
				}
				sourceTree.remove(lefty);
			}
		}
		if (!allowNonPublicLayers) {
			for (TreeNode node : sourceTree.getAllNodes()) {

				if (sourceTree.isLeaf(node) && !((LayerTreeNode) node).getLayer().getLayerModel().isPublic()) {
					sourceTree.remove(node);
				}
			}
		}
	}
}
