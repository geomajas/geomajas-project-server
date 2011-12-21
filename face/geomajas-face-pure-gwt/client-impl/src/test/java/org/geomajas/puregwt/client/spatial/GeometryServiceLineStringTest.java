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

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
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
public class GeometryServiceLineStringTest {

	private static final int SRID = 4326;

	private static final double DELTA = 1E-10;

	private GeometryService geometryService;

	private com.vividsolutions.jts.geom.GeometryFactory jtsFactory;

	private Geometry gwt;

	private Geometry empty;

	private com.vividsolutions.jts.geom.LineString jts;

	// -------------------------------------------------------------------------
	// Constructor, initializes the 2 LineString geometries:
	// -------------------------------------------------------------------------

	@Before
	public void setUp() {
		Injector myInjector = Guice.createInjector(new GeomajasTestModule());
		geometryService = myInjector.getInstance(GeometryService.class);

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
		Assert.assertEquals(jts.getCentroid().getCoordinate().x, geometryService.getCentroid(gwt).getX(), DELTA);
		Assert.assertEquals(jts.getCentroid().getCoordinate().y, geometryService.getCentroid(gwt).getY(), DELTA);
		Assert.assertNull(geometryService.getCentroid(empty));
	}

	@Test
	public void getBounds() {
		Envelope env = jts.getEnvelopeInternal();
		Bbox bbox = geometryService.getBounds(gwt);
		Assert.assertEquals(env.getMinX(), bbox.getX(), DELTA);
		Assert.assertEquals(env.getMinY(), bbox.getY(), DELTA);
		Assert.assertEquals(env.getMaxX(), bbox.getMaxX(), DELTA);
		Assert.assertEquals(env.getMaxY(), bbox.getMaxY(), DELTA);

		Assert.assertNull(geometryService.getBounds(empty));
	}

	@Test
	public void getNumPoints() {
		Assert.assertEquals(jts.getNumPoints(), geometryService.getNumPoints(gwt));
		Assert.assertEquals(0, geometryService.getNumPoints(empty));
	}

	@Test
	public void isEmpty() {
		Assert.assertEquals(jts.isEmpty(), geometryService.isEmpty(gwt));
	}

	@Test
	public void isSimple() {
		Assert.assertEquals(jts.isSimple(), geometryService.isSimple(gwt));
	}

	@Test
	public void isValid() {
		Geometry noPoints = new Geometry(Geometry.LINE_STRING, SRID, 0);
		noPoints.setCoordinates(new Coordinate[] {});
		Geometry onePoint = new Geometry(Geometry.LINE_STRING, SRID, 0);
		onePoint.setCoordinates(new Coordinate[] { new Coordinate(10.0, 10.0) });
		Assert.assertEquals(jts.isValid(), geometryService.isValid(gwt));
		Assert.assertTrue(geometryService.isValid(noPoints));
		Assert.assertFalse(geometryService.isValid(onePoint));
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

		Assert.assertEquals(jts.intersects(jtsLine1), geometryService.intersects(gwt, gwtLine1)); // No intersection
		Assert.assertEquals(jts.intersects(jtsLine2), geometryService.intersects(gwt, gwtLine2)); // crosses LineSegment
		Assert.assertEquals(jts.intersects(jtsLine3), geometryService.intersects(gwt, gwtLine3)); // touches point

		// Corner cases: empty geometries
		Assert.assertFalse(geometryService.intersects(empty, gwt));
		Assert.assertFalse(geometryService.intersects(gwt, empty));
	}

	@Test
	public void getArea() {
		Assert.assertTrue((jts.getArea() - geometryService.getArea(gwt)) < DELTA);
	}

	@Test
	public void getLength() {
		Assert.assertTrue((jts.getLength() - geometryService.getLength(gwt)) < DELTA);
	}

	@Test
	public void getDistance() {
		double gwtDistance = geometryService.getDistance(gwt, new Coordinate(3, 42));
		double jtsDistance = jts.distance(jtsFactory.createPoint(new com.vividsolutions.jts.geom.Coordinate(3, 42)));
		Assert.assertEquals(jtsDistance, gwtDistance, DELTA);

		Assert.assertEquals(Double.MAX_VALUE, geometryService.getDistance(gwt, null), DELTA);
	}

	@Test
	public void toWkt() throws ParseException {
		WKTReader reader = new WKTReader();
		com.vividsolutions.jts.geom.Geometry result = reader.read(geometryService.toWkt(gwt));
		Assert.assertEquals(gwt.getCoordinates()[0].getX(), result.getCoordinates()[0].x, DELTA);
		Assert.assertEquals(gwt.getCoordinates()[0].getY(), result.getCoordinates()[0].y, DELTA);

		com.vividsolutions.jts.geom.Geometry emptyPoint = reader.read(geometryService.toWkt(empty));
		Assert.assertTrue(emptyPoint.isEmpty());
	}
}