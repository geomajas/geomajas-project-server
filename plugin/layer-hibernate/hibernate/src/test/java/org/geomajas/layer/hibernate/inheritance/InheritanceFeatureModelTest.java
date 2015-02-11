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

package org.geomajas.layer.hibernate.inheritance;

import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.feature.attribute.IntegerAttribute;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.layer.hibernate.AbstractHibernateInheritanceTest;
import org.geomajas.layer.hibernate.inheritance.pojo.AbstractHibernateTestFeature;
import org.geomajas.layer.hibernate.inheritance.pojo.ExtendedHibernateTestFeature;
import org.hibernate.metadata.ClassMetadata;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * Test for the HibernateFeatureModel that uses domain objects with inheritance in their model. Checks mainly to see if
 * the properties of the parent class can be properly accessed as well.
 * 
 * @author Pieter De Graef
 */
public class InheritanceFeatureModelTest extends AbstractHibernateInheritanceTest {

	private FeatureModel featureModel;

	private ExtendedHibernateTestFeature feature1;

	private ExtendedHibernateTestFeature feature2;

	private GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
	

	@Before
	public void setUpTestDataWithinTransaction() throws LayerException {
		featureModel = layer.getFeatureModel();

		feature1 = ExtendedHibernateTestFeature.getDefaultInstance1(new Long(1));
		feature2 = ExtendedHibernateTestFeature.getDefaultInstance2(new Long(2));

		feature1.setGeometry(geometryFactory.createPoint(new Coordinate(3, 42)));
		feature2.setGeometry(geometryFactory.createPoint(new Coordinate(0, 0)));
	}

	@Test
	public void testGetAttribute() throws LayerException {
		ClassMetadata metadata = factory.getClassMetadata(AbstractHibernateTestFeature.class);
		Attribute<?> attribute = featureModel.getAttribute(feature1, PARAM_INT_ATTR);
		Assert.assertNotNull(attribute);
		Assert.assertTrue(attribute instanceof IntegerAttribute);
		Assert.assertEquals(feature1.getIntAttr(), attribute.getValue());

		attribute = featureModel.getAttribute(feature1, PARAM_TEXT_ATTR);
		Assert.assertNotNull(attribute);
		Assert.assertTrue(attribute instanceof StringAttribute);
		Assert.assertEquals(feature1.getTextAttr(), attribute.getValue());

		attribute = featureModel.getAttribute(feature1, ATTR__MANY_TO_ONE__DOT__INT);
		Assert.assertNotNull(attribute);
		Assert.assertTrue(attribute instanceof IntegerAttribute);
		Assert.assertEquals(feature1.getManyToOne().getIntAttr(), attribute.getValue());

		attribute = featureModel.getAttribute(feature1, ATTR__MANY_TO_ONE__DOT__TEXT);
		Assert.assertNotNull(attribute);
		Assert.assertTrue(attribute instanceof StringAttribute);
		Assert.assertEquals(feature1.getManyToOne().getTextAttr(), attribute.getValue());

		// attribute = featureModel.getAttribute(feature1, PARAM_ONE_TO_MANY + HibernateLayerUtil.SEPARATOR
		// + PARAM_INT_ATTR + "[0]");
	}
}