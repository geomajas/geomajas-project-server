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
package org.geomajas.plugin.jsapi.client.spatial;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportConstructor;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Javascript exportable implementation of a Geometry.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Export("Geometry")
@ExportPackage("org.geomajas.jsapi.spatial")
@Api(allMethods = true)
public class Geometry implements ExportOverlay<org.geomajas.geometry.Geometry>, Exportable {

	/**
	 * Create geometry.
	 * 
	 * @param geometryType
	 *            geometry type
	 * @param srid
	 *            srid
	 * @param precision
	 *            precision
	 */
	@ExportConstructor
	public static org.geomajas.geometry.Geometry constructor(String geometryType, int srid, int precision) {
		return new org.geomajas.geometry.Geometry(geometryType, srid, precision);
	}

	// -------------------------------------------------------------------------
	// General getters and setters
	// -------------------------------------------------------------------------

	/**
	 * Return the spatial reference ID.
	 * 
	 * @return Returns the srid as an integer.
	 */
	public int getSrid() {
		return 0;
	}

	/**
	 * Get the precision for the geometry.
	 * 
	 * @return geometry precision
	 */
	public int getPrecision() {
		return 0;
	}

	/**
	 * Get the geometry type.
	 * 
	 * @return geometry type
	 */
	public String getGeometryType() {
		return "";
	}

	/**
	 * Get the geometry type.
	 * 
	 * @param geometryType
	 *            geometry type
	 */
	public void setGeometryType(String geometryType) {
	}

	/**
	 * Set the SRID (the "x" in a 'EPSG:x" CRS code) for the geometry.
	 * 
	 * @param srid
	 *            spatial reference id
	 */
	public void setSrid(int srid) {
	}

	/**
	 * Set the precision for the geometry.
	 * 
	 * @param precision
	 *            precision
	 */
	public void setPrecision(int precision) {
	}

	/**
	 * Get the coordinates for this geometry.
	 * 
	 * @return coordinates for geometry
	 */
	public Coordinate[] getCoordinates() {
		return new Coordinate[] {};
	}

	/**
	 * Set the coordinates for this geometry.
	 * 
	 * @param coordinates
	 *            coordinates for geometry
	 */
	public void setCoordinates(Coordinate[] coordinates) {
	}

	/**
	 * Get the contained geometries for this geometry.
	 * 
	 * @return contained geometries
	 */
	public org.geomajas.geometry.Geometry[] getGeometries() {
		return new org.geomajas.geometry.Geometry[] {};
	}

	/**
	 * Set the contained geometries.
	 * 
	 * @param geometries
	 *            contained geometries
	 */
	public void setGeometries(org.geomajas.geometry.Geometry[] geometries) {
	}

}