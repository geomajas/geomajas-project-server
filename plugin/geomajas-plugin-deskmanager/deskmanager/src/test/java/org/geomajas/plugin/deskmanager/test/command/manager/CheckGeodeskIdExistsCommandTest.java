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
package org.geomajas.plugin.deskmanager.test.command.manager;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.CheckGeodeskIdExistsRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.CheckGeodeskIdExistsResponse;
import org.geomajas.plugin.deskmanager.command.security.dto.RetrieveRolesRequest;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;
import org.geomajas.plugin.deskmanager.security.DeskmanagerSecurityService;
import org.geomajas.plugin.deskmanager.security.ProfileService;
import org.geomajas.plugin.deskmanager.test.security.StubProfileService;
import org.geomajas.plugin.deskmanager.test.service.ExampleDatabaseProvisioningServiceImpl;
import org.geomajas.security.GeomajasSecurityException;
import org.geomajas.security.SecurityManager;
import org.geomajas.security.SecurityService;
import org.junit.Assert;
import org.junit.Before;
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
public class CheckGeodeskIdExistsCommandTest {

	@Autowired
	private SecurityService securityService;

	@Autowired
	private SecurityManager securityManager;

	@Autowired
	private CommandDispatcher dispatcher;

	@Autowired
	private ProfileService profileService;

	private String guestToken;

	private String managerToken;

	@Before
	public void setup() throws Exception {
		StubProfileService pService = (StubProfileService) profileService;

		managerToken = ((DeskmanagerSecurityService) securityService).registerRole(RetrieveRolesRequest.MANAGER_ID,
				pService.getProfileByRole(Role.DESK_MANAGER));
		guestToken = ((DeskmanagerSecurityService) securityService).registerRole(RetrieveRolesRequest.MANAGER_ID,
				DeskmanagerSecurityService.createGuestProfile());
		// Log in
		securityManager.createSecurityContext(guestToken);
	}

	@Test
	public void testGeodeskIdDoesNotExist() {
		CheckGeodeskIdExistsRequest request = new CheckGeodeskIdExistsRequest();
		request.setGeodeskId("thequickbrownfoxjumpsoverthelazydog");

		CheckGeodeskIdExistsResponse response = (CheckGeodeskIdExistsResponse) dispatcher.execute(
				CheckGeodeskIdExistsRequest.COMMAND, request, managerToken, "en");

		Assert.assertTrue(response.getErrors().isEmpty());
		Assert.assertEquals(false, response.getExists());
	}

	@Test
	public void testGeodeskIdExists() {
		CheckGeodeskIdExistsRequest request = new CheckGeodeskIdExistsRequest();
		request.setGeodeskId(ExampleDatabaseProvisioningServiceImpl.GEODESK_TEST_BE);

		CheckGeodeskIdExistsResponse response = (CheckGeodeskIdExistsResponse) dispatcher.execute(
				CheckGeodeskIdExistsRequest.COMMAND, request, managerToken, "en");

		Assert.assertTrue(response.getErrors().isEmpty());
		Assert.assertEquals(true, response.getExists());
	}

	@Test
	public void testCommandNotAllowed() {
		CheckGeodeskIdExistsRequest request = new CheckGeodeskIdExistsRequest();
		request.setGeodeskId(ExampleDatabaseProvisioningServiceImpl.GEODESK_TEST_BE);

		CommandResponse response = dispatcher.execute(CheckGeodeskIdExistsRequest.COMMAND, request, guestToken, "en");

		Assert.assertFalse(response.getErrors().isEmpty());
		Assert.assertEquals(response.getExceptions().get(0).getClassName(), GeomajasSecurityException.class.getName());
	}

}
