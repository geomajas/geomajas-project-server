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
import org.geomajas.plugin.deskmanager.command.manager.dto.GeodeskResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetClientLayersRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetClientLayersResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetGeodeskRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetGeodesksRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetGeodesksResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.SaveGeodeskRequest;
import org.geomajas.plugin.deskmanager.domain.dto.GeodeskDto;
import org.geomajas.plugin.deskmanager.domain.dto.LayerDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;
import org.geomajas.plugin.deskmanager.test.LoginBeforeTestingWithPredefinedProfileBase;
import org.geomajas.plugin.deskmanager.test.general.MyClientWidgetInfo;
import org.geomajas.plugin.deskmanager.test.security.StubProfileService;
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
public class SaveGeodeskCommandTest extends LoginBeforeTestingWithPredefinedProfileBase {

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

		GetGeodesksResponse gbpsresp = (GetGeodesksResponse) dispatcher.execute(GetGeodesksRequest.COMMAND,
				new GetGeodesksRequest(), getTokenOfLoggedInBeforeTesting(), "en");

		GetGeodeskRequest gbpreq = new GetGeodeskRequest();
		gbpreq.setGeodeskId(gbpsresp.getGeodesks().get(0).getId());
		GeodeskResponse gbpresp = (GeodeskResponse) dispatcher.execute(GetGeodeskRequest.COMMAND, gbpreq,
				getTokenOfLoggedInBeforeTesting(), "en");

		GeodeskDto bpDto = gbpresp.getGeodesk();

		LayerDto cl = glsresp.getLayers().get(0);
		Assert.assertNull(cl.getClientLayerInfo());
		Assert.assertNull(cl.getWidgetInfo().get("TEST"));
		cl.setClientLayerInfo(cl.getReferencedLayerInfo());
		cl.getWidgetInfo().put("TEST", new MyClientWidgetInfo("testSaveLayers"));
		bpDto.getMainMapLayers().clear();
		bpDto.getMainMapLayers().add(cl);

		SaveGeodeskRequest request = new SaveGeodeskRequest();
		request.setGeodesk(bpDto);
		request.setSaveBitmask(SaveGeodeskRequest.SAVE_LAYERS);

		CommandResponse resp = dispatcher.execute(SaveGeodeskRequest.COMMAND, request,
				getTokenOfLoggedInBeforeTesting(), "en");
		Assert.assertTrue(resp.getExceptions().isEmpty());

		gbpreq = new GetGeodeskRequest();
		gbpreq.setGeodeskId(gbpsresp.getGeodesks().get(0).getId());
		gbpresp = (GeodeskResponse) dispatcher.execute(GetGeodeskRequest.COMMAND, gbpreq,
				getTokenOfLoggedInBeforeTesting(), "en");

		bpDto = gbpresp.getGeodesk();

		Assert.assertEquals(new MyClientWidgetInfo("testSaveLayers"), bpDto.getMainMapLayers().get(0).getWidgetInfo()
				.get("TEST"));
		Assert.assertNotNull(bpDto.getMainMapLayers().get(0).getClientLayerInfo());
	}

	/**
	 * Test security.
	 */
	@Test
	public void testNotAllowed() {
		GetGeodesksResponse gbpsresp = (GetGeodesksResponse) dispatcher.execute(GetGeodesksRequest.COMMAND,
				new GetGeodesksRequest(), getTokenOfLoggedInBeforeTesting(), "en");

		GetGeodeskRequest gbpreq = new GetGeodeskRequest();
		gbpreq.setGeodeskId(gbpsresp.getGeodesks().get(0).getId());
		GeodeskResponse gbpresp = (GeodeskResponse) dispatcher.execute(GetGeodeskRequest.COMMAND, gbpreq,
				getTokenOfLoggedInBeforeTesting(), "en");
		Assert.assertTrue(gbpresp.getExceptions().isEmpty());

		login(Role.GUEST);

		SaveGeodeskRequest request = new SaveGeodeskRequest();
		request.setGeodesk(gbpresp.getGeodesk());

		CommandResponse response = dispatcher.execute(SaveGeodeskRequest.COMMAND, request,
				getToken(Role.GUEST), "en");
		Assert.assertFalse(response.getExceptions().isEmpty());
		Assert.assertEquals(GeomajasSecurityException.class.getName(), response.getExceptions().get(0).getClassName());

	}
}
