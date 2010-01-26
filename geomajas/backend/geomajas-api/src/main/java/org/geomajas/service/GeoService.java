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
import org.geomajas.layer.feature.RenderedFeature;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

/**
 * Collection of utility functions concerning geometries.
 *
 * @author Joachim Van der Auwera
 */
public interface GeoService {

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

	MathTransform findMathTransform(CoordinateReferenceSystem sourceCRS,
			CoordinateReferenceSystem targetCRS) throws FactoryException;

	Coordinate calcDefaultLabelPosition(RenderedFeature feature);

	Geometry createCircle(Point point, double radius, int nrPoints);
}
