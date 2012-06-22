package org.geomajas.plugin.rasterizing.layer;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Geometry;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.LayerType;
import org.geomajas.plugin.rasterizing.command.dto.ClientGeometryLayerInfo;
import org.geomajas.plugin.rasterizing.command.dto.MapRasterizingInfo;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.geomajas.service.StyleConverterService;
import org.geomajas.sld.UserStyleInfo;
import org.geomajas.testdata.TestPathBinaryStreamAssert;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geometry.jts.WKTReader2;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.DirectLayer;
import org.geotools.map.MapContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.io.ParseException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml" })
public class GeometryDirectLayerTest {

	@Autowired
	private GeometryLayerFactory layerFactory;

	@Autowired
	private DtoConverterService converterService;

	@Autowired
	private GeoService geoService;

	@Autowired
	private StyleConverterService styleConverterService;
	// changing this to true and running the test from the base directory will generate the images !
	private boolean writeImages = true;

	private static final double DELTA = 1E-6;

	private static final String IMAGE_CLASS_PATH = "org/geomajas/plugin/rasterizing/images/geometrylayer";

	@Test
	public void testPolygon() throws Exception {
		ClientGeometryLayerInfo geo = new ClientGeometryLayerInfo();
		geo.getGeometries().add(createPolygon());
		geo.setStyle(createPolygonStyle());
		geo.setLayerType(LayerType.POLYGON);

		ClientMapInfo mapInfo = new ClientMapInfo();
		mapInfo.setCrs("EPSG:4326");
		MapRasterizingInfo mapRasterizingInfo = new MapRasterizingInfo();
		mapRasterizingInfo.setBounds(new Bbox(0, 0, 100, 100));
		mapRasterizingInfo.setScale(1);
		mapRasterizingInfo.setTransparent(true);
		mapInfo.getWidgetInfo().put(MapRasterizingInfo.WIDGET_KEY, mapRasterizingInfo);

		DefaultMapContext mapContext = new DefaultMapContext();
		mapContext.setCoordinateReferenceSystem(geoService.getCrs2("EPSG:4326"));
		mapContext.getViewport().setBounds(
				new ReferencedEnvelope(0, 100, 0, 100, mapContext.getCoordinateReferenceSystem()));
		mapContext.getViewport().setCoordinateReferenceSystem(mapContext.getCoordinateReferenceSystem());
		mapContext.getViewport().setScreenArea(new Rectangle(0, 0, 100, 100));
		GeometryDirectLayer layer = (GeometryDirectLayer) layerFactory.createLayer(mapContext, geo);
		BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR);
		layer.draw(image.createGraphics(), mapContext, mapContext.getViewport());
		new DirectLayerAssert(layer, mapContext).assertEqualImage("polygon.png", writeImages, DELTA);
		mapContext.dispose();
	}

	private UserStyleInfo createPolygonStyle() throws LayerException {
		FeatureStyleInfo style = new FeatureStyleInfo();
		style.setFillColor("#D2691E");
		style.setFillOpacity(0.5f);
		style.setStrokeColor("#D2691E");
		style.setStrokeOpacity(1f);
		style.setStrokeWidth(3);
		style.setLayerType(LayerType.POLYGON);
		NamedStyleInfo ns = new NamedStyleInfo();
		ns.getFeatureStyles().add(style);
		ns.applyDefaults();
		return styleConverterService.convert(ns, GeometryDirectLayer.DEFAULT_GEOMETRY_NAME);
	}

	private Geometry createPolygon() throws GeomajasException, ParseException {
		WKTReader2 reader = new WKTReader2();
		return converterService.toDto(reader.read("POLYGON((10 10,90 10,90 90,10 90,10 10 ))"));
	}

	class DirectLayerAssert extends TestPathBinaryStreamAssert {

		private DirectLayer layer;

		private MapContext mapContext;

		public DirectLayerAssert(DirectLayer layer, MapContext mapContext) {
			super(IMAGE_CLASS_PATH);
			this.layer = layer;
			this.mapContext = mapContext;
		}

		public void generateActual(OutputStream out) throws Exception {
			Rectangle rect = mapContext.getViewport().getScreenArea();
			BufferedImage image = new BufferedImage((int) rect.getWidth(), (int) rect.getHeight(),
					BufferedImage.TYPE_4BYTE_ABGR);
			layer.draw(image.createGraphics(), mapContext, mapContext.getViewport());
			ImageIO.write(image, "PNG", out);
		}

	}

}
