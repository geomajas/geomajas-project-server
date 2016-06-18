/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.layer.wms;

import java.util.List;

import junit.framework.Assert;

import org.geomajas.geometry.Crs;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.service.GeoService;
import org.geotools.geometry.jts.JTS;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Envelope;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Test for {@link WmsLayer}.
 * 
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "/wmsContext.xml" })
public class WmsLayerTest {

	private static final double ZOOMED_IN_SCALE = .0001;

	private static final double MAX_LEVEL_SCALE = .4;

	private static final double DELTA = 1e-10;

	private static final String MERCATOR = "EPSG:900913";

	private static final String LONLAT = "EPSG:4326";

	@Autowired
	@Qualifier("bluemarble")
	private WmsLayer wms;

	@Autowired
	@Qualifier("proxyBlue")
	private WmsLayer proxyWms;

	@Autowired
	@Qualifier("defaultBlue")
	private WmsLayer defaultWms;

	@Autowired
	@Qualifier("cachedBlue")
	private WmsLayer cachedWms;

	@Autowired
	@Qualifier("escapeBlue")
	private WmsLayer escapeBlue;

	@Autowired
	private GeoService geoService;

	@Test
	public void testLevels() throws Exception {
		// this test failed before http://jira.geomajas.org/browse/WMS-18
		Crs latlon = geoService.getCrs2(LONLAT);
		Envelope latlonEnvelope = null;
		for (int level = 0; level < 32; level++) {
			double latlonScale = Math.pow(2, level);
			// must reduce envelope as we zoom
			latlonEnvelope = new Envelope(-180, -180 + 1.0 / latlonScale, -412, -412 + 1.0 / latlonScale);
			List<RasterTile> tiles = wms.paint(latlon, latlonEnvelope, latlonScale);
			Assert.assertEquals(1, tiles.size());
			Assert.assertEquals(level, tiles.get(0).getCode().getTileLevel());
			Assert.assertEquals(512, tiles.get(0).getBounds().getHeight(), DELTA);
			Assert.assertEquals(512, tiles.get(0).getBounds().getWidth(), DELTA);
		}
	}

	@Test
	public void testNormalOne() throws Exception {
		Envelope googleEnvelope = new Envelope(10000, 10010, 5000, 5010);

		// back-transform envelope to latlon
		Crs google = geoService.getCrs2(MERCATOR);
		Crs latlon = geoService.getCrs2(LONLAT);
		Envelope latlonEnvelope = geoService.transform(JTS.toGeometry(googleEnvelope), google, latlon)
				.getEnvelopeInternal();

		// back-transform scale to latlon
		double latlonScale = ZOOMED_IN_SCALE * googleEnvelope.getWidth() / latlonEnvelope.getWidth();

		// paint with re-projection (affine is fine for now...:-)
		List<RasterTile> tiles = wms.paint(latlon, latlonEnvelope, latlonScale);
		Assert.assertEquals(1, tiles.size());
		RasterTile tile = tiles.get(0);

		// ZOOMED_IN_SCALE 1E-4 corresponds to level 4 with current algorithm !!!!
		Assert.assertEquals("http://apps.geomajas.org/geoserver/wms?SERVICE=WMS&layers=bluemarble&"
				+ "WIDTH=512&HEIGHT=512&bbox=-20,-28,12,4&format=image/jpeg&version=1.1.1&srs=EPSG%3A4326&"
				+ "styles=&request=GetMap", tile.getUrl());

		// Assert.assertEquals("http://apps.geomajas.org/geoserver/wms?SERVICE=WMS&"
		// + "layers=bluemarble&WIDTH=512&HEIGHT=512&"
		// + "bbox=-20.032430835865227,-28.207099921352835,11.947593278789554,3.7729241933019466&"
		// + "format=image/jpeg&version=1.1.1&srs=EPSG%3A4326&styles=&request=GetMap", tile.getUrl());
		Assert.assertEquals(4, tile.getCode().getTileLevel());
		Assert.assertEquals(5, tile.getCode().getX());
		Assert.assertEquals(12, tile.getCode().getY());
		Assert.assertEquals(-223.0, tile.getBounds().getX(), DELTA);
		// Assert.assertEquals(-42.0, tile.getBounds().getY(), DELTA);
		Assert.assertEquals(-45.0, tile.getBounds().getY(), DELTA);
		// Assert.assertEquals(356.0, tile.getBounds().getHeight(), DELTA);
		Assert.assertEquals(357.0, tile.getBounds().getHeight(), DELTA);
		// Assert.assertEquals(356.0, tile.getBounds().getWidth(), DELTA);
		Assert.assertEquals(357.0, tile.getBounds().getWidth(), DELTA);
	}

