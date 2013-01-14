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

import java.util.List;

import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.service.GeoService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Test for the tile service state.
 * 
 * @author Pieter De Graef
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/layer/tms/tmsContext.xml" })
@DirtiesContext
public class TileServiceStateTest {

	private static final double DELTA = 0.000001;

	@Autowired
	private GeoService geoService;

	@Autowired
	@Qualifier("tmsInfo")
	private RasterLayerInfo layerInfo;

	@Test
	public void testState() {
		layerInfo.setCrs("EPSG:4326");
		TileServiceState state = new TileServiceState(geoService, layerInfo);

		// Check CRS and tile size:
		Assert.assertNotNull(state.getCrs());
		Assert.assertEquals("EPSG:4326", state.getCrs().getId());
		Assert.assertEquals(layerInfo.getTileWidth(), state.getTileWidth());
		Assert.assertEquals(layerInfo.getTileHeight(), state.getTileHeight());

		// Check maximum bounds:
		Envelope maxBounds = state.getMaxBounds();
		Assert.assertEquals(layerInfo.getMaxExtent().getX(), maxBounds.getMinX(), DELTA);
		Assert.assertEquals(layerInfo.getMaxExtent().getY(), maxBounds.getMinY(), DELTA);
		Assert.assertEquals(layerInfo.getMaxExtent().getMaxX(), maxBounds.getMaxX(), DELTA);
		Assert.assertEquals(layerInfo.getMaxExtent().getMaxY(), maxBounds.getMaxY(), DELTA);

		// Check zoom levels:
		List<Double> resolutions = state.getResolutions();
		Assert.assertEquals(layerInfo.getZoomLevels().size(), resolutions.size());

		Assert.assertEquals(1.0, resolutions.get(0).doubleValue(), DELTA);
		Assert.assertEquals(0.5, resolutions.get(1).doubleValue(), DELTA);
		Assert.assertEquals(0.25, resolutions.get(2).doubleValue(), DELTA);
		Assert.assertEquals(0.125, resolutions.get(3).doubleValue(), DELTA);
	}

	@Test
	public void testFaultyState() {
		layerInfo.setCrs("EPSG:something");
		try {
			new TileServiceState(geoService, layerInfo);
			Assert.fail();
		} catch (IllegalStateException e) {
			// As exptected...
		}
	}
}