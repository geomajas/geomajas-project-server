/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.service;

import junit.framework.Assert;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Crs;
import org.geomajas.geometry.CrsTransform;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.internal.layer.feature.InternalFeatureImpl;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.service.GeoService;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Test for {@link GeoService} implementation.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/spring/moreContext.xml"})
public class GeoServiceTest {

	private static final double DELTA = 1e-20;
	private static final String MERCATOR = "EPSG:900913";
	private static final String LONLAT = "EPSG:4326";
	private static final String LAMBERT72 = "EPSG:31300";
	private static final String LAMBERT72_2 = "EPSG:31370";

	@Autowired
	private GeoService geoService;

	@Test
	public void getCrsTest() throws Exception {
		CoordinateReferenceSystem crs = geoService.getCrs(MERCATOR);
		Assert.assertNotNull(crs);
		Assert.assertEquals(900913, geoService.getSridFromCrs(crs));
		Assert.assertEquals(MERCATOR, geoService.getCodeFromCrs(crs));
		crs = geoService.getCrs(LONLAT);
		Assert.assertNotNull(crs);
		Assert.assertEquals(4326, geoService.getSridFromCrs(crs));
		Assert.assertEquals(LONLAT, geoService.getCodeFromCrs(crs));
	}

	@Test
	public void crsInfoTest() throws Exception {
		CoordinateReferenceSystem crs = CRS.decode(LONLAT);
		Assert.assertNotNull(crs);
		Assert.assertEquals(4326, geoService.getSridFromCrs(crs));
		Assert.assertEquals(LONLAT, geoService.getCodeFromCrs(crs));

		Assert.assertEquals(900913, geoService.getSridFromCrs(MERCATOR));
		Assert.assertEquals(4326, geoService.getSridFromCrs(LONLAT));
		Assert.assertEquals(123, geoService.getSridFromCrs("123"));
		Assert.assertEquals(0, geoService.getSridFromCrs("bla"));
	}

	@Test
	public void getCrs2Test() throws Exception {
		Crs crs = geoService.getCrs2(MERCATOR);
		Assert.assertNotNull(crs);
		Assert.assertEquals(900913, geoService.getSridFromCrs(crs));
		Assert.assertEquals(MERCATOR, geoService.getCodeFromCrs(crs));
		crs = geoService.getCrs2(LONLAT);
		Assert.assertNotNull(crs);
		Assert.assertEquals(4326, geoService.getSridFromCrs(crs));
		Assert.assertEquals(LONLAT, geoService.getCodeFromCrs(crs));

		try {
			geoService.getCrs2("BLA:4326");
			Assert.fail("authority should not exist");
		} catch (GeomajasException ge) {
			Assert.assertEquals(ExceptionCode.CRS_DECODE_FAILURE_FOR_MAP, ge.getExceptionCode());
		}
	}

	@Test
	public void transformEnvelopeCrsTest() throws Exception {
		Crs source = geoService.getCrs2(MERCATOR);
		Crs target = geoService.getCrs2(LONLAT);
		Envelope envelope = new Envelope(10, 20, 30, 40);
		Envelope transformed = geoService.transform(envelope, source, target);
		Assert.assertEquals(8.983152841195215E-5, transformed.getMinX(), DELTA);
		Assert.assertEquals(2.6949458522981454E-4, transformed.getMinY(), DELTA);
		Assert.assertEquals(1.796630568239043E-4, transformed.getMaxX(), DELTA);
		Assert.assertEquals(3.593261136397527E-4, transformed.getMaxY(), DELTA);

		Assert.assertEquals(envelope, geoService.transform(envelope, source, source));
	}

	@Test
	public void transformEnvelopeStringTest() throws Exception {
		Envelope envelope = new Envelope(10, 20, 30, 40);
		Envelope transformed = geoService.transform(envelope, MERCATOR, LONLAT);
		Assert.assertEquals(8.983152841195215E-5, transformed.getMinX(), DELTA);
		Assert.assertEquals(2.6949458522981454E-4, transformed.getMinY(), DELTA);
		Assert.assertEquals(1.796630568239043E-4, transformed.getMaxX(), DELTA);
		Assert.assertEquals(3.593261136397527E-4, transformed.getMaxY(), DELTA);
	}

