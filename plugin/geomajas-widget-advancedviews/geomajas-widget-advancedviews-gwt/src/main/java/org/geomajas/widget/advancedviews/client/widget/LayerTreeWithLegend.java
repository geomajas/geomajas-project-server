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

import java.util.List;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientLayerTreeNodeInfo;
import org.geomajas.global.Api;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.RasterLayer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.advancedviews.client.util.UrlBuilder;

import com.google.gwt.core.client.GWT;
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

	private static final String EXPANDED_ATTR = "isExpanded";

	public LayerTreeWithLegend(final MapWidget mapWidget) {
		super(mapWidget);
		treeGrid.setIconSize(DEFAULT_ICONSIZE);
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
				LayerTreeLegendItemNode tn = new LayerTreeLegendItemNode(this, rl.getServerLayerId());
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
		public LayerTreeLegendItemNode(LayerTreeLegendNode parent, String layerId) {
			super(parent.tree, parent.layer);
			this.parent = parent;
			setTitle(layer.getLabel());
			setName(parent.getAttribute("id") + "_legend");
			url.addPath(LEGEND_ICONS_PATH);
			url.addParameter("widgetId", LayerTreeWithLegend.this.getID());
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
	protected void syncNodeState(TreeNode rootNode) {
		for (TreeNode childnode : tree.getAllNodes(rootNode)) {
			if (childnode instanceof LayerTreeLegendNode) {
				if (((LayerTreeLegendNode) childnode).layer.isShowing()) {
					tree.openFolder(childnode);
				} else {
					tree.closeFolder(childnode);
				}
			} else if (!(childnode instanceof LayerTreeLegendItemNode)) {
				if (childnode.getAttributeAsBoolean(EXPANDED_ATTR)) {
					tree.openFolder(childnode);
				} else {
					tree.closeFolder(childnode);
				}
			}
		}
	}
}
