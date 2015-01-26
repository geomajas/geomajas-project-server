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
import org.geomajas.command.dto.UserMaximumExtentRequest;
import org.geomajas.command.dto.UserMaximumExtentResponse;
import org.geomajas.geometry.Bbox;
import org.geomajas.testdata.ReloadContext;
import org.geomajas.testdata.ReloadContextTestExecutionListener;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * Test for {@link UserMaximumExtentCommand}.
 * 
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/layerCountries.xml", "/org/geomajas/testdata/simplevectorsContext.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, ReloadContextTestExecutionListener.class })
@ReloadContext
public class UserMaximumExtentCommandTest {

	private static final double DOUBLE_TOLERANCE = .0000000001;

	private static final String LAYER_ID = "countries";

	private static final String CRS = "EPSG:4326";

	@Autowired
	private CommandDispatcher dispatcher;

	@Test
	public void testUserMaximumExtent() throws Exception {
		UserMaximumExtentRequest request = new UserMaximumExtentRequest();
		request.setCrs(CRS);
		request.setLayerIds(new String[] { LAYER_ID });
		UserMaximumExtentResponse response = (UserMaximumExtentResponse) dispatcher.execute(
				UserMaximumExtentRequest.COMMAND, request, null, "en");
		if (response.isError()) {
			response.getErrors().get(0).printStackTrace();
		}
		Assert.assertFalse(response.isError());
		Bbox bounds = response.getBounds();
		Assert.assertNotNull(bounds);
		Assert.assertEquals(-1.0, bounds.getX(), DOUBLE_TOLERANCE);
		Assert.assertEquals(-1.0, bounds.getY(), DOUBLE_TOLERANCE);
		Assert.assertEquals(1.0, bounds.getMaxX(), DOUBLE_TOLERANCE);
		Assert.assertEquals(1.0, bounds.getMaxY(), DOUBLE_TOLERANCE);
	}
}
