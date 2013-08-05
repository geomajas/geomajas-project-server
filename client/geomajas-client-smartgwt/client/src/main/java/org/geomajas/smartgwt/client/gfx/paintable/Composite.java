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

package org.geomajas.smartgwt.client.gfx.paintable;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.smartgwt.client.gfx.Paintable;
import org.geomajas.smartgwt.client.gfx.PaintableGroup;
import org.geomajas.smartgwt.client.gfx.PainterVisitor;
import org.geomajas.smartgwt.client.spatial.Bbox;

/**
 * A {@link Paintable} that contains other {@link Paintable}s,
 * allowing you to create complex {@link Paintable}s (composite like pattern).
 *
 * <p>Children are visited (painted) in the order they were added.
 * <p>{@link Paintable}s are not aware of parents, so all positioning is absolute.
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
	 * Add a {@link Paintable} to this composite.
	 *
	 * @param p
	 *            the <code>Paintable</code> to add to this composite
	 */
	public void addChild(Paintable p) {
		if (null == p) {
			throw new IllegalArgumentException("Please provide a paintable.");
		}
		if (this.equals(p)) {
			throw new IllegalArgumentException("Cannot add itself as a child.");
		}
		if (children.contains(p)) {
			return;
		} // nothing changes, no exception
		if (p instanceof Composite && ((Composite) p).contains(this)) {
			throw new IllegalArgumentException("Cannot add this Paintable (circular reference).");
		}

		children.add(p);
	}

	/**
	 * Remove child {@link Paintable} object.
	 *
	 * @param p paintable to remove
	 * @return true when object was removed (false when not found as child)
	 */
	public boolean removeChild(Paintable p) {
		return children.remove(p);
	}

	/**
	 * Recursively checks children to find p.
	 *
	 * @param p
	 *            Paintable to search for
	 * @return true when the composite contains the passed object
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
