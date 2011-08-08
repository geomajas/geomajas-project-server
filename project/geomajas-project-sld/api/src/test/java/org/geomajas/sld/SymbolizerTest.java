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
