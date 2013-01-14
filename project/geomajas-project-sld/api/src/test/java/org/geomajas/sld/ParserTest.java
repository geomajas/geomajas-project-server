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

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Assert;

import org.geomajas.sld.StyledLayerDescriptorInfo.ChoiceInfo;
import org.geomajas.sld.filter.FilterTypeInfo;
import org.geotools.data.DataUtilities;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayerDescriptor;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.xml.sax.SAXException;

public class ParserTest {

	@Test
	public void testRead() throws JiBXException {
		IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		Object object = uctx.unmarshalDocument(getClass().getResourceAsStream("samples/example-sld.xml"), null);
		StyledLayerDescriptorInfo sld = (StyledLayerDescriptorInfo) object;
		for (ChoiceInfo choice : sld.getChoiceList()) {
			if (choice.ifNamedLayer()) {
				NamedLayerInfo layer = choice.getNamedLayer();
				Assert.assertEquals("OCEANSEA_1M:Foundation", layer.getName());
			}
		}
		IMarshallingContext ctx = bfact.createMarshallingContext();
		StringWriter sw = new StringWriter();
		ctx.setOutput(sw);
		ctx.marshalDocument(sld);
	}

	@Test
	public void testCrosses() throws JiBXException {
		IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		Object object = uctx.unmarshalDocument(getClass().getResourceAsStream("samples/polygon_crosses.xml"), null);
		StyledLayerDescriptorInfo sld = (StyledLayerDescriptorInfo) object;
		for (ChoiceInfo choice : sld.getChoiceList()) {
			if (choice.ifNamedLayer()) {
				NamedLayerInfo layer = choice.getNamedLayer();
				for (org.geomajas.sld.NamedLayerInfo.ChoiceInfo choice2 : layer.getChoiceList()) {
					if (choice2.ifUserStyle()) {
						UserStyleInfo userStyle = choice2.getUserStyle();
						FeatureTypeStyleInfo style = userStyle.getFeatureTypeStyleList().get(0);
						RuleInfo rule = style.getRuleList().get(0);
						FilterTypeInfo filter = rule.getChoice().getFilter();
					}
				}
			}
		}
	}

	@Test
	public void testDtoToGeotools() throws JiBXException, IOException, SAXException, ParserConfigurationException,
			InterruptedException {
		// read dto
		IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		StyledLayerDescriptorInfo sld = (StyledLayerDescriptorInfo) uctx.unmarshalDocument(getClass()
				.getResourceAsStream("samples/example-sld.xml"), null);

		// pipe to geotools parser
		StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);
		PipedOutputStream pos = new PipedOutputStream();
		PipedInputStream pii = new PipedInputStream(pos);

		final SLDParser parser = new SLDParser(factory, pii);

		IMarshallingContext mctx = bfact.createMarshallingContext();
		mctx.setOutput(new PrintWriter(pos));

		CountDownLatch countDown = new CountDownLatch(2);
		ArrayList<Style> styles = new ArrayList<Style>();
		new Thread(new Parser(countDown, parser, styles)).start();
		new Thread(new Marshaller(countDown, mctx, sld)).start();
		countDown.await();

		Assert.assertEquals("GEOSYM", styles.get(0).getName());
	}
	
	@Test
	public void testXmlParser() throws IOException, SAXException, ParserConfigurationException, SchemaException {
		org.geotools.xml.Configuration configuration = new org.geotools.sld.SLDConfiguration();
	    org.geotools.xml.Parser parser = new org.geotools.xml.Parser(configuration);
	    
	    
	    // parse
	    StyledLayerDescriptor sld = (StyledLayerDescriptor) parser.parse(getClass()
				.getResourceAsStream("samples/point_attribute.sld"));
	    
	    NamedLayer layer = (NamedLayer)sld.getStyledLayers()[0];
	    Filter filter = layer.getStyles()[0].featureTypeStyles().get(0).rules().get(0).getFilter();
	    SimpleFeatureType t = DataUtilities.createType("MyType",
                        "name:String," + // <- a String attribute
                        "pop:Integer" // a number attribute
        );
	    SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(t);
	    SimpleFeature sf = featureBuilder.buildFeature("1", new Object[]{"Test", 50000});


	    filter.evaluate(sf);
	    //sld.layers().get(0)

	}

	@Test
	public void testTwoFeatureStyles() {
		StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);
		SLDParser parser = new SLDParser(factory, getClass().getResourceAsStream("samples/two-feature-styles-sld.xml"));
		Style[] style = parser.readXML();
		Assert.assertEquals(2, style[0].featureTypeStyles().size());
	}

	public class Marshaller implements Runnable {

		private CountDownLatch countDown;

		private IMarshallingContext marshallingContext;

		private StyledLayerDescriptorInfo sld;

		public Marshaller(CountDownLatch countDown, IMarshallingContext marshallingContext,
				StyledLayerDescriptorInfo sld) {
			this.countDown = countDown;
			this.marshallingContext = marshallingContext;
			this.sld = sld;
		}

		public void run() {
			try {
				marshallingContext.marshalDocument(sld);
				countDown.countDown();
			} catch (JiBXException e) {
				e.printStackTrace();
			}
		}

	}

	public class Parser implements Runnable {

		private CountDownLatch countDown;

		private SLDParser parser;

		private ArrayList<Style> styles;

		public Parser(CountDownLatch countDown, SLDParser parser, ArrayList<Style> styles) {
			this.countDown = countDown;
			this.parser = parser;
			this.styles = styles;
		}

		public void run() {
			Style[] stylesArray = parser.readXML();
			for (Style style : stylesArray) {
				styles.add(style);
			}
			countDown.countDown();
		}

	}

}
