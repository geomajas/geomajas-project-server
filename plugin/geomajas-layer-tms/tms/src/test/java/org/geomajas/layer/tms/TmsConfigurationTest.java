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

import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.layer.LayerType;
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
		TileMapInfo tileMapInfo = configurationService
				.getCapabilities("classpath:/org/geomajas/layer/tms/tileMapCapa1.xml");

		// Test basic parameters:
		Assert.assertNotNull(tileMapInfo);
		Assert.assertEquals("abstract", tileMapInfo.getAbstractTxt());
		Assert.assertEquals("srs", tileMapInfo.getSrs());
		Assert.assertEquals("title", tileMapInfo.getTitle());
		Assert.assertEquals("version", tileMapInfo.getVersion());
		Assert.assertEquals("tilemapservice", tileMapInfo.getTileMapService());

		// Test bounding box:
		Assert.assertEquals(1, tileMapInfo.getBoundingBox().getMinX(), DELTA);
		Assert.assertEquals(2, tileMapInfo.getBoundingBox().getMinY(), DELTA);
		Assert.assertEquals(3, tileMapInfo.getBoundingBox().getMaxX(), DELTA);
		Assert.assertEquals(4, tileMapInfo.getBoundingBox().getMaxY(), DELTA);

		// Test origin:
		Assert.assertEquals(5, tileMapInfo.getOrigin().getX(), DELTA);
		Assert.assertEquals(6, tileMapInfo.getOrigin().getY(), DELTA);

		// Test TileFormat:
		Assert.assertEquals(7, tileMapInfo.getTileFormat().getWidth());
		Assert.assertEquals(8, tileMapInfo.getTileFormat().getHeight());
		Assert.assertEquals("extension", tileMapInfo.getTileFormat().getExtension());
		Assert.assertEquals("mimetype", tileMapInfo.getTileFormat().getMimeType());

		// Test Tile sets:
		Assert.assertEquals("profile", tileMapInfo.getTileSets().getProfile());
		Assert.assertEquals(2, tileMapInfo.getTileSets().getTileSets().size());

		// First TileSet:
		TileSetInfo tileSet = tileMapInfo.getTileSets().getTileSets().get(0);
		Assert.assertEquals(9, tileSet.getUnitsPerPixel(), DELTA);
		Assert.assertEquals(0, tileSet.getOrder(), DELTA);
		Assert.assertEquals("href1", tileSet.getHref());

		// Second TileSet:
		tileSet = tileMapInfo.getTileSets().getTileSets().get(1);
		Assert.assertEquals(10, tileSet.getUnitsPerPixel(), DELTA);
		Assert.assertEquals(1, tileSet.getOrder(), DELTA);
		Assert.assertEquals("href2", tileSet.getHref());
	}

	@Test
	public void testAsLayerInfo() throws TmsConfigurationException {
		TileMapInfo tileMapInfo = configurationService
				.getCapabilities("classpath:/org/geomajas/layer/tms/tileMapCapa1.xml");
		RasterLayerInfo layerInfo = configurationService.asLayerInfo(tileMapInfo);

		// Check general parameters:
		Assert.assertEquals(LayerType.RASTER, layerInfo.getLayerType());
		Assert.assertEquals(tileMapInfo.getTitle(), layerInfo.getDataSourceName());
		Assert.assertEquals(tileMapInfo.getSrs(), layerInfo.getCrs());
		Assert.assertEquals(tileMapInfo.getTileFormat().getWidth(), layerInfo.getTileWidth());
		Assert.assertEquals(tileMapInfo.getTileFormat().getHeight(), layerInfo.getTileHeight());

		// Check maximum extent:
		Bbox maxExtent = layerInfo.getMaxExtent();
		Assert.assertEquals(tileMapInfo.getBoundingBox().getMinX(), maxExtent.getX(), DELTA);
		Assert.assertEquals(tileMapInfo.getBoundingBox().getMinY(), maxExtent.getY(), DELTA);
		Assert.assertEquals(tileMapInfo.getBoundingBox().getMaxX(), maxExtent.getMaxX(), DELTA);
		Assert.assertEquals(tileMapInfo.getBoundingBox().getMaxY(), maxExtent.getMaxY(), DELTA);

		// Check the zoom levels:
		Assert.assertEquals(tileMapInfo.getTileSets().getTileSets().size(), layerInfo.getZoomLevels().size());
		for (int i = 0; i < layerInfo.getZoomLevels().size(); i++) {
			Assert.assertEquals(tileMapInfo.getTileSets().getTileSets().get(i).getUnitsPerPixel(), 1 / layerInfo
					.getZoomLevels().get(i).getPixelPerUnit(), DELTA);
		}
	}
}