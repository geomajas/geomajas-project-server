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
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

import junit.framework.Assert;
import org.geomajas.layer.bean.BeanLayer;
import org.geomajas.layer.bean.FeatureBean;
import org.geomajas.layer.feature.Feature;
import org.geomajas.layer.feature.InternalFeature;
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
import java.util.Iterator;
import java.util.List;

/**
 * Tests for VectorLayerService.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/layer/bean/beanContext.xml", "/org/geomajas/layer/bean/layerBeans.xml"})
public class VectorLayerServiceTest {

	private static final String LAYER_ID = "beans";
	private static final String STRING_ATTR = "stringAttr";
	private static final double ALLOWANCE = .00000001;

	@Autowired
	private VectorLayerService layerService;

	@Autowired
	@Qualifier("beans")
	private BeanLayer beanLayer;

	@Autowired
	private FilterService filterService;

	@Autowired
	private DtoConverterService converterService;

	@Test
	public void testUpdate() throws Exception {
		Filter filter = filterService.createFidFilter(new String[]{"3"});
		CoordinateReferenceSystem crs = CRS.decode(beanLayer.getLayerInfo().getCrs());
		List<InternalFeature> oldFeatures = layerService.getFeatures(LAYER_ID,
				crs, filter, null, VectorLayerService.FEATURE_INCLUDE_ATTRIBUTES);
		Assert.assertEquals(1, oldFeatures.size());
		InternalFeature feature = oldFeatures.get(0);
		List<InternalFeature> newFeatures = new ArrayList<InternalFeature>();
		feature = feature.clone();
		feature.getAttributes().put(STRING_ATTR, "changed");
		newFeatures.add(feature);
		feature.getAttributes().put(STRING_ATTR, "changed");
		layerService.saveOrUpdate(LAYER_ID, crs, oldFeatures, newFeatures);

		Iterator<FeatureBean> iterator =
				(Iterator<FeatureBean>) beanLayer.getElements(filterService.createTrueFilter(), 0, 0);
		int count = 0;
		int check = 0;
		while (iterator.hasNext()) {
			FeatureBean featureBean = iterator.next();
			count++;
			if (3 == featureBean.getId()) {
				Assert.assertEquals("changed", featureBean.getStringAttr());
			}
			check |= 1 << (featureBean.getId() - 1);
		}
		Assert.assertEquals(3, count);
		Assert.assertEquals(7, check);
	}

	@Test
	public void testCreateDelete() throws Exception {
		// done in one test to assure the state is back to what is expected,
		// the spring context is not rebuilt between test methods
		
		// Create first
		CoordinateReferenceSystem crs = CRS.decode(beanLayer.getLayerInfo().getCrs());
		List<InternalFeature> oldFeatures = new ArrayList<InternalFeature>();
		List<InternalFeature> newFeatures = new ArrayList<InternalFeature>();
		InternalFeature feature = converterService.toInternal(new Feature());
		feature.setId("beans.4");
		feature.setLayer(beanLayer);
		newFeatures.add(feature);
		layerService.saveOrUpdate(LAYER_ID, crs, oldFeatures, newFeatures);

		Iterator<FeatureBean> iterator =
				(Iterator<FeatureBean>) beanLayer.getElements(filterService.createTrueFilter(), 0, 0);
		int count = 0;
		int check = 0;
		while (iterator.hasNext()) {
			FeatureBean featureBean = iterator.next();
			count++;
			check |= 1 << (featureBean.getId() - 1);
		}
		Assert.assertEquals(4, count);
		Assert.assertEquals(15, check);

		// now delete again
		Filter filter = filterService.createFidFilter(new String[]{"4"});
		oldFeatures = layerService.getFeatures(LAYER_ID,
				crs, filter, null, VectorLayerService.FEATURE_INCLUDE_ATTRIBUTES);
		Assert.assertEquals(1, oldFeatures.size());
		newFeatures = new ArrayList<InternalFeature>();
		layerService.saveOrUpdate(LAYER_ID, crs, oldFeatures, newFeatures);

		iterator = (Iterator<FeatureBean>) beanLayer.getElements(filterService.createTrueFilter(), 0, 0);
		count = 0;
		check = 0;
		while (iterator.hasNext()) {
			FeatureBean featureBean = iterator.next();
			count++;
			check |= 1 << (featureBean.getId() - 1);
		}
		Assert.assertEquals(3, count);
		Assert.assertEquals(7, check);

	}

	@Test
	public void testGetFeaturesLazy() throws Exception {
		List<InternalFeature> features = layerService.getFeatures(LAYER_ID,
				CRS.decode(beanLayer.getLayerInfo().getCrs()), null, null, VectorLayerService.FEATURE_INCLUDE_NONE);
		Assert.assertEquals(3, features.size());
		InternalFeature feature = features.get(0);
		Assert.assertNotNull(feature.getId());
		Assert.assertNull(feature.getGeometry());
		Assert.assertNull(feature.getAttributes());
		Assert.assertNull(feature.getLabel());
		Assert.assertNull(feature.getStyleInfo());
	}

	@Test
	public void testGetFeaturesAllFiltered() throws Exception {
		Filter filter = filterService.createFidFilter(new String[]{"3"});
		List<InternalFeature> features = layerService.getFeatures(LAYER_ID,
				CRS.decode(beanLayer.getLayerInfo().getCrs()), filter, null, VectorLayerService.FEATURE_INCLUDE_ALL);
		Assert.assertEquals(1, features.size());
		InternalFeature feature = features.get(0);
		Assert.assertEquals("3", feature.getLocalId());
		Assert.assertNotNull(feature.getGeometry());
		Assert.assertNotNull(feature.getAttributes().get(STRING_ATTR));
	}

	@Test
	public void testGetBoundsAll() throws Exception {
		Envelope bounds = layerService.getBounds(LAYER_ID, CRS.decode(beanLayer.getLayerInfo().getCrs()), null);
		Assert.assertEquals(0, bounds.getMinX(), ALLOWANCE);
		Assert.assertEquals(0, bounds.getMinY(), ALLOWANCE);
		Assert.assertEquals(7, bounds.getMaxX(), ALLOWANCE);
		Assert.assertEquals(3, bounds.getMaxY(), ALLOWANCE);
	}

	@Test
	public void testGetBoundsFidFiltered() throws Exception {
		Filter filter = filterService.createFidFilter(new String[]{"2", "3"});
		Envelope bounds = layerService.getBounds(LAYER_ID, CRS.decode(beanLayer.getLayerInfo().getCrs()), filter);
		Assert.assertEquals(2, bounds.getMinX(), ALLOWANCE);
		Assert.assertEquals(0, bounds.getMinY(), ALLOWANCE);
		Assert.assertEquals(7, bounds.getMaxX(), ALLOWANCE);
		Assert.assertEquals(3, bounds.getMaxY(), ALLOWANCE);
	}
	
	@Test
	public void testGetBoundsIntersectsFiltered() throws Exception {
		GeometryFactory factory = new GeometryFactory();
		LineString equator = factory.createLineString(new Coordinate[] { new Coordinate(0, 0),
				new Coordinate(-180, 180) });
		Filter filter = filterService.createIntersectsFilter(equator,beanLayer.getLayerInfo().getFeatureInfo().getGeometryType().getName());
		Envelope bounds = layerService.getBounds(LAYER_ID, CRS.decode(beanLayer.getLayerInfo().getCrs()), filter);
		Assert.assertEquals(0, bounds.getMinX(), ALLOWANCE);
		Assert.assertEquals(0, bounds.getMinY(), ALLOWANCE);
		Assert.assertEquals(1, bounds.getMaxX(), ALLOWANCE);
		Assert.assertEquals(1, bounds.getMaxY(), ALLOWANCE);
	}

	// @todo should also test the getObjects() method.

}
