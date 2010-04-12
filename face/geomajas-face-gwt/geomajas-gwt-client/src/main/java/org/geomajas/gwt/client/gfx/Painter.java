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


/**
 * <p>
 * General interface for painter. Implementations of this class can paint <code>Paintable</code> objects in a
 * <code>GraphicsContext</code>, or delete them.
 * </p>
 * 
 * @author Pieter De Graef
 */
public interface Painter {

	/**
	 * Return the class-name of the type of object this painter can paint.
	 * 
	 * @return Return the class-name as a string.
	 */
	String getPaintableClassName();

	/**
	 * Paint a <code>Paintable</code> object on the given <code>GraphicsContext</code>. It the object already exists, it
	 * will be updated.
	 * 
	 * @param paintable
	 *            The object to be painted.
	 * @param Object
	 *            The group to paint in.
	 * @param context
	 *            The context to paint on.
	 */
	void paint(Paintable paintable, Object group, MapContext context);

	/**
	 * Delete a <code>Paintable</code> object from the given <code>GraphicsContext</code>. It the object does not exist,
	 * nothing will be done.
	 * 
	 * @param paintable
	 *            The object to be deleted.
	 * @param Object
	 *            The group where the object resides in (optional).
	 * @param context
	 *            The context to paint on.
	 */
	void deleteShape(Paintable paintable, Object group, MapContext context);
}