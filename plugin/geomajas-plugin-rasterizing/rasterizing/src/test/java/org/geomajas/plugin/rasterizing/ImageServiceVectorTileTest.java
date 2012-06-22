package org.geomajas.plugin.rasterizing;

import java.io.OutputStream;

import org.geomajas.configuration.CircleInfo;
import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.ImageInfo;
import org.geomajas.configuration.LabelStyleInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.RectInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.layer.VectorLayer;
import org.geomajas.plugin.rasterizing.api.ImageService;
import org.geomajas.plugin.rasterizing.command.dto.MapRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.VectorLayerRasterizingInfo;
import org.geomajas.security.SecurityManager;
import org.geomajas.sld.ExternalGraphicInfo;
import org.geomajas.sld.FeatureTypeStyleInfo;
import org.geomajas.sld.GraphicInfo;
import org.geomajas.sld.LineSymbolizerInfo;
import org.geomajas.sld.MarkInfo;
import org.geomajas.sld.PointSymbolizerInfo;
import org.geomajas.sld.PolygonSymbolizerInfo;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.TextSymbolizerInfo;
import org.geomajas.sld.UserStyleInfo;
import org.geomajas.sld.WellKnownNameInfo;
import org.geomajas.spring.ThreadScopeContextHolder;
import org.geomajas.testdata.TestPathBinaryStreamAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.style.StylerUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests the rasterizing service by binary comparison of images. To generate the images, set the writeImages flag to
 * true and urn the tests.
 * 
 * @author Jan De Moerloose
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/META-INF/geomajasContext.xml", "/org/geomajas/plugin/rasterizing/DefaultCachedAndRasterizedPipelines.xml",
		"/org/geomajas/plugin/rasterizing/rasterizing-service.xml", "/org/geomajas/testdata/beanContext.xml",
		"/org/geomajas/testdata/layerBeans.xml", "/org/geomajas/testdata/layerBeansSynthetic.xml", "/org/geomajas/testdata/layerBeansMultiLine.xml",
		"/org/geomajas/testdata/layerBeansMultiPolygon.xml", "/org/geomajas/testdata/layerBeansPoint.xml",
		"/org/geomajas/testdata/layerBeansMixedGeometry.xml" })
public class ImageServiceVectorTileTest {

	@Autowired
	@Qualifier("layerBeansMultiLine")
	private VectorLayer layerBeansMultiLine;

	@Autowired
	@Qualifier("layerBeansMultiLineStyleInfo")
	private NamedStyleInfo layerBeansMultiLineStyleInfo;

	@Autowired
	@Qualifier("layerBeansMultiLineAssociationStyleInfo")
	private NamedStyleInfo layerBeansMultiLineAssociationStyleInfo;

	@Autowired
	@Qualifier("layerBeansMultiPolygon")
	private VectorLayer layerBeansMultiPolygon;

	@Autowired
	@Qualifier("layerBeansMultiPolygonStyleInfo")
	private NamedStyleInfo layerBeansMultiPolygonStyleInfo;

	@Autowired
	@Qualifier("layerBeansSynthetic")
	private VectorLayer layerBeansSynthetic;
	
	@Autowired
	@Qualifier("layerBeansSyntheticStyleInfo")
	private NamedStyleInfo layerBeansSyntheticStyleInfo;

	@Autowired
	@Qualifier("layerBeansPoint")
	private VectorLayer layerBeansPoint;

	@Autowired
	@Qualifier("layerBeansPointStyleInfo")
	private NamedStyleInfo layerBeansPointStyleInfo;

	@Autowired
	@Qualifier("layerBeansMixedGeometry")
	private VectorLayer layerBeansMixedGeometry;

	@Autowired
	@Qualifier("layerBeansMixedGeometryStyleInfo")
	private NamedStyleInfo layerBeansMixedGeometryStyleInfo;

	@Autowired
	private ImageService imageService;

