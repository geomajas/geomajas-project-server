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

package org.geomajas.internal.service;

import junit.framework.Assert;

import org.geomajas.security.SecurityManager;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.TestRecorder;
import org.geomajas.spring.ThreadScopeContextHolder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests for VectorLayerService.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/beanContext.xml", "/org/geomajas/testdata/layerBeans.xml",
		"/org/geomajas/spring/testRecorder.xml", "/org/geomajas/internal/service/layerInvalidateContext.xml"})
public class ConfigurationServiceInvalidateTest {

	private static final String LAYER_ID = "beans";

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private SecurityManager securityManager;

	@Autowired
	private TestRecorder recorder;

	@Before
	public void login() {
		// assure security context is set
		securityManager.createSecurityContext(null);
	}

	@After
	public void clearSecurityContext() {
		securityManager.clearSecurityContext();
	}

	@After
	public void clearTestRecorder() {
		recorder.clear();
		ThreadScopeContextHolder.clear();
	}

	@Test
	public void testInvalidate() throws Exception {
		recorder.clear();
		configurationService.invalidateLayer(LAYER_ID);
		Assert.assertEquals("", recorder.matches("beans", "Invalidate layer"));
		Assert.assertEquals("", recorder.matches("--nullLayer--", "Invalidate layer"));

		// assure no errors thrown for null or invalid layer if
		configurationService.invalidateLayer(null);
		configurationService.invalidateLayer("invalid-layer-id");
	}

	@Test
	public void testInvalidateAll() throws Exception {
		recorder.clear();
		configurationService.invalidateAllLayers();
		Assert.assertEquals("", recorder.matches("beans", "Invalidate layer"));
		Assert.assertEquals("", recorder.matches("--nullLayer--", "Invalidate layer", "Invalidate layer"));
	}
}
