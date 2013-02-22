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
import org.geomajas.command.CommandResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.BlueprintResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetBlueprintRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetBlueprintsRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetBlueprintsResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetLayersRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetLayersResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.SaveBlueprintRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.RetrieveRolesRequest;
import org.geomajas.plugin.deskmanager.domain.dto.BlueprintDto;
import org.geomajas.plugin.deskmanager.domain.dto.LayerDto;
import org.geomajas.plugin.deskmanager.security.DeskmanagerSecurityService;
import org.geomajas.plugin.deskmanager.security.ProfileService;
import org.geomajas.plugin.deskmanager.test.general.MyClientWidgetInfo;
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
 * TODO: tests for all different save scenarios
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/deskmanager/spring/**/*.xml", "/applicationContext.xml" })
@Transactional
public class SaveBlueprintCommandTest {
	@Autowired
	private SecurityService securityService;

	@Autowired
	private ProfileService profileService;

	@Autowired
	private SecurityManager securityManager;

	@Autowired
	private CommandDispatcher dispatcher;

	private String userToken;

	private String guestToken;

	@Before
	public void setup() throws Exception {
		// First profile in list is admin
		userToken = ((DeskmanagerSecurityService) securityService).registerRole(
				RetrieveRolesRequest.MANAGER_ID, profileService.getProfiles().get(0));
		guestToken = ((DeskmanagerSecurityService) securityService).registerRole(
				RetrieveRolesRequest.MANAGER_ID, DeskmanagerSecurityService.createGuestProfile());

		// Log in
		securityManager.createSecurityContext(userToken);
	}

	/**
	 * Test layer settings.
	 */
	@Test
	public void testSaveLayers() {
		GetLayersResponse glsresp = (GetLayersResponse) dispatcher.execute(GetLayersRequest.COMMAND, 
				new GetLayersRequest(), userToken, "en");
		
		GetBlueprintsResponse gbpsresp = (GetBlueprintsResponse) dispatcher.execute(GetBlueprintsRequest.COMMAND, 
				new GetBlueprintsRequest(), userToken, "en");
		
		GetBlueprintRequest gbpreq = new GetBlueprintRequest();
		gbpreq.setUuid(gbpsresp.getBlueprints().get(0).getId());
		BlueprintResponse gbpresp = (BlueprintResponse) dispatcher.execute(GetBlueprintRequest.COMMAND, 
				gbpreq, userToken, "en");

		BlueprintDto bpDto = gbpresp.getBlueprint();
		
		LayerDto cl = glsresp.getLayers().get(0);
		Assert.assertNull(cl.getClientLayerInfo());
		Assert.assertNull(cl.getWidgetInfo().get("TEST"));
		cl.setCLientLayerInfo(cl.getReferencedLayerInfo());
		cl.getWidgetInfo().put("TEST", new MyClientWidgetInfo("testSaveLayers"));
		bpDto.getMainMapLayers().add(cl);
		
		SaveBlueprintRequest request = new SaveBlueprintRequest();
		request.setBlueprint(bpDto);
		request.setSaveBitmask(SaveBlueprintRequest.SAVE_LAYERS);

		CommandResponse resp = dispatcher.execute(SaveBlueprintRequest.COMMAND, request, userToken, "en");
		Assert.assertTrue(resp.getExceptions().isEmpty());

		gbpreq = new GetBlueprintRequest();
		gbpreq.setUuid(gbpsresp.getBlueprints().get(0).getId());
		gbpresp = (BlueprintResponse) dispatcher.execute(GetBlueprintRequest.COMMAND, 
				gbpreq, userToken, "en");
		
		bpDto = gbpresp.getBlueprint();
		
		Assert.assertEquals(bpDto.getMainMapLayers().get(0).getWidgetInfo().get("TEST"), 
				new MyClientWidgetInfo("testSaveLayers"));
		Assert.assertNotNull(bpDto.getMainMapLayers().get(0).getClientLayerInfo());
	}
	
	/**
	 * Test security.
	 */
	@Test
	public void testNotAllowed() {
		GetBlueprintsResponse gbpsresp = (GetBlueprintsResponse) dispatcher.execute(GetBlueprintsRequest.COMMAND, 
				new GetBlueprintsRequest(), userToken, "en");
		
		GetBlueprintRequest gbpreq = new GetBlueprintRequest();
		gbpreq.setUuid(gbpsresp.getBlueprints().get(0).getId());
		BlueprintResponse gbpresp = (BlueprintResponse) dispatcher.execute(GetBlueprintRequest.COMMAND, 
				gbpreq, userToken, "en");
		Assert.assertTrue(gbpresp.getExceptions().isEmpty());
		
		securityManager.createSecurityContext(guestToken);

		
		SaveBlueprintRequest request = new SaveBlueprintRequest();
		request.setBlueprint(gbpresp.getBlueprint());

		BlueprintResponse response = 
			(BlueprintResponse) dispatcher.execute(SaveBlueprintRequest.COMMAND, request, guestToken, "en");
		Assert.assertFalse(response.getExceptions().isEmpty());
		Assert.assertEquals(response.getExceptions().get(0).getClassName(), GeomajasSecurityException.class.getName());
		
	}
}
