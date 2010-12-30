/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.internal.service;

import com.vividsolutions.jts.geom.Envelope;
import junit.framework.Assert;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Crs;
import org.geomajas.geometry.CrsTransform;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.service.GeoService;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.springframework.beans.factory.annotation.Autowired;
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
public class GeoServiceTest {

	private static final double DELTA = 1e-20;
	private static final String MERCATOR = "EPSG:900913";
	private static final String LONLAT = "EPSG:4326";
	private static final String LAMBERT72 = "EPSG:31300";


	@Autowired
	private GeoService geoService;

	/*
	MathTransform findMathTransform(CoordinateReferenceSystem sourceCrs,
			CoordinateReferenceSystem targetCrs) throws GeomajasException;
	CrsTransform getCrsTransform(Crs sourceCrs, Crs targetCrs) throws GeomajasException;
	CrsTransform getCrsTransform(CoordinateReferenceSystem sourceCrs, CoordinateReferenceSystem targetCrs)
			throws GeomajasException;
	Geometry transform(Geometry source, CrsTransform crsTransform);
	Geometry transform(Geometry source, Crs sourceCrs, Crs targetCrs) throws GeomajasException;
	@Deprecated
	Geometry transform(Geometry source, CoordinateReferenceSystem sourceCrs, CoordinateReferenceSystem targetCrs)
			throws GeomajasException;
	Coordinate calcDefaultLabelPosition(InternalFeature feature);
	Geometry createCircle(Point center, double radius, int nrPoints);
	*/

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
}
