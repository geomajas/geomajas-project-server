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

package org.geomajas.gwt.client.gfx.painter;

import org.geomajas.gwt.client.gfx.MapContext;
import org.geomajas.gwt.client.gfx.Paintable;
import org.geomajas.gwt.client.gfx.Painter;
import org.geomajas.gwt.client.gfx.paintable.Image;

/**
 * <p>
 * Painter implementation for images.
 * </p>
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 */
public class ImagePainter implements Painter {

	/**
	 * Return the class-name of the type of object this painter can paint.
	 * 
	 * @return Return the class-name as a string.
	 */
	public String getPaintableClassName() {
		return Image.class.getName();
	}

	/**
	 * The actual painting function. Draws the images with the object's id.
	 * 
	 * @param paintable
	 *            A {@link org.geomajas.gwt.client.gfx.paintable.Image} object.
	 * @param group
	 *            The group where the object resides in (optional).
	 * @param context
	 *            A MapContext object, responsible for actual drawing.
	 */
	public void paint(Paintable paintable, Object group, MapContext context) {
		Image image = (Image) paintable;
		context.getVectorContext().drawImage(group, image.getId(), image.getHref(), image.getBounds(),
				image.getStyle());
	}

	/**
	 * Delete a {@link Paintable} object from the given {@link MapContext}. It the object does not exist,
	 * nothing will be done.
	 * 
	 * @param paintable
	 *            The object to be painted.
	 * @param group
	 *            The group where the object resides in (optional).
	 * @param context
	 *            The context to paint on.
	 */
	public void deleteShape(Paintable paintable, Object group, MapContext context) {
		Image image = (Image) paintable;
		context.getVectorContext().deleteElement(group, image.getId());
	}
}
