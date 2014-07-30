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
package org.geomajas.plugin.deskmanager.test.command.geodesk;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.plugin.deskmanager.command.geodesk.dto.InitializeGeodeskRequest;
import org.geomajas.plugin.deskmanager.command.geodesk.dto.InitializeGeodeskResponse;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;
import org.geomajas.plugin.deskmanager.test.LoginBeforeTestingWithPredefinedProfileBase;
import org.geomajas.plugin.deskmanager.test.service.ExampleDatabaseProvisioningServiceImpl;
import org.geomajas.security.GeomajasSecurityException;
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
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/deskmanager/spring/**/*.xml", "/applicationContext.xml" })
public class InitializeGeodeskCommandTest extends LoginBeforeTestingWithPredefinedProfileBase {

	@Autowired
	private CommandDispatcher dispatcher;

	@Override
	protected Role getRoleToLoginWithBeforeTesting() {
		return null;
	}

	@Test
	public void testGetPublicConfiguration() {
		InitializeGeodeskRequest request = new InitializeGeodeskRequest();
		request.setGeodeskId(ExampleDatabaseProvisioningServiceImpl.GEODESK_TEST_BE);

		InitializeGeodeskResponse response = (InitializeGeodeskResponse) dispatcher.execute(
				InitializeGeodeskResponse.COMMAND, request, getToken(Role.GUEST), "en");

		Assert.assertTrue(response.getErrorMessages().isEmpty());

		Assert.assertEquals(ExampleDatabaseProvisioningServiceImpl.GEODESK_TEST_BE, response.getGeodeskIdentifier());
		Assert.assertEquals("${buildNumber}", response.getDeskmanagerBuild());
		Assert.assertEquals("${project.version}", response.getDeskmanagerVersion());
		Assert.assertEquals(ExampleDatabaseProvisioningServiceImpl.CLIENTAPPLICATION_ID,
				response.getUserApplicationKey());

		Assert.assertNotNull(response.getClientApplicationInfo());
		Assert.assertTrue(response.getClientApplicationInfo().getMaps().isEmpty());
	}

	@Test
	public void testGetPrivateConfiguration() {
		InitializeGeodeskRequest request = new InitializeGeodeskRequest();
		request.setGeodeskId(ExampleDatabaseProvisioningServiceImpl.GEODESK_TEST_DE_PRIVATE);

		InitializeGeodeskResponse response = (InitializeGeodeskResponse) dispatcher.execute(
				InitializeGeodeskResponse.COMMAND, request, getToken(Role.GUEST), "en");

		Assert.assertFalse(response.getErrorMessages().isEmpty());
		Assert.assertEquals(GeomajasSecurityException.class.getName(), response.getExceptions().get(0).getClassName());
	}
}
