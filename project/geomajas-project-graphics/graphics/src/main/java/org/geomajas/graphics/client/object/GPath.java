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
import org.geomajas.graphics.client.object.anchor.Anchorable;
import org.geomajas.graphics.client.object.anchor.Anchored;
import org.geomajas.graphics.client.object.anchor.ResizableAnchorer;
import org.geomajas.graphics.client.object.role.Fillable;
import org.geomajas.graphics.client.object.role.Strokable;
import org.geomajas.graphics.client.shape.CoordinatePath;
import org.geomajas.graphics.client.util.FlipState;
import org.vaadin.gwtgraphics.client.VectorObject;

/**
 * Graphics path.
 * 
 * @author Jan De Moerloose
 * 
 */

public class GPath extends ResizableGraphicsObject implements Fillable, Strokable, CoordinateBased, Anchorable {

	public GPath(double x, double y, boolean closed, String text) {
		this(new Coordinate[] { new Coordinate(x, y) }, closed, text);
	}

	public GPath(Coordinate coordinate, boolean closed, String text) {
		this(new Coordinate[] { coordinate }, closed, text);
	}

	public GPath(Coordinate[] coordinates, boolean closed, String text) {
		this(new ResizablePath(coordinates, closed), text);
	}

	public GPath(ResizablePath path) {
		this(path, null);
	}

	public GPath(ResizablePath path, String text) {
		super(path, text);
		addRole(CoordinateBased.TYPE, this);
		addRole(Strokable.TYPE, this);
		addRole(Anchorable.TYPE, this);
		if (path.isClosed()) {
			addRole(Fillable.TYPE, this);
		}
	}

	@Override
	public GraphicsObject cloneObject() {
		ResizablePath path = getPath();
		GPath clone = new GPath((ResizablePath) path.cloneObject());
		copyTo(clone);
		return clone;
	}

	public Coordinate getLastCoordinate() {
		return getPath().getLastCoordinate();
	}

	public void setCoordinates(Coordinate[] coordinates) {
		getPath().setCoordinates(coordinates);
		update();
	}

	public void addCoordinate(Coordinate coordinate) {
		getPath().addCoordinate(coordinate);
		update();
	}

	public void moveCoordinate(Coordinate coordinate, int index) {
		getPath().moveCoordinate(coordinate, index);
		update();
	}

	public int getCoordinateCount() {
		return getPath().getCoordinateCount();
	}

	public Coordinate[] getCoordinates() {
		return getPath().getCoordinates();
	}

	public void setFillColor(String fillColor) {
		getPath().setFillColor(fillColor);
	}

	public void setFillOpacity(double fillOpacity) {
		getPath().setFillOpacity(fillOpacity);
	}

	public String getFillColor() {
		return getPath().getFillColor();
	}

	public double getFillOpacity() {
		return getPath().getFillOpacity();
	}

	public String getStrokeColor() {
		return getPath().getStrokeColor();
	}

	public void setStrokeColor(String strokeColor) {
		getPath().setStrokeColor(strokeColor);
	}

	public int getStrokeWidth() {
		return getPath().getStrokeWidth();
	}

	public void setStrokeWidth(int strokeWidth) {
		getPath().setStrokeWidth(strokeWidth);
	}

	public double getStrokeOpacity() {
		return getPath().getStrokeOpacity();
	}

	public void setStrokeOpacity(double strokeOpacity) {
		getPath().setStrokeOpacity(strokeOpacity);
	}

	private ResizablePath getPath() {
		return (ResizablePath) getResizable();
	}

	/**
	 * Resizable implementation for path.
	 */
	static class ResizablePath implements Resizable, Fillable, Strokable, CoordinateBased {

		private CoordinatePath path;

		ResizablePath(Coordinate[] coordinates, boolean closed) {
			path = new CoordinatePath(coordinates, closed);
		}

		public Coordinate getLastCoordinate() {
			return path.getLastCoordinate();
		}

		public void setCoordinates(Coordinate[] coordinates) {
			path.setCoordinates(coordinates);
		}

		public void addCoordinate(Coordinate coordinate) {
			path.addCoordinate(coordinate);
		}

		public void moveCoordinate(Coordinate coordinate, int index) {
			path.moveCoordinate(coordinate, index);
		}

		public int getCoordinateCount() {
			return path.getCoordinateCount();
		}

		public Coordinate[] getCoordinates() {
			return path.getCoordinates();
		}
		
		public boolean isClosed() {
			return path.isClosed();
		}

		@Override
		public void flip(FlipState state) {
			// TODO flip coordinates and - for FLIP_X, FLIP_Y - reverse coordinate order of closed paths
			switch (state) {
				case FLIP_X:
					break;
				case FLIP_XY:
					break;
				case FLIP_Y:
					break;
				case NONE:
					break;
			}
		}

		@Override
		public boolean isPreserveRatio() {
			return false;
		}

		@Override
		public void setUserBounds(Bbox bounds) {
			path.setUserBounds(bounds);
		}

		public Bbox getUserBounds() {
			return path.getUserbounds();
		}

		@Override
		public Bbox getBounds() {
			return path.getBounds();
		}

		@Override
		public void setPosition(Coordinate position) {
			path.setUserPosition(position);
		}

		@Override
		public Coordinate getPosition() {
			return path.getUserPosition();
		}

		public VectorObject asObject() {
			return path;
		}

		public Object cloneObject() {
			Coordinate[] coordinates = path.getCoordinates();
			Coordinate[] cc = new Coordinate[coordinates.length];
			for (int i = 0; i < coordinates.length; i++) {
				cc[i] = new Coordinate(coordinates[i].getX(), coordinates[i].getY());
			}
			ResizablePath copy = new ResizablePath(cc, path.isClosed());
			return copy;
		}

		@Override
		public void setFillColor(String fillColor) {
			path.setFillColor(fillColor);
		}

		@Override
		public void setFillOpacity(double fillOpacity) {
			path.setFillOpacity(fillOpacity);
		}

		@Override
		public String getFillColor() {
			return path.getFillColor();
		}

		@Override
		public double getFillOpacity() {
			return path.getFillOpacity();
		}

		@Override
		public String getStrokeColor() {
			return path.getStrokeColor();
		}

		@Override
		public void setStrokeColor(String strokeColor) {
			path.setStrokeColor(strokeColor);
		}

		@Override
		public int getStrokeWidth() {
			return path.getStrokeWidth();
		}

		@Override
		public void setStrokeWidth(int strokeWidth) {
			path.setStrokeWidth(strokeWidth);
		}

		@Override
		public double getStrokeOpacity() {
			return path.getStrokeOpacity();
		}

		@Override
		public void setStrokeOpacity(double strokeOpacity) {
			path.setStrokeOpacity(strokeOpacity);
		}

	}

	@Override
	public void addDefaultPath() {
		addRole(new ResizableAnchorer());
		getRole(Anchored.TYPE).setAnchorPosition(new Coordinate(0, 0));
	}

}
