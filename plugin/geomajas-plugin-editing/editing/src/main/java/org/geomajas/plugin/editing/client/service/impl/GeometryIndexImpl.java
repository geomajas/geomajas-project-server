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

package org.geomajas.plugin.editing.client.service.impl;

import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;

/**
 * Definition of an index in a geometry. This index will point to a specific sub-part of a geometry. Depending on the
 * "type", this sub-part can be a vertex, an edge or a sub-geometry.
 * 
 * @author Pieter De Graef
 */
public class GeometryIndexImpl implements GeometryIndex {

	private GeometryIndexType type;

	private GeometryIndex child;

	private int value;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	protected GeometryIndexImpl(GeometryIndexType type, int value, GeometryIndex child) {
		this.type = type;
		this.value = value;
		this.child = child;
	}

	protected GeometryIndexImpl(GeometryIndex other) {
		type = other.getType();
		value = other.getValue();
		if (other.hasChild()) {
			child = new GeometryIndexImpl(other.getChild());
		}
	}

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	protected void setChild(GeometryIndex child) {
		this.child = child;
	}

	public boolean equals(Object other) {
		GeometryIndex index = (GeometryIndex) other;
		if (hasChild() && index.hasChild()) {
			return getChild().equals(index.getChild()) && type == index.getType() && value == index.getValue();
		}
		return type == index.getType() && value == index.getValue() && hasChild() == index.hasChild();
	}
	
	public int hashCode() {
		if (hasChild()) {
			return (getChild().hashCode() + value) * type.hashCode();
		}
		return (37 + value) * type.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.editing.client.service.GeometryIndex#getType()
	 */
	public GeometryIndexType getType() {
		return type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.editing.client.service.GeometryIndex#hasChild()
	 */
	public boolean hasChild() {
		return child != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.editing.client.service.GeometryIndex#getChild()
	 */
	public GeometryIndex getChild() {
		return child;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.editing.client.service.GeometryIndex#getValue()
	 */
	public int getValue() {
		return value;
	}
}