	@Test
	public void transformEnvelopeCrsTransformTest() throws Exception {
		CrsTransform crsTransform = geoService.getCrsTransform(MERCATOR, LONLAT);
		Envelope envelope = new Envelope(10, 20, 30, 40);
		Envelope transformed = geoService.transform(envelope, crsTransform);
		Assert.assertEquals(8.983152841195215E-5, transformed.getMinX(), DELTA);
		Assert.assertEquals(2.6949458522981454E-4, transformed.getMinY(), DELTA);
		Assert.assertEquals(1.796630568239043E-4, transformed.getMaxX(), DELTA);
		Assert.assertEquals(3.593261136397527E-4, transformed.getMaxY(), DELTA);

		Assert.assertEquals(envelope, geoService.transform(envelope, MERCATOR, MERCATOR));
	}

	@Test
	public void transformEnvelopeCrsNoTransformTest() throws Exception {
		CrsTransform crsTransform = geoService.getCrsTransform(MERCATOR, MERCATOR);
		Envelope envelope = new Envelope(10, 20, 30, 40);
		Envelope transformed = geoService.transform(envelope, crsTransform);
		Assert.assertEquals(10, transformed.getMinX(), DELTA);
		Assert.assertEquals(30, transformed.getMinY(), DELTA);
		Assert.assertEquals(20, transformed.getMaxX(), DELTA);
		Assert.assertEquals(40, transformed.getMaxY(), DELTA);

		Assert.assertEquals(envelope, geoService.transform(envelope, MERCATOR, MERCATOR));
	}

	@Test
	public void transformBboxCrsTest() throws Exception {
		Crs source = geoService.getCrs2(MERCATOR);
		Crs target = geoService.getCrs2(LONLAT);
		Bbox bbox = new Bbox(10, 30, 10, 10);
		Bbox transformed = geoService.transform(bbox, source, target);
		Assert.assertEquals(8.983152841195215E-5, transformed.getX(), DELTA);
		Assert.assertEquals(2.6949458522981454E-4, transformed.getY(), DELTA);
		Assert.assertEquals(1.796630568239043E-4, transformed.getMaxX(), DELTA);
		Assert.assertEquals(3.593261136397527E-4, transformed.getMaxY(), DELTA);

		Assert.assertEquals(bbox, geoService.transform(bbox, source, source));
	}

	@Test
	public void transformBboxStringTest() throws Exception {
		Bbox bbox = new Bbox(10, 30, 10, 10);
		Bbox transformed = geoService.transform(bbox, MERCATOR, LONLAT);
		Assert.assertEquals(8.983152841195215E-5, transformed.getX(), DELTA);
		Assert.assertEquals(2.6949458522981454E-4, transformed.getY(), DELTA);
		Assert.assertEquals(1.796630568239043E-4, transformed.getMaxX(), DELTA);
		Assert.assertEquals(3.593261136397527E-4, transformed.getMaxY(), DELTA);

		Assert.assertEquals(bbox, geoService.transform(bbox, MERCATOR, MERCATOR));
	}

	@Test
	public void transformBboxCrsTransformTest() throws Exception {
		CrsTransform crsTransform = geoService.getCrsTransform(MERCATOR, LONLAT);
		Bbox bbox = new Bbox(10, 30, 10, 10);
		Bbox transformed = geoService.transform(bbox, crsTransform);
		Assert.assertEquals(8.983152841195215E-5, transformed.getX(), DELTA);
		Assert.assertEquals(2.6949458522981454E-4, transformed.getY(), DELTA);
		Assert.assertEquals(1.796630568239043E-4, transformed.getMaxX(), DELTA);
		Assert.assertEquals(3.593261136397527E-4, transformed.getMaxY(), DELTA);
	}

	@Test
	public void transformBboxCrsNoTransformTest() throws Exception {
		CrsTransform crsTransform = geoService.getCrsTransform(MERCATOR, MERCATOR);
		Bbox bbox = new Bbox(10, 20, 30, 40);
		Bbox transformed = geoService.transform(bbox, crsTransform);
		Assert.assertEquals(10, transformed.getX(), DELTA);
		Assert.assertEquals(20, transformed.getY(), DELTA);
		Assert.assertEquals(30, transformed.getWidth(), DELTA);
		Assert.assertEquals(40, transformed.getHeight(), DELTA);
	}

