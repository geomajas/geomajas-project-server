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

import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.spatial.geometry.MultiLineString;
import org.geomajas.gwt.client.spatial.geometry.MultiPoint;
import org.geomajas.gwt.client.spatial.geometry.MultiPolygon;
import org.geomajas.gwt.client.spatial.geometry.Point;
import org.geomajas.gwt.client.spatial.geometry.Polygon;

/**
 * ???
 * 
 * @author Pieter De Graef
 */
public final class TransactionGeomIndexUtil {

	private TransactionGeomIndexUtil() {
	}

	public static boolean isDraggable(String identifier) {
		if (identifier == null || !identifier.contains("featureTransaction.feature")) {
			return false;
		}
		int position = identifier.indexOf("edge");
		if (position > 0) {
			return true;
		}
		position = identifier.indexOf("coordinate");
		return position > 0;
	}

	public static boolean isEdge(String identifier) {
		if (identifier == null || !identifier.contains("featureTransaction.feature")) {
			return false;
		}
		int position = identifier.indexOf("edge");
		return position > 0;
	}

	public static boolean isVertex(String identifier) {
		if (identifier == null || !identifier.contains("featureTransaction.feature")) {
			return false;
		}
		int position = identifier.indexOf("coordinate");
		return position > 0;
	}

	public static boolean isInteriorRing(String identifier, boolean areaOnly) {
		if (identifier == null) {
			return false;
		}
		int position = identifier.indexOf("hole");
		if (areaOnly) {
			int position2 = identifier.indexOf("area");
			if (position > 0 && position2 > 0) {
				return true;
			}
		} else if (position > 0) {
			return true;
		}

		return false;
	}

	public static boolean isExteriorRing(String identifier, boolean areaOnly) {
		if (identifier == null) {
			return false;
		}
		int position = identifier.indexOf("shell");
		if (areaOnly) {
			int position2 = identifier.indexOf("area");
			if (position > 0 && position2 > 0) {
				return true;
			}
		} else if (position > 0) {
			return true;
		}

		return false;
	}

	// ------------------------------------------------------------------------
	// Naming methods (format / parse):
	// ------------------------------------------------------------------------

	public static TransactionGeomIndex getIndex(String identifier) {
		if (identifier == null) {
			return new TransactionGeomIndex();
		}
		TransactionGeomIndex index = new TransactionGeomIndex();
		int position = identifier.indexOf("featureTransaction.feature");
		if (position >= 0) {
			String temp = identifier.substring(position + ("featureTransaction.feature".length()));
			index.setFeatureIndex(readInteger(temp));

			// Geometry index (polygon, lineString or point):
			index.setGeometryIndex(getIndex(identifier, "polygon"));
			if (index.getGeometryIndex() < 0) {
				index.setGeometryIndex(getIndex(identifier, "linestring"));
			}
			if (index.getGeometryIndex() < 0) {
				index.setGeometryIndex(getIndex(identifier, "point"));
			}

			index.setExteriorRing(hasIdentifier(identifier, "shell"));
			index.setInteriorRingIndex(getIndex(identifier, "hole"));

			// Coordinate index (coordinate or edge):
			index.setCoordinateIndex(getIndex(identifier, "coordinate"));
			if (index.getCoordinateIndex() < 0) {
				index.setEdgeIndex(getIndex(identifier, "edge"));
			}
			return index;
		}
		return index;
	}

	public static String getPolygonBackgroundName(Geometry geometry, TransactionGeomIndex index) {
		if (geometry instanceof Polygon) {
			return "featureTransaction.feature0.background";
		} else if (geometry instanceof MultiPolygon) {
			return "featureTransaction.feature0.polygon" + index.getGeometryIndex() + ".background";
		}
		return null;
	}

	public static String getVertexGroupName(String coordinateName) {
		return coordinateName.substring(0, coordinateName.lastIndexOf('.')) + ".vertices";
	}

	public static String getVertexGroupName(Geometry geometry, TransactionGeomIndex index) {
		String id = "featureTransaction.feature0";
		if (geometry instanceof Point) {
		} else if (geometry instanceof MultiPoint) {
			id += ".point" + index.getGeometryIndex();
		} else if (geometry instanceof LineString) {
			id += ".vertices";
		} else if (geometry instanceof MultiLineString) {
			id += ".linestring" + index.getGeometryIndex() + ".vertices";
		} else if (geometry instanceof Polygon) {
			if (index.isExteriorRing()) {
				id += ".shell.vertices";
			} else {
				id += ".hole" + index.getInteriorRingIndex() + ".vertices";
			}
		} else if (geometry instanceof MultiPolygon) {
			id += ".polygon" + index.getGeometryIndex();
			if (index.isExteriorRing()) {
				id += ".shell.vertices";
			} else {
				id += ".hole" + index.getInteriorRingIndex() + ".vertices";
			}
		}
		return id;
	}

