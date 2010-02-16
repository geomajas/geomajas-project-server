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

import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.feature.attribute.BooleanAttribute;
import org.geomajas.layer.feature.attribute.DateAttribute;
import org.geomajas.layer.feature.attribute.DoubleAttribute;
import org.geomajas.layer.feature.attribute.FloatAttribute;
import org.geomajas.layer.feature.attribute.IntegerAttribute;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.layer.hibernate.pojo.HibernateTestFeature;
import org.geomajas.layer.hibernate.pojo.HibernateTestManyToOne;
import org.geomajas.layer.hibernate.pojo.HibernateTestOneToMany;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
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
	
	@Before
    public void setUpTestDataWithinTransaction() throws LayerException {
		featureModel = layer.getFeatureModel();

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
		Assert.assertEquals("default-name-1", featureModel.getAttribute(feature1, PARAM_TEXT_ATTR).getValue());
	}

	@Test
	public void getAttributeInteger() throws Exception {
		Assert.assertEquals(10, featureModel.getAttribute(feature1, PARAM_INT_ATTR).getValue());
	}

	@Test
	public void getAttributeFloat() throws Exception {
		Assert.assertEquals(10.0f, featureModel.getAttribute(feature1, PARAM_FLOAT_ATTR).getValue());
	}

	@Test
	public void getAttributeDouble() throws Exception {
		Assert.assertEquals(10.0, featureModel.getAttribute(feature1, PARAM_DOUBLE_ATTR).getValue());
	}

	@Test
	public void getAttributeBoolean() throws Exception {
		Assert.assertEquals(true, featureModel.getAttribute(feature1, PARAM_BOOLEAN_ATTR).getValue());
	}

	@Test
	public void getAttributeDate() throws Exception {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		date = format.parse("01/01/2009");
		Assert.assertEquals(date, featureModel.getAttribute(feature1, PARAM_DATE_ATTR).getValue());
	}

	// @todo revive test, needs functional associations in feature model
	/*
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
				PARAM_MANY_TO_ONE + HibernateLayerUtil.SEPARATOR + PARAM_TEXT_ATTR).getValue());
		Assert.assertEquals(100, featureModel.getAttribute(feature1, PARAM_MANY_TO_ONE + HibernateLayerUtil.SEPARATOR
				+ PARAM_INT_ATTR).getValue());
		Assert.assertEquals(100.0f, featureModel.getAttribute(feature1, PARAM_MANY_TO_ONE + HibernateLayerUtil.SEPARATOR
				+ PARAM_FLOAT_ATTR).getValue());
		Assert.assertEquals(100.0, featureModel.getAttribute(feature1, PARAM_MANY_TO_ONE + HibernateLayerUtil.SEPARATOR
				+ PARAM_DOUBLE_ATTR).getValue());
		Assert.assertEquals(true, featureModel.getAttribute(feature1, PARAM_MANY_TO_ONE + HibernateLayerUtil.SEPARATOR
				+ PARAM_BOOLEAN_ATTR).getValue());
		Assert.assertEquals(date, featureModel.getAttribute(feature1, PARAM_MANY_TO_ONE + HibernateLayerUtil.SEPARATOR
				+ PARAM_DATE_ATTR).getValue());
	}
	*/

	// @todo revive test, needs functional associations in feature model
	/*
	@Test
	public void getAttributeOneToMany() throws Exception {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		date = format.parse("01/01/2009");
		Object[] result = (Object[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__TEXT).getValue();
		Assert.assertTrue(result[0].equals("oneToMany-1") || result[1].equals("oneToMany-1"));

		result = (Object[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__INT).getValue();
		Assert.assertTrue(result[0].equals(1000) || result[1].equals(1000));

		result = (Object[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__FLOAT).getValue();
		Assert.assertTrue(result[0].equals(1000.0f) || result[1].equals(1000.0f));

		result = (Object[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__DOUBLE).getValue();
		Assert.assertTrue(result[0].equals(1000.0) || result[1].equals(1000.0));

		result = (Object[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__BOOLEAN).getValue();
		Assert.assertTrue(result[0].equals(true) || result[1].equals(true));

		result = (Object[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__DATE).getValue();
		Assert.assertTrue(result[0].equals(date) || result[1].equals(date));
	}
	*/

	@Test
	public void getAttributes() throws Exception {
		Assert.assertEquals("default-name-1", featureModel.getAttributes(feature1).get(PARAM_TEXT_ATTR).getValue());
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
		Map<String, Attribute> attributes = new HashMap<String, Attribute>();
		attributes.put(PARAM_TEXT_ATTR, new StringAttribute("new name"));
		attributes.put(PARAM_INT_ATTR, new IntegerAttribute(5));
		attributes.put(PARAM_FLOAT_ATTR, new FloatAttribute(5.0f));
		attributes.put(PARAM_DOUBLE_ATTR, new DoubleAttribute(5.0));
		attributes.put(PARAM_BOOLEAN_ATTR, new BooleanAttribute(false));
		attributes.put(PARAM_DATE_ATTR, new DateAttribute(date));
		featureModel.setAttributes(feature1, attributes);
		Assert.assertEquals("new name", featureModel.getAttribute(feature1, PARAM_TEXT_ATTR).getValue());
		Assert.assertEquals(5, featureModel.getAttribute(feature1, PARAM_INT_ATTR).getValue());
		Assert.assertEquals(5.0f, featureModel.getAttribute(feature1, PARAM_FLOAT_ATTR).getValue());
		Assert.assertEquals(5.0, featureModel.getAttribute(feature1, PARAM_DOUBLE_ATTR).getValue());
		Assert.assertFalse((Boolean)featureModel.getAttribute(feature1, PARAM_BOOLEAN_ATTR).getValue());
		Assert.assertEquals(date, featureModel.getAttribute(feature1, PARAM_DATE_ATTR).getValue());
	}

	// @todo revive test, needs functional ManyToOneAttribute class
	/*
	@Test
	public void setAttributesManyToOne() throws Exception {
		Map<String, Attribute> attributes = new HashMap<String, Attribute>();
		attributes.put(PARAM_MANY_TO_ONE, HibernateTestManyToOne.getDefaultInstance2(new Long(2)));
		featureModel.setAttributes(feature1, attributes);
		Assert.assertEquals("manyToOne-2", featureModel.getAttribute(feature1, ATTR__MANY_TO_ONE__DOT__TEXT).getValue());
	}
	*/

	// @todo revive test, needs functional associations in feature model
	/*
	@Test
	public void setAttributesManyToOneDotAttribute() throws Exception {
		Date date = new Date();
		Map<String, Attribute> attributes = new HashMap<String, Attribute>();
		attributes.put(ATTR__MANY_TO_ONE__DOT__TEXT, new StringAttribute("new name"));
		attributes.put(ATTR__MANY_TO_ONE__DOT__INT, new IntegerAttribute(5));
		attributes.put(ATTR__MANY_TO_ONE__DOT__FLOAT, new FloatAttribute(5.0f));
		attributes.put(ATTR__MANY_TO_ONE__DOT__DOUBLE, new DoubleAttribute(5.0));
		attributes.put(ATTR__MANY_TO_ONE__DOT__BOOLEAN, new BooleanAttribute(false));
		attributes.put(ATTR__MANY_TO_ONE__DOT__DATE, new DateAttribute(date));
		featureModel.setAttributes(feature1, attributes);
		Assert.assertEquals("new name", featureModel.getAttribute(feature1, ATTR__MANY_TO_ONE__DOT__TEXT).getValue());
		Assert.assertEquals(5, featureModel.getAttribute(feature1, ATTR__MANY_TO_ONE__DOT__INT).getValue());
		Assert.assertEquals(5.0f, featureModel.getAttribute(feature1, ATTR__MANY_TO_ONE__DOT__FLOAT).getValue());
		Assert.assertEquals(5.0, featureModel.getAttribute(feature1, ATTR__MANY_TO_ONE__DOT__DOUBLE).getValue());
		Assert.assertFalse((Boolean)featureModel.getAttribute(feature1, ATTR__MANY_TO_ONE__DOT__BOOLEAN).getValue());
		Assert.assertEquals(date, featureModel.getAttribute(feature1, ATTR__MANY_TO_ONE__DOT__DATE).getValue());
	}
	*/

	// @TODO when setting new one-to-many values that are NEW, they cannot have an ID! We expect ID generation i guess.
	// @todo revive test, needs functional OnteToManyAttribute class
	/*
	@Test
	public void setAttributesOneToMany() throws Exception {
		Map<String, Attribute> attributes = new HashMap<String, Attribute>();
		HibernateTestOneToMany[] oneToMany = new HibernateTestOneToMany[2];
		oneToMany[0] = HibernateTestOneToMany.getDefaultInstance3(null);
		oneToMany[1] = HibernateTestOneToMany.getDefaultInstance4(null);
		attributes.put(PARAM_ONE_TO_MANY, oneToMany);
		featureModel.setAttributes(feature1, attributes);
		Object[] result = (Object[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__TEXT).getValue();
		Assert.assertTrue(result[0].equals("oneToMany-3") || result[0].equals("oneToMany-4"));
	}
	*/

	// @todo revive test, needs functional associations in feature model
	/*
	@Test
	public void setAttributesOneToManyDotAttr() throws Exception {
		Date date = new Date();
		Map<String, Attribute> attributes = new HashMap<String, Attribute>();
		attributes.put(ATTR__ONE_TO_MANY__DOT__TEXT, new StringAttribute("new name"));
		attributes.put(ATTR__ONE_TO_MANY__DOT__INT, new IntegerAttribute(5));
		attributes.put(ATTR__ONE_TO_MANY__DOT__FLOAT, new FloatAttribute(5.0f));
		attributes.put(ATTR__ONE_TO_MANY__DOT__DOUBLE, new DoubleAttribute(5.0));
		attributes.put(ATTR__ONE_TO_MANY__DOT__BOOLEAN, new BooleanAttribute(false));
		attributes.put(ATTR__ONE_TO_MANY__DOT__DATE, new DateAttribute(date));
		featureModel.setAttributes(feature1, attributes);

		Object[] result = (Object[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__TEXT).getValue();
		Assert.assertEquals(result[0], "new name");
		result = (Object[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__INT).getValue();
		Assert.assertEquals(result[0], 5);
		result = (Object[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__FLOAT).getValue();
		Assert.assertEquals(result[0], 5.0f);
		result = (Object[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__DOUBLE).getValue();
		Assert.assertEquals(result[0], 5.0);
		result = (Object[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__BOOLEAN).getValue();
		Assert.assertEquals(result[0], false);
		result = (Object[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__DATE).getValue();
		Assert.assertEquals(result[0], date);
	}
	*/

	// @Test
	// public void setGeometry() {
	// }
}