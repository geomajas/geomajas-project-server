/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.jsapi.client.spatial;

import org.geomajas.annotation.Api;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.Exportable;

/**
 * Service that defines all possible methods on geometries.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
@Export
public interface GeometryService extends Exportable {

	/**
	 * Return the bounding box that defines the outer most border of a geometry.
	 * 
	 * @param geometry
	 *            The geometry for which to calculate the bounding box.
	 * @return The outer bounds for the given geometry.
	 */
	org.geomajas.geometry.Bbox getBounds(org.geomajas.geometry.Geometry geometry);

	/**
	 * Format the given geometry object to Well Known Text representation.
	 * 
	 * @param geometry
	 *            The geometry to format.
	 * @return Get WKT representation of the given geometry, or null in case something went wrong.
	 */
	String toWkt(org.geomajas.geometry.Geometry geometry);

	/**
	 * Parse the given Well Known Text string into a geometry.
	 * 
	 * @param wkt
	 *            The WKT text.
	 * @return The resulting geometry, or null in case something went wrong.
	 */
	org.geomajas.geometry.Geometry toGeometry(String wkt);
	
	/**
	 * This geometry is empty if there are no geometries/coordinates stored inside.
	 * 
	 * @param geometry
	 *            The geometry to check.
	 * @return true or false.
	 */
	boolean isEmpty(org.geomajas.geometry.Geometry geometry);

}