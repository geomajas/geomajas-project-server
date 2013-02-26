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
package org.geomajas.plugin.deskmanager.test.command;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.dto.GetConfigurationRequest;
import org.geomajas.command.dto.GetMapConfigurationRequest;
import org.geomajas.command.dto.GetMapConfigurationResponse;
import org.geomajas.plugin.deskmanager.client.gwt.common.GdmLayout;
import org.geomajas.plugin.deskmanager.command.security.dto.RetrieveRolesRequest;
import org.geomajas.plugin.deskmanager.security.DeskmanagerSecurityService;
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
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Oliver May
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/deskmanager/spring/**/*.xml", "/applicationContext.xml" })
@Transactional
public class GetMapConfigurationCommandTest {

	@Autowired
	private SecurityService securityService;

	@Autowired
	private SecurityManager securityManager;

	@Autowired
	private CommandDispatcher dispatcher;

	private String userToken;

	private String guestToken;

	@Before
	public void setup() throws Exception {
		guestToken = ((DeskmanagerSecurityService) securityService).registerRole(RetrieveRolesRequest.MANAGER_ID,
				DeskmanagerSecurityService.createGuestProfile());
		// Log in
		securityManager.createSecurityContext(userToken);
	}

	@Test
	public void testGetPublicConfiguration() {
		GetMapConfigurationRequest request = new GetMapConfigurationRequest();
		request.setApplicationId(ExampleDatabaseProvisioningServiceImpl.GEODESK_TEST_BE);
		request.setMapId(GdmLayout.MAPMAIN_ID);

		GetMapConfigurationResponse response = (GetMapConfigurationResponse) dispatcher.execute(
				GetConfigurationRequest.COMMAND, request, guestToken, "en");

		Assert.assertTrue(response.getErrorMessages().isEmpty());
		Assert.assertNotNull(response.getMapInfo());
	}

	@Test
	public void testGetPrivateConfiguration() {
		GetMapConfigurationRequest request = new GetMapConfigurationRequest();
		request.setApplicationId(ExampleDatabaseProvisioningServiceImpl.GEODESK_TEST_DE_PRIVATE);
		request.setMapId(GdmLayout.MAPMAIN_ID);

		GetMapConfigurationResponse response = (GetMapConfigurationResponse) dispatcher.execute(
				GetConfigurationRequest.COMMAND, request, guestToken, "en");

		Assert.assertFalse(response.getErrorMessages().isEmpty());
		Assert.assertEquals(response.getExceptions().get(0).getClassName(), GeomajasSecurityException.class.getName());
	}
}
