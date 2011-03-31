/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.widget.advancedviews.client.widget;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientLayerTreeInfo;
import org.geomajas.configuration.client.ClientLayerTreeNodeInfo;
import org.geomajas.configuration.client.ClientToolInfo;
import org.geomajas.global.Api;
import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.gwt.client.action.layertree.LayerTreeAction;
import org.geomajas.gwt.client.action.layertree.LayerTreeModalAction;
import org.geomajas.gwt.client.action.layertree.LayerTreeRegistry;
import org.geomajas.gwt.client.map.event.LayerChangedHandler;
import org.geomajas.gwt.client.map.event.LayerLabeledEvent;
import org.geomajas.gwt.client.map.event.LayerShownEvent;
import org.geomajas.gwt.client.map.event.LayerStyleChangeEvent;
import org.geomajas.gwt.client.map.event.LayerStyleChangedHandler;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.RasterLayer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.advancedviews.client.AdvancedViewsMessages;
import org.geomajas.widget.advancedviews.client.util.LayerIconHelper;
import org.geomajas.widget.advancedviews.client.util.UrlBuilder;
import org.geomajas.widget.advancedviews.client.util.WidgetInfoHelper;
import org.geomajas.widget.advancedviews.configuration.client.LayerTreeWithLegendInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.LeafClickEvent;

/**
 * A layertree widget with combined legend per layer.
 * 
 * @author Kristof Heirwegh
 * @since 1.0.0
 */
@Api
public class LayerTreeWithLegend extends LayerTreeBase {

	private static final String LEGEND_ICONS_PATH = "d/legendIcons";

	private static final String SHOW_LAYERINFO_ICON = "[ISOMORPHIC]/geomajas/silk/cog.png";

	private AdvancedViewsMessages messages = GWT.create(AdvancedViewsMessages.class);

	private static final String EXPANDED_ATTR = "isExpanded";

	private final MapWidget mapWidget;

	private final List<HandlerRegistration> registrations = new ArrayList<HandlerRegistration>();

	protected LayerTreeTreeNode rollOverLayerTreeNode;

	public LayerTreeWithLegend(final MapWidget mapWidget) {
		super(mapWidget);
		this.mapWidget = mapWidget;
		treeGrid.setShowRollOverCanvas(true);
	}

	public int getIconSize() {
		return treeGrid.getImageSize();
	}

	public void setIconSize(int iconSize) {
		treeGrid.setIconSize(iconSize);
	}

	/**
	 * Processes a treeNode (add it to the TreeGrid).
	 * 
	 * @param treeNode
	 *            The treeNode to process
	 * @param nodeRoot
	 *            The root node to which the treeNode has to be added
	 * @param refresh
	 *            True if the tree is refreshed (causing it to keep its expanded
	 *            state)
	 */
	protected void processNode(final ClientLayerTreeNodeInfo treeNode, final TreeNode nodeRoot, final boolean refresh) {
		if (null != treeNode) {
			String treeNodeLabel = treeNode.getLabel();
			final TreeNode node = new TreeNode(treeNodeLabel);
			node.setAttribute(EXPANDED_ATTR, treeNode.isExpanded());
			tree.add(node, nodeRoot);

			// (final leafs)
			for (ClientLayerInfo info : treeNode.getLayers()) {
				Layer<?> layer = mapModel.getLayer(info.getId());
				LayerTreeLegendNode ltln = new LayerTreeLegendNode(this.tree, layer);
				tree.add(ltln, node);
				ltln.init();
			}

			// treeNodes
			List<ClientLayerTreeNodeInfo> childs = treeNode.getTreeNodes();
			for (ClientLayerTreeNodeInfo newNode : childs) {
				processNode(newNode, node, refresh);
			}
		}
	}

