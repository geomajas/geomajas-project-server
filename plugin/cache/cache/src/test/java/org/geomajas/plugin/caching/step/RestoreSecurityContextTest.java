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

package org.geomajas.plugin.caching.step;

import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandResponse;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.bean.BeanLayer;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheManagerServiceImpl;
import org.geomajas.plugin.staticsecurity.command.dto.LoginRequest;
import org.geomajas.plugin.staticsecurity.command.dto.LoginResponse;
import org.geomajas.security.SecurityContext;
import org.geomajas.service.TestRecorder;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineService;
import org.geomajas.spring.ThreadScopeContextHolder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test to verify that the restore of the security context works.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/caching/DefaultCachedPipelines.xml", "/org/geomajas/spring/testRecorder.xml",
		"/org/geomajas/testdata/beanContext.xml", "/org/geomajas/testdata/layerBeans.xml",
		"/cacheServiceContext.xml", "/VectorLayerSecurityArea.xml"})
public class RestoreSecurityContextTest {

	private static final String LAYER_ID = "beans";
	private static final String PIPELINE = "securityTestPipeline";
	private static final double ALLOWANCE = .00000001;

	@Autowired
	private TestRecorder recorder;

	@Autowired
	private VectorLayerService layerService;

	@Autowired
	@Qualifier(LAYER_ID)
	private BeanLayer beanLayer;

	@Autowired
	private org.geomajas.security.SecurityManager securityManager;

	@Autowired
	private CommandDispatcher commandDispatcher;

	@Autowired
	private PipelineService<StringContainer> pipelineService;

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private CacheManagerServiceImpl cacheManager;

	@After
	public void clearSecurityContext() {
		cacheManager.drop(beanLayer);
		recorder.clear();
		ThreadScopeContextHolder.clear();
	}

	// assure we are logged in as a specific user to set correct authorizations
	public void login(String name) {
		LoginRequest request = new LoginRequest();
		request.setLogin(name);
		request.setPassword(name);
		CommandResponse response = commandDispatcher.execute(LoginRequest.COMMAND, request, null, "en");
		junit.framework.Assert.assertFalse(response.isError());
		junit.framework.Assert.assertTrue(response instanceof LoginResponse);
		securityManager.createSecurityContext(((LoginResponse)response).getToken());
	}

	@Test
	public void testRestoreSecurityContext() throws Exception {
		String key;
		StringContainer container;
		PipelineContext context;

		// set the stage

		login("luc");
		recorder.clear();
		context = pipelineService.createContext();
		context.put(PipelineCode.LAYER_ID_KEY, LAYER_ID);
		context.put(PipelineCode.FILTER_KEY, "blabla-luc");
		container = new StringContainer();
		pipelineService.execute(PIPELINE, LAYER_ID, context, container);
		Assert.assertEquals("blabla-luc", container.getString());
		org.junit.Assert.assertEquals("", recorder.matches(CacheCategory.BOUNDS,
				"Put item in cache"));
		key = context.get(SecurityTestInterceptor.CACHE_KEY, String.class);

		checkLuc();
		org.junit.Assert.assertEquals("", recorder.matches(CacheCategory.BOUNDS,
				"Put item in cache"));
		Assert.assertEquals("luc", securityContext.getUserId());

		login("marino");
		recorder.clear();
		context = pipelineService.createContext();
		context.put(PipelineCode.LAYER_ID_KEY, LAYER_ID);
		context.put(PipelineCode.FILTER_KEY, "blabla-marino");
		container = new StringContainer();
		pipelineService.execute(PIPELINE, LAYER_ID, context, container);
		Assert.assertEquals("blabla-marino", container.getString());
		org.junit.Assert.assertEquals("", recorder.matches(CacheCategory.BOUNDS,
				"Put item in cache"));

		checkMarino();
		org.junit.Assert.assertEquals("", recorder.matches(CacheCategory.BOUNDS,
				"Put item in cache"));

		// now try to get luc's stuff from the cache, restoring the security context

		recorder.clear();
		context = pipelineService.createContext();
		context.put(SecurityTestInterceptor.CACHE_KEY, key);
		context.put(PipelineCode.LAYER_ID_KEY, LAYER_ID);
		container = new StringContainer();
		pipelineService.execute(PIPELINE, LAYER_ID, context, container);
		Assert.assertEquals("blabla-luc", container.getString());
		org.junit.Assert.assertEquals("", recorder.matches(CacheCategory.BOUNDS,
				"Got item from cache"));

		checkLuc();
		org.junit.Assert.assertEquals("", recorder.matches(CacheCategory.BOUNDS,
				"Got item from cache"));
		Assert.assertEquals(null, securityContext.getUserId());
	}

	private void checkLuc() throws Exception {
		CoordinateReferenceSystem crs = beanLayer.getCrs();
		Envelope envelope;
		recorder.clear();
		envelope = layerService.getBounds(LAYER_ID, crs, null);
		junit.framework.Assert.assertEquals(0, envelope.getMinX(), ALLOWANCE);
		junit.framework.Assert.assertEquals(0, envelope.getMinY(), ALLOWANCE);
		junit.framework.Assert.assertEquals(7, envelope.getMaxX(), ALLOWANCE);
		junit.framework.Assert.assertEquals(3, envelope.getMaxY(), ALLOWANCE);
	}

	private void checkMarino() throws Exception {
		CoordinateReferenceSystem crs = beanLayer.getCrs();
		Envelope envelope;
		recorder.clear();
		envelope = layerService.getBounds(LAYER_ID, crs, null);
		junit.framework.Assert.assertEquals(2, envelope.getMinX(), ALLOWANCE);
		junit.framework.Assert.assertEquals(0, envelope.getMinY(), ALLOWANCE);
		junit.framework.Assert.assertEquals(7, envelope.getMaxX(), ALLOWANCE);
		junit.framework.Assert.assertEquals(3, envelope.getMaxY(), ALLOWANCE);
	}

}
