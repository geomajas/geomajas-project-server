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
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import junit.framework.Assert;
import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandResponse;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasSecurityException;
import org.geomajas.layer.bean.BeanLayer;
import org.geomajas.layer.feature.Feature;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.plugin.springsecurity.command.dto.LoginRequest;
import org.geomajas.plugin.springsecurity.command.dto.LoginResponse;
import org.geomajas.security.SecurityManager;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.FilterService;
import org.geomajas.service.VectorLayerService;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
		"/org/geomajas/test/security/VectorLayerSecurityFeature.xml"})
public class VectorLayerServiceFeatureTest {

	private static final String LAYER_ID = "beans";
	private static final String STRING_ATTR = "stringAttr";

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
	public void testGetFeaturesFeatureAuthorization() throws Exception {
		List<InternalFeature> features;
		CoordinateReferenceSystem crs = CRS.decode(beanLayer.getLayerInfo().getCrs());

		login("luc");
		features = layerService.getFeatures(LAYER_ID, crs, null, null, VectorLayerService.FEATURE_INCLUDE_NONE);
		Assert.assertEquals(3, features.size());
		Assert.assertTrue(features.get(0).isEditable());
		Assert.assertTrue(features.get(0).isDeletable());
		Assert.assertTrue(features.get(1).isEditable());
		Assert.assertTrue(features.get(1).isDeletable());
		Assert.assertTrue(features.get(2).isEditable());
		Assert.assertTrue(features.get(2).isDeletable());

		login("marino");
		features = layerService.getFeatures(LAYER_ID, crs, null, null, VectorLayerService.FEATURE_INCLUDE_NONE);
		Assert.assertEquals(2, features.size());
		for (InternalFeature feature : features) {
			if ("1".equals(feature.getId())) {
				Assert.assertTrue(feature.isEditable());
				Assert.assertTrue(feature.isDeletable());
			} else if ("3".equals(feature.getId())) {
				Assert.assertFalse(feature.isEditable());
				Assert.assertFalse(feature.isDeletable());
			} else {
				Assert.fail("wrong attribute returned " + feature);
			}
		}
	}

	@Test
	public void testSaveOrUpdateFeatureWritable() throws Exception {
		Filter filter;
		List<InternalFeature> oldFeatures;
		List<InternalFeature> newFeatures;
		InternalFeature feature;
		CoordinateReferenceSystem crs = CRS.decode(beanLayer.getLayerInfo().getCrs());

		login("marino");
		// should be able to update feature "1"
		filter = filterService.createFidFilter(new String[]{"1"});
		oldFeatures = layerService.getFeatures(LAYER_ID, crs, filter, null, 
				VectorLayerService.FEATURE_INCLUDE_ATTRIBUTES);
		Assert.assertEquals(1, oldFeatures.size());
		feature = oldFeatures.get(0);
		newFeatures = new ArrayList<InternalFeature>();
		feature = feature.clone();
		newFeatures.add(feature);
		feature.getAttributes().put(STRING_ATTR, new StringAttribute("changed"));
		layerService.saveOrUpdate(LAYER_ID, crs, oldFeatures, newFeatures);
		// should not able to update feature "3"
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
			Assert.fail("should have throw GeomajasSecurityException");
		} catch (GeomajasSecurityException gse) {
			Assert.assertEquals(ExceptionCode.FEATURE_UPDATE_PROHIBITED, gse.getExceptionCode());
		}

		login("luc");
		// luc should be able to modify "3"
		filter = filterService.createFidFilter(new String[]{"3"});
		oldFeatures = layerService.getFeatures(LAYER_ID,
				crs, filter, null, VectorLayerService.FEATURE_INCLUDE_ATTRIBUTES);
		Assert.assertEquals(1, oldFeatures.size());
		feature = oldFeatures.get(0);
		newFeatures = new ArrayList<InternalFeature>();
		feature = feature.clone();
		newFeatures.add(feature);
		feature.getAttributes().put(STRING_ATTR, new StringAttribute("changed"));
		layerService.saveOrUpdate(LAYER_ID, crs, oldFeatures, newFeatures);
	}

	@Test
	public void testSaveOrUpdateDeleteFeature() throws Exception {
		Filter filter;
		List<InternalFeature> oldFeatures;
		List<InternalFeature> newFeatures;
		CoordinateReferenceSystem crs = CRS.decode(beanLayer.getLayerInfo().getCrs());

		login("marino");
		filter = filterService.createFidFilter(new String[]{"3"});
		oldFeatures = layerService.getFeatures(LAYER_ID,
				crs, filter, null, VectorLayerService.FEATURE_INCLUDE_ATTRIBUTES);
		Assert.assertEquals(1, oldFeatures.size());
		newFeatures = new ArrayList<InternalFeature>();
		try {
			layerService.saveOrUpdate(LAYER_ID, crs, oldFeatures, newFeatures);
			Assert.fail("update should have failed");
		} catch (GeomajasSecurityException gse) {
			Assert.assertEquals(ExceptionCode.FEATURE_DELETE_PROHIBITED, gse.getExceptionCode());
		}

		login("luc");
		filter = filterService.createFidFilter(new String[]{"3"});
		oldFeatures = layerService.getFeatures(LAYER_ID,
				crs, filter, null, VectorLayerService.FEATURE_INCLUDE_ATTRIBUTES);
		Assert.assertEquals(1, oldFeatures.size());
		newFeatures = new ArrayList<InternalFeature>();
		layerService.saveOrUpdate(LAYER_ID, crs, oldFeatures, newFeatures);
		oldFeatures = layerService.getFeatures(LAYER_ID,
				crs, filter, null, VectorLayerService.FEATURE_INCLUDE_ATTRIBUTES);
		Assert.assertEquals(0, oldFeatures.size());
	}

	@Test
	public void testSaveOrUpdateCreateFeature() throws Exception {
		Filter filter;
		List<InternalFeature> oldFeatures;
		List<InternalFeature> newFeatures;
		InternalFeature feature;
		CoordinateReferenceSystem crs = CRS.decode(beanLayer.getLayerInfo().getCrs());
		GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel());
		Geometry geometry;

		login("marino");
		oldFeatures = new ArrayList<InternalFeature>();
		newFeatures = new ArrayList<InternalFeature>();
		feature = converterService.toInternal(new Feature());
		feature.setId("4");
		feature.setLayer(beanLayer);
		// feature needs a geometry or exceptions all over
		geometry = geometryFactory.createPoint(new Coordinate(1.5, 1.5));
		feature.setGeometry(geometry);		
		newFeatures.add(feature);
		try {
			layerService.saveOrUpdate(LAYER_ID, crs, oldFeatures, newFeatures);
			Assert.fail("create should have failed");
		} catch (GeomajasSecurityException gse) {
			Assert.assertEquals(ExceptionCode.FEATURE_CREATE_PROHIBITED, gse.getExceptionCode());
		}
		filter = filterService.createFidFilter(new String[]{"4"});
		oldFeatures = layerService.getFeatures(LAYER_ID,
				crs, filter, null, VectorLayerService.FEATURE_INCLUDE_ATTRIBUTES);
		Assert.assertEquals(0, oldFeatures.size());

		login("luc");
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