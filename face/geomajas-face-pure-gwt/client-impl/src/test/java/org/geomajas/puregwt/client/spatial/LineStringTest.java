package org.geomajas.puregwt.client.spatial;

import org.geomajas.geometry.Coordinate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * <p>
 * The purpose of this class is to test the methods of the GWT {@link LineString} class. We do this by comparing them to
 * JTS results.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class LineStringTest {

	private final static int SRID = 4326;

	private final static int PRECISION = -1;

	private final static double DELTA = 1E-10;

	private GeometryFactory gwtFactory;

	private com.vividsolutions.jts.geom.GeometryFactory jtsFactory;

	private LineString gwt;

	private com.vividsolutions.jts.geom.LineString jts;

	// -------------------------------------------------------------------------
	// Constructor, initializes the 2 LineString geometries:
	// -------------------------------------------------------------------------

	@Before
	public void setUp() {
		Injector myInjector = Guice.createInjector(new TestConfigurationModule());
		gwtFactory = myInjector.getInstance(GeometryFactory.class);
		gwtFactory.init(SRID, PRECISION);
		gwt = gwtFactory.createLineString(new Coordinate[] { new Coordinate(10.0, 10.0), new Coordinate(20.0, 10.0),
				new Coordinate(20.0, 20.0) });

		jtsFactory = new com.vividsolutions.jts.geom.GeometryFactory(new PrecisionModel(), SRID);
		jts = jtsFactory.createLineString(new com.vividsolutions.jts.geom.Coordinate[] {
				new com.vividsolutions.jts.geom.Coordinate(10.0, 10.0),
				new com.vividsolutions.jts.geom.Coordinate(20.0, 10.0),
				new com.vividsolutions.jts.geom.Coordinate(20.0, 20.0) });
	}

	@Test
	public void getCentroid() {
		Assert.assertEquals(jts.getCentroid().getCoordinate().x, gwt.getCentroid().getX(),DELTA);
		Assert.assertEquals(jts.getCentroid().getCoordinate().y, gwt.getCentroid().getY(),DELTA);
	}

	@Test
	public void getCoordinate() {
		Assert.assertEquals(jts.getCoordinate().x, gwt.getCoordinate().getX(),DELTA);
	}

	@Test
	public void getCoordinates() {
		Assert.assertEquals(jts.getCoordinates()[0].x, gwt.getCoordinates()[0].getX(),DELTA);
	}

	@Test
	public void getBounds() {
		Envelope env = jts.getEnvelopeInternal();
		Bbox bbox = gwt.getBounds();
		Assert.assertEquals(env.getMinX(), bbox.getX(),DELTA);
		Assert.assertEquals(env.getMinY(), bbox.getY(),DELTA);
		Assert.assertEquals(env.getMaxX(), bbox.getMaxX(),DELTA);
		Assert.assertEquals(env.getMaxY(), bbox.getMaxY(),DELTA);
	}

	@Test
	public void getNumPoints() {
		Assert.assertEquals(jts.getNumPoints(), gwt.getNumPoints());
	}

	@Test
	public void getGeometryN() {
		Assert.assertEquals(jts.getGeometryN(0).getCoordinate().x, gwt.getGeometryN(0).getCoordinate().getX(),DELTA);
		Assert.assertEquals(jts.getGeometryN(-1).getCoordinate().x, gwt.getGeometryN(-1).getCoordinate().getX(),DELTA);
		Assert.assertEquals(jts.getGeometryN(1).getCoordinate().x, gwt.getGeometryN(1).getCoordinate().getX(),DELTA);
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
		Assert.assertEquals(jts.isValid(), gwt.isValid());
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
	}

	@Test
	public void getArea() {
		Assert.assertTrue((jts.getArea() - gwt.getArea()) < DELTA);
	}

	@Test
	public void getLength() {
		Assert.assertTrue((jts.getLength() - gwt.getLength()) < DELTA);
	}
}
