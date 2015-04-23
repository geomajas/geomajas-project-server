package org.geomajas.plugin.rasterizing.layer;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.plugin.rasterizing.command.dto.ClientSvgLayerInfo;
import org.geomajas.plugin.rasterizing.command.dto.MapRasterizingInfo;
import org.geomajas.service.GeoService;
import org.geomajas.testdata.TestPathBinaryStreamAssert;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.DirectLayer;
import org.geotools.map.MapContent;
import org.geotools.map.MapContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml" })
public class SvgDirectLayerTest {

	@Autowired
	private SvgLayerFactory layerFactory;

	@Autowired
	private GeoService geoService;

	private static final String IMAGE_CLASS_PATH = "org/geomajas/plugin/rasterizing/images/svglayer";

	// changing this to true and running the test from the base directory will
	// generate the images !
	private boolean writeImages = false;

	private static final double DELTA = 1E-6;

	@Test
	public void testPolygon() throws Exception {
		ClientSvgLayerInfo geo = new ClientSvgLayerInfo();
		geo.setSvgContent("<svg>\n"
				+ "  <path d=\"M10 10 L90 10 L90 90 L10 90 Z\" style=\"fill:lime;stroke:purple;stroke-width:3px\"/>\n"
				+ "</svg>");
		geo.setViewBoxWorldBounds(new Bbox(0, 0, 10, 10));
		geo.setViewBoxScreenBounds(new Bbox(0,0,100,100));

		ClientMapInfo mapInfo = new ClientMapInfo();
		mapInfo.setCrs("EPSG:4326");
		MapRasterizingInfo mapRasterizingInfo = new MapRasterizingInfo();
		mapRasterizingInfo.setBounds(new Bbox(0, 0, 100, 100));
		mapRasterizingInfo.setScale(1);
		mapRasterizingInfo.setTransparent(true);
		mapInfo.getWidgetInfo().put(MapRasterizingInfo.WIDGET_KEY,
				mapRasterizingInfo);

		MapContext mapContent = new DefaultMapContext();
		mapContent.getViewport().setBounds(
				new ReferencedEnvelope(0, 100, 0, 100, geoService
						.getCrs2(mapInfo.getCrs())));
		mapContent.getViewport().setScreenArea(new Rectangle(0, 0, 100, 100));
		AffineTransform tf = mapContent.getViewport().getWorldToScreen();
		SvgDirectLayer layer = (SvgDirectLayer) layerFactory.createLayer(
				mapContent, geo);
		new DirectLayerAssert(layer, mapContent).assertEqualImage(
				"polygon2.png", writeImages, DELTA);
		mapContent.dispose();
	}

	class DirectLayerAssert extends TestPathBinaryStreamAssert {

		private DirectLayer layer;

		private MapContent mapContent;

		public DirectLayerAssert(DirectLayer layer, MapContent mapContent) {
			super(IMAGE_CLASS_PATH);
			this.layer = layer;
			this.mapContent = mapContent;
		}

		public void generateActual(OutputStream out) throws Exception {
			Rectangle rect = mapContent.getViewport().getScreenArea();
			BufferedImage image = new BufferedImage((int) rect.getWidth(),
					(int) rect.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
			layer.draw(image.createGraphics(), mapContent,
					mapContent.getViewport());
			ImageIO.write(image, "PNG", out);
		}

	}

}
