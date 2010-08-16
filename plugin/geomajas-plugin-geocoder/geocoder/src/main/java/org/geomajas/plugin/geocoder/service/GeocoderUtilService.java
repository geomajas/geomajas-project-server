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

package org.geomajas.plugin.geocoder.service;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.service.GeoService;
import org.geotools.geometry.jts.JTS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Utility functions which are needed for the geocoder plugin. These may be candidates to move to the backend module.
 *
 * @author Joachim Van der Auwera
 */
@Component
public class GeocoderUtilService {

	private static final double EXTEND_MAPUNIT_TEST_LENGTH = .1; // start with 1/10 crs unit
	private static final double DISTANCE_PRECISION = .001; // mm precision
	private static final int EXTEND_MAX_ITERATIONS = 5; // max 5 iterations

	@Autowired
	private GeoService geoService;

	/**
	 * Transform a {@link Envelope} from the source to the target CRS.
	 *
	 * @param source source geometry
	 * @param sourceCrs source CRS
	 * @param targetCrs target CRS
	 * @return transformed source, now in target CRS
	 * @throws GeomajasException building the transformation or doing the transformation is not possible
	 */
	public Envelope transform(Envelope source, CoordinateReferenceSystem sourceCrs, CoordinateReferenceSystem targetCrs)
			throws GeomajasException {
		if (sourceCrs == targetCrs) {
			// only works when the caching of the CRSs works
			return source;
		}

		MathTransform mathTransform = geoService.findMathTransform(sourceCrs, targetCrs);
		try {
			return JTS.transform(source, mathTransform);
		} catch (TransformException te) {
			throw new GeomajasException(te, ExceptionCode.CRS_TRANSFORMATION_NOT_POSSIBLE, sourceCrs, targetCrs);
		}
	}

	/**
	 * Build an area around a point with given width and height in meters.
	 * <p/>
	 * The calculation tries to get the size right up to mm precision, but it may be less precise as the number of
	 * attempts to reach the precision are limited.
	 *
	 * @param coordinate center for result envelope.
	 * @param crs crs for coordinate
	 * @param width width in meters
	 * @param height height in meters
	 * @return envelope width requested size (up to mm precision) centered on point
	 * @throws GeomajasException transformation problems
	 */
	public Envelope extendPoint(Coordinate coordinate, CoordinateReferenceSystem crs,
								double width, double height) throws GeomajasException {
		double halfCrsWidth = EXTEND_MAPUNIT_TEST_LENGTH;
		double halfCrsHeight = EXTEND_MAPUNIT_TEST_LENGTH;
		double x = coordinate.x;
		double y = coordinate.y;
		for (int i = EXTEND_MAX_ITERATIONS; i > 0; i--) {
			try {
				Coordinate test;
				test = new Coordinate(x + halfCrsWidth, y);
				double deltaX = JTS.orthodromicDistance(coordinate, test, crs);
				test = new Coordinate(x, y + halfCrsHeight);
				double deltaY = JTS.orthodromicDistance(coordinate, test, crs);
				if (Math.abs(deltaX - width / 2) < DISTANCE_PRECISION &&
						Math.abs(deltaY - height / 2) < DISTANCE_PRECISION) {
					break;
				}
				halfCrsWidth = halfCrsWidth / deltaX * width / 2;
				halfCrsHeight = halfCrsHeight / deltaY * height / 2;
			} catch (TransformException te) {
				throw new GeomajasException(te, ExceptionCode.GEOMETRY_TRANSFORMATION_FAILED, crs);
			}
		}
		return new Envelope(x - halfCrsWidth, x + halfCrsWidth, y - halfCrsHeight, y + halfCrsHeight);
	}

}
