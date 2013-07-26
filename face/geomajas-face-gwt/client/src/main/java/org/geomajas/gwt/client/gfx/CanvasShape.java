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
package org.geomajas.gwt.client.gfx;

/**
 * An object that is able to paint itself on a canvas.
 */
import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Matrix;

import com.google.gwt.canvas.client.Canvas;

/**
 * Canvas shape that knows how to render itself.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface CanvasShape {

	/**
	 * Called when the shape should be (re)painted. Painting can be done in world coordinates directly.
	 * 
	 * @param canvas
	 *            the canvas
	 * @param matrix
	 *            the matrix of the current world to screen transformation
	 */
	void paint(Canvas canvas, Matrix matrix);

	/**
	 * Returns the bounds of the shape in world coordinates.
	 * 
	 * @return bounds
	 */
	Bbox getBounds();
}