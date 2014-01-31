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
		"/completeContext.xml"})
public class GetLocationForStringCommandTest {

	private static final double DELTA = 1e-20;

	@Autowired
	private CommandDispatcher commandDispatcher;

	@Test
	public void expandPointTest() throws Exception {
		GetLocationForStringRequest request = new GetLocationForStringRequest();
		request.setCrs("EPSG:900913");
		request.setLocation("booischot");

		CommandResponse commandResponse = commandDispatcher.execute(GetLocationForStringRequest.COMMAND, request, null,
				"en");
		Assert.assertNotNull(commandResponse);
		Assert.assertTrue(commandResponse instanceof GetLocationForStringResponse);
		GetLocationForStringResponse response = (GetLocationForStringResponse)commandResponse;
		Assert.assertTrue(response.isLocationFound());
		Assert.assertEquals("booischot", response.getCanonicalLocation());
		Assert.assertNotNull(response.getCenter());
		Assert.assertNotNull(response.getBbox());
		Assert.assertEquals(621468.063486916, response.getCenter().getX(), DELTA);
		Assert.assertEquals(5706881.117852388, response.getCenter().getY(), DELTA);
		Assert.assertEquals(621325.5343735645, response.getBbox().getX(), DELTA);
		Assert.assertEquals(5706809.617868648, response.getBbox().getY(), DELTA);
		Assert.assertEquals(285.05822670296766, response.getBbox().getWidth(), DELTA);
		Assert.assertEquals(142.99996748007834, response.getBbox().getHeight(), DELTA);
		Assert.assertEquals("static-regex", response.getGeocoderName());
		Assert.assertNotNull(response.getUserData());
		Assert.assertEquals("schotbooi", ((UserDataTestInfo)response.getUserData()).getValue());
	}

	@Test
	public void crsTest() throws Exception {
		GetLocationForStringRequest request = new GetLocationForStringRequest();
		request.setCrs("EPSG:4326");
		request.setLocation("Booischot");

		CommandResponse commandResponse = commandDispatcher.execute(GetLocationForStringRequest.COMMAND, request, null,
				"en");
		Assert.assertNotNull(commandResponse);
		Assert.assertTrue(commandResponse instanceof GetLocationForStringResponse);
		GetLocationForStringResponse response = (GetLocationForStringResponse)commandResponse;
		Assert.assertTrue(response.isLocationFound());
		Assert.assertEquals("Booischot", response.getCanonicalLocation());
		Assert.assertNotNull(response.getCenter());
		Assert.assertNotNull(response.getBbox());
		Assert.assertEquals(5.582742600224577, response.getCenter().getX(), DELTA);
		Assert.assertEquals(45.53964302945367, response.getCenter().getY(), DELTA);
	}

	@Test
	public void secondServiceTest() throws Exception {
		GetLocationForStringRequest request = new GetLocationForStringRequest();
		request.setCrs("EPSG:900913");
		request.setLocation("second");

		CommandResponse commandResponse = commandDispatcher.execute(GetLocationForStringRequest.COMMAND, request, null,
				"en");
		Assert.assertNotNull(commandResponse);
		Assert.assertTrue(commandResponse instanceof GetLocationForStringResponse);
		GetLocationForStringResponse response = (GetLocationForStringResponse)commandResponse;
		Assert.assertTrue(response.isLocationFound());
		Assert.assertEquals("secondService", response.getCanonicalLocation());
		Assert.assertNotNull(response.getCenter());
		Assert.assertNotNull(response.getBbox());
		Assert.assertEquals(10000, response.getCenter().getX(), DELTA);
		Assert.assertEquals(10000, response.getCenter().getY(), DELTA);
		Assert.assertEquals("static-regex", response.getGeocoderName());
		Assert.assertNull(response.getUserData());
	}

	@Test
	public void bboxTest() throws Exception {
		GetLocationForStringRequest request = new GetLocationForStringRequest();
		request.setCrs("EPSG:900913");
		request.setLocation("bbox");

		CommandResponse commandResponse = commandDispatcher.execute(GetLocationForStringRequest.COMMAND, request, null,
				"en");
		Assert.assertNotNull(commandResponse);
		Assert.assertTrue(commandResponse instanceof GetLocationForStringResponse);
		GetLocationForStringResponse response = (GetLocationForStringResponse)commandResponse;
		Assert.assertTrue(response.isLocationFound());
		Assert.assertEquals("bbox", response.getCanonicalLocation());
		Assert.assertNotNull(response.getCenter());
		Assert.assertNotNull(response.getBbox());
		Assert.assertEquals(50000, response.getCenter().getX(), DELTA);
		Assert.assertEquals(90000, response.getCenter().getY(), DELTA);
		Assert.assertEquals(0, response.getBbox().getX(), DELTA);
		Assert.assertEquals(50000, response.getBbox().getY(), DELTA);
		Assert.assertEquals(100000, response.getBbox().getWidth(), DELTA);
		Assert.assertEquals(80000, response.getBbox().getHeight(), DELTA);
		Assert.assertEquals("static-regex", response.getGeocoderName());
		Assert.assertNull(response.getUserData());
	}

	@Test
	public void combineBboxTest() throws Exception {
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
		Assert.assertEquals(57500, response.getCenter().getX(), DELTA);
		Assert.assertEquals(72500, response.getCenter().getY(), DELTA);
		Assert.assertEquals(30000, response.getBbox().getX(), DELTA);
		Assert.assertEquals(50000, response.getBbox().getY(), DELTA);
		Assert.assertEquals(55000, response.getBbox().getWidth(), DELTA);
		Assert.assertEquals(45000, response.getBbox().getHeight(), DELTA);
		Assert.assertNull(response.getGeocoderName());
	}

	@Test
	public void notFoundTest() throws Exception {
		GetLocationForStringRequest request = new GetLocationForStringRequest();
		request.setCrs("EPSG:900913");
		request.setLocation("other");

		CommandResponse commandResponse = commandDispatcher.execute(GetLocationForStringRequest.COMMAND, request, null,
				"en");
		Assert.assertNotNull(commandResponse);
		Assert.assertTrue(commandResponse instanceof GetLocationForStringResponse);
		GetLocationForStringResponse response = (GetLocationForStringResponse)commandResponse;
		Assert.assertFalse(response.isLocationFound());
		Assert.assertNull(response.getCanonicalLocation());
		Assert.assertNull(response.getGeocoderName());
	}

}
