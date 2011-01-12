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
package org.geomajas.internal.service;

import junit.framework.Assert;

import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.PrimitiveType;
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
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/spring/moreContext.xml"})
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
		Assert.assertTrue(((BooleanAttribute)converter.toDto(Boolean.TRUE, attributeInfo)).getValue());
		Assert.assertFalse(((BooleanAttribute)converter.toDto(Boolean.FALSE, attributeInfo)).getValue());
	}
}
