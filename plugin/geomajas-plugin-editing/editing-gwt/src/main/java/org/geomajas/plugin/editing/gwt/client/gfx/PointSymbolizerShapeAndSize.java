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

package org.geomajas.plugin.editing.gwt.client.gfx;

/**
 * Defines the shape of the representation of a point during the drawing of an object.
 *
 * @author Jan Venstermans
 */
public class PointSymbolizerShapeAndSize {

	private Shape shape;

	private int size;

	public PointSymbolizerShapeAndSize() {
		this(Shape.SQUARE, 12);
	}

	public PointSymbolizerShapeAndSize(Shape shape, int size) {
		this.shape = shape;
		this.size = size;
	}

	/**
	 * The basic shapes of a point symbolizer.
	 *
	 * @author Jan Venstermans
	 */
	public enum Shape {
		SQUARE, CIRCLE;
	}

	public Shape getShape() {
		return shape;
	}

	public int getSize() {
		return size;
	}

	public void setShape(Shape shape) {
		this.shape = shape;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
