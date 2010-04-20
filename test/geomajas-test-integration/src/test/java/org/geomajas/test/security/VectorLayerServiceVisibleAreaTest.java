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

package org.geomajas.test.security;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import junit.framework.Assert;
import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandResponse;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.security.GeomajasSecurityException;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.bean.BeanLayer;
import org.geomajas.layer.feature.Feature;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.plugin.springsecurity.command.dto.LoginRequest;
import org.geomajas.plugin.springsecurity.command.dto.LoginResponse;
import org.geomajas.security.SecurityManager;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.FilterService;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for proper application of security in {@link VectorLayerServiceInvisibleLayerTest}.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/layer/bean/beanContext.xml", "/org/geomajas/layer/bean/layerBeans.xml",
		"/org/geomajas/test/security/VectorLayerSecurityArea.xml"})
public class VectorLayerServiceVisibleAreaTest {

	private static final String LAYER_ID = "beans";
	private static final String STRING_ATTR = "stringAttr";
	private static final double ALLOWANCE = .00000001;

	@Autowired
	private VectorLayerService layerService;

	@Autowired
	@Qualifier("beans")
	private BeanLayer beanLayer;

	@Autowired
	private SecurityManager securityManager;

	@Autowired
	private CommandDispatcher commandDispatcher;

	@Autowired
	private DtoConverterService converterService;

	@Autowired
	private FilterService filterService;

	// assure we are logged in as a specific user to set correct authorizations
	public void login(String name) {
		LoginRequest request = new LoginRequest();
		request.setLogin(name);
		request.setPassword(name);
		CommandResponse response = commandDispatcher.execute("command.Login", request, null, "en");
		Assert.assertFalse(response.isError());
		Assert.assertTrue(response instanceof LoginResponse);
		securityManager.createSecurityContext(((LoginResponse)response).getToken());
	}

	@Test
	public void testGetFeaturesVisibleArea() throws Exception {
		List<InternalFeature> features;
		CoordinateReferenceSystem crs = CRS.decode(beanLayer.getLayerInfo().getCrs());

		login("luc");
		features = layerService.getFeatures(LAYER_ID,crs, null, null, VectorLayerService.FEATURE_INCLUDE_NONE);
		Assert.assertEquals(3, features.size());

		login("marino");
		features = layerService.getFeatures(LAYER_ID, crs, null, null, VectorLayerService.FEATURE_INCLUDE_NONE);
		Assert.assertEquals(2, features.size());
		int check = 0;
		for (InternalFeature feature : features) {
			check |= 1 << (Integer.parseInt(feature.getId()) - 1);
		}
		Assert.assertEquals(6, check);

	}

	@Test
	public void testGetBoundsVisibleArea() throws Exception {
		CoordinateReferenceSystem crs = CRS.decode(beanLayer.getLayerInfo().getCrs());
		Envelope envelope;

		login("luc");
		envelope = layerService.getBounds(LAYER_ID, crs, null);
		Assert.assertEquals(0, envelope.getMinX(), ALLOWANCE);
		Assert.assertEquals(0, envelope.getMinY(), ALLOWANCE);
		Assert.assertEquals(7, envelope.getMaxX(), ALLOWANCE);
		Assert.assertEquals(3, envelope.getMaxY(), ALLOWANCE);

		login("marino");
		envelope = layerService.getBounds(LAYER_ID, crs, null);
		Assert.assertEquals(2, envelope.getMinX(), ALLOWANCE);
		Assert.assertEquals(0, envelope.getMinY(), ALLOWANCE);
		Assert.assertEquals(7, envelope.getMaxX(), ALLOWANCE);
		Assert.assertEquals(3, envelope.getMaxY(), ALLOWANCE);
	}

	@Test
	@DirtiesContext
	public void testSaveOrUpdateUpdateArea() throws Exception {
		Filter filter;
		List<InternalFeature> oldFeatures;
		List<InternalFeature> newFeatures;
		InternalFeature feature;
		CoordinateReferenceSystem crs = CRS.decode(beanLayer.getLayerInfo().getCrs());

		login("marino");
		// should be able to update feature "2"
		filter = filterService.createFidFilter(new String[]{"2"});
		oldFeatures = layerService.getFeatures(LAYER_ID, crs, filter, null,
				VectorLayerService.FEATURE_INCLUDE_ATTRIBUTES);
		Assert.assertEquals(1, oldFeatures.size());
		feature = oldFeatures.get(0);
		newFeatures = new ArrayList<InternalFeature>();
		feature = feature.clone();
		newFeatures.add(feature);
		feature.getAttributes().put(STRING_ATTR, new StringAttribute("changed"));
		layerService.saveOrUpdate(LAYER_ID, crs, oldFeatures, newFeatures);
		// should not be able to update feature "3"
		filter = filterService.createFidFilter(new String[]{"3"});
		oldFeatures = layerService.getFeatures(LAYER_ID, crs, filter, null,
				VectorLayerService.FEATURE_INCLUDE_ATTRIBUTES);
		Assert.assertEquals(1, oldFeatures.size());
		feature = oldFeatures.get(0);
		newFeatures = new ArrayList<InternalFeature>();
		feature = feature.clone();
		newFeatures.add(feature);
		feature.getAttributes().put(STRING_ATTR, new StringAttribute("changed"));
		try {
			layerService.saveOrUpdate(LAYER_ID, crs, oldFeatures, newFeatures);
			Assert.fail("update area not checked when updating");
		} catch (GeomajasException ge) {
			Assert.assertEquals(ExceptionCode.FEATURE_UPDATE_PROHIBITED, ge.getExceptionCode());
		}
	}

