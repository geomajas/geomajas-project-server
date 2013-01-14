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

package org.geomajas.test.widget;

import org.geomajas.geometry.Coordinate;
import org.geomajas.service.GeoService;
import org.geomajas.test.client.util.GeoUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Envelope;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml" })
public class MarkerMapTest {

	@Autowired
	private GeoService service;

	@Test
	public void testConversion() throws Exception {
		Coordinate latlon = new Coordinate(50, 4);
		Coordinate google = GeoUtil.convertToGoogle(latlon);
		Envelope env = service.transform(new Envelope(0, 4, 0, 50), "EPSG:4326", "EPSG:900913");
		Assert.assertEquals(google.getX(), env.getMaxX(), 0.01);
		Assert.assertEquals(google.getY(), env.getMaxY(), 0.01);
	}

	@Test
	public void testZoomLevel() {
		Assert.assertEquals(1/156543.034, GeoUtil.getScaleForZoomLevel(0), 1E-10);
		Assert.assertEquals(2/156543.034, GeoUtil.getScaleForZoomLevel(1), 1E-10);
	}
}
