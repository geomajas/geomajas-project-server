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
package org.geomajas.internal.service;

import static junit.framework.Assert.assertTrue;

import junit.framework.Assert;
import org.geomajas.configuration.AttributeInfo;
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
 * TODO: document me !
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
	}
}
