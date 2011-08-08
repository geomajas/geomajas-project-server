package org.geomajas.sld;

import org.geomajas.sld.expression.ExpressionInfo;
import org.geomajas.sld.expression.LiteralTypeInfo;
import org.geomajas.sld.expression.PropertyNameInfo;
import org.geomajas.sld.filter.FilterTypeInfo;
import org.geomajas.sld.filter.PropertyIsEqualToInfo;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.junit.Assert;
import org.junit.Test;

public class FilterTest {

	@Test
	public void testRead() throws JiBXException {
		IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		Object object = uctx.unmarshalDocument(getClass().getResourceAsStream("samples/filter/property_is_equal.xml"), null);
		FilterTypeInfo filter = (FilterTypeInfo) object;
		Assert.assertTrue(filter.ifComparisonOps());
		Assert.assertTrue(filter.getComparisonOps() instanceof PropertyIsEqualToInfo);
		PropertyIsEqualToInfo p = (PropertyIsEqualToInfo) filter.getComparisonOps();
		Assert.assertEquals(2, p.getExpressionList().size());
		ExpressionInfo left = p.getExpressionList().get(0);
		ExpressionInfo right = p.getExpressionList().get(1);
		Assert.assertTrue(left instanceof PropertyNameInfo);
		Assert.assertTrue(right instanceof LiteralTypeInfo);
		PropertyNameInfo prop = (PropertyNameInfo) left;
		LiteralTypeInfo litteral = (LiteralTypeInfo) right;
		Assert.assertEquals("SomeProperty", prop.getValue());
		Assert.assertEquals("100", litteral.getValue());
	}

}