	/**
	 * When a legendItem is selected, select the layer instead.
	 */
	public void onLeafClick(LeafClickEvent event) {
		LayerTreeTreeNode layerTreeNode;
		if (event.getLeaf() instanceof LayerTreeLegendItemNode) {
			layerTreeNode = ((LayerTreeLegendItemNode) event.getLeaf()).parent;
			treeGrid.deselectRecord(event.getLeaf());
			treeGrid.selectRecord(layerTreeNode);
		} else {
			layerTreeNode = (LayerTreeTreeNode) event.getLeaf();
		}

		// -- update model
		if (null != selectedLayerTreeNode
				&& layerTreeNode.getLayer().getId().equals(selectedLayerTreeNode.getLayer().getId())) {
			mapModel.selectLayer(null);
		} else {
			mapModel.selectLayer(layerTreeNode.getLayer());
		}
	}

	// ----------------------------------------------------------

	/**
	 * Node with legend for LayerNode.
	 */
	public class LayerTreeLegendNode extends LayerTreeTreeNode {

		public LayerTreeLegendNode(RefreshableTree tree, Layer<?> layer) {
			super(tree, layer);
		}

		public void init() {
			if (layer instanceof VectorLayer) {
				VectorLayer vl = (VectorLayer) layer;
				NamedStyleInfo nsi = vl.getLayerInfo().getNamedStyleInfo();
				for (FeatureStyleInfo fsi : nsi.getFeatureStyles()) {
					LayerTreeLegendItemNode tn = new LayerTreeLegendItemNode(this, vl.getServerLayerId(),
							nsi.getName(), fsi);
					tree.add(tn, this);
				}
			} else {
				RasterLayer rl = (RasterLayer) layer;
				LayerTreeLegendItemNode tn = new LayerTreeLegendItemNode(this, rl.getServerLayerId(),
						LayerIconHelper.getSmallLayerIconUrl(rl));
				tree.add(tn, this);
			}
		}
	}

	/**
	 * Node which displays a legend icon + description.
	 */
	public class LayerTreeLegendItemNode extends LayerTreeTreeNode {
		private LayerTreeLegendNode parent;
		private UrlBuilder url = new UrlBuilder(GWT.getHostPageBaseURL());

		// rasterlayer
		public LayerTreeLegendItemNode(LayerTreeLegendNode parent, String layerId, String rasterIconUrl) {
			super(parent.tree, parent.layer);
			this.parent = parent;
			setTitle(layer.getLabel());
			setName(parent.getAttribute("id") + "_legend");
			url.addPath(LEGEND_ICONS_PATH);
			url.addParameter("widgetId", LayerTreeWithLegend.this.getID());
			if (rasterIconUrl != null) {
				url.addParameter("styleName", rasterIconUrl);
			}
			url.addParameter("layerId", layerId);
			setIcon(url.toString());
		}

		// vectorlayer
		public LayerTreeLegendItemNode(LayerTreeLegendNode parent, String layerId, String styleName,
				FeatureStyleInfo fsi) {
			super(parent.tree, parent.layer);
			this.parent = parent;
			setName(fsi.getName());
			url.addPath(LEGEND_ICONS_PATH);
			url.addParameter("widgetId", LayerTreeWithLegend.this.getID());
			url.addParameter("layerId", layerId);
			url.addParameter("styleName", styleName);
			url.addParameter("featureStyleId", fsi.getStyleId());
			setIcon(url.toString());
		}

		@Override
		public void updateIcon() {
			// leave my icons alone!
		}

		public LayerTreeLegendNode getParent() {
			return parent;
		}

		public void setParent(LayerTreeLegendNode parent) {
			this.parent = parent;
		}
	}

	@Override
	protected void syncNodeState(boolean layersOnly) {
		for (TreeNode childnode : tree.getAllNodes(tree.getRoot())) {
			if (childnode instanceof LayerTreeLegendNode) {
				if (((LayerTreeLegendNode) childnode).layer.isShowing()) {
					tree.openFolder(childnode);
				} else {
					tree.closeFolder(childnode);
				}
			} else if (!layersOnly && !(childnode instanceof LayerTreeLegendItemNode)) {
				if (childnode.getAttributeAsBoolean(EXPANDED_ATTR)) {
					tree.openFolder(childnode);
				} else {
					tree.closeFolder(childnode);
				}
			}
		}
	}

	@Override
	protected TreeGrid createTreeGrid() {
		return createTreeGridInfoWindowRollover();
	}

