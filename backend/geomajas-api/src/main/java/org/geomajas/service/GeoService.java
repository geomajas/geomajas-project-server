/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.service;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import org.geomajas.global.Api;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.InternalFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

/**
 * Collection of utility functions concerning geometries.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public interface GeoService {

	/**
	 * Get the {@link org.opengis.referencing.crs.CoordinateReferenceSystem} with given code.
	 *
	 * @param crs Coordinate reference system code. (EPSG:xxxx)
	 * @return {@link org.opengis.referencing.crs.CoordinateReferenceSystem}
	 * @throws org.geomajas.layer.LayerException CRS code not found
	 * @since 1.6.1
	 */
	CoordinateReferenceSystem getCrs(String crs) throws LayerException;

	/**
	 * Isn't there a method for this in geotools?
	 *
	 * @param crs
	 *            CRS string in the form of 'EPSG:<srid>'.
	 * @return SRID as integer.
	 */
	int getSridFromCrs(String crs);

	/**
	 * Unreliable but works if srids are same as EPSG numbers.
	 *
	 * @param crs reference system of EPSG type.
	 * @return SRID as integer.
	 */
	int getSridFromCrs(CoordinateReferenceSystem crs);

	/**
	 * Get the transformation which converts between two coordinate systems.
	 *
	 * @param sourceCrs crs used by source data
	 * @param targetCrs crs to be used by target
	 * @return transformation matrix
	 * @throws GeomajasException building the transformation is not possible
	 */
	MathTransform findMathTransform(CoordinateReferenceSystem sourceCrs,
			CoordinateReferenceSystem targetCrs) throws GeomajasException;

	/**
	 * Transform a {@link Geometry} from the source to the target CRS.
	 *
	 * @param source source geometry
	 * @param sourceCrs source CRS
	 * @param targetCrs target CRS
	 * @return transformed source, now in target CRS
	 * @throws GeomajasException building the transformation or doing the transformation is not possible
	 * @since 1.7.0 
	 */
	Geometry transform(Geometry source, CoordinateReferenceSystem sourceCrs, CoordinateReferenceSystem targetCrs)
			throws GeomajasException;;

	/**
	 * Determine a default position for positioning the label for a feature.
	 *
	 * @param feature feature which needs the label
	 * @return coordinate where the label would make sense.
	 */
	Coordinate calcDefaultLabelPosition(InternalFeature feature);

	/**
	 * Create a geometry which approximates like a circle.
	 *
	 * @param center center for the circle
	 * @param radius radius for the circle
	 * @param nrPoints number of points to calculate for approximating the circle
	 * @return created circle
	 */
	Geometry createCircle(Point center, double radius, int nrPoints);
}
