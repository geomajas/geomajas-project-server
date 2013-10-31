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

package org.geomajas.plugin.editing.gwt.client.gfx;

import org.geomajas.gwt2.client.controller.MapController;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;

/**
 * Factory definition that creates controllers for geometry indices. These controllers help out in the editing process.
 * 
 * @author Pieter De Graef
 */
public interface GeometryIndexControllerFactory {

	/**
	 * Create a new controller for the given index.
	 * 
	 * @param editService
	 *            The geometry editing service.
	 * @param index
	 *            The index for which to create a controller. Note that different controllers will be applied to
	 *            different types of index (vertex, edge, geometry).
	 * @return Returns the controller.
	 * @throws GeometryIndexNotFoundException
	 *             Thrown in case the index can't be found, or if the geometry process hasn't started.
	 */
	MapController create(GeometryEditService editService, GeometryIndex index) throws GeometryIndexNotFoundException;
}