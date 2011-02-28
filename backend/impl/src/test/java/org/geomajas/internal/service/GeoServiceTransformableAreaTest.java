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

import junit.framework.Assert;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Crs;
import org.geomajas.geometry.CrsTransform;
import org.geomajas.service.GeoService;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

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
		Assert.assertEquals(4, coordinates.length);
		Assert.assertEquals(123563.57031602281, coordinates[0].x, DELTA);
		Assert.assertEquals(76585.04048137646, coordinates[0].y, DELTA);
		Assert.assertEquals(193837.0401763837 , coordinates[1].x, DELTA);
		Assert.assertEquals(243568.02558119502, coordinates[1].y, DELTA);
		Assert.assertEquals(229406.01730448572, coordinates[2].x, DELTA);
		Assert.assertEquals(188358.75258052722, coordinates[2].y, DELTA);
		Assert.assertEquals(159509.37737547373, coordinates[3].x, DELTA);
		Assert.assertEquals(20914.387943933718, coordinates[3].y, DELTA);
	}

	private Geometry getLineString() {
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);
		return factory.createLineString(new Coordinate[] { new Coordinate(4, 50), new Coordinate(5, 51.5),
				new Coordinate(5.5, 51), new Coordinate(4.5, 49.5) });

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
		Bbox bbox = new Bbox(0, 40, 10, 20);
		Bbox transformed = geoService.transform(bbox, LONLAT, LAMBERT72);
		Assert.assertEquals(-228735.21449864685, transformed.getX(), DELTA);
		Assert.assertEquals(-1040736.2671662597, transformed.getY(), DELTA);
		Assert.assertEquals(866755.5520200664, transformed.getWidth(), DELTA);
		Assert.assertEquals(2247657.6332967337, transformed.getHeight(), DELTA);
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
		Envelope envelope = new Envelope(0, 10, 40, 60);
		Envelope transformed = geoService.transform(envelope, LONLAT, LAMBERT72);
		Assert.assertEquals(-228735.21449864685, transformed.getMinX(), DELTA);
		Assert.assertEquals(-1040736.2671662597, transformed.getMinY(), DELTA);
		Assert.assertEquals(866755.5520200664, transformed.getWidth(), DELTA);
		Assert.assertEquals(2247657.6332967337, transformed.getHeight(), DELTA);
	}

	@Test
	public void transformEnvelopeOutsideAreaTest() throws Exception {
		Envelope envelope = new Envelope(120, 130, 50, 60);
		Envelope transformed = geoService.transform(envelope, LONLAT, LAMBERT72);
		Assert.assertTrue(transformed.isNull());
	}

}
