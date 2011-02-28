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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test for {@link org.geomajas.service.GeoService} implementation.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/spring/transformableArea.xml", "/org/geomajas/spring/moreContext.xml"})
public class GeoServiceTransformableAreaTest {

	private static final double DELTA = 1e-20;
	private static final String LONLAT = "EPSG:4326";
	private static final String LAMBERT72 = "EPSG:31300";

	@Autowired
	private GeoService geoService;

	@Test
	public void transformGeometryString() throws Exception {
		Geometry geometry = getLineString();
		geometry = geoService.transform(geometry, LONLAT, LAMBERT72);
		assertTransformedLineString(geometry);
	}

	@Test
	public void transformGeometryCrs() throws Exception {
		Geometry geometry = getLineString();
		Crs source = geoService.getCrs2(LONLAT);
		Crs target = geoService.getCrs2(LAMBERT72);
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
		geometry = geoService.transform(geometry, source, target);
		assertTransformedLineString(geometry);
	}

	private void assertTransformedLineString(Geometry geometry) {
		Coordinate[] coordinates = geometry.getCoordinates();
		Assert.assertEquals(5, coordinates.length);
		Assert.assertEquals(243228.2415398722, coordinates[0].x, DELTA);
		Assert.assertEquals(-5562212.2922869185, coordinates[0].y, DELTA);
		Assert.assertEquals(3571198.1691051605, coordinates[1].x, DELTA);
		Assert.assertEquals(-4114094.247419103, coordinates[1].y, DELTA);
		Assert.assertEquals(-1559252.030797058, coordinates[2].x, DELTA);
		Assert.assertEquals(4925010.054948342, coordinates[2].y, DELTA);
		Assert.assertEquals(-1576255.123949388, coordinates[3].x, DELTA);
		Assert.assertEquals(4991115.69215949, coordinates[3].y, DELTA);
		Assert.assertEquals(3219427.718819718, coordinates[4].x, DELTA);
		Assert.assertEquals(1050557.615059331, coordinates[4].y, DELTA);
	}

	private Geometry getLineString() {
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);
		return factory.createLineString(new Coordinate[] {
				new Coordinate(5, 4), new Coordinate(30, 10), new Coordinate(120, 150), new Coordinate(50, 50)});
	}

	@Test
	public void transformOutsideAreaTest() throws Exception {
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);
		Geometry geometry = factory.createLineString(new Coordinate[] {
				new Coordinate(110, 50), new Coordinate(120, 60)});
		geometry = geoService.transform(geometry, LONLAT, LAMBERT72);
		Assert.assertTrue(geometry.isEmpty());
	}

	@Test
	public void transformBboxTest() throws Exception {
		Bbox bbox = new Bbox(50, 50, 100, 10);
		Bbox transformed = geoService.transform(bbox, LONLAT, LAMBERT72);
		Assert.assertEquals(2574606.0591756646, transformed.getX(), DELTA);
		Assert.assertEquals(1050557.615059331, transformed.getY(), DELTA);
		Assert.assertEquals(2687255.809476059, transformed.getWidth(), DELTA);
		Assert.assertEquals(3175793.6757641807, transformed.getHeight(), DELTA);
	}

	@Test
	public void transformBboxOutsideAreaTest() throws Exception {
		Bbox bbox = new Bbox(120, 50, 10, 10);
		Bbox transformed = geoService.transform(bbox, LONLAT, LAMBERT72);
		Assert.assertEquals(0.0, transformed.getX(), DELTA);
		Assert.assertEquals(0.0, transformed.getY(), DELTA);
		Assert.assertEquals(0.0, transformed.getMaxX(), DELTA);
		Assert.assertEquals(0.0, transformed.getMaxY(), DELTA);
	}

	@Test
	public void transformEnvelopeTest() throws Exception {
		Envelope envelope = new Envelope(50, 150, 50, 60);
		Envelope transformed = geoService.transform(envelope, LONLAT, LAMBERT72);
		Assert.assertEquals(2574606.0591756646, transformed.getMinX(), DELTA);
		Assert.assertEquals(1050557.615059331, transformed.getMinY(), DELTA);
		Assert.assertEquals(5261861.8686517235, transformed.getMaxX(), DELTA);
		Assert.assertEquals(4226351.290823512, transformed.getMaxY(), DELTA);
	}

	@Test
	public void transformEnvelopeOutsideAreaTest() throws Exception {
		Envelope envelope = new Envelope(120, 130, 50, 60);
		Envelope transformed = geoService.transform(envelope, LONLAT, LAMBERT72);
		Assert.assertTrue(transformed.isNull());
	}

}
