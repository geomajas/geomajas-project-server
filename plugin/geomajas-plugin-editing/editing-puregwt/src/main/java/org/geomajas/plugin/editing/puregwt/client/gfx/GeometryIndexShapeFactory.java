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

package org.geomajas.plugin.editing.puregwt.client.gfx;

import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;
import org.vaadin.gwtgraphics.client.VectorObject;

/**
 * Factory definition that creates {@link VectorObject}s from geometry indices. Depending on the kind of index (vertex, edge,
 * geometry), different types of shape will be created.
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 */
public interface GeometryIndexShapeFactory {

	/**
	 * Create a suitable shape that may represent the given geometry index on the map.
	 * 
	 * @param editService
	 *            The editing service.
	 * @param index
	 *            The index for which to create a shape.
	 * @return The shape for the index.
	 * @throws GeometryIndexNotFoundException
	 *             Thrown in case the index can't be found within the geometry being edited, or if the editing service
	 *             hasn't started.
	 */
	VectorObject create(GeometryEditService editService, GeometryIndex index) throws GeometryIndexNotFoundException;

	/**
	 * Update the location of the given shape.
	 * 
	 * @param shape
	 *            The shape to be updated.
	 * @param editService
	 *            The editing service.
	 * @param index
	 *            The index that contains the new location for the shape.
	 * @throws GeometryIndexNotFoundException
	 *             Thrown in case the index can't be found within the geometry being edited, or if the editing service
	 *             hasn't started.
	 */
	void update(VectorObject shape, GeometryEditService editService, GeometryIndex index)
			throws GeometryIndexNotFoundException;
}