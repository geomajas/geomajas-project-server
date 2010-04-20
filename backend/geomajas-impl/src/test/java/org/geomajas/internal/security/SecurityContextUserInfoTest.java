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
import org.geomajas.security.Authentication;
import org.geomajas.security.BaseAuthorization;
import org.geomajas.security.allowall.AllowAllAuthorization;
import org.geomajas.security.allowall.SecurityContextImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Testing of UserInfo related data in the security context.
 *
 * @author Joachim Van der Auwera
 */
public class SecurityContextUserInfoTest {

	private static final String SECURITY_SERVICE_ID = "ss";
	private static final String USER_ID = "auth";
	private static final String USER_NAME = "Full name";
	private static final Locale USER_LOCALE = Locale.CHINESE;
	private static final String USER_ORGANIZATION = "Geosparc";
	private static final String USER_DIVISION = "Development";

	@Test
	public void testSameUser() {
		SecurityContextImpl securityContext = new SecurityContextImpl();
		List<Authentication> authentications = new ArrayList<Authentication>();
		Authentication auth1 = getAuthentication();
		Authentication auth2 = getAuthentication();
		auth2.setSecurityServiceId("ss2");
		authentications.add(auth1);
		authentications.add(auth2);
		securityContext.setAuthentications("token", authentications);
		Assert.assertEquals("AllowAll@ss|AllowAll@ss2",securityContext.getId());
		Assert.assertEquals(USER_ID,securityContext.getUserId());
		Assert.assertEquals(USER_NAME,securityContext.getUserName());
		Assert.assertEquals(USER_LOCALE,securityContext.getUserLocale());
		Assert.assertEquals(USER_ORGANIZATION,securityContext.getUserOrganization());
		Assert.assertEquals(USER_DIVISION,securityContext.getUserDivision());
	}

	@Test
	public void testDifferentUser() {
		SecurityContextImpl securityContext = new SecurityContextImpl();
		List<Authentication> authentications = new ArrayList<Authentication>();
		Authentication auth1 = getAuthentication();
		Authentication auth2 = getAuthentication();
		auth2.setUserId("bla");
		auth2.setUserName(null);
		auth2.setUserLocale(null);
		auth2.setUserDivision("Marketing");
		authentications.add(auth1);
		authentications.add(auth2);
		securityContext.setAuthentications("token", authentications);
		Assert.assertEquals("AllowAll@ss",securityContext.getId());
		Assert.assertEquals(USER_ID + ", bla", securityContext.getUserId());
		Assert.assertEquals(USER_NAME, securityContext.getUserName());
		Assert.assertEquals(USER_LOCALE, securityContext.getUserLocale());
		Assert.assertEquals(USER_ORGANIZATION, securityContext.getUserOrganization());
		Assert.assertEquals(USER_DIVISION + ", Marketing", securityContext.getUserDivision());
	}

	@Test
	public void testDifferentUser2() {
		SecurityContextImpl securityContext = new SecurityContextImpl();
		List<Authentication> authentications = new ArrayList<Authentication>();
		Authentication auth1 = getAuthentication();
		auth1.setUserName(null);
		auth1.setUserLocale(null);
		auth1.setUserOrganization(null);
		Authentication auth2 = getAuthentication();
		auth2.setUserName(null);
		auth2.setUserLocale(null);
		authentications.add(auth1);
		authentications.add(auth2);
		securityContext.setAuthentications("token", authentications);
		Assert.assertEquals("AllowAll@ss", securityContext.getId());
		Assert.assertEquals(USER_ID, securityContext.getUserId());
		Assert.assertNull(securityContext.getUserName());
		Assert.assertNull(securityContext.getUserLocale());
		Assert.assertEquals(USER_ORGANIZATION, securityContext.getUserOrganization());
		Assert.assertEquals(USER_DIVISION, securityContext.getUserDivision());
	}

	private Authentication getAuthentication() {
		Authentication auth = new Authentication();
		auth.setAuthorizations(new BaseAuthorization[]{new AllowAllAuthorization()});
		auth.setSecurityServiceId(SECURITY_SERVICE_ID);
		auth.setUserId(USER_ID);
		auth.setUserName(USER_NAME);
		auth.setUserLocale(USER_LOCALE);
		auth.setUserOrganization(USER_ORGANIZATION);
		auth.setUserDivision(USER_DIVISION);
		return auth;
	}
}
