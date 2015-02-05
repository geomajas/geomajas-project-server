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

package org.geomajas.layer.google;

import java.util.List;

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

import com.vividsolutions.jts.geom.Envelope;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/googleContext.xml"})
public class GoogleLayerTest {

	private static final double ZOOMED_IN_SCALE = .0001;
	private static final double MAX_LEVEL_SCALE = .1;
	private static final double DELTA = 1e-10;
	public static final double EQUATOR_IN_METERS = 40075016.686;

	@Autowired
	@Qualifier("googleSingle")
	private GoogleLayer google;

	@Autowired
	@Qualifier("googleDs")
	private GoogleLayer googleDs;

	@Autowired
	@Qualifier("satellite")
	private GoogleLayer satellite;

	@Autowired
	@Qualifier("satelliteDs")
	private GoogleLayer satelliteDs;

	@Autowired
	@Qualifier("physical")
	private GoogleLayer physical;

	@Autowired
	@Qualifier("physicalDs")
	private GoogleLayer physicalDs;

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
		double equator = EQUATOR_IN_METERS;
		List<RasterTile> tiles = google.paint(google.getCrs(),
				new Envelope(-equator, equator, -equator, equator), 256 / equator);
		Assert.assertEquals(1, tiles.size());
		Assert.assertEquals("http://maps.googleapis.com/maps/api/staticmap?center=85.05974795815024,180.10000000378653&zoom=0&size=512x512", tiles.iterator().next().getUrl());
	}

	@Test
	public void testPaintToTheSide() throws Exception {
		double equator = EQUATOR_IN_METERS;
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
		Assert.assertEquals("http://maps.googleapis.com/maps/api/staticmap?center=0.034082031132611657,0.07802734564300369&zoom=14&size=512x512", tile.getUrl());
		tiles = googleMaxLevel.paint(googleMaxLevel.getCrs(), envelope, MAX_LEVEL_SCALE);
		Assert.assertEquals(1, tiles.size());
		tile = tiles.get(0);
		Assert.assertEquals("http://maps.googleapis.com/maps/api/staticmap?center=0.012109376802175874,0.012109376892328565&zoom=12&sensor=false&maptype=roadmap&size=512x512", tile.getUrl());
	}

	@Test
	public void testConfigUrlStrategy() throws Exception {
		Envelope envelope = new Envelope(10000, 10002, 5000, 5002);
		RasterTile tile;
		List<RasterTile> tiles;
		tiles = googleStrategy.paint(googleStrategy.getCrs(), envelope, MAX_LEVEL_SCALE);
		Assert.assertEquals(1, tiles.size());
		tile = tiles.get(0);
		Assert.assertEquals("http://maps.googleapis.com/maps/api/staticmap?center=0.034082031132611657,0.07802734564300369&zoom=14&sensor=false&maptype=roadmap&size=512x512", tile.getUrl());
		tiles = googleStrategy.paint(googleStrategy.getCrs(), envelope, MAX_LEVEL_SCALE);
		Assert.assertEquals(1, tiles.size());
		tile = tiles.get(0);
		Assert.assertEquals("http://maps.googleapis.com/maps/api/staticmap?center=0.034082031132611657,0.07802734564300369&zoom=14&sensor=false&maptype=roadmap&size=512x512", tile.getUrl());
	}

	@Test
	public void testNormal() throws Exception {
		List<RasterTile> tiles = google.paint(google.getCrs(),
				new Envelope(10000, 10010, 4000, 4010), ZOOMED_IN_SCALE);
		Assert.assertEquals(1, tiles.size());
		RasterTile tile = tiles.get(0);
		Assert.assertEquals("http://maps.googleapis.com/maps/api/staticmap?center=-21.850259740996247,-22.399999998343407&zoom=4&size=512x512", tile.getUrl());
		Assert.assertEquals(4, tile.getCode().getTileLevel());
		Assert.assertEquals(3, tile.getCode().getX());
		Assert.assertEquals(3, tile.getCode().getY());
		Assert.assertEquals(-500.0, tile.getBounds().getX(), DELTA);
		Assert.assertEquals(-1, tile.getBounds().getY(), DELTA);
		Assert.assertEquals(501.0, tile.getBounds().getHeight(), DELTA);
		Assert.assertEquals(501.0, tile.getBounds().getWidth(), DELTA);
	}

	@Test
	public void testNormalSeveral() throws Exception {
		Envelope envelope = new Envelope(10000, 13000, 5000, 8000);
		List<RasterTile> tiles = google.paint(google.getCrs(), envelope, MAX_LEVEL_SCALE);
		Assert.assertEquals(4, tiles.size());
		Assert.assertEquals("http://maps.googleapis.com/maps/api/staticmap?center=0.034082031132611657,0.07802734564300369&zoom=14&size=512x512", tiles.get(0).getUrl());
		Assert.assertEquals("http://maps.googleapis.com/maps/api/staticmap?center=0.07802732152486058,0.07802734564300369&zoom=14&size=512x512", tiles.get(1).getUrl());
		Assert.assertEquals("http://maps.googleapis.com/maps/api/staticmap?center=0.034082031132611657,0.12197265814346592&zoom=14&size=512x512", tiles.get(2).getUrl());
		Assert.assertEquals("http://maps.googleapis.com/maps/api/staticmap?center=0.07802732152486058,0.12197265814346592&zoom=14&size=512x512", tiles.get(3).getUrl());
		RasterTile tile = tiles.get(3);
		Assert.assertEquals("http://maps.googleapis.com/maps/api/staticmap?center=0.07802732152486058,0.12197265814346592&zoom=14&size=512x512", tile.getUrl());
		Assert.assertEquals(14, tile.getCode().getTileLevel());
		Assert.assertEquals(4096, tile.getCode().getX());
		Assert.assertEquals(4095, tile.getCode().getY());
		Assert.assertEquals(1113.0, tile.getBounds().getX(), DELTA);
		Assert.assertEquals(-1113.0, tile.getBounds().getY(), DELTA);
		Assert.assertEquals(489.0, tile.getBounds().getHeight(), DELTA);
		Assert.assertEquals(489.0, tile.getBounds().getWidth(), DELTA);
	}

	@Test
	public void testSatellite() throws Exception {
		List<RasterTile> tiles = satellite.paint(geoService.getCrs("EPSG:900913"),
				new Envelope(10000, 10010, 4000, 4010), ZOOMED_IN_SCALE);
		Assert.assertEquals(1, tiles.size());
		RasterTile tile = tiles.get(0);
		Assert.assertEquals("http://maps.googleapis.com/maps/api/staticmap?center=-21.850259740996247,-22.399999998343407&zoom=4&sensor=false&maptype=satellite&size=512x512", tile.getUrl());
		Assert.assertEquals(4, tile.getCode().getTileLevel());
		Assert.assertEquals(3, tile.getCode().getX());
		Assert.assertEquals(3, tile.getCode().getY());
		Assert.assertEquals(-500.0, tile.getBounds().getX(), DELTA);
		Assert.assertEquals(-1.0, tile.getBounds().getY(), DELTA);
		Assert.assertEquals(501.0, tile.getBounds().getHeight(), DELTA);
		Assert.assertEquals(501.0, tile.getBounds().getWidth(), DELTA);
	}

	@Test
	public void testTerrain() throws Exception {
		List<RasterTile> tiles = physical.paint(geoService.getCrs("EPSG:900913"),
				new Envelope(10000, 10010, 4000, 4010), ZOOMED_IN_SCALE);
		Assert.assertEquals(1, tiles.size());
		RasterTile tile = tiles.get(0);
		Assert.assertEquals("http://maps.googleapis.com/maps/api/staticmap?center=-21.850259740996247,-22.399999998343407&zoom=4&sensor=false&maptype=terrain&size=512x512", tile.getUrl());
		Assert.assertEquals(4, tile.getCode().getTileLevel());
		Assert.assertEquals(3, tile.getCode().getX());
		Assert.assertEquals(3, tile.getCode().getY());
		Assert.assertEquals(-500.0, tile.getBounds().getX(), DELTA);
		Assert.assertEquals(-1.0, tile.getBounds().getY(), DELTA);
		Assert.assertEquals(501.0, tile.getBounds().getHeight(), DELTA);
		Assert.assertEquals(501.0, tile.getBounds().getWidth(), DELTA);
	}

	@Test
	public void testNormalDs() throws Exception {
		List<RasterTile> tiles = googleDs.paint(googleDs.getCrs(),
				new Envelope(10000, 10010, 4000, 4010), ZOOMED_IN_SCALE);
		Assert.assertEquals(1, tiles.size());
		RasterTile tile = tiles.get(0);
		if (System.getProperty("java.version").startsWith("1.6")) {
			Assert.assertEquals("http://maps.googleapis.com/maps/api/staticmap?center=16.731982094003893,16.975000002070733&zoom=4&sensor=false&maptype=roadmap&size=640x640", tile.getUrl());
		} else {
			Assert.assertEquals("http://maps.googleapis.com/maps/api/staticmap?center=16.731982094003904,16.975000002070733&zoom=4&sensor=false&maptype=roadmap&size=640x640", tile.getUrl());
		}
		Assert.assertEquals(4, tile.getCode().getTileLevel());
		Assert.assertEquals(3, tile.getCode().getX());
		Assert.assertEquals(3, tile.getCode().getY());
		Assert.assertEquals(-124.0, tile.getBounds().getX(), DELTA);
		Assert.assertEquals(-502.0, tile.getBounds().getY(), DELTA);
		Assert.assertEquals(626.0, tile.getBounds().getHeight(), DELTA);
		Assert.assertEquals(626.0, tile.getBounds().getWidth(), DELTA);
	}

	@Test
	public void testSatelliteDs() throws Exception {
		List<RasterTile> tiles = satelliteDs.paint(satelliteDs.getCrs(),
				new Envelope(10000, 10010, 4000, 4010), ZOOMED_IN_SCALE);
		Assert.assertEquals(1, tiles.size());
		RasterTile tile = tiles.get(0);
		if (System.getProperty("java.version").startsWith("1.6")) {
			Assert.assertEquals("http://maps.googleapis.com/maps/api/staticmap?center=16.731982094003893,16.975000002070733&zoom=4&sensor=false&maptype=satellite&size=640x640", tile.getUrl());
		} else {
			Assert.assertEquals("http://maps.googleapis.com/maps/api/staticmap?center=16.731982094003904,16.975000002070733&zoom=4&sensor=false&maptype=satellite&size=640x640", tile.getUrl());
		}
		Assert.assertEquals(4, tile.getCode().getTileLevel());
		Assert.assertEquals(3, tile.getCode().getX());
		Assert.assertEquals(3, tile.getCode().getY());
		Assert.assertEquals(-124.0, tile.getBounds().getX(), DELTA);
		Assert.assertEquals(-502.0, tile.getBounds().getY(), DELTA);
		Assert.assertEquals(626.0, tile.getBounds().getHeight(), DELTA);
		Assert.assertEquals(626.0, tile.getBounds().getWidth(), DELTA);
	}

	@Test
	public void testTerrainDs() throws Exception {
		List<RasterTile> tiles = physicalDs.paint(physicalDs.getCrs(),
				new Envelope(10000, 10010, 4000, 4010), ZOOMED_IN_SCALE);
		Assert.assertEquals(1, tiles.size());
		RasterTile tile = tiles.get(0);
		if (System.getProperty("java.version").startsWith("1.6")) {
			Assert.assertEquals("http://maps.googleapis.com/maps/api/staticmap?center=16.731982094003893,16.975000002070733&zoom=4&sensor=false&maptype=terrain&size=640x640", tile.getUrl());
		} else {
			Assert.assertEquals("http://maps.googleapis.com/maps/api/staticmap?center=16.731982094003904,16.975000002070733&zoom=4&sensor=false&maptype=terrain&size=640x640", tile.getUrl());
		}
		Assert.assertEquals(4, tile.getCode().getTileLevel());
		Assert.assertEquals(3, tile.getCode().getX());
		Assert.assertEquals(3, tile.getCode().getY());
		Assert.assertEquals(-124.0, tile.getBounds().getX(), DELTA);
		Assert.assertEquals(-502.0, tile.getBounds().getY(), DELTA);
		Assert.assertEquals(626.0, tile.getBounds().getHeight(), DELTA);
		Assert.assertEquals(626.0, tile.getBounds().getWidth(), DELTA);
	}

}