	@Test
	@DirtiesContext
	public void testSaveOrUpdateDeleteArea() throws Exception {
		Filter filter;
		List<InternalFeature> oldFeatures;
		List<InternalFeature> newFeatures;
		InternalFeature feature;
		CoordinateReferenceSystem crs = CRS.decode(beanLayer.getLayerInfo().getCrs());

		login("marino");
		// should not be able to delete feature "2"
		filter = filterService.createFidFilter(new String[]{"2"});
		oldFeatures = layerService.getFeatures(LAYER_ID, crs, filter, null,
				VectorLayerService.FEATURE_INCLUDE_ATTRIBUTES);
		Assert.assertEquals(1, oldFeatures.size());
		feature = oldFeatures.get(0);
		newFeatures = new ArrayList<InternalFeature>();
		try {
			layerService.saveOrUpdate(LAYER_ID, crs, oldFeatures, newFeatures);
			Assert.fail("delete area not checked");
		} catch (GeomajasException ge) {
			Assert.assertEquals(ExceptionCode.FEATURE_DELETE_PROHIBITED, ge.getExceptionCode());
		}
		// should be able to delete feature "1"
		filter = filterService.createFidFilter(new String[]{"3"});
		oldFeatures = layerService.getFeatures(LAYER_ID, crs, filter, null,
				VectorLayerService.FEATURE_INCLUDE_ATTRIBUTES);
		Assert.assertEquals(1, oldFeatures.size());
		feature = oldFeatures.get(0);
		newFeatures = new ArrayList<InternalFeature>();
		layerService.saveOrUpdate(LAYER_ID, crs, oldFeatures, newFeatures);
		filter = filterService.createFidFilter(new String[]{"3"});
		oldFeatures = layerService.getFeatures(LAYER_ID,
				crs, filter, null, VectorLayerService.FEATURE_INCLUDE_ATTRIBUTES);
		Assert.assertEquals(0, oldFeatures.size());
	}

	@Test
	@DirtiesContext
	public void testSaveOrUpdateCreateArea() throws Exception {
		Filter filter;
		List<InternalFeature> oldFeatures;
		List<InternalFeature> newFeatures;
		InternalFeature feature;
		CoordinateReferenceSystem crs = CRS.decode(beanLayer.getLayerInfo().getCrs());
		GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel());
		Geometry geometry;

		login("marino");
		// verify failing
		oldFeatures = new ArrayList<InternalFeature>();
		newFeatures = new ArrayList<InternalFeature>();
		feature = converterService.toInternal(new Feature());
		feature.setId("4");
		feature.setLayer(beanLayer);
		// feature needs a geometry or exceptions all over
		geometry = geometryFactory.createPoint(new Coordinate(5.5, 5.5));
		feature.setGeometry(geometry);
		newFeatures.add(feature);
		try {
			layerService.saveOrUpdate(LAYER_ID, crs, oldFeatures, newFeatures);
			Assert.fail("create area not checked");
		} catch (GeomajasSecurityException gse) {
			Assert.assertEquals(ExceptionCode.FEATURE_CREATE_PROHIBITED, gse.getExceptionCode());
		}
		filter = filterService.createFidFilter(new String[]{"4"});
		oldFeatures = layerService.getFeatures(LAYER_ID,
				crs, filter, null, VectorLayerService.FEATURE_INCLUDE_ATTRIBUTES);
		Assert.assertEquals(0, oldFeatures.size());
		// verify success
		oldFeatures = new ArrayList<InternalFeature>();
		newFeatures = new ArrayList<InternalFeature>();
		feature = converterService.toInternal(new Feature());
		feature.setId("4");
		feature.setLayer(beanLayer);
		// feature needs a geometry or exceptions all over
		geometry = geometryFactory.createPoint(new Coordinate(1.5, 1.5));
		feature.setGeometry(geometry);
		newFeatures.add(feature);
		layerService.saveOrUpdate(LAYER_ID, crs, oldFeatures, newFeatures);
		filter = filterService.createFidFilter(new String[]{"4"});
		oldFeatures = layerService.getFeatures(LAYER_ID,
				crs, filter, null, VectorLayerService.FEATURE_INCLUDE_ATTRIBUTES);
		Assert.assertEquals(1, oldFeatures.size());
	}

}