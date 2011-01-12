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

package org.geomajas.gwt.client.spatial.geometry;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.util.Assert;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.spatial.Bbox;
import org.junit.Test;

/**
 * <p>
 * The purpose of this class is to test the methods of the GWT {@link MultiPolygon} class. We do this by comparing them
 * to JTS results.
 * </p>
 *
 * @author Pieter De Graef
 */
public class GwtMultiPolygonTest {

	private final static int SRID = 4326;

	private final static int PRECISION = -1;

	private final static double ZERO = 0.00001;

	private GeometryFactory gwtFactory;

	private com.vividsolutions.jts.geom.GeometryFactory jtsFactory;

	private MultiPolygon gwt;

	private com.vividsolutions.jts.geom.MultiPolygon jts;

	// -------------------------------------------------------------------------
	// Constructor, initializes the 2 MultiPolygon geometries:
	// -------------------------------------------------------------------------

	public GwtMultiPolygonTest() {
		gwtFactory = new GeometryFactory(SRID, PRECISION);
		LinearRing gwtRing1 = gwtFactory.createLinearRing(new Coordinate[] {new Coordinate(10.0, 10.0),
				new Coordinate(20.0, 10.0), new Coordinate(20.0, 20.0), new Coordinate(10.0, 10.0)});
		Polygon gwtPolygon1 = gwtFactory.createPolygon(gwtRing1, null);
		LinearRing gwtRing2 = gwtFactory.createLinearRing(new Coordinate[] {new Coordinate(10.0, 20.0),
				new Coordinate(30.0, 10.0), new Coordinate(40.0, 10.0), new Coordinate(10.0, 20.0)});
		Polygon gwtPolygon2 = gwtFactory.createPolygon(gwtRing2, null);
		gwt = gwtFactory.createMultiPolygon(new Polygon[] {gwtPolygon1, gwtPolygon2});

		jtsFactory = new com.vividsolutions.jts.geom.GeometryFactory(new PrecisionModel(), SRID);
		com.vividsolutions.jts.geom.LinearRing jtsRing1 = jtsFactory
				.createLinearRing(new com.vividsolutions.jts.geom.Coordinate[] {
						new com.vividsolutions.jts.geom.Coordinate(10.0, 10.0),
						new com.vividsolutions.jts.geom.Coordinate(20.0, 10.0),
						new com.vividsolutions.jts.geom.Coordinate(20.0, 20.0),
						new com.vividsolutions.jts.geom.Coordinate(10.0, 10.0)});
		com.vividsolutions.jts.geom.Polygon jtsPolygon1 = jtsFactory.createPolygon(jtsRing1, null);
		com.vividsolutions.jts.geom.LinearRing jtsRing2 = jtsFactory
				.createLinearRing(new com.vividsolutions.jts.geom.Coordinate[] {
						new com.vividsolutions.jts.geom.Coordinate(10.0, 20.0),
						new com.vividsolutions.jts.geom.Coordinate(30.0, 10.0),
						new com.vividsolutions.jts.geom.Coordinate(40.0, 10.0),
						new com.vividsolutions.jts.geom.Coordinate(10.0, 20.0)});
		com.vividsolutions.jts.geom.Polygon jtsPolygon2 = jtsFactory.createPolygon(jtsRing2, null);
		jts = jtsFactory.createMultiPolygon(new com.vividsolutions.jts.geom.Polygon[] {jtsPolygon1, jtsPolygon2});
	}

	// -------------------------------------------------------------------------
	// The actual test cases:
	// -------------------------------------------------------------------------

	@Test
	public void getCentroid() {
		Assert.isTrue(jts.getCentroid().getCoordinate().x - gwt.getCentroid().getX() < ZERO);
		Assert.isTrue(jts.getCentroid().getCoordinate().y - gwt.getCentroid().getY() < ZERO);
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
						new com.vividsolutions.jts.geom.Coordinate(15, 0)});
//		com.vividsolutions.jts.geom.LineString jtsLine2 = jtsFactory
//		        .createLineString(new com.vividsolutions.jts.geom.Coordinate[] {
//		                new com.vividsolutions.jts.geom.Coordinate(15, 5),
//		                new com.vividsolutions.jts.geom.Coordinate(15, 25) });
//		com.vividsolutions.jts.geom.LineString jtsLine3 = jtsFactory
//		        .createLineString(new com.vividsolutions.jts.geom.Coordinate[] {
//		                new com.vividsolutions.jts.geom.Coordinate(0, 0),
//		                new com.vividsolutions.jts.geom.Coordinate(15, 15) });

		LineString gwtLine1 = gwtFactory.createLineString(new Coordinate[] {new Coordinate(0, 0),
				new Coordinate(15, 0)});
		// LineString gwtLine2 = gwtFactory.createLineString(new Coordinate[] { new Coordinate(15, 5),
		// new Coordinate(15, 25) });
		// LineString gwtLine3 = gwtFactory.createLineString(new Coordinate[] { new Coordinate(0, 0),
		// new Coordinate(15, 15) });

		Assert.equals(jts.intersects(jtsLine1), gwt.intersects(gwtLine1)); // No intersection
		// Assert.equals(jts.intersects(jtsLine2), gwt.intersects(gwtLine2)); // crosses LineSegment
		// Assert.equals(jts.intersects(jtsLine3), gwt.intersects(gwtLine3)); // touches point
	}

	@Test
	public void getArea() {
		Assert.isTrue((jts.getArea() - gwt.getArea()) < ZERO);
	}

	@Test
	public void getLength() {
		Assert.isTrue((jts.getLength() - gwt.getLength()) < ZERO);
	}
}
