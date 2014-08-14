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
package org.geomajas.plugin.deskmanager.test.service;

import org.geomajas.configuration.client.ClientWidgetInfo;
import org.geomajas.plugin.deskmanager.command.security.dto.RetrieveRolesRequest;
import org.geomajas.plugin.deskmanager.domain.BaseGeodesk;
import org.geomajas.plugin.deskmanager.domain.Blueprint;
import org.geomajas.plugin.deskmanager.security.DeskmanagerSecurityService;
import org.geomajas.plugin.deskmanager.security.ProfileService;
import org.geomajas.plugin.deskmanager.service.common.BlueprintService;
import org.geomajas.plugin.deskmanager.test.SecurityContainingTestBase;
import org.geomajas.plugin.deskmanager.test.security.StubProfileService;
import org.geomajas.security.SecurityManager;
import org.geomajas.security.SecurityService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

/**
 * @author Oliver May
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/deskmanager/spring/**/*.xml", "/applicationContext.xml" })
public class BlueprintServiceTest extends SecurityContainingTestBase {

	@Autowired
	private BlueprintService blueprintService;

	@Autowired
	private ProfileService profileService;

	@Autowired
	private SecurityManager securityManager;

	@Autowired
	private SecurityService securityService;

	@Test
	public void testLoadBlueprints() throws Exception {

		String token = ((DeskmanagerSecurityService) securityService).registerRole(
				RetrieveRolesRequest.MANAGER_ID, ((StubProfileService) profileService).getPredefinedProfiles().get(1));
		securityManager.createSecurityContext(token);

		List<Blueprint> blueprints = blueprintService.getBlueprints();

		for (BaseGeodesk blueprint : blueprints) {
			Map<String, ClientWidgetInfo> appWidgets = blueprint.getApplicationClientWidgetInfos();
			Assert.assertNotNull(appWidgets);
			Map<String, ClientWidgetInfo> mmapWidgets = blueprint.getMainMapClientWidgetInfos();
			Assert.assertNotNull(mmapWidgets);
			Map<String, ClientWidgetInfo> omapWidgets = blueprint.getOverviewMapClientWidgetInfos();
			Assert.assertNotNull(omapWidgets);

		}

	}
}