	@Override
	protected void onIconClick(TreeNode node) {
		if (node instanceof LayerTreeLegendNode) {
			super.onIconClick(node);
		} else if (node instanceof TreeNode) {
			// TODO -- show/hide all layers in folder
			GWT.log("TODO");
		}
	}

	protected TreeGrid createTreeGridInfoWindowRollover() {
		return new TreeGrid() {
			private HLayout rollOverTools;
			private HLayout emptyRollOver;

			@Override
			protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) {
				if (rollOverTools == null) {
					rollOverTools = new HLayout();
					rollOverTools.setSnapTo("TR");
					rollOverTools.setWidth(25);
					rollOverTools.setHeight(LAYERTREEBUTTON_SIZE);
					emptyRollOver = new HLayout();
					emptyRollOver.setWidth(1);
					emptyRollOver.setHeight(LAYERTREEBUTTON_SIZE);

					ImgButton showInfo = new ImgButton();
					showInfo.setShowDown(false);
					showInfo.setShowRollOver(false);
					showInfo.setLayoutAlign(Alignment.CENTER);
					showInfo.setSrc(SHOW_LAYERINFO_ICON);
					showInfo.setPrompt(messages.layerTreeWithLegendLayerActionsToolTip());
					showInfo.setHeight(16);
					showInfo.setWidth(16);
					showInfo.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							LayerActions la = new LayerActions(rollOverLayerTreeNode.getLayer());
							la.draw();
						}
					});
					rollOverTools.addMember(showInfo);
				}

				ListGridRecord lgr = this.getRecord(rowNum);
				if (lgr instanceof LayerTreeLegendItemNode) {
					rollOverLayerTreeNode = ((LayerTreeLegendItemNode) lgr).parent;
				} else if (lgr instanceof LayerTreeLegendNode) {
					rollOverLayerTreeNode = (LayerTreeTreeNode) lgr;
				} else {
					rollOverLayerTreeNode = null;
					rollOverTools.setVisible(false);
					return emptyRollOver;
				}

				rollOverTools.setVisible(true);
				return rollOverTools;
			}
		};
	}

	protected TreeGrid createTreeGridFullRollover() {
		return new TreeGrid() {
			private HLayout rollOverTools;
			private HLayout emptyRollOver;
			private Canvas[] toolButtons;

			@Override
			protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) {
				if (rollOverTools == null) {
					rollOverTools = new HLayout();
					rollOverTools.setSnapTo("TR");
					rollOverTools.setWidth(50);
					rollOverTools.setHeight(LAYERTREEBUTTON_SIZE);
					emptyRollOver = new HLayout();
					emptyRollOver.setWidth(1);
					emptyRollOver.setHeight(LAYERTREEBUTTON_SIZE);

					ClientLayerTreeInfo layerTreeInfo = mapModel.getMapInfo().getLayerTree();
					if (layerTreeInfo != null) {
						for (ClientToolInfo tool : layerTreeInfo.getTools()) {
							String id = tool.getId();
							IButton button = null;
							ToolbarBaseAction action = LayerTreeRegistry.getToolbarAction(id, mapWidget);
							if (action instanceof LayerTreeAction) {
								button = new LayerTreeButton(LayerTreeWithLegend.this, (LayerTreeAction) action);
							} else if (action instanceof LayerTreeModalAction) {
								button = new LayerTreeModalButton(LayerTreeWithLegend.this,
										(LayerTreeModalAction) action);
							}
							if (button != null) {
								rollOverTools.addMember(button);
								LayoutSpacer spacer = new LayoutSpacer();
								spacer.setWidth(2);
								rollOverTools.addMember(spacer);
							}
						}
					}
					toolButtons = rollOverTools.getMembers();
				}

				ListGridRecord lgr = this.getRecord(rowNum);
				if (lgr instanceof LayerTreeLegendItemNode) {
					rollOverLayerTreeNode = ((LayerTreeLegendItemNode) lgr).parent;
				} else if (lgr instanceof LayerTreeLegendNode) {
					rollOverLayerTreeNode = (LayerTreeTreeNode) lgr;
				} else {
					rollOverLayerTreeNode = null;
					rollOverTools.setVisible(false);
					return emptyRollOver;
				}

				rollOverTools.setVisible(true);
				updateButtonIconsAndStates();
				return rollOverTools;
			}

			/**
			 * Updates the icons and the state of the buttons in the toolbar
			 * based upon the current layer
			 * 
			 * @param toolStripMembers
			 *            data for the toolbar
			 */
			private void updateButtonIconsAndStates() {
				for (Canvas toolButton : toolButtons) {
					if (toolButton instanceof LayerTreeModalButton) {
						((LayerTreeModalButton) toolButton).update();
					} else if (toolButton instanceof LayerTreeButton) {
						((LayerTreeButton) toolButton).update();
					}
				}
			}
		};
	}

	/**
	 * General definition of an action button for the layer tree.
	 * 
	 * @author Frank Wynants
	 * @author Pieter De Graef
	 */
	private class LayerTreeButton extends IButton {

		private LayerTreeWithLegend tree;

		private LayerTreeAction action;

		public LayerTreeButton(final LayerTreeWithLegend tree, final LayerTreeAction action) {
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
						action.onClick(tree.rollOverLayerTreeNode.getLayer());
						update();
					} catch (Throwable t) {
						GWT.log("LayerTreeButton onClick error", t);
					}
				}
			});
		}

		public void update() {
			LayerTreeTreeNode selected = tree.rollOverLayerTreeNode;
			if (selected != null && action.isEnabled(selected.getLayer())) {
				setDisabled(false);
				setIcon(action.getIcon());
				setTooltip(action.getTooltip());
			} else {
				setDisabled(true);
				GWT.log("LayerTreeButton" + action.getDisabledIcon());
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

		private LayerTreeWithLegend tree;

		private LayerTreeModalAction modalAction;

		/**
		 * Constructor
		 * 
		 * @param tree
		 *            The currently selected layer
		 * @param modalAction
		 *            The action coupled to this button
		 */
		public LayerTreeModalButton(final LayerTreeWithLegend tree, final LayerTreeModalAction modalAction) {
			this.tree = tree;
			this.modalAction = modalAction;
			setWidth(LAYERTREEBUTTON_SIZE);
			setHeight(LAYERTREEBUTTON_SIZE);
			setIconSize(LAYERTREEBUTTON_SIZE - 8);
			setIcon(modalAction.getDeselectedIcon());
			setActionType(SelectionType.CHECKBOX);
			setTooltip(modalAction.getDeselectedTooltip());
			setShowDisabledIcon(false);

			this.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					LayerTreeTreeNode selectedLayerNode = tree.rollOverLayerTreeNode;
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
			LayerTreeTreeNode selected = tree.rollOverLayerTreeNode;
			if (selected != null && modalAction.isEnabled(selected.getLayer())) {
				setDisabled(false);
			} else {
				setSelected(false);
				setDisabled(true);
				GWT.log("LayerTreeModalButton" + modalAction.getDisabledIcon());
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

	// -- part of legend

	@Override
	protected void initialize() {
		super.initialize();
		LayerTreeWithLegendInfo ltwli = WidgetInfoHelper.getClientWidgetInfo(LayerTreeWithLegendInfo.IDENTIFIER,
				LayerTreeWithLegendInfo.class, mapWidget);
		setIconSize(ltwli == null ? DEFAULT_ICONSIZE : ltwli.getIconSize());

		for (Layer<?> layer : mapModel.getLayers()) {
			registrations.add(layer.addLayerChangedHandler(new LayerChangedHandler() {
				public void onLabelChange(LayerLabeledEvent event) {
				}

				public void onVisibleChange(LayerShownEvent event) {
					GWT.log("Legend: onVisibleChange()");
					syncNodeState(true);
				}
			}));
			registrations.add(layer.addLayerStyleChangedHandler(new LayerStyleChangedHandler() {
				public void onLayerStyleChange(LayerStyleChangeEvent event) {
					GWT.log("Legend: onLayerStyleChange()");
					// TODO update layerstyles
				}
			}));
		}
	}

	/** Remove all handlers on unload. */
	protected void onUnload() {
		if (registrations != null) {
			for (HandlerRegistration registration : registrations) {
				registration.removeHandler();
			}
		}
		super.onUnload();
	}
}
