/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.rasterizing;

import java.io.OutputStream;

import org.geomajas.command.dto.GetVectorTileRequest;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.layer.tile.TileMetadata;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheManagerServiceImpl;
import org.geomajas.plugin.rasterizing.mvc.RasterizingController;
import org.geomajas.security.SecurityManager;
import org.geomajas.service.TestRecorder;
import org.geomajas.testdata.TestPathBinaryStreamAssert;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests the rasterizing pipeline used by the RasterizingController.
 * 
 * @author Jan De Moerloose
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/rasterizing/rasterizing-service.xml",
		"/org/geomajas/plugin/rasterizing/rasterizing-pipelines.xml", "/org/geomajas/spring/testRecorder.xml",
		"/org/geomajas/testdata/beanContext.xml", "/org/geomajas/testdata/layerBeans.xml",
		"/org/geomajas/testdata/layerBeansPoint.xml","/org/geomajas/testdata/layerBeansMultiLine.xml" })
@DirtiesContext
public class RasterizingPipelineTest {

	@Autowired
	private CacheManagerServiceImpl cacheManager;

	@Autowired
	@Qualifier("beans")
	private VectorLayer layerBeans;

	@Autowired
	@Qualifier("beansStyleInfo")
	private NamedStyleInfo layerBeansStyleInfo;

	@Autowired
	@Qualifier("layerBeansPoint")
	private VectorLayer layerBeansPoint;


	@Autowired
	@Qualifier("layerBeansPointStyleInfo")
	private NamedStyleInfo layerBeansPointStyleInfo;

	@Autowired
	@Qualifier("layerBeansMultiLine")
	private VectorLayer layerBeansMultiLine;

	@Autowired
	@Qualifier("layerBeansMultiLineStyleInfo")
	private NamedStyleInfo layerBeansMultiLineStyleInfo;

	@Autowired
	RasterizingController controller;

	@Autowired
	TestRecorder recorder;

	@Autowired
	private SecurityManager securityManager;

	@Autowired
	private VectorLayerService vectorLayerService;

	private static byte[] DUMMY_BYTES = new byte[] { 0, 1, 2 };

	private static final String IMAGE_CLASS_PATH = "org/geomajas/plugin/rasterizing/images/rasterizingpipeline";

	private static final double DELTA = 1E-6;

	// changing this to true and running the test from the base directory will generate the images !
	private boolean writeImages = false;

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
		Assert.assertTrue(url.startsWith("http://test/rasterizing/layer/beans/"));
		Assert.assertTrue(url.contains("?"));
		String key = url.substring("http://test/rasterizing/layer/beans/".length(), url.indexOf(".png"));
		Object o = cacheManager.get(layerBeans, CacheCategory.RASTER, key);
		Assert.assertNotNull("Missing raster in cache", o);
		MockHttpServletResponse response = new MockHttpServletResponse();
		controller.getImage(layerBeans.getId(), key, response);
		Assert.assertEquals("", recorder.matches(CacheCategory.RASTER, "Rasterization success", "Put item in cache",
				"Put item in cache", "Got item from cache"));
		new ServletResponseAssert(response).assertEqualImage("beans-4-8-8.png", writeImages, DELTA);
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
		metadata.setLayerId(layerBeansPoint.getId());
		metadata.setPanOrigin(new Coordinate(1, 1));
		metadata.setScale(16);
		metadata.setRenderer(TileMetadata.PARAM_SVG_RENDERER);
		metadata.setStyleInfo(layerBeansPointStyleInfo);
		metadata.setPaintLabels(false);
		metadata.setPaintGeometries(true);
		// get tile
		tile = vectorLayerService.getTile(metadata);
		// find the key
		String url = tile.getFeatureContent();
		Assert.assertTrue(url.startsWith("http://test/rasterizing/layer/layerBeansPoint/"));
		Assert.assertTrue(url.contains("?"));
		String key = url.substring("http://test/rasterizing/layer/layerBeansPoint/".length(), url.indexOf(".png"));
		Object o = cacheManager.get(layerBeansPoint, CacheCategory.RASTER, key);
		Assert.assertNull("Unexpected raster in cache", o);

		// clear to test without security context
		securityManager.clearSecurityContext();
		MockHttpServletResponse response = new MockHttpServletResponse();
		controller.getImage(layerBeansPoint.getId(), key, response);
		Assert.assertEquals("", recorder.matches(CacheCategory.REBUILD, "Put item in cache", "Got item from cache",
				"Put item in cache"));
		new ServletResponseAssert(response).assertEqualImage("beansPoint-4-8-8.png", writeImages, DELTA);
		cacheManager.drop(layerBeansPoint);
	}

	@Test
	public void testRasterizeFromAllCache() throws Exception {
		InternalTile tile;
		recorder.clear();
		// create metadata
		GetVectorTileRequest metadata = createRequest();
		// get tile
		tile = vectorLayerService.getTile(metadata);
		// recreate same metadata and get tile again
		metadata = createRequest();
		tile = vectorLayerService.getTile(metadata);
		cacheManager.drop(layerBeansMultiLine);
		Assert.assertEquals("", recorder.matches(CacheCategory.TILE, "Put item in cache", "Got item from cache"));
	}

	private GetVectorTileRequest createRequest() {
		GetVectorTileRequest metadata = new GetVectorTileRequest();
		metadata.setCode(new TileCode(4, 8, 8));
		metadata.setCrs("EPSG:4326");
		metadata.setLayerId(layerBeansMultiLine.getId());
		metadata.setPanOrigin(new Coordinate(12, 10));
		metadata.setScale(16);
		metadata.setRenderer(TileMetadata.PARAM_SVG_RENDERER);
		metadata.setStyleInfo(layerBeansMultiLineStyleInfo);
		metadata.setPaintLabels(false);
		metadata.setPaintGeometries(true);
		return metadata;
	}

	class ServletResponseAssert extends TestPathBinaryStreamAssert {

		private MockHttpServletResponse response;

		public ServletResponseAssert(MockHttpServletResponse response) {
			super(IMAGE_CLASS_PATH);
			this.response = response;
		}

		public void generateActual(OutputStream out) throws Exception {
			out.write(response.getContentAsByteArray());
		}

	}

}
