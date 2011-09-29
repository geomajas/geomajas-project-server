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

package org.geomajas.plugin.editing.client.operation;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.service.GeometryIndex;

/**
 * Definition of an operation onto a geometry that changes that geometries' shape. Example are translating, inserting or
 * deleting vertices.
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
	 * Undo this operation. The resulting geometry is again the initial geometry before this operation was executed.
	 * 
	 * @param geometry
	 *            The transformed geometry after execution.
	 * @return The original geometry before execution.
	 * @throws GeometryOperationFailedException
	 *             In case something goes wrong during the operation undo. All implementations must make sure that if an
	 *             exception occurs, no changes are made to the initial geometry.
	 */
	Geometry undo(Geometry geometry) throws GeometryOperationFailedException;
}