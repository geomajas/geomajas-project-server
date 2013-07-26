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

import java.util.List;

import org.geomajas.annotation.Api;
import org.vaadin.gwtgraphics.client.Transformable;
import org.vaadin.gwtgraphics.client.Transparent;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Container of {@link CanvasShape} shape objects. The container draws the shapes on an HTML5 canvas. It supports
 * adding/removing shapes and transformations.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface CanvasContainer extends Transformable, Transparent, IsWidget {

	/**
	 * Adds a shape to the container.
	 * 
	 * @param shape
	 */
	void addShape(CanvasShape shape);

	/**
	 * Adds multiple shapes to the container.
	 * 
	 * @param shapes
	 */
	void addAll(List<CanvasShape> shapes);

	/**
	 * Removes a shape from the container.
	 * 
	 * @param shape
	 */
	void removeShape(CanvasShape shape);

	/** Removes all shapes from the container. */
	void clear();

	/** Forces repaint. */
	void repaint();

	/**
	 * Sets the size in pixels of this container. Coordinate space width/height is also set equal to width/height.
	 * 
	 * @param width
	 * @param height
	 */
	void setPixelSize(int width, int height);
}