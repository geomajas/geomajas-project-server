/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.geometry.service;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.Matrix;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * <p>
 * The purpose of this class is to test the {@link GeometryService} for LineString types of geometry. We do this by
 * comparing them to JTS results.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class GeometryServiceLineStringTest {

	private static final int SRID = 4326;

	private static final double DELTA = 1E-10;

	private com.vividsolutions.jts.geom.GeometryFactory jtsFactory;

	private Geometry gwt;

	private Geometry empty;

	private com.vividsolutions.jts.geom.LineString jts;

	private Matrix matrix = new Matrix(1, 2, 3, 5, 3, 6);

	private Matrix inverse = new Matrix(-5, 2, 3, -1, 3, -3);

	// -------------------------------------------------------------------------
	// Constructor, initializes the 2 LineString geometries:
	// -------------------------------------------------------------------------

	@Before
	public void setUp() {
		empty = new Geometry(Geometry.LINE_STRING, SRID, 0);
		gwt = new Geometry(Geometry.LINE_STRING, SRID, 0);
		gwt.setCoordinates(new Coordinate[] { new Coordinate(10.0, 10.0), new Coordinate(20.0, 10.0),
				new Coordinate(20.0, 20.0) });

		jtsFactory = new com.vividsolutions.jts.geom.GeometryFactory(new PrecisionModel(), SRID);
		jts = jtsFactory.createLineString(new com.vividsolutions.jts.geom.Coordinate[] {
				new com.vividsolutions.jts.geom.Coordinate(10.0, 10.0),
				new com.vividsolutions.jts.geom.Coordinate(20.0, 10.0),
				new com.vividsolutions.jts.geom.Coordinate(20.0, 20.0) });
	}

	@Test
	public void getCentroid() {
		Assert.assertEquals(jts.getCentroid().getCoordinate().x, GeometryService.getCentroid(gwt).getX(), DELTA);
		Assert.assertEquals(jts.getCentroid().getCoordinate().y, GeometryService.getCentroid(gwt).getY(), DELTA);
		Assert.assertNull(GeometryService.getCentroid(empty));
	}

	@Test
	public void getBounds() {
		Envelope env = jts.getEnvelopeInternal();
		Bbox bbox = GeometryService.getBounds(gwt);
		Assert.assertEquals(env.getMinX(), bbox.getX(), DELTA);
		Assert.assertEquals(env.getMinY(), bbox.getY(), DELTA);
		Assert.assertEquals(env.getMaxX(), bbox.getMaxX(), DELTA);
		Assert.assertEquals(env.getMaxY(), bbox.getMaxY(), DELTA);

		Assert.assertNull(GeometryService.getBounds(empty));
	}

	@Test
	public void getNumPoints() {
		Assert.assertEquals(jts.getNumPoints(), GeometryService.getNumPoints(gwt));
		Assert.assertEquals(0, GeometryService.getNumPoints(empty));
	}

	@Test
	public void isEmpty() {
		Assert.assertEquals(jts.isEmpty(), GeometryService.isEmpty(gwt));
	}

	@Test
	public void isSimple() {
		Assert.assertEquals(jts.isSimple(), GeometryService.isSimple(gwt));
	}

	@Test
	public void isValid() {
		Geometry noPoints = new Geometry(Geometry.LINE_STRING, SRID, 0);
		noPoints.setCoordinates(new Coordinate[] {});
		Geometry onePoint = new Geometry(Geometry.LINE_STRING, SRID, 0);
		onePoint.setCoordinates(new Coordinate[] { new Coordinate(10.0, 10.0) });
		Assert.assertEquals(jts.isValid(), GeometryService.isValid(gwt));
		Assert.assertTrue(GeometryService.isValid(noPoints));
		Assert.assertFalse(GeometryService.isValid(onePoint));
	}

	@Test
	public void intersects() {
		com.vividsolutions.jts.geom.LineString jtsLine1 = jtsFactory
				.createLineString(new com.vividsolutions.jts.geom.Coordinate[] {
						new com.vividsolutions.jts.geom.Coordinate(0, 0),
						new com.vividsolutions.jts.geom.Coordinate(15, 0) });
		com.vividsolutions.jts.geom.LineString jtsLine2 = jtsFactory
				.createLineString(new com.vividsolutions.jts.geom.Coordinate[] {
						new com.vividsolutions.jts.geom.Coordinate(15, 5),
						new com.vividsolutions.jts.geom.Coordinate(15, 25) });
		com.vividsolutions.jts.geom.LineString jtsLine3 = jtsFactory
				.createLineString(new com.vividsolutions.jts.geom.Coordinate[] {
						new com.vividsolutions.jts.geom.Coordinate(0, 0),
						new com.vividsolutions.jts.geom.Coordinate(15, 15) });

		Geometry gwtLine1 = new Geometry(Geometry.LINE_STRING, SRID, 0);
		gwtLine1.setCoordinates(new Coordinate[] { new Coordinate(0, 0), new Coordinate(15, 0) });
		Geometry gwtLine2 = new Geometry(Geometry.LINE_STRING, SRID, 0);
		gwtLine2.setCoordinates(new Coordinate[] { new Coordinate(15, 5), new Coordinate(15, 25) });
		Geometry gwtLine3 = new Geometry(Geometry.LINE_STRING, SRID, 0);
		gwtLine3.setCoordinates(new Coordinate[] { new Coordinate(0, 0), new Coordinate(15, 15) });

		Assert.assertEquals(jts.intersects(jtsLine1), GeometryService.intersects(gwt, gwtLine1)); // No intersection
		Assert.assertEquals(jts.intersects(jtsLine2), GeometryService.intersects(gwt, gwtLine2)); // crosses LineSegment
		Assert.assertEquals(jts.intersects(jtsLine3), GeometryService.intersects(gwt, gwtLine3)); // touches point

		// Corner cases: empty geometries
		Assert.assertFalse(GeometryService.intersects(empty, gwt));
		Assert.assertFalse(GeometryService.intersects(gwt, empty));
	}

	@Test
	public void getArea() {
		Assert.assertEquals(jts.getArea(), GeometryService.getArea(gwt), DELTA);
	}

	@Test
	public void getLength() {
		Assert.assertEquals(jts.getLength(), GeometryService.getLength(gwt), DELTA);
	}

	@Test
	public void getDistance() {
		double gwtDistance = GeometryService.getDistance(gwt, new Coordinate(3, 42));
		double jtsDistance = jts.distance(jtsFactory.createPoint(new com.vividsolutions.jts.geom.Coordinate(3, 42)));
		Assert.assertEquals(jtsDistance, gwtDistance, DELTA);

		Assert.assertEquals(Double.MAX_VALUE, GeometryService.getDistance(gwt, null), DELTA);
	}
	
	@Test
	public void transformTest() throws WktException {
		Geometry transformed = GeometryService.transform(gwt, matrix);
		Geometry back = GeometryService.transform(transformed, inverse);
		Assert.assertEquals(WktService.toWkt(gwt), WktService.toWkt(back));
	}

}