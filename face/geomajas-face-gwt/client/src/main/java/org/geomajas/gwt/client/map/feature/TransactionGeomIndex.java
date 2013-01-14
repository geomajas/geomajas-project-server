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

package org.geomajas.gwt.client.map.feature;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.spatial.geometry.LinearRing;
import org.geomajas.gwt.client.spatial.geometry.MultiLineString;
import org.geomajas.gwt.client.spatial.geometry.MultiPolygon;
import org.geomajas.gwt.client.spatial.geometry.Polygon;

import com.smartgwt.client.util.SC;

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

	private TransactionGeomIndex(TransactionGeomIndex other) {
		this.featureIndex = other.featureIndex;
		this.geometryIndex = other.geometryIndex;
		this.exteriorRing = other.exteriorRing;
		this.interiorRingIndex = other.interiorRingIndex;
		this.coordinateIndex = other.coordinateIndex;
		this.edgeIndex = other.edgeIndex;
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

	/**
	 * Returns the coordinate that is described by this index (if there is one).
	 * 
	 * @param geometry
	 *            The geometry to search in.
	 * @return Returns the coordinate or null.
	 */
	public Coordinate getCoordinate(Geometry geometry) {
		LineString lineString = getLineString(geometry);
		if (lineString != null && coordinateIndex >= 0) {
			return lineString.getCoordinateN(coordinateIndex);
		}
		return null;
	}

	/**
	 * Given this index object and a geometry, return the 2 (maximum!) neighboring edge and coordinate indices. This
	 * index needs to point to a coordinate (coordinate index >= 0). The result will contain maximum 2 indices where
	 * both coordinate and edge indices are filled. In other words the result will contain both neighboring coordinates
	 * and edges.
	 * 
	 * @param geometry
	 *            A geometry that fits this index object.
	 * @return Return an array of maximum 2 other indices, pointing to the 2 neighboring coordinates and edges. Can
	 *         return null if no valid info is found (if no coordinate index is found).
	 */
	public TransactionGeomIndex[] getNeighbors(Geometry geometry) {
		LineString lineString = getLineString(geometry);
		if (lineString != null && lineString.isClosed()) {
			// LinearRing geometry: goes round and round
			if (coordinateIndex >= 0) {
				int c1 = coordinateIndex - 1;
				int c2 = coordinateIndex + 1;
				if (c1 < 0) {
					c1 = lineString.getNumPoints() - 2;
				}
				int e1 = coordinateIndex;
				int e2 = coordinateIndex + 1;
				if (e2 > lineString.getNumPoints() - 1) {
					e2 = 1;
				}
				if (e1 == 0) {
					e1 = lineString.getNumPoints() - 1;
				}
				TransactionGeomIndex i1 = new TransactionGeomIndex(this);
				i1.setCoordinateIndex(c1);
				i1.setEdgeIndex(e1);
				TransactionGeomIndex i2 = new TransactionGeomIndex(this);
				i2.setCoordinateIndex(c2);
				i2.setEdgeIndex(e2);
				return new TransactionGeomIndex[] { i1, i2 };
			}
		} else if (lineString != null) {
			// LineString geometry: not closed.
			if (coordinateIndex >= 0) {
				SC.warn("TransactionGeomIndex.getNeighbors: Implement me");
			}
		}
		return null;
	}

	/**
	 * Return true or false indicating of the given identifier for an edge or vertex is a neighbor for this index.
	 * 
	 * @param identifier
	 *            The given identifier to establish this status for. Must be a coordinate identifier.(TODO support edges
	 *            as well)
	 * @param geometry
	 *            A geometry that fits this index.
	 * @return Returns true or false.
	 */
	public boolean isNeighbor(String identifier, Geometry geometry) {
		LineString lineString = getLineString(geometry);
		if (lineString != null) {
			int index = TransactionGeomIndexUtil.getIndex(identifier).getCoordinateIndex();
			if (index >= 0 && coordinateIndex >= 0) {
				if (coordinateIndex == 0) {
					if (index == 1) {
						return true;
					}
					if (lineString.isClosed() && index == lineString.getNumPoints() - 2) {
						return true;
					}
				} else if (coordinateIndex == lineString.getNumPoints() - 2) {
					if (index == coordinateIndex - 1) {
						return true;
					} else if (lineString.isClosed() && index == 0) {
						return true;
					}
				} else {
					if (index == coordinateIndex - 1 || index == coordinateIndex + 1) {
						return true;
					}
				}
			} else {
				SC.warn("TransactionGeomIndex.isNeighbor: Implement me");
			}
		}
		return false;
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
