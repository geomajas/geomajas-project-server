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
import org.geomajas.plugin.deskmanager.command.manager.dto.BlueprintResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetBlueprintRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetBlueprintsRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetBlueprintsResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetClientLayersRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetClientLayersResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.SaveBlueprintRequest;
import org.geomajas.plugin.deskmanager.domain.dto.BlueprintDto;
import org.geomajas.plugin.deskmanager.domain.dto.LayerDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;
import org.geomajas.plugin.deskmanager.test.LoginBeforeTestingWithPredefinedProfileBase;
import org.geomajas.plugin.deskmanager.test.general.MyClientWidgetInfo;
import org.geomajas.security.GeomajasSecurityException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Oliver May
 * 
 *         TODO: tests for all different save scenarios
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/deskmanager/spring/**/*.xml", "/applicationContext.xml" })
public class SaveBlueprintCommandTest extends LoginBeforeTestingWithPredefinedProfileBase {

	@Autowired
	private CommandDispatcher dispatcher;

	@Override
	protected Role getRoleToLoginWithBeforeTesting() {
		return Role.ADMINISTRATOR;
	}

	/**
	 * Test layer settings.
	 */
	@Test
	@Transactional
	public void testSaveLayers() {
		GetClientLayersResponse glsresp = (GetClientLayersResponse) dispatcher.execute(GetClientLayersRequest.COMMAND,
				new GetClientLayersRequest(), getTokenOfLoggedInBeforeTesting(), "en");

		GetBlueprintsResponse gbpsresp = (GetBlueprintsResponse) dispatcher.execute(GetBlueprintsRequest.COMMAND,
				new GetBlueprintsRequest(), getTokenOfLoggedInBeforeTesting(), "en");

		GetBlueprintRequest gbpreq = new GetBlueprintRequest();
		gbpreq.setBlueprintId(gbpsresp.getBlueprints().get(0).getId());
		BlueprintResponse gbpresp = (BlueprintResponse) dispatcher.execute(GetBlueprintRequest.COMMAND, gbpreq,
				getTokenOfLoggedInBeforeTesting(), "en");

		BlueprintDto bpDto = gbpresp.getBlueprint();

		LayerDto cl = glsresp.getLayers().get(0);
		Assert.assertNull(cl.getClientLayerInfo());
		Assert.assertNull(cl.getWidgetInfo().get("TEST"));
		cl.setClientLayerInfo(cl.getReferencedLayerInfo());
		cl.getWidgetInfo().put("TEST", new MyClientWidgetInfo("testSaveLayers"));
		bpDto.getMainMapLayers().clear();
		bpDto.getMainMapLayers().add(cl);

		SaveBlueprintRequest request = new SaveBlueprintRequest();
		request.setBlueprint(bpDto);
		request.setSaveBitmask(SaveBlueprintRequest.SAVE_LAYERS);

		CommandResponse resp = dispatcher.execute(SaveBlueprintRequest.COMMAND, request,
				getTokenOfLoggedInBeforeTesting(), "en");
		Assert.assertTrue(resp.getExceptions().isEmpty());

		gbpreq = new GetBlueprintRequest();
		gbpreq.setBlueprintId(gbpsresp.getBlueprints().get(0).getId());
		gbpresp = (BlueprintResponse) dispatcher.execute(GetBlueprintRequest.COMMAND, gbpreq,
				getTokenOfLoggedInBeforeTesting(), "en");

		bpDto = gbpresp.getBlueprint();

		Assert.assertEquals(new MyClientWidgetInfo("testSaveLayers"), bpDto.getMainMapLayers().get(0).getWidgetInfo()
				.get("TEST"));
		Assert.assertNotNull(bpDto.getMainMapLayers().get(0).getClientLayerInfo());
	}

	/**
	 * Test security.
	 */
	@Test
	public void testNotAllowed() {
		GetBlueprintsResponse gbpsresp = (GetBlueprintsResponse) dispatcher.execute(GetBlueprintsRequest.COMMAND,
				new GetBlueprintsRequest(), getTokenOfLoggedInBeforeTesting(), "en");

		GetBlueprintRequest gbpreq = new GetBlueprintRequest();
		gbpreq.setBlueprintId(gbpsresp.getBlueprints().get(0).getId());
		BlueprintResponse gbpresp = (BlueprintResponse) dispatcher.execute(GetBlueprintRequest.COMMAND, gbpreq,
				getTokenOfLoggedInBeforeTesting(), "en");
		Assert.assertTrue(gbpresp.getExceptions().isEmpty());

		login(Role.GUEST);

		SaveBlueprintRequest request = new SaveBlueprintRequest();
		request.setBlueprint(gbpresp.getBlueprint());

		CommandResponse response = dispatcher.execute(SaveBlueprintRequest.COMMAND, request,
				getToken(Role.GUEST), "en");
		Assert.assertFalse(response.getExceptions().isEmpty());
		Assert.assertEquals(GeomajasSecurityException.class.getName(), response.getExceptions().get(0).getClassName());

	}
}
