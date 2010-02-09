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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import junit.framework.Assert;
import org.geomajas.security.AreaAuthorization;
import org.geomajas.security.Authentication;
import org.geomajas.security.BaseAuthorization;
import org.geomajas.security.SecurityContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Testing of AreaAuthorization related data in the security context.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/layer/bean/beanContext.xml", "/org/geomajas/layer/bean/layerBeans.xml"})
public class SecurityContextAreaAuthorizationTest {

	private static final String LAYER_ID = "beans";
	private static final int LAYER_SRID = 4326;

	private static final String SECURITY_SERVICE_ID = "ss";
	private static final String USER_ID = "auth";

	@Autowired
	private SecurityContext securityContext;

	@Test
	public void testDefaultVisibleArea() throws Exception {
		SecurityContextImpl securityContext = (SecurityContextImpl)this.securityContext;
		securityContext.setAuthentications(null, null);
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

		Assert.assertTrue(securityContext.isPartlyVisibleSufficient(LAYER_ID));
	}

	@Test
	public void testDefaultVisibleAreaOne() throws Exception {
		SecurityContextImpl securityContext = (SecurityContextImpl)this.securityContext;
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
		SecurityContextImpl securityContext = (SecurityContextImpl)this.securityContext;
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
