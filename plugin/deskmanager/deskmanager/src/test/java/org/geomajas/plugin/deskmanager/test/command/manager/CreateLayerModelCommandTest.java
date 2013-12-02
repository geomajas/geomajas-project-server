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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandResponse;
import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.plugin.deskmanager.command.manager.dto.CreateLayerModelRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.DynamicRasterLayerConfiguration;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetWmsCapabilitiesRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.LayerModelResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.RasterCapabilitiesInfo;
import org.geomajas.plugin.deskmanager.command.security.dto.RetrieveRolesRequest;
import org.geomajas.plugin.deskmanager.configuration.client.DeskmanagerClientLayerInfo;
import org.geomajas.plugin.deskmanager.security.DeskmanagerSecurityService;
import org.geomajas.plugin.deskmanager.security.ProfileService;
import org.geomajas.plugin.deskmanager.service.manager.DiscoveryService;
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
public class CreateLayerModelCommandTest {

	@Autowired
	private SecurityService securityService;

	@Autowired
	private ProfileService profileService;

	@Autowired
	private SecurityManager securityManager;

	@Autowired
	private CommandDispatcher dispatcher;

	@Autowired
	private DiscoveryService discoveryService;

	private String userToken;

	private String guestToken;

	@Before
	public void setup() throws Exception {
		// First profile in list is admin
		userToken = ((DeskmanagerSecurityService) securityService).registerRole(RetrieveRolesRequest.MANAGER_ID,
				profileService.getProfiles(null).get(0));
		guestToken = ((DeskmanagerSecurityService) securityService).registerRole(RetrieveRolesRequest.MANAGER_ID,
				DeskmanagerSecurityService.createGuestProfile());

		// Log in
		securityManager.createSecurityContext(userToken);

	}

	@Test
	@Transactional
	public void testCreateWmsLayerModel() throws Exception {
		//Get configuration object.
		Map<String, String> connection = new HashMap<String, String>();
		connection.put(GetWmsCapabilitiesRequest.GET_CAPABILITIES_URL,
				"http://apps.geomajas.org/geoserver/geosparc/ows?service=wms&version=1.1.1&request=GetCapabilities");
		List<RasterCapabilitiesInfo> rci = discoveryService.getRasterCapabilities(connection);
		DynamicRasterLayerConfiguration configuration = discoveryService.getRasterLayerConfiguration(connection,
				rci.get(0));

		CreateLayerModelRequest request = new CreateLayerModelRequest();

		DeskmanagerClientLayerInfo dcli = new DeskmanagerClientLayerInfo();
		dcli.setActive(true);
		dcli.setName("TEST layer");
		dcli.setPublic(true);
		dcli.setSystemLayer(false);

		ClientRasterLayerInfo cli = configuration.getClientRasterLayerInfo();
		cli.setUserData(dcli);

		RasterLayerInfo rli = configuration.getRasterLayerInfo();

		configuration.setClientRasterLayerInfo(cli);
		configuration.setRasterLayerInfo(rli);
		request.setConfiguration(configuration);

		LayerModelResponse response = (LayerModelResponse) dispatcher.execute(CreateLayerModelRequest.COMMAND, request,
				userToken, "en");

		Assert.assertTrue(response.getErrors().isEmpty());
		Assert.assertTrue(response.getErrorMessages().isEmpty());
	}

	/**
	 * Test security.
	 */
	@Test
	public void testNotAllowed() {
		CommandResponse response = dispatcher.execute(CreateLayerModelRequest.COMMAND, new CreateLayerModelRequest(),
				guestToken, "en");

		Assert.assertFalse(response.getExceptions().isEmpty());
		Assert.assertEquals(GeomajasSecurityException.class.getName(), response.getExceptions().get(0).getClassName());
	}
}
