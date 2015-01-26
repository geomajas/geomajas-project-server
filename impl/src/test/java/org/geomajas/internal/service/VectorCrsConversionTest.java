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

import java.util.List;

import junit.framework.Assert;

import org.geomajas.geometry.Crs;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.security.SecurityManager;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;
import org.geotools.geometry.jts.JTS;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.filter.Filter;
import org.opengis.referencing.operation.MathTransform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Tests for CRS conversions in VectorLayerService, using data which is also used in the CRS showcase for reference.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/beanContext.xml", "/org/geomajas/layer/bean/layerBeansMercator.xml"})
public class VectorCrsConversionTest {

	private static final String LAYER_ID = "beans";
	private static final String FEATURE_ID = "10";
	private static final double TOLERANCE = .00000001;

	private static final double LON1 = 0;
	private static final double LAT1 = 0;
	private static final double LON2 = 0;
	private static final double LAT2 = 20;
	private static final double LON3 = 20;
	private static final double LAT3 = 20;
	private static final double LON4 = 20;
	private static final double LAT4 = 0;
	private static final double X1 = 0;
	private static final double Y1 = -7.081154551613622E-10;
	private static final double X2 = 0;
	private static final double Y2 = 2273030.926987689;
	private static final double X3 = 2226389.8158654715;
	private static final double Y3 = 2273030.926987689;
	private static final double X4 = 2226389.8158654715;
	private static final double Y4 = 0;
	
	@Autowired
	private VectorLayerService layerService;

	@Autowired
	private FilterService filterService;

	@Autowired
	private SecurityManager securityManager;

	@Autowired
	private GeoService geoService;

	private MathTransform layerToMap;

	@Before
	public void login() {
		// assure security context is set
		securityManager.createSecurityContext(null);
	}

	@After
	public void clearSecurityContext() {
		securityManager.clearSecurityContext();
	}

	@Test
	public void testVerifyConversion() throws Exception {
		Crs mercator = geoService.getCrs2("EPSG:900913");
		Crs lonlat = geoService.getCrs2("EPSG:4326");

		layerToMap = geoService.findMathTransform(mercator, lonlat);

		List<InternalFeature> features;
		Filter filter = filterService.createFidFilter(new String[] {FEATURE_ID});
		features =
				layerService.getFeatures(LAYER_ID, mercator, filter, null, VectorLayerService.FEATURE_INCLUDE_GEOMETRY);
		Assert.assertEquals(1, features.size());
		Coordinate[] coordinatesMercator = features.get(0).getGeometry().getCoordinates();
		features =
				layerService.getFeatures(LAYER_ID, lonlat, filter, null, VectorLayerService.FEATURE_INCLUDE_GEOMETRY);
		Assert.assertEquals(1, features.size());
		Coordinate[] coordinatesLonlat = features.get(0).getGeometry().getCoordinates();

		// now check expectations, both from layer as from conversion
		verify(coordinatesMercator[0], coordinatesLonlat[0], X1, Y1, LON1, LAT1);
		verify(coordinatesMercator[1], coordinatesLonlat[1], X2, Y2, LON2, LAT2);
		verify(coordinatesMercator[2], coordinatesLonlat[2], X3, Y3, LON3, LAT3);
		verify(coordinatesMercator[3], coordinatesLonlat[3], X4, Y4, LON4, LAT4);
		verify(coordinatesMercator[3], coordinatesLonlat[3], X4, Y4, LON4, LAT4);
		/*
		verify(coordinatesMercator[3], coordinatesLonlat[3], X4, Y4, 30, 10);
		verify(coordinatesMercator[3], coordinatesLonlat[3], X4, Y4, 40, 10);
		verify(coordinatesMercator[3], coordinatesLonlat[3], X4, Y4, 40, 40);
		verify(coordinatesMercator[3], coordinatesLonlat[3], X4, Y4, 30, 40);
		verify(coordinatesMercator[3], coordinatesLonlat[3], X4, Y4, -14, 31);
		verify(coordinatesMercator[3], coordinatesLonlat[3], X4, Y4, 19, 31);
		verify(coordinatesMercator[3], coordinatesLonlat[3], X4, Y4, 19, 39);
		verify(coordinatesMercator[3], coordinatesLonlat[3], X4, Y4, -14, 39);
		*/
	}

	private void verify(Coordinate mercator, Coordinate lonlat, double x, double y, double lon, double lat)
			throws Exception {
		Coordinate projected = new Coordinate();
		projected = JTS.transform(mercator, projected, layerToMap);
		Assert.assertEquals(x, mercator.x, TOLERANCE);
		Assert.assertEquals(lon, projected.x, TOLERANCE);
		Assert.assertEquals(lon, lonlat.x, TOLERANCE);
		Assert.assertEquals(y, mercator.y, TOLERANCE);
		Assert.assertEquals(lat, projected.y, TOLERANCE);
		Assert.assertEquals(lat, lonlat.y, TOLERANCE);
	}
}
