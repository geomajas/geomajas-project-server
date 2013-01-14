/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.caching.step;

import com.vividsolutions.jts.geom.Envelope;
import junit.framework.Assert;
import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandResponse;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.bean.BeanLayer;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheManagerServiceImpl;
import org.geomajas.plugin.staticsecurity.command.dto.LoginRequest;
import org.geomajas.plugin.staticsecurity.command.dto.LoginResponse;
import org.geomajas.service.TestRecorder;
import org.geomajas.spring.ThreadScopeContextHolder;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test that the security works in combination the the GetBounds caching.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/caching/DefaultCachedPipelines.xml", "/org/geomajas/spring/testRecorder.xml",
		"/org/geomajas/testdata/beanContext.xml", "/org/geomajas/testdata/layerBeans.xml",
		"/cacheServiceContext.xml", "/VectorLayerSecurityArea.xml"})
public class GetBoundsSecurityTest {

	private static final String LAYER_ID = "beans";
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
	private CacheManagerServiceImpl cacheManager;

	// assure we are logged in as a specific user to set correct authorizations
	public void login(String name) {
		LoginRequest request = new LoginRequest();
		request.setLogin(name);
		request.setPassword(name);
		CommandResponse response = commandDispatcher.execute(LoginRequest.COMMAND, request, null, "en");
		Assert.assertFalse(response.isError());
		Assert.assertTrue(response instanceof LoginResponse);
		securityManager.createSecurityContext(((LoginResponse) response).getToken());
	}

	@After
	public void clearSecurityContext() {
		cacheManager.drop(beanLayer);
		recorder.clear();
		ThreadScopeContextHolder.clear();
	}

	@Test
	public void testGetBoundsVisibleArea() throws Exception {
		CoordinateReferenceSystem crs = beanLayer.getCrs();
		Envelope envelope;

		login("luc");
		recorder.clear();
		envelope = layerService.getBounds(LAYER_ID, crs, null);
		Assert.assertEquals(0, envelope.getMinX(), ALLOWANCE);
		Assert.assertEquals(0, envelope.getMinY(), ALLOWANCE);
		Assert.assertEquals(7, envelope.getMaxX(), ALLOWANCE);
		Assert.assertEquals(3, envelope.getMaxY(), ALLOWANCE);
		org.junit.Assert.assertEquals("", recorder.matches(CacheCategory.BOUNDS,
				"Put item in cache"));

		login("marino");
		recorder.clear();
		envelope = layerService.getBounds(LAYER_ID, crs, null);
		Assert.assertEquals(2, envelope.getMinX(), ALLOWANCE);
		Assert.assertEquals(0, envelope.getMinY(), ALLOWANCE);
		Assert.assertEquals(7, envelope.getMaxX(), ALLOWANCE);
		Assert.assertEquals(3, envelope.getMaxY(), ALLOWANCE);
		org.junit.Assert.assertEquals("", recorder.matches(CacheCategory.BOUNDS,
				"Put item in cache"));

		login("luc");
		recorder.clear();
		envelope = layerService.getBounds(LAYER_ID, crs, null);
		Assert.assertEquals(0, envelope.getMinX(), ALLOWANCE);
		Assert.assertEquals(0, envelope.getMinY(), ALLOWANCE);
		Assert.assertEquals(7, envelope.getMaxX(), ALLOWANCE);
		Assert.assertEquals(3, envelope.getMaxY(), ALLOWANCE);
		org.junit.Assert.assertEquals("", recorder.matches(CacheCategory.BOUNDS,
				"Got item from cache"));

		login("marino");
		recorder.clear();
		envelope = layerService.getBounds(LAYER_ID, crs, null);
		Assert.assertEquals(2, envelope.getMinX(), ALLOWANCE);
		Assert.assertEquals(0, envelope.getMinY(), ALLOWANCE);
		Assert.assertEquals(7, envelope.getMaxX(), ALLOWANCE);
		Assert.assertEquals(3, envelope.getMaxY(), ALLOWANCE);
		org.junit.Assert.assertEquals("", recorder.matches(CacheCategory.BOUNDS,
				"Got item from cache"));
	}

}
