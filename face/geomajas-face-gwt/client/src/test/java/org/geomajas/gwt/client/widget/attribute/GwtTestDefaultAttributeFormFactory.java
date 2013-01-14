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

package org.geomajas.gwt.client.widget.attribute;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.PrimitiveType;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.attribute.BooleanAttribute;
import org.geomajas.layer.feature.attribute.CurrencyAttribute;
import org.geomajas.layer.feature.attribute.DateAttribute;
import org.geomajas.layer.feature.attribute.DoubleAttribute;
import org.geomajas.layer.feature.attribute.FloatAttribute;
import org.geomajas.layer.feature.attribute.ImageUrlAttribute;
import org.geomajas.layer.feature.attribute.IntegerAttribute;
import org.geomajas.layer.feature.attribute.LongAttribute;
import org.geomajas.layer.feature.attribute.PrimitiveAttribute;
import org.geomajas.layer.feature.attribute.ShortAttribute;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.layer.feature.attribute.UrlAttribute;

import com.google.gwt.junit.client.GWTTestCase;

public class GwtTestDefaultAttributeFormFactory extends GWTTestCase {

	FeatureFormFactory factory;

	List<AttributeInfoPair> pairs;

	public String getModuleName() {
		return "org.geomajas.gwt.GeomajasClientTest";
	}

	@Override
	protected void gwtSetUp() throws Exception {
		factory = new DefaultFeatureFormFactory();
		pairs = new ArrayList<AttributeInfoPair>();
		pairs.add(new AttributeInfoPair("name1", "label1", (short) 123));
		pairs.add(new AttributeInfoPair("name2", "label2", (int) 123456));
		pairs.add(new AttributeInfoPair("name3", "label3", (long) 123456789));
		pairs.add(new AttributeInfoPair("name4", "label4", (float) 123.456578));
		pairs.add(new AttributeInfoPair("name5", "label5", (double) 456.7890));
		pairs.add(new AttributeInfoPair("name6", "label6", "someString", PrimitiveType.STRING));
		pairs.add(new AttributeInfoPair("name7", "label7", "http://some.domain.com/", PrimitiveType.URL));
		pairs.add(new AttributeInfoPair("name8", "label8", "30.14", PrimitiveType.CURRENCY));
		pairs.add(new AttributeInfoPair("name9", "label9", "http://some.domain.com/image.png", PrimitiveType.IMGURL));
		// This one fails, to deep in the javascript ?
		// pairs.add(new AttributeInfoPair("name10", "label10", new Date("01/01/2009")));
	}

	public void testCreateForm() {
		factory.createFeatureForm(null);
		
//		EditableAttributeForm form = factory.createEditableForm(asInfos(pairs));
//		Assert.assertNotNull(form);
//		Assert.assertNotNull(form.getWidget());
	}

	public void testFromTo() {
//		EditableAttributeForm form = factory.createEditableForm(asInfos(pairs));
//		for (AttributeInfoPair pair : pairs) {
//			pair.testFromTo(form);
//		}
	}

	private List<AttributeInfo> asInfos(List<AttributeInfoPair> pairs) {
		List<AttributeInfo> infos = new ArrayList<AttributeInfo>();
		for (AttributeInfoPair pair : pairs) {
			infos.add(pair.getInfo());
		}
		return infos;
	}

	static class AttributeInfoPair {

		private Attribute attribute;

		private AttributeInfo info;

		AttributeInfoPair(String name, String label, short value) {
			this.attribute = new ShortAttribute(value);
			this.info = new PrimitiveAttributeInfo(name, label, PrimitiveType.SHORT);
		}

		AttributeInfoPair(String name, String label, int value) {
			this.attribute = new IntegerAttribute(value);
			this.info = new PrimitiveAttributeInfo(name, label, PrimitiveType.INTEGER);
		}

		AttributeInfoPair(String name, String label, long value) {
			this.attribute = new LongAttribute(value);
			this.info = new PrimitiveAttributeInfo(name, label, PrimitiveType.LONG);
		}

		AttributeInfoPair(String name, String label, float value) {
			this.attribute = new FloatAttribute(value);
			this.info = new PrimitiveAttributeInfo(name, label, PrimitiveType.FLOAT);
		}

