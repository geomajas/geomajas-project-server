package org.geomajas.layer.feature.attribute;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.layer.feature.Attribute;
import org.junit.Assert;
import org.junit.Test;

public class AssociationValueTest {

	@Test
	public void testNoArgsConstructor() {
		AssociationValue value = new AssociationValue();
		value.setId(new StringAttribute("1"));
		Map<String, Attribute<?>> attrs = new HashMap<String, Attribute<?>>();
		value.setAllAttributes(attrs);
		Assert.assertFalse(value.isPrimitiveOnly());
		try {
			value.getAttributes();
			Assert.fail("Primitive API accessible for mixed association value");
		} catch (UnsupportedOperationException uoe) {
		}
		Assert.assertSame(attrs, value.getAllAttributes());
	}

	@Test
	public void testMixedConstructor() {
		Map<String, Attribute<?>> attrs = new HashMap<String, Attribute<?>>();
		AssociationValue value = new AssociationValue(new StringAttribute("1"), attrs, false);
		Assert.assertFalse(value.isPrimitiveOnly());
		try {
			value.getAttributes();
			Assert.fail("Primitive API accessible for mixed association value");
		} catch (UnsupportedOperationException uoe) {
		}
		Assert.assertSame(attrs, value.getAllAttributes());
	}

	@Test
	public void testPrimitiveConstructor() {
		Map<String, Attribute<?>> attrs = new HashMap<String, Attribute<?>>();
		AssociationValue value = new AssociationValue(new StringAttribute("1"), attrs, true);
		Assert.assertTrue(value.isPrimitiveOnly());
		Assert.assertSame(attrs, value.getAttributes());
		Assert.assertSame(attrs, value.getAllAttributes());
	}
	
	@Test
	public void testPrimitiveConstructorBadArgument() {
		Map<String, Attribute<?>> attrs = new HashMap<String, Attribute<?>>();
		attrs.put("bad", new ManyToOneAttribute());
		try {
			AssociationValue value = new AssociationValue(new StringAttribute("1"), attrs, true);
			Assert.fail("Mixed attributes passed to primitive only value");
		} catch (IllegalArgumentException uoe) {
		}
	}
	
	@Test
	public void testSetAttributes() {
		// mixed
		Map<String, Attribute<?>> attrs = new HashMap<String, Attribute<?>>();
		attrs.put("orig", new ManyToOneAttribute());
		AssociationValue value = new AssociationValue(new StringAttribute("1"), attrs, false);
		Map<String, PrimitiveAttribute<?>> attrs2 = new HashMap<String, PrimitiveAttribute<?>>();
		attrs2.put("new", new StringAttribute("new"));
		try {
			value.setAttributes(attrs2);
			Assert.fail("Primitive attributes passed to mixed value");
		} catch (UnsupportedOperationException uoe) {
		}
		
		// primitive
		Map<String, Attribute<?>> attrs3 = new HashMap<String, Attribute<?>>();
		attrs3.put("orig", new StringAttribute("orig"));
		AssociationValue value2 = new AssociationValue(new StringAttribute("1"), attrs3, true);
		Map<String, PrimitiveAttribute<?>> attrs4 = new HashMap<String, PrimitiveAttribute<?>>();
		attrs4.put("new", new StringAttribute("new"));
		value2.setAttributes(attrs4);
		Assert.assertSame(attrs4, value2.getAttributes());
	}
	
	@Test
	public void testSetAllAttributes() {
		// mixed
		Map<String, Attribute<?>> attrs = new HashMap<String, Attribute<?>>();
		attrs.put("orig", new ManyToOneAttribute());
		AssociationValue value = new AssociationValue(new StringAttribute("1"), attrs, false);
		Map<String, Attribute<?>> attrs2 = new HashMap<String, Attribute<?>>();
		attrs2.put("new", new ManyToOneAttribute());
		value.setAllAttributes(attrs2);
		Assert.assertSame(attrs2, value.getAllAttributes());
		
		// primitive
		Map<String, Attribute<?>> attrs3 = new HashMap<String, Attribute<?>>();
		attrs3.put("orig", new StringAttribute("orig"));
		AssociationValue value2 = new AssociationValue(new StringAttribute("1"), attrs3, true);
		Map<String, Attribute<?>> attrs4 = new HashMap<String, Attribute<?>>();
		attrs4.put("new", new StringAttribute("new"));
		value2.setAllAttributes(attrs4);
		Assert.assertSame(attrs4, value2.getAllAttributes());
	}

	@Test
	public void testGetAttributeValue() {
		Map<String, Attribute<?>> attrs = new HashMap<String, Attribute<?>>();
		attrs.put("string", new StringAttribute("s1"));
		attrs.put("boolean", new BooleanAttribute(true));
		attrs.put("long", new LongAttribute(5L));
		AssociationValue value = new AssociationValue(new StringAttribute("1"), attrs, true);
		Assert.assertEquals("s1", value.getAttributeValue("string"));
		Assert.assertEquals(Boolean.TRUE, value.getAttributeValue("boolean"));
		Assert.assertEquals(new Long(5), value.getAttributeValue("long"));
		Assert.assertEquals(null, value.getAttributeValue("missing"));
	}
	
}
