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

/**
 * @author Kristof Heirwegh
 */
public class LayerTreeDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;

	private LayerTreeNodeDto rootNode = new LayerTreeNodeDto("[ROOT]", false);

	// ----------------------------------------------------------

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LayerTreeNodeDto getRootNode() {
		return rootNode;
	}

	public void setRootNode(LayerTreeNodeDto rootNode) {
		this.rootNode = rootNode;
	}

	public boolean containsNonPublicLayers() {
		if (rootNode != null) {
			Boolean res = rootNode.search(new LayerTreeNodeSearchVisitor<Boolean>() {

				public Boolean search(LayerTreeNodeDto node) {
					if (node.isLeaf() && !node.isPublicLayer()) {
						return Boolean.TRUE;
					}
					return null;
				}
			});
			if (res != null) {
				return res;
			}
		}
		return false;
	}

	// ----------------------------------------------------------

	/**
	 * TODO.
	 * 
	 * @author Jan De Moerloose
	 *
	 */
	public interface LayerTreeNodeVisitor {

		void visit(LayerTreeNodeDto node);
	}

	/**
	 * TODO.
	 * 
	 * @author Jan De Moerloose
	 *
	 */
	public interface LayerTreeNodeSearchVisitor<T> {

		T search(LayerTreeNodeDto node);
	}
}
