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
import org.geomajas.testdata.TestPathBinaryStreamAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
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
		"/org/geomajas/plugin/rasterizing/rasterizing-immediate.xml",
		"/org/geomajas/plugin/rasterizing/rasterizing-service.xml", "/org/geomajas/testdata/beanContext.xml",
		"/org/geomajas/testdata/layerBeans.xml", "/org/geomajas/testdata/layerBeansMultiLine.xml",
		"/org/geomajas/testdata/layerBeansMultiPolygon.xml", "/org/geomajas/testdata/layerBeansPoint.xml",
		"/org/geomajas/testdata/layerBeansMixedGeometry.xml" })
@DirtiesContext
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

	private static final double DELTA = 1E-6;

	private static final String IMAGE_CLASS_PATH = "org/geomajas/plugin/rasterizing/images/imageservice/vectortile";

	@Before
	public void login() {
		// assure security context is set
		securityManager.createSecurityContext(null);
	}

	@Test
	public void testMultiLineStyle() throws Exception {
		// width
		getMultiLineStyle().setStrokeWidth(3);
		checkMultiLine("multiline_black_3.png", false, true);
		getMultiLineStyle().setStrokeWidth(1);
		// color
		getMultiLineStyle().setStrokeColor("#FF6347");
		checkMultiLine("multiline_tomato_1.png", false, true);
		getMultiLineStyle().setStrokeColor("#000000");
		// opacity
		getMultiLineStyle().setStrokeOpacity(0.5f);
		checkMultiLine("multiline_black_1_semitransparent.png", false, true);
		getMultiLineStyle().setStrokeOpacity(1f);
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
		checkMultiLine("multiline_black_1_labeled.png", true, true);
		log.info("stop");
		// color
		getMultiLineLabelStyle().getFontStyle().setColor("#DA70D6");
		checkMultiLine("multiline_black_1_labeled_font_orchid.png", true, true);
		getMultiLineLabelStyle().getFontStyle().setColor("#000000");
		// // family
		getMultiLineLabelStyle().getFontStyle().setFamily("Courier New");
		checkMultiLine("multiline_black_1_labeled_font_courier.png", true, true);
		getMultiLineLabelStyle().getFontStyle().setFamily("Verdana");
		// opacity
		getMultiLineLabelStyle().getFontStyle().setOpacity(0.5f);
		checkMultiLine("multiline_black_1_labeled_font_semitransparent.png", true, true);
		getMultiLineLabelStyle().getFontStyle().setOpacity(1f);
		// size
		getMultiLineLabelStyle().getFontStyle().setSize(10);
		checkMultiLine("multiline_black_1_labeled_font_size_10.png", true, true);
		getMultiLineLabelStyle().getFontStyle().setSize(8);
		// italic
		getMultiLineLabelStyle().getFontStyle().setStyle("italic");
		checkMultiLine("multiline_black_1_labeled_font_italic.png", true, true);
		getMultiLineLabelStyle().getFontStyle().setStyle("normal");
		// weight
		getMultiLineLabelStyle().getFontStyle().setWeight("bold");
		checkMultiLine("multiline_black_1_labeled_font_bold.png", true, true);
		getMultiLineLabelStyle().getFontStyle().setWeight("normal");
	}

	@Test
	public void testPointStyle() throws Exception {
		// default
		checkPoint("point_default.png", false, true);
		// save circle state
		CircleInfo tmp = getPointStyle().getSymbol().getCircle();
		getPointStyle().getSymbol().setCircle(null);
		// symbol rect
		getPointStyle().getSymbol().setRect(createRect());
		checkPoint("point_rect.png", false, true);
		getPointStyle().getSymbol().setRect(null);
		// symbol image
		getPointStyle().getSymbol().setImage(createImage(32));
		checkPoint("point_image.png", false, true);
		getPointStyle().getSymbol().setImage(null);
		// symbol image
		getPointStyle().getSymbol().setImage(createImage(64));
		checkPoint("point_image_big.png", false, true);
		getPointStyle().getSymbol().setImage(null);
		// set circle state back
		getPointStyle().getSymbol().setCircle(tmp);
	}

	@Test
	public void testPointLabelStyle() throws Exception {
		// label on/off
		checkPoint("point_black_1_labeled.png", true, true);
		// color
		getPointLabelStyle().getFontStyle().setColor("#DA70D6");
		checkPoint("point_black_1_labeled_font_orchid.png", true, true);
		getPointLabelStyle().getFontStyle().setColor("#000000");
		// // family
		getPointLabelStyle().getFontStyle().setFamily("Courier New");
		checkPoint("point_black_1_labeled_font_courier.png", true, true);
		getPointLabelStyle().getFontStyle().setFamily("Verdana");
		// opacity
		getPointLabelStyle().getFontStyle().setOpacity(0.5f);
		checkPoint("point_black_1_labeled_font_semitransparent.png", true, true);
		getPointLabelStyle().getFontStyle().setOpacity(1f);
		// size
		getPointLabelStyle().getFontStyle().setSize(10);
		checkPoint("point_black_1_labeled_font_size_10.png", true, true);
		getPointLabelStyle().getFontStyle().setSize(8);
		// italic
		getPointLabelStyle().getFontStyle().setStyle("italic");
		checkPoint("point_black_1_labeled_font_italic.png", true, true);
		getPointLabelStyle().getFontStyle().setStyle("normal");
		// weight
		getPointLabelStyle().getFontStyle().setWeight("bold");
		checkPoint("point_black_1_labeled_font_bold.png", true, true);
		getPointLabelStyle().getFontStyle().setWeight("normal");
	}

	@Test
	public void testMultiPolygonStyle() throws Exception {
		// default
		checkMultiPolygon("multipolygon_default.png", false, true);
		// fill color
		getMultiPolygonStyle().setFillColor("#D2691E");
		getMultiPolygonStyle().setStrokeColor("#A52A2A");
		checkMultiPolygon("multipolygon_chocolate_brown_1.png", false, true);
		getMultiPolygonStyle().setFillColor("#FFFFFF");
		getMultiPolygonStyle().setStrokeColor("#000000");
		// fill opacity
		getMultiPolygonStyle().setFillColor("#D2691E");
		getMultiPolygonStyle().setStrokeColor("#A52A2A");
		getMultiPolygonStyle().setFillOpacity(0.5f);
		checkMultiPolygon("multipolygon_chocolate_brown_1_semitransparent.png", false, true);
		getMultiPolygonStyle().setFillColor("#FFFFFF");
		getMultiPolygonStyle().setStrokeColor("#000000");
		getMultiPolygonStyle().setFillOpacity(1f);
	}

	@Test
	public void testMultiPolygonLabelStyle() throws Exception {
		// label on/off
		checkMultiPolygon("multipolygon_black_1_labeled.png", true, true);
		// color
		getMultiPolygonLabelStyle().getFontStyle().setColor("#DA70D6");
		checkMultiPolygon("multipolygon_black_1_labeled_font_orchid.png", true, true);
		getMultiPolygonLabelStyle().getFontStyle().setColor("#000000");
		// family
		getMultiPolygonLabelStyle().getFontStyle().setFamily("Courier New");
		checkMultiPolygon("multipolygon_black_1_labeled_font_courier.png", true, true);
		getMultiPolygonLabelStyle().getFontStyle().setFamily("Verdana");
		// opacity
		getMultiPolygonLabelStyle().getFontStyle().setOpacity(0.5f);
		checkMultiPolygon("multipolygon_black_1_labeled_font_semitransparent.png", true, true);
		getMultiPolygonLabelStyle().getFontStyle().setOpacity(1f);
		// size
		getMultiPolygonLabelStyle().getFontStyle().setSize(10);
		checkMultiPolygon("multipolygon_black_1_labeled_font_size_10.png", true, true);
		getMultiPolygonLabelStyle().getFontStyle().setSize(8);
		// italic
		getMultiPolygonLabelStyle().getFontStyle().setStyle("italic");
		checkMultiPolygon("multipolygon_black_1_labeled_font_italic.png", true, true);
		getMultiPolygonLabelStyle().getFontStyle().setStyle("normal");
		// weight
		getMultiPolygonLabelStyle().getFontStyle().setWeight("bold");
		checkMultiPolygon("multipolygon_black_1_labeled_font_bold.png", true, true);
		getMultiPolygonLabelStyle().getFontStyle().setWeight("normal");
	}

	private FeatureStyleInfo getMultiLineStyle() {
		return layerBeansMultiLineStyleInfo.getFeatureStyles().get(0);
	}

	private LabelStyleInfo getMultiLineLabelStyle() {
		return layerBeansMultiLineStyleInfo.getLabelStyle();
	}

	private FeatureStyleInfo getMultiPolygonStyle() {
		return layerBeansMultiPolygonStyleInfo.getFeatureStyles().get(0);
	}

	private LabelStyleInfo getMultiPolygonLabelStyle() {
		return layerBeansMultiPolygonStyleInfo.getLabelStyle();
	}

	private FeatureStyleInfo getPointStyle() {
		return layerBeansPointStyleInfo.getFeatureStyles().get(0);
	}

	private LabelStyleInfo getPointLabelStyle() {
		return layerBeansPointStyleInfo.getLabelStyle();
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

	private void checkMixedGeometry(String fileName, boolean paintLabels, boolean paintGeometries) throws Exception {
		checkOrRender(fileName, paintLabels, paintGeometries, layerBeansMixedGeometry, layerBeansMixedGeometryStyleInfo);
	}

	private void checkOrRender(String fileName, boolean paintLabels, boolean paintGeometries, VectorLayer layer,
			NamedStyleInfo styleInfo) throws Exception {

		ClientMapInfo mapInfo = new ClientMapInfo();
		MapRasterizingInfo mapRasterizingInfo = new MapRasterizingInfo();
		mapRasterizingInfo.setBounds(new Bbox(-50, -50, 100, 100));
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
		info.setHref("org/geomajas/plugin/rasterizing/images/imageservice/vectortile/point.png");
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
			super(IMAGE_CLASS_PATH);
			this.map = map;
		}

		public void generateActual(OutputStream out) throws Exception {
			imageService.writeMap(out, map);
		}

	}

}
