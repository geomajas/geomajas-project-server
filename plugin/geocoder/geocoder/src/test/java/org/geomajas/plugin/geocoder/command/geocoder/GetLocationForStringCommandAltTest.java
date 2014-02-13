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
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringAlternative;
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringRequest;
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringResponse;
import org.junit.Ignore;
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
@Ignore //FIXME: GC-54
public class GetLocationForStringCommandAltTest {

	private static final double DELTA = 1e-20;
	// @extract-start UseCommandTest, Usage of the geocoder command
	@Autowired
	private CommandDispatcher commandDispatcher;

	@Test
	public void oneResultTest() throws Exception {
		GetLocationForStringRequest request = new GetLocationForStringRequest();
		request.setCrs("EPSG:4326");
		request.setLocation("booischot");

		CommandResponse commandResponse = commandDispatcher.execute(GetLocationForStringRequest.COMMAND, request, null,
				"en");
		// @extract-skip-start
		Assert.assertNotNull(commandResponse);
		Assert.assertTrue(commandResponse instanceof GetLocationForStringResponse);
		GetLocationForStringResponse response = (GetLocationForStringResponse) commandResponse;
		Assert.assertTrue(response.isLocationFound());
		Assert.assertEquals("Booischot, BE", response.getCanonicalLocation());
		Assert.assertNotNull(response.getCenter());
		Assert.assertNotNull(response.getBbox());
		Assert.assertEquals(4.7751, response.getCenter().getX(), DELTA);
		Assert.assertEquals(51.05219, response.getCenter().getY(), DELTA);
		Assert.assertNull(response.getAlternatives());
		Assert.assertEquals("GeoNames", response.getGeocoderName());
		// @extract-skip-end
	}
	// @extract-end

	@Test
	public void oneResultCrsTest() throws Exception {
		GetLocationForStringRequest request = new GetLocationForStringRequest();
		request.setCrs("EPSG:900913");
		request.setLocation("booischot");

		CommandResponse commandResponse = commandDispatcher.execute(GetLocationForStringRequest.COMMAND, request, null,
				"en");
		Assert.assertNotNull(commandResponse);
		Assert.assertTrue(commandResponse instanceof GetLocationForStringResponse);
		GetLocationForStringResponse response = (GetLocationForStringResponse) commandResponse;
		Assert.assertTrue(response.isLocationFound());
		Assert.assertEquals("Booischot, BE", response.getCanonicalLocation());
		Assert.assertNotNull(response.getCenter());
		Assert.assertNotNull(response.getBbox());
		Assert.assertEquals(531561.7004869606, response.getCenter().getX(), DELTA);
		Assert.assertEquals(6630530.727245597, response.getCenter().getY(), DELTA);
		Assert.assertNull(response.getAlternatives());
		Assert.assertEquals("GeoNames", response.getGeocoderName());
	}

	@Test
	public void alternativesTest() throws Exception {
		GetLocationForStringRequest request = new GetLocationForStringRequest();
		request.setCrs("EPSG:4326");
		request.setLocation("London, GB");

		CommandResponse commandResponse = commandDispatcher.execute(GetLocationForStringRequest.COMMAND, request, null,
				"en");
		Assert.assertNotNull(commandResponse);
		Assert.assertTrue(commandResponse instanceof GetLocationForStringResponse);
		GetLocationForStringResponse response = (GetLocationForStringResponse) commandResponse;
		Assert.assertFalse(response.isLocationFound());
		Assert.assertNotNull(response.getAlternatives());
		Assert.assertTrue(response.getAlternatives().size() > 0);
		GetLocationForStringAlternative alt = response.getAlternatives().get(0);
		Assert.assertEquals(-0.12574, alt.getCenter().getX(), DELTA);
		Assert.assertEquals(51.50853, alt.getCenter().getY(), DELTA);
		Assert.assertEquals("GeoNames", alt.getGeocoderName());
	}

	@Test
	public void alternativesCrsTest() throws Exception {
		GetLocationForStringRequest request = new GetLocationForStringRequest();
		request.setCrs("EPSG:900913");
		request.setLocation("London, GB");

		CommandResponse commandResponse = commandDispatcher.execute(GetLocationForStringRequest.COMMAND, request, null,
				"en");
		Assert.assertNotNull(commandResponse);
		Assert.assertTrue(commandResponse instanceof GetLocationForStringResponse);
		GetLocationForStringResponse response = (GetLocationForStringResponse) commandResponse;
		Assert.assertFalse(response.isLocationFound());
		Assert.assertNotNull(response.getAlternatives());
		Assert.assertTrue(response.getAlternatives().size() > 0);
		GetLocationForStringAlternative alt = response.getAlternatives().get(0);
		Assert.assertEquals(-13997.312772346217, alt.getCenter().getX(), DELTA);
		Assert.assertEquals(6711744.580491004, alt.getCenter().getY(), DELTA);
		Assert.assertEquals("GeoNames", alt.getGeocoderName());
	}

}