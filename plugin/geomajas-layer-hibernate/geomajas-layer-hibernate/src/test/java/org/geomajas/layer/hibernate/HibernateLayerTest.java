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
package org.geomajas.layer.hibernate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.attribute.BooleanAttribute;
import org.geomajas.layer.feature.attribute.DateAttribute;
import org.geomajas.layer.feature.attribute.FloatAttribute;
import org.geomajas.layer.feature.attribute.ManyToOneAttribute;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.layer.hibernate.pojo.HibernateTestFeature;
import org.geomajas.layer.hibernate.pojo.HibernateTestManyToOne;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test that tests all the functions of the HibernateLayer.
 * 
 * @author Pieter De Graef
 */
public class HibernateLayerTest extends AbstractHibernateLayerModelTest {

	@Test
	public void testCreate() throws Exception {
		HibernateTestFeature feature = HibernateTestFeature.getDefaultInstance1(null);
		Object created = layer.create(feature);
		Assert.assertNotNull(created);
		Assert.assertTrue(created instanceof HibernateTestFeature);
		HibernateTestFeature createdFeature = (HibernateTestFeature) created;
		Assert.assertNotNull(createdFeature.getId());
	}

	@Test
	public void testRead() throws Exception {
		HibernateTestFeature f1 = (HibernateTestFeature) layer.create(HibernateTestFeature.getDefaultInstance1(null));
		Assert.assertNotNull(f1.getId());
		Object feature = layer.read(f1.getId().toString());
		Assert.assertNotNull(feature);
	}

	@Test
	public void testUpdate() throws Exception {
		HibernateTestFeature f1 = (HibernateTestFeature) layer.create(HibernateTestFeature.getDefaultInstance1(null));
		Assert.assertNotNull(f1.getId());
		Object feature = layer.read(f1.getId().toString());
		Assert.assertNotNull("The requested feature could not be found!", feature);

		// Create a detached copy
		HibernateTestFeature detached = HibernateTestFeature.getDefaultInstance1(null);
		detached.setId(((HibernateTestFeature) feature).getId());
		Map<String, Attribute> attributes = new HashMap<String, Attribute>();
		attributes.put(PARAM_TEXT_ATTR, new StringAttribute("new name"));
		attributes.put(PARAM_FLOAT_ATTR, new FloatAttribute(5f));
		Calendar c = Calendar.getInstance();
		attributes.put(PARAM_DATE_ATTR, new DateAttribute(c.getTime()));
		attributes.put(PARAM_BOOLEAN_ATTR, new BooleanAttribute(false));

		// Set a ManyToOne attribute without an ID (a new one)
		attributes.put(PARAM_MANY_TO_ONE, HibernateTestManyToOne.getDefaultAttributeInstance1(null));

		layer.getFeatureModel().setAttributes(detached, attributes);
		layer.saveOrUpdate(detached);

		feature = layer.read(f1.getId().toString());
		Assert.assertEquals("new name", layer.getFeatureModel().getAttribute(feature, PARAM_TEXT_ATTR).getValue());
		Assert.assertEquals(5f, layer.getFeatureModel().getAttribute(feature, PARAM_FLOAT_ATTR).getValue());
		Assert.assertEquals(c.getTime(), layer.getFeatureModel().getAttribute(feature, PARAM_DATE_ATTR).getValue());
		Assert.assertEquals(false, layer.getFeatureModel().getAttribute(feature, PARAM_BOOLEAN_ATTR).getValue());
		ManyToOneAttribute manytoOne = (ManyToOneAttribute) layer.getFeatureModel().getAttribute(feature,
				PARAM_MANY_TO_ONE);
		Assert.assertNotNull(manytoOne.getValue());
		Assert.assertNotNull(manytoOne.getValue().getId()); // Test for ID
	}

	@Test
	public void testGetBounds() {
		// TODO implement me
	}

	@Test
	public void testGetBoundsFilter() {
		// TODO implement me
	}

	@Test
	public void testGetElements() throws Exception {
		layer.create(HibernateTestFeature.getDefaultInstance2(null));
		Object feature = null;
		Iterator<?> iterator = layer.getElements(null, 0, 0);
		if (iterator.hasNext()) {
			feature = iterator.next();
		}
		Assert.assertNotNull(feature);
	}

	@Test
	public void testDelete() throws Exception {
		HibernateTestFeature f1 = (HibernateTestFeature) layer.create(HibernateTestFeature.getDefaultInstance1(null));
		Assert.assertNotNull(f1.getId());
		Assert.assertNotNull(layer.read(f1.getId().toString()));
		layer.delete(f1.getId().toString());
		try {
			layer.read(f1.getId().toString());
			Assert.fail("No exception thrown for non-exisiting feature");
		} catch (LayerException e) {
		}
	}

	@Test
	public void testSort() throws Exception {
		HibernateTestFeature f1 = (HibernateTestFeature) layer.create(HibernateTestFeature.getDefaultInstance1(null));
		HibernateTestFeature f2 = (HibernateTestFeature) layer.create(HibernateTestFeature.getDefaultInstance2(null));
		Iterator<?> iterator = layer.getElements(null, 0, 0);
		List<Object> actual = new ArrayList<Object>();
		while (iterator.hasNext()) {
			actual.add(iterator.next());
		}
		List<Object> expected = new ArrayList<Object>();
		expected.add(HibernateTestFeature.getDefaultInstance2(f2.getId()));
		expected.add(HibernateTestFeature.getDefaultInstance1(f1.getId()));
		Assert.assertEquals(expected, actual);
	}
}
