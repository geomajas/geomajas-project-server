/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.layer.tms;

import java.util.List;

import junit.framework.Assert;

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
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
 * Test for the TMS raster layer.
 * 
 * @author Pieter De Graef
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/layer/tms/tmsContext.xml" })
public class TmsLayerTest {

	@Autowired
	@Qualifier("tmsLayer")
	private TmsLayer layer;

	@Autowired
	private GeoService geoService;

	@Test
	public void testLayerGetters() {
		Assert.assertEquals("tmsLayer", layer.getId());
		Assert.assertEquals("EPSG:31370", layer.getCrs().getIdentifiers().iterator().next().toString());
		Assert.assertEquals("classpath:/org/geomajas/layer/tms/tileMapCapa2.xml", layer.getBaseTmsUrl());
		Assert.assertEquals("png", layer.getExtension());
		Assert.assertEquals("1.0.0", layer.getVersion());
		Assert.assertNotNull(layer.getLayerInfo());
		Assert.assertEquals("myLayerTitle", layer.getLayerInfo().getDataSourceName());
	}

	@Test
	@SuppressWarnings("deprecation")
	public void testPaintLevel0() throws LayerException, GeomajasException {
		Envelope maxBounds = new Envelope(18000.0, 259500.250, 152999.75, 244500.0);
		List<RasterTile> tiles = layer.paint(geoService.getCrs("EPSG:31370"), maxBounds, 1.0 / 1024.0);
		Assert.assertEquals(1, tiles.size());
		Assert.assertEquals(0, tiles.get(0).getCode().getTileLevel());
		Assert.assertEquals(0, tiles.get(0).getCode().getX());
		Assert.assertEquals(0, tiles.get(0).getCode().getY());
		Assert.assertEquals("http://tms.osgeo.org/1.0.0/landsat2000/1024/0/0.png", tiles.get(0).getUrl());
	}

	@Test
	@SuppressWarnings("deprecation")
	public void testPaintLevel1() throws LayerException, GeomajasException {
		double resolution = 512.0;
		Envelope maxBounds = new Envelope(18000.0, 259500.250, 152999.75, 244500.0);
		List<RasterTile> tiles = layer.paint(geoService.getCrs("EPSG:31370"), maxBounds, 1.0 / resolution);
		Assert.assertEquals(2, tiles.size());
		Assert.assertEquals(1, tiles.get(0).getCode().getTileLevel());
		Assert.assertEquals(0, tiles.get(0).getCode().getX());
		Assert.assertEquals(0, tiles.get(0).getCode().getY());
		Assert.assertEquals("http://tms.osgeo.org/1.0.0/landsat2000/" + (int) resolution + "/0/0.png", tiles.get(0)
				.getUrl());
		Assert.assertEquals(1, tiles.get(1).getCode().getTileLevel());
		Assert.assertEquals(1, tiles.get(1).getCode().getX());
		Assert.assertEquals(0, tiles.get(1).getCode().getY());
		Assert.assertEquals("http://tms.osgeo.org/1.0.0/landsat2000/" + (int) resolution + "/1/0.png", tiles.get(1)
				.getUrl());
	}
}