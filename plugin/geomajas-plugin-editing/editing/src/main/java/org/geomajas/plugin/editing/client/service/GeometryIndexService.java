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

package org.geomajas.plugin.editing.client.service;

import java.util.List;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;

/**
 * Service for managing sub-parts of geometries through special geometry indices.
 * 
 * @author Pieter De Graef
 */
public interface GeometryIndexService {

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
	GeometryIndex create(GeometryIndexType type, int... values);

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
	GeometryIndex addChildren(GeometryIndex index, GeometryIndexType type, int... values);

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
	String format(GeometryIndex index);

	/**
	 * Given a certain string identifier, parse it as a geometry index.
	 * 
	 * @param identifier
	 *            The identifier to try and parse.
	 * @return Returns the associating geometry index (if no exception was thrown).
	 * @throws GeometryIndexNotFoundException
	 *             In case the identifier could not be parsed.
	 */
	GeometryIndex parse(String identifier) throws GeometryIndexNotFoundException;

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
	Geometry getGeometry(Geometry geometry, GeometryIndex index) throws GeometryIndexNotFoundException;

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
	Coordinate getVertex(Geometry geometry, GeometryIndex index) throws GeometryIndexNotFoundException;

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
	Coordinate[] getEdge(Geometry geometry, GeometryIndex index) throws GeometryIndexNotFoundException;

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
	boolean isVertex(GeometryIndex index);

	/**
	 * Does the given index point to an edge or not? We look at the deepest level to check this.
	 * 
	 * @param index
	 *            The index to check.
	 * @return true or false.
	 */
	boolean isEdge(GeometryIndex index);

	/**
	 * Does the given index point to a sub-geometry or not? We look at the deepest level to check this.
	 * 
	 * @param index
	 *            The index to check.
	 * @return true or false.
	 */
	boolean isGeometry(GeometryIndex index);

	/**
	 * Get the type of sub-part the given index points to. We look at the deepest level to check this.
	 * 
	 * @param index
	 *            The index to check.
	 * @return true or false.
	 */
	GeometryIndexType getType(GeometryIndex index);

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
	List<GeometryIndex> getAdjacentVertices(Geometry geometry, GeometryIndex index)
			throws GeometryIndexNotFoundException;

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
	List<GeometryIndex> getAdjacentEdges(Geometry geometry, GeometryIndex index) throws GeometryIndexNotFoundException;

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
	boolean isAdjacent(Geometry geometry, GeometryIndex one, GeometryIndex two);

	/**
	 * Given a certain index, find the next vertex in line.
	 * 
	 * @param index
	 *            The index to start out from. Must point to either a vertex or and edge.
	 * @return Returns the next vertex index. Note that no geometry is given, and so no actual checking is done. It just
	 *         returns the theoretical answer.
	 */
	GeometryIndex getNextVertex(GeometryIndex index);

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
	int getSiblingCount(Geometry geometry, GeometryIndex index);

	/**
	 * What is the geometry type of the deepest index node of type geometry.
	 * 
	 * @param geometry
	 *            The geometry wherein to search.
	 * @param index
	 *            The index pointing to a vertex/edge/sub-geometry. In the case of a vertex/edge, the parent geometry
	 *            type is returned.
	 * @return The geometry type as defined in the {@link Geometry} class.
	 */
	String getGeometryType(Geometry geometry, GeometryIndex index);
}