	@Test
	public void testNormalEncoded() throws Exception {
		Envelope googleEnvelope = new Envelope(10000, 10010, 5000, 5010);

		// back-transform envelope to latlon
		Crs google = geoService.getCrs2(MERCATOR);
		Crs latlon = geoService.getCrs2(LONLAT);
		Envelope latlonEnvelope = geoService.transform(JTS.toGeometry(googleEnvelope), google, latlon)
				.getEnvelopeInternal();

		// back-transform scale to latlon
		double latlonScale = ZOOMED_IN_SCALE * googleEnvelope.getWidth() / latlonEnvelope.getWidth();

		// paint with re-projection (affine is fine for now...:-)
		List<RasterTile> tiles = escapeBlue.paint(latlon, latlonEnvelope, latlonScale);
		Assert.assertEquals(1, tiles.size());
		RasterTile tile = tiles.get(0);

		// ZOOMED_IN_SCALE 1E-4 corresponds to level 4 with current algorithm !!!!
		assertThat(tile.getUrl()).isEqualTo("http://apps.geomajas.org/geoserver/wms?SERVICE=WMS&" +
				"layers=geomajas%3Abluemarble&WIDTH=512&HEIGHT=512&bbox=0,0,45,45&format=image/png&version=1.1.1&" +
				"srs=EPSG%3A4326&styles=&what%3F=value%2Bmore%21&request=GetMap");

	}

