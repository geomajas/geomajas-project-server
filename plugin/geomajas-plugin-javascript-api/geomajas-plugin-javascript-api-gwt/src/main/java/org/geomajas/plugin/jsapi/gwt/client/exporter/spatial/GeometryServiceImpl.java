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

package org.geomajas.plugin.jsapi.gwt.client.exporter.spatial;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.WktException;
import org.geomajas.geometry.service.WktService;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.plugin.jsapi.client.spatial.GeometryService;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Service that defines all possible methods on geometries.
 * 
 * @author Pieter De Graef
 */
@Export("GeometryService")
@ExportPackage("org.geomajas.jsapi.spatial")
public class GeometryServiceImpl implements GeometryService, Exportable {

	public GeometryServiceImpl() {
	}

	/**
	 * Return the bounding box that defines the outer most border of a geometry.
	 * 
	 * @param geometry
	 *            The geometry for which to calculate the bounding box.
	 * @return The outer bounds for the given geometry.
	 */
	public Bbox getBounds(Geometry geometry) {
		org.geomajas.gwt.client.spatial.geometry.Geometry geom = GeometryConverter.toGwt(geometry);
		return GeometryConverter.toDto(geom.getBounds());
	}

	/**
	 * Format the given geometry object to Well Known Text representation.
	 * 
	 * @param geometry
	 *            The geometry to format.
	 * @return Get WKT representation of the given geometry, or null in case something went wrong.
	 */
	public String toWkt(Geometry geometry) {
		try {
			return WktService.toWkt(geometry);
		} catch (WktException e) {
			return null;
		}
	}

	/**
	 * Parse the given Well Known Text string into a geometry.
	 * 
	 * @param wkt
	 *            The WKT text.
	 * @return The resulting geometry, or null in case something went wrong.
	 */
	public Geometry toGeometry(String wkt) {
		try {
			return WktService.toGeometry(wkt);
		} catch (WktException e) {
			return null;
		}
	}
	
	@Override
	public boolean isEmpty(Geometry geometry) {
		return org.geomajas.geometry.service.GeometryService.isEmpty(geometry);
	}

	@Override
	public double getArea(Geometry geometry) {
		return org.geomajas.geometry.service.GeometryService.getArea(geometry);
	}

	@Override
	public double getLength(Geometry geometry) {
		return org.geomajas.geometry.service.GeometryService.getLength(geometry);
	}

	@Override
	public double getNumPoints(Geometry geometry) {
		return org.geomajas.geometry.service.GeometryService.getNumPoints(geometry);
	}
	
	
}