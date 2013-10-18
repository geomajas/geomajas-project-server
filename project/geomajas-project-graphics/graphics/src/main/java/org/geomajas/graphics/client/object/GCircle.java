/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.graphics.client.object;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.graphics.client.util.FlipState;
import org.vaadin.gwtgraphics.client.VectorObject;
import org.vaadin.gwtgraphics.client.shape.Circle;

/**
 * Graphics circle.
 * 
 * @author Jan De Moerloose
 * 
 */
public class GCircle extends ResizableGraphicsObject {

	public GCircle(double x, double y, double radius, String text) {
		this(new Circle(x, y, radius), text);
	}

	public GCircle(Circle circle, String text) {
		this(new ResizableCircle(circle), text);
	}

	public GCircle(ResizableCircle circle) {
		this(circle, null);
	}
	
	public GCircle(ResizableCircle circle, String text) {
		super(circle, text);
	}

	@Override
	public GraphicsObject cloneObject() {
		ResizableCircle circle = getCircle();
		GCircle clone = new GCircle((ResizableCircle) circle.cloneObject());
		copyTo(clone);
		return clone;
	}

	public ResizableCircle getCircle() {
		return (ResizableCircle) getResizable();
	}

	/**
	 * Resizable implementation for circle.
	 */
	static class ResizableCircle implements Resizable {

		private Circle circle;

		public ResizableCircle(Circle circle) {
			this.circle = circle;
		}

		@Override
		public void setPosition(Coordinate position) {
			circle.setUserX(position.getX());
			circle.setUserY(position.getY());
		}

		@Override
		public Coordinate getPosition() {
			return new Coordinate(circle.getUserX(), circle.getUserY());
		}

		public Object cloneObject() {
			Circle mask = new Circle(circle.getUserX(), circle.getUserY(), circle.getUserRadius());
			return new ResizableCircle(mask);
		}

		@Override
		public void flip(FlipState state) {
			// symmetric
		}

		@Override
		public void setUserBounds(Bbox bounds) {
			Coordinate center = BboxService.getCenterPoint(bounds);
			circle.setUserX(center.getX());
			circle.setUserY(center.getY());
			circle.setUserRadius(bounds.getWidth() / 2);
		}

		@Override
		public boolean isPreserveRatio() {
			return true;
		}

		@Override
		public boolean isAutoHeight() {
			return false;
		}

		@Override
		public Bbox getUserBounds() {
			return new Bbox(circle.getUserX() - circle.getUserRadius(), circle.getUserY() - circle.getUserRadius(),
					2 * circle.getUserRadius(), 2 * circle.getUserRadius());
		}

		@Override
		public Bbox getBounds() {
			return new Bbox(circle.getX() - circle.getRadius(), circle.getY() - circle.getRadius(),
					2 * circle.getRadius(), 2 * circle.getRadius());
		}

		@Override
		public VectorObject asObject() {
			return circle;
		}

	}

}
