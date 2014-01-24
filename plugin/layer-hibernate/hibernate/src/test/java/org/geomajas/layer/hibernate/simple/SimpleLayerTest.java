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
package org.geomajas.layer.hibernate.simple;

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
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.layer.hibernate.AbstractHibernateSimpleTest;
import org.geomajas.layer.hibernate.simple.pojo.SimpleFeature;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test that tests all the functions of the HibernateLayer.
 * 
 * @author Pieter De Graef
 */
public class SimpleLayerTest extends AbstractHibernateSimpleTest {

	@Test
	public void testCreate() throws Exception {
		SimpleFeature feature = SimpleFeature.getDefaultInstance1(null);
		Object created = layer.create(feature);
		Assert.assertNotNull(created);
		Assert.assertTrue(created instanceof SimpleFeature);
		SimpleFeature createdFeature = (SimpleFeature) created;
		Assert.assertNotNull(createdFeature.getId());
	}

	@Test
	public void testRead() throws Exception {
		SimpleFeature f1 = (SimpleFeature) layer.create(SimpleFeature.getDefaultInstance1(null));
		Assert.assertNotNull(f1.getId());
		Object feature = layer.read(f1.getId().toString());
		Assert.assertNotNull(feature);
	}

	@Test
	@SuppressWarnings("rawtypes")
	public void testUpdate() throws Exception {
		SimpleFeature f1 = (SimpleFeature) layer.create(SimpleFeature.getDefaultInstance1(null));
		Assert.assertNotNull(f1.getId());
		Object feature = layer.read(f1.getId().toString());
		Assert.assertNotNull("The requested feature could not be found!", feature);

		// Create a detached copy
		SimpleFeature detached = SimpleFeature.getDefaultInstance1(null);
		detached.setId(((SimpleFeature) feature).getId());
		Map<String, Attribute> attributes = new HashMap<String, Attribute>();
		attributes.put(PARAM_TEXT_ATTR, new StringAttribute("new name"));
		attributes.put(PARAM_FLOAT_ATTR, new FloatAttribute(5f));
		Calendar c = Calendar.getInstance();
		attributes.put(PARAM_DATE_ATTR, new DateAttribute(c.getTime()));
		attributes.put(PARAM_BOOLEAN_ATTR, new BooleanAttribute(false));

		layer.getFeatureModel().setAttributes(detached, attributes);
		layer.saveOrUpdate(detached);

		feature = layer.read(f1.getId().toString());
		Assert.assertEquals("new name", layer.getFeatureModel().getAttribute(feature, PARAM_TEXT_ATTR).getValue());
		Assert.assertEquals(5f, layer.getFeatureModel().getAttribute(feature, PARAM_FLOAT_ATTR).getValue());
		Assert.assertEquals(c.getTime(), layer.getFeatureModel().getAttribute(feature, PARAM_DATE_ATTR).getValue());
		Assert.assertEquals(false, layer.getFeatureModel().getAttribute(feature, PARAM_BOOLEAN_ATTR).getValue());
	}

	@Test
	public void testSave() throws Exception {
		SimpleFeature feature = SimpleFeature.getDefaultInstance1(null);
		Object created = layer.saveOrUpdate(feature);
		Assert.assertNotNull(created);
		Assert.assertTrue(created instanceof SimpleFeature);
		SimpleFeature createdFeature = (SimpleFeature) created;
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
		layer.create(SimpleFeature.getDefaultInstance2(null));
		Object feature = null;
		Iterator<?> iterator = layer.getElements(null, 0, 0);
		if (iterator.hasNext()) {
			feature = iterator.next();
		}
		Assert.assertNotNull(feature);
	}

	@Test
	public void testDelete() throws Exception {
		SimpleFeature f1 = (SimpleFeature) layer.create(SimpleFeature.getDefaultInstance1(null));
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
		SimpleFeature f1 = (SimpleFeature) layer.create(SimpleFeature.getDefaultInstance1(null));
		SimpleFeature f2 = (SimpleFeature) layer.create(SimpleFeature.getDefaultInstance2(null));
		Iterator<?> iterator = layer.getElements(null, 0, 0);

		List<SimpleFeature> actual = new ArrayList<SimpleFeature>();
		while (iterator.hasNext()) {
			actual.add((SimpleFeature) iterator.next());
		}

		Assert.assertEquals(f2.getId(), actual.get(0).getId());
		Assert.assertEquals(f1.getId(), actual.get(1).getId());
	}

	@Test
	public void testScrollableResultSet() throws Exception {
		// TODO this is arguably not a good unit test, there is no certainty a scrollable resultset is actually used
		SimpleFeature f1 = (SimpleFeature) scrollableResultSetLayer.create(SimpleFeature.getDefaultInstance1(null));
		SimpleFeature f2 = (SimpleFeature) scrollableResultSetLayer.create(SimpleFeature.getDefaultInstance2(null));
		Iterator<?> iterator = scrollableResultSetLayer.getElements(null, 0, 0);

		Assert.assertTrue(iterator.hasNext());
		Assert.assertEquals(f2, iterator.next());
		Assert.assertTrue(iterator.hasNext());
		Assert.assertEquals(f1, iterator.next());
		Assert.assertFalse(iterator.hasNext());
	}

	@Test
	public void testEmptyScrollableResultSet() throws Exception {
		// TODO this is arguably not a good unit test, there is no certainty a scrollable resultset is actually used
		Iterator<?> iterator = scrollableResultSetLayer.getElements(null, 0, 0);
		Assert.assertFalse(iterator.hasNext());
	}
}