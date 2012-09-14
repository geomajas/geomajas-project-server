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
package org.geomajas.plugin.deskmanager.client.gwt.manager.util;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientLayerTreeInfo;
import org.geomajas.configuration.client.ClientLayerTreeNodeInfo;

/**
 * @author Balder
 * 
 */
public final class LayerTreeUtil {

	
	private LayerTreeUtil() {}
	
	/**
	 * Checks by layerId if the layer is in the {@link ClientLayerTreeInfo}.
	 * 
	 * @param layerId
	 * @param layerTree {@link ClientLayerTreeInfo}
	 * @return true if layer found in the LayerTree false otherwise
	 */
	public static boolean checkLayerAvailable(String layerId, ClientLayerTreeInfo layerTree) {
		if (null != layerTree) {
			ClientLayerTreeNodeInfo node = layerTree.getTreeNode();
			return check(node, layerId);
		}
		return false;
	}

	private static boolean check(ClientLayerTreeNodeInfo node, String layerId) {
		List<ClientLayerInfo> layers = node.getLayers();
		if (layers != null) {
			for (ClientLayerInfo layer : layers) {
				if (layer.getId().equals(layerId)) {
					return true;
				}
			}
		}
		if (node.getTreeNodes() != null) {
			for (ClientLayerTreeNodeInfo innernode : node.getTreeNodes()) {
				if (check(innernode, layerId)) {
					return true;
				}
			}
		}
		return false;
	}

	public static ClientLayerTreeNodeInfo getParent(ClientLayerInfo layer, ClientLayerTreeNodeInfo node) {
		if (node.getLayers().contains(layer)) {
			return node;
		} else {
			for (ClientLayerTreeNodeInfo tn : node.getTreeNodes()) {
				ClientLayerTreeNodeInfo parentNode = getParent(layer, tn);
				if (parentNode != null) {
					return parentNode;
				}
			}
		}
		return null;
	}

	/**
	 * Check if a layer is visible.
	 * 
	 * @param layerId
	 * @param layerTree
	 * @return true on visible false otherwise
	 */
	public static boolean checkLayerVisibility(String layerId, ClientLayerTreeInfo layerTree) {
		if (null != layerTree) {
			ClientLayerTreeNodeInfo node = layerTree.getTreeNode();
			return checkVisibility(node, layerId);
		}
		return false;
	}

	private static boolean checkVisibility(ClientLayerTreeNodeInfo node, String layerId) {
		List<ClientLayerInfo> layers = node.getLayers();
		if (layers != null) {
			for (ClientLayerInfo layer : layers) {
				if (layer.getId().equals(layerId) && layer.isVisible()) {
					return true;
				}
			}
		}
		if (node.getTreeNodes() != null) {
			for (ClientLayerTreeNodeInfo innernode : node.getTreeNodes()) {
				if (checkVisibility(innernode, layerId)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Retrieves all layers available in a layertree.
	 * 
	 * @param layerTree
	 * @return all layers in the layertree
	 */
	public static List<ClientLayerInfo> getLayersFromLayerTree(ClientLayerTreeInfo layerTree) {
		List<ClientLayerInfo> layers = new ArrayList<ClientLayerInfo>();
		if (null != layerTree) {
			ClientLayerTreeNodeInfo node = layerTree.getTreeNode();
			getLayers(node, layers);
		}

		return layers;
	}

	private static void getLayers(ClientLayerTreeNodeInfo node, List<ClientLayerInfo> layers) {
		if (node.getLayers() != null) {
			layers.addAll(node.getLayers());
		}
		if (node.getTreeNodes() != null) {
			for (ClientLayerTreeNodeInfo innernode : node.getTreeNodes()) {
				getLayers(innernode, layers);
			}
		}
	}

	public static ClientLayerTreeInfo copy(ClientLayerTreeInfo availableLayerTree) {

		return null;
	}

	/**
	 * Removes a layer from the layertree.
	 * 
	 * @param layertree
	 * @param layer
	 * @return
	 */
	public static ClientLayerTreeInfo removeLayer(ClientLayerTreeInfo layertree, ClientLayerInfo layer) {
		if (null != layertree) {
			ClientLayerTreeNodeInfo node = layertree.getTreeNode();
			remove(node, layer);
		}
		return layertree;

	}

	private static void remove(ClientLayerTreeNodeInfo node, ClientLayerInfo layer) {
		if (node.getLayers() != null) {
			ClientLayerInfo toRemove = null;
			for (ClientLayerInfo l : node.getLayers()) {
				if (l.getId().equals(layer.getId())) {
					toRemove = l;
					break;
				}
			}
			if (null != toRemove) {
				node.getLayers().remove(toRemove);
				return;
			}
		}
		if (node.getTreeNodes() != null) {
			for (ClientLayerTreeNodeInfo innernode : node.getTreeNodes()) {
				remove(innernode, layer);
			}
		}

	}

}
