package org.geomajas.plugin.rasterizing;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.geomajas.command.dto.GetVectorTileRequest;
import org.geomajas.configuration.CircleInfo;
import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.ImageInfo;
import org.geomajas.configuration.LabelStyleInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.RectInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.layer.tile.TileMetadata;
import org.geomajas.security.SecurityManager;
import org.geotools.factory.Hints;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests the rasterizing service by binary comparison of images. To generate the images, set the writeImages flag to true and urn the tests.
 * 
 * @author Jan De Moerloose
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/rasterizing/rasterizing.xml", "/org/geomajas/testdata/beanContext.xml",
		"/org/geomajas/testdata/layerBeans.xml", "/org/geomajas/testdata/layerBeansMultiLine.xml",
		"/org/geomajas/testdata/layerBeansMultiPolygon.xml", "/org/geomajas/testdata/layerBeansPoint.xml" })
public class GeotoolsRasterizingServiceTest {

	@Autowired
	@Qualifier("layerBeansMultiLine")
	private VectorLayer layerBeansMultiLine;

	@Autowired
	@Qualifier("layerBeansMultiLineStyleInfo")
	private NamedStyleInfo layerBeansMultiLineStyleInfo;

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
	private VectorLayerService vectorLayerService;

	@Autowired
	private SecurityManager securityManager;

	@Autowired
	GeotoolsRasterizingService geotoolsRasterizingService;

	private boolean writeImages = false;

	private static final String IMAGE_PATH = "src/test/resources/org/geomajas/plugin/rasterizing/images/";

	private final Logger log = LoggerFactory.getLogger(GeotoolsRasterizingServiceTest.class);

	@Before
	public void login() {
		// assure security context is set
		securityManager.createSecurityContext(null);
	}

	@Test
	public void testRenderingHints() throws GeomajasException, IOException {
		checkMultiLine("multiline_black_1.png", false, true);
		Hints hints = (Hints) geotoolsRasterizingService.getRenderingHints().clone();
		geotoolsRasterizingService.getRenderingHints().clear();
		checkMultiLine("multiline_black_1_no_antialias.png", false, true);
		geotoolsRasterizingService.getRenderingHints().add(hints);
	}

	@Test
	public void testMultiLineStyle() throws GeomajasException, IOException {
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
	}