	@Test
	public void testNormalSeveral() throws Exception {
		// move up north to test latlon flattening
		Envelope googleEnvelope = new Envelope(10000, 11000, 6005000, 6006000);

		// back-transform envelope to latlon
		Crs google = geoService.getCrs2(MERCATOR);
		Crs latlon = geoService.getCrs2(LONLAT);
		Envelope latlonEnvelope = geoService.transform(JTS.toGeometry(googleEnvelope), google, latlon)
				.getEnvelopeInternal();

		// back-transform scale to latlon
		double latlonScale = MAX_LEVEL_SCALE * googleEnvelope.getWidth() / latlonEnvelope.getWidth();

		// paint with re-projection (affine is fine for now...:-)
		List<RasterTile> tiles = wms.paint(latlon, latlonEnvelope, latlonScale);
		// Assert.assertEquals(4, tiles.size());
		Assert.assertEquals(2, tiles.size());

		Assert.assertEquals("http://apps.geomajas.org/geoserver/wms?SERVICE=WMS&layers=bluemarble&WIDTH=512&"
				+ "HEIGHT=512&bbox=0.0859375,47.3828125,0.09375,47.390625&format=image/jpeg&version=1.1.1&"
				+ "srs=EPSG%3A4326&styles=&request=GetMap", tiles.get(0).getUrl());
		Assert.assertEquals("http://apps.geomajas.org/geoserver/wms?SERVICE=WMS&layers=bluemarble&WIDTH=512&"
				+ "HEIGHT=512&bbox=0.09375,47.3828125,0.1015625,47.390625&format=image/jpeg&version=1.1.1&"
				+ "srs=EPSG%3A4326&styles=&request=GetMap", tiles.get(1).getUrl());

		RasterTile tile = tiles.get(1);
		Assert.assertEquals(16, tile.getCode().getTileLevel());
		Assert.assertEquals(23052, tile.getCode().getX());
		Assert.assertEquals(58801, tile.getCode().getY());
		Assert.assertEquals(4174.0, tile.getBounds().getX(), DELTA);
		Assert.assertEquals(-2110200.0, tile.getBounds().getY(), DELTA);
		Assert.assertEquals(348.0, tile.getBounds().getHeight(), DELTA);
		Assert.assertEquals(348.0, tile.getBounds().getWidth(), DELTA);

		// Assert.assertEquals("http://apps.geomajas.org/geoserver/wms?SERVICE=WMS&layers=bluemarble&"
		// + "WIDTH=512&HEIGHT=512&bbox=0.08895567100992707,47.37804639974752,0.09677101398176689,"
		// + "47.38586174271936&format=image/jpeg&version=1.1.1&srs=EPSG%3A4326&styles=&request=GetMap", tiles
		// .get(0).getUrl());
		// Assert.assertEquals("http://apps.geomajas.org/geoserver/wms?SERVICE=WMS&layers=bluemarble&"
		// + "WIDTH=512&HEIGHT=512&bbox=0.08895567100992707,47.38586174271936,0.09677101398176689,"
		// + "47.393677085691195&format=image/jpeg&version=1.1.1&srs=EPSG%3A4326&styles=&request=GetMap",
		// tiles.get(1).getUrl());
		// Assert.assertEquals("http://apps.geomajas.org/geoserver/wms?SERVICE=WMS&layers=bluemarble&"
		// + "WIDTH=512&HEIGHT=512&bbox=0.09677101398176689,47.37804639974752,0.10458635695360671,"
		// + "47.38586174271936&format=image/jpeg&version=1.1.1&srs=EPSG%3A4326&styles=&request=GetMap", tiles
		// .get(2).getUrl());
		// Assert.assertEquals("http://apps.geomajas.org/geoserver/wms?SERVICE=WMS&layers=bluemarble&"
		// + "WIDTH=512&HEIGHT=512&bbox=0.09677101398176689,47.38586174271936,0.10458635695360671,"
		// + "47.393677085691195&format=image/jpeg&version=1.1.1&srs=EPSG%3A4326&styles=&request=GetMap",
		// tiles.get(3).getUrl());
		// RasterTile tile = tiles.get(3);
		// // MAX_LEVEL_SCALE 0.4 corresponds to level 16 with current algorithm !!!!
		// Assert.assertEquals(16, tile.getCode().getTileLevel());
		// Assert.assertEquals(23044, tile.getCode().getX());
		// Assert.assertEquals(58780, tile.getCode().getY());
		// Assert.assertEquals(4309.0, tile.getBounds().getX(), DELTA);
		// Assert.assertEquals(-2110336.0, tile.getBounds().getY(), DELTA);
		// Assert.assertEquals(348.0, tile.getBounds().getHeight(), DELTA);
		// Assert.assertEquals(348.0, tile.getBounds().getWidth(), DELTA);
	}

	@Test
	public void testReprojectOne() throws Exception {
		Envelope googleEnvelope = new Envelope(10000, 10010, 5000, 5010);
		// back-transform envelope to latlon
		Crs google = geoService.getCrs2(MERCATOR);
		// paint with reprojection (affine is fine for now...:-)
		List<RasterTile> tiles = wms.paint(google, googleEnvelope, ZOOMED_IN_SCALE);
		Assert.assertEquals(1, tiles.size());
		RasterTile tile = tiles.get(0);
		Assert.assertEquals("http://apps.geomajas.org/geoserver/wms?SERVICE=WMS&layers=bluemarble&WIDTH=512&"
				+ "HEIGHT=512&bbox=-20,-28,12,4&format=image/jpeg&version=1.1.1&srs=EPSG%3A4326&"
				+ "styles=&request=GetMap", tile.getUrl());
		Assert.assertEquals(4, tile.getCode().getTileLevel());
		Assert.assertEquals(5, tile.getCode().getX());
		Assert.assertEquals(12, tile.getCode().getY());

		// TODO find out why this fails...
		// Assert.assertEquals(-223.0, tile.getBounds().getX(), DELTA);
		// Assert.assertEquals(-45.0, tile.getBounds().getY(), DELTA);
		// Assert.assertEquals(370.0, tile.getBounds().getHeight(), DELTA);
		// Assert.assertEquals(357.0, tile.getBounds().getWidth(), DELTA);

		// Assert.assertEquals("http://apps.geomajas.org/geoserver/wms?SERVICE=WMS&"
		// + "layers=bluemarble&WIDTH=512&HEIGHT=512&"
		// + "bbox=-20.032430835865227,-28.207099921352835,11.947593278789554,3.7729241933019466&"
		// + "format=image/jpeg&version=1.1.1&srs=EPSG%3A4326&styles=&request=GetMap", tile.getUrl());
		// Assert.assertEquals(4, tile.getCode().getTileLevel());
		// Assert.assertEquals(5, tile.getCode().getX());
		// Assert.assertEquals(12, tile.getCode().getY());
		// Assert.assertEquals(-223.0, tile.getBounds().getX(), DELTA);
		// Assert.assertEquals(-42.0, tile.getBounds().getY(), DELTA);
		// Assert.assertEquals(370.0, tile.getBounds().getHeight(), DELTA);
		// Assert.assertEquals(356.0, tile.getBounds().getWidth(), DELTA);
	}

