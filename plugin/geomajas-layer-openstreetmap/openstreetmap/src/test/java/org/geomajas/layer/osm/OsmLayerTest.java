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
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/osmContext.xml"})
public class OsmLayerTest {

	private static final double ZOOMED_IN_SCALE = .0001;
	private static final double MAX_LEVEL_SCALE = .1;
	private static final double DELTA = 1e-10;

	@Autowired
	@Qualifier("osmSingle")
	private OsmLayer osm;

	@Autowired
	@Qualifier("osmWrongCrs")
	private OsmLayer osmWrongCrs;

	@Autowired
	@Qualifier("osmMaxLevel")
	private OsmLayer osmMaxLevel;

	@Autowired
	@Qualifier("osmCycleMap")
	private OsmLayer osmCycleMap;

	@Autowired
	private GeoService geoService;

	@Test
	public void testPaintOutOfBounds() throws Exception {
		double equator = TiledRasterLayerService.EQUATOR_IN_METERS;
		List<RasterTile> tiles =
				osm.paint(osm.getCrs(), new Envelope(-equator, equator, -equator, equator), 256 / equator);
		Assert.assertEquals(1, tiles.size());
		Assert.assertEquals("http://a.tile.openstreetmap.org/0/0/0.png", tiles.iterator().next().getUrl());
	}

	@Test
	public void testPaintToTheSide() throws Exception {
		double equator = TiledRasterLayerService.EQUATOR_IN_METERS;
		List<RasterTile> tiles = osm.paint(osm.getCrs(),
				new Envelope(equator * 2 / 3, (equator * 2 / 3) + 100, 0, 100), ZOOMED_IN_SCALE);
		Assert.assertEquals(0, tiles.size());
	}

	@Test
	public void testWrongCrsCorrected() throws Exception {
		CoordinateReferenceSystem crs = osmWrongCrs.getCrs();
		Assert.assertEquals(900913, geoService.getSridFromCrs(crs));
		Assert.assertEquals("EPSG:900913", osmWrongCrs.getLayerInfo().getCrs());
	}

	@Test
	public void testMaxLevel() throws Exception {
		Envelope envelope = new Envelope(10000, 10002, 5000, 5002);
		RasterTile tile;
		List<RasterTile> tiles;
		tiles = osm.paint(osm.getCrs(), envelope, MAX_LEVEL_SCALE);
		Assert.assertEquals(1, tiles.size());
		tile = tiles.get(0);
		Assert.assertEquals("http://a.tile.openstreetmap.org/14/8196/8189.png", tile.getUrl());
		tiles = osmMaxLevel.paint(osmMaxLevel.getCrs(), envelope, MAX_LEVEL_SCALE);
		Assert.assertEquals(1, tiles.size());
		tile = tiles.get(0);
		Assert.assertEquals("http://a.tile.openstreetmap.org/12/2049/2047.png", tile.getUrl());
	}

	@Test
	public void testConfigUrlStrategy() throws Exception {
		Envelope envelope = new Envelope(10000, 10002, 5000, 5002);
		RasterTile tile;
		List<RasterTile> tiles;
		tiles = osmCycleMap.paint(osmCycleMap.getCrs(), envelope, MAX_LEVEL_SCALE);
		Assert.assertEquals(1, tiles.size());
		tile = tiles.get(0);
		Assert.assertEquals("http://c.tile.opencyclemap.org/14/8196/8189.png", tile.getUrl());
		tiles = osmCycleMap.paint(osmCycleMap.getCrs(), envelope, MAX_LEVEL_SCALE);
		Assert.assertEquals(1, tiles.size());
		tile = tiles.get(0);
		Assert.assertEquals("http://c.tile.opencyclemap.org/14/8196/8189.png", tile.getUrl());
	}

	@Test
	public void testNormalOne() throws Exception {
		List<RasterTile> tiles = osm.paint(osm.getCrs(),
				new Envelope(10000, 10010, 5000, 5010), ZOOMED_IN_SCALE);
		Assert.assertEquals(1, tiles.size());
		RasterTile tile = tiles.get(0);
		Assert.assertEquals("http://a.tile.openstreetmap.org/4/8/7.png", tile.getUrl());
		Assert.assertEquals(4, tile.getCode().getTileLevel());
		Assert.assertEquals(8, tile.getCode().getX());
		Assert.assertEquals(7, tile.getCode().getY());
		Assert.assertEquals(0.0, tile.getBounds().getX(), DELTA);
		Assert.assertEquals(-250.0, tile.getBounds().getY(), DELTA);
		Assert.assertEquals(250.0, tile.getBounds().getHeight(), DELTA);
		Assert.assertEquals(250.0, tile.getBounds().getWidth(), DELTA);
	}

	@Test
	public void testNormalSeveral() throws Exception {
		Envelope envelope = new Envelope(10000, 13000, 5000, 8000);
		List<RasterTile> tiles = osm.paint(osm.getCrs(), envelope, MAX_LEVEL_SCALE);
		Assert.assertEquals(4, tiles.size());
		Assert.assertEquals("http://a.tile.openstreetmap.org/14/8196/8188.png", tiles.get(0).getUrl());
		Assert.assertEquals("http://a.tile.openstreetmap.org/14/8196/8189.png", tiles.get(1).getUrl());
		Assert.assertEquals("http://a.tile.openstreetmap.org/14/8197/8188.png", tiles.get(2).getUrl());
		Assert.assertEquals("http://a.tile.openstreetmap.org/14/8197/8189.png", tiles.get(3).getUrl());
		RasterTile tile = tiles.get(3);
		Assert.assertEquals("http://a.tile.openstreetmap.org/14/8197/8189.png", tile.getUrl());
		Assert.assertEquals(14, tile.getCode().getTileLevel());
		Assert.assertEquals(8197, tile.getCode().getX());
		Assert.assertEquals(8189, tile.getCode().getY());
		Assert.assertEquals(1223, tile.getBounds().getX(), DELTA);
		Assert.assertEquals(-733.0, tile.getBounds().getY(), DELTA);
		Assert.assertEquals(245.0, tile.getBounds().getHeight(), DELTA);
		Assert.assertEquals(245.0, tile.getBounds().getWidth(), DELTA);
	}

}
