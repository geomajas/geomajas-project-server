package org.geomajas.puregwt.client.spatial;

import org.geomajas.geometry.Coordinate;
import org.junit.Test;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.util.Assert;

/**
 * <p>
 * The purpose of this class is to test the methods of the GWT {@link Polygon} class. We do this by comparing them to
 * JTS results.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class PolygonTest {

	private final static int SRID = 4326;

	private final static int PRECISION = -1;

	private GeometryFactory gwtFactory;

	private com.vividsolutions.jts.geom.GeometryFactory jtsFactory;

	private Polygon gwt;

	private com.vividsolutions.jts.geom.Polygon jts;

	// -------------------------------------------------------------------------
	// Constructor, initializes the 2 Polygon geometries:
	// -------------------------------------------------------------------------

	/**
	 * Creates polygons with a single hole in them.
	 */
	public PolygonTest() {
		// gwtFactory = new GeometryFactory(SRID, PRECISION);
		LinearRing gwtShell = gwtFactory.createLinearRing(new Coordinate[] { new Coordinate(10.0, 10.0),
				new Coordinate(20.0, 10.0), new Coordinate(20.0, 20.0), new Coordinate(10.0, 10.0) });
		LinearRing gwtHole = gwtFactory.createLinearRing(new Coordinate[] { new Coordinate(12.0, 12.0),
				new Coordinate(18.0, 12.0), new Coordinate(18.0, 18.0), new Coordinate(12.0, 12.0) });
		gwt = gwtFactory.createPolygon(gwtShell, new LinearRing[] { gwtHole });

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
		Assert.isTrue(jts.getCentroid().getCoordinate().x - gwt.getCentroid().getX() < 1);
		Assert.isTrue(jts.getCentroid().getCoordinate().y - gwt.getCentroid().getY() < 1);
	}

	@Test
	public void getCoordinate() {
		Assert.equals(jts.getCoordinate().x, gwt.getCoordinate().getX());
	}

	@Test
	public void getCoordinates() {
		Assert.equals(jts.getCoordinates()[6].x, gwt.getCoordinates()[6].getX());
		Assert.equals(jts.getCoordinates().length, gwt.getCoordinates().length);
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
		Assert.equals(jts.getGeometryN(-1).getCoordinate().x, gwt.getGeometryN(-1).getCoordinate().getX());
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

		// TODO: problem with JTS intersection calculation...
		Assert.equals(jts.intersects(jtsLine1), gwt.intersects(gwtLine1)); // No intersection
		// Assert.equals(jts.intersects(jtsLine2), gwt.intersects(gwtLine2)); // crosses LineSegment
		// Assert.equals(jts.intersects(jtsLine3), gwt.intersects(gwtLine3)); // touches point
		Assert.equals(true, gwt.intersects(gwtLine2)); // crosses LineSegment
		Assert.equals(true, gwt.intersects(gwtLine3)); // touches point
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