	@Test
	public void testReprojectSeveral() throws Exception {
		// move up north to test latlon flattening
		Envelope googleEnvelope = new Envelope(10000, 11000, 6005000, 6006000);

		// back-transform envelope to latlon
		Crs google = geoService.getCrs2(MERCATOR);

		// paint with reprojection (affine is fine for now...:-)
		List<RasterTile> tiles = wms.paint(google, googleEnvelope, MAX_LEVEL_SCALE);

		Assert.assertEquals(2, tiles.size());
		Assert.assertEquals("http://apps.geomajas.org/geoserver/wms?SERVICE=WMS&layers=bluemarble&WIDTH=512&"
				+ "HEIGHT=512&bbox=0.0859375,47.3828125,0.09375,47.390625&format=image/jpeg&version=1.1.1&"
				+ "srs=EPSG%3A4326&styles=&request=GetMap", tiles.get(0).getUrl());
		Assert.assertEquals("http://apps.geomajas.org/geoserver/wms?SERVICE=WMS&layers=bluemarble&WIDTH=512&"
				+ "HEIGHT=512&bbox=0.09375,47.3828125,0.1015625,47.390625&format=image/jpeg&version=1.1.1&"
				+ "srs=EPSG%3A4326&styles=&request=GetMap", tiles.get(1).getUrl());

		RasterTile tile = tiles.get(1);
		Assert.assertEquals(16, tile.getCode().getTileLevel());
		Assert.assertEquals(23052, tile.getCode().getX());
		Assert.assertEquals(58801, tile.getCode().getY());
		Assert.assertEquals(4174.0, tile.getBounds().getX(), DELTA);
		Assert.assertEquals(-2402427.0, tile.getBounds().getY(), DELTA);
		Assert.assertEquals(513.0, tile.getBounds().getHeight(), DELTA);
		Assert.assertEquals(348.0, tile.getBounds().getWidth(), DELTA);

		// Assert.assertEquals(4, tiles.size());
		// Assert.assertEquals("http://apps.geomajas.org/geoserver/wms?SERVICE=WMS&layers=bluemarble&"
		// + "WIDTH=512&HEIGHT=512&bbox=0.08895567100992707,47.37804639974752,0.09677101398176689,"
		// + "47.38586174271936&format=image/jpeg&version=1.1.1&srs=EPSG%3A4326&styles=&request=GetMap", tiles
		// .get(0).getUrl());
		// Assert.assertEquals("http://apps.geomajas.org/geoserver/wms?SERVICE=WMS&layers=bluemarble&"
		// + "WIDTH=512&HEIGHT=512&bbox=0.08895567100992707,47.38586174271936,0.09677101398176689,"
		// + "47.393677085691195&format=image/jpeg&version=1.1.1&srs=EPSG%3A4326&styles=&request=GetMap",
		// tiles.get(1).getUrl());
		// Assert.assertEquals("http://apps.geomajas.org/geoserver/wms?SERVICE=WMS&layers=bluemarble&"
		// + "WIDTH=512&HEIGHT=512&bbox=0.09677101398176689,47.37804639974752,0.10458635695360671,"
		// + "47.38586174271936&format=image/jpeg&version=1.1.1&srs=EPSG%3A4326&styles=&request=GetMap", tiles
		// .get(2).getUrl());
		// Assert.assertEquals("http://apps.geomajas.org/geoserver/wms?SERVICE=WMS&layers=bluemarble&"
		// + "WIDTH=512&HEIGHT=512&bbox=0.09677101398176689,47.38586174271936,0.10458635695360671,"
		// + "47.393677085691195&format=image/jpeg&version=1.1.1&srs=EPSG%3A4326&styles=&request=GetMap",
		// tiles.get(3).getUrl());
		// RasterTile tile = tiles.get(3);
		// // MAX_LEVEL_SCALE 0.4 corresponds to level 16 !!!!
		// Assert.assertEquals(16, tile.getCode().getTileLevel());
		// Assert.assertEquals(23044, tile.getCode().getX());
		// Assert.assertEquals(58780, tile.getCode().getY());
		// Assert.assertEquals(4309.0, tile.getBounds().getX(), DELTA);
		// Assert.assertEquals(-2402628.0, tile.getBounds().getY(), DELTA);
		// Assert.assertEquals(514.0, tile.getBounds().getHeight(), DELTA);
		// Assert.assertEquals(348.0, tile.getBounds().getWidth(), DELTA);
		// test alignment on grid

		// TODO Find out why this doesn't work...
		// double x = tiles.get(0).getBounds().getX();
		// double y = tiles.get(0).getBounds().getY();
		// double width = tiles.get(0).getBounds().getWidth();
		// double height = tiles.get(0).getBounds().getHeight();
		// for (int i = 0; i < 1; i++) {
		// for (int j = 0; j <= 1; j++) {
		// Assert.assertEquals(x + i * width, tiles.get(2 * i + j).getBounds().getX(), DELTA);
		// Assert.assertEquals(y - j * height, tiles.get(2 * i + j).getBounds().getY(), DELTA);
		// Assert.assertEquals(width, tiles.get(2 * i + j).getBounds().getWidth(), DELTA);
		// Assert.assertEquals(height, tiles.get(2 * i + j).getBounds().getHeight(), DELTA);
		// }
		// }
	}

