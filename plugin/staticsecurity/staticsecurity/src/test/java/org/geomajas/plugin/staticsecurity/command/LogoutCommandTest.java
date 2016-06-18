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

package org.geomajas.plugin.staticsecurity.command;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.command.EmptyCommandRequest;
import org.geomajas.command.SuccessCommandResponse;
import org.geomajas.internal.security.DefaultSecurityContext;
import org.geomajas.plugin.staticsecurity.command.staticsecurity.LogoutCommand;
import org.geomajas.plugin.staticsecurity.security.AuthenticationTokenService;
import org.geomajas.plugin.staticsecurity.security.StaticSecurityService;
import org.geomajas.security.Authentication;
import org.geomajas.security.BaseAuthorization;
import org.geomajas.security.SecurityContext;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Verify the functioning of the LogoutCommand class.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/staticsecurity/security.xml"})
public class LogoutCommandTest {

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private AuthenticationTokenService tokenService;

	@Autowired
	private LogoutCommand logoutCommand;
	
	@Test
	public void testLogout() throws Exception {
		Authentication auth = new Authentication();
		auth.setAuthorizations(new BaseAuthorization[0]);
		auth.setSecurityServiceId(StaticSecurityService.SECURITY_SERVICE_ID); // mimic that this comes from the StaticSecurity stuff
		String token = tokenService.login(auth);
		DefaultSecurityContext securityContext = (DefaultSecurityContext) this.securityContext;
		List<Authentication> auths = new ArrayList<Authentication>();
		auths.add(auth);
		securityContext.setAuthentications(token, auths);

		Assert.assertEquals(token, securityContext.getToken());
		EmptyCommandRequest request = new EmptyCommandRequest();
		SuccessCommandResponse response = logoutCommand.getEmptyCommandResponse();
		logoutCommand.execute(request, response);
		Assert.assertTrue(response.isSuccess());
		Assert.assertNull(tokenService.getAuthentication(token));
	}
}
