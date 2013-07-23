package org.geomajas.sld;

import java.io.StringWriter;

import junit.framework.Assert;

import org.geomajas.sld.StyledLayerDescriptorInfo.ChoiceInfo;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.junit.Test;

public class TransformationTest {

	@Test
	public void testBbox() throws JiBXException {
		IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		Object object = uctx.unmarshalDocument(getClass()
				.getResourceAsStream("samples/transformation/pointstacker.xml"), null);
		StyledLayerDescriptorInfo sld = (StyledLayerDescriptorInfo) object;
		Assert.assertEquals(1, sld.getChoiceList().size());
		ChoiceInfo choice = sld.getChoiceList().get(0);
		Assert.assertTrue(choice.ifNamedLayer());
		NamedLayerInfo layer = choice.getNamedLayer();
		Assert.assertEquals(1, layer.getChoiceList().size());
		Assert.assertTrue(layer.getChoiceList().get(0).ifUserStyle());
		UserStyleInfo userStyleInfo = layer.getChoiceList().get(0).getUserStyle();
		TransformationInfo tx = (TransformationInfo) userStyleInfo.getFeatureTypeStyleList().get(0).getTransformation();
		Assert.assertEquals(5, tx.getFunction().getExpressionList().size());
	}

}
