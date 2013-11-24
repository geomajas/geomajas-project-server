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

package org.geomajas.plugin.geocoder.command.geocoder;

import junit.framework.Assert;
import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandResponse;
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringRequest;
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringResponse;
import org.geomajas.plugin.geocoder.service.UserDataTestInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test for GetLocationForStringCommand command.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/nameSelectContext.xml"})
public class GetLocationForStringNameSelectTest {

	private static final double DELTA = 1e-20;

	@Autowired
	private CommandDispatcher commandDispatcher;

	@Test
	public void testDefault() throws Exception {
		GetLocationForStringRequest request = new GetLocationForStringRequest();
		request.setCrs("EPSG:900913");
		request.setLocation("bla");

		CommandResponse commandResponse = commandDispatcher.execute(GetLocationForStringRequest.COMMAND, request, null,
				"en");
		Assert.assertNotNull(commandResponse);
		Assert.assertTrue(commandResponse instanceof GetLocationForStringResponse);
		GetLocationForStringResponse response = (GetLocationForStringResponse)commandResponse;
		Assert.assertTrue(response.isLocationFound());
		Assert.assertEquals("bla", response.getCanonicalLocation());
		Assert.assertNotNull(response.getCenter());
		Assert.assertNotNull(response.getBbox());
		Assert.assertEquals(30, response.getCenter().getX(), DELTA);
		Assert.assertEquals(50, response.getCenter().getY(), DELTA);
		Assert.assertNull(response.getGeocoderName()); // null when two results
		Assert.assertNull(response.getUserData()); // null when two results
	}

	@Test
	public void testAlt1() throws Exception {
		GetLocationForStringRequest request = new GetLocationForStringRequest();
		request.setCrs("EPSG:900913");
		request.setLocation("bla");
		request.setServicePattern("alt1");

		CommandResponse commandResponse = commandDispatcher.execute(GetLocationForStringRequest.COMMAND, request, null,
				"en");
		Assert.assertNotNull(commandResponse);
		Assert.assertTrue(commandResponse instanceof GetLocationForStringResponse);
		GetLocationForStringResponse response = (GetLocationForStringResponse)commandResponse;
		Assert.assertTrue(response.isLocationFound());
		Assert.assertEquals("bla", response.getCanonicalLocation());
		Assert.assertNotNull(response.getCenter());
		Assert.assertNotNull(response.getBbox());
		Assert.assertEquals(30, response.getCenter().getX(), DELTA);
		Assert.assertEquals(50, response.getCenter().getY(), DELTA);
		Assert.assertNotNull(response.getUserData());
		Assert.assertEquals("alt1", response.getGeocoderName());
		Assert.assertEquals("alt1", ((UserDataTestInfo)response.getUserData()).getValue());
	}

	@Test
	public void testAlt2() throws Exception {
		GetLocationForStringRequest request = new GetLocationForStringRequest();
		request.setCrs("EPSG:900913");
		request.setLocation("bla");
		request.setServicePattern(".?.?.?2");

		CommandResponse commandResponse = commandDispatcher.execute(GetLocationForStringRequest.COMMAND, request, null,
				"en");
		Assert.assertNotNull(commandResponse);
		Assert.assertTrue(commandResponse instanceof GetLocationForStringResponse);
		GetLocationForStringResponse response = (GetLocationForStringResponse)commandResponse;
		Assert.assertTrue(response.isLocationFound());
		Assert.assertEquals("bla", response.getCanonicalLocation());
		Assert.assertNotNull(response.getCenter());
		Assert.assertNotNull(response.getBbox());
		Assert.assertEquals(30, response.getCenter().getX(), DELTA);
		Assert.assertEquals(50, response.getCenter().getY(), DELTA);
		Assert.assertEquals("alt2", response.getGeocoderName());
		Assert.assertNotNull(response.getUserData());
		Assert.assertEquals("alt2", ((UserDataTestInfo)response.getUserData()).getValue());
	}

	@Test
	public void testBoth() throws Exception {
		GetLocationForStringRequest request = new GetLocationForStringRequest();
		request.setCrs("EPSG:900913");
		request.setLocation("bla");
		request.setServicePattern("alt.?");

		CommandResponse commandResponse = commandDispatcher.execute(GetLocationForStringRequest.COMMAND, request, null,
				"en");
		Assert.assertNotNull(commandResponse);
		Assert.assertTrue(commandResponse instanceof GetLocationForStringResponse);
		GetLocationForStringResponse response = (GetLocationForStringResponse)commandResponse;
		Assert.assertTrue(response.isLocationFound());
		Assert.assertEquals("bla", response.getCanonicalLocation());
		Assert.assertNotNull(response.getCenter());
		Assert.assertNotNull(response.getBbox());
		Assert.assertEquals(30, response.getCenter().getX(), DELTA);
		Assert.assertEquals(50, response.getCenter().getY(), DELTA);
		Assert.assertNull(response.getGeocoderName());
		Assert.assertNull(response.getUserData());
	}

	@Test
	public void testNone() throws Exception {
		GetLocationForStringRequest request = new GetLocationForStringRequest();
		request.setCrs("EPSG:900913");
		request.setLocation("bla");
		request.setServicePattern("a");

		CommandResponse commandResponse = commandDispatcher.execute(GetLocationForStringRequest.COMMAND, request, null,
				"en");
		Assert.assertNotNull(commandResponse);
		Assert.assertTrue(commandResponse instanceof GetLocationForStringResponse);
		GetLocationForStringResponse response = (GetLocationForStringResponse)commandResponse;
		Assert.assertFalse(response.isLocationFound());
		Assert.assertNull(response.getGeocoderName());
		Assert.assertNull(response.getUserData());
	}

}
