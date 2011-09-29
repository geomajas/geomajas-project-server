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

package org.geomajas.plugin.editing.client.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;
import org.geomajas.plugin.editing.client.service.GeometryIndexService;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;

/**
 * Implementation of the {@link GeometryIndexService}.
 * 
 * @author Pieter De Graef
 */
public class GeometryIndexServiceImpl implements GeometryIndexService {

	public GeometryIndexImpl create(GeometryIndexType type, int... values) {
		GeometryIndexImpl index = null;
		if (values.length > 0) {
			index = new GeometryIndexImpl(type, values[values.length - 1], null);
		} else {
			throw new NullPointerException("Cannot create a GeometryIndex since no values where given.");
		}
		if (values.length > 1) {
			for (int i = values.length - 2; i >= 0; i--) {
				index = new GeometryIndexImpl(GeometryIndexType.TYPE_GEOMETRY, values[i], index);
			}
		}
		return index;
	}

	public GeometryIndexImpl addChildren(GeometryIndex index, GeometryIndexType type, int... values) {
		if (index == null) {
			return create(type, values);
		}
		GeometryIndexImpl clone = new GeometryIndexImpl(index);
		clone.setChild(create(type, values));
		return clone;
	}

	public String format(GeometryIndex index) {
		if (index.hasChild()) {
			return "geometry" + index.getValue() + "." + format(index.getChild());
		}
		switch (index.getType()) {
			case TYPE_VERTEX:
				return "vertex" + index.getValue();
			case TYPE_EDGE:
				return "edge" + index.getValue();
			default:
				return "geometry" + index.getValue();
		}
	}

	public GeometryIndex parse(String id) throws GeometryIndexNotFoundException {
		try {
			GeometryIndex index = parseRecursive(id.toLowerCase());
			if (index == null) {
				throw new GeometryIndexNotFoundException("Could not parse '" + id + "' as a GeometryIndex.");
			}
			return index;
		} catch (GeometryIndexNotFoundException e) {
			throw new GeometryIndexNotFoundException("Could not parse '" + id + "' as a GeometryIndex.");
		}
	}

	public Geometry getGeometry(Geometry geometry, GeometryIndex index) throws GeometryIndexNotFoundException {
		if (index.hasChild()) {
			if (geometry.getGeometries() != null && geometry.getGeometries().length > index.getValue()) {
				return getGeometry(geometry.getGeometries()[index.getValue()], index.getChild());
			}
			throw new GeometryIndexNotFoundException("Could not match index with given geometry");
		}
		if (index.getType() == GeometryIndexType.TYPE_GEOMETRY && geometry.getGeometries() != null
				&& geometry.getGeometries().length > index.getValue()) {
			return geometry.getGeometries()[index.getValue()];
		}
		throw new GeometryIndexNotFoundException("Could not match index with given geometry");
	}

	public Coordinate getVertex(Geometry geometry, GeometryIndex index) throws GeometryIndexNotFoundException {
		if (index.hasChild()) {
			if (geometry.getGeometries() != null && geometry.getGeometries().length > index.getValue()) {
				return getVertex(geometry.getGeometries()[index.getValue()], index.getChild());
			}
			throw new GeometryIndexNotFoundException("Could not match index with given geometry");
		}
		if (index.getType() == GeometryIndexType.TYPE_VERTEX && geometry.getCoordinates() != null
				&& geometry.getCoordinates().length > index.getValue() && index.getValue() >= 0) {
			return geometry.getCoordinates()[index.getValue()];
		}
		throw new GeometryIndexNotFoundException("Could not match index with given geometry");
	}

