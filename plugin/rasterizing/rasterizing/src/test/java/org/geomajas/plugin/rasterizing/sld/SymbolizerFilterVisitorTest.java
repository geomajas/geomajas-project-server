package org.geomajas.plugin.rasterizing.sld;

import java.io.IOException;
import java.util.Iterator;

import junit.framework.Assert;

import org.geomajas.plugin.rasterizing.command.dto.VectorLayerRasterizingInfo;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.junit.Test;
import org.opengis.style.RasterSymbolizer;

public class SymbolizerFilterVisitorTest {

	private StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);

	@Test
	public void testLabels() throws IOException {
		SymbolizerFilterVisitor visitor = new SymbolizerFilterVisitor();
		visitor.setIncludeGeometry(false);
		visitor.setIncludeText(true);
		SLDParser parser = new SLDParser(styleFactory);
		parser.setInput(getClass().getResource("point_pointwithdefaultlabel.sld"));
		Style[] styles = parser.readXML();
		Assert.assertEquals(1, styles.length);
		visitor.visit(styles[0]);
		Style copy = (Style) visitor.getCopy();
		FeatureTypeStyle featureTypeStyle = copy.featureTypeStyles().iterator().next();
		Rule rule = featureTypeStyle.rules().iterator().next();
		Iterator<Symbolizer> it = rule.symbolizers().iterator();
		Assert.assertTrue(it.next() instanceof TextSymbolizer);
		Assert.assertFalse(it.hasNext());
	}

	@Test
	public void testGeometries() throws IOException{
		SymbolizerFilterVisitor visitor = new SymbolizerFilterVisitor();
		visitor.setIncludeGeometry(true);
		visitor.setIncludeText(false);
		SLDParser parser = new SLDParser(styleFactory);
		parser.setInput(getClass().getResource("point_pointwithdefaultlabel.sld"));
		Style[] styles = parser.readXML();
		Assert.assertEquals(1, styles.length);
		visitor.visit(styles[0]);
		Style copy = (Style) visitor.getCopy();
		FeatureTypeStyle featureTypeStyle = copy.featureTypeStyles().iterator().next();
		Rule rule = featureTypeStyle.rules().iterator().next();
		Iterator<Symbolizer> it = rule.symbolizers().iterator();
		Assert.assertTrue(it.next() instanceof PointSymbolizer);
		Assert.assertFalse(it.hasNext());
	}

	@Test
	public void testLabelsAndGeometries() throws IOException{
		SymbolizerFilterVisitor visitor = new SymbolizerFilterVisitor();
		visitor.setIncludeGeometry(true);
		visitor.setIncludeText(true);
		SLDParser parser = new SLDParser(styleFactory);
		parser.setInput(getClass().getResource("point_pointwithdefaultlabel.sld"));
		Style[] styles = parser.readXML();
		Assert.assertEquals(1, styles.length);
		visitor.visit(styles[0]);
		Style copy = (Style) visitor.getCopy();
		FeatureTypeStyle featureTypeStyle = copy.featureTypeStyles().iterator().next();
		Rule rule = featureTypeStyle.rules().iterator().next();
		Iterator<Symbolizer> it = rule.symbolizers().iterator();
		Assert.assertTrue(it.next() instanceof PointSymbolizer);
		Assert.assertTrue(it.next() instanceof TextSymbolizer);
		Assert.assertFalse(it.hasNext());
	}
	
	@Test
	public void testTransformation() throws IOException{
		SymbolizerFilterVisitor visitor = new SymbolizerFilterVisitor();
		visitor.setIncludeGeometry(true);
		visitor.setIncludeText(true);
		SLDParser parser = new SLDParser(styleFactory);
		parser.setInput(getClass().getResource("heatmap.sld"));
		Style[] styles = parser.readXML();
		Assert.assertEquals(1, styles.length);
		visitor.visit(styles[0]);
		Style copy = (Style) visitor.getCopy();
		FeatureTypeStyle featureTypeStyle = copy.featureTypeStyles().iterator().next();
		Assert.assertNotNull(featureTypeStyle.getTransformation());		
		Rule rule = featureTypeStyle.rules().iterator().next();
		Iterator<Symbolizer> it = rule.symbolizers().iterator();
		Assert.assertTrue(it.next() instanceof RasterSymbolizer);
		Assert.assertFalse(it.hasNext());
	}
}
