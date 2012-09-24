package org.geomajas.plugin.deskmanager.client.gwt.manager.common.layertree;

import org.geomajas.plugin.deskmanager.domain.dto.LayerDto;
import org.geomajas.widget.layer.configuration.client.ClientAbstractNodeInfo;
import org.geomajas.widget.layer.configuration.client.ClientBranchNodeInfo;
import org.geomajas.widget.layer.configuration.client.ClientLayerNodeInfo;

import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 * 
 */
public class LayerTreeNode extends TreeNode {

	public static final String FLD_NAME = "name";

	public static final String FLD_PUBLIC = "public";

	private ClientAbstractNodeInfo node;

	private LayerDto layer;

	public LayerTreeNode(ClientBranchNodeInfo node) {
		this(node, null);
	}
	
	public LayerTreeNode(ClientAbstractNodeInfo node, LayerDto layer) {
		if (node == null) {
			throw new IllegalArgumentException("Was expecting a node!");
		}
		this.node = node;
		this.layer = layer;
		if (node instanceof ClientBranchNodeInfo) {
			setName(((ClientBranchNodeInfo) node).getLabel());
			setIsFolder(true);
		} else if (node instanceof ClientLayerNodeInfo) {
			if (layer != null) {
				if (layer.getClientLayerInfo() != null) {
					setName(layer.getClientLayerInfo().getLabel());
				} else {
					setName(layer.getLayerModel().getName());
				}
			}
			setIsFolder(false);
		}
	}

	public ClientAbstractNodeInfo getNode() {
		return node;
	}

	/**
	 * Get client layer info, might return null if node isn't a leaf.
	 * 
	 * @return the clientLayerInfo
	 */
	public LayerDto getLayer() {
		return layer;
	}
}