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
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.command.dto.GetMapConfigurationRequest;
import org.geomajas.command.dto.GetMapConfigurationResponse;
import org.geomajas.geometry.Bbox;
import org.geomajas.layer.Layer;
import org.geomajas.service.ConfigurationService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Verify the getting of the map configuration (and especially the transformation of maxExtent coordinates).
 *
 * @author Joachim Van der Auwera
 */
public class GetMapConfigurationCommandTest {

	private static final double DOUBLE_TOLERANCE = .0000000001;

	@Test
	public void testConvertMaxExtent() throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				new String[] {"org/geomajas/spring/geomajasContext.xml",
						"org/geomajas/testdata/layerCountries.xml",
						"org/geomajas/testdata/simplevectorsContext.xml"});
		// first verify that the configuration is as expected
		ConfigurationService configurationService = applicationContext.getBean(
				"service.ConfigurationService", ConfigurationService.class);
		Layer layer = configurationService.getLayer("countries");
		Bbox configMaxExtent = layer.getLayerInfo().getMaxExtent();
		Assert.assertEquals(-85.05112877980659, configMaxExtent.getX(), DOUBLE_TOLERANCE);
		Assert.assertEquals(-85.05112877980659, configMaxExtent.getY(), DOUBLE_TOLERANCE);
		Assert.assertEquals(170.102257, configMaxExtent.getWidth(), DOUBLE_TOLERANCE);
		Assert.assertEquals(170.102257, configMaxExtent.getHeight(), DOUBLE_TOLERANCE);

		GetMapConfigurationRequest request = new GetMapConfigurationRequest();
		request.setApplicationId("simplevectors");
		request.setMapId("coordTestMap");
		CommandDispatcher dispatcher = applicationContext.getBean("command.CommandDispatcher", CommandDispatcher.class);
		GetMapConfigurationResponse response = (GetMapConfigurationResponse) dispatcher.execute(
				"command.configuration.GetMap", request, null, "en");
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
	}

}
