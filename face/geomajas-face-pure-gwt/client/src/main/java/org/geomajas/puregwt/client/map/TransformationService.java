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

package org.geomajas.puregwt.client.map;

import org.geomajas.geometry.Coordinate;
import org.geomajas.global.Api;
import org.geomajas.puregwt.client.spatial.Bbox;
import org.geomajas.puregwt.client.spatial.Geometry;

/**
 * This interface presents a way to transform coordinates, geometries and bounding boxes from world space to view space,
 * and the other way around. World space means that the objects are expressed in the coordinate system of the map they
 * are in, while view space is expressed in the pixel coordinates.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api
public interface TransformationService {

	/**
	 * Transform a single coordinate from world space to view space.
	 * 
	 * @param coordinate
	 *            The coordinate in world space.
	 * @return Returns a new coordinate that is the view space equivalent of the given coordinate.
	 */
	Coordinate worldToView(Coordinate coordinate);

	/**
	 * Transform an entire geometry from world space to view space.
	 * 
	 * @param geometry
	 *            The geometry to transform.
	 * @return Returns a new geometry that is the view space equivalent of the given geometry.
	 */
	Geometry worldToView(Geometry geometry);

	/**
	 * Transform a bounding box from world to view space.
	 * 
	 * @param bbox
	 *            The bounding box in world coordinates.
	 * @returns The view space equivalent of the given bounding box.
	 */
	Bbox worldToView(Bbox bbox);

	/**
	 * Transform a coordinate from view space to world space.
	 * 
	 * @param coordinate
	 *            The views pace coordinate.
	 * @return Returns the world space equivalent of the given coordinate.
	 */
	Coordinate viewToWorld(Coordinate coordinate);

	/**
	 * Transform an entire geometry from view space to world space.
	 * 
	 * @param geometry
	 *            The geometry to transform.
	 * @return Returns a new geometry that is the world space equivalent of the given geometry.
	 */
	Geometry viewToWorld(Geometry geometry);

	/**
	 * Transform a bounding box from view space to world space.
	 * 
	 * @param bbox
	 *            The bounding box in view coordinates.
	 * @returns The world space equivalent of the given bounding box.
	 */
	Bbox viewToWorld(Bbox bbox);
}