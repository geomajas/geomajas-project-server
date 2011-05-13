/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.layer.hibernate.recursive;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.layer.hibernate.AbstractHibernateRecursiveTest;
import org.geomajas.layer.hibernate.recursive.pojo.RecursiveTopFeature;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * <p>
 * Testing recursive capabilities of the Hibernate FeatureModel.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class RecursiveFeatureModelTest extends AbstractHibernateRecursiveTest {

	private FeatureModel featureModel1;

	private FeatureModel featureModel2;

	private FeatureModel featureModel3;

	private RecursiveTopFeature feature1;

	private RecursiveTopFeature feature2;

	private GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

	@Before
	public void setUpTestDataWithinTransaction() throws LayerException {
		featureModel1 = layer1.getFeatureModel();
		feature1 = RecursiveTopFeature.getDefaultInstance1(new Long(1));
		feature1.getManyToOne().setGeometry(geometryFactory.createPoint(new Coordinate(3, 42)));

		featureModel2 = layer2.getFeatureModel();
		feature2 = RecursiveTopFeature.getDefaultInstance1(new Long(2));
		feature2.getManyToOne().setGeometry(geometryFactory.createPoint(new Coordinate(342, 0)));

		featureModel3 = layer3.getFeatureModel();
	}

	public boolean isInitialised() {
		return false; // need a new database for each run
	}

	@Test
	public void testGetGeometry() throws LayerException {
		Geometry geometry = featureModel1.getGeometry(feature1);
		Assert.assertNotNull(geometry);
	}

	@Test
	public void testSetGeometry() throws LayerException {
		Geometry geometry = geometryFactory.createPoint(new Coordinate(1, 2));
		featureModel1.setGeometry(feature1, geometry);

		Geometry result = featureModel1.getGeometry(feature1);
		Assert.assertNotNull(result);
		Assert.assertEquals(1.0, result.getCoordinate().x, 0.0001);
		Assert.assertEquals(2.0, result.getCoordinate().y, 0.0001);
	}

	@Test
	public void testGetAttributeLayer1() throws LayerException {
		Attribute<?> attribute = featureModel1.getAttribute(feature1, MTO_TEXT);
		Assert.assertEquals(feature1.getManyToOne().getTextAttr(), attribute.getValue());

		attribute = featureModel1.getAttribute(feature1, MTO_MTO_TEXT);
		Assert.assertEquals(feature1.getManyToOne().getManyToOne().getTextAttr(), attribute.getValue());
	}

	@Test
	public void testGetAttributeLayer2() throws LayerException {
		Attribute<?> attribute = featureModel2.getAttribute(feature2, MTO_MTO_TEXT);
		Assert.assertEquals(feature2.getManyToOne().getManyToOne().getTextAttr(), attribute.getValue());

		Object value1 = featureModel1.getAttribute(feature1, MTO_MTO_TEXT).getValue();
		Object value2 = featureModel2.getAttribute(feature2, MTO_MTO_TEXT).getValue();
		Assert.assertEquals(value1, value2);
	}

	@Test
	public void testGetAttributeLayer3() throws LayerException {
		Attribute<?> attribute = featureModel3.getAttribute(feature1, MTO_MTO_TEXT);
		Assert.assertEquals(feature1.getManyToOne().getManyToOne().getTextAttr(), attribute.getValue());

		Object value1 = featureModel1.getAttribute(feature1, MTO_MTO_TEXT).getValue();
		Object value2 = featureModel3.getAttribute(feature1, MTO_MTO_TEXT).getValue();
		Assert.assertEquals(value1, value2);
	}

	@Test
	@SuppressWarnings("rawtypes")
	public void testSetAttributesLayer1() throws Exception {
		final String value = "hello world";

		Map<String, Attribute> attributes = new HashMap<String, Attribute>();
		attributes.put(MTO_TEXT, new StringAttribute(value));
		attributes.put(MTO_MTO_TEXT, new StringAttribute(value));
		featureModel1.setAttributes(feature1, attributes);

		String result = (String) featureModel1.getAttribute(feature1, MTO_TEXT).getValue();
		Assert.assertEquals(value, result);
		result = (String) featureModel1.getAttribute(feature1, MTO_MTO_TEXT).getValue();
		Assert.assertEquals(value, result);
	}

	@Test
	@SuppressWarnings("rawtypes")
	public void testSetAttributesLayer2() throws Exception {
		final String value = "hello world";

		Map<String, Attribute> attributes = new HashMap<String, Attribute>();
		attributes.put(MTO_MTO_TEXT, new StringAttribute(value));
		featureModel2.setAttributes(feature1, attributes);

		String result = (String) featureModel2.getAttribute(feature1, MTO_MTO_TEXT).getValue();
		Assert.assertEquals(value, result);
	}

	@Test
	@SuppressWarnings("rawtypes")
	public void testSetAttributesLayer3() throws Exception {
		final String value = "hello world";

		Map<String, Attribute> attributes = new HashMap<String, Attribute>();
		attributes.put(MTO_MTO_TEXT, new StringAttribute(value));
		featureModel3.setAttributes(feature1, attributes);

		String result = (String) featureModel3.getAttribute(feature1, MTO_MTO_TEXT).getValue();
		Assert.assertEquals(value, result);
	}
}