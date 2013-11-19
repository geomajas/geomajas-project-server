package org.geomajas.layer.feature.attribute;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import org.geomajas.configuration.PrimitiveType;
import org.geomajas.layer.feature.Attribute;
import org.junit.Assert;
import org.junit.Test;

public class AttributeToStringTest {

	@Test
	public void testPrimitive() {
		for (PrimitiveType type : PrimitiveType.values()) {
			switch(type){
				case BOOLEAN:
					Assert.assertEquals("null", new BooleanAttribute().toString());
					Assert.assertEquals("true", new BooleanAttribute(true).toString());
					Assert.assertEquals("false", new BooleanAttribute(false).toString());
					break;
				case CURRENCY:
					Assert.assertEquals("null", new CurrencyAttribute().toString());
					Assert.assertEquals("100.34", new CurrencyAttribute("100.34").toString());
					break;
				case DATE:
					Assert.assertEquals("null", new DateAttribute().toString());
					Date date = new Date();
					Assert.assertEquals(date.toString(), new DateAttribute(date).toString());
					break;
				case DOUBLE:
					Assert.assertEquals("null", new DoubleAttribute().toString());
					Assert.assertEquals("100.34", new DoubleAttribute(100.34).toString());
					break;
				case FLOAT:
					Assert.assertEquals("null", new FloatAttribute().toString());
					Assert.assertEquals("1.3523", new FloatAttribute(1.3523F).toString());
					break;
				case IMGURL:
					Assert.assertEquals("null", new ImageUrlAttribute().toString());
					Assert.assertEquals("http://test", new ImageUrlAttribute("http://test").toString());
					break;
				case INTEGER:
					Assert.assertEquals("null", new IntegerAttribute().toString());
					Assert.assertEquals("147436", new IntegerAttribute(147436).toString());
					break;
				case LONG:
					Assert.assertEquals("null", new LongAttribute().toString());
					Assert.assertEquals("13523", new LongAttribute(13523L).toString());
					break;
				case SHORT:
					Assert.assertEquals("null", new ShortAttribute().toString());
					Assert.assertEquals("123", new ShortAttribute((short)123).toString());
					break;
				case STRING:
					Assert.assertEquals("null", new StringAttribute().toString());
					Assert.assertEquals("100.34", new StringAttribute("100.34").toString());
					break;
				case URL:
					Assert.assertEquals("null", new UrlAttribute().toString());
					Assert.assertEquals("http://test", new UrlAttribute("http://test").toString());
					break;
				default:
					Assert.fail("Missing test for primitive type "+type);
			}
		}
	}
	
	@Test
	public void testManyToOne() {
		ManyToOneAttribute attribute = new ManyToOneAttribute();
		// all null
		Assert.assertEquals("null", attribute.toString());
		attribute.setValue(new AssociationValue());
		// empty value
		Assert.assertEquals("{id=null, attrs=null}", attribute.toString());
		attribute.getValue().setId(new IntegerAttribute(15));
		// id but no attributes
		Assert.assertEquals("{id=15, attrs=null}", attribute.toString());
		attribute.getValue().setId(null);
		attribute.getValue().setAllAttributes(new LinkedHashMap<String, Attribute<?>>());
		// no id, empty attributes
		Assert.assertEquals("{id=null, attrs={}}", attribute.toString());
		attribute.getValue().setId(new IntegerAttribute(12));
		// id, empty attributes
		Assert.assertEquals("{id=12, attrs={}}", attribute.toString());
		// id, primitive attributes
		attribute.getValue().getAllAttributes().put("name1", new StringAttribute("value1"));
		attribute.getValue().getAllAttributes().put("name2", new IntegerAttribute(5));
		attribute.getValue().getAllAttributes().put("name3", new FloatAttribute(3.14F));
		Assert.assertEquals("{id=12, name1=value1, name2=5, name3=3.14}", attribute.toString());
	}

	@Test
	public void testOneToMany() {
		OneToManyAttribute attribute = new OneToManyAttribute();
		// all null
		Assert.assertEquals("null", attribute.toString());
		attribute.setValue(new ArrayList<AssociationValue>());
		// empty value
		Assert.assertEquals("[]", attribute.toString());
		// some values
		AssociationValue val1 = new AssociationValue();
		attribute.getValue().add(val1);
		Assert.assertEquals("[{id=null, attrs=null}]", attribute.toString());
		AssociationValue val2 = new AssociationValue();
		attribute.getValue().add(val2);
		Assert.assertEquals("[{id=null, attrs=null}, {id=null, attrs=null}]", attribute.toString());

		val1.setId(new IntegerAttribute(15));
		val1.setAllAttributes(new LinkedHashMap<String, Attribute<?>>());
		// id, primitive attributes
		val1.getAllAttributes().put("name1", new StringAttribute("value1"));
		val1.getAllAttributes().put("name2", new IntegerAttribute(5));
		val1.getAllAttributes().put("name3", new FloatAttribute(3.14F));

		val2.setId(new IntegerAttribute(12));
		val2.setAllAttributes(new LinkedHashMap<String, Attribute<?>>());
		// id, primitive attributes
		val2.getAllAttributes().put("name1", new StringAttribute("value3"));
		val2.getAllAttributes().put("name2", new IntegerAttribute(7));
		Assert.assertEquals("[{id=15, name1=value1, name2=5, name3=3.14}, {id=12, name1=value3, name2=7}]", attribute.toString());
		
	}

}
