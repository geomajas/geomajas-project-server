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
package org.geomajas.internal.service;

import com.vividsolutions.jts.algorithm.InteriorPointArea;
import com.vividsolutions.jts.algorithm.InteriorPointLine;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.service.GeoService;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.springframework.stereotype.Component;

/**
 * Collection of utility functions concerning geometries.
 *
 * @author Joachim Van der Auwera
 * @author Jan De Moerloose
 * @author Pieter De Graef
 */
@Component()
public final class GeoServiceImpl implements GeoService {

	//private CoordinateReferenceSystem mercatorCrs;

	public CoordinateReferenceSystem getCrs(String crs) throws LayerException {
		try {
			/* example how support for extra CRSs could be added without forcing gt-epsg-wkt module
			if ("EPSG:900913".equals(crs)) {
				if (null == mercatorCrs) {
					mercatorCrs = CRS.parseWKT("PROJCS[\"Google Mercator\", "
							+ "GEOGCS[\"WGS 84\", "
							+ "DATUM[\"World Geodetic System 1984\", "
							+ "SPHEROID[\"WGS 84\", 6378137.0, 298.257223563, AUTHORITY[\"EPSG\",\"7030\"]], "
							+ "AUTHORITY[\"EPSG\",\"6326\"]], "
							+ "PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]], "
							+ "UNIT[\"degree\", 0.017453292519943295], "
							+ "AXIS[\"Geodetic latitude\", NORTH], "
							+ "AXIS[\"Geodetic longitude\", EAST], "
							+ "AUTHORITY[\"EPSG\",\"4326\"]],  "
							+ "PROJECTION[\"Mercator (1SP)\", AUTHORITY[\"EPSG\",\"9804\"]], "
							+ "PARAMETER[\"semi_major\", 6378137.0], "
							+ "PARAMETER[\"semi_minor\", 6378137.0], "
							+ "PARAMETER[\"latitude_of_origin\", 0.0], "
							+ "PARAMETER[\"central_meridian\", 0.0], "
							+ "PARAMETER[\"scale_factor\", 1.0],  "
							+ "PARAMETER[\"false_easting\", 0.0],  "
							+ "PARAMETER[\"false_northing\", 0.0],  "
							+ "UNIT[\"m\", 1.0],  "
							+ "AXIS[\"Easting\", EAST],  "
							+ "AXIS[\"Northing\", NORTH], "
							+ "AUTHORITY[\"EPSG\",\"900913\"]]");
				}
				return mercatorCrs;
			}
			*/
			return CRS.decode(crs);
		} catch (NoSuchAuthorityCodeException e) {
			throw new LayerException(e, ExceptionCode.CRS_DECODE_FAILURE_FOR_MAP, crs);
		} catch (FactoryException e) {
			throw new LayerException(e, ExceptionCode.LAYER_CRS_PROBLEMATIC, crs);
		}
	}

	/**
	 * Isn't there a method for this in GeoTools?
	 *
	 * @param crs
	 *            CRS string in the form of 'EPSG:<srid>'.
	 * @return SRID as integer.
	 */
	public int getSridFromCrs(String crs) {
		int crsInt;
		if (crs.indexOf(':') != -1) {
			crsInt = Integer.parseInt(crs.substring(crs.indexOf(':') + 1));
		} else {
			try {
				crsInt = Integer.parseInt(crs);
			} catch (Exception e) {
				crsInt = 0;
			}
		}
		return crsInt;
	}

	/**
	 * Unreliable but works if srids are same as EPSG numbers.
	 *
	 * @param crs reference system of EPSG type.
	 * @return SRID as integer.
	 */
	public int getSridFromCrs(CoordinateReferenceSystem crs) {
		return getSridFromCrs(crs.getIdentifiers().iterator().next().toString());
	}

	/**
	 * @inheritDoc
	 */
	public MathTransform findMathTransform(CoordinateReferenceSystem sourceCrs,
			CoordinateReferenceSystem targetCrs) throws GeomajasException {
		try {
			MathTransform transform;
			try {
				transform = CRS.findMathTransform(sourceCrs, targetCrs);
			} catch (Exception e) {
				transform = CRS.findMathTransform(sourceCrs, targetCrs, true);
			}
			return transform;
		} catch (FactoryException fe) {
			throw new GeomajasException(fe, ExceptionCode.CRS_TRANSFORMATION_NOT_POSSIBLE, sourceCrs, targetCrs);
		}
	}

	/**
	 * @inheritDoc
	 */
	public Geometry transform(Geometry source, CoordinateReferenceSystem sourceCrs, CoordinateReferenceSystem targetCrs)
			throws GeomajasException {
		if (sourceCrs == targetCrs) {
			// only works when the caching of the CRSs works
			return source;
		}
		
		MathTransform mathTransform = findMathTransform(sourceCrs, targetCrs);
		try {
			return JTS.transform(source, mathTransform);
		} catch (TransformException te) {
			throw new GeomajasException(te, ExceptionCode.CRS_TRANSFORMATION_NOT_POSSIBLE, sourceCrs, targetCrs);
		}
	}

	/**
	 * @inheritDoc
	 */
	public Coordinate calcDefaultLabelPosition(InternalFeature feature) {
		Geometry geometry = feature.getGeometry();
		Coordinate labelPoint;
		if (geometry == null || geometry.isEmpty()) {
			labelPoint = null;
		} else if (geometry.isValid()) {
			if (geometry instanceof Polygon || geometry instanceof MultiPolygon) {
				com.vividsolutions.jts.geom.Coordinate c;
				try {
					InteriorPointArea ipa = new InteriorPointArea(geometry);
					c = ipa.getInteriorPoint();
				} catch (Throwable t) {
					// BUG in JTS for some valid geometries ? fall back to
					// centroid
					c = geometry.getCentroid().getCoordinate();
				}
				return new Coordinate(c);
			} else if (geometry instanceof LineString || geometry instanceof MultiLineString) {
				InteriorPointLine ipa = new InteriorPointLine(geometry);
				com.vividsolutions.jts.geom.Coordinate c = ipa.getInteriorPoint();
				labelPoint = new Coordinate(c);
			} else {
				labelPoint = geometry.getCentroid().getCoordinate();
			}
		} else {
			labelPoint = geometry.getCentroid().getCoordinate();
		}
		if (null != labelPoint && (Double.isNaN(labelPoint.x) || Double.isNaN(labelPoint.y))) {
			labelPoint = new Coordinate(geometry.getCoordinate());
		}
		return labelPoint;
	}

	/**
	 * @inheritDoc
	 */
	public Geometry createCircle(final Point point, final double radius, final int nrPoints) {
		double x = point.getX();
		double y = point.getY();
		Coordinate[] coords = new Coordinate[nrPoints + 1];
		for (int i = 0; i < nrPoints; i++) {
			double angle = ((double) i / (double) nrPoints) * Math.PI * 2.0;
			double dx = Math.cos(angle) * radius;
			double dy = Math.sin(angle) * radius;
			coords[i] = new Coordinate(x + dx, y + dy);
		}
		coords[nrPoints] = coords[0];

		LinearRing ring = point.getFactory().createLinearRing(coords);
		return point.getFactory().createPolygon(ring, null);
	}
}