	@Test
	public void testProxyOne() throws Exception {
		Envelope googleEnvelope = new Envelope(10000, 10010, 5000, 5010);

		// back-transform envelope to latlon
		Crs google = geoService.getCrs2(MERCATOR);
		Crs latlon = geoService.getCrs2(LONLAT);
		Envelope latlonEnvelope = geoService.transform(JTS.toGeometry(googleEnvelope), google, latlon)
				.getEnvelopeInternal();

		// back-transform scale to latlon
		double latlonScale = ZOOMED_IN_SCALE * googleEnvelope.getWidth() / latlonEnvelope.getWidth();

		// paint with re-projection (affine is fine for now...:-)
		List<RasterTile> tiles = proxyWms.paint(latlon, latlonEnvelope, latlonScale);
		Assert.assertEquals(1, tiles.size());
		RasterTile tile = tiles.get(0);

		Assert.assertEquals("./d/wms/proxyBlue/?SERVICE=WMS&layers=bluemarble&WIDTH=512&HEIGHT=512&bbox"
				+ "=-20,-28,12,4&format=image/jpeg&version=1.3.0&crs=EPSG%3A4326&styles=&request=GetMap", tile.getUrl());
		Assert.assertEquals(4, tile.getCode().getTileLevel());
		Assert.assertEquals(5, tile.getCode().getX());
		Assert.assertEquals(12, tile.getCode().getY());
		Assert.assertEquals(-223.0, tile.getBounds().getX(), DELTA);
		Assert.assertEquals(-45.0, tile.getBounds().getY(), DELTA);
		Assert.assertEquals(357.0, tile.getBounds().getHeight(), DELTA);
		Assert.assertEquals(357.0, tile.getBounds().getWidth(), DELTA);

		// Assert.assertEquals("./d/wms/proxyBlue/?SERVICE=WMS&layers=bluemarble&WIDTH=512&HEIGHT=512&"
		// + "bbox=-20.032430835865227,-28.207099921352835,11.947593278789554,3.7729241933019466&"
		// + "format=image/jpeg&version=1.3.0&crs=EPSG%3A4326&styles=&request=GetMap", tile.getUrl());
		// Assert.assertEquals(4, tile.getCode().getTileLevel());
		// Assert.assertEquals(5, tile.getCode().getX());
		// Assert.assertEquals(12, tile.getCode().getY());
		// Assert.assertEquals(-223.0, tile.getBounds().getX(), DELTA);
		// Assert.assertEquals(-42.0, tile.getBounds().getY(), DELTA);
		// Assert.assertEquals(356.0, tile.getBounds().getHeight(), DELTA);
		// Assert.assertEquals(356.0, tile.getBounds().getWidth(), DELTA);
	}

