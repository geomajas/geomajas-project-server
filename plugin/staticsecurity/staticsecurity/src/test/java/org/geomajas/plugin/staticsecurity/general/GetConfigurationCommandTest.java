/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
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
import org.geomajas.command.dto.GetMapConfigurationRequest;
import org.geomajas.command.dto.GetMapConfigurationResponse;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientToolbarInfo;
import org.geomajas.layer.bean.BeanLayer;
import org.geomajas.plugin.staticsecurity.command.dto.LoginRequest;
import org.geomajas.plugin.staticsecurity.command.dto.LoginResponse;
import org.geomajas.security.SecurityContext;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Test the filtering in the configuration command.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/beanContext.xml", "/org/geomajas/testdata/layerBeans.xml",
		"/org/geomajas/plugin/staticsecurity/general/GetConfiguration.xml"})
public class GetConfigurationCommandTest {

	private static final String APP_ID = "bean";
	private static final String MAP_ID = "beanMap";
	private static final String CLIENT_LAYER_ID = "beansLayer";
	private static final String LAYER_ID = "beans";

	@Autowired
	@Qualifier("beans")
	private BeanLayer beanLayer;

	@Autowired
	private org.geomajas.security.SecurityManager securityManager;

	@Autowired
	private SecurityContext securityContext;

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
	public void testGetConfigurationFilterToolsAndInvisibleLayers() throws Exception {
		login("luc");
		GetMapConfigurationRequest request = new GetMapConfigurationRequest();
		request.setApplicationId(APP_ID);
		request.setMapId(MAP_ID);
		CommandResponse response = commandDispatcher.execute(GetMapConfigurationRequest.COMMAND, request,
				securityContext.getToken(), "en");
		Assert.assertFalse(response.isError());
		Assert.assertTrue(response instanceof GetMapConfigurationResponse);
		ClientMapInfo mapInfo = ((GetMapConfigurationResponse)response).getMapInfo();

		Assert.assertEquals(MAP_ID, mapInfo.getId());
		Assert.assertEquals("EPSG:4326", mapInfo.getCrs());

		ClientToolbarInfo toolbar = mapInfo.getToolbar();
		Assert.assertNotNull(toolbar);
		Assert.assertEquals("toolbar", toolbar.getId());
		Assert.assertEquals(1, toolbar.getTools().size());
		Assert.assertEquals("ZoomToSelection", toolbar.getTools().get(0).getId());

		List<ClientLayerInfo> layers = mapInfo.getLayers();
		Assert.assertEquals(0, layers.size());
	}

	@Test
	public void testGetConfigurationFilterAttributes() throws Exception {
		login("marino");
		GetMapConfigurationRequest request = new GetMapConfigurationRequest();
		request.setApplicationId(APP_ID);
		request.setMapId(MAP_ID);
		CommandResponse response = commandDispatcher.execute(GetMapConfigurationRequest.COMMAND, request,
				securityContext.getToken(), "en");
		Assert.assertFalse(response.isError());
		Assert.assertTrue(response instanceof GetMapConfigurationResponse);
		ClientMapInfo mapInfo = ((GetMapConfigurationResponse)response).getMapInfo();

		Assert.assertEquals(MAP_ID, mapInfo.getId());
		Assert.assertEquals("EPSG:4326", mapInfo.getCrs());

		ClientToolbarInfo toolbar = mapInfo.getToolbar();
		Assert.assertNotNull(toolbar);
		Assert.assertEquals("toolbar", toolbar.getId());
		Assert.assertEquals(4, toolbar.getTools().size());

		List<ClientLayerInfo> layers = mapInfo.getLayers();
		Assert.assertEquals(1, layers.size());
		ClientLayerInfo layer = layers.get(0);
		Assert.assertEquals(CLIENT_LAYER_ID, layer.getId());
		Assert.assertEquals(LAYER_ID, layer.getServerLayerId());
		Assert.assertEquals("Test beans", layer.getLabel());
		Assert.assertTrue(layer.getLayerInfo() instanceof VectorLayerInfo);
		VectorLayerInfo vectorLayerInfo = ((VectorLayerInfo)layer.getLayerInfo());
		Assert.assertEquals("EPSG:4326", vectorLayerInfo.getCrs());
		Assert.assertNotNull(vectorLayerInfo.getFeatureInfo());
		FeatureInfo featureInfo = vectorLayerInfo.getFeatureInfo();
		List<AttributeInfo> attributes = featureInfo.getAttributes();
		Assert.assertNotNull(attributes);
		Assert.assertEquals(1, attributes.size());
		Assert.assertEquals("stringAttr", attributes.get(0).getName());
	}

}
