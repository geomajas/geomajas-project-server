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

package org.geomajas.internal.service;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandResponse;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.ExceptionDto;
import org.geomajas.global.GeomajasException;
import org.geomajas.internal.security.AllowTestAuthorization;
import org.geomajas.internal.security.InMemorySecurityService;
import org.geomajas.internal.security.SecurityTestCommand;
import org.geomajas.security.Authentication;
import org.geomajas.security.BaseAuthorization;
import org.geomajas.security.GeomajasSecurityException;
import org.geomajas.security.SecurityContext;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test for security handling in {@link CommandDispatcher}.
 *
 * @author Jan De Moerloose
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/internal/service/inMemorySecurityContext.xml" })
public class CommandDispatcherSecurityTest {

	@Autowired
	private CommandDispatcher commandDispatcher;

	@Autowired
	private InMemorySecurityService securityService;

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private SecurityTestCommand securityCommand;

	@Test
	public void testValidToken() {
		securityService.put("someToken", createTestAuthentication());
		CommandResponse response = commandDispatcher.execute("test.SecurityTestCommand", null, "someToken", null);
		Assert.assertEquals(0, response.getErrors().size());
	}
	
	@Test
	public void testBadCommand() {
		securityService.put("someToken", createTestAuthentication());
		CommandResponse response = commandDispatcher.execute("test.BadCommand", null, "someToken", null);
		Assert.assertEquals(1, response.getErrors().size());
		Assert.assertEquals(1, response.getExceptions().size());
		Throwable error = response.getErrors().get(0);
		Assert.assertTrue(error instanceof GeomajasException);
		Assert.assertEquals(response.getExceptions().get(0).getClassName(),error.getClass().getName());
		Assert.assertEquals(ExceptionCode.COMMAND_ACCESS_DENIED, ((GeomajasException) error).getExceptionCode());
		Assert.assertEquals(ExceptionCode.COMMAND_ACCESS_DENIED, response.getExceptions().get(0).getExceptionCode());
	}

	@Test
	public void testInValidToken() {
		securityService.put("someToken", createTestAuthentication());
		CommandResponse response = commandDispatcher.execute("test.SecurityTestCommand", null, "invalid", null);
		Assert.assertEquals(1, response.getErrors().size());
		Throwable error = response.getErrors().get(0);
		Assert.assertTrue(error instanceof GeomajasSecurityException);
		Assert.assertEquals(ExceptionCode.CREDENTIALS_MISSING_OR_INVALID,
				((GeomajasSecurityException) error).getExceptionCode());

		Assert.assertEquals(1, response.getExceptions().size());
		ExceptionDto exceptionDto = response.getExceptions().get(0);
		Assert.assertEquals(exceptionDto.getExceptionCode(),((GeomajasSecurityException) error).getExceptionCode());
		Assert.assertEquals(exceptionDto.getClassName(),error.getClass().getName());

	}

	@Test
	public void testNullToken() {
		CommandResponse response = commandDispatcher.execute("test.SecurityTestCommand", null, null, null);
		Assert.assertEquals(1, response.getErrors().size());
		Throwable error = response.getErrors().get(0);
		Assert.assertTrue(error instanceof GeomajasSecurityException);
		Assert.assertEquals(ExceptionCode.CREDENTIALS_MISSING_OR_INVALID,
				((GeomajasSecurityException) error).getExceptionCode());

		Assert.assertEquals(1, response.getExceptions().size());
		ExceptionDto exceptionDto = response.getExceptions().get(0);
		Assert.assertEquals(exceptionDto.getExceptionCode(),((GeomajasSecurityException) error).getExceptionCode());
		Assert.assertEquals(exceptionDto.getClassName(),error.getClass().getName());
	}
	
	@Test
	public void testSecurityContext() {
		securityService.put("someToken", createTestAuthentication());
		securityCommand.setTest(new Runnable() {

			public void run() {
				Assert.assertEquals("someToken", securityContext.getToken());
			}

		});
		CommandResponse response = commandDispatcher.execute("test.SecurityTestCommand", null, "someToken", null);
		Assert.assertEquals(0, response.getErrors().size());
		Assert.assertEquals(0, response.getExceptions().size());

	}

	@After
	public void after() {
		securityService.clear();
	}

	private Authentication createTestAuthentication() {
		Authentication authentication = new Authentication();
		authentication.setAuthorizations(new BaseAuthorization[]{new AllowTestAuthorization()});
		return authentication;
	}
}
