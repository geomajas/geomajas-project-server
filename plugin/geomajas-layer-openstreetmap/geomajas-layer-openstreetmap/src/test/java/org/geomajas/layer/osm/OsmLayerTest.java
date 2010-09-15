/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.layer.osm;

import com.vividsolutions.jts.geom.Envelope;
import junit.framework.Assert;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.service.GeoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/osmContext.xml" })
public class OsmLayerTest {

	private static final double ZOOMED_IN_SCALE = .0001;
	private static final double DELTA = 1e-10;

	@Autowired
	@Qualifier("osm")
	private OsmLayer osm;

	@Autowired
	private GeoService geoService;

	@Test
	public void testPaintOutOfBounds() throws Exception {
		double equator = TiledRasterLayerService.EQUATOR_IN_METERS;
		List<RasterTile> tiles = osm.paint(geoService.getCrs("EPSG:900913"),
				new Envelope(-equator, equator, -equator, equator), 256 / equator);
		Assert.assertEquals(1, tiles.size());
		Assert.assertEquals("http://a.tile.openstreetmap.org/0/0/0.png", tiles.iterator().next().getUrl());
	}

	@Test
	public void testPaintToTheSide() throws Exception {
		double equator = TiledRasterLayerService.EQUATOR_IN_METERS;
		List<RasterTile> tiles = osm.paint(geoService.getCrs("EPSG:900913"),
				new Envelope(equator * 2 / 3, (equator * 2 / 3) + 100, 0, 100), ZOOMED_IN_SCALE);
		Assert.assertEquals(0, tiles.size());
	}

	@Test
	public void testNormalOne() throws Exception {
		List<RasterTile> tiles = osm.paint(geoService.getCrs("EPSG:900913"),
				new Envelope(10000, 10010, 5000, 5010), ZOOMED_IN_SCALE);
		Assert.assertEquals(1, tiles.size());
		RasterTile tile = tiles.get(0);
		Assert.assertEquals("http://b.tile.openstreetmap.org/4/8/7.png", tile.getUrl());
		Assert.assertEquals(4, tile.getCode().getTileLevel());
		Assert.assertEquals(8, tile.getCode().getX());
		Assert.assertEquals(7, tile.getCode().getY());
		Assert.assertEquals(0.0, tile.getBounds().getX(), DELTA);
		/*
		Assert.assertEquals(-256.0, tile.getBounds().getY(), DELTA);
		Assert.assertEquals(256.0, tile.getBounds().getHeight(), DELTA);
		Assert.assertEquals(256.0, tile.getBounds().getWidth(), DELTA);
		*/
	}

	/*
	@Test
	public void testNormalSeveral() throws Exception {
		List<RasterTile> tiles = osm.paint(geoService.getCrs("EPSG:900913"),
				new Envelope(10000, 10010, 5000, 5010), ZOOMED_IN_SCALE);
		Assert.assertEquals(1, tiles.size());
		Assert.assertEquals("http://b.tile.openstreetmap.org/4/8/7.png", tiles.iterator().next().getUrl());
	}
	*/

}
