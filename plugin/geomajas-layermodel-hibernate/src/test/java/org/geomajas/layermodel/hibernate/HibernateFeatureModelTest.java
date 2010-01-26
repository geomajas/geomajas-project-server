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

import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layermodel.hibernate.pojo.HibernateTestFeature;
import org.geomajas.layermodel.hibernate.pojo.HibernateTestManyToOne;
import org.geomajas.layermodel.hibernate.pojo.HibernateTestOneToMany;
import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Testcase testing all methods of the Hibernate FeatureModel.
 * </p>
 *
 * @author Pieter De Graef
 */
public class HibernateFeatureModelTest extends AbstractHibernateLayerModelTest {

	private FeatureModel featureModel;
	private HibernateTestFeature feature1;

	public HibernateFeatureModelTest() throws Exception {
		super();
		featureModel = layerModel.getFeatureModel();

		feature1 = HibernateTestFeature.getDefaultInstance1(new Long(1));
		feature1.setManyToOne(HibernateTestManyToOne.getDefaultInstance1(new Long(1)));

		Set<HibernateTestOneToMany> set1 = new HashSet<HibernateTestOneToMany>();
		set1.add(HibernateTestOneToMany.getDefaultInstance1(new Long(1)));
		set1.add(HibernateTestOneToMany.getDefaultInstance2(new Long(2)));
		feature1.setOneToMany(set1);
	}

	public boolean isInitialised() {
		return false; // need a new database for each run
	}

	@Test
	public void getId() throws Exception {
		Assert.assertEquals("1", featureModel.getId(feature1));
	}

	@Test
	public void getAttributeText() throws Exception {
		Assert.assertEquals("default-name-1", featureModel.getAttribute(feature1, PARAM_TEXT_ATTR));
	}

	@Test
	public void getAttributeInteger() throws Exception {
		Assert.assertEquals(10, featureModel.getAttribute(feature1, PARAM_INT_ATTR));
	}

	@Test
	public void getAttributeFloat() throws Exception {
		Assert.assertEquals(10.0f, featureModel.getAttribute(feature1, PARAM_FLOAT_ATTR));
	}

	@Test
	public void getAttributeDouble() throws Exception {
		Assert.assertEquals(10.0, featureModel.getAttribute(feature1, PARAM_DOUBLE_ATTR));
	}

	@Test
	public void getAttributeBoolean() throws Exception {
		Assert.assertEquals(true, featureModel.getAttribute(feature1, PARAM_BOOLEAN_ATTR));
	}

	@Test
	public void getAttributeDate() throws Exception {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		date = format.parse("01/01/2009");
		Assert.assertEquals(date, featureModel.getAttribute(feature1, PARAM_DATE_ATTR));
	}

	@Test
	public void getAttributeManyToOne() throws Exception {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		try {
			date = format.parse("01/01/2009");
		} catch (ParseException e) {
			date = new Date();
		}
		Assert.assertEquals("manyToOne-1", featureModel.getAttribute(feature1,
				PARAM_MANY_TO_ONE + HibernateLayerUtil.SEPARATOR + PARAM_TEXT_ATTR));
		Assert.assertEquals(100, featureModel.getAttribute(feature1, PARAM_MANY_TO_ONE + HibernateLayerUtil.SEPARATOR
				+ PARAM_INT_ATTR));
		Assert.assertEquals(100.0f, featureModel.getAttribute(feature1, PARAM_MANY_TO_ONE + HibernateLayerUtil.SEPARATOR
				+ PARAM_FLOAT_ATTR));
		Assert.assertEquals(100.0, featureModel.getAttribute(feature1, PARAM_MANY_TO_ONE + HibernateLayerUtil.SEPARATOR
				+ PARAM_DOUBLE_ATTR));
		Assert.assertEquals(true, featureModel.getAttribute(feature1, PARAM_MANY_TO_ONE + HibernateLayerUtil.SEPARATOR
				+ PARAM_BOOLEAN_ATTR));
		Assert.assertEquals(date, featureModel.getAttribute(feature1, PARAM_MANY_TO_ONE + HibernateLayerUtil.SEPARATOR
				+ PARAM_DATE_ATTR));
	}