	// @Test
	public void testMultiLineLabelStyle() throws GeomajasException, IOException {
		// label on/off
		log.info("start");
		checkMultiLine("multiline_black_1_labeled.png", true, true);
		log.info("stop");
		// color
		getMultiLineLabelStyle().getFontStyle().setColor("#DA70D6");
		checkMultiLine("multiline_black_1_labeled_font_orchid.png", true, true);
		getMultiLineLabelStyle().getFontStyle().setColor("#000000");
		// family
		getMultiLineLabelStyle().getFontStyle().setFamily("courier");
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
	public void testPointStyle() throws GeomajasException, IOException {
		// default
		checkPoint("point_default.png", false, true);
		// symbol rect
		CircleInfo circle = getPointStyle().getSymbol().getCircle();
		getPointStyle().getSymbol().setRect(createRect());
		checkPoint("point_rect.png", false, true);
		getPointStyle().getSymbol().setRect(null);
		getPointStyle().getSymbol().setCircle(circle);
		// symbol image
		getPointStyle().getSymbol().setCircle(null);
		getPointStyle().getSymbol().setImage(createImage());
		checkPoint("point_image.png", false, true);
		getPointStyle().getSymbol().setImage(null);
		getPointStyle().getSymbol().setCircle(circle);
	}

	// @Test
	public void testPointLabelStyle() throws GeomajasException, IOException {
		// label on/off
		checkPoint("point_black_1_labeled.png", true, true);
		// color
		getPointLabelStyle().getFontStyle().setColor("#DA70D6");
		checkPoint("point_black_1_labeled_font_orchid.png", true, true);
		getPointLabelStyle().getFontStyle().setColor("#000000");
		// family
		getPointLabelStyle().getFontStyle().setFamily("courier");
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
	public void testMultiPolygonStyle() throws GeomajasException, IOException {
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

	// @Test
	public void testMultiPolygonLabelStyle() throws GeomajasException, IOException {
		// label on/off
		checkMultiPolygon("multipolygon_black_1_labeled.png", true, true);
		// color
		getMultiPolygonLabelStyle().getFontStyle().setColor("#DA70D6");
		checkMultiPolygon("multipolygon_black_1_labeled_font_orchid.png", true, true);
		getMultiPolygonLabelStyle().getFontStyle().setColor("#000000");
		// family
		getMultiPolygonLabelStyle().getFontStyle().setFamily("courier");
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

	public static void main(String[] args) throws GeomajasException, IOException {
		GeotoolsRasterizingServiceTest test = new GeotoolsRasterizingServiceTest();
		test.writeImages = true;
		test.testMultiLineLabelStyle();
		test.testMultiLineStyle();
		test.testMultiPolygonLabelStyle();
		test.testMultiPolygonStyle();
		test.testPointLabelStyle();
		test.testPointStyle();
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

	private void checkPoint(String fileName, boolean paintLabels, boolean paintGeometries)
			throws FileNotFoundException, GeomajasException, IOException {
		checkOrRender(fileName, paintLabels, paintGeometries, layerBeansPoint, layerBeansPointStyleInfo);
	}

	private void checkMultiLine(String fileName, boolean paintLabels, boolean paintGeometries)
			throws FileNotFoundException, GeomajasException, IOException {
		checkOrRender(fileName, paintLabels, paintGeometries, layerBeansMultiLine, layerBeansMultiLineStyleInfo);
	}

	private void checkMultiPolygon(String fileName, boolean paintLabels, boolean paintGeometries)
			throws FileNotFoundException, GeomajasException, IOException {
		checkOrRender(fileName, paintLabels, paintGeometries, layerBeansMultiPolygon, layerBeansMultiPolygonStyleInfo);
	}

	private void checkOrRender(String fileName, boolean paintLabels, boolean paintGeometries, VectorLayer layer,
			NamedStyleInfo styleInfo) throws FileNotFoundException, GeomajasException, IOException {
		GetVectorTileRequest metadata = new GetVectorTileRequest();
		metadata.setCode(new TileCode(0, 0, 0));
		metadata.setCrs("EPSG:4326");
		metadata.setLayerId(layer.getId());
		metadata.setPanOrigin(new Coordinate(0, 0));
		metadata.setScale(1);
		metadata.setRenderer(TileMetadata.PARAM_SVG_RENDERER);
		metadata.setStyleInfo(styleInfo);
		metadata.setPaintLabels(paintLabels);
		metadata.setPaintGeometries(paintGeometries);
		InternalTile tile = vectorLayerService.getTile(metadata);
		File file = new File(IMAGE_PATH + fileName);
		if (writeImages) {
			FileOutputStream fos;
			fos = new FileOutputStream(file);
			geotoolsRasterizingService.rasterize(fos, layer, styleInfo, metadata, tile);
			fos.flush();
			fos.close();
		} else {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			geotoolsRasterizingService.rasterize(baos, layer, styleInfo, metadata, tile);
			baos.flush();
			baos.close();
			FileInputStream fis = new FileInputStream(file);
			byte[] expecteds = new byte[(int) file.length()];
			fis.read(expecteds);
			fis.close();
			log.info(expecteds.length+":"+baos.toByteArray().length);
			Assert.assertArrayEquals(expecteds, baos.toByteArray());
		}
	}

	private ImageInfo createImage() {
		ImageInfo info = new ImageInfo();
		info.setHeight(32);
		info.setWidth(46);
		info.setHref("org/geomajas/plugin/rasterizing/point.png");
		return info;
	}

	private RectInfo createRect() {
		RectInfo info = new RectInfo();
		info.setH(10);
		info.setW(20);
		return info;
	}

}
