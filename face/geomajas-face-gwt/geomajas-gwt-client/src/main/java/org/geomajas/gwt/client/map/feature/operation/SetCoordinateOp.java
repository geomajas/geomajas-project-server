/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client.map.feature.operation;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.feature.TransactionGeomIndex;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.spatial.geometry.GeometryEditor;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.spatial.geometry.LinearRing;
import org.geomajas.gwt.client.spatial.geometry.MultiLineString;
import org.geomajas.gwt.client.spatial.geometry.MultiPolygon;
import org.geomajas.gwt.client.spatial.geometry.Point;
import org.geomajas.gwt.client.spatial.geometry.Polygon;
import org.geomajas.gwt.client.spatial.geometry.operation.SetCoordinateOperation;

/**
 * ???
 *
 * @author Pieter De Graef
 */
public class SetCoordinateOp extends GeometryEditor implements FeatureOperation {

	private TransactionGeomIndex index;

	private Coordinate oldPosition;

	private Coordinate newPosition;

	public SetCoordinateOp(TransactionGeomIndex index, Coordinate newPosition) {
		this.index = index;
		this.newPosition = newPosition;
	}

	public void execute(Feature feature) {
		Geometry geometry = feature.getGeometry();
		if (geometry instanceof MultiPolygon) {
			execute((MultiPolygon) geometry);
		} else if (geometry instanceof MultiLineString) {
			execute((MultiLineString) geometry);
		} else if (geometry instanceof Polygon) {
			execute((Polygon) geometry);
		} else if (geometry instanceof LineString) {
			feature.setGeometry(execute((LineString) geometry));
		} else if (geometry instanceof Point) {
			execute((Point) geometry);
		}
	}

	public void undo(Feature feature) {
		Geometry geometry = feature.getGeometry();
		if (geometry instanceof MultiPolygon) {
			undo((MultiPolygon) geometry);
		} else if (geometry instanceof MultiLineString) {
			undo((MultiLineString) geometry);
		} else if (geometry instanceof Polygon) {
			undo((Polygon) geometry);
		} else if (geometry instanceof LineString) {
			feature.setGeometry(undo((LineString) geometry));
		} else if (geometry instanceof Point) {
			undo((Point) geometry);
		}
	}

	// -------------------------------------------------------------------------
	// Private execution functions:
	// -------------------------------------------------------------------------

	private void execute(MultiPolygon multiPolygon) {
		execute((Polygon) multiPolygon.getGeometryN(index.getGeometryIndex()));
	}

	private void execute(MultiLineString multiLineString) {
		LineString lineString = execute((LineString) multiLineString.getGeometryN(index.getGeometryIndex()));
		setLineStringN(multiLineString, lineString, index.getGeometryIndex());
	}

	private void execute(Polygon polygon) {
		LinearRing ring = null;
		if (index.isExteriorRing()) {
			ring = polygon.getExteriorRing();
			LineString lineString = execute(ring);
			setExteriorRing(polygon, (LinearRing) lineString);
		} else if (index.getInteriorRingIndex() >= 0) {
			ring = polygon.getInteriorRingN(index.getInteriorRingIndex());
			LineString lineString = execute(ring);
			setInteriorRingN(polygon, (LinearRing) lineString, index.getInteriorRingIndex());
		}
	}

	private LineString execute(LineString lineString) {
		oldPosition = (Coordinate) lineString.getCoordinateN(index.getCoordinateIndex()).clone();
		SetCoordinateOperation op = new SetCoordinateOperation(index.getCoordinateIndex(), newPosition);
		return (LineString) op.execute(lineString);
	}

	private void execute(Point point) {
		if (index.getCoordinateIndex() == 0) {
			oldPosition = (Coordinate) point.getCoordinate().clone();
			setCoordinate(point, newPosition);
		}
	}

	// -------------------------------------------------------------------------
	// Private undo functions:
	// -------------------------------------------------------------------------

	private void undo(MultiPolygon multiPolygon) {
		undo((Polygon) multiPolygon.getGeometryN(index.getGeometryIndex()));
	}

	private void undo(MultiLineString multiLineString) {
		LineString lineString = undo((LineString) multiLineString.getGeometryN(index.getGeometryIndex()));
		setLineStringN(multiLineString, lineString, index.getGeometryIndex());
	}

	private void undo(Polygon polygon) {
		LinearRing ring = null;
		if (index.isExteriorRing()) {
			ring = polygon.getExteriorRing();
			LineString lineString = undo(ring);
			setExteriorRing(polygon, (LinearRing) lineString);
		} else if (index.getInteriorRingIndex() >= 0) {
			ring = polygon.getInteriorRingN(index.getInteriorRingIndex());
			LineString lineString = undo(ring);
			setInteriorRingN(polygon, (LinearRing) lineString, index.getInteriorRingIndex());
		}
	}

	private LineString undo(LineString lineString) {
		SetCoordinateOperation op = new SetCoordinateOperation(index.getCoordinateIndex(), oldPosition);
		return (LineString) op.execute(lineString);
	}

	private void undo(Point point) {
		if (index.getCoordinateIndex() == 0) {
			setCoordinate(point, oldPosition);
		}
	}
}
