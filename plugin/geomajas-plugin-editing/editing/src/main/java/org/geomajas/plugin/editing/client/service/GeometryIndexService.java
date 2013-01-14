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

package org.geomajas.plugin.editing.client.service;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;

/**
 * Service for managing sub-parts of geometries through special geometry indices.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class GeometryIndexService {

	// ------------------------------------------------------------------------
	// Methods concerning index construction:
	// ------------------------------------------------------------------------

	/**
	 * Create a new index given a type and a list of values. This index will be built recursively and will have a depth
	 * equal to the number of values given.
	 * 
	 * @param type
	 *            The type of index applied on the deepest child index. Note that all parent index nodes will be of the
	 *            type <code>GeometryIndexType.TYPE_GEOMETRY</code>. Only the deepest child will use this given type.
	 * @param values
	 *            A list of integer values that determine the indices on each level in the index.
	 * @return The recursive geometry index resulting from the given parameters.
	 */
	public GeometryIndex create(GeometryIndexType type, int... values) {
		GeometryIndex index;
		if (values.length > 0) {
			index = new GeometryIndex(type, values[values.length - 1], null);
		} else {
			throw new IllegalArgumentException("Cannot create a GeometryIndex since no values where given.");
		}
		if (values.length > 1) {
			for (int i = values.length - 2; i >= 0; i--) {
				index = new GeometryIndex(GeometryIndexType.TYPE_GEOMETRY, values[i], index);
			}
		}
		return index;
	}

	/**
	 * Given a certain geometry index, add more levels to it.
	 * 
	 * @param index
	 *            The index to start out from.
	 * @param type
	 *            Add more levels to it, where the deepest level should be of this type.
	 * @param values
	 *            A list of integer values that determine the indices on each level in the index.
	 * @return The recursive geometry index resulting from adding the given parameters to the given parent index.
	 */
	public GeometryIndex addChildren(GeometryIndex index, GeometryIndexType type, int... values) {
		if (index == null) {
			return create(type, values);
		}
		if (getType(index) != GeometryIndexType.TYPE_GEOMETRY) {
			throw new IllegalArgumentException("Can only add children to an index of type geometry.");
		}
		GeometryIndex clone = new GeometryIndex(index);

		GeometryIndex deepestChild = clone;
		while (deepestChild.hasChild()) {
			deepestChild = deepestChild.getChild();
		}
		deepestChild.setChild(create(type, values));
		return clone;
	}

	// ------------------------------------------------------------------------
	// Methods concerning index parsing/formatting:
	// ------------------------------------------------------------------------

	/**
	 * Format a given geometry index, creating something like "geometry2.vertex1".
	 * 
	 * @param index
	 *            The geometry index to format.
	 * @return Returns the string value resulting from the index.
	 */
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

	/**
	 * Given a certain string identifier, parse it as a geometry index.
	 * 
	 * @param id
	 *            The identifier to try and parse.
	 * @return Returns the associating geometry index (if no exception was thrown).
	 * @throws GeometryIndexNotFoundException
	 *             In case the identifier could not be parsed.
	 */
	public GeometryIndex parse(String id) throws GeometryIndexNotFoundException {
		try {
			GeometryIndex index = parseRecursive(id.toLowerCase());
			if (index == null) {
				throw new GeometryIndexNotFoundException("Could not parse '" + id + "' as a GeometryIndex.");
			}
			return index;
		} catch (GeometryIndexNotFoundException e) {
			throw new GeometryIndexNotFoundException("Could not parse '" + id + "' as a GeometryIndex.", e);
		}
	}

	// ------------------------------------------------------------------------
	// Methods for geometry retrieval:
	// ------------------------------------------------------------------------

	/**
	 * Given a certain geometry, get the sub-geometry the index points to. This only works if the index actually points
	 * to a sub-geometry.
	 * 
	 * @param geometry
	 *            The geometry to search in.
	 * @param index
	 *            The index that points to a sub-geometry within the given geometry.
	 * @return Returns the sub-geometry if it exists.
	 * @throws GeometryIndexNotFoundException
	 *             Thrown in case the index is of the wrong type, or if the sub-geometry could not be found within the
	 *             given geometry.
	 */
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

	/**
	 * Given a certain geometry, get the vertex the index points to. This only works if the index actually points to a
	 * vertex.
	 * 
	 * @param geometry
	 *            The geometry to search in.
	 * @param index
	 *            The index that points to a vertex within the given geometry.
	 * @return Returns the vertex if it exists.
	 * @throws GeometryIndexNotFoundException
	 *             Thrown in case the index is of the wrong type, or if the vertex could not be found within the given
	 *             geometry.
	 */
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

	/**
	 * Given a certain geometry, get the edge the index points to. This only works if the index actually points to an
	 * edge.
	 * 
	 * @param geometry
	 *            The geometry to search in.
	 * @param index
	 *            The index that points to an edge within the given geometry.
	 * @return Returns the edge if it exists.
	 * @throws GeometryIndexNotFoundException
	 *             Thrown in case the index is of the wrong type, or if the edge could not be found within the given
	 *             geometry.
	 */
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

	// ------------------------------------------------------------------------
	// Helper methods:
	// ------------------------------------------------------------------------

	/**
	 * Does the given index point to a vertex or not? We look at the deepest level to check this.
	 * 
	 * @param index
	 *            The index to check.
	 * @return true or false.
	 */
	public boolean isVertex(GeometryIndex index) {
		if (index.hasChild()) {
			return isVertex(index.getChild());
		}
		return index.getType() == GeometryIndexType.TYPE_VERTEX;
	}

	/**
	 * Does the given index point to an edge or not? We look at the deepest level to check this.
	 * 
	 * @param index
	 *            The index to check.
	 * @return true or false.
	 */
	public boolean isEdge(GeometryIndex index) {
		if (index.hasChild()) {
			return isEdge(index.getChild());
		}
		return index.getType() == GeometryIndexType.TYPE_EDGE;
	}

	/**
	 * Does the given index point to a sub-geometry or not? We look at the deepest level to check this.
	 * 
	 * @param index
	 *            The index to check.
	 * @return true or false.
	 */
	public boolean isGeometry(GeometryIndex index) {
		if (index.hasChild()) {
			return isGeometry(index.getChild());
		}
		return index.getType() == GeometryIndexType.TYPE_GEOMETRY;
	}

	/**
	 * Get the type of sub-part the given index points to. We look at the deepest level to check this.
	 * 
	 * @param index
	 *            The index to check.
	 * @return true or false.
	 */
	public GeometryIndexType getType(GeometryIndex index) {
		if (index.hasChild()) {
			return getType(index.getChild());
		}
		return index.getType();
	}

	/**
	 * What is the geometry type of the sub-geometry pointed to by the given index? If the index points to a vertex or
	 * edge, the geometry type at the parent level is returned.
	 * 
	 * @param geometry
	 *            The geometry wherein to search.
	 * @param index
	 *            The index pointing to a vertex/edge/sub-geometry. In the case of a vertex/edge, the parent geometry
	 *            type is returned. If index is null, the type of the given geometry is returned.
	 * @return The geometry type as defined in the {@link Geometry} class.
	 * @throws GeometryIndexNotFoundException
	 *             Thrown in case the index points to a non-existing sub-geometry.
	 */
	public String getGeometryType(Geometry geometry, GeometryIndex index) throws GeometryIndexNotFoundException {
		if (index != null && index.getType() == GeometryIndexType.TYPE_GEOMETRY) {
			if (geometry.getGeometries() != null && geometry.getGeometries().length > index.getValue()) {
				return getGeometryType(geometry.getGeometries()[index.getValue()], index.getChild());
			} else {
				throw new GeometryIndexNotFoundException("Can't find the geometry referred to in the given index.");
			}
		}
		return geometry.getGeometryType();
	}

	/**
	 * Checks to see if a given index is the child of another index.
	 * 
	 * @param parentIndex
	 *            The so-called parent index.
	 * @param childIndex
	 *            The so-called child index.
	 * @return Is the second index really a child of the first index?
	 */
	public boolean isChildOf(GeometryIndex parentIndex, GeometryIndex childIndex) {
		if (parentIndex.getValue() != childIndex.getValue()) {
			return false;
		}
		if (parentIndex.hasChild() && childIndex.hasChild()) {
			return isChildOf(parentIndex.getChild(), childIndex.getChild());
		} else if (!parentIndex.hasChild() && childIndex.hasChild()) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the value of the innermost child index.
	 * 
	 * @param index
	 *            The index to recursively search.
	 * @return The value of the deepest child.
	 */
	public int getValue(GeometryIndex index) {
		if (index.hasChild()) {
			return getValue(index.getChild());
		}
		return index.getValue();
	}

	// ------------------------------------------------------------------------
	// Methods concerning adjacency (finding ones neighbors):
	// ------------------------------------------------------------------------

	/**
	 * Given a certain geometry and index, find the neighboring vertices. It is important to understand that searching
	 * vertices within a closed ring will always return 2 vertices (unless the ring contains only 1 or 2 coordinates),
	 * while searching within a LineString can yield different results (the beginning or end only has 1 neighbor).
	 * 
	 * @param geometry
	 *            The geometry wherein to search for neighboring vertices.
	 * @param index
	 *            The index to start out from. Must point to either a vertex or and edge.
	 * @return The list of neighboring vertices.
	 * @throws GeometryIndexNotFoundException
	 *             Thrown in case the given index does not match the given geometry.
	 */
	public List<GeometryIndex> getAdjacentVertices(Geometry geometry, GeometryIndex index)
			throws GeometryIndexNotFoundException {
		if (geometry == null || index == null) {
			throw new IllegalArgumentException("No null values allowed!");
		}
		GeometryIndexCombo combo = recursiveSearch(geometry, index);

		int[] indices = new int[] {};
		if (isVertex(index)) {
			indices = getAdjacentVerticesForVertex(combo.getGeometry(), combo.getIndex());
		} else if (isEdge(index)) {
			indices = getAdjacentVerticesForEdge(combo.getGeometry(), combo.getIndex());
		}

		List<GeometryIndex> indexList = new ArrayList<GeometryIndex>();
		for (int current : indices) {
			indexList.add(recursiveCreate(index, current, GeometryIndexType.TYPE_VERTEX));
		}

		// Can return an empty list.
		return indexList;
	}

	/**
	 * Given a certain geometry and index, find the neighboring edges. It is important to understand that searching
	 * edges within a closed ring will always return 2 results (unless the ring contains only 1 or 2 coordinates), while
	 * searching within a LineString can yield different results (the beginning or end only has 1 neighbor).
	 * 
	 * @param geometry
	 *            The geometry wherein to search for neighboring edges.
	 * @param index
	 *            The index to start out from. Must point to either a vertex or and edge.
	 * @return The list of neighboring edges.
	 * @throws GeometryIndexNotFoundException
	 *             Thrown in case the given index does not match the given geometry.
	 */
	public List<GeometryIndex> getAdjacentEdges(Geometry geometry, GeometryIndex index)
			throws GeometryIndexNotFoundException {
		if (geometry == null || index == null) {
			throw new IllegalArgumentException("No null values allowed!");
		}
		GeometryIndexCombo combo = recursiveSearch(geometry, index);
		int[] indices = new int[] {};
		if (isVertex(index)) {
			indices = getAdjacentEdgesForVertex(combo.getGeometry(), combo.getIndex());
		} else if (isEdge(index)) {
			indices = getAdjacentEdgesForEdge(combo.getGeometry(), combo.getIndex());
		}

		List<GeometryIndex> indexList = new ArrayList<GeometryIndex>();
		for (int indice : indices) {
			indexList.add(recursiveCreate(index, indice, GeometryIndexType.TYPE_EDGE));
		}

		// Can return an empty list.
		return indexList;
	}

	/**
	 * Given a certain geometry and index (one), check if the the other index (two) is a neighbor.
	 * 
	 * @param geometry
	 *            The geometry wherein to search if indices one and two are neighbors.
	 * @param one
	 *            One of the indices. Must point to either a vertex or and edge.
	 * @param two
	 *            Another one of the indices. Must point to either a vertex or and edge.
	 * @return true or false.
	 */
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

	/**
	 * Given a certain index, find the next vertex in line.
	 * 
	 * @param index
	 *            The index to start out from. Must point to either a vertex or and edge.
	 * @return Returns the next vertex index. Note that no geometry is given, and so no actual checking is done. It just
	 *         returns the theoretical answer.
	 */
	public GeometryIndex getNextVertex(GeometryIndex index) {
		if (index.hasChild()) {
			return new GeometryIndex(index.getType(), index.getValue(), getNextVertex(index.getChild()));
		} else {
			return new GeometryIndex(GeometryIndexType.TYPE_VERTEX, index.getValue() + 1, null);
		}
	}

	/**
	 * Given a certain index, find the previous vertex in line.
	 * 
	 * @param index
	 *            The index to start out from. Must point to either a vertex or and edge.
	 * @return Returns the previous vertex index. Note that no geometry is given, and so no actual checking is done. It
	 *         just returns the theoretical answer.
	 */
	public GeometryIndex getPreviousVertex(GeometryIndex index) {
		if (index.hasChild()) {
			return new GeometryIndex(index.getType(), index.getValue(), getPreviousVertex(index.getChild()));
		} else {
			return new GeometryIndex(GeometryIndexType.TYPE_VERTEX, index.getValue() - 1, null);
		}
	}

	/**
	 * Given a certain index, how many indices of the same type can be found within the given geometry. This count
	 * includes the given index.<br>
	 * For example, if the index points to a vertex on a LinearRing within a polygon, then this will return the amount
	 * of vertices on that LinearRing.
	 * 
	 * @param geometry
	 *            The geometry to look into.
	 * @param index
	 *            The index to take as example (can be of any type).
	 * @return Returns the total amount of siblings.
	 */
	public int getSiblingCount(Geometry geometry, GeometryIndex index) {
		if (index.hasChild() && geometry.getGeometries() != null && 
				geometry.getGeometries().length > index.getValue()) {
			return getSiblingCount(geometry.getGeometries()[index.getValue()], index.getChild());
		}
		switch (index.getType()) {
			case TYPE_VERTEX:
				return geometry.getCoordinates() != null ? geometry.getCoordinates().length : 0;
			case TYPE_EDGE:
				if (Geometry.LINE_STRING.equals(geometry.getGeometryType())) {
					int count = geometry.getCoordinates() != null ? geometry.getCoordinates().length - 1 : 0;
					if (count < 0) {
						count = 0;
					}
					return count;
				} else if (Geometry.LINEAR_RING.equals(geometry.getGeometryType())) {
					return geometry.getCoordinates() != null ? geometry.getCoordinates().length : 0;
				}
				return 0;
			case TYPE_GEOMETRY:
			default:
				return geometry.getGeometries() != null ? geometry.getGeometries().length : 0;
		}
	}

	/**
	 * Get the full list of sibling vertices in the form of a coordinate array.
	 * 
	 * @param geometry
	 *            The geometry wherein to search for a certain coordinate array.
	 * @param index
	 *            An index pointing to a vertex or edge within the geometry. This index will then naturally be a part of
	 *            a coordinate array. It is this array we're looking for.
	 * @return Returns the array of coordinate from within the geometry where the given index is a part of.
	 * @throws GeometryIndexNotFoundException
	 *             geometry index not found
	 */
	public Coordinate[] getSiblingVertices(Geometry geometry, GeometryIndex index)
			throws GeometryIndexNotFoundException {
		if (index.hasChild() && geometry.getGeometries() != null && 
				geometry.getGeometries().length > index.getValue()) {
			return getSiblingVertices(geometry.getGeometries()[index.getValue()], index.getChild());
		}
		switch (index.getType()) {
			case TYPE_VERTEX:
			case TYPE_EDGE:
				return geometry.getCoordinates();
			case TYPE_GEOMETRY:
			default:
				throw new GeometryIndexNotFoundException("Given index is of wrong type. Can't find sibling vertices "
						+ "for a geometry type of index.");
		}
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private GeometryIndex recursiveCreate(GeometryIndex index, int lastValue, GeometryIndexType lastType) {
		if (index.hasChild()) {
			return new GeometryIndex(index.getType(), index.getValue(), recursiveCreate(index.getChild(), lastValue,
					lastType));
		} else {
			GeometryIndexType type = lastType == null ? index.getType() : lastType;
			return new GeometryIndex(type, lastValue, null);
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
		int n;
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
		int n;
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
			return new GeometryIndex(GeometryIndexType.TYPE_GEOMETRY, value, childIndex);
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
			return new GeometryIndex(type, value, null);
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
		} catch (Exception e) { // NOSONAR
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
	private static class GeometryIndexCombo {

		private final Geometry geometry;

		private final GeometryIndex index;

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