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
package org.geomajas.layer.hibernate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.feature.attribute.AssociationValue;
import org.geomajas.layer.feature.attribute.BooleanAttribute;
import org.geomajas.layer.feature.attribute.DateAttribute;
import org.geomajas.layer.feature.attribute.DoubleAttribute;
import org.geomajas.layer.feature.attribute.FloatAttribute;
import org.geomajas.layer.feature.attribute.IntegerAttribute;
import org.geomajas.layer.feature.attribute.LongAttribute;
import org.geomajas.layer.feature.attribute.ManyToOneAttribute;
import org.geomajas.layer.feature.attribute.OneToManyAttribute;
import org.geomajas.layer.feature.attribute.PrimitiveAttribute;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.layer.hibernate.pojo.HibernateTestFeature;
import org.geomajas.layer.hibernate.pojo.HibernateTestManyToOne;
import org.geomajas.layer.hibernate.pojo.HibernateTestOneToMany;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
	public void getIdAttributeLong() throws Exception {
		Assert.assertEquals(1L, featureModel.getAttribute(feature1, PARAM_ID_ATTR).getValue());
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

	@Test
	public void getAttributeManyToOne() throws Exception {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		try {
			date = format.parse("01/01/2009");
		} catch (ParseException e) {
			date = new Date();
		}
		Object value = featureModel.getAttribute(feature1, PARAM_MANY_TO_ONE);
		Assert.assertTrue(value instanceof ManyToOneAttribute);
		Assert.assertTrue(((ManyToOneAttribute) value).isEmpty() == false);
		Assert.assertEquals("manyToOne-1", featureModel.getAttribute(feature1,
				PARAM_MANY_TO_ONE + HibernateLayerUtil.SEPARATOR + PARAM_TEXT_ATTR).getValue());
		Assert.assertEquals(100, featureModel.getAttribute(feature1,
				PARAM_MANY_TO_ONE + HibernateLayerUtil.SEPARATOR + PARAM_INT_ATTR).getValue());
		Assert.assertEquals(100.0f, featureModel.getAttribute(feature1,
				PARAM_MANY_TO_ONE + HibernateLayerUtil.SEPARATOR + PARAM_FLOAT_ATTR).getValue());
		Assert.assertEquals(100.0, featureModel.getAttribute(feature1,
				PARAM_MANY_TO_ONE + HibernateLayerUtil.SEPARATOR + PARAM_DOUBLE_ATTR).getValue());
		Assert.assertEquals(true, featureModel.getAttribute(feature1,
				PARAM_MANY_TO_ONE + HibernateLayerUtil.SEPARATOR + PARAM_BOOLEAN_ATTR).getValue());
		Assert.assertEquals(date, featureModel.getAttribute(feature1,
				PARAM_MANY_TO_ONE + HibernateLayerUtil.SEPARATOR + PARAM_DATE_ATTR).getValue());
	}

//	@Test
//	public void getAttributeOneToMany() throws Exception {
//		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//		Date date;
//		date = format.parse("01/01/2009");
//		Object oneToMany = featureModel.getAttribute(feature1, PARAM_ONE_TO_MANY);
//		Assert.assertTrue(oneToMany instanceof OneToManyAttribute);
//		Assert.assertTrue(((OneToManyAttribute) oneToMany).isEmpty() == false);
//		Assert.assertTrue(((OneToManyAttribute) oneToMany).getValue().size() == 2);
//
//		// Cast the attribute values to the correct types!
//		String[] strings = (String[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__TEXT).getValue();
//		Assert.assertTrue(strings[0].equals("oneToMany-1") || strings[1].equals("oneToMany-1"));
//
//		Integer[] integers = (Integer[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__INT).getValue();
//		Assert.assertTrue(integers[0].equals(1000) || integers[1].equals(1000));
//
//		Float[] floats = (Float[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__FLOAT).getValue();
//		Assert.assertTrue(floats[0].equals(1000.0f) || floats[1].equals(1000.0f));
//
//		Double[] doubles = (Double[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__DOUBLE).getValue();
//		Assert.assertTrue(doubles[0].equals(1000.0) || doubles[1].equals(1000.0));
//
//		Boolean[] bools = (Boolean[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__BOOLEAN).getValue();
//		Assert.assertTrue(bools[0].equals(true) || bools[1].equals(true));
//
//		Date[] dates = (Date[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__DATE).getValue();
//		Assert.assertTrue(dates[0].equals(date) || dates[1].equals(date));
//	}

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

	@SuppressWarnings("unchecked")
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
		Assert.assertFalse((Boolean) featureModel.getAttribute(feature1, PARAM_BOOLEAN_ATTR).getValue());
		Assert.assertEquals(date, featureModel.getAttribute(feature1, PARAM_DATE_ATTR).getValue());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void setAttributesManyToOne() throws Exception {
		String newValue = "A new value";
		Map<String, Attribute> attributes = new HashMap<String, Attribute>();
		ManyToOneAttribute attr = HibernateTestManyToOne.getDefaultAttributeInstance1(new Long(1));
		attr.getValue().getAttributes().remove(PARAM_TEXT_ATTR);
		attr.getValue().getAttributes().put(PARAM_TEXT_ATTR, new StringAttribute(newValue));
		attributes.put(PARAM_MANY_TO_ONE, attr);
		featureModel.setAttributes(feature1, attributes);
		Assert.assertEquals("A new value", featureModel.getAttribute(feature1, ATTR__MANY_TO_ONE__DOT__TEXT).getValue());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void setAttributesManyToOneDotAttribute() throws Exception {
//		Date date = new Date();
//		Map<String, Attribute> attributes = new HashMap<String, Attribute>();
//		attributes.put(ATTR__MANY_TO_ONE__DOT__TEXT, new StringAttribute("new name"));
//		attributes.put(ATTR__MANY_TO_ONE__DOT__INT, new IntegerAttribute(5));
//		attributes.put(ATTR__MANY_TO_ONE__DOT__FLOAT, new FloatAttribute(5.0f));
//		attributes.put(ATTR__MANY_TO_ONE__DOT__DOUBLE, new DoubleAttribute(5.0));
//		attributes.put(ATTR__MANY_TO_ONE__DOT__BOOLEAN, new BooleanAttribute(false));
//		attributes.put(ATTR__MANY_TO_ONE__DOT__DATE, new DateAttribute(date));
//		featureModel.setAttributes(feature1, attributes);
//		Assert.assertEquals("new name", featureModel.getAttribute(feature1, ATTR__MANY_TO_ONE__DOT__TEXT).getValue());
//		Assert.assertEquals(5, featureModel.getAttribute(feature1, ATTR__MANY_TO_ONE__DOT__INT).getValue());
//		Assert.assertEquals(5.0f, featureModel.getAttribute(feature1, ATTR__MANY_TO_ONE__DOT__FLOAT).getValue());
//		Assert.assertEquals(5.0, featureModel.getAttribute(feature1, ATTR__MANY_TO_ONE__DOT__DOUBLE).getValue());
//		Assert.assertFalse((Boolean) featureModel.getAttribute(feature1, ATTR__MANY_TO_ONE__DOT__BOOLEAN).getValue());
//		Assert.assertEquals(date, featureModel.getAttribute(feature1, ATTR__MANY_TO_ONE__DOT__DATE).getValue());
	}

	// TODO when setting new one-to-many values that are NEW, they cannot have an ID! We expect ID generation I guess.
	@SuppressWarnings("unchecked")
	@Test
	public void setAttributesOneToMany() throws Exception {
//		Map<String, PrimitiveAttribute<?>> oneToManyAttr = new HashMap<String, PrimitiveAttribute<?>>();
//		oneToManyAttr.put(PARAM_TEXT_ATTR, new StringAttribute("new name"));
//		oneToManyAttr.put(PARAM_INT_ATTR, new IntegerAttribute(5));
//		oneToManyAttr.put(PARAM_FLOAT_ATTR, new FloatAttribute(5.0f));
//		oneToManyAttr.put(PARAM_DOUBLE_ATTR, new DoubleAttribute(5.0));
//		oneToManyAttr.put(PARAM_BOOLEAN_ATTR, new BooleanAttribute(false));
//		oneToManyAttr.put(PARAM_DATE_ATTR, new DateAttribute(new Date()));
//		List<AssociationValue> associationValues = new ArrayList<AssociationValue>();
//
//		// Feature1 currently contains 2 oneToMany values; with id=1 and id=2.
//		// By sending a list with id=2 and id=null, id=1 will be deleted, id=2 will be updated, id=null will be added.
//		associationValues.add(new AssociationValue(new LongAttribute(new Long(2)), oneToManyAttr)); // Update - id=2
//		associationValues.add(new AssociationValue(null, oneToManyAttr)); // New item - id=null
//		OneToManyAttribute oneToMany = new OneToManyAttribute(associationValues);
//
//		Map<String, Attribute> attributes = new HashMap<String, Attribute>();
//		attributes.put(PARAM_ONE_TO_MANY, oneToMany);
//
//		featureModel.setAttributes(feature1, attributes);
//		String[] result = (String[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__TEXT).getValue();
//		Assert.assertTrue(result[0].equals("new name"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void setAttributesOneToManyDotAttr() throws Exception {
//		Date date = new Date();
//		Map<String, Attribute> attributes = new HashMap<String, Attribute>();
//		attributes.put(ATTR__ONE_TO_MANY__DOT__TEXT, new StringAttribute("new name"));
//		attributes.put(ATTR__ONE_TO_MANY__DOT__INT, new IntegerAttribute(5));
//		attributes.put(ATTR__ONE_TO_MANY__DOT__FLOAT, new FloatAttribute(5.0f));
//		attributes.put(ATTR__ONE_TO_MANY__DOT__DOUBLE, new DoubleAttribute(5.0));
//		attributes.put(ATTR__ONE_TO_MANY__DOT__BOOLEAN, new BooleanAttribute(false));
//		attributes.put(ATTR__ONE_TO_MANY__DOT__DATE, new DateAttribute(date));
//		featureModel.setAttributes(feature1, attributes);
//
//		String[] strings = (String[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__TEXT).getValue();
//		Assert.assertEquals(strings[0], "new name");
//		Integer[] ints = (Integer[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__INT).getValue();
//		Assert.assertEquals((Object) ints[0], 5);
//		Float[] floats = (Float[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__FLOAT).getValue();
//		Assert.assertEquals((Object) floats[0], 5.0f);
//		Double[] doubles = (Double[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__DOUBLE).getValue();
//		Assert.assertEquals((Object) doubles[0], 5.0);
//		Boolean[] bools = (Boolean[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__BOOLEAN).getValue();
//		Assert.assertEquals(bools[0], false);
//		Date[] dates = (Date[]) featureModel.getAttribute(feature1, ATTR__ONE_TO_MANY__DOT__DATE).getValue();
//		Assert.assertEquals(dates[0], date);
	}
}