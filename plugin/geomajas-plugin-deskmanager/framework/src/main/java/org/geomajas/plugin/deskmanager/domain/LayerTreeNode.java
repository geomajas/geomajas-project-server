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
package org.geomajas.plugin.deskmanager.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.plugin.deskmanager.configuration.client.ExtraClientLayerInfo;
import org.geomajas.plugin.deskmanager.domain.LayerTree.LayerTreeNodeVisitor;

/**
 * Leafs map layers. Nodes can be used to group leafs.
 * 
 * <p>
 * The label of a node is set in LayerView
 * 
 * @author Kristof Heirwegh
 */
@Entity
@Table(name = "config_layertreenodes")
public class LayerTreeNode implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * The name of the node or layer.
	 * <p>
	 */
	@Column(name = "name", nullable = false)
	private String name;

	/**
	 * Is this a leaf-node. (has no children and represents a layer)
	 */
	@Column(name = "leaf")
	private boolean leaf = true;

	/**
	 * Should the node be expanded at startup.
	 */
	@Column(name = "expanded")
	private boolean expanded = true; // only for node

	// composite pattern - leaf
	@ManyToOne
	private LayerTreeNode parentNode;

	// composite pattern
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "parentnode_id")
	@OrderColumn(name = "sortorder")
	private List<LayerTreeNode> children = new ArrayList<LayerTreeNode>();

	@OneToOne(optional = true, orphanRemoval = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "view_id")
	private LayerView view;

	// must be set if leaf == true
	@Column(name = "clientlayerid")
	private String clientLayerId; // referenced Bean name

	@Column(name = "publiclayer")
	private boolean publicLayer = true;

	/**
	 * UUID reference to the SLD style file
	 */
	@Column(name = "style_uuid")
	private String styleUuid;

	// ----------------------------------------------------------

	public LayerTreeNode() {
	}

	public LayerTreeNode(String name) {
		this.name = name;
	}

	// ----------------------------------------------------------

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

	public LayerTreeNode getParentNode() {
		return parentNode;
	}

	public void setParentNode(LayerTreeNode parentNode) {
		this.parentNode = parentNode;
	}

	public List<LayerTreeNode> getChildren() {
		return children;
	}

	public LayerView getView() {
		return view;
	}

	public void setView(LayerView view) {
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

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Parents are visited before children.
	 * 
	 * @param visitor
	 */
	public void visit(LayerTreeNodeVisitor visitor) {
		visitor.visit(this);
		if (!isLeaf()) {
			for (LayerTreeNode ltn : children) {
				ltn.visit(visitor);
			}
		}
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

	public void setClientLayerId(String clientLayerId) {
		this.clientLayerId = clientLayerId;
	}

	/**
	 * persistent compare, so will not find persisted vs. non-persisted items. No check on real values, only id.
	 * 
	 * @param clientLayerId
	 * @return
	 */
	public boolean contains(LayerTreeNode node) {
		if (isLeaf()) {
			if (id == 0) {
				return false;
			}
			return (id == node.id);
		} else {
			for (LayerTreeNode child : getChildren()) {
				if (child.contains(node)) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * Does a compare using only clientlayerId.
	 * 
	 * @return
	 */
	public boolean contains(String clientLayerId) {
		if (isLeaf()) {
			return getClientLayerId().equals(clientLayerId);
		} else {
			for (LayerTreeNode node : getChildren()) {
				if (node.contains(clientLayerId)) {
					return true;
				}
			}
			return false;
		}
	}

	// -- convenience methods from View --

	public ScaleInfo getMaximumScale(LayerView parent, LayerModel model, ClientLayerInfo layer) {
		if (view != null) {
			return view.getMaximumScale();
		} else if (parent != null) {
			return parent.getMaximumScale();
		} else if (model != null) {
			return model.getMaxScale();
		} else if (layer != null) {
			return layer.getMaximumScale();
		} else {
			return null;
		}
	}

	public ScaleInfo getMinimumScale(LayerView parent, LayerModel model, ClientLayerInfo layer) {
		if (view != null) {
			return view.getMinimumScale();
		} else if (parent != null) {
			return parent.getMinimumScale();
		} else if (model != null) {
			return model.getMinScale();
		} else if (layer != null) {
			return layer.getMinimumScale();
		} else {
			return null;
		}
	}

	public boolean isShowInLegend(LayerView parent, LayerModel model, ClientLayerInfo layer) {
		if (view != null) {
			return view.isShowInLegend();
		} else if (parent != null) {
			return parent.isShowInLegend();
		} else if (model != null) {
			return model.isShowInLegend();
		} else if (layer != null && getExtraClientLayerInfo(layer) != null) {
			return getExtraClientLayerInfo(layer).isShowInLegend();
		} else {
			return true;
		}
	}

	public Boolean isDefaultVisible(LayerView parent, LayerModel model, ClientLayerInfo layer) {
		if (view != null) {
			return view.isDefaultVisible();
		} else if (parent != null) {
			return parent.isDefaultVisible();
		} else if (model != null) {
			return model.isDefaultVisible();
		} else if (layer != null) {
			if (getExtraClientLayerInfo(layer) != null) {
				return getExtraClientLayerInfo(layer).isActive();
			} else {
				return layer.isVisible();
			}
		} else {
			return null;
		}
	}

	public boolean isPublicLayer() {
		return publicLayer;
	}

	public void setPublicLayer(boolean publicLayer) {
		this.publicLayer = publicLayer;
	}
	
	// -------------------------------------------------
	
	private ExtraClientLayerInfo getExtraClientLayerInfo(ClientLayerInfo cli) {
		if (cli.getUserData() != null && cli.getUserData() instanceof ExtraClientLayerInfo) {
			return (ExtraClientLayerInfo) cli.getUserData();
		}
		return null;
	}
}
