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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
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
 * Path based on a coordinate array. Can be used to represent a geometry.
 * 
 * @author Jan De Moerloose
 * 
 */
public class GeometryPath extends Shape {

	private List<PathStep> steps = new ArrayList<PathStep>();

	private boolean closed;

	private Coordinate[] coordinates;

	private List<SubPath> subPaths = new ArrayList<SubPath>();

	private Bbox bounds;

	private boolean skipTransform;

	/**
	 * Create a zero-length path at (0,0).
	 * 
	 * @param closed if true, the path is closed or a ring, else the path is open or a line.
	 */
	public GeometryPath(boolean closed) {
		this(new Coordinate[] { new Coordinate(), new Coordinate() }, closed);
	}

	/**
	 * Create a path with these coordinates.
	 * 
	 * @param coordinates
	 * @param closed if true, path is closed
	 */
	public GeometryPath(Coordinate[] coordinates, boolean closed) {
		super(0, 0);
		this.closed = closed;
		if (!closed) {
			setFillOpacity(0);
		}
		setCoordinates(coordinates);
	}

	/**
	 * Create a path with this Geometry.
	 * 
	 * @param coordinates
	 * @param closed if true, path is closed
	 */
	public GeometryPath(Geometry geometry) {
		super(0, 0);
		setGeometry(geometry);
	}

	public void setGeometry(Geometry geometry) {
		closed = Geometry.POLYGON.equals(geometry.getGeometryType())
				|| Geometry.MULTI_POLYGON.equals(geometry.getGeometryType());
		skipTransform = true;
		try {
			if (Geometry.LINE_STRING.equals(geometry.getGeometryType())) {
				setLineString(geometry);
			} else if (Geometry.LINEAR_RING.equals(geometry.getGeometryType())) {
				setLinearRing(geometry);
			} else if (Geometry.POLYGON.equals(geometry.getGeometryType())) {
				setPolygon(geometry);
			} else if (Geometry.MULTI_LINE_STRING.equals(geometry.getGeometryType())) {
				setMultiLineString(geometry);
			} else if (Geometry.MULTI_POLYGON.equals(geometry.getGeometryType())) {
				setMultiPolygon(geometry);
			} else {
				throw new IllegalArgumentException("Unsupported geometry " + geometry.getGeometryType());
			}
		} finally {
			skipTransform = false;
			drawTransformed();
		}
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

	/**
	 * Add a new sub-path to the path. A path can have multiple sub-paths. Sub-paths may be disconnected lines or rings
	 * but, thanks to even-odd rule, holes can be added as well.
	 * 
	 * @param coordinates
	 * @param closed
	 */
	public void addSubPath(Coordinate[] coordinates, boolean closed) {
		subPaths.add(new SubPath(coordinates, closed));
		updateCoordinates();

	}

	/**
	 * Add a coordinate to the path.
	 * 
	 * @param coordinate
	 */
	public void addCoordinate(Coordinate coordinate) {
		Coordinate[] newCoords = new Coordinate[coordinates.length + 1];
		System.arraycopy(coordinates, 0, newCoords, 0, coordinates.length);
		newCoords[coordinates.length] = coordinate;
		setCoordinates(newCoords);
	}

	/**
	 * Move the coordinate at the specified index.
	 * 
	 * @param coordinate the new coordinate
	 * @param index
	 */
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
		translateCoordinates(coordinates, dX, dY);
		for (SubPath s : subPaths) {
			translateCoordinates(s.getCoordinates(), dX, dY);
		}
		setCoordinates(coordinates);
		super.setUserX(x);
		super.setUserY(y);
	}

	@Override
	public void setUserX(double userX) {
		// called by Shape constructor !
		if (coordinates != null) {
			setUserPosition(new Coordinate(userX, getUserPosition().getY()));
		}
	}

