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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
@Entity
@Table(name = "config_layertrees")
public class LayerTree implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne()
	@Cascade(CascadeType.ALL)
	@JoinColumn(name = "rootNode_id")
	private LayerTreeNode rootNode = new LayerTreeNode("[ROOT]");

	// ----------------------------------------------------------

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LayerTreeNode getRootNode() {
		return rootNode;
	}

	public void setRootNode(LayerTreeNode rootNode) {
		this.rootNode = rootNode;
	}

	public void visitNodes(LayerTreeNodeVisitor visitor) {
		rootNode.visit(visitor);
	}

	// ----------------------------------------------------------

	/**
	 * TODO.
	 * 
	 * @author Jan De Moerloose
	 *
	 */
	public interface LayerTreeNodeVisitor {

		void visit(LayerTreeNode node);
	}

}