	public Coordinate[] getEdge(Geometry geometry, GeometryIndex index) throws GeometryIndexNotFoundException {
		if (index.hasChild()) {
			if (geometry.getGeometries() != null && geometry.getGeometries().length > index.getValue()) {
				return getEdge(geometry.getGeometries()[index.getValue()], index.getChild());
			}
			throw new GeometryIndexNotFoundException("Could not match index with given geometry");
		}
		if (index.getType() == GeometryIndexType.TYPE_EDGE && geometry.getCoordinates() != null
				&& geometry.getCoordinates().length > (index.getValue() - 1)) {
			return new Coordinate[] { geometry.getCoordinates()[index.getValue()],
					geometry.getCoordinates()[index.getValue() + 1] };
		}
		throw new GeometryIndexNotFoundException("Could not match index with given geometry");
	}

	public List<GeometryIndex> getAdjacentVertices(Geometry geometry, GeometryIndex index)
			throws GeometryIndexNotFoundException {
		if (geometry == null || index == null) {
			throw new NullPointerException("No null values allowed!");
		}
		GeometryIndexCombo combo = recursiveSearch(geometry, index);

		int[] indices = new int[] {};
		if (isVertex(index)) {
			indices = getAdjacentVerticesForVertex(combo.getGeometry(), combo.getIndex());
		} else if (isEdge(index)) {
			indices = getAdjacentVerticesForEdge(combo.getGeometry(), combo.getIndex());
		}

		List<GeometryIndex> indexList = new ArrayList<GeometryIndex>();
		for (int i = 0; i < indices.length; i++) {
			indexList.add(recursiveCreate(index, indices[i], GeometryIndexType.TYPE_VERTEX));
		}

		// Can return an empty list.
		return indexList;
	}

	public List<GeometryIndex> getAdjacentEdges(Geometry geometry, GeometryIndex index)
			throws GeometryIndexNotFoundException {
		if (geometry == null || index == null) {
			throw new NullPointerException("No null values allowed!");
		}
		GeometryIndexCombo combo = recursiveSearch(geometry, index);
		int[] indices = new int[] {};
		if (isVertex(index)) {
			indices = getAdjacentEdgesForVertex(combo.getGeometry(), combo.getIndex());
		} else if (isEdge(index)) {
			indices = getAdjacentEdgesForEdge(combo.getGeometry(), combo.getIndex());
		}

		List<GeometryIndex> indexList = new ArrayList<GeometryIndex>();
		for (int i = 0; i < indices.length; i++) {
			indexList.add(recursiveCreate(index, indices[i], GeometryIndexType.TYPE_EDGE));
		}

		// Can return an empty list.
		return indexList;
	}

	public boolean isAdjacent(Geometry geometry, GeometryIndex one, GeometryIndex two) {
		List<GeometryIndex> neighbors = null;
		try {
			if (isVertex(two)) {
				neighbors = getAdjacentVertices(geometry, one);
			} else if (isEdge(two)) {
				neighbors = getAdjacentEdges(geometry, one);
			}
		} catch (GeometryIndexNotFoundException e) {
			return false;
		}

		if (neighbors != null) {
			for (GeometryIndex neighbor : neighbors) {
				if (neighbor.equals(two)) {
					return true;
				}
			}
		}

		return false;
	}

	public GeometryIndex getNextVertex(GeometryIndex index) {
		if (index.hasChild()) {
			return new GeometryIndexImpl(index.getType(), index.getValue(), getNextVertex(index.getChild()));
		} else {
			return new GeometryIndexImpl(GeometryIndexType.TYPE_VERTEX, index.getValue() + 1, null);
		}
	}

	public GeometryIndex getPreviousVertex(GeometryIndex index) {
		if (index.hasChild()) {
			return new GeometryIndexImpl(index.getType(), index.getValue(), getNextVertex(index.getChild()));
		} else {
			return new GeometryIndexImpl(GeometryIndexType.TYPE_VERTEX, index.getValue() - 1, null);
		}
	}

