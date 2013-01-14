/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.sld;

import junit.framework.Assert;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.junit.Test;


public class EqualsTest {
	@Test
	public void testRead() throws JiBXException {
		Assert.assertEquals(createStyle("samples/example-sld.xml"), createStyle("samples/example-sld.xml"));
		Assert.assertEquals(createStyle("samples/point_attribute.sld"), createStyle("samples/point_attribute.sld"));
	}

	private StyledLayerDescriptorInfo createStyle(String path) throws JiBXException {
		IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		Object object = uctx.unmarshalDocument(getClass().getResourceAsStream(path), null);
		return (StyledLayerDescriptorInfo) object;
	}

}
