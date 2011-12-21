/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client.spatial;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.spatial.MathService;

import com.google.inject.Inject;

/**
 * Implementation of the geometry service.
 * 
 * @author Pieter De Graef
 */
public class GeometryServiceImpl implements GeometryService {

	public static final Double PARAM_DEFAULT_DELTA = 1e-10;

	@Inject
	private BboxService bboxService;

	@Inject
	public GeometryServiceImpl() {
	}

	// ------------------------------------------------------------------------
	// GeometryService implementation:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public Geometry clone(Geometry geometry) {
		if (geometry == null) {
			throw new IllegalArgumentException("Cannot clone null geometry.");
		}
		Geometry clone = new Geometry(geometry.getGeometryType(), geometry.getSrid(), geometry.getPrecision());
		if (geometry.getGeometries() != null) {
			Geometry[] clones = new Geometry[geometry.getGeometries().length];
			for (int i = 0; i < geometry.getGeometries().length; i++) {
				clones[i] = clone(geometry.getGeometries()[i]);
			}
			clone.setGeometries(clones);
		}
		if (geometry.getCoordinates() != null) {
			Coordinate[] clones = new Coordinate[geometry.getCoordinates().length];
			for (int i = 0; i < geometry.getCoordinates().length; i++) {
				clones[i] = new Coordinate(geometry.getCoordinates()[i]);
			}
			clone.setCoordinates(clones);
		}
		return clone;
	}

	/** {@inheritDoc} */
	public Bbox getBounds(Geometry geometry) {
		if (geometry == null) {
			throw new IllegalArgumentException("Cannot get bounds for null geometry.");
		}
		if (isEmpty(geometry)) {
			return null;
		}
		Bbox bbox = null;
		if (geometry.getGeometries() != null) {
			bbox = getBounds(geometry.getGeometries()[0]);
			for (int i = 1; i < geometry.getGeometries().length; i++) {
				bbox = bboxService.union(bbox, getBounds(geometry.getGeometries()[i]));
			}
		}
		if (geometry.getCoordinates() != null) {
			double minX = Double.MAX_VALUE;
			double minY = Double.MAX_VALUE;
			double maxX = -Double.MAX_VALUE;
			double maxY = -Double.MAX_VALUE;

			for (Coordinate coordinate : geometry.getCoordinates()) {
				if (coordinate.getX() < minX) {
					minX = coordinate.getX();
				}
				if (coordinate.getY() < minY) {
					minY = coordinate.getY();
				}
				if (coordinate.getX() > maxX) {
					maxX = coordinate.getX();
				}
				if (coordinate.getY() > maxY) {
					maxY = coordinate.getY();
				}
			}
			if (bbox == null) {
				bbox = new Bbox(minX, minY, maxX - minX, maxY - minY);
			} else {
				bbox = bboxService.union(bbox, new Bbox(minX, minY, maxX - minX, maxY - minY));
			}
		}
		return bbox;
	}

	/** {@inheritDoc} */
	public Geometry toPolygon(Bbox bounds) {
		double minX = bounds.getX();
		double minY = bounds.getY();
		double maxX = bounds.getMaxX();
		double maxY = bounds.getMaxY();

		Geometry polygon = new Geometry(Geometry.POLYGON, 0, 0);
		polygon.setCoordinates(new Coordinate[] { new Coordinate(minX, minY), new Coordinate(maxX, minY),
				new Coordinate(maxX, maxY), new Coordinate(minX, maxY), new Coordinate(minX, minY) });
		return polygon;
	}

	/** {@inheritDoc} */
	public int getNumPoints(Geometry geometry) {
		if (geometry == null) {
			throw new IllegalArgumentException("Cannot get total number of points for null geometry.");
		}
		int count = 0;
		if (geometry.getGeometries() != null) {
			for (Geometry child : geometry.getGeometries()) {
				count += getNumPoints(child);
			}
		}
		if (geometry.getCoordinates() != null) {
			count += geometry.getCoordinates().length;
		}
		return count;
	}

	/** {@inheritDoc} */
	public boolean isEmpty(Geometry geometry) {
		return (geometry.getCoordinates() == null || geometry.getCoordinates().length == 0)
				&& (geometry.getGeometries() == null || geometry.getGeometries().length == 0);
	}

	/** {@inheritDoc} */
	public boolean isSimple(Geometry geometry) {
		if (isEmpty(geometry)) {
			return true;
		}
		if (geometry.getGeometries() != null) {
			for (Geometry child : geometry.getGeometries()) {
				if (!isSimple(child)) {
					return false;
				}
				if (Geometry.MULTI_LINE_STRING.equals(geometry.getGeometryType())) {
					for (Geometry child2 : geometry.getGeometries()) {
						if (child != child2) {
							if (intersects(child, child2)) {
								return false;
							}
						}
					}
				}
			}
		}
		if (geometry.getCoordinates() != null) {
			List<Coordinate> coords1 = new ArrayList<Coordinate>();
			List<Coordinate> coords2 = new ArrayList<Coordinate>();
			getAllCoordinates(geometry, coords1);
			getAllCoordinates(geometry, coords2);

			// Search for any intersection:
			if (coords1.size() > 1 && coords2.size() > 1) {
				for (int i = 0; i < coords2.size() - 1; i++) {
					for (int j = 0; j < coords1.size() - 1; j++) {
						if (MathService.intersectsLineSegment(coords2.get(i), coords2.get(i + 1), coords1.get(j),
								coords1.get(j + 1))) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	/** {@inheritDoc} */
	public boolean isValid(Geometry geometry) {
		if (Geometry.POINT.equals(geometry.getGeometryType())) {
			return true;
		} else if (Geometry.LINE_STRING.equals(geometry.getGeometryType())) {
			return isEmpty(geometry) || (geometry.getCoordinates().length != 1);
		} else if (Geometry.LINEAR_RING.equals(geometry.getGeometryType())) {
			return isValidLinearRing(geometry);
		} else if (Geometry.POLYGON.equals(geometry.getGeometryType())) {
			return isValidPolygon(geometry);
		} else if (Geometry.MULTI_POINT.equals(geometry.getGeometryType())) {
			return true;
		} else if (Geometry.MULTI_LINE_STRING.equals(geometry.getGeometryType())) {
			return isValidMultiLineString(geometry);
		} else if (Geometry.MULTI_POLYGON.equals(geometry.getGeometryType())) {
			return isValidMultiPolygon(geometry);
		}
		return false;
	}

	/** {@inheritDoc} */
	public boolean intersects(Geometry one, Geometry two) {
		if (one == null || two == null || isEmpty(one) || isEmpty(two)) {
			return false;
		}
		if (Geometry.POINT.equals(one.getGeometryType())) {
			return intersectsPoint(one, two);
		} else if (Geometry.LINE_STRING.equals(one.getGeometryType())) {
			return intersectsLineString(one, two);
		} else if (Geometry.MULTI_POINT.equals(one.getGeometryType())
				|| Geometry.MULTI_LINE_STRING.equals(one.getGeometryType())) {
			return intersectsMultiSomething(one, two);
		}
		List<Coordinate> coords1 = new ArrayList<Coordinate>();
		List<Coordinate> coords2 = new ArrayList<Coordinate>();
		getAllCoordinates(one, coords1);
		getAllCoordinates(two, coords2);

		for (int i = 0; i < coords1.size() - 1; i++) {
			for (int j = 0; j < coords2.size() - 1; j++) {
				if (MathService.intersectsLineSegment(coords1.get(i), coords1.get(i + 1), coords2.get(j),
						coords2.get(j + 1))) {
					return true;
				}
			}
		}
		return false;
	}

	/** {@inheritDoc} */
	public double getArea(Geometry geometry) {
		double area = 0;
		if (geometry.getGeometries() != null) {
			for (Geometry child : geometry.getGeometries()) {
				area += getArea(child);
			}
		}
		if (geometry.getCoordinates() != null && Geometry.LINEAR_RING.equals(geometry.getGeometryType())) {
			double temp = 0;
			for (int i = 1; i < geometry.getCoordinates().length; i++) {
				double x1 = geometry.getCoordinates()[i - 1].getX();
				double y1 = geometry.getCoordinates()[i - 1].getY();
				double x2 = geometry.getCoordinates()[i].getX();
				double y2 = geometry.getCoordinates()[i].getY();
				temp += x1 * y2 - x2 * y1;
			}
			area += Math.abs(temp / 2);
		}
		return area;
	}

	/** {@inheritDoc} */
	public double getLength(Geometry geometry) {
		double length = 0;
		if (geometry.getGeometries() != null) {
			for (Geometry child : geometry.getGeometries()) {
				length += getLength(child);
			}
		}
		if (geometry.getCoordinates() != null
				&& (Geometry.LINE_STRING.equals(geometry.getGeometryType()) || Geometry.LINEAR_RING.equals(geometry
						.getGeometryType()))) {
			for (int i = 0; i < geometry.getCoordinates().length - 1; i++) {
				double deltaX = geometry.getCoordinates()[i + 1].getX() - geometry.getCoordinates()[i].getX();
				double deltaY = geometry.getCoordinates()[i + 1].getY() - geometry.getCoordinates()[i].getY();
				length += Math.sqrt(deltaX * deltaX + deltaY * deltaY);
			}
		}
		return length;
	}

	/** {@inheritDoc} */
	public Coordinate getCentroid(Geometry geometry) {
		if (Geometry.POINT.equals(geometry.getGeometryType())) {
			return getCentroidPoint(geometry);
		} else if (Geometry.LINE_STRING.equals(geometry.getGeometryType())) {
			return getCentroidLineString(geometry);
		} else if (Geometry.LINEAR_RING.equals(geometry.getGeometryType())) {
			return getCentroidLinearRing(geometry);
		} else if (Geometry.POLYGON.equals(geometry.getGeometryType())) {
			return getCentroidPolygon(geometry);
		} else if (Geometry.MULTI_POINT.equals(geometry.getGeometryType())) {
			return getCentroidMultiPoint(geometry);
		} else if (Geometry.MULTI_LINE_STRING.equals(geometry.getGeometryType())) {
			return getCentroidMultiLineString(geometry);
		} else if (Geometry.MULTI_POLYGON.equals(geometry.getGeometryType())) {
			return getCentroidMultiPolygon(geometry);
		}
		return null;
	}

	/** {@inheritDoc} */
	public double getDistance(Geometry geometry, Coordinate coordinate) {
		double minDistance = Double.MAX_VALUE;
		if (coordinate != null && geometry != null) {
			if (geometry.getGeometries() != null) {
				for (Geometry child : geometry.getGeometries()) {
					double distance = getDistance(child, coordinate);
					if (distance < minDistance) {
						minDistance = distance;
					}
				}
			}
			if (geometry.getCoordinates() != null) {
				if (geometry.getCoordinates().length == 1) {
					double distance = MathService.distance(coordinate, geometry.getCoordinates()[0]);
					if (distance < minDistance) {
						minDistance = distance;
					}
				} else if (geometry.getCoordinates().length > 1) {
					for (int i = 0; i < geometry.getCoordinates().length - 1; i++) {
						double distance = MathService.distance(geometry.getCoordinates()[i],
								geometry.getCoordinates()[i + 1], coordinate);
						if (distance < minDistance) {
							minDistance = distance;
						}
					}
				}
			}
		}

		return minDistance;
	}

	/** {@inheritDoc} */
	public String toWkt(Geometry geometry) {
		if (Geometry.POINT.equals(geometry.getGeometryType())) {
			return toWktPoint(geometry);
		} else if (Geometry.LINE_STRING.equals(geometry.getGeometryType())
				|| Geometry.LINEAR_RING.equals(geometry.getGeometryType())) {
			return toWktLineString(geometry);
		} else if (Geometry.POLYGON.equals(geometry.getGeometryType())) {
			return toWktPolygon(geometry);
		} else if (Geometry.MULTI_POINT.equals(geometry.getGeometryType())) {
			return toWktMultiPoint(geometry);
		} else if (Geometry.MULTI_LINE_STRING.equals(geometry.getGeometryType())) {
			return toWktMultiLineString(geometry);
		} else if (Geometry.MULTI_POLYGON.equals(geometry.getGeometryType())) {
			return toWktMultiPolygon(geometry);
		}
		return "";
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void getAllCoordinates(Geometry geometry, List<Coordinate> coordinates) {
		if (geometry.getGeometries() != null) {
			for (Geometry child : geometry.getGeometries()) {
				getAllCoordinates(child, coordinates);
			}
		}
		if (geometry.getCoordinates() != null) {
			for (Coordinate coordinate : geometry.getCoordinates()) {
				coordinates.add(coordinate);
			}
		}
	}

	private Coordinate getCentroidPoint(Geometry geometry) {
		if (geometry.getCoordinates() != null) {
			return geometry.getCoordinates()[0];
		}
		return null;
	}

	private Coordinate getCentroidLineString(Geometry geometry) {
		if (geometry.getCoordinates() == null) {
			return null;
		}
		double sumX = 0;
		double sumY = 0;
		double totalLength = 0;
		for (int i = 0; i < geometry.getCoordinates().length - 1; i++) {
			double length = geometry.getCoordinates()[i].distance(geometry.getCoordinates()[i + 1]);
			totalLength += length;
			double midx = (geometry.getCoordinates()[i].getX() + geometry.getCoordinates()[i + 1].getX()) / 2;
			sumX += length * midx;
			double midy = (geometry.getCoordinates()[i].getY() + geometry.getCoordinates()[i + 1].getY()) / 2;
			sumY += length * midy;
		}
		return new Coordinate(sumX / totalLength, sumY / totalLength);
	}

	private Coordinate getCentroidLinearRing(Geometry geometry) {
		if (geometry.getCoordinates() == null) {
			return null;
		}
		double area = getArea(geometry);
		double x = 0;
		double y = 0;
		for (int i = 1; i < geometry.getCoordinates().length; i++) {
			double x1 = geometry.getCoordinates()[i - 1].getX();
			double y1 = geometry.getCoordinates()[i - 1].getY();
			double x2 = geometry.getCoordinates()[i].getX();
			double y2 = geometry.getCoordinates()[i].getY();
			x += (x1 + x2) * (x1 * y2 - x2 * y1);
			y += (y1 + y2) * (x1 * y2 - x2 * y1);
		}
		x = x / (6 * area);
		y = y / (6 * area);
		return new Coordinate(x, y);
	}

	private Coordinate getCentroidPolygon(Geometry geometry) {
		if (geometry.getGeometries() == null) {
			return null;
		}
		return getCentroidLinearRing(geometry.getGeometries()[0]);
	}

	private Coordinate getCentroidMultiPoint(Geometry geometry) {
		if (geometry.getGeometries() == null) {
			return null;
		}
		double sumX = 0;
		double sumY = 0;
		double numPoints = geometry.getGeometries().length;
		for (Geometry point : geometry.getGeometries()) {
			sumX += point.getCoordinates()[0].getX();
			sumY += point.getCoordinates()[0].getY();
		}
		return new Coordinate(sumX / numPoints, sumY / numPoints);
	}

	private Coordinate getCentroidMultiLineString(Geometry geometry) {
		if (isEmpty(geometry)) {
			return null;
		}
		double sumX = 0;
		double sumY = 0;
		double totalLength = 0;
		Coordinate[] coordinates = new Coordinate[geometry.getGeometries().length];
		for (int i = 0; i < geometry.getGeometries().length; i++) {
			coordinates[i] = getCentroidLineString(geometry.getGeometries()[i]);
		}
		for (int i = 0; i < coordinates.length - 1; i++) {
			double length = coordinates[i].distance(coordinates[i + 1]);
			totalLength += length;
			double midx = (coordinates[i].getX() + coordinates[i + 1].getX()) / 2;
			sumX += length * midx;
			double midy = (coordinates[i].getY() + coordinates[i + 1].getY()) / 2;
			sumY += length * midy;
		}
		return new Coordinate(sumX / totalLength, sumY / totalLength);
	}

	private Coordinate getCentroidMultiPolygon(Geometry geometry) {
		if (geometry.getGeometries() == null) {
			return null;
		}
		double sumX = 0;
		double sumY = 0;
		double totalLength = 0;
		Coordinate[] coordinates = new Coordinate[geometry.getGeometries().length];
		for (int i = 0; i < geometry.getGeometries().length; i++) {
			coordinates[i] = getCentroidPolygon(geometry.getGeometries()[i]);
		}
		for (int i = 0; i < coordinates.length - 1; i++) {
			double length = coordinates[i].distance(coordinates[i + 1]);
			totalLength += length;
			double midx = (coordinates[i].getX() + coordinates[i + 1].getX()) / 2;
			sumX += length * midx;
			double midy = (coordinates[i].getY() + coordinates[i + 1].getY()) / 2;
			sumY += length * midy;
		}
		return new Coordinate(sumX / totalLength, sumY / totalLength);
	}

	private boolean isClosed(Geometry geometry) {
		if (geometry.getCoordinates() != null && geometry.getCoordinates().length > 1) {
			Coordinate first = geometry.getCoordinates()[0];
			Coordinate last = geometry.getCoordinates()[geometry.getCoordinates().length - 1];
			return first.equals(last);
		}
		return false;
	}

	private boolean isValidLinearRing(Geometry geometry) {
		if (isEmpty(geometry)) {
			return true;
		}
		if (!isClosed(geometry)) {
			return false;
		}
		Coordinate[] coordinates = geometry.getCoordinates();
		if (coordinates.length < 4) {
			return false;
		}
		for (int i = 0; i < coordinates.length - 1; i++) {
			for (int j = 0; j < coordinates.length - 1; j++) {
				if (MathService.intersectsLineSegment(coordinates[i], coordinates[i + 1], coordinates[j],
						coordinates[j + 1])) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean isValidPolygon(Geometry geometry) {
		if (isEmpty(geometry)) {
			return true;
		}
		for (Geometry ring1 : geometry.getGeometries()) {
			if (!isValidLinearRing(ring1)) {
				return false;
			}
			for (Geometry ring2 : geometry.getGeometries()) {
				if (!ring1.equals(ring2) && intersects(ring1, ring2)) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean isValidMultiLineString(Geometry geometry) {
		if (isEmpty(geometry)) {
			return true;
		}
		for (Geometry lineString : geometry.getGeometries()) {
			if (!isValid(lineString)) {
				return false;
			}
		}
		return true;
	}

	private boolean isValidMultiPolygon(Geometry geometry) {
		if (!isEmpty(geometry)) {
			for (int i = 0; i < geometry.getGeometries().length; i++) {
				if (!isValidPolygon(geometry.getGeometries()[i])) {
					return false;
				}
				for (int j = i + 1; j < geometry.getGeometries().length; j++) {
					if (intersects(geometry.getGeometries()[i], geometry.getGeometries()[j])) {
						return false;
					}
				}
			}
		}
		return true;
	}

	// We assume neither is null or empty.
	private boolean intersectsPoint(Geometry point, Geometry geometry) {
		if (geometry.getGeometries() != null) {
			for (Geometry child : geometry.getGeometries()) {
				if (intersectsPoint(point, child)) {
					return true;
				}
			}
		}
		if (geometry.getCoordinates() != null) {
			Coordinate coordinate = point.getCoordinates()[0];
			if (geometry.getCoordinates().length == 1) {
				return coordinate.equals(geometry.getCoordinates()[0]);
			} else {
				for (int i = 0; i < geometry.getCoordinates().length - 1; i++) {
					double distance = MathService.distance(geometry.getCoordinates()[i],
							geometry.getCoordinates()[i + 1], coordinate);
					if (distance < PARAM_DEFAULT_DELTA) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean intersectsLineString(Geometry lineString, Geometry geometry) {
		if (geometry.getGeometries() != null) {
			for (Geometry child : geometry.getGeometries()) {
				if (intersectsLineString(lineString, child)) {
					return true;
				}
			}
		}
		if (geometry.getCoordinates() != null) {
			if (lineString.getCoordinates().length > 1 && geometry.getCoordinates().length > 1) {
				for (int i = 0; i < lineString.getCoordinates().length - 1; i++) {
					for (int j = 0; j < geometry.getCoordinates().length - 1; j++) {
						if (MathService.intersectsLineSegment(lineString.getCoordinates()[i],
								lineString.getCoordinates()[i + 1], geometry.getCoordinates()[j],
								geometry.getCoordinates()[j + 1])) {
							return true;
						}
					}
					if (MathService.touches(geometry, lineString.getCoordinates()[i])) {
						return true;
					}
				}
			} else {
				// Check if points are equal...
			}
		}
		return false;
	}

	private boolean intersectsMultiSomething(Geometry multiSomething, Geometry geometry) {
		if (isEmpty(multiSomething)) {
			return false;
		}
		if (multiSomething.getGeometries() != null) {
			for (Geometry child : multiSomething.getGeometries()) {
				if (intersects(child, geometry)) {
					return true;
				}
			}
		}
		return false;
	}

	// ------------------------------------------------------------------------
	// Private methods for parsing WKT:
	// ------------------------------------------------------------------------

	private String toWktPoint(Geometry geometry) {
		if (isEmpty(geometry)) {
			return "POINT EMPTY";
		}
		return "POINT (" + geometry.getCoordinates()[0].getX() + " " + geometry.getCoordinates()[0].getY() + ")";
	}

	private String toWktLineString(Geometry geometry) {
		if (isEmpty(geometry)) {
			return "LINESTRING EMPTY";
		}
		String wkt = "LINESTRING (";
		for (int i = 0; i < geometry.getCoordinates().length; i++) {
			if (i > 0) {
				wkt += ", ";
			}
			wkt += geometry.getCoordinates()[i].getX() + " " + geometry.getCoordinates()[i].getY();
		}
		return wkt + ")";
	}

	private String toWktPolygon(Geometry geometry) {
		if (isEmpty(geometry)) {
			return "POLYGON EMPTY";
		}
		String wkt = "POLYGON (";
		for (int i = 0; i < geometry.getGeometries().length; i++) {
			String ringWkt = toWktLineString(geometry.getGeometries()[i]);
			if (i > 0) {
				wkt += ",";
			}
			wkt += ringWkt.substring(ringWkt.indexOf("("));
		}

		return wkt + ")";
	}

	private String toWktMultiPoint(Geometry geometry) {
		if (isEmpty(geometry)) {
			return "MULTIPOINT EMPTY";
		}
		String wkt = "MULTIPOINT (";
		for (int i = 0; i < geometry.getGeometries().length; i++) {
			String lineWkt = toWktPoint(geometry.getGeometries()[i]);
			if (i > 0) {
				wkt += ",";
			}
			wkt += lineWkt.substring(lineWkt.indexOf("("));
		}
		return wkt + ")";
	}

	private String toWktMultiLineString(Geometry geometry) {
		if (isEmpty(geometry)) {
			return "MULTILINESTRING EMPTY";
		}
		String wkt = "MULTILINESTRING (";
		for (int i = 0; i < geometry.getGeometries().length; i++) {
			String lineWkt = toWktLineString(geometry.getGeometries()[i]);
			if (i > 0) {
				wkt += ",";
			}
			wkt += lineWkt.substring(lineWkt.indexOf("("));
		}
		return wkt + ")";
	}

	private String toWktMultiPolygon(Geometry geometry) {
		if (isEmpty(geometry)) {
			return "MULTIPOLYGON EMPTY";
		}
		String wkt = "MULTIPOLYGON (";
		for (int i = 0; i < geometry.getGeometries().length; i++) {
			String lineWkt = toWktPolygon(geometry.getGeometries()[i]);
			if (i > 0) {
				wkt += ",";
			}
			wkt += lineWkt.substring(lineWkt.indexOf("("));
		}
		return wkt + ")";
	}
}