	@Autowired
	private SecurityManager securityManager;

	// changing this to true and running the test from the base directory will generate the images !
	private boolean writeImages = false;

	private final Logger log = LoggerFactory.getLogger(ImageServiceVectorTileTest.class);

	private static final double DELTA = 0.01;

	@Qualifier("ImageServiceVectorTileTest.path")
	@Autowired
	private String imagePath;

	@Before
	public void login() {
		// assure security context is set
		securityManager.createSecurityContext(null);
	}

	@After
	public void clearSecurityContext() {
		ThreadScopeContextHolder.clear();
	}

	@Test
	public void testMultiLineStyle() throws Exception {
		// width
		getMultiLineStyle().getStroke().setStrokeWidth(3);
		checkMultiLine("multiline_black_3.png", false, true);
		getMultiLineStyle().getStroke().setStrokeWidth(1);
		// color
		getMultiLineStyle().getStroke().setStrokeColor("#FF6347");
		checkMultiLine("multiline_tomato_1.png", false, true);
		getMultiLineStyle().getStroke().setStrokeColor("#000000");
		// opacity
		getMultiLineStyle().getStroke().setStrokeOpacity(0.5f);
		checkMultiLine("multiline_black_1_semitransparent.png", false, true);
		getMultiLineStyle().getStroke().setStrokeOpacity(1f);
		// association
		checkMultiLineAssociation("multiline_association.png", false, true);
		
	}

	@Test
	public void testMixedGeometryStyle() throws Exception {
		checkMixedGeometry("mixed.png", false, true);
	}

	@Test
	public void testMultiLineLabelStyle() throws Exception {
		// label on/off
		log.info("start");
		getMultiLineStyle().getStroke().setStrokeColor("#000000");
		checkMultiLine("multiline_black_1_labeled.png", true, true);
		log.info("stop");
		// color
		getMultiLineLabelStyle().getFill().setFillColor("#DA70D6");
		checkMultiLine("multiline_black_1_labeled_font_orchid.png", true, true);
		getMultiLineLabelStyle().getFill().setFillColor("#000000");
		// // family
		getMultiLineLabelStyle().getFont().setFamily("Courier New");
		checkMultiLine("multiline_black_1_labeled_font_courier.png", true, true);
		getMultiLineLabelStyle().getFont().setFamily("Verdana");
		// opacity
		getMultiLineLabelStyle().getFill().setFillOpacity(0.5f);
		checkMultiLine("multiline_black_1_labeled_font_semitransparent.png", true, true);
		getMultiLineLabelStyle().getFill().setFillOpacity(1f);
		// size
		getMultiLineLabelStyle().getFont().setSize(10);
		checkMultiLine("multiline_black_1_labeled_font_size_10.png", true, true);
		getMultiLineLabelStyle().getFont().setSize(8);
		// italic
		getMultiLineLabelStyle().getFont().setStyle("italic");
		checkMultiLine("multiline_black_1_labeled_font_italic.png", true, true);
		getMultiLineLabelStyle().getFont().setStyle("normal");
		// weight
		getMultiLineLabelStyle().getFont().setWeight("bold");
		checkMultiLine("multiline_black_1_labeled_font_bold.png", true, true);
		getMultiLineLabelStyle().getFont().setWeight("normal");
	}

