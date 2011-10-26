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
