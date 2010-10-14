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
package org.geomajas.layer.feature.attribute;

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

		attribute = new ArrayAttribute<String>(new String[] {"test"});
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
		Assert.assertEquals(clonedValue.getAttributes().get("stringAttribute").getValue(), "value1");
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
		Assert.assertEquals(clonedValue.get(0).getAttributes().get("stringAttribute").getValue(), "value1");
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
}
