/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.editing.client.service;

import org.geomajas.annotation.Api;

/**
 * Definition of an index in a geometry. This index will point to a specific sub-part of a geometry. Depending on the
 * "type", this sub-part can be a vertex, an edge or a sub-geometry.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class GeometryIndex {

	private GeometryIndexType type;

	private GeometryIndex child;

	private int value;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/** Don't use this. Use the index service instead. */
	public GeometryIndex() {
	}

	protected GeometryIndex(GeometryIndexType type, int value, GeometryIndex child) {
		this.type = type;
		this.value = value;
		this.child = child;
	}

	protected GeometryIndex(GeometryIndex other) {
		type = other.getType();
		value = other.getValue();
		if (other.hasChild()) {
			child = new GeometryIndex(other.getChild());
		}
	}

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	protected void setChild(GeometryIndex child) {
		this.child = child;
	}

	/**
	 * Get the type of sub-part this index points to. Can be a vertex, edge or sub-geometry.
	 * 
	 * @return The type of sub-part this index points to.
	 */
	public GeometryIndexType getType() {
		return type;
	}

	/**
	 * Does this index have a child index or not? If this index points to a sub-geometry, and a child may point to some
	 * part within the sub-geometry. Recursiveness rules the world.
	 * 
	 * @return true or false.
	 */
	public boolean hasChild() {
		return child != null;
	}

	/**
	 * Get the child index. If this index points to a sub-geometry, and a child may point to some part within the
	 * sub-geometry.
	 * 
	 * @return Returns the child index, or null if there is no child.
	 */
	public GeometryIndex getChild() {
		return child;
	}

	/**
	 * Get the index value for this index. This value tells us to exactly which vertex/edge/sub-geometry we are
	 * pointing. Vertices and geometries just point to the index in the respective arrays in a geometry, while edges
	 * point to the edge after the vertex with the same index value (edge 0 has coordinate 0 and 1).
	 * 
	 * @return The integer index value.
	 */
	public int getValue() {
		return value;
	}

	/** {@inheritDoc} */
	public boolean equals(Object other) {
		if (other == null || !(other instanceof GeometryIndex)) {
			return false;
		}
		GeometryIndex index = (GeometryIndex) other;
		if (hasChild() && index.hasChild()) {
			return getChild().equals(index.getChild()) && type == index.getType() && value == index.getValue();
		}
		return type == index.getType() && value == index.getValue() && hasChild() == index.hasChild();
	}

	/** {@inheritDoc} */
	public int hashCode() {
		if (hasChild()) {
			return (getChild().hashCode() + value) * type.hashCode();
		}
		return (37 + value) * type.hashCode();
	}

	/** {@inheritDoc} */
	public String toString() {
		if (child != null) {
			return type.toString() + "-" + value + " / " + child.toString();
		}
		return type.toString() + "-" + value;
	}
}