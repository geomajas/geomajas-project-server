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
package org.geomajas.layer.feature.attribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.layer.feature.Attribute;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test-case that tests the clone methods for all attributes.
 * 
 * @author Pieter De Graef
 */
public class AttributeCloneTest {

	@Test
	public void testArrayAttribute() {
		Attribute<?> attribute = new ArrayAttribute<String>();
		Attribute<?> clone = (Attribute<?>) attribute.clone();
		Assert.assertEquals(attribute.getValue(), clone.getValue());

		attribute = new ArrayAttribute<String>(new String[] { "test" });
		attribute.setEditable(true);
		clone = (Attribute<?>) attribute.clone();
		Assert.assertEquals(((Object[]) attribute.getValue())[0], ((Object[]) clone.getValue())[0]);
		Assert.assertEquals(attribute.isEditable(), clone.isEditable());
	}

	@Test
	public void testBooleanAttribute() {
		Attribute<?> attribute = new BooleanAttribute();
		Attribute<?> clone = (Attribute<?>) attribute.clone();
		Assert.assertEquals(attribute.getValue(), clone.getValue());

		attribute = new BooleanAttribute(true);
		attribute.setEditable(true);
		clone = (Attribute<?>) attribute.clone();
		Assert.assertEquals(attribute.getValue(), clone.getValue());
		Assert.assertEquals(attribute.isEditable(), clone.isEditable());
	}

	@Test
	public void testCurrencyAttribute() {
		Attribute<?> attribute = new CurrencyAttribute();
		Attribute<?> clone = (Attribute<?>) attribute.clone();
		Assert.assertEquals(attribute.getValue(), clone.getValue());

		attribute = new CurrencyAttribute("342€");
		attribute.setEditable(true);
		clone = (Attribute<?>) attribute.clone();
		Assert.assertEquals(attribute.getValue(), clone.getValue());
		Assert.assertEquals(attribute.isEditable(), clone.isEditable());
	}

	@Test
	public void testDateAttribute() {
		Attribute<?> attribute = new DateAttribute();
		Attribute<?> clone = (Attribute<?>) attribute.clone();
		Assert.assertEquals(attribute.getValue(), clone.getValue());

		attribute = new DateAttribute(new Date());
		attribute.setEditable(true);
		clone = (Attribute<?>) attribute.clone();
		Assert.assertEquals(attribute.getValue(), clone.getValue());
		Assert.assertEquals(attribute.isEditable(), clone.isEditable());
	}

	@Test
	public void testDoubleAttribute() {
		Attribute<?> attribute = new DoubleAttribute();
		Attribute<?> clone = (Attribute<?>) attribute.clone();
		Assert.assertEquals(attribute.getValue(), clone.getValue());

		attribute = new DoubleAttribute(342.0);
		attribute.setEditable(true);
		clone = (Attribute<?>) attribute.clone();
		Assert.assertEquals(attribute.getValue(), clone.getValue());
		Assert.assertEquals(attribute.isEditable(), clone.isEditable());
	}

	@Test
	public void testFloatAttribute() {
		Attribute<?> attribute = new FloatAttribute();
		Attribute<?> clone = (Attribute<?>) attribute.clone();
		Assert.assertEquals(attribute.getValue(), clone.getValue());

		attribute = new FloatAttribute(342.0F);
		attribute.setEditable(true);
		clone = (Attribute<?>) attribute.clone();
		Assert.assertEquals(attribute.getValue(), clone.getValue());
		Assert.assertEquals(attribute.isEditable(), clone.isEditable());
	}

	@Test
	public void testImageUrlAttribute() {
		Attribute<?> attribute = new ImageUrlAttribute();
		Attribute<?> clone = (Attribute<?>) attribute.clone();
		Assert.assertEquals(attribute.getValue(), clone.getValue());

		attribute = new ImageUrlAttribute("the_image_url");
		attribute.setEditable(true);
		clone = (Attribute<?>) attribute.clone();
		Assert.assertEquals(attribute.getValue(), clone.getValue());
		Assert.assertEquals(attribute.isEditable(), clone.isEditable());
	}

	@Test
	public void testIntegerAttribute() {
		Attribute<?> attribute = new IntegerAttribute();
		Attribute<?> clone = (Attribute<?>) attribute.clone();
		Assert.assertEquals(attribute.getValue(), clone.getValue());

		attribute = new IntegerAttribute(342);
		attribute.setEditable(true);
		clone = (Attribute<?>) attribute.clone();
		Assert.assertEquals(attribute.getValue(), clone.getValue());
		Assert.assertEquals(attribute.isEditable(), clone.isEditable());
	}

