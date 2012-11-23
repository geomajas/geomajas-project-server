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
import org.geomajas.geometry.Geometry;
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
	
	/**
	 * Return the area of the geometry. If a polygon should contain a hole, the area of such a hole will be subtracted.
	 * 
	 * @param geometry
	 *            The other geometry to calculate the area for.
	 * @return The total area within this geometry.
	 */
	double getArea(org.geomajas.geometry.Geometry geometry);

	/**
	 * Return the length of the geometry. This adds up the length of all edges within the geometry.
	 * 
	 * @param geometry
	 *            The other geometry to calculate the length for.
	 * @return The total length of all edges of the given geometry.
	 */
	double getLength(org.geomajas.geometry.Geometry geometry);

	/**
	 * Return the total number of coordinates within the geometry. This add up all coordinates within the
	 * sub-geometries.
	 * 
	 * @param geometry
	 *            The geometry to calculate the total number of points for.
	 * @return The total number of coordinates within this geometry.
	 */
	double getNumPoints(org.geomajas.geometry.Geometry geometry);	

}