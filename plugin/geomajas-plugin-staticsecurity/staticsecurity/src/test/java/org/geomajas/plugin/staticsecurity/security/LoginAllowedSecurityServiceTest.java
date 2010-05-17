/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.plugin.staticsecurity.security;

import org.geomajas.security.Authentication;
import org.geomajas.security.BaseAuthorization;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Verify functioning of LoginAllowedSecurityService.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/staticsecurity/security.xml"})
public class LoginAllowedSecurityServiceTest {

	@Test
	public void testLoginAllowedService() {
		LoginAllowedSecurityService service = new LoginAllowedSecurityService();
		Authentication auth = service.getAuthentication("bla");
		Assert.assertNotNull(auth);
		Assert.assertEquals(1, auth.getAuthorizations().length);
		BaseAuthorization authorization = auth.getAuthorizations()[0];
		Assert.assertTrue(authorization.isCommandAuthorized("command.staticsecurity.Login"));
		Assert.assertTrue(authorization.isCommandAuthorized("command.staticsecurity.Logout"));
		Assert.assertFalse(authorization.isCommandAuthorized("command.staticsecurity.Other"));
	}
}
