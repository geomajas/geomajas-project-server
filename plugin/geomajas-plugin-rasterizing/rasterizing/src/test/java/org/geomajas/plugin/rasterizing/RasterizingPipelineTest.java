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

import java.io.IOException;
import java.io.InputStream;

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
import org.springframework.mock.web.MockHttpServletResponse;
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

	private static final String IMAGE_PATH = "org/geomajas/plugin/rasterizing/images/";

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
		controller.getImage(layerBeans.getId(), key, response);
		Assert.assertEquals("", recorder.matches(CacheCategory.RASTER, "Got item from cache"));
		Assert.assertArrayEquals(getBytes(IMAGE_PATH + "beans-4-8-8.png"), response.getContentAsByteArray());
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
		controller.getImage(layerBeans.getId(), key, response);
		Assert.assertEquals("",
				recorder.matches(CacheCategory.REBUILD, 
						key, 
						"Rasterization success"
		));
		Assert.assertArrayEquals(getBytes(IMAGE_PATH + "beans-4-8-8.png"), response.getContentAsByteArray());
		cacheManager.drop(layerBeans);
	}

	public byte[] getBytes( String name )
            throws IOException
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if ( null == classLoader )
        {
            classLoader = getClass().getClassLoader();
        }
        InputStream stream = classLoader.getResourceAsStream( name );
        ByteBuffer buffer = new ByteBuffer(20000);
        try
        {
            byte[] b = new byte[4096];
            for (int n; (n = stream.read(b)) != -1;) {
                buffer.append(b, 0, n);
            }
        }
        finally
        {
            try
            {
                stream.close();
            }
            catch (IOException ioe)
            { /* ignore exception on close */}
        }
        return buffer.toByteArray();
    }

	public final class ByteBuffer {

		private int length;

		private byte[] buffer;

		public ByteBuffer(int size) {
			buffer = new byte[size];
			length = 0;
		}

		public void ensureCapacity(int size) {
			if (size > buffer.length) {
				int newsize = (buffer.length + 1) * 2;
				if (size > newsize) {
					newsize = size;
				}
				byte[] old = buffer;
				buffer = new byte[newsize];
				System.arraycopy(old, 0, buffer, 0, old.length);
			}
		}

		public void append(byte[] value, int offset, int width) {
			if (width < 0) {
				throw new IndexOutOfBoundsException();
			}
			ensureCapacity(length + width);
			System.arraycopy(value, offset, buffer, length, width);
			length += width;
		}

		public byte[] toByteArray() {
			byte[] value = new byte[length];
			System.arraycopy(buffer, 0, value, 0, length);
			return value;
		}

		public String toString() {
			return new String(buffer, 0, length);
		}
	}
}
