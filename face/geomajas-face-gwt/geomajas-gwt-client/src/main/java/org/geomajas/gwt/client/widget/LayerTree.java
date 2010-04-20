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

package org.geomajas.gwt.client.widget;

import java.util.List;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientLayerTreeInfo;
import org.geomajas.configuration.client.ClientLayerTreeNodeInfo;
import org.geomajas.configuration.client.ClientToolInfo;
import org.geomajas.global.Api;
import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.gwt.client.action.layertree.LayerTreeAction;
import org.geomajas.gwt.client.action.layertree.LayerTreeModalAction;
import org.geomajas.gwt.client.action.layertree.LayerTreeRegistry;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.event.LayerDeselectedEvent;
import org.geomajas.gwt.client.map.event.LayerSelectedEvent;
import org.geomajas.gwt.client.map.event.LayerSelectionHandler;
import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.gwt.client.map.layer.Layer;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderClickEvent;
import com.smartgwt.client.widgets.tree.events.FolderClickHandler;
import com.smartgwt.client.widgets.tree.events.LeafClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafClickHandler;

/**
 * The LayerTree shows a tree resembling the available layers for the map Several actions can be executed on the layers
 * (make them invisible, ...).
 * 
 * @author Frank Wynants
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api
public class LayerTree extends Canvas implements LeafClickHandler, FolderClickHandler, LayerSelectionHandler {

	protected static final int LAYERTREEBUTTON_SIZE = 24;

	private final HTMLFlow htmlSelectedLayer = new HTMLFlow(I18nProvider.getLayerTree().activeLayer(
			I18nProvider.getLayerTree().none()));

	private ToolStrip toolStrip;

	private LayerTreeTreeNode selectedLayerTreeNode;

	private final TreeGrid treeGrid = new TreeGrid();

	private RefreshableTree tree;

	private MapModel mapModel;

	private boolean initialized;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	/**
	 * Initialize the LayerTree, using a MapWidget as base reference. It will display the map's layers, as configured in
	 * the XML configuration, and select/deselect the layer as the user clicks on them in the tree.
	 */
	public LayerTree(final MapWidget mapWidget) {
		super();
		setHeight100();
		this.mapModel = mapWidget.getMapModel();
		htmlSelectedLayer.setWidth100();

		// Wait for the MapModel to be loaded
		mapModel.addMapModelHandler(new MapModelHandler() {

			public void onMapModelChange(MapModelEvent event) {
				if (!initialized) {
					buildTree(mapModel);
					toolStrip = buildToolstrip(mapWidget);

					// display the toolbar and the tree
					VLayout vLayout = new VLayout();
					vLayout.setSize("100%", "100%");
					vLayout.addMember(toolStrip);
					htmlSelectedLayer.setBackgroundColor("#cccccc");
					htmlSelectedLayer.setAlign(Alignment.CENTER);
					vLayout.addMember(htmlSelectedLayer);
					vLayout.addMember(treeGrid);
					treeGrid.redraw();
					LayerTree.this.addChild(vLayout);
					LayerTree.this.redraw();
				}
				initialized = true;
			}
		});
		mapModel.addLayerSelectionHandler(this);
	}

	// -------------------------------------------------------------------------
	// LayerSelectionHandler implementation:
	// -------------------------------------------------------------------------

	/**
	 * When a layer deselection event comes in, the LayerTree must also deselect the correct node in the tree, update
	 * the selected layer text, and update all buttons icons.
	 */
	public void onDeselectLayer(LayerDeselectedEvent event) {
		ListGridRecord selected = treeGrid.getSelectedRecord();
		if (selected != null) {
			treeGrid.deselectRecord(selected);
		}
		selectedLayerTreeNode = null;
		htmlSelectedLayer.setContents(I18nProvider.getLayerTree().activeLayer(I18nProvider.getLayerTree().none()));

		Canvas[] toolStripMembers = toolStrip.getMembers();
		updateButtonIconsAndStates(toolStripMembers);
	}

	/**
	 * When a layer selection event comes in, the LayerTree must also select the correct node in the tree, update the
	 * selected layer text, and update all buttons icons.
	 */
	public void onSelectLayer(LayerSelectedEvent event) {
		for (TreeNode node : tree.getAllNodes()) {
			if (node.getName().equals(event.getLayer().getLabel())) {
				selectedLayerTreeNode = (LayerTreeTreeNode) node;
				treeGrid.selectRecord(selectedLayerTreeNode);
				htmlSelectedLayer.setContents(I18nProvider.getLayerTree().activeLayer(
						selectedLayerTreeNode.getLayer().getLabel()));

				Canvas[] toolStripMembers = toolStrip.getMembers();
				updateButtonIconsAndStates(toolStripMembers);
			}
		}
	}

	// -------------------------------------------------------------------------
	// LeafClickHandler, FolderClickHandler
	// -------------------------------------------------------------------------

	/**
	 * When the user clicks on a folder nothing gets selected.
	 */
	public void onFolderClick(FolderClickEvent event) {
		mapModel.selectLayer(null);
	}

	/**
	 * When the user clicks on a leaf the headertext of the treetable is changed to the selected leaf and the toolbar
	 * buttons are updated to represent the correct state of the buttons.
	 */
	public void onLeafClick(LeafClickEvent event) {
		LayerTreeTreeNode layerTreeNode = (LayerTreeTreeNode) event.getLeaf();
		if (null != selectedLayerTreeNode
				&& layerTreeNode.getLayer().getId().equals(selectedLayerTreeNode.getLayer().getId())) {
			mapModel.selectLayer(null);
		} else {
			mapModel.selectLayer(layerTreeNode.getLayer());
		}
	}

	// -------------------------------------------------------------------------
	// Getters:
	// -------------------------------------------------------------------------

	/**
	 * Get the currently selected tree node.
	 * 
	 * @return selected node
	 */
	public LayerTreeTreeNode getSelectedLayerTreeNode() {
		return selectedLayerTreeNode;
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	/**
	 * Builds the toolbar
	 * 
	 * @param mapWidget
	 *            The mapWidget containing the layerTree
	 * @return {@link com.smartgwt.client.widgets.toolbar.ToolStrip} which was built
	 */
	private ToolStrip buildToolstrip(MapWidget mapWidget) {
		toolStrip = new ToolStrip();
		toolStrip.setWidth100();
		toolStrip.setPadding(3);

		ClientLayerTreeInfo layerTreeInfo = mapModel.getMapInfo().getLayerTree();
		if (layerTreeInfo != null) {
			for (ClientToolInfo tool : layerTreeInfo.getTools()) {
				String id = tool.getId();
				IButton button = null;
				ToolbarBaseAction action = LayerTreeRegistry.getToolbarAction(id, mapWidget);
				if (action instanceof LayerTreeAction) {
					button = new LayerTreeButton(this, (LayerTreeAction) action);
				} else if (action instanceof LayerTreeModalAction) {
					button = new LayerTreeModalButton(this, (LayerTreeModalAction) action);
				}
				if (button != null) {
					toolStrip.addMember(button);
					LayoutSpacer spacer = new LayoutSpacer();
					spacer.setWidth(2);
					toolStrip.addMember(spacer);
				}
			}
		}
		Canvas[] toolStripMembers = toolStrip.getMembers();
		updateButtonIconsAndStates(toolStripMembers);
		return toolStrip;
	}

	/**
	 * Builds up the tree showing the layers
	 * 
	 * @param mapModel
	 *            The mapModel containing the layerTree
	 */
	private void buildTree(MapModel mapModel) {
		treeGrid.setWidth100();
		treeGrid.setHeight100();
		treeGrid.setShowHeader(false);
		tree = new RefreshableTree();
		final TreeNode nodeRoot = new TreeNode("ROOT");
		tree.setRoot(nodeRoot); // invisible ROOT node (ROOT node is required)

		ClientLayerTreeInfo layerTreeInfo = mapModel.getMapInfo().getLayerTree();
		if (layerTreeInfo != null) {
			ClientLayerTreeNodeInfo treeNode = layerTreeInfo.getTreeNode();
			processNode(treeNode, nodeRoot, tree, mapModel, false);
		}

		treeGrid.setData(tree);
		treeGrid.addLeafClickHandler(this);
		treeGrid.addFolderClickHandler(this);
	}

	/**
	 * Processes a treeNode (add it to the TreeGrid)
	 * 
	 * @param treeNode
	 *            The treeNode to process
	 * @param nodeRoot
	 *            The root node to which the treeNode has te be added
	 * @param tree
	 *            The tree to which the node has to be added
	 * @param mapModel
	 *            map model
	 * @param refresh
	 *            True if the tree is refreshed (causing it to keep its expanded state)
	 */
	private void processNode(final ClientLayerTreeNodeInfo treeNode, final TreeNode nodeRoot, final Tree tree,
			final MapModel mapModel, final boolean refresh) {
		if (null != treeNode) {
			String treeNodeLabel = treeNode.getLabel();
			final TreeNode node = new TreeNode(treeNodeLabel);

			tree.add(node, nodeRoot);

			// (final leafs)
			for (ClientLayerInfo info : treeNode.getLayers()) {
				Layer<?> layer = mapModel.getLayer(info.getId());
				tree.add(new LayerTreeTreeNode(this.tree, layer), node);
			}

			// treeNodes
			List<ClientLayerTreeNodeInfo> childs = treeNode.getTreeNodes();
			for (ClientLayerTreeNodeInfo newNode : childs) {
				processNode(newNode, node, tree, mapModel, refresh);
			}

			// expand tree nodes
			// when not refreshing expand them like configured
			// when refreshing expand them as before the refresh
			boolean isTreeNodeExpanded = treeNode.isExpanded();
			if (!refresh) {
				if (isTreeNodeExpanded) {
					tree.openFolder(node);
				}
			} else {
				// TODO close previously opened tree nodes, close others
			}
		}
	}

	/**
	 * Updates the icons and the state of the buttons in the toolbar based upon the currently selected layer
	 * 
	 * @param toolStripMembers
	 *            data for the toolbar
	 */
	private void updateButtonIconsAndStates(Canvas[] toolStripMembers) {
		for (Canvas toolStripMember : toolStripMembers) {
			if (toolStripMember instanceof LayerTreeModalButton) {
				((LayerTreeModalButton) toolStripMember).update();
			} else if (toolStripMember instanceof LayerTreeButton) {
				((LayerTreeButton) toolStripMember).update();
			}
		}
	}

	/**
	 * General definition of an action button for the layer tree.
	 * 
	 * @author Frank Wynants
	 * @author Pieter De Graef
	 */
	private class LayerTreeButton extends IButton {

		private LayerTree tree;

		private LayerTreeAction action;

		public LayerTreeButton(final LayerTree tree, final LayerTreeAction action) {
			this.tree = tree;
			this.action = action;
			setWidth(LAYERTREEBUTTON_SIZE);
			setHeight(LAYERTREEBUTTON_SIZE);
			setIconSize(LAYERTREEBUTTON_SIZE - 8);
			setIcon(action.getIcon());
			setTooltip(action.getTooltip());
			setActionType(SelectionType.BUTTON);
			setShowDisabledIcon(false);
			addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					try {
						action.onClick(tree.getSelectedLayerTreeNode().getLayer());
						update();
					} catch (Throwable t) {
						GWT.log("LayerTreeButton", t);
					}
				}
			});
		}

		public void update() {
			LayerTreeTreeNode selected = tree.getSelectedLayerTreeNode();
			if (selected != null && action.isEnabled(selected.getLayer())) {
				setDisabled(false);
				setIcon(action.getIcon());
				setTooltip(action.getTooltip());
			} else {
				setDisabled(true);
				setIcon(action.getDisabledIcon());
				setTooltip("");
			}
		}
	}

	/**
	 * General definition of a modal button for the layer tree.
	 * 
	 * @author Frank Wynants
	 * @author Pieter De Graef
	 */
	private class LayerTreeModalButton extends IButton {

		private LayerTree tree;

		private LayerTreeModalAction modalAction;

		/**
		 * Constructor
		 * 
		 * @param tree
		 *            The currently selected layer
		 * @param modalAction
		 *            The action coupled to this button
		 */
		public LayerTreeModalButton(final LayerTree tree, final LayerTreeModalAction modalAction) {
			this.tree = tree;
			this.modalAction = modalAction;
			setWidth(LayerTree.LAYERTREEBUTTON_SIZE);
			setHeight(LayerTree.LAYERTREEBUTTON_SIZE);
			setIconSize(LayerTree.LAYERTREEBUTTON_SIZE - 8);
			setIcon(modalAction.getDeselectedIcon());
			setActionType(SelectionType.CHECKBOX);
			setTooltip(modalAction.getDeselectedTooltip());
			setShowDisabledIcon(false);

			this.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					LayerTreeTreeNode selectedLayerNode = tree.getSelectedLayerTreeNode();
					if (LayerTreeModalButton.this.isSelected()) {
						modalAction.onSelect(selectedLayerNode.getLayer());
					} else {
						modalAction.onDeselect(selectedLayerNode.getLayer());
					}
					selectedLayerNode.updateIcon();
					update();
				}
			});
		}

		public void update() {
			LayerTreeTreeNode selected = tree.getSelectedLayerTreeNode();
			if (selected != null && modalAction.isEnabled(selected.getLayer())) {
				setDisabled(false);
			} else {
				setSelected(false);
				setDisabled(true);
				setIcon(modalAction.getDisabledIcon());
				setTooltip("");
			}
			if (selected != null && modalAction.isSelected(selected.getLayer())) {
				setIcon(modalAction.getSelectedIcon());
				setTooltip(modalAction.getSelectedTooltip());
				select();
			} else if (selected != null) {
				setIcon(modalAction.getDeselectedIcon());
				setTooltip(modalAction.getDeselectedTooltip());
				deselect();
			}
		}
	}

	/**
	 * A SmartGWT Tree with one extra method 'refresh'. This is needed to update icons on the fly in a tree
	 * 
	 * @author Frank Wynants
	 */
	private class RefreshableTree extends Tree {

		/**
		 * Refreshes the icons in the tree, this is done by closing and reopening all nodes A dirty solution but no
		 * other option was found at the time
		 */
		public void refreshIcons() {
			TreeNode[] openNodes = this.getOpenList(this.getRoot());

			this.closeAll();
			for (TreeNode openNode : openNodes) {
				this.openFolder(openNode);
			}
		}
	}

	/**
	 * A node inside the LayerTree.
	 * 
	 * @author Frank Wynants
	 * @author Pieter De Graef
	 */
	public class LayerTreeTreeNode extends TreeNode {

		private RefreshableTree tree;

		private Layer<?> layer;

		/**
		 * Constructor creates a TreeNode with layer.getLabel as label.
		 * 
		 * @param tree
		 *            tree for node
		 * @param layer
		 *            The layer object
		 */
		public LayerTreeTreeNode(RefreshableTree tree, Layer<?> layer) {
			super(layer.getLabel());
			this.layer = layer;
			this.tree = tree;
			updateIcon();
		}

		/**
		 * Causes the node to check its status (visible, showing labels, ...) and to update its icon to match its
		 * status.
		 */
		public void updateIcon() {
			if (getLayer().isShowing()) {
				if (getLayer().isLabeled()) {
					// show icon labeled and showing
					setIcon("[ISOMORPHIC]/geomajas/widget/layertree/layer-show-labeled.png");
				} else {
					// show showing icon
					setIcon("[ISOMORPHIC]/geomajas/widget/layertree/layer-show.png");
				}
			} else {
				// show not showing
				setIcon("[ISOMORPHIC]/geomajas/widget/layertree/layer-hide.png");
			}
			tree.refreshIcons();
		}

		public Layer<?> getLayer() {
			return layer;
		}
	}
}