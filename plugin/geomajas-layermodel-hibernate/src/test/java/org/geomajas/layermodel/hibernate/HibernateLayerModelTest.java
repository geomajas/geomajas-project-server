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
package org.geomajas.layermodel.hibernate;

import org.geomajas.layermodel.hibernate.pojo.HibernateTestFeature;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Unit test that tests all the functions of the HibernateLayerModel.
 *
 * @author Pieter De Graef
 */
public class HibernateLayerModelTest extends AbstractHibernateLayerModelTest {

	private static String createdId;
	private static boolean initialised;

	public HibernateLayerModelTest() throws Exception {
		super();
		initialised = true;
	}

	public boolean isInitialised() {
		return initialised;
	}

	@Test
	public void testCreate() throws Exception {
		Object created = null;
		HibernateTestFeature feature = HibernateTestFeature.getDefaultInstance1(null);
		try {
			created = layerModel.create(feature);
			transactionInterceptor.commitTransaction();
		} catch (Exception e) {
			transactionInterceptor.rollbackTransaction();
		}
		Assert.assertNotNull(created);
		Assert.assertTrue(created instanceof HibernateTestFeature);
		HibernateTestFeature createdFeature = (HibernateTestFeature) created;
		Assert.assertNotNull(createdFeature.getId());
		createdId = Long.toString(createdFeature.getId().longValue());
	}

	@Test
	public void testRead() throws Exception {
		Assert.assertNotNull(createdId);
		Object feature = layerModel.read(createdId);
		Assert.assertNotNull(feature);
	}

	@Test
	public void testUpdate() throws Exception {
		Assert.assertNotNull(createdId);
		Object feature = layerModel.read(createdId);
		Assert.assertNotNull("The requested feature could not be found!", feature);
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("name", "new name");
		attributes.put("number", 5f);
		attributes.put("data", new Date());
		attributes.put("available", false);
		layerModel.getFeatureModel().setAttributes(feature, attributes);
		layerModel.saveOrUpdate(feature);
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
		layerModel.create(HibernateTestFeature.getDefaultInstance2(null));
		Object feature = null;
		Iterator<?> iterator = layerModel.getElements(null);
		if (iterator.hasNext()) {
			feature = iterator.next();
		}
		Assert.assertNotNull(feature);
	}

	@Test
	public void testDelete() throws Exception {
		Assert.assertNotNull(createdId);
		Assert.assertNotNull(layerModel.read(createdId));
		layerModel.delete(createdId);
		Assert.assertNull(layerModel.read(createdId));
	}
}
