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

package org.geomajas.layer.tms;

import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.layer.LayerType;
import org.geomajas.layer.tms.xml.TileMap;
import org.geomajas.layer.tms.xml.TileSet;
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
	public void testParseConfiguration() throws TmsLayerException {
		TileMap tileMap = configurationService
				.getCapabilities("classpath:/org/geomajas/layer/tms/tileMapCapa1.xml");

		// Test basic parameters:
		Assert.assertNotNull(tileMap);
		Assert.assertEquals("abstract", tileMap.getAbstractTxt());
		Assert.assertEquals("srs", tileMap.getSrs());
		Assert.assertEquals("title", tileMap.getTitle());
		Assert.assertEquals("version", tileMap.getVersion());
		Assert.assertEquals("tilemapservice", tileMap.getTileMapService());

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
		TileSet tileSet = tileMap.getTileSets().getTileSets().get(0);
		Assert.assertEquals(9, tileSet.getUnitsPerPixel(), DELTA);
		Assert.assertEquals(0, tileSet.getOrder(), DELTA);
		Assert.assertEquals("href1", tileSet.getHref());

		// Second TileSet:
		tileSet = tileMap.getTileSets().getTileSets().get(1);
		Assert.assertEquals(10, tileSet.getUnitsPerPixel(), DELTA);
		Assert.assertEquals(1, tileSet.getOrder(), DELTA);
		Assert.assertEquals("href2", tileSet.getHref());
	}

	@Test
	public void testAsLayerInfo() throws TmsLayerException {
		TileMap tileMap = configurationService
				.getCapabilities("classpath:/org/geomajas/layer/tms/tileMapCapa1.xml");
		RasterLayerInfo layerInfo = configurationService.asLayerInfo(tileMap);

		// Check general parameters:
		Assert.assertEquals(LayerType.RASTER, layerInfo.getLayerType());
		Assert.assertEquals(tileMap.getTitle(), layerInfo.getDataSourceName());
		Assert.assertEquals(tileMap.getSrs(), layerInfo.getCrs());
		Assert.assertEquals(tileMap.getTileFormat().getWidth(), layerInfo.getTileWidth());
		Assert.assertEquals(tileMap.getTileFormat().getHeight(), layerInfo.getTileHeight());

		// Check maximum extent:
		Bbox maxExtent = layerInfo.getMaxExtent();
		Assert.assertEquals(tileMap.getBoundingBox().getMinX(), maxExtent.getX(), DELTA);
		Assert.assertEquals(tileMap.getBoundingBox().getMinY(), maxExtent.getY(), DELTA);
		Assert.assertEquals(tileMap.getBoundingBox().getMaxX(), maxExtent.getMaxX(), DELTA);
		Assert.assertEquals(tileMap.getBoundingBox().getMaxY(), maxExtent.getMaxY(), DELTA);

		// Check the zoom levels:
		Assert.assertEquals(tileMap.getTileSets().getTileSets().size(), layerInfo.getZoomLevels().size());
		for (int i = 0; i < layerInfo.getZoomLevels().size(); i++) {
			Assert.assertEquals(tileMap.getTileSets().getTileSets().get(i).getUnitsPerPixel(), 1 / layerInfo
					.getZoomLevels().get(i).getPixelPerUnit(), DELTA);
		}
	}
}