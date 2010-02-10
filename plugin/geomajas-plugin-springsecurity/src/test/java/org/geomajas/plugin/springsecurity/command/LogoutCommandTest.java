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

package org.geomajas.plugin.springsecurity.command;

import org.geomajas.command.EmptyCommandRequest;
import org.geomajas.command.SuccessCommandResponse;
import org.geomajas.internal.security.SecurityContextImpl;
import org.geomajas.plugin.springsecurity.security.AuthenticationTokenService;
import org.geomajas.security.Authentication;
import org.geomajas.security.BaseAuthorization;
import org.geomajas.security.SecurityContext;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Verify the functioning of the LogoutCommand class.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/springsecurity/security.xml"})
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
		auth.setSecurityServiceId("SecurityService"); // mimic that this comes from the SpringSecurity stuff
		String token = tokenService.login(auth);
		SecurityContextImpl securityContext = (SecurityContextImpl) this.securityContext;
		List<Authentication> auths = new ArrayList<Authentication>();
		auths.add(auth);
		securityContext.setAuthentications(token, auths);

		Assert.assertEquals(token, securityContext.getToken());
		EmptyCommandRequest request = new EmptyCommandRequest();
		SuccessCommandResponse response = logoutCommand.getEmptyCommandResponse();
		logoutCommand.execute(request, response);
		Assert.assertTrue(response.isSucces());
		Assert.assertNull(tokenService.getAuthentication(token));
	}
}
