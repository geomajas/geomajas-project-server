package org.geomajas.layer.google;

import java.util.List;

import junit.framework.Assert;

import org.geomajas.layer.tile.RasterTile;
import org.geomajas.service.GeoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Envelope;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/layer/google/googleContext.xml" })
public class GoogleLayerTest {

	private static final double ZOOMED_IN_SCALE = .0001;
	private static final double DELTA = 1e-10;

	@Autowired
	@Qualifier("google")
	private GoogleLayer google;

	@Autowired
	@Qualifier("satellite")
	private GoogleLayer satellite;

	@Autowired
	@Qualifier("terrain")
	private GoogleLayer terrain;

	@Autowired
	private GeoService geoService;

	@Test
	public void testPaintOutOfBounds() throws Exception {
		double equator = GoogleLayer.EQUATOR_IN_METERS;
		List<RasterTile> tiles = google.paint(geoService.getCrs("EPSG:900913"),
				new Envelope(-equator, equator, -equator, equator), 256 / equator);
		Assert.assertEquals(1, tiles.size());
		Assert.assertEquals("http://mt0.google.com/vt?v=w2.95&x=0&y=0&z=0", tiles.iterator().next().getUrl());
	}

	@Test
	public void testPaintToTheSide() throws Exception {
		double equator = GoogleLayer.EQUATOR_IN_METERS;
		List<RasterTile> tiles = google.paint(geoService.getCrs("EPSG:900913"),
				new Envelope(equator * 2 / 3, (equator * 2 / 3) + 100, 0, 100), ZOOMED_IN_SCALE);
		Assert.assertEquals(0, tiles.size());
	}

	/*
	@Test
	public void testNormal() throws Exception {
		List<RasterTile> tiles = google.paint(geoService.getCrs("EPSG:900913"),
				new Envelope(10000, 10010, 4000, 4010), ZOOMED_IN_SCALE);
		Assert.assertEquals(1, tiles.size());
		RasterTile tile = tiles.get(0);
		Assert.assertEquals("http://mt0.google.com/vt?v=w2.95&x=0&y=0&z=0", tile.getUrl());
		Assert.assertEquals(4, tile.getCode().getTileLevel());
		Assert.assertEquals(8, tile.getCode().getX());
		Assert.assertEquals(7, tile.getCode().getY());
		Assert.assertEquals(0.0, tile.getBounds().getX(), DELTA);
		Assert.assertEquals(-256.0, tile.getBounds().getY(), DELTA);
		Assert.assertEquals(256.0, tile.getBounds().getHeight(), DELTA);
		Assert.assertEquals(256.0, tile.getBounds().getWidth(), DELTA);
	}

	@Test
	public void testSatellite() throws Exception {
		List<RasterTile> tiles = satellite.paint(geoService.getCrs("EPSG:900913"),
				new Envelope(10000, 10010, 4000, 4010), ZOOMED_IN_SCALE);
		Assert.assertEquals(1, tiles.size());
		RasterTile tile = tiles.get(0);
		Assert.assertEquals("http://mt0.google.com/vt?v=w2.95&x=0&y=0&z=0", tile.getUrl());
		Assert.assertEquals(4, tile.getCode().getTileLevel());
		Assert.assertEquals(8, tile.getCode().getX());
		Assert.assertEquals(7, tile.getCode().getY());
		Assert.assertEquals(0.0, tile.getBounds().getX(), DELTA);
		Assert.assertEquals(-256.0, tile.getBounds().getY(), DELTA);
		Assert.assertEquals(256.0, tile.getBounds().getHeight(), DELTA);
		Assert.assertEquals(256.0, tile.getBounds().getWidth(), DELTA);
	}

	@Test
	public void testTerrain() throws Exception {
		List<RasterTile> tiles = terrain.paint(geoService.getCrs("EPSG:900913"),
				new Envelope(10000, 10010, 4000, 4010), ZOOMED_IN_SCALE);
		Assert.assertEquals(1, tiles.size());
		RasterTile tile = tiles.get(0);
		Assert.assertEquals("http://mt0.google.com/vt?v=w2.95&x=0&y=0&z=0", tile.getUrl());
		Assert.assertEquals(4, tile.getCode().getTileLevel());
		Assert.assertEquals(8, tile.getCode().getX());
		Assert.assertEquals(7, tile.getCode().getY());
		Assert.assertEquals(0.0, tile.getBounds().getX(), DELTA);
		Assert.assertEquals(-256.0, tile.getBounds().getY(), DELTA);
		Assert.assertEquals(256.0, tile.getBounds().getHeight(), DELTA);
		Assert.assertEquals(256.0, tile.getBounds().getWidth(), DELTA);
	}
	*/

}
