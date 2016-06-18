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

package org.geomajas.command.configuration;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.dto.GetMapConfigurationRequest;
import org.geomajas.command.dto.GetMapConfigurationResponse;
import org.geomajas.configuration.client.BoundsLimitOption;
import org.geomajas.configuration.client.ClientApplicationInfo;
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
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/layerCountries.xml", "/org/geomajas/testdata/simplevectorsContext.xml",
		"/org/geomajas/testdata/viewBoundsOptionContext.xml",
		"/org/geomajas/command/ServerSideOnlyConfiguration.xml" })
public class GetMapConfigurationCommandTest {

	private static final double DOUBLE_TOLERANCE = .0000000001;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private CommandDispatcher dispatcher;

	@Test
	public void testConvertMaxExtent() throws Exception {
		Layer layer = configurationService.getLayer("countries");
		Bbox configMaxExtent = layer.getLayerInfo().getMaxExtent();
		Assert.assertEquals(-85.05112877980659, configMaxExtent.getX(), DOUBLE_TOLERANCE);
		Assert.assertEquals(-85.05112877980659, configMaxExtent.getY(), DOUBLE_TOLERANCE);
		Assert.assertEquals(170.102257, configMaxExtent.getWidth(), DOUBLE_TOLERANCE);
		Assert.assertEquals(170.102257, configMaxExtent.getHeight(), DOUBLE_TOLERANCE);

		GetMapConfigurationRequest request = new GetMapConfigurationRequest();
		request.setApplicationId("simplevectors");
		request.setMapId("coordTestMap");
		GetMapConfigurationResponse response = (GetMapConfigurationResponse) dispatcher.execute(
				GetMapConfigurationRequest.COMMAND, request, null, "en");
		if (response.isError()) {
			response.getErrors().get(0).printStackTrace();
		}
		Assert.assertFalse(response.isError());
		ClientMapInfo mapInfo = response.getMapInfo();
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
		// Default value of ViewBounds LimitOption
		BoundsLimitOption boundsLimitOption = mapInfo.getViewBoundsLimitOption();
		Assert.assertNotNull(info);
		Assert.assertEquals(BoundsLimitOption.COMPLETELY_WITHIN_MAX_BOUNDS, boundsLimitOption);
				
	}
	
	@Test
	public void testViewBoundsLimitOption() throws Exception {
		GetMapConfigurationRequest request = new GetMapConfigurationRequest();
		request.setApplicationId("vectorsMapWithViewBoundsOption");
		request.setMapId("viewBoundsLimitTestMap");
		GetMapConfigurationResponse response = (GetMapConfigurationResponse) dispatcher.execute(
				GetMapConfigurationRequest.COMMAND, request, null, "en");
		if (response.isError()) {
			response.getErrors().get(0).printStackTrace();
		}
		Assert.assertFalse(response.isError());
		ClientMapInfo mapInfo = response.getMapInfo();
		Assert.assertNotNull(mapInfo);

		// ViewBounds LimitOption
		BoundsLimitOption boundsLimitOption = mapInfo.getViewBoundsLimitOption();
		Assert.assertNotNull(boundsLimitOption);
		Assert.assertEquals(BoundsLimitOption.CENTER_WITHIN_MAX_BOUNDS, boundsLimitOption);
	}
	
	@Test
	public void testServerSideOnlyInfo() throws Exception {
		GetMapConfigurationRequest request = new GetMapConfigurationRequest();
		request.setApplicationId("appServerSideOnly");
		request.setMapId("mapServerSideOnly");
		GetMapConfigurationResponse response = (GetMapConfigurationResponse) dispatcher.execute(
				GetMapConfigurationRequest.COMMAND, request, null, "en");
		if (response.isError()) {
			response.getErrors().get(0).printStackTrace();
		}
		Assert.assertFalse(response.isError());
		ClientMapInfo mapInfo = response.getMapInfo();
		Assert.assertNotNull(mapInfo);

		// user data
		ClientUserDataInfo info = mapInfo.getUserData();
		Assert.assertNull(info);

		// widget data
		Assert.assertNotNull(mapInfo.getWidgetInfo());
		Assert.assertNull(mapInfo.getWidgetInfo("appDummy")); // not present
		Assert.assertNotNull(mapInfo.getWidgetInfo("layerTree")); // present
		Assert.assertNull(mapInfo.getWidgetInfo("mapDummy")); // filtered because ServerSideOnlyInfo
		
		// ViewBounds LimitOption
		Assert.assertEquals(BoundsLimitOption.COMPLETELY_WITHIN_MAX_BOUNDS, 
				mapInfo.getViewBoundsLimitOption());
		
	}

}