	@Test
	public void testLongAttribute() {
		Attribute<?> attribute = new LongAttribute();
		Attribute<?> clone = (Attribute<?>) attribute.clone();
		Assert.assertEquals(attribute.getValue(), clone.getValue());

		attribute = new LongAttribute(342L);
		attribute.setEditable(true);
		clone = (Attribute<?>) attribute.clone();
		Assert.assertEquals(attribute.getValue(), clone.getValue());
		Assert.assertEquals(attribute.isEditable(), clone.isEditable());
	}

	@Test
	public void testManyToOneAttribute() {
		Attribute<?> attribute = new ManyToOneAttribute();
		Attribute<?> clone = (Attribute<?>) attribute.clone();
		Assert.assertEquals(attribute.getValue(), clone.getValue());

		Long idValue = 1L;
		Map<String, PrimitiveAttribute<?>> attributes = new HashMap<String, PrimitiveAttribute<?>>();
		attributes.put("stringAttribute", new StringAttribute("value1"));
		AssociationValue value = new AssociationValue(new LongAttribute(idValue), attributes);
		attribute = new ManyToOneAttribute(value);
		attribute.setEditable(true);
		clone = (Attribute<?>) attribute.clone();
		Assert.assertEquals(attribute.isEditable(), clone.isEditable());

		AssociationValue clonedValue = (AssociationValue) clone.getValue();
		Assert.assertEquals(clonedValue.getId().getValue(), idValue);
		Assert.assertEquals(clonedValue.getAllAttributes().get("stringAttribute").getValue(), "value1");
		// Assert.assertEquals(attribute.getValue(), clone.getValue()); // AssociationValue has no equals method...
	}

	@Test
	public void testOneToManyAttribute() {
		Attribute<?> attribute = new OneToManyAttribute();
		Attribute<?> clone = (Attribute<?>) attribute.clone();
		Assert.assertEquals(attribute.getValue(), clone.getValue());

		Long idValue = 1L;
		Map<String, PrimitiveAttribute<?>> attributes = new HashMap<String, PrimitiveAttribute<?>>();
		attributes.put("stringAttribute", new StringAttribute("value1"));
		AssociationValue value = new AssociationValue(new LongAttribute(idValue), attributes);
		attribute = new OneToManyAttribute(Collections.singletonList(value));
		attribute.setEditable(true);
		clone = (Attribute<?>) attribute.clone();
		Assert.assertEquals(attribute.isEditable(), clone.isEditable());

		List<AssociationValue> clonedValue = (List<AssociationValue>) clone.getValue();
		Assert.assertEquals(clonedValue.get(0).getId().getValue(), idValue);
		Assert.assertEquals(clonedValue.get(0).getAllAttributes().get("stringAttribute").getValue(), "value1");
	}

	@Test
	public void testNestedAttribute() {
		OneToManyAttribute many = new OneToManyAttribute(new ArrayList<AssociationValue>());
		ManyToOneAttribute one = new ManyToOneAttribute(new AssociationValue(null, new HashMap<String, Attribute<?>>(),
				false));

		// create 2 manyInMany
		OneToManyAttribute manyInMany = new OneToManyAttribute(new ArrayList<AssociationValue>());
		AssociationValue manyInMany1 = new AssociationValue(null, new HashMap<String, Attribute<?>>(), false);
		manyInMany1.getAllAttributes().put("textAttr", new StringAttribute("manyInMany1"));
		manyInMany.getValue().add(manyInMany1);
		AssociationValue manyInMany2 = new AssociationValue(null, new HashMap<String, Attribute<?>>(), false);
		manyInMany2.getAllAttributes().put("textAttr", new StringAttribute("manyInMany2"));
		manyInMany.getValue().add(manyInMany2);

		// create oneInMany
		ManyToOneAttribute oneInMany = new ManyToOneAttribute();
		AssociationValue oneInManyValue = new AssociationValue(null, new HashMap<String, Attribute<?>>(), false);
		oneInManyValue.getAllAttributes().put("textAttr", new StringAttribute("oneInMany"));
		oneInMany.setValue(oneInManyValue);

		// create 2 manyInOne
		OneToManyAttribute manyInOne = new OneToManyAttribute(new ArrayList<AssociationValue>());
		AssociationValue manyInOne1 = new AssociationValue(null, new HashMap<String, Attribute<?>>(), false);
		manyInOne1.getAllAttributes().put("textAttr", new StringAttribute("manyInOne1"));
		manyInOne.getValue().add(manyInOne1);
		AssociationValue manyInOne2 = new AssociationValue(null, new HashMap<String, Attribute<?>>(), false);
		manyInOne2.getAllAttributes().put("textAttr", new StringAttribute("manyInOne2"));
		manyInOne.getValue().add(manyInOne2);

		// create oneInOne
		ManyToOneAttribute oneInOne = new ManyToOneAttribute();
		AssociationValue oneInOneValue = new AssociationValue(null, new HashMap<String, Attribute<?>>(), false);
		oneInOneValue.getAllAttributes().put("textAttr", new StringAttribute("oneInOne"));
		oneInOne.setValue(oneInOneValue);

		// create 2 many
		AssociationValue many1 = new AssociationValue(null, new HashMap<String, Attribute<?>>(), false);
		AssociationValue many2 = new AssociationValue(null, new HashMap<String, Attribute<?>>(), false);
		// add manyInMany to many1
		many1.getAllAttributes().put("manyInMany", manyInMany);
		// add oneInMany to many2
		many2.getAllAttributes().put("oneInMany", oneInMany);
		// add to many
		many.getValue().add(many1);
		many.getValue().add(many2);

		// add manyInOne to one
		one.getValue().getAllAttributes().put("manyInOne", manyInOne);
		// add oneInOne to one
		one.getValue().getAllAttributes().put("oneInOne", oneInOne);

		Attribute<?> manyAttr = (Attribute<?>) many.clone();
		Attribute<?> oneAttr = (Attribute<?>) one.clone();

		Assert.assertTrue(manyAttr instanceof OneToManyAttribute);
		Assert.assertTrue(oneAttr instanceof ManyToOneAttribute);
		OneToManyAttribute manyClone = (OneToManyAttribute) manyAttr;
		ManyToOneAttribute oneClone = (ManyToOneAttribute) oneAttr;

		Assert.assertNotNull(manyClone);
		Assert.assertNotNull(oneClone);

		assertCloned(many, manyClone);
		assertCloned(one, oneClone);

	}


