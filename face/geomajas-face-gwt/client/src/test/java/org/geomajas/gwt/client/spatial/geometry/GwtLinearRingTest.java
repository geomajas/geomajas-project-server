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

package org.geomajas.gwt.client.spatial.geometry;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.service.WktException;
import org.geomajas.geometry.service.WktService;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * <p>
 * The purpose of this class is to test the methods of the GWT {@link LinearRing} class. We do this by comparing them to
 * JTS results.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class GwtLinearRingTest {

	private static final int SRID = 4326;

	private static final int PRECISION = -1;

	private static final double DELTA = 1e-4;

	private GeometryFactory gwtFactory;

	private com.vividsolutions.jts.geom.GeometryFactory jtsFactory;

	private LinearRing gwt;

	private com.vividsolutions.jts.geom.LinearRing jts;

	// -------------------------------------------------------------------------
	// Constructor, initializes the 2 LineString geometries:
	// -------------------------------------------------------------------------

	@Before
	public void init() {
		gwtFactory = new GeometryFactory(SRID, PRECISION);
		gwt = gwtFactory.createLinearRing(new Coordinate[] { new Coordinate(10.0, 10.0), new Coordinate(20.0, 10.0),
				new Coordinate(20.0, 20.0), new Coordinate(10.0, 10.0) });

		jtsFactory = new com.vividsolutions.jts.geom.GeometryFactory(new PrecisionModel(), SRID);
		jts = jtsFactory.createLinearRing(new com.vividsolutions.jts.geom.Coordinate[] {
				new com.vividsolutions.jts.geom.Coordinate(10.0, 10.0),
				new com.vividsolutions.jts.geom.Coordinate(20.0, 10.0),
				new com.vividsolutions.jts.geom.Coordinate(20.0, 20.0),
				new com.vividsolutions.jts.geom.Coordinate(10.0, 10.0) });
	}

	@Test
	public void getCentroid() throws WktException, ParseException {
		Assert.assertTrue(jts.getCentroid().getCoordinate().x - gwt.getCentroid().getX() < 1);
		Assert.assertTrue(jts.getCentroid().getCoordinate().y - gwt.getCentroid().getY() < 1);

		String wkt = "POLYGON ((948732.820500011 6078756.569848541, 1068024.3649477751 6028202.01937218, "
				+ "1072333.0579502126 5998997.156511122, 1055305.3793291312 5958871.450440632, 1105675.0935204166 "
				+ "5929144.458480931, 1162476.2079476311 5924715.38833866, 1153645.9759600186 5858183.406784581, "
				+ "1104605.1110124618 5830956.472744024, 1066165.5338210524 5840392.513424949, 946203.9886702409 "
				+ "6074778.917027304, 948732.820500011 6078756.569848541))";
		Geometry geometry = GeometryConverter.toGwt(WktService.toGeometry(wkt));
		Coordinate c = geometry.getCentroid();

		WKTReader r = new WKTReader(jtsFactory);
		com.vividsolutions.jts.geom.Geometry geometry2 = r.read(wkt);
		com.vividsolutions.jts.geom.Point c2 = geometry2.getCentroid();

		Assert.assertEquals(c2.getY(), c.getY(), DELTA);
		Assert.assertEquals(c2.getX(), c.getX(), DELTA);
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
		Assert.assertEquals(jts.isValid(), gwt.isValid());
		LineString one = gwtFactory.createLinearRing(new Coordinate[] { new Coordinate(10.0, 10.0) });
		LineString two = gwtFactory.createLinearRing(new Coordinate[] { new Coordinate(10.0, 10.0),
				new Coordinate(10.0, 10.0) });
		LineString three = gwtFactory.createLinearRing(new Coordinate[] { new Coordinate(10.0, 10.0),
				new Coordinate(20.0, 10.0), new Coordinate(10.0, 10.0) });
		LineString four = gwtFactory.createLinearRing(new Coordinate[] { new Coordinate(10.0, 10.0),
				new Coordinate(20.0, 10.0), new Coordinate(20.0, 20.0), new Coordinate(10.0, 10.0) });
		LineString intersects = gwtFactory.createLinearRing(new Coordinate[] { new Coordinate(10.0, 10.0),
				new Coordinate(20.0, 10.0), new Coordinate(10.0, 20.0), new Coordinate(20.0, 20.0),
				new Coordinate(10.0, 10.0) });
		Assert.assertFalse(one.isValid());
		Assert.assertFalse(two.isValid());
		Assert.assertFalse(three.isValid());
		Assert.assertTrue(four.isValid());
		Assert.assertFalse(intersects.isValid());
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
		double ja = jts.getArea();
		if (ja > 0) { // sanity check, getArea does not seem to work for linear ring. Once it does, the test will use it
			Assert.assertEquals(ja, gwt.getArea(), DELTA);
		}
	}

	@Test
	public void getAreaJtsBugFix() {
		// convert LinearRing to polygon to make the test work because of JTS problem with getArea() on LinearRing
		com.vividsolutions.jts.geom.Polygon p = jts.getFactory().createPolygon(jts, null);
		double ja = p.getArea();
		Assert.assertEquals(ja, gwt.getArea(), DELTA);
	}

	@Test
	public void getLength() {
		Assert.assertEquals(jts.getLength(), gwt.getLength(), DELTA);
	}
}