	public int getSiblingCount(Geometry geom, GeometryIndex index) {
		if (index.hasChild() && geom.getGeometries() != null && geom.getGeometries().length > index.getValue()) {
			return getSiblingCount(geom.getGeometries()[index.getValue()], index.getChild());
		}
		switch (index.getType()) {
			case TYPE_VERTEX:
				return geom.getCoordinates() != null ? geom.getCoordinates().length : 0;
			case TYPE_EDGE:
				if (geom.getGeometryType() == Geometry.LINE_STRING) {
					int count = geom.getCoordinates() != null ? geom.getCoordinates().length - 1 : 0;
					if (count < 0) {
						count = 0;
					}
					return count;
				} else if (geom.getGeometryType() == Geometry.LINEAR_RING) {
					return geom.getCoordinates() != null ? geom.getCoordinates().length : 0;
				}
				return 0;
			case TYPE_GEOMETRY:
			default:
				return geom.getGeometries() != null ? geom.getGeometries().length : 0;
		}
	}

	public String getGeometryType(Geometry geom, GeometryIndex index) {
		if (index.hasChild() && geom.getGeometries() != null && geom.getGeometries().length > index.getValue()) {
			return getGeometryType(geom.getGeometries()[index.getValue()], index.getChild());
		}
		return geom.getGeometryType();
	}

	public boolean isVertex(GeometryIndex index) {
		if (index.hasChild()) {
			return isVertex(index.getChild());
		}
		return index.getType() == GeometryIndexType.TYPE_VERTEX;
	}

	public boolean isEdge(GeometryIndex index) {
		if (index.hasChild()) {
			return isEdge(index.getChild());
		}
		return index.getType() == GeometryIndexType.TYPE_EDGE;
	}

	public boolean isGeometry(GeometryIndex index) {
		if (index.hasChild()) {
			return isGeometry(index.getChild());
		}
		return index.getType() == GeometryIndexType.TYPE_GEOMETRY;
	}

	public GeometryIndexType getType(GeometryIndex index) {
		if (index.hasChild()) {
			return getType(index.getChild());
		}
		return index.getType();
	}

	// ------------------------------------------------------------------------
	// Protected methods that are not part of the interface:
	// ------------------------------------------------------------------------

	protected void setVertex(Geometry geom, GeometryIndex index, Coordinate coordinate)
			throws GeometryIndexNotFoundException {
		if (index.hasChild() && geom.getGeometries() != null && geom.getGeometries().length > index.getValue()) {
			setVertex(geom.getGeometries()[index.getValue()], index.getChild(), coordinate);
		} else if (index.getType() == GeometryIndexType.TYPE_VERTEX && geom.getCoordinates() != null
				&& geom.getCoordinates().length > index.getValue()) {
			geom.getCoordinates()[index.getValue()] = coordinate;
			if (index.getValue() == 0 && geom.getGeometryType().equals(Geometry.LINEAR_RING)) {
				// In case of closed ring, keep last coordinate equal to the first:
				geom.getCoordinates()[geom.getCoordinates().length - 1] = new Coordinate(coordinate);
			}
		} else {
			throw new GeometryIndexNotFoundException("Could not match index with given geometry");
		}
	}

