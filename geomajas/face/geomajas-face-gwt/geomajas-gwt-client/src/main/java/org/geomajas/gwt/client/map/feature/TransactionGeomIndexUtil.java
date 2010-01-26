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

/**
 * ???
 *
 * @author check subversion
 */
public final class TransactionGeomIndexUtil {

	private TransactionGeomIndexUtil() {
	}

	public static boolean isDraggable(String identifier) {
		if (identifier == null || identifier.indexOf("featureTransaction.feature") < 0) {
			return false;
		}
		int position = identifier.indexOf("edge");
		if (position > 0) {
			return true;
		}
		position = identifier.indexOf("coordinate");
		if (position > 0) {
			return true;
		}
		return false;
	}

	public static boolean isEdge(String identifier) {
		if (identifier == null || identifier.indexOf("featureTransaction.feature") < 0) {
			return false;
		}
		int position = identifier.indexOf("edge");
		if (position > 0) {
			return true;
		}
		return false;
	}

	public static boolean isVertex(String identifier) {
		if (identifier == null || identifier.indexOf("featureTransaction.feature") < 0) {
			return false;
		}
		int position = identifier.indexOf("coordinate");
		if (position > 0) {
			return true;
		}
		return false;
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
				index.setGeometryIndex(getIndex(identifier, "lineString"));
			}
			if (index.getGeometryIndex() < 0) {
				index.setGeometryIndex(getIndex(identifier, "point"));
			}

			index.setExteriorRingIndex(getIndex(identifier, "shell"));
			index.setInteriorRingIndex(getIndex(identifier, "hole"));

			// Coordinate index (coordinate or edge):
			index.setCoordinateIndex(getIndex(identifier, "coordinate"));
			if (index.getCoordinateIndex() < 0) {
				index.setCoordinateIndex(getIndex(identifier, "edge"));
			}
			return index;
		}
		return index;
	}

	private static int getIndex(String identifier, String subIdentifier) {
		int position = identifier.indexOf(subIdentifier);
		if (position > 0) {
			String temp = identifier.substring(position + (subIdentifier.length()));
			return readInteger(temp);
		}
		return -1;
	}

	private static int readInteger(String identifier) {
		int position = identifier.indexOf('.');
		try {
			if (position >= 0) {
				return Integer.parseInt(identifier.substring(0, position));
			}
			return Integer.parseInt(identifier);
		} catch (Exception e) {
			return -1;
		}
	}
}
