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
 * The purpose of this class is to test the methods of the GWT {@link Point} class. We do this by comparing them to JTS
 * results.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class PointTest {

	private static final int SRID = 4326;

	private static final double DELTA = 1E-10;

	private GeometryFactory gwtFactory;

	private com.vividsolutions.jts.geom.GeometryFactory jtsFactory;

	private Point gwt;

	private com.vividsolutions.jts.geom.Point jts;

	private Point empty;

	// -------------------------------------------------------------------------
	// Constructor, initializes the 2 point geometries:
	// -------------------------------------------------------------------------

	@Before
	public void setUp() {
		Injector myInjector = Guice.createInjector(new GeomajasTestModule());
		gwtFactory = myInjector.getInstance(GeometryFactory.class);
		gwt = gwtFactory.createPoint(new Coordinate(10.0, 10.0));
		empty = gwtFactory.createPoint(null);

		jtsFactory = new com.vividsolutions.jts.geom.GeometryFactory(new PrecisionModel(), SRID);
		jts = jtsFactory.createPoint(new com.vividsolutions.jts.geom.Coordinate(10.0, 10.0));
	}

	@Test
	public void getDistance() {
		Assert.assertEquals(5.0, gwt.getDistance(new Coordinate(13, 14)), DELTA);
		Assert.assertEquals(0.0, gwt.getDistance(gwt.getCoordinate()), DELTA);
		Assert.assertEquals(Double.MAX_VALUE, empty.getDistance(new Coordinate(13, 14)), DELTA);
	}

	@Test
	public void getCentroid() {
		Assert.assertEquals(jts.getCentroid().getCoordinate().x, gwt.getCentroid().getX(), DELTA);
		Assert.assertNull(empty.getCentroid());
	}

	@Test
	public void getCoordinate() {
		Assert.assertEquals(jts.getCoordinate().x, gwt.getCoordinate().getX(), DELTA);
		Assert.assertNull(empty.getCoordinate());
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

		// Corner case: empty geometry
		Assert.assertNull(empty.getBounds());
	}

	@Test
	public void getNumPoints() {
		Assert.assertEquals(jts.getNumPoints(), gwt.getNumPoints());
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
		Assert.assertFalse(gwt.isEmpty());
		Assert.assertTrue(empty.isEmpty());
	}

	@Test
	public void isSimple() {
		Assert.assertEquals(jts.isSimple(), gwt.isSimple());
	}

	@Test
	public void isValid() {
		Assert.assertEquals(jts.isValid(), gwt.isValid());
	}

	@Test
	public void intersects() {
		com.vividsolutions.jts.geom.Coordinate jtsC1 = new com.vividsolutions.jts.geom.Coordinate(0, 0);
		com.vividsolutions.jts.geom.Coordinate jtsC2 = new com.vividsolutions.jts.geom.Coordinate(20, 20);
		com.vividsolutions.jts.geom.Coordinate jtsC3 = new com.vividsolutions.jts.geom.Coordinate(20, 0);
		com.vividsolutions.jts.geom.LineString jtsLine1 = jtsFactory
				.createLineString(new com.vividsolutions.jts.geom.Coordinate[] { jtsC1, jtsC2 });
		com.vividsolutions.jts.geom.LineString jtsLine2 = jtsFactory
				.createLineString(new com.vividsolutions.jts.geom.Coordinate[] { jtsC1, jtsC3 });

		Coordinate gwtC1 = new Coordinate(0, 0);
		Coordinate gwtC2 = new Coordinate(20, 20);
		Coordinate gwtC3 = new Coordinate(20, 0);
		LineString gwtLine1 = gwtFactory.createLineString(new Coordinate[] { gwtC1, gwtC2 });
		LineString gwtLine2 = gwtFactory.createLineString(new Coordinate[] { gwtC1, gwtC3 });

		Assert.assertEquals(jts.intersects(jtsLine1), gwt.intersects(gwtLine1));
		Assert.assertEquals(jts.intersects(jtsLine2), gwt.intersects(gwtLine2));

		Assert.assertFalse(empty.intersects(gwtLine1));
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
	public void toWkt() throws ParseException {
		WKTReader reader = new WKTReader();
		com.vividsolutions.jts.geom.Geometry result = reader.read(gwt.toWkt());
		Assert.assertEquals(gwt.getX(), result.getCoordinate().x, DELTA);
		Assert.assertEquals(gwt.getY(), result.getCoordinate().y, DELTA);

		Assert.assertEquals("POINT(EMPTY)", empty.toWkt());
	}
}