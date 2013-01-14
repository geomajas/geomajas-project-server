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

package org.geomajas.internal.filter;

import java.util.Map;

import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.internal.filter.FeatureModelPropertyAccessorFactory.FeatureModelPropertyAccessor;
import org.geomajas.internal.layer.feature.FeatureModelRegistry;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.junit.Assert;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * Test cases for the {@link FeatureModelPropertyAccessorFactory}.
 * 
 * @author Pieter De Graef
 */
public class PropertyAccessorTest {

	public static class TestFeatureModel implements FeatureModel {

		private static GeometryFactory factory = new GeometryFactory();

		private static Object SINGLE_FEATURE = new Object();

		private static Geometry GEOMETRY = factory.createPoint(new Coordinate(0,0));

		private static StringAttribute ATTRIBUTE = new StringAttribute("attr");

		private static String ID = "myId";

		private static String NESTED_ID = "nestedId";

		private static StringAttribute ID_ATTRIBUTE = new StringAttribute(NESTED_ID);

		public void setLayerInfo(VectorLayerInfo vectorLayerInfo) throws LayerException {
		}

		public Attribute getAttribute(Object feature, String name) throws LayerException {
			if(name.endsWith("@id")) {
				return ID_ATTRIBUTE;
			} else {
				return ATTRIBUTE;
			}
		}

		public Map<String, Attribute> getAttributes(Object feature) throws LayerException {
			return null;
		}

		public String getId(Object feature) throws LayerException {
			return ID;
		}

		public Geometry getGeometry(Object feature) throws LayerException {
			return GEOMETRY;
		}

		public void setAttributes(Object feature, Map<String, Attribute> attributes) throws LayerException {
		}

		public void setGeometry(Object feature, Geometry geometry) throws LayerException {
		}

		public Object newInstance() throws LayerException {
			return null;
		}

		public Object newInstance(String id) throws LayerException {
			return null;
		}

		public int getSrid() throws LayerException {
			return 0;
		}

		public String getGeometryAttributeName() throws LayerException {
			return null;
		}

		public boolean canHandle(Object feature) {
			return SINGLE_FEATURE == feature;
		}

	}

	@Test
	public void testPattern() {
		Assert.assertTrue(FeatureModelPropertyAccessor.PROPERTY_PATTERN.matcher("aa").matches());

		Assert.assertTrue(FeatureModelPropertyAccessor.PROPERTY_PATTERN.matcher("aa/bb").matches());
		Assert.assertTrue(FeatureModelPropertyAccessor.PROPERTY_PATTERN.matcher("aa.bb").matches());

		Assert.assertTrue(FeatureModelPropertyAccessor.PROPERTY_PATTERN.matcher("aa/bb/cc").matches());
		Assert.assertTrue(FeatureModelPropertyAccessor.PROPERTY_PATTERN.matcher("aa.bb.cc").matches());
		Assert.assertTrue(FeatureModelPropertyAccessor.PROPERTY_PATTERN.matcher("aa/bb.cc").matches());

		Assert.assertFalse(FeatureModelPropertyAccessor.PROPERTY_PATTERN.matcher("aa:bb").matches());
		Assert.assertFalse(FeatureModelPropertyAccessor.PROPERTY_PATTERN.matcher("aa\bb").matches());
		Assert.assertFalse(FeatureModelPropertyAccessor.PROPERTY_PATTERN.matcher("aa\\bb").matches());
		Assert.assertFalse(FeatureModelPropertyAccessor.PROPERTY_PATTERN.matcher("aa+bb").matches());

		Assert.assertTrue(FeatureModelPropertyAccessor.PROPERTY_PATTERN.matcher("aa.bb.@id").matches());
		Assert.assertTrue(FeatureModelPropertyAccessor.PROPERTY_PATTERN.matcher("aa/bb/@id").matches());
}

	@Test
	public void testAccess() {
		FeatureModelPropertyAccessor fmpa = new FeatureModelPropertyAccessor();
		FeatureModelRegistry.getRegistry().register(new TestFeatureModel());
		// a common attribute
		Assert.assertTrue(fmpa.canHandle(TestFeatureModel.SINGLE_FEATURE, "name", String.class));
		Assert.assertEquals(TestFeatureModel.ATTRIBUTE.getValue(),fmpa.get(TestFeatureModel.SINGLE_FEATURE, "name", String.class));
		// default geometry
		Assert.assertTrue(fmpa.canHandle(TestFeatureModel.SINGLE_FEATURE, "", Geometry.class));
		Assert.assertEquals(TestFeatureModel.GEOMETRY,fmpa.get(TestFeatureModel.SINGLE_FEATURE, "", Geometry.class));
		// id
		Assert.assertTrue(fmpa.canHandle(TestFeatureModel.SINGLE_FEATURE, "@id", Object.class));
		Assert.assertEquals(TestFeatureModel.ID,fmpa.get(TestFeatureModel.SINGLE_FEATURE, "@id", Object.class));
		// nested id
		Assert.assertTrue(fmpa.canHandle(TestFeatureModel.SINGLE_FEATURE, "name/@id", Object.class));
		Assert.assertEquals(TestFeatureModel.NESTED_ID, fmpa.get(TestFeatureModel.SINGLE_FEATURE, "name/@id", Object.class));
	}
}
