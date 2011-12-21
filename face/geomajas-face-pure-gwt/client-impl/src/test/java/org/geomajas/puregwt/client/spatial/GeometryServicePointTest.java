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
 * The purpose of this class is to test the methods of the GeometryService for point geometries.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class GeometryServicePointTest {

	private static final int SRID = 4326;

	private static final double DELTA = 1E-10;

	private GeometryService geometryService;

	private com.vividsolutions.jts.geom.GeometryFactory jtsFactory;

	private com.vividsolutions.jts.geom.Point jts;

	private Geometry gwt;

	private Geometry empty;

	// -------------------------------------------------------------------------
	// Constructor, initializes the 2 point geometries:
	// -------------------------------------------------------------------------

	@Before
	public void setUp() {
		Injector myInjector = Guice.createInjector(new GeomajasTestModule());
		geometryService = myInjector.getInstance(GeometryService.class);

		empty = new Geometry(Geometry.POINT, SRID, 0);
		gwt = new Geometry(Geometry.POINT, SRID, 0);
		gwt.setCoordinates(new Coordinate[] { new Coordinate(10.0, 10.0) });

		jtsFactory = new com.vividsolutions.jts.geom.GeometryFactory(new PrecisionModel(), SRID);
		jts = jtsFactory.createPoint(new com.vividsolutions.jts.geom.Coordinate(10.0, 10.0));
	}

	@Test
	public void getDistance() {
		Assert.assertEquals(5.0, geometryService.getDistance(gwt, new Coordinate(13, 14)), DELTA);
		Assert.assertEquals(0.0, geometryService.getDistance(gwt, gwt.getCoordinates()[0]), DELTA);
		Assert.assertEquals(Double.MAX_VALUE, geometryService.getDistance(empty, new Coordinate(13, 14)), DELTA);
	}

	@Test
	public void getCentroid() {
		Assert.assertEquals(jts.getCentroid().getCoordinate().x, geometryService.getCentroid(gwt).getX(), DELTA);
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

		// Corner case: empty geometry
		Assert.assertNull(geometryService.getBounds(empty));
	}

	@Test
	public void getNumPoints() {
		Assert.assertEquals(jts.getNumPoints(), geometryService.getNumPoints(gwt));
	}

	@Test
	public void isEmpty() {
		Assert.assertEquals(jts.isEmpty(), geometryService.isEmpty(gwt));
		Assert.assertFalse(geometryService.isEmpty(gwt));
		Assert.assertTrue(geometryService.isEmpty(empty));
	}

	@Test
	public void isSimple() {
		Assert.assertEquals(jts.isSimple(), geometryService.isSimple(gwt));
	}

	@Test
	public void isValid() {
		Assert.assertEquals(jts.isValid(), geometryService.isValid(gwt));
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
		Geometry gwtLine1 = new Geometry(Geometry.LINE_STRING, SRID, 0);
		gwtLine1.setCoordinates(new Coordinate[] { gwtC1, gwtC2 });
		Geometry gwtLine2 = new Geometry(Geometry.LINE_STRING, SRID, 0);
		gwtLine2.setCoordinates(new Coordinate[] { gwtC1, gwtC3 });

		Assert.assertEquals(jts.intersects(jtsLine1), geometryService.intersects(gwt, gwtLine1));
		Assert.assertEquals(jts.intersects(jtsLine2), geometryService.intersects(gwt, gwtLine2));

		Assert.assertFalse(geometryService.intersects(empty, gwtLine1));
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
	public void toWkt() throws ParseException {
		WKTReader reader = new WKTReader();
		com.vividsolutions.jts.geom.Geometry point = reader.read(geometryService.toWkt(gwt));
		Assert.assertEquals(gwt.getCoordinates()[0].getX(), point.getCoordinate().x, DELTA);
		Assert.assertEquals(gwt.getCoordinates()[0].getY(), point.getCoordinate().y, DELTA);
		Assert.assertEquals("POINT (10 10)", point.toText());

		com.vividsolutions.jts.geom.Geometry emptyPoint = reader.read(geometryService.toWkt(empty));
		Assert.assertTrue(emptyPoint.isEmpty());
	}
}