	@Test
	public void testPointStyle() throws Exception {
		// default
		checkPoint("point_default.png", false, true);
		// save circle state
		GraphicInfo.ChoiceInfo choice = getPointStyle().getGraphic().getChoiceList().get(0);
		MarkInfo circle = choice.getMark();
		getPointStyle().getGraphic().getChoiceList().clear();
		// symbol rect
		MarkInfo mark = StyleUtil.createMark(StyleUtil.WKN_SQUARE,
				StyleUtil.createFill(circle.getFill().getCssParameterList()),
				StyleUtil.createStroke(circle.getStroke().getCssParameterList()));
		getPointStyle().setGraphic(StyleUtil.createGraphic(mark, 20));
		checkPoint("point_rect.png", false, true);
		// symbol image
		ExternalGraphicInfo point = StyleUtil.createExternalGraphic("/"+imagePath+"/point.png");
		getPointStyle().setGraphic(StyleUtil.createGraphic(point, 32));
		checkPoint("point_image.png", false, true);
		// symbol image
		getPointStyle().setGraphic(StyleUtil.createGraphic(point, 64));
		checkPoint("point_image_big.png", false, true);
		// set circle state back
		getPointStyle().setGraphic(StyleUtil.createGraphic(circle, 20));
	}

	@Test
	public void testPointLabelStyle() throws Exception {
		// label on/off
		checkPoint("point_black_1_labeled.png", true, true);
		// color
		getPointLabelStyle().getFill().setFillColor("#DA70D6");
		checkPoint("point_black_1_labeled_font_orchid.png", true, true);
		getPointLabelStyle().getFill().setFillColor("#000000");
		// // family
		getPointLabelStyle().getFont().setFamily("Courier New");
		checkPoint("point_black_1_labeled_font_courier.png", true, true);
		getPointLabelStyle().getFont().setFamily("Verdana");
		// opacity
		getPointLabelStyle().getFill().setFillOpacity(0.5f);
		checkPoint("point_black_1_labeled_font_semitransparent.png", true, true);
		getPointLabelStyle().getFill().setFillOpacity(1f);
		// size
		getPointLabelStyle().getFont().setSize(10);
		checkPoint("point_black_1_labeled_font_size_10.png", true, true);
		getPointLabelStyle().getFont().setSize(8);
		// italic
		getPointLabelStyle().getFont().setStyle("italic");
		checkPoint("point_black_1_labeled_font_italic.png", true, true);
		getPointLabelStyle().getFont().setStyle("normal");
		// weight
		getPointLabelStyle().getFont().setWeight("bold");
		checkPoint("point_black_1_labeled_font_bold.png", true, true);
		getPointLabelStyle().getFont().setWeight("normal");
		// point just outside tile border
		checkPoint("point_black_1_border.png", true, true, new Bbox(0.001, 0.001, 100, 100));
	}

	@Test
	public void testMultiPolygonStyle() throws Exception {
		// default
		checkMultiPolygon("multipolygon_default.png", false, true);
		// fill color
		getMultiPolygonStyle().getFill().setFillColor("#D2691E");
		getMultiPolygonStyle().getStroke().setStrokeColor("#A52A2A");
		checkMultiPolygon("multipolygon_chocolate_brown_1.png", false, true);
		getMultiPolygonStyle().getFill().setFillColor("#FFFFFF");
		getMultiPolygonStyle().getStroke().setStrokeColor("#000000");
		// fill opacity
		getMultiPolygonStyle().getFill().setFillColor("#D2691E");
		getMultiPolygonStyle().getStroke().setStrokeColor("#A52A2A");
		getMultiPolygonStyle().getFill().setFillOpacity(0.5f);
		checkMultiPolygon("multipolygon_chocolate_brown_1_semitransparent.png", false, true);
		getMultiPolygonStyle().getFill().setFillColor("#FFFFFF");
		getMultiPolygonStyle().getStroke().setStrokeColor("#000000");
		getMultiPolygonStyle().getFill().setFillOpacity(1f);
	}

