/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.smartgwt.client.spatial.geometry;

import org.geomajas.geometry.Coordinate;
import org.geomajas.smartgwt.client.spatial.Bbox;
import org.geomajas.smartgwt.client.spatial.geometry.GeometryFactory;
import org.geomajas.smartgwt.client.spatial.geometry.LineString;
import org.junit.Assert;
import org.junit.Test;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKTReader;

/**
 * <p>
 * The purpose of this class is to test the methods of the GWT
 * {@link org.geomajas.smartgwt.client.spatial.geometry.LineString} class. We do this by comparing them to JTS results.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class GwtLineStringTest {

	private static final int SRID = 4326;

	private static final int PRECISION = -1;

	private static final double ZERO = 0.00001;

	private static final double DELTA = 1E-10;

	private GeometryFactory gwtFactory;

	private com.vividsolutions.jts.geom.GeometryFactory jtsFactory;

	private LineString gwt;

	private LineString empty;

	private com.vividsolutions.jts.geom.LineString jts;

	// -------------------------------------------------------------------------
	// Constructor, initializes the 2 LineString geometries:
	// -------------------------------------------------------------------------

	public GwtLineStringTest() {
		gwtFactory = new GeometryFactory(SRID, PRECISION);
		gwt = gwtFactory.createLineString(new Coordinate[] {new Coordinate(10.0, 10.0), new Coordinate(20.0, 10.0),
				new Coordinate(20.0, 20.0)});
		empty = gwtFactory.createLineString(null);

		jtsFactory = new com.vividsolutions.jts.geom.GeometryFactory(new PrecisionModel(), SRID);
		jts = jtsFactory.createLineString(new com.vividsolutions.jts.geom.Coordinate[] {
				new com.vividsolutions.jts.geom.Coordinate(10.0, 10.0),
				new com.vividsolutions.jts.geom.Coordinate(20.0, 10.0),
				new com.vividsolutions.jts.geom.Coordinate(20.0, 20.0)});
	}

	@Test
	public void getCentroid() {
		Assert.assertEquals(jts.getCentroid().getCoordinate().x, gwt.getCentroid().getX(), DELTA);
		Assert.assertEquals(jts.getCentroid().getCoordinate().y, gwt.getCentroid().getY(), DELTA);
	}

	@Test
	public void getCoordinate() {
		Assert.assertEquals(jts.getCoordinate().x, gwt.getCoordinate().getX(), DELTA);
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
	public void intersects() {
		com.vividsolutions.jts.geom.LineString jtsLine1 = jtsFactory
				.createLineString(new com.vividsolutions.jts.geom.Coordinate[] {
						new com.vividsolutions.jts.geom.Coordinate(0, 0),
						new com.vividsolutions.jts.geom.Coordinate(15, 0)});
		com.vividsolutions.jts.geom.LineString jtsLine2 = jtsFactory
				.createLineString(new com.vividsolutions.jts.geom.Coordinate[] {
						new com.vividsolutions.jts.geom.Coordinate(15, 5),
						new com.vividsolutions.jts.geom.Coordinate(15, 25)});
		com.vividsolutions.jts.geom.LineString jtsLine3 = jtsFactory
				.createLineString(new com.vividsolutions.jts.geom.Coordinate[] {
						new com.vividsolutions.jts.geom.Coordinate(0, 0),
						new com.vividsolutions.jts.geom.Coordinate(15, 15)});

		LineString gwtLine1 = gwtFactory.createLineString(new Coordinate[] {new Coordinate(0, 0),
				new Coordinate(15, 0)});
		LineString gwtLine2 = gwtFactory.createLineString(new Coordinate[] {new Coordinate(15, 5),
				new Coordinate(15, 25)});
		LineString gwtLine3 = gwtFactory.createLineString(new Coordinate[] {new Coordinate(0, 0),
				new Coordinate(15, 15)});

		Assert.assertEquals(jts.intersects(jtsLine1), gwt.intersects(gwtLine1)); // No intersection
		Assert.assertEquals(jts.intersects(jtsLine2), gwt.intersects(gwtLine2)); // crosses LineSegment
		Assert.assertEquals(jts.intersects(jtsLine3), gwt.intersects(gwtLine3)); // touches point
	}

	@Test
	public void getArea() {
		Assert.assertTrue((jts.getArea() - gwt.getArea()) < ZERO);
	}

	@Test
	public void getLength() {
		Assert.assertTrue((jts.getLength() - gwt.getLength()) < ZERO);
	}

	@Test
	public void toWkt() throws Exception {
		WKTReader reader = new WKTReader();
		com.vividsolutions.jts.geom.Geometry result = reader.read(gwt.toWkt());
		Assert.assertEquals(gwt.getCoordinate().getX(), result.getCoordinate().x, DELTA);
		Assert.assertEquals(gwt.getCoordinate().getY(), result.getCoordinate().y, DELTA);
		Assert.assertEquals("LINESTRING EMPTY", empty.toWkt());
	}
}
