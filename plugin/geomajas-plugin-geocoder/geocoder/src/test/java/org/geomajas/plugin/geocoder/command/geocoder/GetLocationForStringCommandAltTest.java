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

package org.geomajas.plugin.geocoder.command.geocoder;

import junit.framework.Assert;
import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandResponse;
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringRequest;
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test for GetLocationForStringCommand command, with explicit combiner.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/completeAltContext.xml"})
public class GetLocationForStringCommandAltTest {

	private static final double DELTA = 1e-20;
	private static final String COMMAND = "command.geocoder.GetLocationForString";

	@Autowired
	private CommandDispatcher commandDispatcher;

	@Test
	public void oneBboxTest() throws Exception {
		GetLocationForStringRequest request = new GetLocationForStringRequest();
		request.setCrs("EPSG:900913");
		request.setLocation("one");

		CommandResponse commandResponse = commandDispatcher.execute(COMMAND, request, null, "en");
		Assert.assertNotNull(commandResponse);
		Assert.assertTrue(commandResponse instanceof GetLocationForStringResponse);
		GetLocationForStringResponse response = (GetLocationForStringResponse)commandResponse;
		Assert.assertTrue(response.isLocationFound());
		Assert.assertEquals("one", response.getMatchedLocation());
		Assert.assertNotNull(response.getCenter());
		Assert.assertNotNull(response.getBbox());
		Assert.assertEquals(50000, response.getCenter().getX(), DELTA);
		Assert.assertEquals(50000, response.getCenter().getY(), DELTA);
		Assert.assertEquals(0, response.getBbox().getX(), DELTA);
		Assert.assertEquals(0, response.getBbox().getY(), DELTA);
		Assert.assertEquals(100000, response.getBbox().getWidth(), DELTA);
		Assert.assertEquals(100000, response.getBbox().getHeight(), DELTA);
	}

	@Test
	public void combineBboxTest() throws Exception {
		GetLocationForStringRequest request = new GetLocationForStringRequest();
		request.setCrs("EPSG:900913");
		request.setLocation("bla");

		CommandResponse commandResponse = commandDispatcher.execute(COMMAND, request, null, "en");
		Assert.assertNotNull(commandResponse);
		Assert.assertTrue(commandResponse instanceof GetLocationForStringResponse);
		GetLocationForStringResponse response = (GetLocationForStringResponse)commandResponse;
		Assert.assertTrue(response.isLocationFound());
		Assert.assertEquals("bla", response.getMatchedLocation());
		Assert.assertNotNull(response.getCenter());
		Assert.assertNotNull(response.getBbox());
		Assert.assertEquals(87500, response.getCenter().getX(), DELTA);
		Assert.assertEquals(90000, response.getCenter().getY(), DELTA);
		Assert.assertEquals(75000, response.getBbox().getX(), DELTA);
		Assert.assertEquals(85000, response.getBbox().getY(), DELTA);
		Assert.assertEquals(25000, response.getBbox().getWidth(), DELTA);
		Assert.assertEquals(10000, response.getBbox().getHeight(), DELTA);
	}

}
