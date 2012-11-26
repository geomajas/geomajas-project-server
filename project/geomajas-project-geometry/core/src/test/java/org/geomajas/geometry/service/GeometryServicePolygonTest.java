/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * <p>
 * The purpose of this class is to test the {@link GeometryService} for Polygon types of geometry. We do this by
 * comparing them to JTS results.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class GeometryServicePolygonTest {

	private static final int SRID = 4326;

	private static final double DELTA = 1E-10;

	private com.vividsolutions.jts.geom.GeometryFactory jtsFactory;

	private Geometry gwt;

	private Geometry noholes;

	private com.vividsolutions.jts.geom.Polygon jts;

	// -------------------------------------------------------------------------
	// Constructor, initializes the 2 Polygon geometries:
	// -------------------------------------------------------------------------

	/**
	 * Creates polygons with a single hole in them.
	 */
	@Before
	public void setUp() {
		Geometry gwtShell = new Geometry(Geometry.LINEAR_RING, SRID, 0);
		gwtShell.setCoordinates(new Coordinate[] { new Coordinate(10.0, 10.0), new Coordinate(20.0, 10.0),
				new Coordinate(20.0, 20.0), new Coordinate(10.0, 10.0) });
		Geometry gwtHole = new Geometry(Geometry.LINEAR_RING, SRID, 0);
		gwtHole.setCoordinates(new Coordinate[] { new Coordinate(12.0, 12.0), new Coordinate(18.0, 12.0),
				new Coordinate(18.0, 18.0), new Coordinate(12.0, 12.0) });
		gwt = new Geometry(Geometry.POLYGON, SRID, 0);
		gwt.setGeometries(new Geometry[] { gwtShell, gwtHole });
		noholes = new Geometry(Geometry.POLYGON, SRID, 0);
		noholes.setGeometries(new Geometry[] { gwtShell });

		jtsFactory = new com.vividsolutions.jts.geom.GeometryFactory(new PrecisionModel(), SRID);
		com.vividsolutions.jts.geom.LinearRing jtsShell = jtsFactory
				.createLinearRing(new com.vividsolutions.jts.geom.Coordinate[] {
						new com.vividsolutions.jts.geom.Coordinate(10.0, 10.0),
						new com.vividsolutions.jts.geom.Coordinate(20.0, 10.0),
						new com.vividsolutions.jts.geom.Coordinate(20.0, 20.0),
						new com.vividsolutions.jts.geom.Coordinate(10.0, 10.0) });
		com.vividsolutions.jts.geom.LinearRing jtsHole = jtsFactory
				.createLinearRing(new com.vividsolutions.jts.geom.Coordinate[] {
						new com.vividsolutions.jts.geom.Coordinate(12.0, 12.0),
						new com.vividsolutions.jts.geom.Coordinate(18.0, 12.0),
						new com.vividsolutions.jts.geom.Coordinate(18.0, 18.0),
						new com.vividsolutions.jts.geom.Coordinate(12.0, 12.0) });
		jts = jtsFactory.createPolygon(jtsShell, new com.vividsolutions.jts.geom.LinearRing[] { jtsHole });
	}

	// -------------------------------------------------------------------------
	// The actual test cases:
	// -------------------------------------------------------------------------

	@Test
	public void getCentroid() {
		Assert.assertTrue(jts.getCentroid().getCoordinate().x - GeometryService.getCentroid(gwt).getX() < 1);
		Assert.assertTrue(jts.getCentroid().getCoordinate().y - GeometryService.getCentroid(gwt).getY() < 1);
	}

	@Test
	public void getBounds() {
		Envelope env = jts.getEnvelopeInternal();
		Bbox bbox = GeometryService.getBounds(gwt);
		Assert.assertEquals(env.getMinX(), bbox.getX(), DELTA);
		Assert.assertEquals(env.getMinY(), bbox.getY(), DELTA);
		Assert.assertEquals(env.getMaxX(), bbox.getMaxX(), DELTA);
		Assert.assertEquals(env.getMaxY(), bbox.getMaxY(), DELTA);
	}

	@Test
	public void getNumPoints() {
		Assert.assertEquals(jts.getNumPoints(), GeometryService.getNumPoints(gwt));
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
		// Assert.assertEquals(jts.isValid(), geometryService.isValid(gwt));
	}

	@Test
	public void intersects() {
		com.vividsolutions.jts.geom.LineString jtsLine1 = jtsFactory
				.createLineString(new com.vividsolutions.jts.geom.Coordinate[] {
						new com.vividsolutions.jts.geom.Coordinate(0, 0),
						new com.vividsolutions.jts.geom.Coordinate(15, 0) });
		// com.vividsolutions.jts.geom.LineString jtsLine2 = jtsFactory
		// .createLineString(new com.vividsolutions.jts.geom.Coordinate[] {
		// new com.vividsolutions.jts.geom.Coordinate(15, 5),
		// new com.vividsolutions.jts.geom.Coordinate(15, 25) });
		// com.vividsolutions.jts.geom.LineString jtsLine3 = jtsFactory
		// .createLineString(new com.vividsolutions.jts.geom.Coordinate[] {
		// new com.vividsolutions.jts.geom.Coordinate(0, 0),
		// new com.vividsolutions.jts.geom.Coordinate(15, 15) });

		Geometry gwtLine1 = new Geometry(Geometry.LINE_STRING, SRID, 0);
		gwtLine1.setCoordinates(new Coordinate[] { new Coordinate(0, 0), new Coordinate(15, 0) });
		Geometry gwtLine2 = new Geometry(Geometry.LINE_STRING, SRID, 0);
		gwtLine2.setCoordinates(new Coordinate[] { new Coordinate(15, 5), new Coordinate(15, 25) });
		Geometry gwtLine3 = new Geometry(Geometry.LINE_STRING, SRID, 0);
		gwtLine3.setCoordinates(new Coordinate[] { new Coordinate(0, 0), new Coordinate(15, 15) });

		// TODO: problem with JTS intersection calculation...
		Assert.assertEquals(jts.intersects(jtsLine1), GeometryService.intersects(gwt, gwtLine1)); // No intersection
		// Assert.assertEquals(jts.intersects(jtsLine2), geometryService.intersects(gwt, gwtLine2)); // crosses
		// LineSegment
		// Assert.assertEquals(jts.intersects(jtsLine3), geometryService.intersects(gwt, gwtLine3)); // touches point
		// Assert.assertEquals(true, geometryService.intersects(gwt, gwtLine2)); // crosses LineSegment
		// Assert.assertEquals(true, geometryService.intersects(gwt, gwtLine3)); // touches point
	}

	@Test
	public void getArea() {
		Assert.assertEquals(jts.getArea(), GeometryService.getArea(gwt), DELTA);
	}

	@Test
	public void getLength() {
		Assert.assertEquals(jts.getLength(), GeometryService.getLength(gwt), DELTA);
	}
}