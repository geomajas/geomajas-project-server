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

package org.geomajas.gwt.client.gfx;

import org.geomajas.gwt.client.gfx.style.Style;

/**
 * <p>
 * A {@link Paintable} object that has the ability to also be drawn in world space (next to screen space). For objects
 * in world space, it is important that they have the ability to scale their styles, as stroke-widths for example, may
 * become completely invisible when zooming out. Since this effect may not always be wanted, this interface allows you
 * to set it as an option.
 * </p>
 * <p>
 * Also to effectively draw your objects in world space, and have them move about automatically when navigating, you
 * must not only render them, but also add them to the <code>MapModel</code>. This <code>MapModel</code> will apply the
 * correct transformation matrix on the parent group of all world space objects, so they are drawn at the correct
 * location.
 * </p>
 * 
 * @author Pieter De Graef
 */
public interface WorldPaintable extends Paintable {

	/**
	 * Retrieve the style to be used for this particular instance. When the <code>MapModel</code> transforms the
	 * <code>WorldPaintable</code>s, it may also scale it's style object (i.e. to keep stroke-width constant).
	 */
	Style getStyle();

	/**
	 * Scale the object. The scale parameter will usually be the opposite of the <code>MapView</code> scale. By default
	 * only the style object will be scaled. If you really want to scale the entire object, consider simply drawing in
	 * screen space.
	 * 
	 * @param scale
	 *            The new scale factor to be applied on this object.
	 */
	void scale(double scale);

	/**
	 * Boolean value that determines whether or not the scaling should happen.
	 * 
	 * @param compensatingForScale
	 *            Set a new value.
	 */
	void setCompensatingForScale(boolean compensatingForScale);

	/** Returns the boolean value that determines whether or not the scaling should happen. */
	boolean isCompensatingForScale();
}
