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

import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;
import org.geomajas.plugin.editing.client.service.GeometryIndexService;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;

/**
 * Geometry index operation that inserts a single vertex after the given index. Supported index types are vertex and
 * edge. This implementation does not create a new geometry instance, but changes the given geometry.
 * 
 * @author Pieter De Graef
 */
public class InsertGeometryOperation implements GeometryIndexOperation {

	private final GeometryIndexService service;

	private final Geometry child;

	private GeometryIndex index;

	/**
	 * Initialize this operation with an indexing service.
	 * 
	 * @param service
	 *            geometry index service.
	 * @param child
	 *            The child geometry to insert.
	 */
	public InsertGeometryOperation(GeometryIndexService service, Geometry child) {
		this.service = service;
		this.child = child;
	}

	/** {@inheritDoc} */
	public Geometry execute(Geometry geometry, GeometryIndex index) throws GeometryOperationFailedException {
		this.index = index;
		if (service.getType(index) != GeometryIndexType.TYPE_GEOMETRY) {
			throw new GeometryOperationFailedException("Index of wrong type. Must be TYPE_GEOMETRY.");
		}
		try {
			insert(geometry, index, child);
			return geometry;
		} catch (GeometryIndexNotFoundException e) {
			throw new GeometryOperationFailedException(e);
		}
	}

	/** {@inheritDoc} */
	public GeometryIndexOperation getInverseOperation() {
		return new DeleteGeometryOperation(service);
	}

	/** {@inheritDoc} */
	public GeometryIndex getGeometryIndex() {
		return index;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void insert(Geometry geom, GeometryIndex index, Geometry child) throws GeometryIndexNotFoundException {
		if (index.hasChild() && geom.getGeometries() != null && geom.getGeometries().length > index.getValue()) {
			insert(geom.getGeometries()[index.getValue()], index.getChild(), child);
		} else if (checkType(geom, child)) {
			if (geom.getGeometries() == null && index.getValue() == 0) {
				geom.setGeometries(new Geometry[] { child });
			} else if (geom.getGeometries() == null || index.getValue() < 0
					|| index.getValue() > geom.getGeometries().length) {
				throw new GeometryIndexNotFoundException("Geometry index out of bounds.");
			} else {
				Geometry[] result = new Geometry[geom.getGeometries().length + 1];
				int count = 0;
				for (int i = 0; i < result.length; i++) {
					if (i == index.getValue()) {
						result[i] = child;
					} else {
						result[i] = geom.getGeometries()[count];
						count++;
					}
				}
				geom.setGeometries(result);
			}
		} else {
			throw new GeometryIndexNotFoundException("Cannot insert geometry at the requested location.");
		}
	}

	private boolean checkType(Geometry parent, Geometry child) {
		if (Geometry.POLYGON.equals(parent.getGeometryType()) && Geometry.LINEAR_RING.equals(child.getGeometryType())) {
			return true;
		} else if (Geometry.MULTI_POLYGON.equals(parent.getGeometryType())
				&& Geometry.POLYGON.equals(child.getGeometryType())) {
			return true;
		} else if (Geometry.MULTI_LINE_STRING.equals(parent.getGeometryType())
				&& Geometry.LINE_STRING.equals(child.getGeometryType())) {
			return true;
		} else if (Geometry.MULTI_POINT.equals(parent.getGeometryType())
				&& Geometry.POINT.equals(child.getGeometryType())) {
			return true;
		}
		return false;
	}
}