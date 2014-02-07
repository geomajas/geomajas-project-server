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

package org.geomajas.plugin.staticsecurity.security;

import org.geomajas.plugin.staticsecurity.command.dto.LoginRequest;
import org.geomajas.plugin.staticsecurity.command.dto.LogoutRequest;
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
		Assert.assertTrue(authorization.isCommandAuthorized(LoginRequest.COMMAND));
		Assert.assertTrue(authorization.isCommandAuthorized(LogoutRequest.COMMAND));
		Assert.assertFalse(authorization.isCommandAuthorized("command.staticsecurity.Other"));
	}
}
