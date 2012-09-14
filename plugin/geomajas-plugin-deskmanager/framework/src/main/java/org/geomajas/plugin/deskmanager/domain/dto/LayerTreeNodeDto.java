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
package org.geomajas.plugin.deskmanager.domain.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.geomajas.plugin.deskmanager.domain.dto.LayerTreeDto.LayerTreeNodeSearchVisitor;
import org.geomajas.plugin.deskmanager.domain.dto.LayerTreeDto.LayerTreeNodeVisitor;

/**
 * Leafs map layers. Nodes can be used to group leafs.
 * 
 * <p>
 * The label of a node is set in LayerView
 * 
 * @author Kristof Heirwegh
 */
public class LayerTreeNodeDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private boolean leaf = true;

	private boolean expanded = true; // only for node

	private String name;

	private LayerTreeNodeDto parentNode;

	private List<LayerTreeNodeDto> children = new ArrayList<LayerTreeNodeDto>();

	private LayerViewDto view;

	private String clientLayerId;

	private String styleUuid;

	private boolean publicLayer = true;

	// ----------------------------------------------------------

	public LayerTreeNodeDto() {
	}

	public LayerTreeNodeDto(String name, boolean leaf) {
		this.name = name;
		this.leaf = leaf;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public LayerTreeNodeDto getParentNode() {
		return parentNode;
	}

	public void setParentNode(LayerTreeNodeDto parentNode) {
		this.parentNode = parentNode;
	}

	public List<LayerTreeNodeDto> getChildren() {
		return children;
	}

	public LayerViewDto getView() {
		return view;
	}

	public void setView(LayerViewDto view) {
		this.view = view;
	}

	public String getStyleUuid() {
		return styleUuid;
	}

	public void setStyleUuid(String styleUuid) {
		this.styleUuid = styleUuid;
	}

	/**
	 * Convenience method to get direct or inferred name.
	 */
	public String getName() {
		if (view != null) {
			return view.getLabel();
		} else if (name != null) {
			return name;
		} else {
			return "[Naam node niet gezet ?]";
		}
	}

	public String getNodeName() {
		return name;
	}

	/**
	 * Convenience method to get clientlayerid from model.
	 */
	public String getClientLayerId() {
		if (isLeaf()) {
			return clientLayerId;
		} else {
			return "-1";
		}
	}

	/**
	 * Parents are visited before children.
	 * 
	 * @param visitor
	 */
	public void visit(LayerTreeNodeVisitor visitor) {
		visitor.visit(this);
		if (!isLeaf()) {
			for (LayerTreeNodeDto ltn : children) {
				ltn.visit(visitor);
			}
		}
	}

	/**
	 * Parents are visited before children.
	 * <p>
	 * Search stops at the first non-null value that is returned.
	 * 
	 * @param visitor
	 */
	public <T> T search(LayerTreeNodeSearchVisitor<T> visitor) {
		T val = visitor.search(this);
		if (val != null) {
			return val;
		}
		if (!isLeaf()) {
			for (LayerTreeNodeDto ltn : children) {
				val = ltn.search(visitor);
				if (val != null) {
					return val; // shortcut
				}
			}
		}
		return null;
	}

	/**
	 * Does a compare using clientlayerId/name.
	 * 
	 * @return
	 */
	public boolean contains(LayerTreeNodeDto ltn) {
		if (isLeaf()) {
			if (ltn.isLeaf()) { // compare using clientlayerId
				return getClientLayerId().equals(ltn.getClientLayerId());
			} else { // compare using name
				if (name != null) {
					return name.equals(ltn.getNodeName());
				} else {
					return false;
				}
			}
		} else {
			for (LayerTreeNodeDto node : getChildren()) {
				if (node.contains(ltn)) {
					return true;
				}
			}
			return false;
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setClientLayerId(String clientLayerId) {
		this.clientLayerId = clientLayerId;
	}

	/**
	 * This only clones the nodes, id nor view/model/style are cloned (should not be necessary as long as you only
	 * clone. the systemlayertreenode)
	 * <p>
	 * uses null as parent (so only use for rootnode)
	 */
	public LayerTreeNodeDto clone() {
		return clone(null);
	}

	/**
	 * This only clones the nodes, id nor view/model/style are cloned (should not be necessary as long as you only clone
	 * the systemlayertreenode).
	 */
	public LayerTreeNodeDto clone(LayerTreeNodeDto clonedParent) {
		LayerTreeNodeDto clone = new LayerTreeNodeDto(name, leaf);
		clone.expanded = expanded;
		clone.parentNode = clonedParent;
		clone.view = view;
		clone.clientLayerId = clientLayerId;
		clone.publicLayer = publicLayer;
		clone.styleUuid = styleUuid;
		if (!leaf) {
			for (LayerTreeNodeDto child : children) {
				clone.children.add(child.clone(clone));
			}
		}
		return clone;
	}

	public boolean isPublicLayer() {
		return publicLayer;
	}

	public void setPublicLayer(boolean publicLayer) {
		this.publicLayer = publicLayer;
	}
}
