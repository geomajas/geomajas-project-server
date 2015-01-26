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
package org.geomajas.command.configuration;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.dto.GetConfigurationRequest;
import org.geomajas.command.dto.GetConfigurationResponse;
import org.geomajas.command.dto.GetMapConfigurationRequest;
import org.geomajas.command.dto.GetMapConfigurationResponse;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientUserDataInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.layer.Layer;
import org.geomajas.service.ConfigurationService;
import org.junit.After;
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
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/layerCountries.xml", "/org/geomajas/testdata/simplevectorsContext.xml",
		"/org/geomajas/command/ServerSideOnlyConfiguration.xml"})
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
				GetConfigurationRequest.COMMAND, request, null, "en");
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
		for (ClientLayerInfo i : mapInfo.getLayers()) {
			if (i.getId().equals("countries")) {
				layerInfo = i;
			}
		}
		Assert.assertNotNull(layerInfo);
		Assert.assertNotNull(layerInfo.getWidgetInfo());
		Assert.assertNull(layerInfo.getWidgetInfo("layerTree"));
		Assert.assertNotNull(layerInfo.getWidgetInfo("customLayerInfoWidget"));
		Assert.assertEquals("org.geomajas.widget.IpsumWidget",
				((ClientApplicationInfo.DummyClientWidgetInfo) layerInfo.getWidgetInfo("customLayerInfoWidget"))
						.getDummy());

	}

	@Test
	public void testServerSideOnlyInfo() throws Exception {
		GetConfigurationRequest request = new GetConfigurationRequest();
		request.setApplicationId("appServerSideOnly");
		GetConfigurationResponse response = (GetConfigurationResponse) dispatcher.execute(
				GetConfigurationRequest.COMMAND, request, null, "en");
		if (response.isError()) {
			response.getErrors().get(0).printStackTrace();
		}
		Assert.assertFalse(response.isError());
		ClientApplicationInfo appInfo = response.getApplication();
		Assert.assertNotNull(appInfo);

		// user data
		Assert.assertNull(appInfo.getUserData());

		// widget data
		Assert.assertNotNull(appInfo.getWidgetInfo());
		Assert.assertNull(appInfo.getWidgetInfo("appDummy")); // not present
		Assert.assertNotNull(appInfo.getWidgetInfo("mapSelect")); // present
		Assert.assertNull(appInfo.getWidgetInfo("mapDummy")); // filtered because ServerSideOnlyInfo

		// map data
		ClientMapInfo mapInfo = appInfo.getMaps().get(0);
		Assert.assertNull(mapInfo.getUserData());
		Assert.assertNotNull(mapInfo);
		Assert.assertNotNull(mapInfo.getWidgetInfo());
		Assert.assertNull(mapInfo.getWidgetInfo("appDummy")); // not present
		Assert.assertNotNull(mapInfo.getWidgetInfo("layerTree")); // present
		Assert.assertNull(mapInfo.getWidgetInfo("mapDummy")); // filtered because ServerSideOnlyInfo
	}

}