	protected void insert(Geometry geom, GeometryIndex index, List<Coordinate> coordinates)
			throws GeometryIndexNotFoundException {
		if (index.hasChild() && geom.getGeometries() != null && geom.getGeometries().length > index.getValue()) {
			insert(geom.getGeometries()[index.getValue()], index.getChild(), coordinates);
		} else if (index.getType() == GeometryIndexType.TYPE_EDGE && geom.getCoordinates() != null
				&& geom.getCoordinates().length > index.getValue()) {
			// Inserting on edges allows only to insert on existing edges. No adding at the end:
			Coordinate[] result = new Coordinate[geom.getCoordinates().length + coordinates.size()];
			int count = 0;
			for (int i = 0; i < geom.getCoordinates().length; i++) {
				if (i == (index.getValue() + 1)) {
					for (count = 0; count < coordinates.size(); count++) {
						result[i + count] = coordinates.get(count);
					}
				}
				result[i + count] = geom.getCoordinates()[i];
			}
			geom.setCoordinates(result);
		} else if (index.getType() == GeometryIndexType.TYPE_VERTEX) {
			// TODO check if we need to close a ring....
			// When inserting between vertices, this should also support adding at the end.
			Coordinate[] result = null;
			if (geom.getCoordinates() == null) {
				result = new Coordinate[] { coordinates.get(0) };
			} else {
				result = new Coordinate[geom.getCoordinates().length + coordinates.size()];
				int count1 = 0, count2 = 0;
				for (int i = 0; i < result.length; i++) {
					if (i < index.getValue()) {
						result[i] = geom.getCoordinates()[i];
						count1++;
					} else if (i == index.getValue()) {
						if (index.getValue() < result.length - 1) {
							result[i] = geom.getCoordinates()[i];
							count1++;
						} else {
							result[i] = coordinates.get(count2);
							count2++;
						}
					} else if (i < index.getValue() + geom.getCoordinates().length) {
						result[i] = coordinates.get(count2);
						count2++;
					} else {
						result[i] = geom.getCoordinates()[i - count1];
					}
				}
			}
			if (Geometry.POINT.equals(geom.getGeometryType()) || Geometry.LINE_STRING.equals(geom.getGeometryType())
					|| Geometry.LINEAR_RING.equals(geom.getGeometryType())) {
				geom.setCoordinates(result);
			} else {
				throw new GeometryIndexNotFoundException("Could not match index with given geometry");
			}
		} else {
			if (index.hasChild()) {
				// We need to go deeper, but the necessary geometries don't exist yet...so we create them.
			}
			throw new GeometryIndexNotFoundException("Could not match index with given geometry");
		}
	}

