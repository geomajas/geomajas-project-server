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
package org.geomajas.command.render;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.RegisterNamedStyleInfoRequest;
import org.geomajas.command.dto.RegisterNamedStyleInfoResponse;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.service.StyleService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Oliver May
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/layerCountries.xml", "/org/geomajas/testdata/simplevectorsContext.xml"})
public class RegisterNamedStyleInfoCommandTest {

	@Autowired
	private CommandDispatcher dispatcher;

	@Autowired
	private StyleService styleService;

	@Test
	public void testRegisterStyle() throws Exception {
		NamedStyleInfo nsi = new NamedStyleInfo();
		nsi.setName("test");

		RegisterNamedStyleInfoRequest req = new RegisterNamedStyleInfoRequest();
		req.setLayerId("countries");
		req.setNamedStyleInfo(nsi);

		CommandResponse response = dispatcher.execute(
				RegisterNamedStyleInfoRequest.COMMAND, req, null, "en");
		
		if (response.isError()) {
			response.getErrors().get(0).printStackTrace();
		}
		Assert.assertFalse(response.isError());
		Assert.assertTrue(response instanceof RegisterNamedStyleInfoResponse);
		Assert.assertEquals(nsi, styleService.retrieveStyle("countries", ((RegisterNamedStyleInfoResponse) response).getStyleName()));

	}
}
