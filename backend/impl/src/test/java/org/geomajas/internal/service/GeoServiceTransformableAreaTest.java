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
		Assert.assertEquals(243226.22754535213, coordinates[0].x, DELTA);
		Assert.assertEquals(-5562215.514234281, coordinates[0].y, DELTA);
		Assert.assertEquals(3571200.025158979, coordinates[1].x, DELTA);
		Assert.assertEquals(-4114095.376986935, coordinates[1].y, DELTA);
		Assert.assertEquals(-1559247.1313968082, coordinates[2].x, DELTA);
		Assert.assertEquals(4925007.245555261, coordinates[2].y, DELTA);
		Assert.assertEquals(-1576250.1368931415, coordinates[3].x, DELTA);
		Assert.assertEquals(4991112.851941023, coordinates[3].y, DELTA);
		Assert.assertEquals(3219426.4637164664, coordinates[4].x, DELTA);
		Assert.assertEquals(1050557.6016714368, coordinates[4].y, DELTA);
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
		Assert.assertEquals(2574604.73895413, transformed.getX(), DELTA);
		Assert.assertEquals(1050557.6016714368, transformed.getY(), DELTA);
		Assert.assertEquals(5261856.632877763, transformed.getMaxX(), DELTA);
		Assert.assertEquals(4226349.363675014, transformed.getMaxY(), DELTA);
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
		Assert.assertEquals(2574604.73895413, transformed.getMinX(), DELTA);
		Assert.assertEquals(1050557.6016714368, transformed.getMinY(), DELTA);
		Assert.assertEquals(5261856.632877763, transformed.getMaxX(), DELTA);
		Assert.assertEquals(4226349.363675014, transformed.getMaxY(), DELTA);
	}

	@Test
	public void transformEnvelopeOutsideAreaTest() throws Exception {
		Envelope envelope = new Envelope(120, 130, 50, 60);
		Envelope transformed = geoService.transform(envelope, LONLAT, LAMBERT72);
		Assert.assertTrue(transformed.isNull());
	}

}
