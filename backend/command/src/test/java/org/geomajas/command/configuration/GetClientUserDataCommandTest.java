/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.command.configuration;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.dto.GetClientUserDataRequest;
import org.geomajas.command.dto.GetClientUserDataResponse;
import org.geomajas.testdata.ClientUserDataObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Testcase for the GetClientUserDataCommand command.
 * 
 * @author Pieter De Graef
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/clientUserDataContext.xml" })
public class GetClientUserDataCommandTest {

	private static final String ALLOWED_BEAN_ID = "myClientUserData";

	private static final String DISALLOWED_BEAN_ID = "myOtherData";

	@Autowired
	private CommandDispatcher dispatcher;

	@Test
	public void testFetchCorrectObject() {
		GetClientUserDataRequest request = new GetClientUserDataRequest();
		request.setIdentifier(ALLOWED_BEAN_ID);
		request.setClassName(ClientUserDataObject.class.getName());

		GetClientUserDataResponse response = (GetClientUserDataResponse) dispatcher.execute(
				GetClientUserDataRequest.COMMAND, request, null, "en");
		if (response.isError()) {
			response.getErrors().get(0).printStackTrace();
		}
		Assert.assertFalse(response.isError());
		Assert.assertNotNull(response.getInformation());
	}

	@Test
	public void testFetchWrongObject() {
		GetClientUserDataRequest request = new GetClientUserDataRequest();
		request.setIdentifier(DISALLOWED_BEAN_ID);
		request.setClassName("org.geomajas.configuration.RasterLayerInfo");

		GetClientUserDataResponse response = (GetClientUserDataResponse) dispatcher.execute(
				GetClientUserDataRequest.COMMAND, request, null, "en");
		if (response.isError()) {
			response.getErrors().get(0).printStackTrace();
		}
		Assert.assertTrue(response.isError());
		Assert.assertNull(response.getInformation());
	}
}