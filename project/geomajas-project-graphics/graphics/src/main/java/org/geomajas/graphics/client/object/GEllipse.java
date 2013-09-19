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
import org.vaadin.gwtgraphics.client.shape.Ellipse;

/**
 * Graphics ellipse.
 * 
 * @author Jan De Moerloose
 * 
 */
public class GEllipse extends ResizableGraphicsObject {

	public GEllipse(double userX, double userY, double userRadiusX, double userRadiusY, String text) {
		this(new Ellipse(userX, userY, userRadiusX, userRadiusY), text);
	}

	public GEllipse(Ellipse ellipse, String text) {
		this(new ResizableEllipse(ellipse), text);
	}
	
	public GEllipse(ResizableEllipse ellipse) {
		super(ellipse);
	}

	public GEllipse(ResizableEllipse ellipse, String text) {
		super(ellipse, text);
	}

	@Override
	public GraphicsObject cloneObject() {
		ResizableEllipse ellipse = getEllipse();
		GEllipse clone = new GEllipse((ResizableEllipse) ellipse.cloneObject());
		copyTo(clone);
		return clone;
	}

	public ResizableEllipse getEllipse() {
		return (ResizableEllipse) getResizable();
	}

	/**
	 * Resizable implementation for ellipse.
	 */
	static class ResizableEllipse implements Resizable {

		private Ellipse ellipse;

		public ResizableEllipse(Ellipse ellipse) {
			this.ellipse = ellipse;
		}

		@Override
		public void setPosition(Coordinate position) {
			ellipse.setUserX(position.getX());
			ellipse.setUserY(position.getY());
		}

		@Override
		public Coordinate getPosition() {
			return new Coordinate(ellipse.getUserX(), ellipse.getUserY());
		}		

		public Object cloneObject() {
			Ellipse mask = new Ellipse(ellipse.getUserX(), ellipse.getUserY(), ellipse.getUserRadiusX(),
					ellipse.getUserRadiusY());
			return new ResizableEllipse(mask);
		}

		@Override
		public void flip(FlipState state) {
			// symmetric
		}

		@Override
		public void setUserBounds(Bbox bounds) {
			Coordinate center = BboxService.getCenterPoint(bounds);
			ellipse.setUserX(center.getX());
			ellipse.setUserY(center.getY());
			ellipse.setUserRadiusX(bounds.getWidth() / 2);
			ellipse.setUserRadiusY(bounds.getHeight() / 2);
		}

		@Override
		public boolean isPreserveRatio() {
			return false;
		}

		@Override
		public Bbox getUserBounds() {
			return new Bbox(ellipse.getUserX() - ellipse.getUserRadiusX(), ellipse.getUserY()
					- ellipse.getUserRadiusY(), 2 * ellipse.getUserRadiusX(), 2 * ellipse.getUserRadiusY());
		}

		@Override
		public Bbox getBounds() {
			return new Bbox(ellipse.getX() - ellipse.getRadiusX(), ellipse.getY() - ellipse.getRadiusY(),
					2 * ellipse.getRadiusX(), 2 * ellipse.getRadiusY());
		}

		@Override
		public VectorObject asObject() {
			return ellipse;
		}

	}

}
