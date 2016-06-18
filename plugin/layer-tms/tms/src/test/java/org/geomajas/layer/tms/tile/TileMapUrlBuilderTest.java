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

package org.geomajas.layer.tms.tile;

import org.geomajas.layer.tile.TileCode;
import org.geomajas.layer.tms.TmsConfigurationService;
import org.geomajas.layer.tms.TmsLayer;
import org.geomajas.layer.tms.TmsLayerException;
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
		TmsLayer tmsLayer = new TmsLayer();
		tmsLayer.setBaseTmsUrl("classpath:/org/geomajas/layer/tms/tileMapCapa1.xml");
		TileMap tileMap = configurationService
				.getCapabilities(tmsLayer);
		TileMapUrlBuilder builder = new TileMapUrlBuilder(tileMap);
		String url = builder.buildUrl(new TileCode(1, 2, 3), BASE_TMS_URL);
		Assert.assertEquals("http://www.geomajas.org/tms/some_layer/href2/2/3.extension", url);
	}

	@Test
	public void testBuildUrl2() throws TmsLayerException {
		TmsLayer tmsLayer = new TmsLayer();
		tmsLayer.setBaseTmsUrl("classpath:/org/geomajas/layer/tms/tileMapCapa2.xml");
		TileMap tileMap = configurationService
				.getCapabilities(tmsLayer);
		TileMapUrlBuilder builder = new TileMapUrlBuilder(tileMap);
		String url = builder.buildUrl(new TileCode(1, 2, 3), BASE_TMS_URL);
		Assert.assertEquals("http://tms.osgeo.org/1.0.0/landsat2000/512/2/3.png", url);
	}

	@Test
	public void testBuildUrlCornerCases() throws TmsLayerException {
		try {
			new TileMapUrlBuilder(null);
			Assert.fail();
		} catch (IllegalStateException e) {
			// As expected...
		}

		TmsLayer tmsLayer = new TmsLayer();
		tmsLayer.setBaseTmsUrl("classpath:/org/geomajas/layer/tms/tileMapCapa2.xml");
		TileMap tileMap = configurationService
				.getCapabilities(tmsLayer);

		TileMapUrlBuilder builder = new TileMapUrlBuilder(tileMap);
		try {
			builder.buildUrl(null, BASE_TMS_URL);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			// As expected...
		}

		try {
			builder.buildUrl(new TileCode(), null);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			// As expected...
		}
	}
	
	@Test
	public void testParseTileCode() {
		String relativeUrl = "12/5/7.jpg";
		TileCode tc = TileMapUrlBuilder.parseTileCode(relativeUrl);
		
		Assert.assertNotNull(tc);
		Assert.assertTrue(tc.getTileLevel() == 12);
		Assert.assertTrue(tc.getX() == 5);
		Assert.assertTrue(tc.getY() == 7);
	}
}