	@Test
	public void getCrsTransformCrsTest() throws Exception {
		MathTransform mathTransform = geoService.findMathTransform(CRS.decode(LONLAT), CRS.decode(LAMBERT72));
		Assert.assertEquals("EPSG:4326->EPSG:31300", ((CrsTransform)mathTransform).getId());

		CrsTransform crsTransform = geoService.getCrsTransform(LONLAT, LAMBERT72);
		Assert.assertEquals("EPSG:4326->EPSG:31300", crsTransform.getId());
		Assert.assertTrue(crsTransform.equals(mathTransform));

		CrsTransform crsTransform2 = geoService.getCrsTransform(CRS.decode(LONLAT), CRS.decode(LAMBERT72));
		Assert.assertEquals("EPSG:4326->EPSG:31300", crsTransform2.getId());
		Assert.assertTrue(crsTransform2.equals(mathTransform));
	}

	@Test
	public void testCalcDefaultLabelPosition() throws Exception {
		Geometry geometry;
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);
		Coordinate coordinate;
		InternalFeature feature = new InternalFeatureImpl();
		feature.setId("x");
		feature.setLabel("Label x");
		coordinate = geoService.calcDefaultLabelPosition(feature);
		Assert.assertNull(coordinate);

		feature.setGeometry(factory.createMultiPolygon(new Polygon[] {}));
		coordinate = geoService.calcDefaultLabelPosition(feature);
		Assert.assertNull(coordinate);

		feature.setGeometry(JTS.toGeometry(new Envelope(10, 20, 30, 40)));
		coordinate = geoService.calcDefaultLabelPosition(feature);
		// this tests current behaviour, without claims that this is the "best" (or even "good") position
		Assert.assertEquals(15.0, coordinate.x, DELTA);
		Assert.assertEquals(35.0, coordinate.y, DELTA);

