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

import org.geomajas.sld.OverlapBehaviorInfo.OverlapBehaviorInfoInner;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.junit.Test;

public class SymbolizerTest {

	@Test
	public void testOverlap() throws JiBXException {
		IBindingFactory bfact = BindingDirectory.getFactory(RasterSymbolizerInfo.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		Object object = uctx.unmarshalDocument(getClass()
				.getResourceAsStream("samples/symbolizer/rastersymbolizer.xml"), null);
		RasterSymbolizerInfo rasterSymbolizerInfo = (RasterSymbolizerInfo) object;
		Assert.assertEquals(OverlapBehaviorInfoInner.AVERAGE, rasterSymbolizerInfo.getOverlapBehavior()
				.getOverlapBehavior());
	}
}