	protected void delete(Geometry geom, GeometryIndex index) throws GeometryIndexNotFoundException {
		if (index.hasChild() && geom.getGeometries() != null && geom.getGeometries().length > index.getValue()) {
			delete(geom.getGeometries()[index.getValue()], index.getChild());
		} else if (index.getType() == GeometryIndexType.TYPE_VERTEX && geom.getCoordinates() != null
				&& geom.getCoordinates().length > index.getValue()) {
			// Delete a vertex:
			Coordinate[] result = new Coordinate[geom.getCoordinates().length - 1];
			int count = 0;
			for (int i = 0; i < geom.getCoordinates().length; i++) {
				if (i != index.getValue()) {
					result[count] = geom.getCoordinates()[i];
					count++;
				}
			}
			if (Geometry.LINEAR_RING.equals(geom.getGeometryType())) {
				result[result.length - 1] = new Coordinate(result[0]);
			}
			geom.setCoordinates(result);
		} else if (index.getType() == GeometryIndexType.TYPE_EDGE && geom.getCoordinates() != null
				&& geom.getCoordinates().length > index.getValue()) {
			// Delete an edge:
			Coordinate[] result = new Coordinate[geom.getCoordinates().length - 2];
			int count = 0;
			for (int i = 0; i < geom.getCoordinates().length; i++) {
				if (i != index.getValue() || i != index.getValue() + 1) {
					result[count] = geom.getCoordinates()[i];
					count++;
				}
			}
			geom.setCoordinates(result);
		} else if (index.getType() == GeometryIndexType.TYPE_GEOMETRY && geom.getGeometries() != null
				&& geom.getGeometries().length > index.getValue()) {
			// Delete a sub-geometry:
			// TODO implement me...
		} else {
			throw new GeometryIndexNotFoundException("Could not match index with given geometry");
		}
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private GeometryIndexImpl recursiveCreate(GeometryIndex index, int lastValue, GeometryIndexType lastType) {
		if (index.hasChild()) {
			return new GeometryIndexImpl(index.getType(), index.getValue(), recursiveCreate(index.getChild(),
					lastValue, lastType));
		} else {
			GeometryIndexType type = lastType == null ? index.getType() : lastType;
			return new GeometryIndexImpl(type, lastValue, null);
		}
	}

	private GeometryIndexCombo recursiveSearch(Geometry geometry, GeometryIndex index)
			throws GeometryIndexNotFoundException {
		// We need to stop at the last GEOMETRY level, so check for this first:
		if (!index.getType().equals(GeometryIndexType.TYPE_GEOMETRY) || !index.hasChild()) {
			return new GeometryIndexCombo(geometry, index);
		} else if (geometry.getGeometries() != null && geometry.getGeometries().length > index.getValue()) {
			// Go deeper:
			return recursiveSearch(geometry.getGeometries()[index.getValue()], index.getChild());
		}
		throw new GeometryIndexNotFoundException("Could not match index onto geometry.");
	}

	private int[] getAdjacentVerticesForVertex(Geometry geometry, GeometryIndex index)
			throws GeometryIndexNotFoundException {
		int n = 0;
		int i = index.getValue();

		if (i < 0) {
			throw new GeometryIndexNotFoundException("Cannot find a negative index.");
		} else if (geometry.getGeometryType().equals(Geometry.LINEAR_RING)) {
			n = geometry.getCoordinates() == null ? 0 : geometry.getCoordinates().length - 1;
		} else if (geometry.getGeometryType().equals(Geometry.LINE_STRING)) {
			n = geometry.getCoordinates() == null ? 0 : geometry.getCoordinates().length;
		}
		if (i >= n) {
			throw new GeometryIndexNotFoundException("Index too big.");
		} else if (n == 0) {
			return new int[] {};
		}

		int previous = (i - 1) % n;
		int next = (i + 1) % n;

		// A LineString is not closed, so check for this:
		if (geometry.getGeometryType().equals(Geometry.LINE_STRING)) {
			if (previous < 0) {
				return new int[] { next };
			} else if (next < i) {
				return new int[] { previous };
			}
		}

		if (previous < 0) {
			// This can only be executed in the case of a LinearRing:
			previous += n;
		}
		if (previous == next) {
			if (previous == i) {
				return new int[] {};
			} else {
				return new int[] { previous };
			}
		} else {
			return new int[] { previous, next };
		}
	}

	private int[] getAdjacentEdgesForVertex(Geometry geometry, GeometryIndex index)
			throws GeometryIndexNotFoundException {
		int n = 0;
		int i = index.getValue();

		if (i < 0) {
			throw new GeometryIndexNotFoundException("Cannot find a negative index.");
		} else if (geometry.getGeometryType().equals(Geometry.LINEAR_RING)
				|| geometry.getGeometryType().equals(Geometry.LINE_STRING)) {
			n = geometry.getCoordinates() == null ? 0 : geometry.getCoordinates().length - 1;
			if (i > n) {
				throw new GeometryIndexNotFoundException("Index too big.");
			}
		} else if (i >= n) {
			throw new GeometryIndexNotFoundException("Index too big.");
		}

		if (n == 0) {
			return new int[] {};
		}

		int previous = (i - 1) % n;
		int next = i % n;

		// A LineString is not closed, so check for this:
		if (geometry.getGeometryType().equals(Geometry.LINE_STRING)) {
			if (previous < 0) {
				return new int[] { next };
			} else if (next < i) {
				return new int[] { previous };
			}
		}

		if (previous < 0) {
			// This can only be executed in the case of a LinearRing:
			previous += n;
		}
		if (previous == next) {
			return new int[] { previous };
		} else {
			return new int[] { previous, next };
		}
	}

	private int[] getAdjacentVerticesForEdge(Geometry geometry, GeometryIndex index)
			throws GeometryIndexNotFoundException {
		// Edges always have 2 adjacent vertices.
		int n = 0;
		int i = index.getValue();

		if (i < 0) {
			throw new GeometryIndexNotFoundException("Cannot find a negative index.");
		} else if (geometry.getGeometryType().equals(Geometry.LINEAR_RING)
				|| geometry.getGeometryType().equals(Geometry.LINE_STRING)) {
			n = geometry.getCoordinates() == null ? 0 : geometry.getCoordinates().length - 1;
			if (i >= n) {
				throw new GeometryIndexNotFoundException("Index too big.");
			}
		} else {
			throw new GeometryIndexNotFoundException("Index too big.");
		}

		int previous = i;
		int next = i + 1;
		if (i == n - 1 && geometry.getGeometryType().equals(Geometry.LINEAR_RING)) {
			next = 0;
		}
		return new int[] { previous, next };
	}

	private int[] getAdjacentEdgesForEdge(Geometry geom, GeometryIndex index) throws GeometryIndexNotFoundException {
		int n = 0;
		int i = index.getValue();

		if (i < 0) {
			throw new GeometryIndexNotFoundException("Cannot find a negative index.");
		} else if (geom.getGeometryType().equals(Geometry.LINEAR_RING)
				|| geom.getGeometryType().equals(Geometry.LINE_STRING)) {
			n = geom.getCoordinates() == null ? 0 : geom.getCoordinates().length - 1;
			if (i >= n) {
				throw new GeometryIndexNotFoundException("Index too big.");
			}
		} else {
			throw new GeometryIndexNotFoundException("Index too big.");
		}

		int previous = (i - 1) % n;
		int next = (i + 1) % n;

		// A LineString is not closed, so check for this:
		if (geom.getGeometryType().equals(Geometry.LINE_STRING)) {
			if (previous < 0) {
				return new int[] { next };
			} else if (next < i) {
				return new int[] { previous };
			}
		}

		if (previous < 0) {
			// This can only be executed in the case of a LinearRing:
			previous += n;
		}
		if (previous == next) {
			if (previous == i) {
				return new int[] {};
			} else {
				return new int[] { previous };
			}
		} else {
			return new int[] { previous, next };
		}
	}

	// ------------------------------------------------------------------------
	// Private methods for ID parsing:
	// ------------------------------------------------------------------------

	private GeometryIndex parseRecursive(String id) throws GeometryIndexNotFoundException {
		int position = id.indexOf("geometry");
		if (position >= 0) {
			String temp = id.substring(position + 8);
			int value = readInteger(temp);
			if (value < 0) {
				throw new GeometryIndexNotFoundException("Could not read value from " + temp);
			}
			GeometryIndex childIndex = parseRecursive(temp);
			return new GeometryIndexImpl(GeometryIndexType.TYPE_GEOMETRY, value, childIndex);
		} else {
			GeometryIndex index = parseSingle(id, "vertex", GeometryIndexType.TYPE_VERTEX);
			if (index == null) {
				index = parseSingle(id, "edge", GeometryIndexType.TYPE_EDGE);
			}
			return index;
		}
	}

	private GeometryIndex parseSingle(String id, String rexexp, GeometryIndexType type)
			throws GeometryIndexNotFoundException {
		int position = id.indexOf(rexexp);
		if (position >= 0) {
			String temp = id.substring(position + rexexp.length());
			int value = readInteger(temp);
			if (value < 0) {
				throw new GeometryIndexNotFoundException("Could not read value from " + temp);
			}
			return new GeometryIndexImpl(type, value, null);
		}
		return null;
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

	// ------------------------------------------------------------------------
	// Private classes:
	// ------------------------------------------------------------------------

	/**
	 * Combines a geometry and an index. Used internally only.
	 * 
	 * @author Pieter De Graef
	 */
	private class GeometryIndexCombo {

		private Geometry geometry;

		private GeometryIndex index;

		protected GeometryIndexCombo(Geometry geometry, GeometryIndex index) {
			this.geometry = geometry;
			this.index = index;
		}

		protected GeometryIndex getIndex() {
			return index;
		}

		protected Geometry getGeometry() {
			return geometry;
		}
	}
}