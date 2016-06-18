/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.staticsecurity.security;

import org.geomajas.security.Authentication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Verify that the StaticSecurityService does its work.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/staticsecurity/security.xml"})
public class StaticSecurityServiceTest {

	@Autowired
	private AuthenticationTokenService tokenService;

	@Autowired
	private StaticSecurityService staticSecurityService;

	@Test
	public void testService() throws Exception {
		Authentication auth = new Authentication();
		String token = tokenService.login(auth);
		Authentication res = staticSecurityService.getAuthentication(token);
		Assert.assertEquals(auth, res);
		tokenService.logout(token);
		res = staticSecurityService.getAuthentication(token);
		Assert.assertNull(res);
	}
}
