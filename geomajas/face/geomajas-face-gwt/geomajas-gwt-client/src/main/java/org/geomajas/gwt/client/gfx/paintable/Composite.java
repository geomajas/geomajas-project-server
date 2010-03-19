/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client.gfx.paintable;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.gwt.client.gfx.Paintable;
import org.geomajas.gwt.client.gfx.PaintableGroup;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.spatial.Bbox;

/**
 * A <code>Paintable</code> that contains other <code>Paintables</code>,
 * allowing you to create complex <code>Paintable</code>s (composite like pattern).
 *
 * <p>Children are visited (painted) in the order they were added.
 * <p><code>Paintables</code> are not aware of parents, so all positioning is absolute.
 *
 * @author Kristof Heirwegh
 */
public class Composite implements PaintableGroup {

	/**
	 * A preferably unique ID that identifies the object even after it is
	 * painted. This can later be used to update or delete it from the
	 * <code>GraphicsContext</code>.
	 */
	private String groupName;

	protected final List<Paintable> children = new ArrayList<Paintable>();

	public Composite() {
	}

	/**
	 * Construct a composite with the specified name.
	 * 
	 * @param groupName a nice group name for the paintable to use in the DOM, not necessarily unique
	 */
	public Composite(String groupName) {
		this.groupName = groupName;
	}

	// ----------------------------------------------------------

	public void accept(PainterVisitor visitor, Object group, Bbox bounds, boolean recursive) {
		for (Paintable p : children) {
			visitor.visit(p, group);
		}
	}

	/**
	 * Returns a nice name for the group to use in the DOM, not necessarily unique.
	 * 
	 * @return name
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * Adds a <code>Paintable</code> to this composite.
	 *
	 * @param p
	 *            the <code>Paintable</code> to add to this composite
	 */
	public void addChild(Paintable p) {
		if (null == p) {
			throw new IllegalArgumentException("Please provide a paintable");
		}
		if (this.equals(p)) {
			throw new IllegalArgumentException("Cannot add itself as a child");
		}
		if (children.contains(p)) {
			return;
		} // nothing changes, no exception
		if (p instanceof Composite && ((Composite) p).contains(this)) {
			throw new IllegalArgumentException("Cannot add this Paintable (circular reference)");
		}

		children.add(p);
	}

	public boolean removeChild(Paintable p) {
		return children.remove(p);
	}

	/**
	 * Recursively checks children to find p.
	 *
	 * @param p
	 *            Paintable to search for
	 * @return
	 */
	public boolean contains(Paintable p) {
		if (children.contains(p)) {
			return true;
		}

		for (Paintable t : children) {
			if (t instanceof Composite) {
				if (((Composite) t).contains(p)) {
					return true;
				}
			}
		}
		return false;
	}
}
