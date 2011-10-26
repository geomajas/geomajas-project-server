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

package org.geomajas.jsapi.spatial;

import org.geomajas.annotation.Api;
import org.timepedia.exporter.client.Exportable;

/**
 * Service that defines all possible methods on geometries.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface GeometryService extends Exportable {

	/**
	 * Return the bounding box that defines the outer most border of a geometry.
	 * 
	 * @param geometry
	 *            The geometry for which to calculate the bounding box.
	 * @return The outer bounds for the given geometry.
	 */
	org.geomajas.geometry.Bbox getBounds(org.geomajas.geometry.Geometry geometry);
}