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

package org.geomajas.layer.tms.tile;

import org.geomajas.layer.tile.TileCode;
import org.geomajas.layer.tms.TmsLayerException;
import org.geomajas.layer.tms.TmsConfigurationService;
import org.geomajas.layer.tms.xml.TileMap;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test cases to test the simple URL builder for TMS.
 * 
 * @author Pieter De Graef
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/layer/tms/tmsContext.xml" })
public class TileMapUrlBuilderTest {

	private static final String BASE_TMS_URL = "http://www.geomajas.org/tms/some_layer";

	@Autowired
	private TmsConfigurationService configurationService;

	@Test
	public void testBuildUrl1() throws TmsLayerException {
		TileMap tileMap = configurationService
				.getCapabilities("classpath:/org/geomajas/layer/tms/tileMapCapa1.xml");
		TileMapUrlBuilder builder = new TileMapUrlBuilder(tileMap, BASE_TMS_URL);
		String url = builder.buildUrl(new TileCode(1, 2, 3));
		Assert.assertEquals("http://www.geomajas.org/tms/some_layer/href2/2/3.extension", url);
	}

	@Test
	public void testBuildUrl2() throws TmsLayerException {
		TileMap tileMap = configurationService
				.getCapabilities("classpath:/org/geomajas/layer/tms/tileMapCapa2.xml");
		TileMapUrlBuilder builder = new TileMapUrlBuilder(tileMap, BASE_TMS_URL);
		String url = builder.buildUrl(new TileCode(1, 2, 3));
		Assert.assertEquals("http://tms.osgeo.org/1.0.0/landsat2000/512/2/3.png", url);
	}

	@Test
	public void testBuildUrlCornerCases() throws TmsLayerException {
		try {
			new TileMapUrlBuilder(null, BASE_TMS_URL);
			Assert.fail();
		} catch (IllegalStateException e) {
			// As expected...
		}

		TileMap tileMap = configurationService
				.getCapabilities("classpath:/org/geomajas/layer/tms/tileMapCapa2.xml");
		try {
			new TileMapUrlBuilder(tileMap, null);
			Assert.fail();
		} catch (IllegalStateException e) {
			// As expected...
		}

		TileMapUrlBuilder builder = new TileMapUrlBuilder(tileMap, BASE_TMS_URL);
		try {
			builder.buildUrl(null);
			Assert.fail();
		} catch (NullPointerException e) {
			// As expected...
		}
	}
}