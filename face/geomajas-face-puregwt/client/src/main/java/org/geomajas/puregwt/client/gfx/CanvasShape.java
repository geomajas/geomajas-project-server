/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.puregwt.client.gfx;

/**
 * An object that is able to paint itself on a canvas.
 */
import com.google.gwt.canvas.client.Canvas;

/**
 * Canvas shape that knows how to render itself.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface CanvasShape {

	/**
	 * Called when the shape should be (re)painted.
	 * 
	 * @param canvas the canvas
	 */
	void paint(Canvas canvas);
}
