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

package org.geomajas.plugin.editing.jsapi.client.service;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;
import org.geomajas.plugin.editing.client.service.GeometryIndexService;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;

/**
 * Service for managing sub-parts of geometries through special geometry indices.
 * <p>
 * TODO All methods that return an array or a list have been excluded. We still need to get these to work...
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Export("GeometryIndexService")
@ExportPackage("org.geomajas.plugin.editing.service")
@Api(allMethods = true)
public class JsGeometryIndexService implements ExportOverlay<GeometryIndexService> {

	/**
	 * Create a new geometry index instance.
	 * 
	 * @param instance
	 *            The index service instance to help you create the index.
	 * @param type
	 *            The type for the deepest index value.
	 * @param values
	 *            A list of values for the children to create.
	 * @return The new index.
	 */
	@ExportInstanceMethod
	public static GeometryIndex create(GeometryIndexService instance, String type, int[] values) {
		if ("geometry".equalsIgnoreCase(type)) {
			return instance.create(GeometryIndexType.TYPE_GEOMETRY, values);
		} else if ("vertex".equalsIgnoreCase(type)) {
			return instance.create(GeometryIndexType.TYPE_VERTEX, values);
		} else if ("edge".equalsIgnoreCase(type)) {
			return instance.create(GeometryIndexType.TYPE_EDGE, values);
		}
		return null;
	}

	/**
	 * Create a new geometry index instance, by adding more depth to a given parent index.
	 * 
	 * @param instance
	 *            The index service instance to help you create the index.
	 * @param index
	 *            The parent index to start from.
	 * @param type
	 *            The type for the deepest index value.
	 * @param values
	 *            A list of values for the children to attach to the parent index.
	 * @return The next index.
	 */
	@ExportInstanceMethod
	public static GeometryIndex addChildren(GeometryIndexService instance, GeometryIndex index, String type,
			int[] values) {
		if ("geometry".equalsIgnoreCase(type)) {
			return instance.addChildren(index, GeometryIndexType.TYPE_GEOMETRY, values);
		} else if ("vertex".equalsIgnoreCase(type)) {
			return instance.addChildren(index, GeometryIndexType.TYPE_VERTEX, values);
		} else if ("edge".equalsIgnoreCase(type)) {
			return instance.addChildren(index, GeometryIndexType.TYPE_EDGE, values);
		}
		return null;
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
		return null;
	}

	/**
	 * Given a certain string identifier, parse it as a geometry index.
	 * 
	 * @param identifier
	 *            The identifier to try and parse.
	 * @return Returns the associating geometry index (if no exception was thrown).
	 * @throws GeometryIndexNotFoundException
	 *             In case the identifier could not be parsed.
	 */
	public GeometryIndex parse(String identifier) throws GeometryIndexNotFoundException {
		return null;
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
		return null;
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
		return null;
	}

	//
	// public Coordinate[] getEdge(Geometry geometry, GeometryIndex index) throws GeometryIndexNotFoundException {
	// return null;
	// }

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
		return false;
	}

	/**
	 * Does the given index point to an edge or not? We look at the deepest level to check this.
	 * 
	 * @param index
	 *            The index to check.
	 * @return true or false.
	 */
	public boolean isEdge(GeometryIndex index) {
		return false;
	}

	/**
	 * Does the given index point to a sub-geometry or not? We look at the deepest level to check this.
	 * 
	 * @param index
	 *            The index to check.
	 * @return true or false.
	 */
	public boolean isGeometry(GeometryIndex index) {
		return false;
	}

	/**
	 * Get the type of sub-part the given index points to. We look at the deepest level to check this.
	 * 
	 * @param index
	 *            The index to check.
	 * @return true or false.
	 */
	@ExportInstanceMethod
	public static String getType(GeometryIndexService instance, GeometryIndex index) {
		switch (instance.getType(index)) {
			case TYPE_GEOMETRY:
				return "geometry";
			case TYPE_VERTEX:
				return "vertex";
			case TYPE_EDGE:
				return "edge";
			default:
				return "unknown";
		}
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
		return null;
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
		return 0;
	}

	// ------------------------------------------------------------------------
	// Methods concerning adjacency (finding ones neighbors):
	// ------------------------------------------------------------------------

	//
	// public List<GeometryIndex> getAdjacentVertices(Geometry geometry, GeometryIndex index)
	// throws GeometryIndexNotFoundException {
	// return null;
	// }
	//
	// public List<GeometryIndex> getAdjacentEdges(Geometry geometry, GeometryIndex index)
	// throws GeometryIndexNotFoundException {
	// return null;
	// }

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
		return null;
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
		return null;
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
		return 0;
	}
	//
	// public Coordinate[] getSiblingVertices(Geometry geometry, GeometryIndex index)
	// throws GeometryIndexNotFoundException {
	// return null;
	// }
}