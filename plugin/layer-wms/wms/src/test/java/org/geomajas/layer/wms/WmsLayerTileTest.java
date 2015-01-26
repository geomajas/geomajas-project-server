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

package org.geomajas.layer.wms;

import java.util.List;

import junit.framework.Assert;

import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.geometry.Crs;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.service.GeoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Test for {@link WmsLayer}.
 * 
 * @author Pieter De Graef
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "/wmsContext2.xml" })
public class WmsLayerTileTest {

	private static final String LAMBERT = "EPSG:31300";

	@Autowired
	@Qualifier("layerLuchtfotos")
	private WmsLayer wms;

	@Autowired
	private GeoService geoService;

	@Test
	public void testLowestTileLevel() throws Exception {
		Crs lambert = geoService.getCrs2(LAMBERT);

		double x = wms.getLayerInfo().getMaxExtent().getX();
		double y = wms.getLayerInfo().getMaxExtent().getY();
		ScaleInfo scaleInfo = wms.getLayerInfo().getZoomLevels().get(wms.getLayerInfo().getZoomLevels().size()- 1);

		Envelope bounds = new Envelope(x - 1, x + 1, y - 1, y + 1);
		double scale = scaleInfo.getPixelPerUnit();

		List<RasterTile> tiles = wms.paint(lambert, bounds, scale);

		Assert.assertEquals(1, tiles.size());
		RasterTile tile = tiles.get(0);
		double resultX = tile.getBounds().getX() / scale;
		double resultY = -tile.getBounds().getY() / scale - tile.getBounds().getHeight() / scale;
		Assert.assertEquals(x, resultX, 0.1);
		Assert.assertEquals(y, resultY, 0.1);
	}
}