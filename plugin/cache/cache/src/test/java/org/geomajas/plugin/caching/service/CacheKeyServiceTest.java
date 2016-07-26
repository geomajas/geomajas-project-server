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

package org.geomajas.plugin.caching.service;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import junit.framework.Assert;
import org.geomajas.internal.service.pipeline.PipelineContextImpl;
import org.geomajas.service.pipeline.PipelineContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Test for the {@link CacheKeyService} implementation.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml", "/managerContext.xml",
		"/dummySecurity.xml", "/org/geomajas/testdata/layerBeans.xml"})
public class CacheKeyServiceTest {

	@Autowired
	private CacheKeyService cacheKeyService;

	@Test
	public void testGetCacheContextAndKey() throws Exception {
		String key1 = "key1";
		String key2 = "key2";
		String key3 = "key3";
		String key4 = "key4";
		PipelineContext pipelineContext = new PipelineContextImpl();
		pipelineContext.put(key1, key1);
		pipelineContext.put(key3, key3);
		GeometryFactory geometryFactory = new GeometryFactory();
		pipelineContext.put(key4, new GeometryCollection(new Geometry[] {}, geometryFactory));

		// a context
		CacheContext cacheContext1 = cacheKeyService.getCacheContext(pipelineContext, new String[] {key1, key2, key4});
		Assert.assertEquals(key1, cacheContext1.get(key1));
		Assert.assertNull(cacheContext1.get(key2));
		Assert.assertNull(cacheContext1.get(key3));
		Assert.assertTrue(cacheContext1.get(key4) instanceof GeometryCollection);
		String cacheKey1 = cacheKeyService.getCacheKey(cacheContext1);
		Assert.assertNotNull(cacheKey1);

		// same context (in a different instance), should be same key
		CacheContext cacheContext2 = cacheKeyService.getCacheContext(pipelineContext, new String[] {key1, key2, key4});
		Assert.assertEquals(key1, cacheContext2.get(key1));
		Assert.assertNull(cacheContext2.get(key2));
		Assert.assertNull(cacheContext2.get(key3));
		Assert.assertTrue(cacheContext1.get(key4) instanceof GeometryCollection);
		String cacheKey2 = cacheKeyService.getCacheKey(cacheContext2);
		Assert.assertNotNull(cacheKey2);
		Assert.assertEquals(cacheKey1, cacheKey2);

		// context with different keys and values, key should be different
		CacheContext cacheContext3 = cacheKeyService.getCacheContext(pipelineContext, new String[] {key1, key2});
		Assert.assertEquals(key1, cacheContext3.get(key1));
		Assert.assertNull(cacheContext3.get(key2));
		Assert.assertNull(cacheContext3.get(key3));
		Assert.assertNull(key4, cacheContext3.get(key4));
		String cacheKey3 = cacheKeyService.getCacheKey(cacheContext2);
		Assert.assertNotNull(cacheKey3);
		Assert.assertEquals(cacheKey1, cacheKey3);

		// context with different keys but same values as context1, key should be different
		// (same would be acceptable, but this is better)
		CacheContext cacheContext4 = cacheKeyService.getCacheContext(pipelineContext, new String[] {key1, key4});
		Assert.assertEquals(key1, cacheContext4.get(key1));
		Assert.assertNull(cacheContext4.get(key2));
		Assert.assertNull(cacheContext4.get(key3));
		Assert.assertTrue(cacheContext1.get(key4) instanceof GeometryCollection);
		String cacheKey4 = cacheKeyService.getCacheKey(cacheContext2);
		Assert.assertNotNull(cacheKey4);
		Assert.assertEquals(cacheKey1, cacheKey4);
	}

	@Test
	public void testMakeUnique() throws Exception {
		String data = "bla";
		Assert.assertNotNull(cacheKeyService.makeUnique(data));
		Assert.assertNotSame(data, cacheKeyService.makeUnique(data));
	}

	@Test
	public void testOtherContextImpl() {
		CacheContext context = new MyCacheContext();
		String cacheKey1 = cacheKeyService.getCacheKey(context);
		context.put("bla", "bla");
		String cacheKey2 = cacheKeyService.getCacheKey(context);
		GeometryFactory geometryFactory = new GeometryFactory();
		context.put("geom", new GeometryCollection(new Geometry[] {}, geometryFactory));
		String cacheKey3 = cacheKeyService.getCacheKey(context);
		System.out.println("keys " + cacheKey1 + " " + cacheKey2 + " " + cacheKey3);
		Assert.assertFalse(cacheKey1.equals(cacheKey2));
		Assert.assertFalse(cacheKey2.equals(cacheKey3));
		Assert.assertFalse(cacheKey3.equals(cacheKey1));
	}

	private static class MyCacheContext implements CacheContext {
		private Map<String, Object> map = new HashMap<String, Object>();

		public void put(String key, Object object) {
			map.put(key, object);
		}

		public Object get(String key) {
			return map.get(key);
		}

		public <TYPE> TYPE get(String key, Class<TYPE> type) {
			return (TYPE) get(key);
		}
	}
}
