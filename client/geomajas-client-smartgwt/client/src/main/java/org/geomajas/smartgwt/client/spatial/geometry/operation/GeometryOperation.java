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

package org.geomajas.smartgwt.client.spatial.geometry.operation;

import org.geomajas.annotation.Api;
import org.geomajas.smartgwt.client.spatial.geometry.Geometry;

/**
 * <p>
 * General interface for operations on Geometry objects. All operations should return a new Geometry instead of changing
 * the original.
 * </p>
 *
 * @author Pieter De Graef
 * @since 1.6.0
 */
// @extract-start GeometryOperation, GeometryOperation interface
@Api(allMethods = true)
public interface GeometryOperation {

	/**
	 * The main edit function. It is passed a geometry object. If other values are needed, pass them through the
	 * constructor, or via setters.
	 *
	 * @param geometry
	 *            The {@link org.geomajas.smartgwt.client.spatial.geometry.Geometry} object to be adjusted.
	 * @return Returns the resulting geometry, leaving the original unharmed.
	 */
	Geometry execute(Geometry geometry);
}
// @extract-end
