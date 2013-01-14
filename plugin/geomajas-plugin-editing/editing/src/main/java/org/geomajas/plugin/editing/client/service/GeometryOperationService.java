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
package org.geomajas.plugin.editing.client.service;

import java.util.List;

import org.geomajas.command.dto.BufferInfo;
import org.geomajas.command.dto.UnionInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Geometry;

import com.google.gwt.core.client.Callback;

/**
 * Service to perform common geometry operations (buffer, union, convex hull, etc...).
 * 
 * @author Jan De Moerloose
 * 
 */
public interface GeometryOperationService {

	/**
	 * Compute a buffer area around this geometry.
	 * 
	 * @param geometry the geometry
	 * @param bufferInfo info object
	 * @param callback callback
	 */
	void buffer(Geometry geometry, BufferInfo bufferInfo, Callback<Geometry, Throwable> callback);

	/**
	 * Compute a buffer area around these geometries.
	 * 
	 * @param geometries the geometries
	 * @param bufferInfo info object
	 * @param callback callback
	 */
	void buffer(List<Geometry> geometries, BufferInfo bufferInfo, Callback<List<Geometry>, Throwable> callback);

	/**
	 * Compute the union of these geometries.
	 * 
	 * @param geometries the geometries
	 * @param bufferInfo info object
	 * @param callback callback
	 */
	void union(List<Geometry> geometries, UnionInfo unionInfo, Callback<Geometry, Throwable> callback);

	/**
	 * Compute the convex hull of this geometry.
	 * 
	 * @param geometry the geometry
	 * @param bufferInfo info object
	 * @param callback callback
	 */
	void convexHull(Geometry geometry, Callback<Geometry, Throwable> callback);

	/**
	 * Compute the convex hull of these geometries.
	 * 
	 * @param geometries the geometries
	 * @param bufferInfo info object
	 * @param callback callback
	 */
	void convexHull(List<Geometry> geometries, Callback<List<Geometry>, Throwable> callback);

	/**
	 * Compute the bounds of these geometries.
	 * 
	 * @param geometries the geometries
	 * @param bufferInfo info object
	 * @param callback callback
	 */
	void bounds(List<Geometry> geometries, Callback<Bbox, Throwable> callback);

}
