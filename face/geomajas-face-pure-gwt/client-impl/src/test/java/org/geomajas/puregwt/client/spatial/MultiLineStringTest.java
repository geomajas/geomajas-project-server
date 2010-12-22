package org.geomajas.puregwt.client.spatial;

import org.geomajas.geometry.Coordinate;
import org.junit.Test;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.util.Assert;

/**
 * <p>
 * The purpose of this class is to test the methods of the GWT {@link MultiLineString} class. We do this by comparing
 * them to JTS results.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class MultiLineStringTest {

	private final static int SRID = 4326;

	private final static int PRECISION = -1;

	private GeometryFactory gwtFactory;

	private com.vividsolutions.jts.geom.GeometryFactory jtsFactory;

	private MultiLineString gwt;

	private com.vividsolutions.jts.geom.MultiLineString jts;

	// -------------------------------------------------------------------------
	// Constructor, initializes the 2 MultiLineString geometries:
	// -------------------------------------------------------------------------

	public MultiLineStringTest() {
		// gwtFactory = new GeometryFactory(SRID, PRECISION);
		LineString gwtLine1 = gwtFactory.createLineString(new Coordinate[] { new Coordinate(10.0, 10.0),
				new Coordinate(20.0, 10.0), new Coordinate(20.0, 20.0) });
		LineString gwtLine2 = gwtFactory.createLineString(new Coordinate[] { new Coordinate(10.0, 20.0),
				new Coordinate(30.0, 10.0), new Coordinate(40.0, 10.0) });
		gwt = gwtFactory.createMultiLineString(new LineString[] { gwtLine1, gwtLine2 });

		jtsFactory = new com.vividsolutions.jts.geom.GeometryFactory(new PrecisionModel(), SRID);
		com.vividsolutions.jts.geom.LineString jtsLine1 = jtsFactory
				.createLineString(new com.vividsolutions.jts.geom.Coordinate[] {
						new com.vividsolutions.jts.geom.Coordinate(10.0, 10.0),
						new com.vividsolutions.jts.geom.Coordinate(20.0, 10.0),
						new com.vividsolutions.jts.geom.Coordinate(20.0, 20.0) });
		com.vividsolutions.jts.geom.LineString jtsLine2 = jtsFactory
				.createLineString(new com.vividsolutions.jts.geom.Coordinate[] {
						new com.vividsolutions.jts.geom.Coordinate(10.0, 20.0),
						new com.vividsolutions.jts.geom.Coordinate(30.0, 10.0),
						new com.vividsolutions.jts.geom.Coordinate(40.0, 10.0) });
		jts = jtsFactory.createMultiLineString(new com.vividsolutions.jts.geom.LineString[] { jtsLine1, jtsLine2 });
	}

	// -------------------------------------------------------------------------
	// The actual test cases:
	// -------------------------------------------------------------------------

	@Test
	public void getCentroid() {
		// Assert.equals(jts.getCentroid().getCoordinate().x, gwt.getCentroid().x);
		// Assert.equals(jts.getCentroid().getCoordinate().y, gwt.getCentroid().y);
	}

	@Test
	public void getCoordinate() {
		Assert.equals(jts.getCoordinate().x, gwt.getCoordinate().getX());
	}

	@Test
	public void getCoordinates() {
		Assert.equals(jts.getCoordinates()[0].x, gwt.getCoordinates()[0].getX());
	}

	@Test
	public void getBounds() {
		Envelope env = jts.getEnvelopeInternal();
		Bbox bbox = gwt.getBounds();
		Assert.equals(env.getMinX(), bbox.getX());
		Assert.equals(env.getMinY(), bbox.getY());
		Assert.equals(env.getMaxX(), bbox.getMaxX());
		Assert.equals(env.getMaxY(), bbox.getMaxY());
	}

	@Test
	public void getNumPoints() {
		Assert.equals(jts.getNumPoints(), gwt.getNumPoints());
	}

	@Test
	public void getGeometryN() {
		Assert.equals(jts.getGeometryN(0).getCoordinate().x, gwt.getGeometryN(0).getCoordinate().getX());
		Assert.equals(jts.getGeometryN(1).getCoordinate().x, gwt.getGeometryN(1).getCoordinate().getX());
	}

	@Test
	public void getNumGeometries() {
		Assert.equals(jts.getNumGeometries(), gwt.getNumGeometries());
	}

	@Test
	public void isEmpty() {
		Assert.equals(jts.isEmpty(), gwt.isEmpty());
	}

	@Test
	public void isSimple() {
		Assert.equals(jts.isSimple(), gwt.isSimple());
	}

	@Test
	public void isValid() {
		Assert.equals(jts.isValid(), gwt.isValid());
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

		Assert.equals(jts.intersects(jtsLine1), gwt.intersects(gwtLine1)); // No intersection
		Assert.equals(jts.intersects(jtsLine2), gwt.intersects(gwtLine2)); // crosses LineSegment
		Assert.equals(jts.intersects(jtsLine3), gwt.intersects(gwtLine3)); // touches point
	}

	@Test
	public void getArea() {
		Assert.isTrue((jts.getArea() - gwt.getArea()) < SpatialService.ZERO);
	}

	@Test
	public void getLength() {
		Assert.isTrue((jts.getLength() - gwt.getLength()) < SpatialService.ZERO);
	}
}
