/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.layer.hibernate;

import org.geomajas.layer.LayerException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerAssociationSupport;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.attribute.BooleanAttribute;
import org.geomajas.layer.feature.attribute.DateAttribute;
import org.geomajas.layer.feature.attribute.FloatAttribute;
import org.geomajas.layer.feature.attribute.ManyToOneAttribute;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.layer.hibernate.pojo.HibernateTestFeature;
import org.geomajas.layer.hibernate.pojo.HibernateTestManyToOne;
import org.geomajas.service.FilterService;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Unit test that tests all the functions of the HibernateLayer.
 * 
 * @author Pieter De Graef
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/testContextSimplifiedConfig.xml" })
@Transactional(rollbackFor = {org.geomajas.global.GeomajasException.class})
public class HibernateLayerSimplifiedConfigTest {

	@Resource(name="layer")
	private VectorLayer layer;

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
		attributes.put(AbstractHibernateLayerModelTest.PARAM_TEXT_ATTR, new StringAttribute("a name"));

		layer.getFeatureModel().setAttributes(detached, attributes);
		layer.saveOrUpdate(detached);

		feature = layer.read(f1.getId().toString());
		Assert.assertEquals("a name", layer.getFeatureModel().
				getAttribute(feature, AbstractHibernateLayerModelTest.PARAM_TEXT_ATTR).getValue());
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

}
