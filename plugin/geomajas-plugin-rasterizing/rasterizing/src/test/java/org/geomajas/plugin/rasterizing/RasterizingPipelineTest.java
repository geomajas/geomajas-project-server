package org.geomajas.plugin.rasterizing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.geomajas.command.dto.GetVectorTileRequest;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.layer.tile.TileMetadata;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheManagerService;
import org.geomajas.security.SecurityManager;
import org.geomajas.service.TestRecorder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Tests the rasterizing pipeline used by the RasterizingController.
 * 
 * @author Jan De Moerloose
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/rasterizing/rasterizing.xml", "/org/geomajas/spring/testRecorder.xml",
		"/org/geomajas/testdata/beanContext.xml", "/org/geomajas/testdata/layerBeans.xml",
		"/org/geomajas/plugin/rasterizing/DefaultRasterizedPipelines.xml" })
public class RasterizingPipelineTest {

	@Autowired
	private CacheManagerService cacheManager;

	@Autowired
	@Qualifier("beans")
	private VectorLayer layerBeans;

	@Autowired
	@Qualifier("beansStyleInfo")
	private NamedStyleInfo layerBeansStyleInfo;

	@Autowired
	RasterizingController controller;

	@Autowired
	TestRecorder recorder;

	@Autowired
	private SecurityManager securityManager;

	@Autowired
	private VectorLayerService vectorLayerService;

	private static byte[] DUMMY_BYTES = new byte[] { 0, 1, 2 };

	private static final String IMAGE_PATH = "src/test/resources/org/geomajas/plugin/rasterizing/images/";

	@Before
	public void login() {
		// assure security context is set
		securityManager.createSecurityContext(null);
	}

	@Test
	public void testRasterizeFromCache() throws Exception {
		InternalTile tile;
		recorder.clear();
		// create metadata
		GetVectorTileRequest metadata = new GetVectorTileRequest();
		metadata.setCode(new TileCode(4, 8, 8));
		metadata.setCrs("EPSG:4326");
		metadata.setLayerId(layerBeans.getId());
		metadata.setPanOrigin(new Coordinate(0, 0));
		metadata.setScale(16);
		metadata.setRenderer(TileMetadata.PARAM_SVG_RENDERER);
		metadata.setStyleInfo(layerBeansStyleInfo);
		metadata.setPaintLabels(false);
		metadata.setPaintGeometries(true);
		// get tile
		tile = vectorLayerService.getTile(metadata);
		// find the key
		String url = tile.getFeatureContent();
		Assert.assertTrue(url.startsWith("http://test/rasterizing/beans/"));
		Assert.assertTrue(url.endsWith(".png"));
		String key = url.substring("http://test/rasterizing/beans/".length(), url.length()-4);
		Object o = cacheManager.get(layerBeans, CacheCategory.RASTER, key);
		Assert.assertNotNull("Missing raster in cache",o);
		MockHttpServletResponse response = new MockHttpServletResponse();
		controller.getWms(layerBeans.getId(), key, new MockHttpServletRequest(), response);
		Assert.assertEquals("", recorder.matches(CacheCategory.RASTER, "Got item from cache"));
		Assert.assertArrayEquals(getBytesFromFile(IMAGE_PATH + "beans-4-8-8.png"), response.getContentAsByteArray());
		cacheManager.drop(layerBeans);
	}

	@Test
	public void testRasterizeFromRebuildCache() throws Exception {
		InternalTile tile;
		recorder.clear();
		// create metadata
		GetVectorTileRequest metadata = new GetVectorTileRequest();
		metadata.setCode(new TileCode(4, 8, 8));
		metadata.setCrs("EPSG:4326");
		metadata.setLayerId(layerBeans.getId());
		metadata.setPanOrigin(new Coordinate(0, 0));
		metadata.setScale(16);
		metadata.setRenderer(TileMetadata.PARAM_SVG_RENDERER);
		metadata.setStyleInfo(layerBeansStyleInfo);
		metadata.setPaintLabels(false);
		metadata.setPaintGeometries(true);
		// get tile
		tile = vectorLayerService.getTile(metadata);
		// find the key
		String url = tile.getFeatureContent();
		Assert.assertTrue(url.startsWith("http://test/rasterizing/beans/"));
		Assert.assertTrue(url.endsWith(".png"));
		String key = url.substring("http://test/rasterizing/beans/".length(), url.length()-4);
		Object o = cacheManager.get(layerBeans, CacheCategory.RASTER, key);
		Assert.assertNotNull("Missing raster in cache",o);
		// remove from normal cache
		cacheManager.get(layerBeans, CacheCategory.RASTER, key);
		MockHttpServletResponse response = new MockHttpServletResponse();
		controller.getWms(layerBeans.getId(), key, new MockHttpServletRequest(), response);
		Assert.assertEquals("",
				recorder.matches(CacheCategory.REBUILD, 
						key, 
						"Rasterization success"
		));
		Assert.assertArrayEquals(getBytesFromFile(IMAGE_PATH + "beans-4-8-8.png"), response.getContentAsByteArray());
		cacheManager.drop(layerBeans);
	}

	private byte[] getBytesFromFile(String fileName) throws IOException {
		File file = new File(fileName);
		FileInputStream fis = new FileInputStream(file);
		byte[] buff = new byte[(int) file.length()];
		fis.read(buff);
		fis.close();
		return buff;
	}
}
