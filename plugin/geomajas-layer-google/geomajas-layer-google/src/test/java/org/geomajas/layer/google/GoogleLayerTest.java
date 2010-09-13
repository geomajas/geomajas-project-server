package org.geomajas.layer.google;

import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.RasterLayerService;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.service.GeoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Envelope;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/layer/google/googleContext.xml" })
public class GoogleLayerTest {

	@Autowired
	private Map<String, GoogleLayer> layers;

	@Autowired
	private GeoService geoService;

	@Test
	public void testContextOk() {
		Assert.assertNotNull(layers);
		Assert.assertEquals(1, layers.size());
	}

	@Test
	public void testPaintOutOfBounds() throws Exception {
		double equator = GoogleLayer.EQUATOR_IN_METERS;
		List<RasterTile> tiles = layers.get("google").paint(geoService.getCrs("EPSG:900913"),
				new Envelope(-equator, equator, -equator, equator), 256 / equator);
		Assert.assertEquals(1, tiles.size());
		Assert.assertEquals("http://mt0.google.com/vt?v=w2.95&x=0&y=0&z=0", tiles.iterator().next().getUrl());
	}
}
