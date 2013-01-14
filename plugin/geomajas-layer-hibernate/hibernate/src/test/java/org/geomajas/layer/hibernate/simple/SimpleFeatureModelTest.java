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
package org.geomajas.layer.hibernate.simple;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.feature.attribute.BooleanAttribute;
import org.geomajas.layer.feature.attribute.DateAttribute;
import org.geomajas.layer.feature.attribute.DoubleAttribute;
import org.geomajas.layer.feature.attribute.FloatAttribute;
import org.geomajas.layer.feature.attribute.IntegerAttribute;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.layer.hibernate.AbstractHibernateSimpleTest;
import org.geomajas.layer.hibernate.simple.pojo.SimpleFeature;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * Test for the HibernateFeatureModel that only uses simple domain objects in it's model.
 * 
 * @author Pieter De Graef
 */
public class SimpleFeatureModelTest extends AbstractHibernateSimpleTest {

	private FeatureModel featureModel;

	private SimpleFeature feature1;

	private GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

	@Before
	public void setUpTestDataWithinTransaction() throws LayerException {
		featureModel = layer.getFeatureModel();

		feature1 = SimpleFeature.getDefaultInstance1(new Long(1));
		feature1.setGeometry(geometryFactory.createPoint(new Coordinate(3, 42)));
	}

	public boolean isInitialised() {
		return false; // need a new database for each run
	}

	@Test
	public void getId() throws Exception {
		Assert.assertEquals("1", featureModel.getId(feature1));
	}

	@Test
	public void getNullAttribute() {
		try {
			featureModel.getAttribute(null, PARAM_TEXT_ATTR);
		} catch (LayerException e) {
			Assert.assertTrue(e instanceof LayerException);
		}
	}

	@Test
	public void getNonExistingAttribute() {
		try {
			featureModel.getAttribute(feature1, "non-existing-attribute");
		} catch (LayerException e) {
			Assert.assertTrue(e instanceof LayerException);
		}
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
	public void getAttributes() throws Exception {
		Assert.assertEquals("default-name-1", featureModel.getAttributes(feature1).get(PARAM_TEXT_ATTR).getValue());
	}

	@Test
	public void getGeometry() throws LayerException {
		Geometry geometry = featureModel.getGeometry(feature1);
		Assert.assertNotNull(geometry);
		Assert.assertTrue(geometry instanceof Point);
		Assert.assertEquals(3.0, geometry.getCoordinate().x, 0.00001);
		Assert.assertEquals(42.0, geometry.getCoordinate().y, 0.00001);

		geometry = featureModel.getGeometry(null);
		Assert.assertNull(geometry);
	}

	@Test
	public void setGeometry() throws LayerException {
		Point point = geometryFactory.createPoint(new Coordinate(1, 2));
		featureModel.setGeometry(feature1, point);

		Geometry geometry = featureModel.getGeometry(feature1);
		Assert.assertNotNull(geometry);
		Assert.assertTrue(geometry instanceof Point);
		Assert.assertEquals(1.0, geometry.getCoordinate().x, 0.00001);
		Assert.assertEquals(2.0, geometry.getCoordinate().y, 0.00001);

		featureModel.setGeometry(feature1, null);
		geometry = featureModel.getGeometry(feature1);
		Assert.assertNull(geometry);
	}

	@Test
	public void getGeometryAttributeName() throws LayerException {
		String name = featureModel.getGeometryAttributeName();
		Assert.assertEquals("geometry", name);
	}

	@Test
	public void getSrid() throws Exception {
		Assert.assertEquals(4326, featureModel.getSrid());
	}

	@Test
	@SuppressWarnings("rawtypes")
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

	@Test
	@SuppressWarnings("rawtypes")
	public void setNullAttributes() throws Exception {
		Date date = new Date();
		Map<String, Attribute> attributes = new HashMap<String, Attribute>();
		attributes.put(PARAM_TEXT_ATTR, null);
		attributes.put(PARAM_INT_ATTR, null);
		attributes.put(PARAM_FLOAT_ATTR, null);
		attributes.put(PARAM_DOUBLE_ATTR, null);
		attributes.put(PARAM_BOOLEAN_ATTR, null);
		attributes.put(PARAM_DATE_ATTR, null);
		featureModel.setAttributes(feature1, attributes);
		Assert.assertNull(featureModel.getAttribute(feature1, PARAM_TEXT_ATTR).getValue());
		Assert.assertNull(featureModel.getAttribute(feature1, PARAM_INT_ATTR).getValue());
		Assert.assertNull(featureModel.getAttribute(feature1, PARAM_FLOAT_ATTR).getValue());
		Assert.assertNull(featureModel.getAttribute(feature1, PARAM_DOUBLE_ATTR).getValue());
		Assert.assertNull(featureModel.getAttribute(feature1, PARAM_BOOLEAN_ATTR).getValue());
		Assert.assertNull(featureModel.getAttribute(feature1, PARAM_DATE_ATTR).getValue());

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
}