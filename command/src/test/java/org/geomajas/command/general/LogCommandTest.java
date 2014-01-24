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

package org.geomajas.command.general;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.LogRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test for {@link LogCommand}.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/layerCountries.xml", "/org/geomajas/testdata/simplevectorsContext.xml"})
public class LogCommandTest {

	@Autowired
	private CommandDispatcher dispatcher;

	@Test
	public void testLog() throws Exception {
		LogRequest request = new LogRequest();
		request.setLevel(LogRequest.LEVEL_INFO);
		request.setStatement("Test log command");
		CommandResponse response = dispatcher.execute(LogRequest.COMMAND, request, null, "en");
		if (response.isError()) {
			response.getErrors().get(0).printStackTrace();
		}
		Assert.assertFalse(response.isError());

		request.setLevel(LogRequest.LEVEL_DEBUG);
		request.setStatement("Test debug log command");
		response = dispatcher.execute(LogRequest.COMMAND, request, null, "en");
		if (response.isError()) {
			response.getErrors().get(0).printStackTrace();
		}
		Assert.assertFalse(response.isError());

		request.setLevel(LogRequest.LEVEL_WARN);
		request.setStatement("Test warn log command");
		response = dispatcher.execute(LogRequest.COMMAND, request, null, "en");
		if (response.isError()) {
			response.getErrors().get(0).printStackTrace();
		}
		Assert.assertFalse(response.isError());

		request.setLevel(LogRequest.LEVEL_ERROR);
		request.setStatement("Test error log command");
		response = dispatcher.execute(LogRequest.COMMAND, request, null, "en");
		if (response.isError()) {
			response.getErrors().get(0).printStackTrace();
		}
		Assert.assertFalse(response.isError());
	}

}
