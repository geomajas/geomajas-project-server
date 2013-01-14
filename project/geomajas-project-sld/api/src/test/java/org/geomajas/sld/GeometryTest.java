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

import org.geomajas.sld.geometry.LineStringTypeInfo;
import org.geomajas.sld.geometry.MultiLineStringInfo;
import org.geomajas.sld.geometry.MultiPointInfo;
import org.geomajas.sld.geometry.MultiPolygonInfo;
import org.geomajas.sld.geometry.PointTypeInfo;
import org.geomajas.sld.geometry.PolygonTypeInfo;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.junit.Test;

public class GeometryTest {

	@Test
	public void testPoint() throws JiBXException {
		IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		Object object = uctx.unmarshalDocument(getClass().getResourceAsStream("samples/geometry/point.xml"), null);
		PointTypeInfo point = (PointTypeInfo) object;
	}

	@Test
	public void testLinestring() throws JiBXException {
		IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		Object object = uctx.unmarshalDocument(getClass().getResourceAsStream("samples/geometry/linestring.xml"), null);
		LineStringTypeInfo linestring = (LineStringTypeInfo) object;
	}

	@Test
	public void testPolygon() throws JiBXException {
		IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		Object object = uctx.unmarshalDocument(getClass().getResourceAsStream("samples/geometry/polygon.xml"), null);
		PolygonTypeInfo polygon = (PolygonTypeInfo) object;
	}
	
	@Test
	public void testMultiPoint() throws JiBXException {
		IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		Object object = uctx.unmarshalDocument(getClass().getResourceAsStream("samples/geometry/multipoint.xml"), null);
		MultiPointInfo mpoint = (MultiPointInfo) object;
	}

	@Test
	public void testMultiLinestring() throws JiBXException {
		IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		Object object = uctx.unmarshalDocument(getClass().getResourceAsStream("samples/geometry/multilinestring.xml"), null);
		MultiLineStringInfo mlinestring = (MultiLineStringInfo) object;
	}

	@Test
	public void testMultiPolygon() throws JiBXException {
		IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		Object object = uctx.unmarshalDocument(getClass().getResourceAsStream("samples/geometry/multipolygon.xml"), null);
		MultiPolygonInfo mpolygon = (MultiPolygonInfo) object;
	}
}
