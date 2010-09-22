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
