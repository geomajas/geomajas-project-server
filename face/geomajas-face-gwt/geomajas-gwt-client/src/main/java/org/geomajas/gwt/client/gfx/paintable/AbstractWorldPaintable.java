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

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.gfx.WorldPaintable;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.WorldViewTransformer;
import org.geomajas.gwt.client.spatial.geometry.Geometry;

/**
 * <p>
 * Abstraction of a <code>Paintable</code> object that has the ability to also be drawn in world space. Extensions of
 * this abstract class have the ability to be drawn in screen space as well as world space.
 * </p>
 * <p>
 * See the {@link WorldPaintable} interface for more information on how to draw objects in world space. This abstract
 * implementation, facilitates the transformation of the location objects, while still storing the original location
 * object.
 * </p>
 * 
 * @author Pieter De Graef
 */
public abstract class AbstractWorldPaintable implements WorldPaintable {

	private String id;

	protected Object original;

	protected Object transformed;

	// -------------------------------------------------------------------------
	// Package visible constructor:
	// -------------------------------------------------------------------------

	/**
	 * Package visible constructor that requires you to immediately set the ID.
	 * 
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
	 * Returns a preferably unique ID that identifies the object even after it is painted. This can later be used to
	 * update or delete it from the <code>GraphicsContext</code>.
	 */
	public String getId() {
		return id;
	}

	/** Get the object that represents the original location (usually a coordinate, bbox or geometry). */
	public Object getOriginalLocation() {
		return original;
	}

	/**
	 * Perform a transformation from world space to pan space, so the location object can than be shown on the map,
	 * using the pan group. Supports only {@link Coordinate}, {@link Bbox} and {@link Geometry} classes as location
	 * object.
	 * 
	 * @param transformer
	 *            The map's transformer.
	 */
	public void transform(WorldViewTransformer transformer) {
		if (original != null) {
			if (original instanceof Coordinate) {
				transformed = transformer.worldToPan((Coordinate) original);
			} else if (original instanceof Bbox) {
				transformed = transformer.worldToPan((Bbox) original);
			} else if (original instanceof Geometry) {
				transformed = transformer.worldToPan((Geometry) original);
			}
		}
	}

	/** Set a new original location object. Also resets the transformed location to null. */
	protected void setOriginalLocation(Object location) {
		original = location;
		transformed = null;
	}

	/** Return the general location object. Preferably the transformed one, but if that is null, return the original. */
	protected Object getLocation() {
		if (transformed != null) {
			return transformed;
		}
		return original;
	}
}
