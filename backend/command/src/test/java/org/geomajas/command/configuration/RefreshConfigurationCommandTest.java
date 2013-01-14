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

package org.geomajas.command.configuration;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.dto.RefreshConfigurationRequest;
import org.geomajas.command.dto.RefreshConfigurationResponse;
import org.geomajas.service.TestRecorder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test for {@link RefreshConfigurationCommand}.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/layerCountries.xml", "/org/geomajas/testdata/simplevectorsContext.xml",
		"/org/geomajas/spring/testRecorder.xml"})
public class RefreshConfigurationCommandTest {

	@Autowired
	private CommandDispatcher dispatcher;

	@Autowired
	private TestRecorder recorder;

	@Test
	@DirtiesContext
	public void testRefreshConfiguration() throws Exception {
		Assert.assertEquals("", recorder.matches("test", "postConstruct"));
		recorder.clear();

		RefreshConfigurationRequest request = new RefreshConfigurationRequest();
		request.setConfigLocations(new String[] {});
		RefreshConfigurationResponse response = (RefreshConfigurationResponse) dispatcher.execute(
				RefreshConfigurationRequest.COMMAND, request, null, "en");
		/* @todo test commented because test runner does not build a ReconfigurableApplicationContext
		if (response.isError()) {
			response.getErrors().get(0).printStackTrace();
		}
		Assert.assertFalse(response.isError());
		Assert.assertEquals("", recorder.matches("test", "postConstruct"));
		*/
	}
}
