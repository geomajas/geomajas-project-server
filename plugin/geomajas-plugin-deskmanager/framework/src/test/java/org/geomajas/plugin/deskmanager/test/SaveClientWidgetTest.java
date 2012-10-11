/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.test;

import junit.framework.Assert;

import org.geomajas.configuration.client.ClientApplicationInfo.DummyClientWidgetInfo;
import org.geomajas.plugin.deskmanager.domain.Geodesk;
import org.geomajas.plugin.deskmanager.security.ProfileService;
import org.geomajas.plugin.deskmanager.security.DeskmanagerSecurityService;
import org.geomajas.plugin.deskmanager.service.common.BlueprintService;
import org.geomajas.plugin.deskmanager.service.common.GeodeskService;
import org.geomajas.security.GeomajasSecurityException;
import org.geomajas.security.SecurityManager;
import org.geomajas.security.SecurityService;
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
public class SaveClientWidgetTest {

	@Autowired
	private GeodeskService geodeskService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private ProfileService profileService;

	@Autowired
	private SecurityManager securityManager;

	@Autowired
	private BlueprintService blueprintService;

	public void login() {
		// First profile in list is admin
		String token = ((DeskmanagerSecurityService) securityService).registerRole(
				DeskmanagerSecurityService.MANAGER_GEODESK_ID, profileService.getProfiles().get(0));
		// Log in
		securityManager.createSecurityContext(token);
	}

	@Test
	@Transactional
	public void testApplicationClientWidgetInfo() throws GeomajasSecurityException {

		login();

		Geodesk geodesk = new Geodesk();
		geodesk.setGeodeskId("42");
		geodesk.setName("Test");
		geodesk.setBlueprint(blueprintService.getBlueprints().get(0));

		DummyClientWidgetInfo widgetInfo = new DummyClientWidgetInfo();
		widgetInfo.setDummy("Test client widget info 1");
		geodesk.getApplicationClientWidgetInfos().put("1", widgetInfo);

		DummyClientWidgetInfo widgetInfo2 = new DummyClientWidgetInfo();
		widgetInfo2.setDummy("Test client widget info 2");
		geodesk.getApplicationClientWidgetInfos().put("2", widgetInfo2);

		geodeskService.saveOrUpdateGeodesk(geodesk);

		Geodesk desk = geodeskService.getGeodeskByPublicId(geodesk.getGeodeskId());

		Assert.assertEquals(((DummyClientWidgetInfo) desk.getApplicationClientWidgetInfos().get("1")).getDummy(),
				widgetInfo.getDummy());

		Assert.assertEquals(((DummyClientWidgetInfo) desk.getApplicationClientWidgetInfos().get("2")).getDummy(),
				widgetInfo2.getDummy());

	}

	public void testClientUserDataConfig() {

	}

	public void testMapClientWidgetInfo() {

	}
}
