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

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.geomajas.security.Authentication;
import org.geomajas.security.BaseAuthorization;
import org.geomajas.security.SecurityContext;
import org.geomajas.security.VectorLayerSelectFilterAuthorization;
import org.geomajas.security.allowall.AllowAllAuthorization;
import org.geomajas.service.FilterService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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

	@Autowired
	private org.geomajas.security.SecurityManager securityManager;

	@After
	public void clearSecurityContext() {
		securityManager.clearSecurityContext();
	}

	@Test
	@DirtiesContext // setting SecurityContext
	public void testNoFilters() {
		DefaultSecurityContext securityContext = (DefaultSecurityContext)this.securityContext;
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
		DefaultSecurityContext securityContext = (DefaultSecurityContext)this.securityContext;
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
		DefaultSecurityContext securityContext = (DefaultSecurityContext)this.securityContext;
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
