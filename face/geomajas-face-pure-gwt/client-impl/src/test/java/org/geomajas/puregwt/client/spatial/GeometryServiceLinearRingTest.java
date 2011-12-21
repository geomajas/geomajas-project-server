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

/**
 * <p>
 * The purpose of this class is to test the methods of the GWT {@link LinearRing} class. We do this by comparing them to
 * JTS results.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class GeometryServiceLinearRingTest {

	private static final int SRID = 4326;

	private static final double DELTA = 1E-10;

	private GeometryService geometryService;

	private com.vividsolutions.jts.geom.GeometryFactory jtsFactory;

	private Geometry gwt;

	private com.vividsolutions.jts.geom.LinearRing jts;

	// -------------------------------------------------------------------------
	// Constructor, initializes the 2 LineString geometries:
	// -------------------------------------------------------------------------

	@Before
	public void setUp() {
		Injector myInjector = Guice.createInjector(new GeomajasTestModule());
		geometryService = myInjector.getInstance(GeometryService.class);

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
		Assert.assertTrue(jts.getCentroid().getCoordinate().x - geometryService.getCentroid(gwt).getX() < 1);
		Assert.assertTrue(jts.getCentroid().getCoordinate().y - geometryService.getCentroid(gwt).getY() < 1);
	}

	@Test
	public void getBounds() {
		Envelope env = jts.getEnvelopeInternal();
		Bbox bbox = geometryService.getBounds(gwt);
		Assert.assertEquals(env.getMinX(), bbox.getX(), DELTA);
		Assert.assertEquals(env.getMinY(), bbox.getY(), DELTA);
		Assert.assertEquals(env.getMaxX(), bbox.getMaxX(), DELTA);
		Assert.assertEquals(env.getMaxY(), bbox.getMaxY(), DELTA);
	}

	@Test
	public void getNumPoints() {
		Assert.assertEquals(jts.getNumPoints(), geometryService.getNumPoints(gwt));
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
		Assert.assertEquals(jts.isValid(), geometryService.isValid(gwt));
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
		Assert.assertFalse(geometryService.isValid(one));
		Assert.assertFalse(geometryService.isValid(two));
		Assert.assertFalse(geometryService.isValid(three));
		Assert.assertTrue(geometryService.isValid(four));
		Assert.assertFalse(geometryService.isValid(intersects));
	}

	@Test
	public void getArea() {
		Assert.assertEquals(50.0, geometryService.getArea(gwt), DELTA);
	}

	@Test
	public void getLength() {
		Assert.assertEquals(jts.getLength(), geometryService.getLength(gwt), DELTA);
	}
}