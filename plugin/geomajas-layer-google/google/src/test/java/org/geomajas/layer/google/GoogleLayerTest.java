package org.geomajas.layer.google;

import java.util.List;

import junit.framework.Assert;

import org.geomajas.layer.osm.TiledRasterLayerService;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.service.GeoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Envelope;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/googleContext.xml"})
public class GoogleLayerTest {

	private static final double ZOOMED_IN_SCALE = .0001;
	private static final double MAX_LEVEL_SCALE = .1;
	private static final double DELTA = 1e-10;

	@Autowired
	@Qualifier("googleSingle")
	private GoogleLayer google;

	@Autowired
	@Qualifier("satellite")
	private GoogleLayer satellite;

	@Autowired
	@Qualifier("physical")
	private GoogleLayer terrain;

	@Autowired
	@Qualifier("googleWrongCrs")
	private GoogleLayer googleWrongCrs;

	@Autowired
	@Qualifier("googleMaxLevel")
	private GoogleLayer googleMaxLevel;

	@Autowired
	@Qualifier("googleStrategy")
	private GoogleLayer googleStrategy;

	@Autowired
	private GeoService geoService;

	@Test
	public void testPaintOutOfBounds() throws Exception {
		double equator = TiledRasterLayerService.EQUATOR_IN_METERS;
		List<RasterTile> tiles = google.paint(google.getCrs(),
				new Envelope(-equator, equator, -equator, equator), 256 / equator);
		Assert.assertEquals(1, tiles.size());
		Assert.assertEquals("http://mt0.google.com/vt?v=w2.95&x=0&y=0&z=0", tiles.iterator().next().getUrl());
	}

	@Test
	public void testPaintToTheSide() throws Exception {
		double equator = TiledRasterLayerService.EQUATOR_IN_METERS;
		List<RasterTile> tiles = google.paint(google.getCrs(),
				new Envelope(equator * 2 / 3, (equator * 2 / 3) + 100, 0, 100), ZOOMED_IN_SCALE);
		Assert.assertEquals(0, tiles.size());
	}

	@Test
	public void testWrongCrsCorrected() throws Exception {
		CoordinateReferenceSystem crs = googleWrongCrs.getCrs();
		Assert.assertEquals(900913, geoService.getSridFromCrs(crs));
		Assert.assertEquals("EPSG:900913", googleWrongCrs.getLayerInfo().getCrs());
	}

	@Test
	public void testMaxLevel() throws Exception {
		Envelope envelope = new Envelope(10000, 10002, 5000, 5002);
		RasterTile tile;
		List<RasterTile> tiles;
		tiles = google.paint(google.getCrs(), envelope, MAX_LEVEL_SCALE);
		Assert.assertEquals(1, tiles.size());
		tile = tiles.get(0);
		Assert.assertEquals("http://mt0.google.com/vt?v=w2.95&x=8196&y=8189&z=14", tile.getUrl());
		tiles = googleMaxLevel.paint(googleMaxLevel.getCrs(), envelope, MAX_LEVEL_SCALE);
		Assert.assertEquals(1, tiles.size());
		tile = tiles.get(0);
		Assert.assertEquals("http://mt0.google.com/vt?v=w2.95&x=2049&y=2047&z=12", tile.getUrl());
	}

	@Test
	public void testConfigUrlStrategy() throws Exception {
		Envelope envelope = new Envelope(10000, 10002, 5000, 5002);
		RasterTile tile;
		List<RasterTile> tiles;
		tiles = googleStrategy.paint(googleStrategy.getCrs(), envelope, MAX_LEVEL_SCALE);
		Assert.assertEquals(1, tiles.size());
		tile = tiles.get(0);
		Assert.assertEquals("http://mt3.google.com/vt?v=w2.95&x=8196&y=8189&z=14", tile.getUrl());
		tiles = googleStrategy.paint(googleStrategy.getCrs(), envelope, MAX_LEVEL_SCALE);
		Assert.assertEquals(1, tiles.size());
		tile = tiles.get(0);
		Assert.assertEquals("http://mt3.google.com/vt?v=w2.95&x=8196&y=8189&z=14", tile.getUrl());
	}

