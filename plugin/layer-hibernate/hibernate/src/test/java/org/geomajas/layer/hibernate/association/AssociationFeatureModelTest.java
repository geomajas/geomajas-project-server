/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.layer.hibernate.association;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.geomajas.layer.hibernate.AbstractHibernateAssociationTest;
import org.geomajas.layer.hibernate.association.pojo.AssociationFeature;
import org.geomajas.layer.hibernate.association.pojo.ManyToOneProperty;
import org.geomajas.layer.hibernate.association.pojo.OneToManyProperty;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * <p>
 * Testcase testing all methods of the Hibernate FeatureModel using the association model.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class AssociationFeatureModelTest extends AbstractHibernateAssociationTest {

	private FeatureModel featureModel;

	private AssociationFeature feature1;

	private GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

	@Before
	public void setUpTestDataWithinTransaction() throws LayerException {
		featureModel = layer.getFeatureModel();

		feature1 = AssociationFeature.getDefaultInstance1(new Long(1));
		feature1.setGeometry(geometryFactory.createPoint(new Coordinate(3, 42)));
	}

	public boolean isInitialised() {
		return false; // need a new database for each run
	}

	@Test
	public void getNonExistingAttribute() {
		try {
			featureModel.getAttribute(feature1, "manyToOne.non-existing-attribute");
		} catch (LayerException e) {
			Assert.assertTrue(e instanceof LayerException);
		}
	}

	@Test
	public void getAttributeManyToOne() throws Exception {
		Object value = featureModel.getAttribute(feature1, MTO);
		Assert.assertTrue(value instanceof ManyToOneAttribute);
		Assert.assertTrue(((ManyToOneAttribute) value).isEmpty() == false);

		Attribute<?> attribute = featureModel.getAttribute(feature1, MTO_TEXT);
		Assert.assertEquals(feature1.getManyToOne().getTextAttr(), attribute.getValue());

		attribute = featureModel.getAttribute(feature1, MTO_INT);
		Assert.assertEquals(feature1.getManyToOne().getIntAttr(), attribute.getValue());

		attribute = featureModel.getAttribute(feature1, MTO_FLOAT);
		Assert.assertEquals(feature1.getManyToOne().getFloatAttr(), attribute.getValue());

		attribute = featureModel.getAttribute(feature1, MTO_DOUBLE);
		Assert.assertEquals(feature1.getManyToOne().getDoubleAttr(), attribute.getValue());

		attribute = featureModel.getAttribute(feature1, MTO_BOOLEAN);
		Assert.assertEquals(feature1.getManyToOne().getBooleanAttr(), attribute.getValue());

		attribute = featureModel.getAttribute(feature1, MTO_DATE);
		Assert.assertEquals(feature1.getManyToOne().getDateAttr(), attribute.getValue());
	}

	@Test
	public void getAttributeOneToMany() throws Exception {
		Object oneToMany = featureModel.getAttribute(feature1, OTM);
		Assert.assertTrue(oneToMany instanceof OneToManyAttribute);
		Assert.assertTrue(((OneToManyAttribute) oneToMany).isEmpty() == false);
		Assert.assertTrue(((OneToManyAttribute) oneToMany).getValue().size() == 2);

		// Cast the attribute values to the correct types!
//		String[] strings = (String[]) featureModel.getAttribute(feature1, OTM_TEXT).getValue();
//		String expected = feature1.getOneToMany().iterator().next().getTextAttr();
//		Assert.assertTrue(strings[0].equals(expected) || strings[1].equals(expected));
//
//		Integer[] integers = (Integer[]) featureModel.getAttribute(feature1, OTM_INT).getValue();
//		Integer expectedInt = feature1.getOneToMany().iterator().next().getIntAttr();
//		Assert.assertTrue(integers[0].equals(expectedInt) || integers[1].equals(expectedInt));
//
//		Float[] floats = (Float[]) featureModel.getAttribute(feature1, OTM_FLOAT).getValue();
//		Float expectedFloat = feature1.getOneToMany().iterator().next().getFloatAttr();
//		Assert.assertTrue(floats[0].equals(expectedFloat) || floats[1].equals(expectedFloat));
//
//		Double[] doubles = (Double[]) featureModel.getAttribute(feature1, OTM_DOUBLE).getValue();
//		Double expectedDouble = feature1.getOneToMany().iterator().next().getDoubleAttr();
//		Assert.assertTrue(doubles[0].equals(expectedDouble) || doubles[1].equals(expectedDouble));
//
//		Boolean[] bools = (Boolean[]) featureModel.getAttribute(feature1, OTM_BOOLEAN).getValue();
//		Boolean expectedBoolean = feature1.getOneToMany().iterator().next().getBooleanAttr();
//		Assert.assertTrue(bools[0].equals(expectedBoolean) || bools[1].equals(expectedBoolean));
//
//		Date[] dates = (Date[]) featureModel.getAttribute(feature1, OTM_DATE).getValue();
//		Date expectedDate = feature1.getOneToMany().iterator().next().getDateAttr();
//		Assert.assertTrue(dates[0].equals(expectedDate) || dates[1].equals(expectedDate));
	}

	@Test
	@SuppressWarnings("rawtypes")
	public void setAttributesManyToOne() throws Exception {
		// TODO: check manyToOne value with an ID (so the corresponding object must be fetched from DB).

		final String newValue = "hello world";
		Map<String, PrimitiveAttribute<?>> mtoAttributes = new HashMap<String, PrimitiveAttribute<?>>();
		mtoAttributes.put(ManyToOneProperty.PARAM_TEXT_ATTR, new StringAttribute(newValue));
		AssociationValue assoValue = new AssociationValue(null, mtoAttributes);
		ManyToOneAttribute mtoAttribute = new ManyToOneAttribute(assoValue);

		Map<String, Attribute> attributes = new HashMap<String, Attribute>();
		attributes.put(MTO, mtoAttribute);
		featureModel.setAttributes(feature1, attributes);

		Assert.assertEquals(newValue, featureModel.getAttribute(feature1, MTO_TEXT).getValue());
	}

	@Test
	@SuppressWarnings("rawtypes")
	public void setAttributesManyToOneDotAttribute() throws Exception {
//		Date date = new Date();
//		Map<String, Attribute> attributes = new HashMap<String, Attribute>();
//		attributes.put(MTO_TEXT, new StringAttribute("new name"));
//		attributes.put(MTO_INT, new IntegerAttribute(5));
//		attributes.put(MTO_FLOAT, new FloatAttribute(5.0f));
//		attributes.put(MTO_DOUBLE, new DoubleAttribute(5.0));
//		attributes.put(MTO_BOOLEAN, new BooleanAttribute(false));
//		attributes.put(MTO_DATE, new DateAttribute(date));
//		featureModel.setAttributes(feature1, attributes);
//		Assert.assertEquals("new name", featureModel.getAttribute(feature1, MTO_TEXT).getValue());
//		Assert.assertEquals(5, featureModel.getAttribute(feature1, MTO_INT).getValue());
//		Assert.assertEquals(5.0f, featureModel.getAttribute(feature1, MTO_FLOAT).getValue());
//		Assert.assertEquals(5.0, featureModel.getAttribute(feature1, MTO_DOUBLE).getValue());
//		Assert.assertFalse((Boolean) featureModel.getAttribute(feature1, MTO_BOOLEAN).getValue());
//		Assert.assertEquals(date, featureModel.getAttribute(feature1, MTO_DATE).getValue());
	}

	// TODO when setting new one-to-many values that are NEW, they cannot have an ID! We expect ID generation I guess.
	@Test
	@SuppressWarnings("rawtypes")
	public void setAttributesOneToMany() throws Exception {
//		Map<String, PrimitiveAttribute<?>> oneToManyAttr = new HashMap<String, PrimitiveAttribute<?>>();
//		oneToManyAttr.put(OneToManyProperty.PARAM_TEXT_ATTR, new StringAttribute("new name"));
//		oneToManyAttr.put(OneToManyProperty.PARAM_INT_ATTR, new IntegerAttribute(5));
//		oneToManyAttr.put(OneToManyProperty.PARAM_FLOAT_ATTR, new FloatAttribute(5.0f));
//		oneToManyAttr.put(OneToManyProperty.PARAM_DOUBLE_ATTR, new DoubleAttribute(5.0));
//		oneToManyAttr.put(OneToManyProperty.PARAM_BOOLEAN_ATTR, new BooleanAttribute(false));
//		oneToManyAttr.put(OneToManyProperty.PARAM_DATE_ATTR, new DateAttribute(new Date()));
//		List<AssociationValue> associationValues = new ArrayList<AssociationValue>();
//
//		// Feature1 currently contains 2 oneToMany values; with id=1 and id=2.
//		// By sending a list with id=2 and id=null, id=1 will be deleted, id=2 will be updated, id=null will be added.
//		associationValues.add(new AssociationValue(new LongAttribute(new Long(2)), oneToManyAttr)); // Update - id=2
//		associationValues.add(new AssociationValue(null, oneToManyAttr)); // New item - id=null
//		OneToManyAttribute oneToMany = new OneToManyAttribute(associationValues);
//
//		Map<String, Attribute> attributes = new HashMap<String, Attribute>();
//		attributes.put(OTM, oneToMany);
//
//		featureModel.setAttributes(feature1, attributes);
//		String[] result = (String[]) featureModel.getAttribute(feature1, OTM_TEXT).getValue();
//		Assert.assertTrue(result[0].equals("new name"));
	}

	@Test
	@SuppressWarnings("rawtypes")
	public void setAttributesOneToManyDotAttr() throws Exception {
//		Date date = new Date();
//		Map<String, Attribute> attributes = new HashMap<String, Attribute>();
//		attributes.put(OTM_TEXT, new StringAttribute("new name"));
//		attributes.put(OTM_INT, new IntegerAttribute(5));
//		attributes.put(OTM_FLOAT, new FloatAttribute(5.0f));
//		attributes.put(OTM_DOUBLE, new DoubleAttribute(5.0));
//		attributes.put(OTM_BOOLEAN, new BooleanAttribute(false));
//		attributes.put(OTM_DATE, new DateAttribute(date));
//		featureModel.setAttributes(feature1, attributes);
//
//		String[] strings = (String[]) featureModel.getAttribute(feature1, OTM_TEXT).getValue();
//		Assert.assertEquals(strings[0], "new name");
//		Integer[] ints = (Integer[]) featureModel.getAttribute(feature1, OTM_INT).getValue();
//		Assert.assertEquals((Object) ints[0], 5);
//		Float[] floats = (Float[]) featureModel.getAttribute(feature1, OTM_FLOAT).getValue();
//		Assert.assertEquals((Object) floats[0], 5.0f);
//		Double[] doubles = (Double[]) featureModel.getAttribute(feature1, OTM_DOUBLE).getValue();
//		Assert.assertEquals((Object) doubles[0], 5.0);
//		Boolean[] bools = (Boolean[]) featureModel.getAttribute(feature1, OTM_BOOLEAN).getValue();
//		Assert.assertEquals(bools[0], false);
//		Date[] dates = (Date[]) featureModel.getAttribute(feature1, OTM_DATE).getValue();
//		Assert.assertEquals(dates[0], date);
	}

	/** Set 'null' on a OTM, expect an empty list as value.... */
	@Test
	@SuppressWarnings("rawtypes")
	public void setNullAttributes() throws Exception {
		Map<String, Attribute> attributes = new HashMap<String, Attribute>();
		attributes.put(MTO, null);
		attributes.put(OTM, null);
		featureModel.setAttributes(feature1, attributes);
		Assert.assertNull(featureModel.getAttribute(feature1, MTO).getValue());
		OneToManyAttribute attribute = (OneToManyAttribute) featureModel.getAttribute(feature1, OTM);
		Assert.assertNotNull(attribute.getValue());
		Assert.assertEquals(0, attribute.getValue().size());
	}
}