	@Test
	public void getAttributeOneToMany() throws Exception {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		date = format.parse("01/01/2009");
		Object[] result = (Object[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__TEXT);
		Assert.assertTrue(result[0].equals("oneToMany-1") || result[1].equals("oneToMany-1"));

		result = (Object[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__INT);
		Assert.assertTrue(result[0].equals(1000) || result[1].equals(1000));

		result = (Object[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__FLOAT);
		Assert.assertTrue(result[0].equals(1000.0f) || result[1].equals(1000.0f));

		result = (Object[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__DOUBLE);
		Assert.assertTrue(result[0].equals(1000.0) || result[1].equals(1000.0));

		result = (Object[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__BOOLEAN);
		Assert.assertTrue(result[0].equals(true) || result[1].equals(true));

		result = (Object[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__DATE);
		Assert.assertTrue(result[0].equals(date) || result[1].equals(date));
	}

	@Test
	public void getAttributes() throws Exception {
		Assert.assertEquals(featureModel.getAttributes(feature1).get(PARAM_TEXT_ATTR), "default-name-1");
	}

	@Test
	public void getGeometry() {
	}

	@Test
	public void getGeometryAttributeName() {
	}

	@Test
	public void getSrid() throws Exception {
		Assert.assertEquals(4326, featureModel.getSrid());
	}

	@Test
	public void setAttributes() throws Exception {
		Date date = new Date();
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put(PARAM_TEXT_ATTR, "new name");
		attributes.put(PARAM_INT_ATTR, 5);
		attributes.put(PARAM_FLOAT_ATTR, 5.0f);
		attributes.put(PARAM_DOUBLE_ATTR, 5.0);
		attributes.put(PARAM_BOOLEAN_ATTR, false);
		attributes.put(PARAM_DATE_ATTR, date);
		featureModel.setAttributes(feature1, attributes);
		Assert.assertEquals(featureModel.getAttribute(feature1, PARAM_TEXT_ATTR), "new name");
		Assert.assertEquals(featureModel.getAttribute(feature1, PARAM_INT_ATTR), 5);
		Assert.assertEquals(featureModel.getAttribute(feature1, PARAM_FLOAT_ATTR), 5.0f);
		Assert.assertEquals(featureModel.getAttribute(feature1, PARAM_DOUBLE_ATTR), 5.0);
		Assert.assertEquals(featureModel.getAttribute(feature1, PARAM_BOOLEAN_ATTR), false);
		Assert.assertEquals(featureModel.getAttribute(feature1, PARAM_DATE_ATTR), date);
	}

	@Test
	public void setAttributesManyToOne() throws Exception {
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put(PARAM_MANY_TO_ONE, HibernateTestManyToOne.getDefaultInstance2(new Long(2)));
		featureModel.setAttributes(feature1, attributes);
		Assert.assertEquals(featureModel.getAttribute(feature1, ATTR__MANY_TO_ONE__DOT__TEXT), "manyToOne-2");
	}

	@Test
	public void setAttributesManyToOneDotAttribute() throws Exception {
		Date date = new Date();
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put(ATTR__MANY_TO_ONE__DOT__TEXT, "new name");
		attributes.put(ATTR__MANY_TO_ONE__DOT__INT, 5);
		attributes.put(ATTR__MANY_TO_ONE__DOT__FLOAT, 5.0f);
		attributes.put(ATTR__MANY_TO_ONE__DOT__DOUBLE, 5.0);
		attributes.put(ATTR__MANY_TO_ONE__DOT__BOOLEAN, false);
		attributes.put(ATTR__MANY_TO_ONE__DOT__DATE, date);
		featureModel.setAttributes(feature1, attributes);
		Assert.assertEquals(featureModel.getAttribute(feature1, ATTR__MANY_TO_ONE__DOT__TEXT), "new name");
		Assert.assertEquals(featureModel.getAttribute(feature1, ATTR__MANY_TO_ONE__DOT__INT), 5);
		Assert.assertEquals(featureModel.getAttribute(feature1, ATTR__MANY_TO_ONE__DOT__FLOAT), 5.0f);
		Assert.assertEquals(featureModel.getAttribute(feature1, ATTR__MANY_TO_ONE__DOT__DOUBLE), 5.0);
		Assert.assertEquals(featureModel.getAttribute(feature1, ATTR__MANY_TO_ONE__DOT__BOOLEAN), false);
		Assert.assertEquals(featureModel.getAttribute(feature1, ATTR__MANY_TO_ONE__DOT__DATE), date);
	}

	/**
	 * TODO: when setting new one-to-many values that are NEW, they cannot have an ID! We expect ID generation i guess.
	 */
	@Test
	public void setAttributesOneToMany() throws Exception {
		Map<String, Object> attributes = new HashMap<String, Object>();
		HibernateTestOneToMany[] oneToMnyArray = new HibernateTestOneToMany[2];
		oneToMnyArray[0] = HibernateTestOneToMany.getDefaultInstance3(null);
		oneToMnyArray[1] = HibernateTestOneToMany.getDefaultInstance4(null);
		attributes.put(PARAM_ONE_TO_MANY, oneToMnyArray);
		featureModel.setAttributes(feature1, attributes);
		Object[] result = (Object[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__TEXT);
		Assert.assertTrue(result[0].equals("oneToMany-3") || result[0].equals("oneToMany-4"));
	}

	@Test
	public void setAttributesOneToManyDotAttr() throws Exception {
		Date date = new Date();
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put(ATTR__ONE_TO_MANY__DOT__TEXT, "new name");
		attributes.put(ATTR__ONE_TO_MANY__DOT__INT, 5);
		attributes.put(ATTR__ONE_TO_MANY__DOT__FLOAT, 5.0f);
		attributes.put(ATTR__ONE_TO_MANY__DOT__DOUBLE, 5.0);
		attributes.put(ATTR__ONE_TO_MANY__DOT__BOOLEAN, false);
		attributes.put(ATTR__ONE_TO_MANY__DOT__DATE, date);
		featureModel.setAttributes(feature1, attributes);

		Object[] result = (Object[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__TEXT);
		Assert.assertEquals(result[0], "new name");
		result = (Object[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__INT);
		Assert.assertEquals(result[0], 5);
		result = (Object[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__FLOAT);
		Assert.assertEquals(result[0], 5.0f);
		result = (Object[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__DOUBLE);
		Assert.assertEquals(result[0], 5.0);
		result = (Object[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__BOOLEAN);
		Assert.assertEquals(result[0], false);
		result = (Object[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__DATE);
		Assert.assertEquals(result[0], date);
	}

	// @Test
	// public void setGeometry() {
	// }
}