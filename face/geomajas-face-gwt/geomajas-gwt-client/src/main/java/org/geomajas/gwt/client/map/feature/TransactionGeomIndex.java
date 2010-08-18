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

package org.geomajas.gwt.client.map.feature;

import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.spatial.geometry.LinearRing;
import org.geomajas.gwt.client.spatial.geometry.MultiLineString;
import org.geomajas.gwt.client.spatial.geometry.MultiPolygon;
import org.geomajas.gwt.client.spatial.geometry.Polygon;

/**
 * ???
 * 
 * @author Pieter De Graef
 */
public class TransactionGeomIndex {

	private int featureIndex = -1;

	private int geometryIndex = -1;

	private boolean exteriorRing;

	private int interiorRingIndex = -1;

	private int coordinateIndex = -1;

	private int edgeIndex = -1;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	public TransactionGeomIndex() {
	}

	// ------------------------------------------------------------------------
	// Class specific methods:
	// ------------------------------------------------------------------------

	public Geometry getGeometry(FeatureTransaction featureTransaction) {
		if (featureIndex >= 0 && featureTransaction.getNewFeatures() != null
				&& featureTransaction.getNewFeatures().length > featureIndex) {
			return featureTransaction.getNewFeatures()[featureIndex].getGeometry();
		}
		return null;
	}

	public LinearRing getLinearRing(Geometry geometry) {
		if (geometry instanceof MultiPolygon) {
			if (geometryIndex >= 0 && geometryIndex < geometry.getNumGeometries()) {
				return getLinearRing(geometry.getGeometryN(geometryIndex));
			}
		} else if (geometry instanceof Polygon) {
			Polygon polygon = (Polygon) geometry;
			if (exteriorRing) {
				return polygon.getExteriorRing();
			} else if (interiorRingIndex >= 0 && interiorRingIndex < polygon.getNumInteriorRing()) {
				return polygon.getInteriorRingN(interiorRingIndex);
			}
		}
		return null;
	}

	/**
	 * Returns a LineString or LinearRing that is described by this index.
	 * 
	 * @param geometry
	 *            The original geometry.
	 * @return A LineString or LinearRing.
	 */
	public LineString getLineString(Geometry geometry) {
		if (geometry instanceof MultiLineString) {
			if (geometryIndex >= 0 && geometryIndex < geometry.getNumGeometries()) {
				return getLinearRing(geometry.getGeometryN(geometryIndex));
			}
		} else if (geometry instanceof LineString) {
			return (LineString) geometry;
		}
		return getLinearRing(geometry);
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	public int getFeatureIndex() {
		return featureIndex;
	}

	public void setFeatureIndex(int featureIndex) {
		this.featureIndex = featureIndex;
	}

	public int getGeometryIndex() {
		return geometryIndex;
	}

	public void setGeometryIndex(int geometryIndex) {
		this.geometryIndex = geometryIndex;
	}

	public int getCoordinateIndex() {
		return coordinateIndex;
	}

	public void setCoordinateIndex(int coordinateIndex) {
		this.coordinateIndex = coordinateIndex;
	}

	public boolean isExteriorRing() {
		return exteriorRing;
	}

	public void setExteriorRing(boolean exteriorRing) {
		this.exteriorRing = exteriorRing;
	}

	public int getInteriorRingIndex() {
		return interiorRingIndex;
	}

	public void setInteriorRingIndex(int interiorRingIndex) {
		this.interiorRingIndex = interiorRingIndex;
	}

	public int getEdgeIndex() {
		return edgeIndex;
	}

	public void setEdgeIndex(int edgeIndex) {
		this.edgeIndex = edgeIndex;
	}
}
