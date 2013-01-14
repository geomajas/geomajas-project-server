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
package org.geomajas.internal.service;

import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.AssociationType;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.PrimitiveType;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.attribute.AssociationAttribute;
import org.geomajas.layer.feature.attribute.AssociationValue;
import org.geomajas.layer.feature.attribute.BooleanAttribute;
import org.geomajas.service.DtoConverterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test for attribute converter.
 * 
 * @author Jan De Moerloose
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "/org/geomajas/spring/moreContext.xml" })
public class AttributeConverterTest {

	@Autowired
	private DtoConverterService converter;

	@Test
	public void testToObject() throws Exception {
		BooleanAttribute attribute = new BooleanAttribute(true);
		Assert.assertTrue(converter.toInternal(attribute) instanceof Boolean);
		Assert.assertTrue((Boolean) converter.toInternal(attribute));
	}

	@Test
	public void testBooleanToDto() throws Exception {
		PrimitiveAttributeInfo attributeInfo = new PrimitiveAttributeInfo();
		attributeInfo.setType(PrimitiveType.BOOLEAN);
		Assert.assertTrue(converter.toDto(Boolean.TRUE, attributeInfo) instanceof BooleanAttribute);
		Assert.assertTrue(((BooleanAttribute) converter.toDto(Boolean.TRUE, attributeInfo)).getValue());
		Assert.assertFalse(((BooleanAttribute) converter.toDto(Boolean.FALSE, attributeInfo)).getValue());
	}

	@Test
	public void testAssociation() throws GeomajasException {
		AssociationAttributeInfo many = new AssociationAttributeInfo();
		many.setType(AssociationType.ONE_TO_MANY);
		many.setEditable(true);
		many.setLabel("manyInMany");
		many.setName("manyInMany");

		PrimitiveAttributeInfo textAttr = new PrimitiveAttributeInfo("text", "text", PrimitiveType.STRING);
		FeatureInfo featureInfo = new FeatureInfo();
		featureInfo.setIdentifier(new PrimitiveAttributeInfo("id", "id", PrimitiveType.LONG));
		featureInfo.setAttributes(Collections.singletonList((AttributeInfo) textAttr));
		many.setFeature(featureInfo);
		Attribute<?> attr = converter.toDto(new Bean[] { new Bean("t1",1L), new Bean("t2",2L) }, many);
		Assert.assertTrue(attr instanceof AssociationAttribute);
		AssociationAttribute assoc = (AssociationAttribute)attr;
		List<AssociationValue> value = (List<AssociationValue>)assoc.getValue();
		value.get(0).getAttributes();
	}

	private class Bean {

		private String text;
		
		private Long id;
		
		public Bean(String text, Long id) {
			this.text = text;
			this.id = id;
		}


		public Long getId() {
			return id;
		}

		
		public void setId(Long id) {
			this.id = id;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

	}
}