		geometry = factory.createLineString(new Coordinate[] { new Coordinate(5,4), new Coordinate(30,10) });
		feature.setGeometry(geometry);
		coordinate = geoService.calcDefaultLabelPosition(feature);
		// this tests current behaviour, without claims that this is the "best" (or even "good") position
		Assert.assertEquals(5.0, coordinate.x, DELTA);
		Assert.assertEquals(4.0, coordinate.y, DELTA);
	}

	@Test
	public void testCreateCircle() throws Exception {
		Geometry geometry;
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);
		Point point = factory.createPoint(new Coordinate(0, 0));
		Point inside = factory.createPoint(new Coordinate(9.5, 0));
		Point insideFine = factory.createPoint(new Coordinate(6.8, 6.8));
		Point outsideAll = factory.createPoint(new Coordinate(9, 5));

		geometry = geoService.createCircle(point, 10, 4);
		Assert.assertEquals(5, geometry.getCoordinates().length);
		Assert.assertTrue(geometry.contains(inside));
		Assert.assertFalse(geometry.contains(insideFine));
		Assert.assertFalse(geometry.contains(outsideAll));

		geometry = geoService.createCircle(point, 10, 16);
		Assert.assertEquals(17, geometry.getCoordinates().length);
		Assert.assertTrue(geometry.contains(inside));
		Assert.assertTrue(geometry.contains(insideFine));
		Assert.assertFalse(geometry.contains(outsideAll));
	}

	@Test
	public void transformGeometryString() throws Exception {
		Geometry geometry = getLineString();

		Assert.assertEquals(geometry, geoService.transform(geometry, LONLAT, LONLAT));

		geometry = geoService.transform(geometry, LONLAT, LAMBERT72);
		assertTransformedLineString(geometry);
	}

	@Test
	public void transformGeometryCrs() throws Exception {
		Geometry geometry = getLineString();
		Crs source = geoService.getCrs2(LONLAT);
		Crs target = geoService.getCrs2(LAMBERT72);

		Assert.assertEquals(geometry, geoService.transform(geometry, source, source));

		geometry = geoService.transform(geometry, source, target);
		assertTransformedLineString(geometry);
	}

	@Test
	public void transformGeometryCrsTransform() throws Exception {
		Geometry geometry = getLineString();
		CrsTransform transform = geoService.getCrsTransform(LONLAT, LAMBERT72);
		geometry = geoService.transform(geometry, transform);
		assertTransformedLineString(geometry);
	}

	@Test
	public void transformGeometryJtsCrs() throws Exception {
		Geometry geometry = getLineString();
		CoordinateReferenceSystem source = CRS.decode(LONLAT);
		CoordinateReferenceSystem target = CRS.decode(LAMBERT72);

		Assert.assertEquals(geometry, geoService.transform(geometry, source, source));

		geometry = geoService.transform(geometry, source, target);
		assertTransformedLineString(geometry);
	}

	@Test
	public void transformGeometryEmptyResultOnException() throws Exception {
		GeometryFactory geometryFactory = new GeometryFactory();
		WKTReader reader = new WKTReader( geometryFactory );
		
		Point point = (Point) reader.read("POINT (1 1)");
		Geometry geometry = geoService.transform(point, new ThrowingTransform());
		Assert.assertEquals(Point.class, geometry.getClass());
		Assert.assertTrue(geometry.isEmpty());
		
		LineString lineString = (LineString) reader.read("LINESTRING (0 1,1 1)");
		geometry = geoService.transform(lineString, new ThrowingTransform());
		Assert.assertEquals(LineString.class, geometry.getClass());
		Assert.assertTrue(geometry.isEmpty());
		
		Polygon polygon = (Polygon) reader.read("POLYGON ((0 0,1 1,0 1,0 0))");
		geometry = geoService.transform(polygon, new ThrowingTransform());
		Assert.assertEquals(Polygon.class, geometry.getClass());
		Assert.assertTrue(geometry.isEmpty());

		MultiPoint multipoint = (MultiPoint) reader.read("MULTIPOINT ((1 1),(2 1))");
		geometry = geoService.transform(multipoint, new ThrowingTransform());
		Assert.assertEquals(MultiPoint.class, geometry.getClass());
		Assert.assertTrue(geometry.isEmpty());
		
		MultiLineString multilineString = (MultiLineString) reader.read("MULTILINESTRING ((0 1,1 1),(0 2,2 2))");
		geometry = geoService.transform(multilineString, new ThrowingTransform());
		Assert.assertEquals(MultiLineString.class, geometry.getClass());
		Assert.assertTrue(geometry.isEmpty());
		
		MultiPolygon multipolygon = (MultiPolygon) reader.read("MULTIPOLYGON (((0 0,1 1,0 1,0 0)),((0 0,2 2,0 2,0 0)))");
		geometry = geoService.transform(multipolygon, new ThrowingTransform());
		Assert.assertEquals(MultiPolygon.class, geometry.getClass());
		Assert.assertTrue(geometry.isEmpty());
		
		Geometry collection = (GeometryCollection) reader.read("GEOMETRYCOLLECTION(POINT(4 6),LINESTRING(4 6,7 10)) ");
		geometry = geoService.transform(collection, new ThrowingTransform());
		Assert.assertEquals(GeometryCollection.class, geometry.getClass());
		Assert.assertTrue(geometry.isEmpty());
	}

	@Test
	public void transformGeometryCrsNoTransform() throws Exception {
		Geometry geometry = getLineString();
		CrsTransform transform = geoService.getCrsTransform(LONLAT, LONLAT);
		geometry = geoService.transform(geometry, transform);
		Coordinate[] coordinates = geometry.getCoordinates();
		Assert.assertEquals(4, coordinates.length);
		Assert.assertEquals(5, coordinates[0].x, DELTA);
		Assert.assertEquals(4, coordinates[0].y, DELTA);
		Assert.assertEquals(30, coordinates[1].x, DELTA);
		Assert.assertEquals(10, coordinates[1].y, DELTA);
		Assert.assertEquals(120, coordinates[2].x, DELTA);
		Assert.assertEquals(150, coordinates[2].y, DELTA);
		Assert.assertEquals(50, coordinates[3].x, DELTA);
		Assert.assertEquals(50, coordinates[3].y, DELTA);
	}

	@Test
	public void transformCoordinateTest() throws Exception {
		double llx = 10;
		double lly = 30;
		double mx = 8.983152841195215E-5;
		double my = 2.6949458522981454E-4;
		Coordinate ll = new Coordinate(llx, lly);
		Coordinate mc;
		mc = geoService.transform(ll, MERCATOR, LONLAT);
		Assert.assertEquals(mx, mc.x, DELTA);
		Assert.assertEquals(my, mc.y, DELTA);
		mc = geoService.transform(ll, geoService.getCrs2(MERCATOR), geoService.getCrs2(LONLAT));
		Assert.assertEquals(mx, mc.x, DELTA);
		Assert.assertEquals(my, mc.y, DELTA);
		CrsTransform crsTransform = geoService.getCrsTransform(MERCATOR, LONLAT);
		mc = geoService.transform(ll, crsTransform);
		Assert.assertEquals(mx, mc.x, DELTA);
		Assert.assertEquals(my, mc.y, DELTA);
	}

	/**
	 * This is a coordinate and its transformation given by a member from AGIV and is held to be "TRUE".
	 */
	@Test
	public void transformCoordinateLambert72Test() throws Exception {
		Coordinate source = new Coordinate(4.468493, 50.856057);
		Coordinate target = geoService.transform(source, LONLAT, LAMBERT72_2);
		Assert.assertNotNull(target);
		
		// expected result: 157022.870 171745.084 (31370)
		// discrepancy +- 20 cm.
		Assert.assertEquals(157022.9162798329, target.x, DELTA);
		Assert.assertEquals(171745.2290776642, target.y, DELTA);
	}


	private void assertTransformedLineString(Geometry geometry) {
		System.out.println(geometry.toString());
		Coordinate[] coordinates = geometry.getCoordinates();
		Assert.assertEquals(4, coordinates.length);
		Assert.assertEquals(243228.2415398722, coordinates[0].x, DELTA);
		Assert.assertEquals(-5562212.2922869185, coordinates[0].y, DELTA);
		Assert.assertEquals(3571198.1691051605, coordinates[1].x, DELTA);
		Assert.assertEquals(-4114094.247419103, coordinates[1].y, DELTA);
		Assert.assertEquals(-5635610.296096718, coordinates[2].x, DELTA);
		Assert.assertEquals(488056.5712725008, coordinates[2].y, DELTA);
		Assert.assertEquals(3219427.718819718, coordinates[3].x, DELTA);
		Assert.assertEquals(1050557.615059331, coordinates[3].y, DELTA);
	}

	private Geometry getLineString() {
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);
		return factory.createLineString(new Coordinate[] {
				new Coordinate(5, 4), new Coordinate(30, 10), new Coordinate(120, 150), new Coordinate(50, 50)});
	}
	
	public class ThrowingTransform extends CrsTransformImpl {

		public ThrowingTransform() throws LayerException {
			super(null, geoService.getCrs2(MERCATOR), geoService.getCrs2(LAMBERT72), null);
		}

		@Override
		public DirectPosition transform(DirectPosition directPosition, DirectPosition directPosition1)
				throws MismatchedDimensionException, TransformException {
					throw new IllegalArgumentException();
		}

		@Override
		public void transform(double[] doubles, int i, double[] doubles1, int i1, int i2) throws TransformException {
			throw new IllegalArgumentException();
		}

		@Override
		public void transform(float[] floats, int i, float[] floats1, int i1, int i2) throws TransformException {
			throw new IllegalArgumentException();
		}

		@Override
		public void transform(float[] floats, int i, double[] doubles, int i1, int i2) throws TransformException {
			throw new IllegalArgumentException();
		}

		@Override
		public void transform(double[] doubles, int i, float[] floats, int i1, int i2) throws TransformException {
			throw new IllegalArgumentException();
		}

		@Override
		public Geometry getTransformableGeometry() {
			return JTS.toGeometry(getTransformableEnvelope());
		}

		@Override
		public Envelope getTransformableEnvelope() {
			return new Envelope(-10E20, 10E20, -10E20, 10E20);
		}

		@Override
		public Bbox getTransformableBbox() {
			return null;
		}
	}

}