	@Test
	public void testNormal() throws Exception {
		List<RasterTile> tiles = google.paint(google.getCrs(),
				new Envelope(10000, 10010, 4000, 4010), ZOOMED_IN_SCALE);
		Assert.assertEquals(1, tiles.size());
		RasterTile tile = tiles.get(0);
		Assert.assertEquals("http://mt0.google.com/vt?v=w2.95&x=8&y=7&z=4", tile.getUrl());
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
		List<RasterTile> tiles = google.paint(google.getCrs(), envelope, MAX_LEVEL_SCALE);
		Assert.assertEquals(4, tiles.size());
		Assert.assertEquals("http://mt0.google.com/vt?v=w2.95&x=8196&y=8188&z=14", tiles.get(0).getUrl());
		Assert.assertEquals("http://mt0.google.com/vt?v=w2.95&x=8196&y=8189&z=14", tiles.get(1).getUrl());
		Assert.assertEquals("http://mt0.google.com/vt?v=w2.95&x=8197&y=8188&z=14", tiles.get(2).getUrl());
		Assert.assertEquals("http://mt0.google.com/vt?v=w2.95&x=8197&y=8189&z=14", tiles.get(3).getUrl());
		RasterTile tile = tiles.get(3);
		Assert.assertEquals("http://mt0.google.com/vt?v=w2.95&x=8197&y=8189&z=14", tile.getUrl());
		Assert.assertEquals(14, tile.getCode().getTileLevel());
		Assert.assertEquals(8197, tile.getCode().getX());
		Assert.assertEquals(8189, tile.getCode().getY());
		Assert.assertEquals(1223, tile.getBounds().getX(), DELTA);
		Assert.assertEquals(-733.0, tile.getBounds().getY(), DELTA);
		Assert.assertEquals(245.0, tile.getBounds().getHeight(), DELTA);
		Assert.assertEquals(245.0, tile.getBounds().getWidth(), DELTA);
	}

	@Test
	public void testSatellite() throws Exception {
		List<RasterTile> tiles = satellite.paint(geoService.getCrs("EPSG:900913"),
				new Envelope(10000, 10010, 4000, 4010), ZOOMED_IN_SCALE);
		Assert.assertEquals(1, tiles.size());
		RasterTile tile = tiles.get(0);
		Assert.assertEquals("http://khm0.google.com/kh?v=57&x=8&y=7&z=4", tile.getUrl());
		Assert.assertEquals(4, tile.getCode().getTileLevel());
		Assert.assertEquals(8, tile.getCode().getX());
		Assert.assertEquals(7, tile.getCode().getY());
		Assert.assertEquals(0.0, tile.getBounds().getX(), DELTA);
		Assert.assertEquals(-250.0, tile.getBounds().getY(), DELTA);
		Assert.assertEquals(250.0, tile.getBounds().getHeight(), DELTA);
		Assert.assertEquals(250.0, tile.getBounds().getWidth(), DELTA);
	}

	@Test
	public void testTerrain() throws Exception {
		List<RasterTile> tiles = terrain.paint(geoService.getCrs("EPSG:900913"),
				new Envelope(10000, 10010, 4000, 4010), ZOOMED_IN_SCALE);
		Assert.assertEquals(1, tiles.size());
		RasterTile tile = tiles.get(0);
		Assert.assertEquals("http://mt0.google.com/vt?lyrs=t@125,r@128&x=8&y=7&z=4", tile.getUrl());
		Assert.assertEquals(4, tile.getCode().getTileLevel());
		Assert.assertEquals(8, tile.getCode().getX());
		Assert.assertEquals(7, tile.getCode().getY());
		Assert.assertEquals(0.0, tile.getBounds().getX(), DELTA);
		Assert.assertEquals(-250.0, tile.getBounds().getY(), DELTA);
		Assert.assertEquals(250.0, tile.getBounds().getHeight(), DELTA);
		Assert.assertEquals(250.0, tile.getBounds().getWidth(), DELTA);
	}

}
