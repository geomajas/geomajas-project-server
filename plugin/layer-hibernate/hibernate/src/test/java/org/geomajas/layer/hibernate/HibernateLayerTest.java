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
import org.opengis.filter.Filter;

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
	public void testSave() throws Exception {
		HibernateTestFeature feature = HibernateTestFeature.getDefaultInstance1(null);
		Object created = layer.saveOrUpdate(feature);
		Assert.assertNotNull(created);
		Assert.assertTrue(created instanceof HibernateTestFeature);
		HibernateTestFeature createdFeature = (HibernateTestFeature) created;
		Assert.assertNotNull(createdFeature.getId());
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

	@Test
	public void testScrollableResultSet() throws Exception {
		// @todo this is arguably not a good unittest, there is no certainty a scrollable resultset is actually used
		HibernateTestFeature f1 = (HibernateTestFeature) scrollableResultSetLayer.create(HibernateTestFeature
				.getDefaultInstance1(null));
		HibernateTestFeature f2 = (HibernateTestFeature) scrollableResultSetLayer.create(HibernateTestFeature
				.getDefaultInstance2(null));
		Iterator<?> iterator = scrollableResultSetLayer.getElements(null, 0, 0);

		Assert.assertTrue(iterator.hasNext());
		Assert.assertEquals(f2, iterator.next());
		Assert.assertTrue(iterator.hasNext());
		Assert.assertEquals(f1, iterator.next());
		Assert.assertFalse(iterator.hasNext());
	}

	@Test
	public void testEmptyScrollableResultSet() throws Exception {
		// @todo this is arguably not a good unittest, there is no certainty a scrollable resultset is actually used
		Iterator<?> iterator = scrollableResultSetLayer.getElements(null, 0, 0);

		Assert.assertFalse(iterator.hasNext());
	}

	@Test
	public void testGetAttributes() throws Exception {
		List<Attribute<?>> attributes = associationLayer.getAttributes(PARAM_MANY_TO_ONE, Filter.INCLUDE);
		Assert.assertNotNull(attributes);
	}
}
