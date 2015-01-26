/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.security;

import junit.framework.Assert;

import org.geomajas.security.SecurityContext;
import org.geomajas.security.SecurityManager;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests to verify correct behaviour of the SecurityManager implementation class, no looping.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/spring/securityNoLoopContext.xml"})
public class SecurityManagerNoLoopTest {

	@Autowired
	private SecurityManager securityManager;

	@Autowired
	private SecurityContext securityContext;

	@After
	public void clearSecurityContext() {
		securityManager.clearSecurityContext();
	}

	@Test
	public void testLooping() {
		Assert.assertTrue(securityManager.createSecurityContext("TEST"));
		Assert.assertEquals(1, securityContext.getSecurityServiceResults().size());
	}

	@Test
	public void testNotAuthenticated() {
		Assert.assertFalse(securityManager.createSecurityContext("false"));
		Assert.assertEquals(0, securityContext.getSecurityServiceResults().size());
	}

	@Test
	public void testSecondOnly() {
		Assert.assertTrue(securityManager.createSecurityContext("SECOND"));
		Assert.assertEquals(1, securityContext.getSecurityServiceResults().size());
	}
}
