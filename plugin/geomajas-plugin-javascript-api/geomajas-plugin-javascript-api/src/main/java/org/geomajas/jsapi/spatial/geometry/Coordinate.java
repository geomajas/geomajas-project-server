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
package org.geomajas.jsapi.spatial.geometry;

import org.geomajas.annotation.FutureApi;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;
import org.timepedia.exporter.client.NoExport;

/**
 * Javascript exportable facade for a Coordinate.
 * 
 * @author Oliver May
 * @since 1.0.0
 */
@Export
@ExportPackage("org.geomajas.jsapi.spatial.geometry")
@FutureApi(allMethods = true)
public class Coordinate implements Exportable {

	private static final long serialVersionUID = 100;

	private org.geomajas.geometry.Coordinate originalCoordinate;

	/**
	 * TODO.
	 * 
	 * @param originalCoordinate
	 *            the original coordinate
	 * @since 1.0.0
	 */
	@FutureApi
	public Coordinate() {
		this.originalCoordinate = new org.geomajas.geometry.Coordinate();
	}

	/**
	 * TODO.
	 * 
	 * @param coordinate
	 *            the original coordinate
	 * @since 1.0.0
	 */
	@FutureApi
	public Coordinate(org.geomajas.geometry.Coordinate coordinate) {
		this.originalCoordinate = coordinate;
	}

	/**
	 * TODO.
	 * 
	 * @param x
	 * @param y
	 */
	public Coordinate(double x, double y) {
		this.originalCoordinate = new org.geomajas.geometry.Coordinate(x, y);
	}

	/**
	 * TODO.
	 * 
	 * @return
	 * @since 1.0.0
	 */
	@FutureApi
	@NoExport
	public org.geomajas.geometry.Coordinate getCoordinate() {
		return originalCoordinate;
	}

	/**
	 * Helper method to convert JS Coordinates to geomajas smartgwt coordinates.
	 * 
	 * @param coord
	 * @return
	 * @since 1.0.0
	 */
	@FutureApi
	@NoExport
	public static org.geomajas.geometry.Coordinate toGeomajasCoordinate(Coordinate coord) {
		return new org.geomajas.geometry.Coordinate(coord.getX(), coord.getY());
	}

	/**
	 * Return <code>true</code> if <code>other</code> has the same values for the x and y ordinates.
	 * 
	 * @param other
	 *            A <code>Coordinate</code> with which to do the comparison.
	 * @return <code>true</code> if <code>other</code> is a <code>Coordinate</code> with the same values for the x and y
	 *         ordinates.
	 */
	public boolean equalsCoordinate(Coordinate other) {
		return originalCoordinate.equals(toGeomajasCoordinate(other));
	}

	/**
	 * Comparison using a tolerance for the equality check.
	 * 
	 * @param originalCoordinate
	 *            coordinate to compare with
	 * @param delta
	 *            maximum deviation (along one axis, the actual maximum distance is sqrt(2*delta^2))
	 * @return true
	 */
	public boolean equalsDelta(Coordinate c, double delta) {
		return originalCoordinate.equalsDelta(toGeomajasCoordinate(c), delta);
	}

	/**
	 * Computes the 2-dimensional Euclidean distance to another location.
	 * 
	 * @param c
	 *            Another coordinate
	 * @return the 2-dimensional Euclidean distance between the locations
	 */
	public double distance(Coordinate c) {
		return originalCoordinate.distance(toGeomajasCoordinate(c));
	}

	/**
	 * Get x component of the coordinate.
	 * 
	 * @return x
	 */
	public double getX() {
		return originalCoordinate.getX();
	}

	/**
	 * Set the x component for the coordinate.
	 * 
	 * @param x
	 *            x
	 */
	public void setX(double x) {
		originalCoordinate.setX(x);
	}

	/**
	 * Get the y component of the coordinate.
	 * 
	 * @return y
	 */
	public double getY() {
		return originalCoordinate.getY();
	}

	/**
	 * Set the y component of the coordinate.
	 * 
	 * @param y
	 *            y
	 */
	public void setY(double y) {
		originalCoordinate.setY(y);
	}
}
