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

package org.geomajas.internal.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import junit.framework.Assert;

import org.geomajas.security.Authentication;
import org.geomajas.security.BaseAuthorization;
import org.geomajas.security.allowall.AllowAllAuthorization;
import org.junit.Test;

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
		DefaultSecurityContext securityContext = new DefaultSecurityContext();
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
		DefaultSecurityContext securityContext = new DefaultSecurityContext();
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
		DefaultSecurityContext securityContext = new DefaultSecurityContext();
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
