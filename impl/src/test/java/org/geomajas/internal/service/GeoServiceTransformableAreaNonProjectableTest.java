/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.service;

import junit.framework.Assert;

import org.geomajas.geometry.Bbox;
import org.geomajas.service.GeoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test for {@link org.geomajas.service.GeoService} implementation.
 * 
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/spring/transformableAreaNonProjectable.xml", "/org/geomajas/spring/moreContext.xml" })
public class GeoServiceTransformableAreaNonProjectableTest {

	private static final double DELTA = 1e-20;

	private static final String LONLAT = "EPSG:4326";

	private static final String LAMBERT72 = "EPSG:31300";

	@Autowired
	private GeoService geoService;

	/**
	 * Transforms a bbox in an area that is outside the map projection area of validity.
	 * 
	 * @throws Exception
	 */
	@Test
	public void transformBboxOutsideTest() throws Exception {
		Bbox bbox = new Bbox(0, 0, 1, 1);
		Bbox transformed = geoService.transform(bbox, LONLAT, LAMBERT72);
		// we expect a warning and an empty box
		Assert.assertEquals(0.0, transformed.getX(), DELTA);
		Assert.assertEquals(0.0, transformed.getY(), DELTA);
		Assert.assertEquals(0.0, transformed.getMaxX(), DELTA);
		Assert.assertEquals(0.0, transformed.getMaxY(), DELTA);
	}

	/**
	 * Transforms a bbox in an area that is partly outside the map projection area of validity.
	 * 
	 * @throws Exception
	 */
	@Test
	public void transformBboxOverlappingTest() throws Exception {
		Bbox bbox = new Bbox(51, 5, 10, 10);
		Bbox transformed1 = geoService.transform(bbox, LONLAT, LAMBERT72);
		// we expect the same result if we transform the clipped part
		Bbox bboxClipped = new Bbox(51, 5, 6, 4);
		Bbox transformed2 = geoService.transform(bboxClipped, LONLAT, LAMBERT72);

		Assert.assertEquals(transformed1.getX(), transformed2.getX(), DELTA);
		Assert.assertEquals(transformed1.getY(), transformed2.getY(), DELTA);
		Assert.assertEquals(transformed1.getMaxX(), transformed2.getMaxX(), DELTA);
		Assert.assertEquals(transformed1.getMaxY(), transformed2.getMaxY(), DELTA);
	}

}
