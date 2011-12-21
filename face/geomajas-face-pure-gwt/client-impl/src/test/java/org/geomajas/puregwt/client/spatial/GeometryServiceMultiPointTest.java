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
 * The purpose of this class is to test the methods of the GWT {@link MultiPoint} class. We do this by comparing them to
 * JTS results.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class GeometryServiceMultiPointTest {

	private static final int SRID = 4326;

	private static final double DELTA = 1E-10;

	private GeometryService geometryService;

	private com.vividsolutions.jts.geom.GeometryFactory jtsFactory;

	private Geometry gwt;

	private com.vividsolutions.jts.geom.MultiPoint jts;

	// -------------------------------------------------------------------------
	// Constructor, initializes the 2 point geometries:
	// -------------------------------------------------------------------------

	@Before
	public void setUp() {
		Injector myInjector = Guice.createInjector(new GeomajasTestModule());
		geometryService = myInjector.getInstance(GeometryService.class);

		Geometry point1 = new Geometry(Geometry.POINT, SRID, 0);
		point1.setCoordinates(new Coordinate[] { new Coordinate(10.0, 10.0) });
		Geometry point2 = new Geometry(Geometry.POINT, SRID, 0);
		point2.setCoordinates(new Coordinate[] { new Coordinate(10.0, 20.0) });
		Geometry point3 = new Geometry(Geometry.POINT, SRID, 0);
		point3.setCoordinates(new Coordinate[] { new Coordinate(20.0, 20.0) });

		gwt = new Geometry(Geometry.MULTI_POINT, SRID, 0);
		gwt.setGeometries(new Geometry[] { point1, point2, point3 });

		jtsFactory = new com.vividsolutions.jts.geom.GeometryFactory(new PrecisionModel(), SRID);
		jts = jtsFactory.createMultiPoint(new com.vividsolutions.jts.geom.Coordinate[] {
				new com.vividsolutions.jts.geom.Coordinate(10.0, 10.0),
				new com.vividsolutions.jts.geom.Coordinate(10.0, 20.0),
				new com.vividsolutions.jts.geom.Coordinate(20.0, 20.0) });
	}

	@Test
	public void getCentroid() {
		Assert.assertEquals(jts.getCentroid().getCoordinate().x, geometryService.getCentroid(gwt).getX(), DELTA);
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
	}

	@Test
	public void getArea() {
		Assert.assertTrue((jts.getArea() - geometryService.getArea(gwt)) < DELTA);
	}

	@Test
	public void getLength() {
		Assert.assertTrue((jts.getLength() - geometryService.getLength(gwt)) < DELTA);
	}
}