	@Test
	public void testMultiPolygonLabelStyle() throws Exception {
		// label on/off
		checkMultiPolygon("multipolygon_black_1_labeled.png", true, true);
		// color
		getMultiPolygonLabelStyle().getFill().setFillColor("#DA70D6");
		checkMultiPolygon("multipolygon_black_1_labeled_font_orchid.png", true, true);
		getMultiPolygonLabelStyle().getFill().setFillColor("#000000");
		// family
		getMultiPolygonLabelStyle().getFont().setFamily("Courier New");
		checkMultiPolygon("multipolygon_black_1_labeled_font_courier.png", true, true);
		getMultiPolygonLabelStyle().getFont().setFamily("Verdana");
		// opacity
		getMultiPolygonLabelStyle().getFill().setFillOpacity(0.5f);
		checkMultiPolygon("multipolygon_black_1_labeled_font_semitransparent.png", true, true);
		getMultiPolygonLabelStyle().getFill().setFillOpacity(1f);
		// size
		getMultiPolygonLabelStyle().getFont().setSize(10);
		checkMultiPolygon("multipolygon_black_1_labeled_font_size_10.png", true, true);
		getMultiPolygonLabelStyle().getFont().setSize(8);
		// italic
		getMultiPolygonLabelStyle().getFont().setStyle("italic");
		checkMultiPolygon("multipolygon_black_1_labeled_font_italic.png", true, true);
		getMultiPolygonLabelStyle().getFont().setStyle("normal");
		// weight
		getMultiPolygonLabelStyle().getFont().setWeight("bold");
		checkMultiPolygon("multipolygon_black_1_labeled_font_bold.png", true, true);
		getMultiPolygonLabelStyle().getFont().setWeight("normal");
		// synthetic
		checkSynthetic("synthetic_black_1_labeled.png", true, true);
	}
	
	private RuleInfo getFirstRule(UserStyleInfo userStyle){
		FeatureTypeStyleInfo fts = userStyle.getFeatureTypeStyleList().get(0);
		return fts.getRuleList().get(0);
	}

	private LineSymbolizerInfo getMultiLineStyle() {
		RuleInfo rule = getFirstRule(layerBeansMultiLineStyleInfo.getUserStyle());
		return (LineSymbolizerInfo) rule.getSymbolizerList().get(0);
	}

	private TextSymbolizerInfo getMultiLineLabelStyle() {
		RuleInfo rule = getFirstRule(layerBeansMultiLineStyleInfo.getUserStyle());
		return (TextSymbolizerInfo) rule.getSymbolizerList().get(1);
	}

	private PolygonSymbolizerInfo getMultiPolygonStyle() {
		RuleInfo rule = getFirstRule(layerBeansMultiPolygonStyleInfo.getUserStyle());
		return (PolygonSymbolizerInfo) rule.getSymbolizerList().get(0);
	}

	private PolygonSymbolizerInfo getSyntheticStyle() {
		RuleInfo rule = getFirstRule(layerBeansSyntheticStyleInfo.getUserStyle());
		return (PolygonSymbolizerInfo) rule.getSymbolizerList().get(0);
	}

	private TextSymbolizerInfo getMultiPolygonLabelStyle() {
		RuleInfo rule = getFirstRule(layerBeansMultiPolygonStyleInfo.getUserStyle());
		return (TextSymbolizerInfo) rule.getSymbolizerList().get(1);
	}

	private PointSymbolizerInfo getPointStyle() {
		RuleInfo rule = getFirstRule(layerBeansPointStyleInfo.getUserStyle());
		return (PointSymbolizerInfo) rule.getSymbolizerList().get(0);
	}

	private TextSymbolizerInfo getPointLabelStyle() {
		RuleInfo rule = getFirstRule(layerBeansPointStyleInfo.getUserStyle());
		return (TextSymbolizerInfo) rule.getSymbolizerList().get(1);
	}
	
	private void checkPoint(String fileName, boolean paintLabels, boolean paintGeometries, Bbox box) throws Exception {
		checkOrRender(fileName, paintLabels, paintGeometries, layerBeansPoint, layerBeansPointStyleInfo, box);
	}

	private void checkPoint(String fileName, boolean paintLabels, boolean paintGeometries) throws Exception {
		checkOrRender(fileName, paintLabels, paintGeometries, layerBeansPoint, layerBeansPointStyleInfo);
	}

