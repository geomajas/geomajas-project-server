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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * <p>
 * The purpose of this class is to test the {@link GeometryService} for LinearRing types of geometry. We do this by
 * comparing them to JTS results.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class GeometryServiceLinearRingTest {

	private static final int SRID = 4326;

	private static final double DELTA = 1E-4;

	private com.vividsolutions.jts.geom.GeometryFactory jtsFactory;

	private Geometry gwt;

	private com.vividsolutions.jts.geom.LinearRing jts;

	// -------------------------------------------------------------------------
	// Constructor, initializes the 2 LineString geometries:
	// -------------------------------------------------------------------------

	@Before
	public void setUp() {
		gwt = new Geometry(Geometry.LINEAR_RING, SRID, 0);
		gwt.setCoordinates(new Coordinate[] { new Coordinate(10.0, 10.0), new Coordinate(20.0, 10.0),
				new Coordinate(20.0, 20.0), new Coordinate(10.0, 10.0) });

		jtsFactory = new com.vividsolutions.jts.geom.GeometryFactory(new PrecisionModel(), SRID);
		jts = jtsFactory.createLinearRing(new com.vividsolutions.jts.geom.Coordinate[] {
				new com.vividsolutions.jts.geom.Coordinate(10.0, 10.0),
				new com.vividsolutions.jts.geom.Coordinate(20.0, 10.0),
				new com.vividsolutions.jts.geom.Coordinate(20.0, 20.0),
				new com.vividsolutions.jts.geom.Coordinate(10.0, 10.0) });
	}

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
		Assert.assertEquals(jts.isValid(), GeometryService.isValid(gwt));
		Geometry one = new Geometry(Geometry.LINEAR_RING, SRID, 0);
		one.setCoordinates(new Coordinate[] { new Coordinate(10.0, 10.0) });
		Geometry two = new Geometry(Geometry.LINEAR_RING, SRID, 0);
		two.setCoordinates(new Coordinate[] { new Coordinate(10.0, 10.0), new Coordinate(10.0, 10.0) });
		Geometry three = new Geometry(Geometry.LINEAR_RING, SRID, 0);
		three.setCoordinates(new Coordinate[] { new Coordinate(10.0, 10.0), new Coordinate(20.0, 10.0),
				new Coordinate(10.0, 10.0) });
		Geometry four = new Geometry(Geometry.LINEAR_RING, SRID, 0);
		four.setCoordinates(new Coordinate[] { new Coordinate(10.0, 10.0), new Coordinate(20.0, 10.0),
				new Coordinate(20.0, 20.0), new Coordinate(10.0, 10.0) });
		Geometry intersects = new Geometry(Geometry.LINEAR_RING, SRID, 0);
		intersects.setCoordinates(new Coordinate[] { new Coordinate(10.0, 10.0), new Coordinate(20.0, 10.0),
				new Coordinate(10.0, 20.0), new Coordinate(20.0, 20.0), new Coordinate(10.0, 10.0) });
		Assert.assertFalse(GeometryService.isValid(one));
		Assert.assertFalse(GeometryService.isValid(two));
		Assert.assertFalse(GeometryService.isValid(three));
		Assert.assertTrue(GeometryService.isValid(four));
		Assert.assertFalse(GeometryService.isValid(intersects));
	}

	@Test
	public void getArea() {
		Assert.assertEquals(50.0, GeometryService.getArea(gwt), DELTA);
	}

	@Test
	public void getLength() {
		Assert.assertEquals(jts.getLength(), GeometryService.getLength(gwt), DELTA);
	}

	@Test
	public void centroidTest() throws WktException, ParseException {
		String wkt = "POLYGON ((948732.820500011 6078756.569848541, 1068024.3649477751 6028202.01937218, "
				+ "1072333.0579502126 5998997.156511122, 1055305.3793291312 5958871.450440632, 1105675.0935204166 "
				+ "5929144.458480931, 1162476.2079476311 5924715.38833866, 1153645.9759600186 5858183.406784581, "
				+ "1104605.1110124618 5830956.472744024, 1066165.5338210524 5840392.513424949, 946203.9886702409 "
				+ "6074778.917027304, 948732.820500011 6078756.569848541))";
		Geometry geometry = WktService.toGeometry(wkt);
		Coordinate c = GeometryService.getCentroid(geometry);

		WKTReader r = new WKTReader(jtsFactory);
		com.vividsolutions.jts.geom.Geometry geometry2 = r.read(wkt);
		com.vividsolutions.jts.geom.Point c2 = geometry2.getCentroid();

		Assert.assertEquals(c2.getY(), c.getY(), DELTA);
		Assert.assertEquals(c2.getX(), c.getX(), DELTA);
	}
}