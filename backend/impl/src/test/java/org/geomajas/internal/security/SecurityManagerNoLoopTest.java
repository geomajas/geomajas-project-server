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

package org.geomajas.internal.security;

import junit.framework.Assert;
import org.geomajas.security.SecurityContext;
import org.geomajas.security.SecurityManager;
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
