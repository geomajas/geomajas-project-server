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

package org.geomajas.layer.osm;

import com.vividsolutions.jts.geom.Envelope;
import junit.framework.Assert;

import org.geomajas.geometry.Crs;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.service.GeoService;
import org.geotools.geometry.jts.JTS;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Test for {@link OsmLayer}.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "/osmContext.xml" })
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
		List<RasterTile> tiles = osm.paint(osm.getCrs(), new Envelope(-equator, equator, -equator, equator),
				256 / equator);
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
		Assert.assertEquals("http://c.tile.opencyclemap.org/cycle/14/8196/8189.png", tile.getUrl());
		tiles = osmCycleMap.paint(osmCycleMap.getCrs(), envelope, MAX_LEVEL_SCALE);
		Assert.assertEquals(1, tiles.size());
		tile = tiles.get(0);
		Assert.assertEquals("http://c.tile.opencyclemap.org/cycle/14/8196/8189.png", tile.getUrl());
	}

	@Test
	public void testNormalOne() throws Exception {
		List<RasterTile> tiles = osm.paint(osm.getCrs(), new Envelope(10000, 10010, 5000, 5010), ZOOMED_IN_SCALE);
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

	@Test
	public void testReprojectOne() throws Exception {
		Envelope googleEnvelope = new Envelope(10000, 10010, 5000, 5010);
		// back-transform envelope to latlon
		Crs google = geoService.getCrs2("EPSG:900913");
		Crs latlon = geoService.getCrs2("EPSG:4326");
		Envelope latlonEnvelope = geoService.transform(JTS.toGeometry(googleEnvelope), google, latlon)
				.getEnvelopeInternal();
		// back-transform scale to latlon
		double latlonScale = ZOOMED_IN_SCALE * googleEnvelope.getWidth() / latlonEnvelope.getWidth();
		// paint with reprojection (affine is fine for now...:-)
		List<RasterTile> tiles = osm.paint(latlon, latlonEnvelope, latlonScale);
		Assert.assertEquals(1, tiles.size());
		RasterTile tile = tiles.get(0);
		Assert.assertEquals("http://a.tile.openstreetmap.org/4/8/7.png", tile.getUrl());
		Assert.assertEquals(4, tile.getCode().getTileLevel());
		Assert.assertEquals(8, tile.getCode().getX());
		Assert.assertEquals(7, tile.getCode().getY());
		Assert.assertEquals(0.0, tile.getBounds().getX(), DELTA);
		Assert.assertEquals(-244.0, tile.getBounds().getY(), DELTA);
		Assert.assertEquals(244.0, tile.getBounds().getHeight(), DELTA);
		Assert.assertEquals(250.0, tile.getBounds().getWidth(), DELTA);
	}

	@Test
	public void testReprojectSeveral() throws Exception {
		// move up north to test latlon flattening
		Envelope googleEnvelope = new Envelope(10000, 13000, 6005000, 6008000);
		// back-transform envelope to latlon
		Crs google = geoService.getCrs2("EPSG:900913");
		Crs latlon = geoService.getCrs2("EPSG:4326");
		Envelope latlonEnvelope = geoService.transform(JTS.toGeometry(googleEnvelope), google, latlon)
				.getEnvelopeInternal();
		// back-transform scale to latlon
		double latlonScale = MAX_LEVEL_SCALE * googleEnvelope.getWidth() / latlonEnvelope.getWidth();
		// paint with reprojection (affine is fine for now...:-)
		List<RasterTile> tiles = osm.paint(latlon, latlonEnvelope, latlonScale);
		Assert.assertEquals(4, tiles.size());
		Assert.assertEquals("http://a.tile.openstreetmap.org/14/8196/5735.png", tiles.get(0).getUrl());
		Assert.assertEquals("http://a.tile.openstreetmap.org/14/8196/5736.png", tiles.get(1).getUrl());
		Assert.assertEquals("http://a.tile.openstreetmap.org/14/8197/5735.png", tiles.get(2).getUrl());
		Assert.assertEquals("http://a.tile.openstreetmap.org/14/8197/5736.png", tiles.get(3).getUrl());
		// test first tile
		double width = tiles.get(0).getBounds().getWidth();
		double height = tiles.get(0).getBounds().getHeight();
		double x = tiles.get(0).getBounds().getX();
		double y = tiles.get(0).getBounds().getY();
		Assert.assertEquals(245, width, DELTA);
		Assert.assertEquals(166, height, DELTA);
		Assert.assertEquals(978, x, DELTA);
		Assert.assertEquals(-527802, y, DELTA);
		// test alignment on grid
		for (int i = 0; i <= 1; i++) {
			for (int j = 0; j <= 1; j++) {
				Assert.assertEquals(x + i * width, tiles.get(2 * i + j).getBounds().getX(), DELTA);
				Assert.assertEquals(y + j * height, tiles.get(2 * i + j).getBounds().getY(), DELTA);
				Assert.assertEquals(width, tiles.get(2 * i + j).getBounds().getWidth(), DELTA);
				Assert.assertEquals(height, tiles.get(2 * i + j).getBounds().getHeight(), DELTA);
			}
		}
	}

}
