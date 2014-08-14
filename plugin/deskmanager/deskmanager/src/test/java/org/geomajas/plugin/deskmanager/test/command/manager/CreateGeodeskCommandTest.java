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
package org.geomajas.plugin.deskmanager.test.command.manager;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.CreateGeodeskRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GeodeskResponse;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;
import org.geomajas.plugin.deskmanager.service.common.BlueprintService;
import org.geomajas.plugin.deskmanager.test.LoginBeforeTestingWithPredefinedProfileBase;
import org.geomajas.plugin.deskmanager.test.security.StubProfileService;
import org.geomajas.security.GeomajasSecurityException;
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
public class CreateGeodeskCommandTest extends LoginBeforeTestingWithPredefinedProfileBase {

	@Autowired
	private CommandDispatcher dispatcher;
	
	@Autowired
	private BlueprintService blueprintService;

	private String blueprintId;

	@Override
	protected Role getRoleToLoginWithBeforeTesting() {
		return Role.ADMINISTRATOR;
	}

	@Before
	public void setup() throws Exception {
		blueprintId = blueprintService.getBlueprintsInternal().get(0).getId();
	}

	@Test
	@Transactional
	public void testCreateGeodesk() {
		CreateGeodeskRequest request = new CreateGeodeskRequest();
		request.setName("TEST name");
		request.setBlueprintId(blueprintId);
		
		GeodeskResponse response = (GeodeskResponse) dispatcher.execute(CreateGeodeskRequest.COMMAND, request,
				getTokenOfLoggedInBeforeTesting(), "en");
		
		Assert.assertTrue(response.getErrors().isEmpty());
		Assert.assertEquals(request.getName(), response.getGeodesk().getName());
		Assert.assertEquals(request.getBlueprintId(), response.getGeodesk().getBlueprint().getId());
	}

	/**
	 * Test security.
	 */
	@Test
	public void testNotAllowed() {
		CommandResponse response = dispatcher.execute(CreateGeodeskRequest.COMMAND, new CreateGeodeskRequest(),
				getToken(Role.GUEST), "en");

		Assert.assertFalse(response.getExceptions().isEmpty());
		Assert.assertEquals(GeomajasSecurityException.class.getName(), response.getExceptions().get(0).getClassName());
	}
}
