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
import org.geomajas.plugin.deskmanager.command.manager.dto.CheckLayerModelInUseRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.CheckLayerModelInUseResponse;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;
import org.geomajas.plugin.deskmanager.service.common.LayerModelService;
import org.geomajas.plugin.deskmanager.test.LoginBeforeTestingWithPredefinedProfileBase;
import org.geomajas.plugin.deskmanager.test.service.ExampleDatabaseProvisioningServiceImpl;
import org.geomajas.security.GeomajasSecurityException;
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
public class CheckLayerModelInUseCommandTest extends LoginBeforeTestingWithPredefinedProfileBase {

	@Autowired
	private CommandDispatcher dispatcher;

	@Autowired
	private LayerModelService layerModelService;

	private String layerModelId;

	@Override
	protected Role getRoleToLoginWithBeforeTesting() {
		return Role.ADMINISTRATOR;
	}

	@Before
	public void setup() throws Exception {
		// get a layerModel Id , but not the osm layer.
		// The OSM layer might be added due to a map in manager territory section.
		if (!layerModelService.getLayerModelsInternal().get(0).getClientLayerId().equals("clientLayerOsm")) {
			layerModelId = layerModelService.getLayerModelsInternal().get(0).getId();
		} else {
			layerModelId = layerModelService.getLayerModelsInternal().get(1).getId();
		}
	}

	@Test
	public void testLayerModelInUse() {
		CheckLayerModelInUseRequest request = new CheckLayerModelInUseRequest();
		request.setLayerModelId(layerModelId);

		CheckLayerModelInUseResponse response = (CheckLayerModelInUseResponse) dispatcher.execute(
				CheckLayerModelInUseRequest.COMMAND, request, getTokenOfLoggedInBeforeTesting(), "en");

		Assert.assertTrue(response.getErrors().isEmpty());
		Assert.assertEquals(true, response.isLayerModelInUse());
	}

	@Test
	public void testLayerModelNotInUse() {
		CheckLayerModelInUseRequest request = new CheckLayerModelInUseRequest();
		request.setLayerModelId("thequickbrownfoxjumpsoverthelazydog");

		CheckLayerModelInUseResponse response = (CheckLayerModelInUseResponse) dispatcher.execute(
				CheckLayerModelInUseRequest.COMMAND, request, getTokenOfLoggedInBeforeTesting(), "en");

		Assert.assertTrue(response.getErrors().isEmpty());
		Assert.assertEquals(false, response.isLayerModelInUse());
	}

	@Test
	public void testCommandNotAllowed() {
		CheckLayerModelInUseRequest request = new CheckLayerModelInUseRequest();
		request.setLayerModelId(ExampleDatabaseProvisioningServiceImpl.GEODESK_TEST_BE);

		CommandResponse response = dispatcher.execute(CheckLayerModelInUseRequest.COMMAND, request,
				getToken(Role.GUEST), "en");

		Assert.assertFalse(response.getErrors().isEmpty());
		Assert.assertEquals(GeomajasSecurityException.class.getName(), response.getExceptions().get(0).getClassName());
	}

}
