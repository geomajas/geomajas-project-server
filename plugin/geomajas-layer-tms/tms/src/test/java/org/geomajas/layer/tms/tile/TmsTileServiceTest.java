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

package org.geomajas.layer.tms.tile;

import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.service.GeoService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test for the tile service.
 * 
 * @author Pieter De Graef
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/layer/tms/tmsContext.xml" })
public class TmsTileServiceTest {

	private static final double DELTA = 0.000001;

	@Autowired
	private TileService tileService;

	@Autowired
	private GeoService geoService;

	@Autowired
	@Qualifier("tmsInfo")
	private RasterLayerInfo layerInfo;

	@Test
	public void testGetTileLevel() {
		TileServiceState state = new TileServiceState(geoService, layerInfo);
		Assert.assertEquals(layerInfo.getZoomLevels().size() - 1, tileService.getTileLevel(state, 0));
		Assert.assertEquals(0, tileService.getTileLevel(state, Double.MAX_VALUE));

		Assert.assertEquals(0, tileService.getTileLevel(state, 1.0));
		Assert.assertEquals(0, tileService.getTileLevel(state, 0.7));
		Assert.assertEquals(1, tileService.getTileLevel(state, 2.0 / 3.0));
		Assert.assertEquals(1, tileService.getTileLevel(state, 0.6));
		Assert.assertEquals(1, tileService.getTileLevel(state, 0.5));
		Assert.assertEquals(2, tileService.getTileLevel(state, 0.25));
		Assert.assertEquals(3, tileService.getTileLevel(state, 0.125));
	}

	@Test
	public void testGetTileSize() {
		TileServiceState state = new TileServiceState(geoService, layerInfo);

		// Check tile width:
		Assert.assertEquals(1.0 * layerInfo.getTileWidth(), tileService.getTileWidth(state, 0), DELTA);
		Assert.assertEquals(0.5 * layerInfo.getTileWidth(), tileService.getTileWidth(state, 1), DELTA);
		Assert.assertEquals(0.25 * layerInfo.getTileWidth(), tileService.getTileWidth(state, 2), DELTA);
		Assert.assertEquals(0.125 * layerInfo.getTileWidth(), tileService.getTileWidth(state, 3), DELTA);

		// Check tile height:
		Assert.assertEquals(1.0 * layerInfo.getTileHeight(), tileService.getTileHeight(state, 0), DELTA);
		Assert.assertEquals(0.5 * layerInfo.getTileHeight(), tileService.getTileHeight(state, 1), DELTA);
		Assert.assertEquals(0.25 * layerInfo.getTileHeight(), tileService.getTileHeight(state, 2), DELTA);
		Assert.assertEquals(0.125 * layerInfo.getTileHeight(), tileService.getTileHeight(state, 3), DELTA);
	}
}