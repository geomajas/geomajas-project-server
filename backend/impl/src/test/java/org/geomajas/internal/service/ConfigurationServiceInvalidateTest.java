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

package org.geomajas.internal.service;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.PrecisionModel;
import junit.framework.Assert;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.bean.BeanLayer;
import org.geomajas.layer.bean.FeatureBean;
import org.geomajas.layer.feature.Feature;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.security.SecurityManager;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;
import org.geomajas.service.TestRecorder;
import org.junit.Before;
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
import java.util.Iterator;
import java.util.List;

/**
 * Tests for VectorLayerService.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/layer/bean/beanContext.xml", "/org/geomajas/layer/bean/layerBeans.xml",
		"/org/geomajas/spring/testRecorder.xml", "/org/geomajas/internal/service/layerInvalidateContext.xml"})
public class ConfigurationServiceInvalidateTest {

	private static final String LAYER_ID = "beans";

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private SecurityManager securityManager;

	@Autowired
	private TestRecorder recorder;

	@Before
	public void login() {
		// assure security context is set
		securityManager.createSecurityContext(null);
	}

	@Test
	public void testInvalidate() throws Exception {
		recorder.clear();
		configurationService.invalidateLayer(LAYER_ID);
		Assert.assertEquals("", recorder.matches("beans", "Invalidate layer"));

		// assure no errors thrown for null or invalid layer if
		configurationService.invalidateLayer(null);
		configurationService.invalidateLayer("invalid-layer-id");
	}

	@Test
	public void testInvalidateAll() throws Exception {
		recorder.clear();
		configurationService.invalidateAllLayers();
		Assert.assertEquals("", recorder.matches("beans", "Invalidate layer"));
	}
}
