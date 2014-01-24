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
package org.geomajas.plugin.deskmanager.test.command.security;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.plugin.deskmanager.command.security.dto.RetrieveRolesRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.RetrieveRolesResponse;
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
public class RetrieveRolesCommandTest {

	@Autowired
	private CommandDispatcher dispatcher;

	@Test
	public void testGetRoles() {
		RetrieveRolesRequest request = new RetrieveRolesRequest();
		request.setGeodeskId(RetrieveRolesRequest.MANAGER_ID);

		RetrieveRolesResponse response = (RetrieveRolesResponse) dispatcher.execute(RetrieveRolesRequest.COMMAND,
				request, null, "en");

		Assert.assertTrue(response.getErrors().isEmpty());
		Assert.assertNotNull(response.getRoles());
		Assert.assertTrue(response.getRoles().size() > 0);

	}

	@Test
	public void testNoGeodeskId() {
		RetrieveRolesRequest request = new RetrieveRolesRequest();

		RetrieveRolesResponse response = (RetrieveRolesResponse) dispatcher.execute(RetrieveRolesRequest.COMMAND,
				request, null, "en");

		Assert.assertFalse(response.getErrors().isEmpty());
		Assert.assertEquals(IllegalArgumentException.class, response.getErrors().get(0).getClass());
	}

}
