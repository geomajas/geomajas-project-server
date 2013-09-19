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
package org.geomajas.graphics.client.shape;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.service.BboxService;
import org.vaadin.gwtgraphics.client.Shape;
import org.vaadin.gwtgraphics.client.VectorObject;
import org.vaadin.gwtgraphics.client.shape.Path;
import org.vaadin.gwtgraphics.client.shape.path.ClosePath;
import org.vaadin.gwtgraphics.client.shape.path.LineTo;
import org.vaadin.gwtgraphics.client.shape.path.MoveTo;
import org.vaadin.gwtgraphics.client.shape.path.PathStep;
import org.vaadin.gwtgraphics.client.shape.path.ScaleHelper;

/**
 * Path based on coordinate array.
 * 
 * @author Jan De Moerloose
 * 
 */
public class CoordinatePath extends Shape {

	private List<PathStep> steps = new ArrayList<PathStep>();

	private boolean closed;

	private Coordinate[] coordinates;

	private Bbox bounds;

	public CoordinatePath(boolean closed) {
		this(new Coordinate[] { new Coordinate(), new Coordinate() }, closed);
	}

	public CoordinatePath(Coordinate[] coordinates, boolean closed) {
		this.closed = closed;
		if (!closed) {
			setFillOpacity(0);
		}
		setCoordinates(coordinates);
	}

	@Override
	protected Class<? extends VectorObject> getType() {
		return Path.class;
	}

	public void setCoordinates(Coordinate[] coordinates) {
		this.coordinates = coordinates;
		updateCoordinates();
	}

	public Coordinate[] getCoordinates() {
		return coordinates;
	}

	public Coordinate getLastCoordinate() {
		return (Coordinate) coordinates[coordinates.length - 1].clone();
	}

	public void addCoordinate(Coordinate coordinate) {
		Coordinate[] newCoords = new Coordinate[coordinates.length + 1];
		System.arraycopy(coordinates, 0, newCoords, 0, coordinates.length);
		newCoords[coordinates.length] = coordinate;
		setCoordinates(newCoords);
	}

	public void moveCoordinate(Coordinate coordinate, int index) {
		if (index < coordinates.length) {
			coordinates[index] = (Coordinate) coordinate.clone();
		}
		setCoordinates(coordinates);
	}

	public int getCoordinateCount() {
		return coordinates.length;
	}

	public void setUserPosition(Coordinate position) {
		double x = coordinates[0].getX();
		double y = coordinates[0].getY();
		double dX = position.getX() - x;
		double dY = position.getY() - y;
		for (int i = 0; i < coordinates.length; i++) {
			coordinates[i].setX(coordinates[i].getX() + dX);
			coordinates[i].setY(coordinates[i].getY() + dY);
		}
		setCoordinates(coordinates);
	}

	public Coordinate getUserPosition() {
		return (Coordinate) coordinates[0].clone();
	}

	public void setUserBounds(Bbox bounds) {
		double width = this.bounds.getWidth();
		double height = this.bounds.getHeight();

		double newWidth = bounds.getWidth();
		double newHeight = bounds.getHeight();

		double scaleX = width == 0 ? 1 : newWidth / width;
		double scaleY = height == 0 ? 1 : newHeight / height;

		double x = this.bounds.getX();
		double y = this.bounds.getY();

		double newX = bounds.getX();
		double newY = bounds.getY();

		for (int i = 0; i < coordinates.length; i++) {
			coordinates[i].setX(newX + scaleX * (coordinates[i].getX() - x));
			coordinates[i].setY(newY + scaleY * (coordinates[i].getY() - y));
		}
		updateCoordinates();
	}

	private void updateCoordinates() {
		this.bounds = calcBounds();
		this.steps = calcSteps();
		setUserX(coordinates[0].getX());
		setUserY(coordinates[0].getX());
		drawTransformed();
	}

	public Bbox getUserbounds() {
		return this.bounds;
	}

	public Bbox getBounds() {
		// transform all points and calculate new bounds
		double x1 = getUserbounds().getX() * getScaleX() + getDeltaX();
		double y1 = getUserbounds().getY() * getScaleY() + getDeltaY();
		double x2 = (getUserX() + getUserbounds().getWidth()) * getScaleX() + getDeltaX();
		double y2 = (getUserY() + getUserbounds().getHeight()) * getScaleY() + getDeltaY();
		return new Bbox(Math.round(Math.min(x1, x2)), Math.round(Math.min(y1, y2)), Math.abs(x1 - x2),
				Math.abs(y1 - y2));
	}

	public boolean isClosed() {
		return closed;
	}

	protected List<PathStep> calcSteps() {
		List<PathStep> result = new ArrayList<PathStep>();
		Coordinate prev = coordinates[0];
		result.add(new MoveTo(false, prev.getX(), prev.getY()));
		for (int i = 1; i < coordinates.length; i++) {
			Coordinate next = coordinates[i];
			result.add(new LineTo(true, next.getX() - prev.getX(), next.getY() - prev.getY()));
			prev = next;
		}
		if (closed) {
			result.add(new ClosePath());
		}
		return result;
	}

	protected Bbox calcBounds() {
		Coordinate c = coordinates[0];
		Bbox result = new Bbox(c.getX(), c.getY(), 0, 0);
		for (int i = 1; i < coordinates.length; i++) {
			c = coordinates[i];
			result = BboxService.union(result, new Bbox(c.getX(), c.getY(), 0, 0));
		}
		return result;
	}

	protected void drawTransformed() {
		// apply translation
		MoveTo moveTo = (MoveTo) steps.get(0);
		steps.set(0, new MoveTo(moveTo.isRelativeCoords(), moveTo.getUserX() + getDeltaX(), moveTo.getUserY()
				+ getDeltaY()));
		// apply scale
		ScaleHelper scaleHelper = new ScaleHelper(getScaleX(), getScaleY());
		for (PathStep step : steps) {
			step.scale(scaleHelper);
		}
		getImpl().drawPath(getElement(), steps);
	}

}
