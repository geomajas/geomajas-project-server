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

import org.geomajas.gwt.client.gfx.WorldPaintable;
import org.geomajas.gwt.client.gfx.style.Style;

/**
 * <p>
 * Abstraction of a <code>Paintable</code> object that has the ability to also be drawn in world space. Extensions of
 * this abstract class have the ability to be drawn in screen space as well as world space.
 * </p>
 * <p>
 * See the {@link WorldPaintable} interface for more information on how to draw objects in world space. This abstract
 * implementation, facilitates the scaling of the style objects, so that for example the stroke-widths remain constant
 * even while zooming in and out.
 * </p>
 * 
 * @author Pieter De Graef
 */
public abstract class AbstractWorldPaintable implements WorldPaintable {

	private String id;

	private Style original;

	private Style scaled;

	private boolean compensatingForScale;

	// -------------------------------------------------------------------------
	// Package visible constructor:
	// -------------------------------------------------------------------------

	/**
	 * Package visible constructor that requires you to immediately set the ID.
	 * 
	 * @param parent
	 *            parent group's object  
	 * @param id
	 *            A preferably unique ID that identifies the object even after it is painted. This can later be used to
	 *            update or delete it from the <code>GraphicsContext</code>.
	 */
	AbstractWorldPaintable(String id) {
		this.id = id;
	}

	// -------------------------------------------------------------------------
	// WorldPaintable implementation:
	// -------------------------------------------------------------------------

	/**
	 * <p>
	 * Scale the object. The scale parameter will usually be the opposite of the <code>MapView</code> scale. By default
	 * only the style object will be scaled.
	 * </p>
	 * <p>
	 * This method will created a clone from the original style, and scale that. This is only done when
	 * <code>compensatingForScale</code> is set to <code>true</code>, otherwise this method does nothing.
	 * </p>
	 * 
	 * @param scale
	 *            The new scale factor to be applied on the style object.
	 */
	public void scale(double scale) {
		if (compensatingForScale) {
			scaled = original.clone();
			scaled.scale(scale);
		}
	}

	/** Returns the boolean value that determines whether or not the scaling should happen. */
	public boolean isCompensatingForScale() {
		return compensatingForScale;
	}

	/**
	 * Boolean value that determines whether or not the scaling should happen.
	 * 
	 * @param compensatingForScale
	 *            Set a new value.
	 */
	public void setCompensatingForScale(boolean compensatingForScale) {
		this.compensatingForScale = compensatingForScale;
	}

	/**
	 * Returns a preferably unique ID that identifies the object even after it is painted. This can later be used to
	 * update or delete it from the <code>GraphicsContext</code>.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Return the currently valid scale. When drawing in screen space or when not compensating for scale, this will
	 * always return the original style. But when compensating for scale, and drawing in world space, this method will
	 * return a scaled style object.
	 */
	public Style getStyle() {
		if (compensatingForScale) {
			return scaled;
		}
		return original;
	}

	// -------------------------------------------------------------------------
	// Protected methods:
	// -------------------------------------------------------------------------

	/**
	 * A protected method that allows you to set the original style. This original will never be touched. It will be
	 * used on the other hand to create clones as scaled style objects.
	 */
	protected void setOriginalStyle(Style original) {
		this.original = original;
		this.scaled = original.clone();
	}
}
