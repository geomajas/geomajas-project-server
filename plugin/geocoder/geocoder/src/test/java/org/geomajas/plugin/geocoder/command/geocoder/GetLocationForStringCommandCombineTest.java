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
		"/completeCombineContext.xml"})
public class GetLocationForStringCommandCombineTest {

	private static final double DELTA = 1e-20;

	@Autowired
	private CommandDispatcher commandDispatcher;

	@Test
	public void oneBboxTest() throws Exception {
		GetLocationForStringRequest request = new GetLocationForStringRequest();
		request.setCrs("EPSG:900913");
		request.setLocation("one");

		CommandResponse commandResponse = commandDispatcher.execute(GetLocationForStringRequest.COMMAND, request, null,
				"en");
		Assert.assertNotNull(commandResponse);
		Assert.assertTrue(commandResponse instanceof GetLocationForStringResponse);
		GetLocationForStringResponse response = (GetLocationForStringResponse)commandResponse;
		Assert.assertTrue(response.isLocationFound());
		Assert.assertEquals("one", response.getCanonicalLocation());
		Assert.assertNotNull(response.getCenter());
		Assert.assertNotNull(response.getBbox());
		Assert.assertEquals(50000, response.getCenter().getX(), DELTA);
		Assert.assertEquals(50000, response.getCenter().getY(), DELTA);
		Assert.assertEquals(0, response.getBbox().getX(), DELTA);
		Assert.assertEquals(0, response.getBbox().getY(), DELTA);
		Assert.assertEquals(100000, response.getBbox().getWidth(), DELTA);
		Assert.assertEquals(100000, response.getBbox().getHeight(), DELTA);
		Assert.assertEquals("static-regex", response.getGeocoderName());
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
		Assert.assertEquals(87500, response.getCenter().getX(), DELTA);
		Assert.assertEquals(90000, response.getCenter().getY(), DELTA);
		Assert.assertEquals(75000, response.getBbox().getX(), DELTA);
		Assert.assertEquals(85000, response.getBbox().getY(), DELTA);
		Assert.assertEquals(25000, response.getBbox().getWidth(), DELTA);
		Assert.assertEquals(10000, response.getBbox().getHeight(), DELTA);
		Assert.assertNull(response.getGeocoderName());
	}

}
