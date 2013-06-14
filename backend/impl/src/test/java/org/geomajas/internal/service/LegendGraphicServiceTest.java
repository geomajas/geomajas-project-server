package org.geomajas.internal.service;

import java.io.File;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.geomajas.layer.RasterLayer;
import org.geomajas.service.LegendGraphicService;
import org.geomajas.service.legend.LegendGraphicMetadata;
import org.geomajas.sld.FeatureTypeStyleInfo;
import org.geomajas.sld.NamedLayerInfo;
import org.geomajas.sld.NamedStyleInfo;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.StyledLayerDescriptorInfo;
import org.geomajas.sld.UserStyleInfo;
import org.geomajas.testdata.TestPathRenderedImageAssert;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sun.media.jai.util.ImageUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/internal/service/legend/rasterLayerContext.xml" })
public class LegendGraphicServiceTest {

	@Autowired
	private LegendGraphicService service;

	private TestPathRenderedImageAssert imageAssert = new TestPathRenderedImageAssert(
			"org/geomajas/internal/service/legend");

	@Autowired
	private RasterLayer blueMarble;

	private boolean writeImages = true;

	@Test
	public void testSimpleRules18() throws Exception {
		LegendGraphicMetadata p = new SimpleRulesData("point:1", 18, 18);
		imageAssert.assertEquals("point1-18x18.png", service.getLegendGraphic(p), 0.01, writeImages);
		LegendGraphicMetadata l = new SimpleRulesData("line:1", 18, 18);
		imageAssert.assertEquals("line1-18x18.png", service.getLegendGraphic(l), 0.01, writeImages);
		LegendGraphicMetadata po = new SimpleRulesData("polygon:1", 18, 18);
		imageAssert.assertEquals("polygon1-18x18.png", service.getLegendGraphic(po), 0.01, writeImages);
		LegendGraphicMetadata po2 = new SimpleRulesData("polygon:2", 18, 18);
		imageAssert.assertEquals("polygon2-18x18.png", service.getLegendGraphic(po2), 0.01, writeImages);
		LegendGraphicMetadata po3 = new SimpleRulesData("polygon:3", 18, 18);
		imageAssert.assertEquals("polygon3-18x18.png", service.getLegendGraphic(po3), 0.01, writeImages);
		LegendGraphicMetadata po4 = new SimpleRulesData("polygon:4", 18, 18);
		imageAssert.assertEquals("polygon4-18x18.png", service.getLegendGraphic(po4), 0.01, writeImages);
		
		imageAssert.assertEquals("combined-18x18.png", 
				service.getLegendGrapics(Arrays.asList(p, l, po, po2, po3, po4)), 0.01, writeImages);
	}

	@Test
	public void testSimpleRules19() throws Exception {
		LegendGraphicMetadata p = new SimpleRulesData("point:1", 19, 19);
		imageAssert.assertEquals("point1-19x19.png", service.getLegendGraphic(p), 0.01, writeImages);
		LegendGraphicMetadata l = new SimpleRulesData("line:1", 19, 19);
		imageAssert.assertEquals("line1-19x19.png", service.getLegendGraphic(l), 0.01, writeImages);
		LegendGraphicMetadata po = new SimpleRulesData("polygon:1", 19, 19);
		imageAssert.assertEquals("polygon1-19x19.png", service.getLegendGraphic(po), 0.01, writeImages);
		LegendGraphicMetadata po2 = new SimpleRulesData("polygon:2", 19, 19);
		imageAssert.assertEquals("polygon2-19x19.png", service.getLegendGraphic(po2), 0.01, writeImages);
		LegendGraphicMetadata po3 = new SimpleRulesData("polygon:3", 19, 19);
		imageAssert.assertEquals("polygon3-19x19.png", service.getLegendGraphic(po3), 0.01, writeImages);
		LegendGraphicMetadata po4 = new SimpleRulesData("polygon:4", 19, 19);
		imageAssert.assertEquals("polygon4-19x19.png", service.getLegendGraphic(po4), 0.01, writeImages);

		imageAssert.assertEquals("combined-19x19.png", 
				service.getLegendGrapics(Arrays.asList(p, l, po, po2, po3, po4)), 0.01, writeImages);
	}

	@Test
	public void testRasterLayer() throws Exception {
		LegendGraphicMetadata rasterMetadata = new RasterData(blueMarble.getId());
		imageAssert.assertEquals("layer-raster.png", service.getLegendGraphic(rasterMetadata), 0.01, writeImages);
	}

	public class SimpleRulesData implements LegendGraphicMetadata {

		private int width;

		private int height;

		private RuleInfo ruleInfo;

		public SimpleRulesData(String ruleName, int width, int height) throws JiBXException {
			this.width = width;
			this.height = height;
			IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
			IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
			Object object = uctx.unmarshalDocument(
					getClass().getResourceAsStream("/org/geomajas/testdata/sld/simple_rules.sld"), null);
			StyledLayerDescriptorInfo sld = (StyledLayerDescriptorInfo) object;
			NamedLayerInfo namedLayerInfo = sld.getChoiceList().get(0).getNamedLayer();
			UserStyleInfo userStyleInfo = namedLayerInfo.getChoiceList().get(0).getUserStyle();
			FeatureTypeStyleInfo featureTypeStyleInfo = userStyleInfo.getFeatureTypeStyleList().get(0);
			for (RuleInfo rule : featureTypeStyleInfo.getRuleList()) {
				if (ruleName.equals(rule.getName())) {
					ruleInfo = rule;
				}
			}
		}

		public String getLayerId() {
			return null;
		}

		public UserStyleInfo getUserStyle() {
			return null;
		}

		public NamedStyleInfo getNamedStyle() {
			return null;
		}

		public RuleInfo getRule() {
			return ruleInfo;
		}

		public double getScale() {
			return 0;
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}

	}

	public class RasterData implements LegendGraphicMetadata {

		private String layerId;

		public RasterData(String layerId) {
			this.layerId = layerId;
		}

		public String getLayerId() {
			return layerId;
		}

		public UserStyleInfo getUserStyle() {
			return null;
		}

		public NamedStyleInfo getNamedStyle() {
			return null;
		}

		public RuleInfo getRule() {
			return null;
		}

		public double getScale() {
			return 0;
		}

		public int getWidth() {
			return 0;
		}

		public int getHeight() {
			return 0;
		}

	}

}
