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

package org.geomajas.plugin.editing.client.operation;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;
import org.geomajas.plugin.editing.client.service.GeometryIndexService;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;

/**
 * <p>
 * Geometry index operation that inserts a single vertex after the given index. Supported index types are vertex and
 * edge. This implementation does not create a new geometry instance, but changes the given geometry.
 * </p>
 * <p>
 * Note that this operation will only insert into existing geometries. It will never create new sub-geometries. For
 * example, if you want to add an extra Point to a MultiPoint, then first make sure there is an empty Point wherein this
 * operation can insert the vertex.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class InsertVertexOperation implements GeometryIndexOperation {

	private final GeometryIndexService service;

	private final Coordinate coordinate;

	private GeometryIndex index;

	/**
	 * Initialize this operation with an indexing service.
	 * 
	 * @param service
	 *            geometry index service.
	 */
	public InsertVertexOperation(GeometryIndexService service, Coordinate coordinate) {
		this.service = service;
		this.coordinate = coordinate;
	}

	/** {@inheritDoc} */
	public Geometry execute(Geometry geometry, GeometryIndex index) throws GeometryOperationFailedException {
		this.index = index;
		if (service.getType(index) != GeometryIndexType.TYPE_VERTEX
				&& service.getType(index) != GeometryIndexType.TYPE_EDGE) {
			throw new GeometryOperationFailedException("Index of wrong type. Must be TYPE_VERTEX or TYPE_EDGE.");
		}
		try {
			insert(geometry, index, coordinate);
			return geometry;
		} catch (GeometryIndexNotFoundException e) {
			throw new GeometryOperationFailedException(e);
		}
	}

	/** {@inheritDoc} */
	public GeometryIndexOperation getInverseOperation() {
		return new DeleteVertexOperation(service);
	}

	/** {@inheritDoc} */
	public GeometryIndex getGeometryIndex() {
		switch (service.getType(index)) {
			case TYPE_EDGE:
				return service.getNextVertex(index);
			default:
				return index;
		}
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void insert(Geometry geom, GeometryIndex index, Coordinate coordinate)
			throws GeometryIndexNotFoundException {
		if (index.hasChild() && geom.getGeometries() != null && geom.getGeometries().length > index.getValue()) {
			insert(geom.getGeometries()[index.getValue()], index.getChild(), coordinate);
		} else if (index.getType() == GeometryIndexType.TYPE_EDGE) {
			insertAfterEdge(geom, index, coordinate);
		} else if (index.getType() == GeometryIndexType.TYPE_VERTEX) {
			insertAfterVertex(geom, index, coordinate);
		} else {
			throw new GeometryIndexNotFoundException("Could not match index with given geometry.");
		}
	}

	/**
	 * Insert a point into a given edge. There can be only edges if there are at least 2 points in a LineString
	 * geometry.
	 */
	private void insertAfterEdge(Geometry geom, GeometryIndex index, Coordinate coordinate)
			throws GeometryIndexNotFoundException {
		// First we check the geometry type:
		if (!Geometry.LINE_STRING.equals(geom.getGeometryType())
				&& !Geometry.LINEAR_RING.equals(geom.getGeometryType())) {
			throw new GeometryIndexNotFoundException("Could not match index with given geometry.");
		}
		if (index.getValue() < 0) {
			throw new GeometryIndexNotFoundException("Cannot insert in a negative index.");
		}

		// Then we check if the edge exists:
		if (geom.getCoordinates() != null && geom.getCoordinates().length > index.getValue() + 1) {
			// Inserting on edges allows only to insert on existing edges. No adding at the end:
			Coordinate[] result = new Coordinate[geom.getCoordinates().length + 1];
			int count = 0;
			for (int i = 0; i < geom.getCoordinates().length; i++) {
				if (i == (index.getValue() + 1)) {
					result[i] = coordinate;
					count++;
				}
				result[i + count] = geom.getCoordinates()[i];
			}
			geom.setCoordinates(result);
		} else {
			throw new GeometryIndexNotFoundException("Cannot insert a vertex into an edge that does not exist.");
		}
	}

	private void insertAfterVertex(Geometry geom, GeometryIndex index, Coordinate coordinate)
			throws GeometryIndexNotFoundException {
		// First we check the geometry type (allow only Point, LineString and LinearRing):
		if (Geometry.POINT.equals(geom.getGeometryType())) {
			if (index.getValue() != 0 || geom.getCoordinates() != null) {
				throw new GeometryIndexNotFoundException("A point can have only one coordinate.");
			}
			geom.setCoordinates(new Coordinate[] { coordinate });
		} else if (Geometry.LINE_STRING.equals(geom.getGeometryType())) {
			if (geom.getCoordinates() == null && index.getValue() == 0) {
				geom.setCoordinates(new Coordinate[] { coordinate });
			} else if (geom.getCoordinates() == null || index.getValue() < 0
					|| index.getValue() > geom.getCoordinates().length) {
				throw new GeometryIndexNotFoundException("Vertex index out of bounds.");
			} else {
				Coordinate[] result = new Coordinate[geom.getCoordinates().length + 1];
				int count = 0;
				for (int i = 0; i < result.length; i++) {
					if (i == index.getValue()) {
						result[i] = coordinate;
					} else {
						result[i] = geom.getCoordinates()[count];
						count++;
					}
				}
				geom.setCoordinates(result);
			}
		} else if (Geometry.LINEAR_RING.equals(geom.getGeometryType())) {
			if (geom.getCoordinates() == null && index.getValue() == 0) {
				geom.setCoordinates(new Coordinate[] { coordinate, coordinate });
			} else if (geom.getCoordinates() == null || index.getValue() < 0
					|| index.getValue() > geom.getCoordinates().length - 1) {
				throw new GeometryIndexNotFoundException("Vertex index out of bounds.");
			} else {
				Coordinate[] result = new Coordinate[geom.getCoordinates().length + 1];
				int count = 0;
				for (int i = 0; i < result.length; i++) {
					if (i == index.getValue()) {
						result[i] = coordinate;
					} else if (i < result.length - 1) {
						result[i] = geom.getCoordinates()[count];
						count++;
					} else {
						result[i] = new Coordinate(result[0]);
					}
				}
				geom.setCoordinates(result);
			}
		} else {
			throw new GeometryIndexNotFoundException("Could not match index with given geometry.");
		}
	}
}