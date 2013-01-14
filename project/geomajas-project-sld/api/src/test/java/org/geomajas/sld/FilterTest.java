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
	public void testBbox() throws JiBXException {
		IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		Object object = uctx.unmarshalDocument(getClass().getResourceAsStream("samples/filter/bbox.xml"), null);
		FilterTypeInfo filter = (FilterTypeInfo) object;
	}
	
	@Test
	public void testBetween() throws JiBXException {
		IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		Object object = uctx.unmarshalDocument(getClass().getResourceAsStream("samples/filter/between.xml"), null);
		FilterTypeInfo filter = (FilterTypeInfo) object;
	}
	@Test
	public void testDateBetween() throws JiBXException {
		IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		Object object = uctx.unmarshalDocument(getClass().getResourceAsStream("samples/filter/date_between.xml"), null);
		FilterTypeInfo filter = (FilterTypeInfo) object;
	}

	@Test
	public void testFeatureId() throws JiBXException {
		IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		Object object = uctx.unmarshalDocument(getClass().getResourceAsStream("samples/filter/feature_id.xml"), null);
		FilterTypeInfo filter = (FilterTypeInfo) object;
	}

	@Test
	public void testFunction() throws JiBXException {
		IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		Object object = uctx.unmarshalDocument(getClass().getResourceAsStream("samples/filter/function.xml"), null);
		FilterTypeInfo filter = (FilterTypeInfo) object;
	}
	
	@Test
	public void testLogicAnd() throws JiBXException {
		IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		Object object = uctx.unmarshalDocument(getClass().getResourceAsStream("samples/filter/logic_and.xml"), null);
		FilterTypeInfo filter = (FilterTypeInfo) object;
	}

	@Test
	public void testMathAnd() throws JiBXException {
		IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		Object object = uctx.unmarshalDocument(getClass().getResourceAsStream("samples/filter/math_and.xml"), null);
		FilterTypeInfo filter = (FilterTypeInfo) object;
	}

	
	@Test
	public void testMultiFeatureId() throws JiBXException {
		IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		Object object = uctx.unmarshalDocument(getClass().getResourceAsStream("samples/filter/multi_feature_id.xml"), null);
		FilterTypeInfo filter = (FilterTypeInfo) object;
	}

	@Test
	public void testNotDisjoint() throws JiBXException {
		IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		Object object = uctx.unmarshalDocument(getClass().getResourceAsStream("samples/filter/not_disjoint.xml"), null);
		FilterTypeInfo filter = (FilterTypeInfo) object;
	}

	@Test
	public void testOverlaps() throws JiBXException {
		IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		Object object = uctx.unmarshalDocument(getClass().getResourceAsStream("samples/filter/overlaps.xml"), null);
		FilterTypeInfo filter = (FilterTypeInfo) object;
	}

	@Test
	public void testIsEqual() throws JiBXException {
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
	
	@Test
	public void testIsLessThan() throws JiBXException {
		IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		Object object = uctx.unmarshalDocument(getClass().getResourceAsStream("samples/filter/property_is_less_than.xml"), null);
		FilterTypeInfo filter = (FilterTypeInfo) object;
	}
	@Test
	public void testIsLike() throws JiBXException {
		IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		Object object = uctx.unmarshalDocument(getClass().getResourceAsStream("samples/filter/property_is_like.xml"), null);
		FilterTypeInfo filter = (FilterTypeInfo) object;
	}
	
	
	
}
