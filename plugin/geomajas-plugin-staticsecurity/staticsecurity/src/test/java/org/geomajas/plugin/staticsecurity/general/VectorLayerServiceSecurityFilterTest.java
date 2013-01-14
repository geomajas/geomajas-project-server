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

package org.geomajas.plugin.staticsecurity.general;

import junit.framework.Assert;
import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandResponse;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.bean.BeanLayer;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.plugin.staticsecurity.command.dto.LoginRequest;
import org.geomajas.plugin.staticsecurity.command.dto.LoginResponse;
import org.geomajas.security.SecurityManager;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Tests for proper application of security in {@link VectorLayerServiceInvisibleLayerTest}.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/beanContext.xml", "/org/geomajas/testdata/layerBeans.xml",
		"/org/geomajas/plugin/staticsecurity/general/VectorLayerSecurityFilter.xml"})
public class VectorLayerServiceSecurityFilterTest {

	private static final String LAYER_ID = "beans";

	@Autowired
	private VectorLayerService layerService;

	@Autowired
	@Qualifier("beans")
	private BeanLayer beanLayer;

	@Autowired
	private SecurityManager securityManager;

	@Autowired
	private CommandDispatcher commandDispatcher;

	@After
	public void clearSecurityContext() {
		securityManager.clearSecurityContext();
	}

	// assure we are logged in as a specific user to set correct authorizations
	public void login(String name) {
		LoginRequest request = new LoginRequest();
		request.setLogin(name);
		request.setPassword(name);
		CommandResponse response = commandDispatcher.execute("command.staticsecurity.Login", request, null, "en");
		Assert.assertFalse(response.isError());
		Assert.assertTrue(response instanceof LoginResponse);
		securityManager.createSecurityContext(((LoginResponse)response).getToken());
	}

	@Test
	public void testGetFeaturesSecurityFilter() throws Exception {
		List<InternalFeature> features;

		// check expected result when no filtering
		login("luc");
		features = layerService.getFeatures(LAYER_ID,
				beanLayer.getCrs(), null, null, VectorLayerService.FEATURE_INCLUDE_NONE);
		Assert.assertEquals(3, features.size());

		// check expected result when filter is applied
		login("marino");
		features = layerService.getFeatures(LAYER_ID,
				beanLayer.getCrs(), null, null, VectorLayerService.FEATURE_INCLUDE_NONE);
		Assert.assertEquals(1, features.size());
	}
}