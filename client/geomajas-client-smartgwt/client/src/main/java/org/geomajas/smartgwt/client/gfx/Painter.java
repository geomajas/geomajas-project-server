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

package org.geomajas.smartgwt.client.gfx;

import org.geomajas.annotation.Api;


/**
 * <p>
 * General interface for painter. Implementations of this class can paint {@link Paintable}
 * objects in a <code>GraphicsContext</code>, or delete them.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api
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
	 * @param group
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
	 * @param group
	 *            The group where the object resides in (optional).
	 * @param context
	 *            The context to paint on.
	 */
	void deleteShape(Paintable paintable, Object group, MapContext context);
}