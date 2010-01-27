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
import org.geomajas.layer.feature.RenderedFeature;
import org.geomajas.service.GeoService;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.springframework.stereotype.Component;

/**
 * Collection of utility functions concerning geometries.
 *
 * @author check subversion
 */
@Component()
public final class GeoServiceImpl implements GeoService {

	/**
	 * Isn't there a method for this in geotools?
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
	public MathTransform findMathTransform(CoordinateReferenceSystem sourceCRS,
			CoordinateReferenceSystem targetCRS) throws FactoryException {
		// AllAuthoritiesFactory f;
		MathTransform transform;
		try {
			transform = CRS.findMathTransform(sourceCRS, targetCRS);
		} catch (Exception e) {
			transform = CRS.findMathTransform(sourceCRS, targetCRS, true);
		}
		return transform;
	}

	/**
	 * @inheritDoc
	 */
	public Coordinate calcDefaultLabelPosition(RenderedFeature feature) {
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