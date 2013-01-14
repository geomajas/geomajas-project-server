/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt.client.util;

import java.io.StringWriter;

import junit.framework.Assert;

import org.geomajas.sld.FillInfo;
import org.geomajas.sld.PolygonSymbolizerInfo;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.StrokeInfo;
import org.geomajas.sld.StyledLayerDescriptorInfo;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.JiBXException;
import org.junit.Test;

public class StyleUtilTest {

	@Test
	public void testSimpleStyle() throws JiBXException {
		FillInfo fillInfo = StyleUtil.createFill("#FFFF00", 0.5f);
		StrokeInfo strokeInfo = StyleUtil.createStroke("red", 3, 0.5f, "3 4");
		PolygonSymbolizerInfo polygonSymbolizerInfo = StyleUtil.createPolygonSymbolizer(fillInfo, strokeInfo);
		RuleInfo ruleInfo = StyleUtil.createRule("myTitle", "myName", polygonSymbolizerInfo);
		IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
		IMarshallingContext mctx = bfact.createMarshallingContext();
		StringWriter sw = new StringWriter();
		mctx.setOutput(sw);
		mctx.marshalDocument(ruleInfo);
		Assert.assertEquals(
				"<sld:Rule xmlns:sld=\"http://www.opengis.net/sld\">" +
				"<sld:Name>myName</sld:Name>" +
				"<sld:Title>myTitle</sld:Title>" +
				"<sld:PolygonSymbolizer>" +
				"<sld:Fill>" +
				"<sld:CssParameter name=\"fill\">#FFFF00</sld:CssParameter>" +
				"<sld:CssParameter name=\"fill-opacity\">0.5</sld:CssParameter>" +
				"</sld:Fill>" +
				"<sld:Stroke>" +
				"<sld:CssParameter name=\"stroke\">red</sld:CssParameter>" +
				"<sld:CssParameter name=\"stroke-width\">3</sld:CssParameter>" +
				"<sld:CssParameter name=\"stroke-dasharray\">3 4</sld:CssParameter>" +
				"<sld:CssParameter name=\"stroke-opacity\">0.5</sld:CssParameter>" +
				"</sld:Stroke>" +
				"</sld:PolygonSymbolizer>" +
				"</sld:Rule>",
				sw.toString());
	}
}
