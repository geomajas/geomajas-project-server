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

package org.geomajas.internal.security;

import org.geomajas.security.Authentication;
import org.geomajas.security.BaseAuthorization;
import org.geomajas.security.SavedAuthorization;
import org.geomajas.security.SecurityContext;
import org.geomajas.security.SecurityManager;
import org.geomajas.service.TestRecorder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test for wiring of spring fields in security authentication objects.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/internal/service/inMemorySecurityContext.xml", "/org/geomajas/spring/testRecorder.xml" })
@DirtiesContext
public class AuthorizationWiringTest {

	private static final String TOKEN = "someToken";

	@Autowired
	private InMemorySecurityService securityService;

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private SecurityManager securityManager;

	@Autowired
	private TestRecorder testRecorder;
	
	@After
	public void clearSecurityContext() {
		// need to clear security context
		securityManager.clearSecurityContext();
	}

	@Test
	public void testValidToken() {
		securityService.put(TOKEN, createTestAuthentication());

		testRecorder.clear();
		securityManager.createSecurityContext(TOKEN);
		Assert.assertEquals("", testRecorder.matches(AllowNoneWiredAuthorization.GROUP,
				AllowNoneWiredAuthorization.VALUE));

		SavedAuthorization saved = securityContext.getSavedAuthorization();
		securityManager.clearSecurityContext();
		testRecorder.clear();
		securityManager.restoreSecurityContext(saved);
		Assert.assertEquals("", testRecorder.matches(AllowNoneWiredAuthorization.GROUP,
				AllowNoneWiredAuthorization.VALUE));
	}

	private Authentication createTestAuthentication() {
		Authentication authentication = new Authentication();
		authentication.setAuthorizations(new BaseAuthorization[]{new AllowNoneWiredAuthorization()});
		return authentication;
	}
}