		AttributeInfoPair(String name, String label, double value) {
			this.attribute = new DoubleAttribute(value);
			this.info = new PrimitiveAttributeInfo(name, label, PrimitiveType.DOUBLE);
		}

		AttributeInfoPair(String name, String label, String value, PrimitiveType type) {
			switch (type) {
				case CURRENCY:
					attribute = new CurrencyAttribute(value);
					break;
				case STRING:
					attribute = new StringAttribute(value);
					break;
				case URL:
					attribute = new UrlAttribute(value);
					break;
				case IMGURL:
					attribute = new ImageUrlAttribute(value);
					break;
			}
			this.attribute = new StringAttribute(value);
			this.info = new PrimitiveAttributeInfo(name, label, type);
		}

		AttributeInfoPair(String name, String label, Date value) {
			this.attribute = new DateAttribute(value);
			this.info = new PrimitiveAttributeInfo(name, label, PrimitiveType.DATE);
		}

//		void testFromTo(EditableAttributeForm form) {
//			form.toForm(info.getName(), attribute);
//			Attribute copy = newAttribute();
//			form.fromForm(info.getName(), copy);
//			if (attribute instanceof PrimitiveAttribute<?>) {
//				PrimitiveAttribute<?> p = (PrimitiveAttribute<?>) attribute;
//				switch (p.getType()) {
//					case BOOLEAN:
//					case SHORT:
//					case INTEGER:
//					case LONG:
//					case CURRENCY:
//					case STRING:
//					case DATE:
//					case URL:
//					case IMGURL:
//						Assert.assertEquals(getAttributeValue(attribute), getAttributeValue(copy));
//						break;
//					case FLOAT:
//						Assert.assertEquals((Float) getAttributeValue(attribute), (Float) getAttributeValue(copy),
//								0.0001);
//						break;
//					case DOUBLE:
//						// Only checked to float precision !!!!
//						Assert.assertEquals((Double) getAttributeValue(attribute), (Double) getAttributeValue(copy),
//								0.0001);
//						break;
//				}
//			}
//		}

		Object getAttributeValue(Attribute attribute) {
			if (attribute instanceof PrimitiveAttribute<?>) {
				PrimitiveAttribute<?> p = (PrimitiveAttribute<?>) attribute;
				switch (p.getType()) {
					case BOOLEAN:
						return ((BooleanAttribute) p).getValue();
					case SHORT:
						return ((ShortAttribute) p).getValue();
					case INTEGER:
						return ((IntegerAttribute) p).getValue();
					case LONG:
						return ((LongAttribute) p).getValue();
					case FLOAT:
						return ((FloatAttribute) p).getValue();
					case DOUBLE:
						return ((DoubleAttribute) p).getValue();
					case CURRENCY:
						return ((CurrencyAttribute) p).getValue();
					case STRING:
						return ((StringAttribute) p).getValue();
					case DATE:
						return ((DateAttribute) p).getValue();
					case URL:
						return ((UrlAttribute) p).getValue();
					case IMGURL:
						return ((ImageUrlAttribute) p).getValue();
				}
				throw new IllegalArgumentException("Unknown primitive in test");
			} else {
				throw new IllegalArgumentException("Non primitives not supported by test");
			}
		}

		Attribute newAttribute() {
			if (attribute instanceof PrimitiveAttribute<?>) {
				PrimitiveAttribute<?> p = (PrimitiveAttribute<?>) attribute;
				switch (p.getType()) {
					case BOOLEAN:
						return new BooleanAttribute();
					case SHORT:
						return new ShortAttribute();
					case INTEGER:
						return new IntegerAttribute();
					case LONG:
						return new LongAttribute();
					case FLOAT:
						return new FloatAttribute();
					case DOUBLE:
						return new DoubleAttribute();
					case CURRENCY:
						return new CurrencyAttribute();
					case STRING:
						return new StringAttribute();
					case DATE:
						return new DateAttribute();
					case URL:
						return new UrlAttribute();
					case IMGURL:
						return new ImageUrlAttribute();
				}
				throw new IllegalArgumentException("Unknown primtive in test");
			} else {
				throw new IllegalArgumentException("Non primitives not supported by test");
			}
		}

		public Attribute getAttribute() {
			return attribute;
		}

		public AttributeInfo getInfo() {
			return info;
		}
	}
}
