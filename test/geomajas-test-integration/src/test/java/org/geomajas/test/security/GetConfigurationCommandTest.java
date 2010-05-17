/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.test.security;

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
import org.geomajas.security.*;
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
		"/org/geomajas/layer/bean/beanContext.xml", "/org/geomajas/layer/bean/layerBeans.xml",
		"/org/geomajas/test/security/GetConfiguration.xml"})
public class GetConfigurationCommandTest {

	private static final String APP_ID = "bean";
	private static final String MAP_ID = "beanMap";
	private static final String CLIENT_LAYER_ID = "beansLayer";
	private static final String LAYER_ID = "beans";
	private static final String GET_MAP = "command.configuration.GetMap";

	@Autowired
	@Qualifier("beans")
	private BeanLayer beanLayer;

	@Autowired
	private org.geomajas.security.SecurityManager securityManager;

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private CommandDispatcher commandDispatcher;

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
		CommandResponse response = commandDispatcher.execute(GET_MAP, request, securityContext.getToken(), "en");
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
		CommandResponse response = commandDispatcher.execute(GET_MAP, request, securityContext.getToken(), "en");
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
