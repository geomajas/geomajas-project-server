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
package org.geomajas.plugin.deskmanager.staticsecurity;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.geometry.service.WktException;
import org.geomajas.geometry.service.WktService;
import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.deskmanager.domain.security.Profile;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.geomajas.plugin.deskmanager.domain.security.TerritoryCategory;
import org.geomajas.plugin.deskmanager.security.ProfileService;
import org.geomajas.plugin.deskmanager.test.service.DeskmanagerExampleDatabaseProvisioningService;
import org.geomajas.plugin.deskmanager.test.service.ExampleDatabaseProvisioningServiceImpl;
import org.geomajas.service.DtoConverterService;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service that loads and stores static security profiles for the deskmanager plug-in.
 *
 * @author Oliver May
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/deskmanager/spring/**/*.xml", "/applicationContext.xml",
		"/staticSercurityProfileService.xml"})
@Transactional
public class StaticSecurityProfileServiceTest {

	@Autowired
	LoginService loginService;

	@Autowired
	ProfileService profileService;

	@Autowired
	DeskmanagerExampleDatabaseProvisioningService databaseProvisioningService;

	@Test
	public void testLoadStaticSecurityProfiles() throws WktException, GeomajasException {
		String token = loginService.login("luc", "luc");
		List<Profile> profiles = profileService.getProfiles(token);

		Assert.assertEquals(1, profiles.size());
		Assert.assertNotNull(profiles.get(0).getTerritory());
	}
}