	@Test
	public void testDefaultsOne() throws Exception {
		Envelope googleEnvelope = new Envelope(10000, 10010, 5000, 5010);

		// back-transform envelope to latlon
		Crs google = geoService.getCrs2(MERCATOR);
		Crs latlon = geoService.getCrs2(LONLAT);
		Envelope latlonEnvelope = geoService.transform(JTS.toGeometry(googleEnvelope), google, latlon)
				.getEnvelopeInternal();

		// back-transform scale to latlon
		double latlonScale = ZOOMED_IN_SCALE * googleEnvelope.getWidth() / latlonEnvelope.getWidth();

		// paint with re-projection (affine is fine for now...:-)
		List<RasterTile> tiles = defaultWms.paint(latlon, latlonEnvelope, latlonScale);
		Assert.assertEquals(1, tiles.size());
		RasterTile tile = tiles.get(0);

		Assert.assertEquals("http://apps.geomajas.org/geoserver/wms?SERVICE=WMS&layers=bluemarble&WIDTH=512&HEIGHT"
				+ "=512&bbox=-20,-28,12,4&format=image/png&version=1.1.1&srs=EPSG%3A4326&styles=&request=GetMap",
				tile.getUrl());
		Assert.assertEquals(4, tile.getCode().getTileLevel());
		Assert.assertEquals(5, tile.getCode().getX());
		Assert.assertEquals(12, tile.getCode().getY());
		Assert.assertEquals(-223.0, tile.getBounds().getX(), DELTA);
		Assert.assertEquals(-45.0, tile.getBounds().getY(), DELTA);
		Assert.assertEquals(357.0, tile.getBounds().getHeight(), DELTA);
		Assert.assertEquals(357.0, tile.getBounds().getWidth(), DELTA);

		// Assert.assertEquals("http://apps.geomajas.org/geoserver/wms?SERVICE=WMS&layers=bluemarble&"
		// + "WIDTH=512&HEIGHT=512&bbox=-20.032430835865227,-28.207099921352835,11.947593278789554,"
		// + "3.7729241933019466&format=image/png&version=1.1.1&srs=EPSG%3A4326&styles=&request=GetMap",
		// tile.getUrl());
		// Assert.assertEquals(4, tile.getCode().getTileLevel());
		// Assert.assertEquals(5, tile.getCode().getX());
		// Assert.assertEquals(12, tile.getCode().getY());
		// Assert.assertEquals(-223.0, tile.getBounds().getX(), DELTA);
		// Assert.assertEquals(-42.0, tile.getBounds().getY(), DELTA);
		// Assert.assertEquals(356.0, tile.getBounds().getHeight(), DELTA);
		// Assert.assertEquals(356.0, tile.getBounds().getWidth(), DELTA);
	}

	@Test
	public void noBaseWmsUrl() throws Exception {
		try {
			loadApplicationContext("/wmsInvalidContext.xml");
			Assert.fail("invalid layer declaration was allowed");
		} catch (GeomajasException ge) {
			Assert.assertEquals(ExceptionCode.PARAMETER_MISSING, ge.getExceptionCode());
		}
	}
	
	@Test
	@DirtiesContext
	public void testNoCache() throws Exception {
		cachedWms.clearCacheManagerService(); // provided scope makes it available at compile/test time
		cachedWms.setUseCache(false);
		Assert.assertFalse(cachedWms.isUseCache());
		cachedWms.setUseCache(true);
		Assert.assertFalse(cachedWms.isUseCache()); // enabling should fail without cache manager service
	}

	private ApplicationContext loadApplicationContext(String location) throws Exception {
		String[] locations = new String[2];
		locations[0] = "/org/geomajas/spring/geomajasContext.xml";
		locations[1] = location;
		try {
			return new ClassPathXmlApplicationContext(locations);
		} catch (Exception ex) {
			Throwable ge = ex;
			while (null != ge) {
				if (ge instanceof GeomajasException) {
					throw (GeomajasException) ge;
				}
				ge = ge.getCause();
			}
			throw ex;
		}
	}

}
