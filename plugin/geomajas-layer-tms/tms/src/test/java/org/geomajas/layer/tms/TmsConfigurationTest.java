/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.layer.tms;

import org.geomajas.layer.tms.configuration.TileMapInfo;
import org.geomajas.layer.tms.configuration.TileSetInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test for the TMS configuration service.
 * 
 * @author Pieter De Graef
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/layer/tms/tmsContext.xml" })
public class TmsConfigurationTest {

	private static final double DELTA = 0.00001;

	@Autowired
	private TmsConfigurationService configurationService;

	@Test
	public void testParseConfiguration() throws TmsConfigurationException {
		TileMapInfo tileMap = configurationService
				.getCapabilities("classpath:/org/geomajas/layer/tms/tileMapCapa1.xml");

		// Test basic parameters:
		Assert.assertNotNull(tileMap);
		Assert.assertEquals("abstract", tileMap.getAbstractTxt());
		Assert.assertEquals("srs", tileMap.getSrs());
		Assert.assertEquals("title", tileMap.getTitle());
		Assert.assertEquals("version", tileMap.getVersion());

		// Test bounding box:
		Assert.assertEquals(1, tileMap.getBoundingBox().getMinX(), DELTA);
		Assert.assertEquals(2, tileMap.getBoundingBox().getMinY(), DELTA);
		Assert.assertEquals(3, tileMap.getBoundingBox().getMaxX(), DELTA);
		Assert.assertEquals(4, tileMap.getBoundingBox().getMaxY(), DELTA);

		// Test origin:
		Assert.assertEquals(5, tileMap.getOrigin().getX(), DELTA);
		Assert.assertEquals(6, tileMap.getOrigin().getY(), DELTA);

		// Test TileFormat:
		Assert.assertEquals(7, tileMap.getTileFormat().getWidth());
		Assert.assertEquals(8, tileMap.getTileFormat().getHeight());
		Assert.assertEquals("extension", tileMap.getTileFormat().getExtension());
		Assert.assertEquals("mimetype", tileMap.getTileFormat().getMimeType());

		// Test Tile sets:
		Assert.assertEquals("profile", tileMap.getTileSets().getProfile());
		Assert.assertEquals(2, tileMap.getTileSets().getTileSets().size());

		// First TileSet:
		TileSetInfo tileSet = tileMap.getTileSets().getTileSets().get(0);
		Assert.assertEquals(9, tileSet.getUnitsPerPixel(), DELTA);
		Assert.assertEquals(0, tileSet.getOrder(), DELTA);
		Assert.assertEquals("href1", tileSet.getHref());

		// Second TileSet:
		tileSet = tileMap.getTileSets().getTileSets().get(1);
		Assert.assertEquals(10, tileSet.getUnitsPerPixel(), DELTA);
		Assert.assertEquals(1, tileSet.getOrder(), DELTA);
		Assert.assertEquals("href2", tileSet.getHref());
	}
}