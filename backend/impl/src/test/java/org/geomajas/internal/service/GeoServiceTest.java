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

package org.geomajas.internal.service;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import junit.framework.Assert;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Crs;
import org.geomajas.geometry.CrsTransform;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.internal.layer.feature.InternalFeatureImpl;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.service.GeoService;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test for {@link GeoService} implementation.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/spring/moreContext.xml"})
@DirtiesContext
public class GeoServiceTest {

	private static final double DELTA = 1e-20;
	private static final String MERCATOR = "EPSG:900913";
	private static final String LONLAT = "EPSG:4326";
	private static final String LAMBERT72 = "EPSG:31300";

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

	private void assertTransformedLineString(Geometry geometry) {
		Coordinate[] coordinates = geometry.getCoordinates();
		Assert.assertEquals(4, coordinates.length);
		Assert.assertEquals(243226.22754535213, coordinates[0].x, DELTA);
		Assert.assertEquals(-5562215.514234281, coordinates[0].y, DELTA);
		Assert.assertEquals(3571200.025158979, coordinates[1].x, DELTA);
		Assert.assertEquals(-4114095.376986935, coordinates[1].y, DELTA);
		Assert.assertEquals(-5635607.7135451175, coordinates[2].x, DELTA);
		Assert.assertEquals(488062.62359615415, coordinates[2].y, DELTA);
		Assert.assertEquals(3219426.4637164664, coordinates[3].x, DELTA);
		Assert.assertEquals(1050557.6016714368, coordinates[3].y, DELTA);
	}

	private Geometry getLineString() {
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);
		return factory.createLineString(new Coordinate[] {
				new Coordinate(5, 4), new Coordinate(30, 10), new Coordinate(120, 150), new Coordinate(50, 50)});

	}
}
