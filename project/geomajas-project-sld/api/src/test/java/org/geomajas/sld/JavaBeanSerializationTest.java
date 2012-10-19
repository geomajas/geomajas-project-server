/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld;

import java.io.IOException;

import org.geomajas.sld.filter.FilterTypeInfo;
import org.geomajas.sld.filter.PropertyIsLikeTypeInfo;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.javabean.JavaBeanConverter;

/**
 * Tests Spring compliance by using another javabeans based serializer (xstream).
 * 
 * @author Jan De Moerloose
 * 
 */
public class JavaBeanSerializationTest {

	@Test
	public void testSerialization() throws IOException, ClassNotFoundException {
		FilterTypeInfo type = new FilterTypeInfo();
		type.setComparisonOps(new PropertyIsLikeTypeInfo());
		XStream stream = new XStream();
		// register javabean converter with highest priority
		stream.registerConverter(new JavaBeanConverter(stream.getMapper(), "class"), Integer.MAX_VALUE);
		// this was failing when featureIdList was initialized with empty list !!!
		stream.fromXML(stream.toXML(type));
	}
}
