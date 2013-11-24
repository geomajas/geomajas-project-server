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

import junit.framework.Assert;

import org.geomajas.security.AreaAuthorization;
import org.geomajas.security.Authentication;
import org.geomajas.security.BaseAuthorization;
import org.geomajas.security.SecurityContext;
import org.geomajas.security.allowall.AllowAllAuthorization;
import org.geomajas.spring.ThreadScopeContextHolder;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * Testing of AreaAuthorization related data in the security context.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/beanContext.xml", "/org/geomajas/testdata/layerBeans.xml"})
public class SecurityContextAreaAuthorizationTest {

	private static final String LAYER_ID = "beans";
	private static final int LAYER_SRID = 4326;

	private static final String SECURITY_SERVICE_ID = "ss";
	private static final String USER_ID = "auth";

	@Autowired
	private SecurityContext securityContext;

	@After
	public void fixSideEffects() {
		ThreadScopeContextHolder.clear();
	}

	@Test
	@DirtiesContext // changing security context
	public void testNotAuthenticatedVisibleArea() throws Exception {
		DefaultSecurityContext securityContext = (DefaultSecurityContext)this.securityContext;
		securityContext.setAuthentications(null, null);
		Assert.assertFalse(securityContext.isLayerVisible(LAYER_ID));
		Geometry geometry = securityContext.getVisibleArea(LAYER_ID);
		Assert.assertNull(geometry);
		securityContext.setAuthentications(null, null);
	}

	@Test
	public void testDefaultVisibleArea() throws Exception {
		DefaultSecurityContext securityContext = (DefaultSecurityContext)this.securityContext;
		List<Authentication> authentications = new ArrayList<Authentication>();
		Authentication auth1 = getBaseAuthentication();
		authentications.add(auth1);
		securityContext.setAuthentications("token", authentications);
		Assert.assertTrue(securityContext.isLayerVisible(LAYER_ID));
		Geometry geometry = securityContext.getVisibleArea(LAYER_ID);
		Assert.assertNotNull(geometry);
		PrecisionModel precisionModel  = new PrecisionModel(PrecisionModel.FLOATING);
		GeometryFactory geometryFactory = new GeometryFactory(precisionModel, LAYER_SRID);
		Coordinate coordinate = new Coordinate();

		coordinate.x = coordinate.y = -86;
		Assert.assertFalse(geometry.contains(geometryFactory.createPoint(coordinate)));
		coordinate.x = -85.05;
		coordinate.y = -85.05;
		Assert.assertTrue(geometry.contains(geometryFactory.createPoint(coordinate)));
		coordinate.x = -85.05;
		coordinate.y = 85.05;
		Assert.assertTrue(geometry.contains(geometryFactory.createPoint(coordinate)));
		coordinate.x = 85.05;
		coordinate.y = -85.05;
		Assert.assertTrue(geometry.contains(geometryFactory.createPoint(coordinate)));
		coordinate.x = 85.05;
		coordinate.y = 85.05;
		Assert.assertTrue(geometry.contains(geometryFactory.createPoint(coordinate)));
		coordinate.x = coordinate.y = 86;
		Assert.assertFalse(geometry.contains(geometryFactory.createPoint(coordinate)));

		Assert.assertFalse(securityContext.isPartlyVisibleSufficient(LAYER_ID));
	}

	@Test
	public void testDefaultVisibleAreaOne() throws Exception {
		DefaultSecurityContext securityContext = (DefaultSecurityContext)this.securityContext;
		List<Authentication> authentications = new ArrayList<Authentication>();
		Authentication auth1 = getAuthentication();
		Authentication auth2 = getAreaAuthentication(1);
		authentications.add(auth1);
		authentications.add(auth2);
		securityContext.setAuthentications("token", authentications);

		Geometry geometry = securityContext.getVisibleArea(LAYER_ID);
		Assert.assertNotNull(geometry);
		PrecisionModel precisionModel  = new PrecisionModel(PrecisionModel.FLOATING);
		GeometryFactory geometryFactory = new GeometryFactory(precisionModel, LAYER_SRID);
		Coordinate coordinate = new Coordinate();
		coordinate.x = coordinate.y = 0.5;
		Assert.assertFalse(geometry.contains(geometryFactory.createPoint(coordinate)));
		coordinate.x = coordinate.y = 1.5;
		Assert.assertTrue(geometry.contains(geometryFactory.createPoint(coordinate)));
		coordinate.x = coordinate.y = 2.5;
		Assert.assertTrue(geometry.contains(geometryFactory.createPoint(coordinate)));
		coordinate.x = coordinate.y = 3.5;
		Assert.assertFalse(geometry.contains(geometryFactory.createPoint(coordinate)));
		coordinate.x = coordinate.y = 4.5;
		Assert.assertFalse(geometry.contains(geometryFactory.createPoint(coordinate)));

		Assert.assertFalse(securityContext.isPartlyVisibleSufficient(LAYER_ID));
	}

