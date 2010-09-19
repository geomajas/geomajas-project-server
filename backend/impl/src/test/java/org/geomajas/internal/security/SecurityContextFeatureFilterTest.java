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
import org.geomajas.security.SecurityContext;
import org.geomajas.security.VectorLayerSelectFilterAuthorization;
import org.geomajas.security.allowall.AllowAllAuthorization;
import org.geomajas.service.FilterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Testing of FeatureFilter combination in the security context.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/spring/moreContext.xml"})
public class SecurityContextFeatureFilterTest {

	private static final String SECURITY_SERVICE_ID = "ss";
	private static final String LAYER_ID = "layer";
	private static final String USER_ID = "auth";

	@Autowired
	private FilterService filterService;

	@Autowired
	private SecurityContext securityContext;

	@Test
	public void testNoFilters() {
		SecurityContextImpl securityContext = (SecurityContextImpl)this.securityContext;
		List<Authentication> authentications = new ArrayList<Authentication>();
		Authentication auth1 = getAuthentication();
		Authentication auth2 = getAuthentication();
		auth2.setSecurityServiceId("ss2");
		authentications.add(auth1);
		authentications.add(auth2);
		securityContext.setAuthentications("token", authentications);
		Assert.assertNull(securityContext.getFeatureFilter(LAYER_ID));
	}

	@Test
	public void testOneFilter() {
		SecurityContextImpl securityContext = (SecurityContextImpl)this.securityContext;
		List<Authentication> authentications = new ArrayList<Authentication>();
		Authentication auth1 = getAuthentication();
		Authentication auth2 = getFilterAuthentication();
		auth2.setSecurityServiceId("ss2");
		authentications.add(auth1);
		authentications.add(auth2);
		securityContext.setAuthentications("token", authentications);
		Filter filter = securityContext.getFeatureFilter(LAYER_ID);
		Assert.assertNotNull(filter);
		// these tests are (geotools) implementation dependent
		Assert.assertEquals("org.geotools.filter.LikeFilterImpl", filter.getClass().getName());
		Assert.assertEquals("[ name is like bla% ]", filter.toString());
	}

	@Test
	public void testTwoFilters() {
		SecurityContextImpl securityContext = (SecurityContextImpl)this.securityContext;
		List<Authentication> authentications = new ArrayList<Authentication>();
		Authentication auth1 = getFilterAuthentication();
		Authentication auth2 = getFilterAuthentication();
		auth2.setSecurityServiceId("ss2");
		authentications.add(auth1);
		authentications.add(auth2);
		securityContext.setAuthentications("token", authentications);
		Filter filter = securityContext.getFeatureFilter(LAYER_ID);
		Assert.assertNotNull(filter);
		// these tests are (geotools) implementation dependent
		Assert.assertEquals("org.geotools.filter.AndImpl", filter.getClass().getName());
		Assert.assertEquals("[[ name is like bla% ] AND [ name is like bla% ]]", filter.toString());
	}

	private Authentication getAuthentication() {
		Authentication auth = new Authentication();
		auth.setAuthorizations(new BaseAuthorization[]{new AllowAllAuthorization()});
		auth.setSecurityServiceId(SECURITY_SERVICE_ID);
		auth.setUserId(USER_ID);
		return auth;
	}

	private Authentication getFilterAuthentication() {
		Authentication auth = new Authentication();
		auth.setAuthorizations(new BaseAuthorization[]{new FilterAuthorization()});
		auth.setSecurityServiceId(SECURITY_SERVICE_ID);
		auth.setUserId(USER_ID);
		return auth;
	}

	private class FilterAuthorization extends AllowAllAuthorization implements VectorLayerSelectFilterAuthorization {
		public Filter getFeatureFilter(String layerId) {
			return filterService.createLikeFilter("name", "bla%");
		}
	}
}
