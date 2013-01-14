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

import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.feature.TransactionGeomIndex;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.spatial.geometry.GeometryEditor;
import org.geomajas.gwt.client.spatial.geometry.LinearRing;
import org.geomajas.gwt.client.spatial.geometry.MultiPolygon;
import org.geomajas.gwt.client.spatial.geometry.Polygon;
import org.geomajas.gwt.client.spatial.geometry.operation.InsertRingOperation;
import org.geomajas.gwt.client.spatial.geometry.operation.RemoveRingOperation;

/**
 * Remove ring operation.
 *
 * @author Pieter De Graef
 */
public class RemoveRingOp extends GeometryEditor implements FeatureOperation {

	private TransactionGeomIndex index;

	private LinearRing interiorRing;

	public RemoveRingOp(TransactionGeomIndex index) {
		this.index = index;
	}

	public void execute(Feature feature) {
		Geometry geometry = feature.getGeometry();
		if (geometry instanceof MultiPolygon) {
			execute((MultiPolygon) geometry);
		} else if (geometry instanceof Polygon) {
			feature.setGeometry(execute((Polygon) geometry));
		}
	}

	public void undo(Feature feature) {
		Geometry geometry = feature.getGeometry();
		if (geometry instanceof MultiPolygon) {
			undo((MultiPolygon) geometry);
		} else if (geometry instanceof Polygon) {
			feature.setGeometry(undo((Polygon) geometry));
		}
	}

	// -------------------------------------------------------------------------
	// Private execution functions:
	// -------------------------------------------------------------------------

	private void execute(MultiPolygon multiPolygon) {
		Polygon polygon = execute((Polygon) multiPolygon.getGeometryN(index.getGeometryIndex()));
		setPolygonN(multiPolygon, polygon, index.getGeometryIndex());
	}

	private Polygon execute(Polygon polygon) {
		Polygon result = null;
		if (index.getInteriorRingIndex() >= 0) {
			interiorRing = polygon.getInteriorRingN(index.getInteriorRingIndex());
			RemoveRingOperation op = new RemoveRingOperation(index.getInteriorRingIndex());
			result = (Polygon) op.execute(polygon);
		}
		if (result != null) {
			return result;
		}
		return polygon;
	}

	// -------------------------------------------------------------------------
	// Private undo functions:
	// -------------------------------------------------------------------------

	private void undo(MultiPolygon multiPolygon) {
		Polygon polygon = undo((Polygon) multiPolygon.getGeometryN(index.getGeometryIndex()));
		setPolygonN(multiPolygon, polygon, index.getGeometryIndex());
	}

	private Polygon undo(Polygon polygon) {
		Polygon result = null;
		if (index.getInteriorRingIndex() >= 0) {
			InsertRingOperation op = new InsertRingOperation(interiorRing, index.getInteriorRingIndex());
			result = (Polygon) op.execute(polygon);
		}
		if (result != null) {
			return result;
		}
		return polygon;
	}
}