	@Override
	public void setUserY(double userY) {
		// called by Shape constructor !
		if (coordinates != null) {
			setUserPosition(new Coordinate(getUserPosition().getX(), userY));
		}
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

		scaleCoordinates(coordinates, scaleX, scaleY, x, y, newX, newY);
		for (SubPath s : subPaths) {
			scaleCoordinates(s.getCoordinates(), scaleX, scaleY, x, y, newX, newY);
		}
		updateCoordinates();
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

	private void updateCoordinates() {
		this.bounds = calcBounds();
		this.steps = calcSteps();
		if (!skipTransform) {
			drawTransformed();
		}
	}

	protected List<PathStep> calcSteps() {
		List<PathStep> result = calcSteps(coordinates, closed);
		for (SubPath path : subPaths) {
			result.addAll(calcSteps(path.getCoordinates(), path.isClosed()));
		}
		return result;
	}

	private List<PathStep> calcSteps(Coordinate[] coordinates, boolean closed) {
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
		Bbox result = calcBounds(coordinates);
		for (SubPath s : subPaths) {
			result = BboxService.union(result, calcBounds(s.getCoordinates()));
		}
		return result;
	}

	private Bbox calcBounds(Coordinate[] coordinates) {
		Coordinate c = coordinates[0];
		Bbox result = new Bbox(c.getX(), c.getY(), 0, 0);
		for (int i = 1; i < coordinates.length; i++) {
			c = coordinates[i];
			result = BboxService.union(result, new Bbox(c.getX(), c.getY(), 0, 0));
		}
		return result;
	}

	protected void drawTransformed() {
		if (steps != null) {
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

	private void scaleCoordinates(Coordinate[] coordinates, double scaleX, double scaleY, double x, double y,
			double newX, double newY) {
		for (int i = 0; i < coordinates.length; i++) {
			coordinates[i].setX(newX + scaleX * (coordinates[i].getX() - x));
			coordinates[i].setY(newY + scaleY * (coordinates[i].getY() - y));
		}
	}

	private void translateCoordinates(Coordinate[] coordinates, double dX, double dY) {
		for (int i = 0; i < coordinates.length; i++) {
			coordinates[i].setX(coordinates[i].getX() + dX);
			coordinates[i].setY(coordinates[i].getY() + dY);
		}
	}

	private void setMultiPolygon(Geometry multiPolygon) {
		if (multiPolygon.getGeometries() != null && multiPolygon.getGeometries().length > 0) {
			setPolygon(multiPolygon.getGeometries()[0]);
			for (int i = 1; i < multiPolygon.getGeometries().length; i++) {
				Geometry polygon = multiPolygon.getGeometries()[i];
				for (int j = 0; j < polygon.getGeometries().length; j++) {
					Geometry ring = polygon.getGeometries()[j];
					Coordinate[] pathCoords = removeLastCoordinate(ring);
					addSubPath(pathCoords, true);
				}
			}
		}
	}

	private void setMultiLineString(Geometry multiLineString) {
		if (multiLineString.getGeometries() != null && multiLineString.getGeometries().length > 0) {
			setLineString(multiLineString.getGeometries()[0]);
			for (int i = 1; i < multiLineString.getGeometries().length; i++) {
				Geometry lineString = multiLineString.getGeometries()[i];
				addSubPath(clone(lineString.getCoordinates()), false);
			}
		}
	}

	private void setPolygon(Geometry polygon) {
		if (polygon.getGeometries() != null && polygon.getGeometries().length > 0) {
			setLinearRing(polygon.getGeometries()[0]);
			getElement().getStyle().setProperty("fillRule", "evenOdd");
			for (int i = 1; i < polygon.getGeometries().length; i++) {
				Geometry ring = polygon.getGeometries()[i];
				Coordinate[] pathCoords = removeLastCoordinate(ring);
				addSubPath(pathCoords, true);
			}
		}
	}

	private void setLinearRing(Geometry linearRing) {
		Coordinate[] coordinates = linearRing.getCoordinates();
		if (coordinates != null && coordinates.length > 1) {
			Coordinate[] pathCoords = removeLastCoordinate(linearRing);
			this.closed = true;
			setCoordinates(pathCoords);
		}
	}

	private void setLineString(Geometry lineString) {
		if (lineString.getCoordinates() != null && lineString.getCoordinates().length > 0) {
			setCoordinates(clone(lineString.getCoordinates()));
		}
	}

	private Coordinate[] clone(Coordinate[] coordinates) {
		Coordinate[] copy = new Coordinate[coordinates.length];
		System.arraycopy(coordinates, 0, copy, 0, coordinates.length);
		return copy;
	}

	private Coordinate[] removeLastCoordinate(Geometry ring) {
		Coordinate[] coordinates = ring.getCoordinates();
		Coordinate[] pathCoords = new Coordinate[coordinates.length - 1];
		System.arraycopy(coordinates, 0, pathCoords, 0, coordinates.length - 1);
		return pathCoords;
	}

	/**
	 * Inner class for tracking sub-paths.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class SubPath {

		private Coordinate[] coordinates;

		private boolean closed;

		public SubPath(Coordinate[] coordinates, boolean closed) {
			this.coordinates = coordinates;
			this.closed = closed;
		}

		public Coordinate[] getCoordinates() {
			return coordinates;
		}

		public boolean isClosed() {
			return closed;
		}

	}

}
