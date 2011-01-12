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
package org.geomajas.command.configuration;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.dto.GetConfigurationRequest;
import org.geomajas.command.dto.GetConfigurationResponse;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientUserDataInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.layer.Layer;
import org.geomajas.service.ConfigurationService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Verify the getting of the map configuration (and especially the transformation of maxExtent coordinates).
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/layerCountries.xml", "/org/geomajas/testdata/simplevectorsContext.xml" })
public class GetConfigurationCommandTest {

	private static final double DOUBLE_TOLERANCE = .0000000001;
	private static final String APP_ID = "simplevectors";

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private CommandDispatcher dispatcher;

	@Test
	public void testConvertApplication() throws Exception {
		GetConfigurationRequest request = new GetConfigurationRequest();
		request.setApplicationId(APP_ID);
		GetConfigurationResponse response = (GetConfigurationResponse) dispatcher.execute(
				"command.configuration.Get", request, null, "en");
		if (response.isError()) {
			response.getErrors().get(0).printStackTrace();
		}
		Assert.assertFalse(response.isError());
		ClientApplicationInfo appInfo = response.getApplication();
		Assert.assertNotNull(appInfo);
		Assert.assertEquals(APP_ID, appInfo.getId());
		Assert.assertEquals(96, appInfo.getScreenDpi());

		// widget data
		Assert.assertNotNull(appInfo.getWidgetInfo());
		Assert.assertNotNull(appInfo.getWidgetInfo("mapSelect"));
		Assert.assertNull(appInfo.getWidgetInfo("layerTree"));
		Assert.assertEquals("map1, map2",
				((ClientApplicationInfo.DummyClientWidgetInfo) appInfo.getWidgetInfo("mapSelect")).getDummy());

		verifyMap(appInfo.getMaps().get(2));
	}

	private void verifyMap(ClientMapInfo mapInfo) {
		// first test base assumptions
		Layer layer = configurationService.getLayer("countries");
		Bbox configMaxExtent = layer.getLayerInfo().getMaxExtent();
		Assert.assertEquals(-85.05112877980659, configMaxExtent.getX(), DOUBLE_TOLERANCE);
		Assert.assertEquals(-85.05112877980659, configMaxExtent.getY(), DOUBLE_TOLERANCE);
		Assert.assertEquals(170.102257, configMaxExtent.getWidth(), DOUBLE_TOLERANCE);
		Assert.assertEquals(170.102257, configMaxExtent.getHeight(), DOUBLE_TOLERANCE);

		// now test the map conversion

		Assert.assertNotNull(mapInfo);
		Bbox mapMaxExtent = mapInfo.getLayers().get(0).getMaxExtent();

		// these values were registered during a first run, they have *not* been externally verified
		Assert.assertEquals(-9467848.347161204, mapMaxExtent.getX(), DOUBLE_TOLERANCE);
		Assert.assertEquals(-2.0037508342789236E7, mapMaxExtent.getY(), DOUBLE_TOLERANCE);
		Assert.assertEquals(1.8935696632026553E7, mapMaxExtent.getWidth(), DOUBLE_TOLERANCE);
		Assert.assertEquals(4.007501596344786E7, mapMaxExtent.getHeight(), DOUBLE_TOLERANCE);

		// user data
		ClientUserDataInfo info = mapInfo.getUserData();
		Assert.assertNotNull(info);
		Assert.assertTrue(info instanceof ClientApplicationInfo.DummyClientUserDataInfo);
		Assert.assertEquals("some data", ((ClientApplicationInfo.DummyClientUserDataInfo) info).getDummy());

		// widget data
		Assert.assertNotNull(mapInfo.getWidgetInfo());
		Assert.assertNull(mapInfo.getWidgetInfo("mapSelect"));
		Assert.assertNotNull(mapInfo.getWidgetInfo("layerTree"));
		Assert.assertEquals("layer1, layer2",
				((ClientApplicationInfo.DummyClientWidgetInfo) mapInfo.getWidgetInfo("layerTree")).getDummy());
		
		// widget data on the layer
		ClientLayerInfo layerInfo = mapInfo.getLayers().get(0);
		for(ClientLayerInfo i: mapInfo.getLayers()) {
			if(i.getId().equals("countries")) {
				layerInfo = i;
			}
		}
		Assert.assertNotNull(layerInfo);
		Assert.assertNotNull(layerInfo.getWidgetInfo());
		Assert.assertNull(layerInfo.getWidgetInfo("layerTree"));
		Assert.assertNotNull(layerInfo.getWidgetInfo("customLayerInfoWidget"));
		Assert.assertEquals("org.geomajas.widget.IpsumWidget",
				((ClientApplicationInfo.DummyClientWidgetInfo) layerInfo.getWidgetInfo("customLayerInfoWidget")).getDummy());
		
	}

}
