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
package org.geomajas.layer.hibernate.association;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.geomajas.layer.LayerException;
import org.geomajas.layer.VectorLayerAssociationSupport;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.attribute.ManyToOneAttribute;
import org.geomajas.layer.hibernate.AbstractHibernateAssociationTest;
import org.geomajas.layer.hibernate.HibernateLayerException;
import org.geomajas.layer.hibernate.association.pojo.AssociationFeature;
import org.geomajas.layer.hibernate.association.pojo.ManyToOneProperty;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.filter.Filter;

/**
 * Unit test that tests all the functions of the HibernateLayer, using a model with a many-to-one and a one-to-many
 * relation.
 * 
 * @author Pieter De Graef
 */
public class AssociationLayerTest extends AbstractHibernateAssociationTest {

	@Test
	public void testCreate() throws Exception {
		AssociationFeature feature = AssociationFeature.getDefaultInstance1(null);
		Object created = layer.create(feature);
		Assert.assertNotNull(created);
		Assert.assertTrue(created instanceof AssociationFeature);
		AssociationFeature createdFeature = (AssociationFeature) created;
		Assert.assertNotNull(createdFeature.getId());
	}

	@Test
	public void testRead() throws Exception {
		AssociationFeature f1 = (AssociationFeature) layer.create(AssociationFeature.getDefaultInstance1(null));
		Assert.assertNotNull(f1.getId());
		Object feature = layer.read(f1.getId().toString());
		Assert.assertNotNull(feature);
	}

	@Test
	@SuppressWarnings("rawtypes")
	public void testUpdate() throws Exception {
		AssociationFeature f1 = (AssociationFeature) layer.create(AssociationFeature.getDefaultInstance1(null));
		Assert.assertNotNull(f1.getId());
		Object feature = layer.read(f1.getId().toString());
		Assert.assertNotNull("The requested feature could not be found!", feature);

		// Create a detached copy
		AssociationFeature detached = AssociationFeature.getDefaultInstance1(null);
		detached.setId(((AssociationFeature) feature).getId());
		Map<String, Attribute> attributes = new HashMap<String, Attribute>();

		// Set a ManyToOne attribute without an ID (a new one)
		attributes.put(MTO, ManyToOneProperty.getDefaultAttributeInstance(null));

		layer.getFeatureModel().setAttributes(detached, attributes);
		layer.saveOrUpdate(detached);

		feature = layer.read(f1.getId().toString());
		ManyToOneAttribute manytoOne = (ManyToOneAttribute) layer.getFeatureModel().getAttribute(feature, MTO);
		Assert.assertNotNull(manytoOne.getValue());
		Assert.assertNotNull(manytoOne.getValue().getId()); // Test for ID
	}

	@Test
	public void testSave() throws Exception {
		AssociationFeature feature = AssociationFeature.getDefaultInstance1(null);
		Object created = layer.saveOrUpdate(feature);
		Assert.assertNotNull(created);
		Assert.assertTrue(created instanceof AssociationFeature);
		AssociationFeature createdFeature = (AssociationFeature) created;
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
		layer.create(AssociationFeature.getDefaultInstance2(null));
		Object feature = null;
		Iterator<?> iterator = layer.getElements(null, 0, 0);
		if (iterator.hasNext()) {
			feature = iterator.next();
		}
		Assert.assertNotNull(feature);
	}

	@Test
	public void testDelete() throws Exception {
		AssociationFeature f1 = (AssociationFeature) layer.create(AssociationFeature.getDefaultInstance1(null));
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
		// TODO sorting on many-to-one or one-to-many not supported.
	}

	@Test
	public void testGetAttributes() throws Exception {
		VectorLayerAssociationSupport support = (VectorLayerAssociationSupport) layer;

		List<Attribute<?>> attributes = support.getAttributes(MTO, Filter.INCLUDE);
		Assert.assertNotNull(attributes);

		attributes = support.getAttributes(MTO, null);
		Assert.assertNotNull(attributes);

		try {
			attributes = support.getAttributes(null, Filter.INCLUDE);
			Assert.fail();
		} catch (HibernateLayerException e) {
			// We expect this exception to occur.
		}
	}
}