	@Test
	public void testShortAttribute() {
		Attribute<?> attribute = new ShortAttribute();
		Attribute<?> clone = (Attribute<?>) attribute.clone();
		Assert.assertEquals(attribute.getValue(), clone.getValue());

		attribute = new ShortAttribute(new Short("342"));
		attribute.setEditable(true);
		clone = (Attribute<?>) attribute.clone();
		Assert.assertEquals(attribute.getValue(), clone.getValue());
		Assert.assertEquals(attribute.isEditable(), clone.isEditable());
	}

	@Test
	public void testStringAttribute() {
		Attribute<?> attribute = new StringAttribute();
		Attribute<?> clone = (Attribute<?>) attribute.clone();
		Assert.assertEquals(attribute.getValue(), clone.getValue());

		attribute = new StringAttribute("some value");
		attribute.setEditable(true);
		clone = (Attribute<?>) attribute.clone();
		Assert.assertEquals(attribute.getValue(), clone.getValue());
		Assert.assertEquals(attribute.isEditable(), clone.isEditable());
	}

	@Test
	public void testUrlAttribute() {
		Attribute<?> attribute = new UrlAttribute();
		Attribute<?> clone = (Attribute<?>) attribute.clone();
		Assert.assertEquals(attribute.getValue(), clone.getValue());

		attribute = new UrlAttribute("some URL");
		attribute.setEditable(true);
		clone = (Attribute<?>) attribute.clone();
		Assert.assertEquals(attribute.getValue(), clone.getValue());
		Assert.assertEquals(attribute.isEditable(), clone.isEditable());
	}
	
	private void assertCloned(AssociationValue original, AssociationValue clone) {
		for (Map.Entry<String, Attribute<?>> entry : original.getAllAttributes().entrySet()) {
			assertCloned(entry.getValue(), clone.getAllAttributes().get(entry.getKey()));
		}
	}

	private void assertCloned(Attribute<?> original, Attribute<?> clone) {
		if(original instanceof PrimitiveAttribute){
			PrimitiveAttribute prim = (PrimitiveAttribute)original;
			Assert.assertTrue(clone instanceof PrimitiveAttribute);
			PrimitiveAttribute primClone = (PrimitiveAttribute)clone;
			Assert.assertEquals(prim.getType(), primClone.getType());
			Assert.assertEquals(original.getValue(), clone.getValue());
		} else if(original instanceof OneToManyAttribute){
			OneToManyAttribute many = (OneToManyAttribute)original;
			Assert.assertTrue(clone instanceof OneToManyAttribute);
			OneToManyAttribute manyClone = (OneToManyAttribute)clone;
			int index = 0;
			Assert.assertEquals(many.getValue().size(), manyClone.getValue().size());
			for (AssociationValue assoc : many.getValue()) {
				assertCloned(assoc, manyClone.getValue().get(index++));
			}
		} else if(original instanceof ManyToOneAttribute){
			ManyToOneAttribute one = (ManyToOneAttribute)original;
			Assert.assertTrue(clone instanceof ManyToOneAttribute);
			ManyToOneAttribute oneClone = (ManyToOneAttribute)clone;
			assertCloned(one.getValue(), oneClone.getValue());
		}
	}

}
