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

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.service.GeometryIndex;

/**
 * Definition of an operation onto a geometry that changes that geometries' shape. Example are translating, inserting or
 * deleting vertices or sub-geometries.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface GeometryIndexOperation {

	/**
	 * Given a certain geometry and an index into that geometry, execute some operation onto that specific index
	 * resulting in the returned geometry.
	 * 
	 * @param geometry
	 *            The initial geometry.
	 * @param index
	 *            The index into the initial geometry, that points to the part where upon the operation takes place.
	 * @return The resulting geometry after the operation was successfully.
	 * @throws GeometryOperationFailedException
	 *             In case something goes wrong during the operation execution. All implementations must make sure that
	 *             if an exception occurs, no changes are made to the initial geometry.
	 */
	Geometry execute(Geometry geometry, GeometryIndex index) throws GeometryOperationFailedException;

	/**
	 * Get the inverse operation of this operation. The inverse can for example be used to undo the result of this
	 * operation.
	 * 
	 * @return The inverse operation.
	 */
	GeometryIndexOperation getInverseOperation();

	/**
	 * Get the index (vertex/edge/sub-geometry) onto which this operation executes.
	 * 
	 * @return The index onto which this operation executes.
	 */
	GeometryIndex getGeometryIndex();
}