	public static String getEdgeGroupName(String coordinateName) {
		return coordinateName.substring(0, coordinateName.lastIndexOf('.')) + ".edges";
	}

	public static String getEdgeGroupName(Geometry geometry, TransactionGeomIndex index) {
		String id = "featureTransaction.feature0";
		if (geometry instanceof LineString) {
			id += ".edges";
		} else if (geometry instanceof MultiLineString) {
			id += ".linestring" + index.getGeometryIndex() + ".edges";
		} else if (geometry instanceof Polygon) {
			if (index.isExteriorRing()) {
				id += ".shell.edges";
			} else {
				id += ".hole" + index.getInteriorRingIndex() + ".edges";
			}
		} else if (geometry instanceof MultiPolygon) {
			id += ".polygon" + index.getGeometryIndex();
			if (index.isExteriorRing()) {
				id += ".shell.edges";
			} else {
				id += ".hole" + index.getInteriorRingIndex() + ".edges";
			}
		}
		return id;
	}

	public static String getSelectionGroupName(String coordinateName) {
		return coordinateName.substring(0, coordinateName.lastIndexOf('.')) + ".selection";
	}

	public static String getVertexName(Geometry geometry, TransactionGeomIndex index) {
		String id = "featureTransaction.feature0";
		if (geometry instanceof Point) {
			id += ".coordinate0";
		} else if (geometry instanceof MultiPoint) {
			id += ".point" + index.getGeometryIndex();
		} else if (geometry instanceof LineString) {
			id += ".coordinate" + index.getCoordinateIndex();
		} else if (geometry instanceof MultiLineString) {
			id += ".linestring" + index.getGeometryIndex() + ".coordinate" + index.getCoordinateIndex();
		} else if (geometry instanceof Polygon) {
			if (index.isExteriorRing()) {
				id += ".shell.coordinate" + index.getCoordinateIndex();
			} else {
				id += ".hole" + index.getInteriorRingIndex() + ".coordinate" + index.getCoordinateIndex();
			}
		} else if (geometry instanceof MultiPolygon) {
			id += ".polygon" + index.getGeometryIndex();
			if (index.isExteriorRing()) {
				id += ".shell.coordinate" + index.getCoordinateIndex();
			} else {
				id += ".hole" + index.getInteriorRingIndex() + ".coordinate" + index.getCoordinateIndex();
			}
		}
		return id;
	}

	public static String getEdgeName(Geometry geometry, TransactionGeomIndex index) {
		String id = "featureTransaction.feature0";
		if (geometry instanceof Point) {
			return null;
		} else if (geometry instanceof MultiPoint) {
			return null;
		} else if (geometry instanceof LineString) {
			id += ".edge" + index.getEdgeIndex();
		} else if (geometry instanceof MultiLineString) {
			id += ".linestring" + index.getGeometryIndex() + ".edge" + index.getEdgeIndex();
		} else if (geometry instanceof Polygon) {
			if (index.isExteriorRing()) {
				id += ".shell.edge" + index.getEdgeIndex();
			} else {
				id += ".hole" + index.getInteriorRingIndex() + ".edge" + index.getEdgeIndex();
			}
		} else if (geometry instanceof MultiPolygon) {
			id += ".polygon" + index.getGeometryIndex();
			if (index.isExteriorRing()) {
				id += ".shell.edge" + index.getEdgeIndex();
			} else {
				id += ".hole" + index.getInteriorRingIndex() + ".edge" + index.getEdgeIndex();
			}
		}
		return id;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private static int getIndex(String identifier, String subIdentifier) {
		int position = identifier.indexOf(subIdentifier);
		if (position > 0) {
			String temp = identifier.substring(position + (subIdentifier.length()));
			return readInteger(temp);
		}
		return -1;
	}

	private static boolean hasIdentifier(String identifier, String subIdentifier) {
		int position = identifier.indexOf(subIdentifier);
		return position > 0;
	}

	private static int readInteger(String identifier) {
		int position = identifier.indexOf('.');
		try {
			if (position >= 0) {
				return Integer.parseInt(identifier.substring(0, position));
			}
			return Integer.parseInt(identifier);
		} catch (NumberFormatException e) {
			return -1;
		}
	}
}