	private void checkMultiLine(String fileName, boolean paintLabels, boolean paintGeometries) throws Exception {
		checkOrRender(fileName, paintLabels, paintGeometries, layerBeansMultiLine, layerBeansMultiLineStyleInfo);
	}

	private void checkMultiLineAssociation(String fileName, boolean paintLabels, boolean paintGeometries) throws Exception {
		checkOrRender(fileName, paintLabels, paintGeometries, layerBeansMultiLine, layerBeansMultiLineAssociationStyleInfo);
	}

	private void checkMultiPolygon(String fileName, boolean paintLabels, boolean paintGeometries) throws Exception {
		checkOrRender(fileName, paintLabels, paintGeometries, layerBeansMultiPolygon, layerBeansMultiPolygonStyleInfo);
	}

	private void checkSynthetic(String fileName, boolean paintLabels, boolean paintGeometries) throws Exception {
		checkOrRender(fileName, paintLabels, paintGeometries, layerBeansSynthetic, layerBeansSyntheticStyleInfo);
	}

	private void checkMixedGeometry(String fileName, boolean paintLabels, boolean paintGeometries) throws Exception {
		checkOrRender(fileName, paintLabels, paintGeometries, layerBeansMixedGeometry, layerBeansMixedGeometryStyleInfo);
	}
	
	private void checkOrRender(String fileName, boolean paintLabels, boolean paintGeometries, VectorLayer layer,
			NamedStyleInfo styleInfo) throws Exception {
		checkOrRender(fileName, paintLabels, paintGeometries, layer, styleInfo, new Bbox(-50, -50, 100, 100));
	}

	private void checkOrRender(String fileName, boolean paintLabels, boolean paintGeometries, VectorLayer layer,
			NamedStyleInfo styleInfo, Bbox box) throws Exception {

		ClientMapInfo mapInfo = new ClientMapInfo();
		MapRasterizingInfo mapRasterizingInfo = new MapRasterizingInfo();
		mapRasterizingInfo.setBounds(box);
		mapInfo.setCrs("EPSG:4326");
		mapRasterizingInfo.setScale(1);
		mapInfo.getWidgetInfo().put(MapRasterizingInfo.WIDGET_KEY, mapRasterizingInfo);
		ClientVectorLayerInfo clientVectorLayerInfo = new ClientVectorLayerInfo();
		clientVectorLayerInfo.setVisible(true);
		clientVectorLayerInfo.setServerLayerId(layer.getId());
		clientVectorLayerInfo.setNamedStyleInfo(styleInfo);
		VectorLayerRasterizingInfo vectorLayerRasterizingInfo = new VectorLayerRasterizingInfo();
		vectorLayerRasterizingInfo.setPaintGeometries(true);
		vectorLayerRasterizingInfo.setPaintLabels(true);
		vectorLayerRasterizingInfo.setStyle(styleInfo);
		clientVectorLayerInfo.getWidgetInfo().put(VectorLayerRasterizingInfo.WIDGET_KEY, vectorLayerRasterizingInfo);
		mapInfo.getLayers().add(clientVectorLayerInfo);
		new MapAssert(mapInfo).assertEqualImage(fileName, writeImages, DELTA);
	}

	private ImageInfo createImage(int height) {
		ImageInfo info = new ImageInfo();
		info.setHeight(height);
		// width is not important
		info.setWidth(height);
		info.setHref(imagePath+"/point.png");
		return info;
	}

	private RectInfo createRect() {
		RectInfo info = new RectInfo();
		info.setH(10);
		info.setW(20);
		return info;
	}

	class MapAssert extends TestPathBinaryStreamAssert {

		private ClientMapInfo map;

		public MapAssert(ClientMapInfo map) {
			super(imagePath);
			this.map = map;
		}

		public void generateActual(OutputStream out) throws Exception {
			imageService.writeMap(out, map);
		}

	}

}
