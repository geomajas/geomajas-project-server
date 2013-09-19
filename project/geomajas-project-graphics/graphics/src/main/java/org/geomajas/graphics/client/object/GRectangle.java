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
import org.geomajas.graphics.client.object.role.Fillable;
import org.geomajas.graphics.client.object.role.Strokable;
import org.geomajas.graphics.client.util.FlipState;
import org.vaadin.gwtgraphics.client.VectorObject;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

/**
 * Graphics rectangle.
 * 
 * @author Jan De Moerloose
 * 
 */
public class GRectangle extends ResizableGraphicsObject implements Fillable, Strokable {

	public GRectangle(double userX, double userY, double width, double height, String text) {
		this(new Rectangle(userX, userY, width, height), text);
	}

	public GRectangle(Rectangle rectangle, String text) {
		this(new ResizableRectangle(rectangle), text);
	}

	public GRectangle(ResizableRectangle rectangle) {
		this(rectangle, null);
	}

	public GRectangle(ResizableRectangle rectangle, String text) {
		super(rectangle, text);
		addRole(Fillable.TYPE, this);
		addRole(Strokable.TYPE, this);
	}

	@Override
	public GraphicsObject cloneObject() {
		ResizableRectangle rectangle = getRectangle();
		GRectangle clone = new GRectangle((ResizableRectangle) rectangle.cloneObject());
		copyTo(clone);
		return clone;
	}

	public String getFillColor() {
		return getRectangle().getFillColor();
	}

	public void setFillColor(String color) {
		getRectangle().setFillColor(color);
	}

	public double getFillOpacity() {
		return getRectangle().getFillOpacity();
	}

	public void setFillOpacity(double opacity) {
		getRectangle().setFillOpacity(opacity);
	}

	public String getStrokeColor() {
		return getRectangle().getStrokeColor();
	}

	public void setStrokeColor(String color) {
		getRectangle().setStrokeColor(color);
	}

	public int getStrokeWidth() {
		return getRectangle().getStrokeWidth();
	}

	public void setStrokeWidth(int width) {
		getRectangle().setStrokeWidth(width);
	}

	public double getStrokeOpacity() {
		return getRectangle().getStrokeOpacity();
	}

	public void setStrokeOpacity(double opacity) {
		getRectangle().setStrokeOpacity(opacity);
	}

	private ResizableRectangle getRectangle() {
		return (ResizableRectangle) getResizable();
	}

	/**
	 * Resizable implementation for rectangle.
	 */
	static class ResizableRectangle implements Resizable, Fillable, Strokable {

		private Rectangle rectangle;

		public ResizableRectangle(Rectangle rectangle) {
			this.rectangle = rectangle;
		}

		@Override
		public void setPosition(Coordinate position) {
			rectangle.setUserX(position.getX());
			rectangle.setUserY(position.getY());
		}

		@Override
		public Coordinate getPosition() {
			return new Coordinate(rectangle.getUserX(), rectangle.getUserY());
		}

		public Object cloneObject() {
			Rectangle mask = new Rectangle(rectangle.getUserX(), rectangle.getUserY(), rectangle.getUserWidth(),
					rectangle.getUserHeight());
			return new ResizableRectangle(mask);
		}

		@Override
		public void flip(FlipState state) {
			// symmetric
		}

		@Override
		public void setUserBounds(Bbox bounds) {
			rectangle.setUserX(bounds.getX());
			rectangle.setUserY(bounds.getY());
			rectangle.setUserWidth(bounds.getWidth());
			rectangle.setUserHeight(bounds.getHeight());
		}

		@Override
		public boolean isPreserveRatio() {
			return false;
		}

		@Override
		public Bbox getUserBounds() {
			return new Bbox(rectangle.getUserX(), rectangle.getUserY(), rectangle.getUserWidth(),
					rectangle.getUserHeight());
		}

		@Override
		public Bbox getBounds() {
			return new Bbox(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
		}

		@Override
		public VectorObject asObject() {
			return rectangle;
		}

		public String getFillColor() {
			return rectangle.getFillColor();
		}

		public void setFillColor(String color) {
			rectangle.setFillColor(color);
		}

		public double getFillOpacity() {
			return rectangle.getFillOpacity();
		}

		public void setFillOpacity(double opacity) {
			rectangle.setFillOpacity(opacity);
		}

		public String getStrokeColor() {
			return rectangle.getStrokeColor();
		}

		public void setStrokeColor(String color) {
			rectangle.setStrokeColor(color);
		}

		public int getStrokeWidth() {
			return rectangle.getStrokeWidth();
		}

		public void setStrokeWidth(int width) {
			rectangle.setStrokeWidth(width);
		}

		public double getStrokeOpacity() {
			return rectangle.getStrokeOpacity();
		}

		public void setStrokeOpacity(double opacity) {
			rectangle.setStrokeOpacity(opacity);
		}

	}
}
