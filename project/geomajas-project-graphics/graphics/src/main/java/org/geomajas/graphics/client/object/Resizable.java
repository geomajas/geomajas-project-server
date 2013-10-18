/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.graphics.client.object;

import org.geomajas.geometry.Bbox;
import org.geomajas.graphics.client.object.role.RoleType;
import org.geomajas.graphics.client.util.FlipState;

/**
 * Implemented by resizable graphics objects. Resizing can include flipping if the resizing handle is "folded".
 * 
 * @author Jan De Moerloose
 * 
 */
public interface Resizable extends Draggable {

	RoleType<Resizable> TYPE = new RoleType<Resizable>("Resizable");

	/**
	 * Flip the object according to the specified state.
	 * 
	 * @param state
	 */
	void flip(FlipState state);

	/**
	 * Set the bounds in user space.
	 * 
	 * @param bounds
	 *            the new bounds
	 */
	void setUserBounds(Bbox bounds);

	/**
	 * @see #setUserBounds(Bbox)
	 * @return the bounds
	 */
	Bbox getUserBounds();

	/**
	 * Get the bounds in screen space.
	 * 
	 * @return the bounds in screen space
	 */
	Bbox getBounds();

	/**
	 * Should the width/height ratio be preserved when resizing this object ?
	 * 
	 * @return true if preserved, false otherwise
	 */
	boolean isPreserveRatio();

	/**
	 * Should the object calculate its own height ?
	 * 
	 * @return true if height is automatic.
	 */
	boolean isAutoHeight();

}
