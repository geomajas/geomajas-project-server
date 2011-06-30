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

package org.geomajas.puregwt.client.spatial;

import org.geomajas.geometry.Coordinate;
import org.geomajas.puregwt.client.GeomajasTestModule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * <p>
 * The purpose of this class is to test the methods of the GWT {@link LineString} class. We do this by comparing them to
 * JTS results.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class LineStringTest {

	private static final int SRID = 4326;

	private static final double DELTA = 1E-10;

	private GeometryFactory gwtFactory;

	private com.vividsolutions.jts.geom.GeometryFactory jtsFactory;

	private LineString gwt;

	private LineString empty;

	private com.vividsolutions.jts.geom.LineString jts;

	// -------------------------------------------------------------------------
	// Constructor, initializes the 2 LineString geometries:
	// -------------------------------------------------------------------------

	@Before
	public void setUp() {
		Injector myInjector = Guice.createInjector(new GeomajasTestModule());
		gwtFactory = myInjector.getInstance(GeometryFactory.class);
		gwt = gwtFactory.createLineString(new Coordinate[] { new Coordinate(10.0, 10.0), new Coordinate(20.0, 10.0),
				new Coordinate(20.0, 20.0) });
		empty = gwtFactory.createLineString(null);

		jtsFactory = new com.vividsolutions.jts.geom.GeometryFactory(new PrecisionModel(), SRID);
		jts = jtsFactory.createLineString(new com.vividsolutions.jts.geom.Coordinate[] {
				new com.vividsolutions.jts.geom.Coordinate(10.0, 10.0),
				new com.vividsolutions.jts.geom.Coordinate(20.0, 10.0),
				new com.vividsolutions.jts.geom.Coordinate(20.0, 20.0) });
	}

	@Test
	public void getCentroid() {
		Assert.assertEquals(jts.getCentroid().getCoordinate().x, gwt.getCentroid().getX(), DELTA);
		Assert.assertEquals(jts.getCentroid().getCoordinate().y, gwt.getCentroid().getY(), DELTA);
		Assert.assertNull(empty.getCentroid());
	}

	@Test
	public void getCoordinate() {
		Assert.assertEquals(jts.getCoordinate().x, gwt.getCoordinate().getX(), DELTA);
	}

	@Test
	public void getCoordinateN() {
		Assert.assertEquals(jts.getCoordinateN(0).x, gwt.getCoordinateN(0).getX(), DELTA);
		Assert.assertEquals(jts.getCoordinateN(1).x, gwt.getCoordinateN(1).getX(), DELTA);
		Assert.assertEquals(jts.getCoordinateN(2).x, gwt.getCoordinateN(2).getX(), DELTA);
		Assert.assertNull(gwt.getCoordinateN(-1));
		Assert.assertNull(gwt.getCoordinateN(3));
		Assert.assertNull(empty.getCoordinateN(0));
	}

	@Test
	public void getCoordinates() {
		Assert.assertEquals(jts.getCoordinates()[0].x, gwt.getCoordinates()[0].getX(), DELTA);
	}

	@Test
	public void getBounds() {
		Envelope env = jts.getEnvelopeInternal();
		Bbox bbox = gwt.getBounds();
		Assert.assertEquals(env.getMinX(), bbox.getX(), DELTA);
		Assert.assertEquals(env.getMinY(), bbox.getY(), DELTA);
		Assert.assertEquals(env.getMaxX(), bbox.getMaxX(), DELTA);
		Assert.assertEquals(env.getMaxY(), bbox.getMaxY(), DELTA);

		Assert.assertNull(empty.getBounds());
	}

	@Test
	public void getNumPoints() {
		Assert.assertEquals(jts.getNumPoints(), gwt.getNumPoints());
		Assert.assertEquals(0, empty.getNumPoints());
	}

	@Test
	public void getGeometryN() {
		Assert.assertEquals(jts.getGeometryN(0).getCoordinate().x, gwt.getGeometryN(0).getCoordinate().getX(), DELTA);
		Assert.assertEquals(jts.getGeometryN(-1).getCoordinate().x, gwt.getGeometryN(-1).getCoordinate().getX(), DELTA);
		Assert.assertEquals(jts.getGeometryN(1).getCoordinate().x, gwt.getGeometryN(1).getCoordinate().getX(), DELTA);
	}

	@Test
	public void getNumGeometries() {
		Assert.assertEquals(jts.getNumGeometries(), gwt.getNumGeometries());
	}

	@Test
	public void isEmpty() {
		Assert.assertEquals(jts.isEmpty(), gwt.isEmpty());
	}

	@Test
	public void isSimple() {
		Assert.assertEquals(jts.isSimple(), gwt.isSimple());
	}

	@Test
	public void isValid() {
		LineString noPoints = gwtFactory.createLineString(new Coordinate[] {});
		LineString onePoint = gwtFactory.createLineString(new Coordinate[] {new Coordinate(10.0, 10.0)});
		Assert.assertEquals(jts.isValid(), gwt.isValid());
		Assert.assertTrue(noPoints.isValid());
		Assert.assertFalse(onePoint.isValid());
	}


	@Test
	public void isClosed() {
		Assert.assertFalse(empty.isClosed());
		Assert.assertFalse(gwt.isClosed());
		LineString closedLine = gwtFactory.createLineString(new Coordinate[] { new Coordinate(1, 1),
				new Coordinate(2, 2), new Coordinate(1, 1) });
		Assert.assertTrue(closedLine.isClosed());
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

		LineString gwtLine1 = gwtFactory.createLineString(new Coordinate[] { new Coordinate(0, 0),
				new Coordinate(15, 0) });
		LineString gwtLine2 = gwtFactory.createLineString(new Coordinate[] { new Coordinate(15, 5),
				new Coordinate(15, 25) });
		LineString gwtLine3 = gwtFactory.createLineString(new Coordinate[] { new Coordinate(0, 0),
				new Coordinate(15, 15) });

		Assert.assertEquals(jts.intersects(jtsLine1), gwt.intersects(gwtLine1)); // No intersection
		Assert.assertEquals(jts.intersects(jtsLine2), gwt.intersects(gwtLine2)); // crosses LineSegment
		Assert.assertEquals(jts.intersects(jtsLine3), gwt.intersects(gwtLine3)); // touches point

		// Corner cases: empty geometries
		Assert.assertFalse(empty.intersects(gwt));
		Assert.assertFalse(gwt.intersects(empty));
	}

	@Test
	public void getArea() {
		Assert.assertTrue((jts.getArea() - gwt.getArea()) < DELTA);
	}

	@Test
	public void getLength() {
		Assert.assertTrue((jts.getLength() - gwt.getLength()) < DELTA);
	}

	@Test
	public void getDistance() {
		double gwtDistance = gwt.getDistance(new Coordinate(3, 42));
		double jtsDistance = jts.distance(jtsFactory.createPoint(new com.vividsolutions.jts.geom.Coordinate(3, 42)));
		Assert.assertEquals(jtsDistance, gwtDistance, DELTA);

		// Assert.assertEquals(Double.MAX_VALUE, gwt.getDistance(null), DELTA);
	}

	@Test
	public void toWkt() throws ParseException {
		WKTReader reader = new WKTReader();
		com.vividsolutions.jts.geom.Geometry result = reader.read(gwt.toWkt());
		Assert.assertEquals(gwt.getCoordinate().getX(), result.getCoordinate().x, DELTA);
		Assert.assertEquals(gwt.getCoordinate().getY(), result.getCoordinate().y, DELTA);

		Assert.assertEquals("LINESTRING(EMPTY)", empty.toWkt());
	}
}