	@Test
	public void testDefaultVisibleAreaTwo() throws Exception {
		DefaultSecurityContext securityContext = (DefaultSecurityContext)this.securityContext;
		List<Authentication> authentications = new ArrayList<Authentication>();
		Authentication auth1 = getAreaAuthentication(1);
		Authentication auth2 = getAreaAuthentication(2);
		authentications.add(auth1);
		authentications.add(auth2);
		securityContext.setAuthentications("token", authentications);

		Geometry geometry = securityContext.getVisibleArea(LAYER_ID);
		Assert.assertNotNull(geometry);
		PrecisionModel precisionModel  = new PrecisionModel(PrecisionModel.FLOATING);
		GeometryFactory geometryFactory = new GeometryFactory(precisionModel, LAYER_SRID);
		Coordinate coordinate = new Coordinate();
		coordinate.x = coordinate.y = 0.5;
		Assert.assertFalse(geometry.contains(geometryFactory.createPoint(coordinate)));
		coordinate.x = coordinate.y = 1.5;
		Assert.assertFalse(geometry.contains(geometryFactory.createPoint(coordinate)));
		coordinate.x = coordinate.y = 2.5;
		Assert.assertTrue(geometry.contains(geometryFactory.createPoint(coordinate)));
		coordinate.x = coordinate.y = 3.5;
		Assert.assertFalse(geometry.contains(geometryFactory.createPoint(coordinate)));
		coordinate.x = coordinate.y = 4.5;
		Assert.assertFalse(geometry.contains(geometryFactory.createPoint(coordinate)));

		Assert.assertFalse(securityContext.isPartlyVisibleSufficient(LAYER_ID));
	}

	private Authentication getAuthentication() {
		Authentication auth = new Authentication();
		auth.setAuthorizations(new BaseAuthorization[]{new AllowAllAuthorization()});
		auth.setSecurityServiceId(SECURITY_SERVICE_ID);
		auth.setUserId(USER_ID);
		return auth;
	}

	private Authentication getBaseAuthentication() {
		Authentication auth = new Authentication();
		auth.setAuthorizations(new BaseAuthorization[]{new AllowBaseAuthorization()});
		auth.setSecurityServiceId(SECURITY_SERVICE_ID);
		auth.setUserId(USER_ID);
		return auth;
	}

	private Authentication getAreaAuthentication(int which) {
		Authentication auth = new Authentication();
		auth.setAuthorizations(new BaseAuthorization[]{new TestAuthorization(which)});
		auth.setSecurityServiceId(SECURITY_SERVICE_ID + which);
		auth.setUserId(USER_ID);
		return auth;
	}

	private class TestAuthorization extends AllowAllAuthorization implements AreaAuthorization {
		private Geometry geometry;
		private boolean partly;
		
		public TestAuthorization(int which) {
			Envelope envelope;
			if (1 == which) {
				partly = false;
				 envelope = new Envelope(1, 3, 1, 3);
			} else {
				partly = true;
				 envelope = new Envelope(2, 4, 2, 4);
			}
			PrecisionModel precisionModel  = new PrecisionModel(PrecisionModel.FLOATING);
			GeometryFactory geometryFactory = new GeometryFactory(precisionModel, LAYER_SRID);
			geometry = geometryFactory.toGeometry(envelope);
		}

		public Geometry getVisibleArea(String layerId) {
			return geometry;
		}

		public boolean isPartlyVisibleSufficient(String layerId) {
			return partly;
		}

		public Geometry getUpdateAuthorizedArea(String layerId) {
			return geometry;
		}

		public boolean isPartlyUpdateAuthorizedSufficient(String layerId) {
			return partly;
		}

		public Geometry getCreateAuthorizedArea(String layerId) {
			return geometry;
		}

		public boolean isPartlyCreateAuthorizedSufficient(String layerId) {
			return partly;
		}

		public Geometry getDeleteAuthorizedArea(String layerId) {
			return geometry;
		}

		public boolean isPartlyDeleteAuthorizedSufficient(String layerId) {
			return partly;
		}
	}

}
