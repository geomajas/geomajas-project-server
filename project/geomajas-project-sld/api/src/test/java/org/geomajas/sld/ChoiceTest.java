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

import java.io.StringWriter;
import java.util.Arrays;

import junit.framework.Assert;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.JiBXException;
import org.junit.Test;


public class ChoiceTest {

	@Test
	public void testClearChoiceListSelect() throws JiBXException {
		GraphicInfo graphicInfo = new GraphicInfo();
		graphicInfo.setChoiceList(Arrays.asList(new GraphicInfo.ChoiceInfo()));
		graphicInfo.getChoiceList().get(0).setExternalGraphic(new ExternalGraphicInfo());
		graphicInfo.getChoiceList().get(0).getExternalGraphic().setOnlineResource(new OnlineResourceInfo());
		graphicInfo.getChoiceList().get(0).getExternalGraphic().setFormat(new FormatInfo());
		graphicInfo.getChoiceList().get(0).getExternalGraphic().getFormat().setFormat("image/png");
		String noClear = getAsXml(graphicInfo);
		
		graphicInfo = new GraphicInfo();
		graphicInfo.setChoiceList(Arrays.asList(new GraphicInfo.ChoiceInfo()));
		graphicInfo.getChoiceList().get(0).setMark(new MarkInfo());
		graphicInfo.getChoiceList().get(0).clearChoiceListSelect();
		graphicInfo.getChoiceList().get(0).setExternalGraphic(new ExternalGraphicInfo());
		graphicInfo.getChoiceList().get(0).getExternalGraphic().setOnlineResource(new OnlineResourceInfo());
		graphicInfo.getChoiceList().get(0).getExternalGraphic().setFormat(new FormatInfo());
		graphicInfo.getChoiceList().get(0).getExternalGraphic().getFormat().setFormat("image/png");
		String withClear = getAsXml(graphicInfo);
		
		Assert.assertEquals(withClear, noClear);
	}
	
	String getAsXml(Object o) throws JiBXException {
		IBindingFactory bfact = BindingDirectory.getFactory(o.getClass());
		IMarshallingContext ctx = bfact.createMarshallingContext();
		StringWriter sw = new StringWriter();
		ctx.setOutput(sw);
		ctx.marshalDocument(o);
		return sw.toString();
		
	}
}
