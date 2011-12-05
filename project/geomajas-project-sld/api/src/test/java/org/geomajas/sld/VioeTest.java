package org.geomajas.sld;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.junit.Test;


public class VioeTest {

	@Test
	public void testRead() throws JiBXException {
		IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		Object o1 = uctx.unmarshalDocument(getClass().getResourceAsStream("samples/vioe/vioe_dibe_gehelen.sld"), null);
		Object o2 = uctx.unmarshalDocument(getClass().getResourceAsStream("samples/vioe/vioe_dibe_orgels_zonder_filter.sld"), null);
		Object o3 = uctx.unmarshalDocument(getClass().getResourceAsStream("samples/vioe/vioe_dibe_relicten_met_filter.sld"), null);
		Object o4 = uctx.unmarshalDocument(getClass().getResourceAsStream("samples/vioe/vioe_dibe_relicten_zonder_filter.sld"), null);
		Object o5 = uctx.unmarshalDocument(getClass().getResourceAsStream("samples/vioe/vioe_woi_relict_zonder_filter.sld"), null);
	}

}
