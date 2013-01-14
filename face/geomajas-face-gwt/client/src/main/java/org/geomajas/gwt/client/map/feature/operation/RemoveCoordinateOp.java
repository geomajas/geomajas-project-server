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
import org.geomajas.gwt.client.spatial.geometry.Polygon;
import org.geomajas.gwt.client.spatial.geometry.operation.InsertCoordinateOperation;
import org.geomajas.gwt.client.spatial.geometry.operation.RemoveCoordinateOperation;

/**
 * Remove coordinate operation.
 *
 * @author Pieter De Graef
 */
public class RemoveCoordinateOp extends GeometryEditor implements FeatureOperation {

	private TransactionGeomIndex index;

	private Coordinate coordinate;

	public RemoveCoordinateOp(TransactionGeomIndex index) {
		this.index = index;
		if (index.getCoordinateIndex() < 0) {
			index.setCoordinateIndex(index.getEdgeIndex());
		}
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
		coordinate = (Coordinate) lineString.getCoordinateN(index.getCoordinateIndex()).clone();
		RemoveCoordinateOperation op = new RemoveCoordinateOperation(index.getCoordinateIndex());
		return (LineString) op.execute(lineString);
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
		InsertCoordinateOperation op = new InsertCoordinateOperation(index.getCoordinateIndex(), coordinate);
		return (LineString) op.execute(lineString);
	}
}
