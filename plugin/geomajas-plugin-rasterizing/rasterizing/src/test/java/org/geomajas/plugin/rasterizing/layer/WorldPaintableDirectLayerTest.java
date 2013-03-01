package org.geomajas.plugin.rasterizing.layer;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.io.StringReader;

import javax.imageio.ImageIO;

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.plugin.rasterizing.command.dto.ClientWorldPaintableLayerInfo;
import org.geomajas.plugin.rasterizing.command.dto.MapRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.WorldEllipseInfo;
import org.geomajas.plugin.rasterizing.command.dto.WorldGeometryInfo;
import org.geomajas.plugin.rasterizing.command.dto.WorldImageInfo;
import org.geomajas.plugin.rasterizing.command.dto.WorldPaintableInfo;
import org.geomajas.plugin.rasterizing.command.dto.WorldRectangleInfo;
import org.geomajas.plugin.rasterizing.command.dto.WorldTextInfo;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.geomajas.sld.StyledLayerDescriptorInfo;
import org.geomajas.sld.SymbolizerTypeInfo;
import org.geomajas.sld.TextSymbolizerInfo;
import org.geomajas.testdata.TestPathBinaryStreamAssert;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geometry.jts.WKTReader2;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.DirectLayer;
import org.geotools.map.MapContext;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/rasterizing/rasterizing-service.xml" })
public class WorldPaintableDirectLayerTest {

	@Autowired
	private WorldPaintableLayerFactory layerFactory;

	@Autowired
	private DtoConverterService converterService;

	@Autowired
	private GeoService geoService;

	// changing this to true and running the test from the base directory will generate the images !
	private boolean writeImages = false;

	private static final double DELTA = 1E-6;
	
	@Qualifier("WorldPaintableDirectLayerTest.path")
	@Autowired
	private String imagePath;
	
	private static int size = 400;

	@Test
	public void testOneOfEach() throws Exception {
		ClientWorldPaintableLayerInfo wp = new ClientWorldPaintableLayerInfo();
		wp.getPaintables().add(createPoint());
		wp.getPaintables().add(createLine());
		wp.getPaintables().add(createPolygon());
		wp.getPaintables().add(createText());
		wp.getPaintables().add(createImage());
		wp.getPaintables().add(createEllipse());
		wp.getPaintables().add(createRectangle());

		ClientMapInfo mapInfo = new ClientMapInfo();
		mapInfo.setCrs("EPSG:4326");
		MapRasterizingInfo mapRasterizingInfo = new MapRasterizingInfo();
		mapRasterizingInfo.setBounds(new Bbox(0, 0, size, size));
		mapRasterizingInfo.setScale(1);
		mapRasterizingInfo.setTransparent(true);
		mapInfo.getWidgetInfo().put(MapRasterizingInfo.WIDGET_KEY, mapRasterizingInfo);

		DefaultMapContext mapContext = new DefaultMapContext();
		mapContext.setCoordinateReferenceSystem(geoService.getCrs2("EPSG:4326"));
		mapContext.getViewport().setBounds(
				new ReferencedEnvelope(0, size, 0, size, mapContext.getCoordinateReferenceSystem()));
		mapContext.getViewport().setCoordinateReferenceSystem(mapContext.getCoordinateReferenceSystem());
		mapContext.getViewport().setScreenArea(new Rectangle(0, 0, size, size));
		WorldPaintableDirectLayer layer = (WorldPaintableDirectLayer) layerFactory.createLayer(mapContext, wp);
		BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR);
		layer.draw(image.createGraphics(), mapContext, mapContext.getViewport());
		new DirectLayerAssert(layer, mapContext).assertEqualImage("worldpaintable-mix.png", writeImages, DELTA);
		mapContext.dispose();
	}

	private WorldPaintableInfo createRectangle() throws Exception{
		WorldRectangleInfo rect = new WorldRectangleInfo();
		rect.setBbox(new Bbox(50, 60, 30, 60));
		rect.setGeometrySymbolizerInfo(toStyle("" +
				"          <PolygonSymbolizer>\n" + 
				"            <Fill>\n" + 
				"              <CssParameter name=\"fill\">#4030FF</CssParameter>\n" + 
				"            </Fill>\n" + 
				"            <Stroke>\n" + 
				"              <CssParameter name=\"stroke\">#000000</CssParameter>\n" + 
				"              <CssParameter name=\"stroke-width\">4</CssParameter>    \n" + 
				"            </Stroke>\n" + 
				"          </PolygonSymbolizer>\n" 
				));
		rect.setLabel("rectangle");
		rect.setLabelSymbolizerInfo((TextSymbolizerInfo)toStyle("" +
				"          <TextSymbolizer>\n" + 
				"            <Font />\n" + 
				"            <Fill>\n" + 
				"              <CssParameter name=\"fill\">#00FF40</CssParameter>\n" + 
				"            </Fill>\n" + 
				"          </TextSymbolizer>"
				));
		return rect;
	}

	private WorldPaintableInfo createEllipse() throws Exception {
		WorldEllipseInfo ellipse = new WorldEllipseInfo();
		ellipse.setBbox(new Bbox(150, 60, 50, 100));
		ellipse.setGeometrySymbolizerInfo(toStyle("" +
				"          <PolygonSymbolizer>\n" + 
				"            <Fill>\n" + 
				"              <CssParameter name=\"fill\">#F830FF</CssParameter>\n" + 
				"            </Fill>\n" + 
				"            <Stroke>\n" + 
				"              <CssParameter name=\"stroke\">#FF00FF</CssParameter>\n" + 
				"              <CssParameter name=\"stroke-width\">3</CssParameter>    \n" + 
				"            </Stroke>\n" + 
				"          </PolygonSymbolizer>\n" 
				));
		ellipse.setLabel("ellipse");
		ellipse.setLabelSymbolizerInfo((TextSymbolizerInfo)toStyle("" +
				"          <TextSymbolizer>\n" + 
				"            <Font />\n" + 
				"            <Fill>\n" + 
				"              <CssParameter name=\"fill\">#00FF40</CssParameter>\n" + 
				"            </Fill>\n" + 
				"          </TextSymbolizer>"
				));
		return ellipse;
	}

	private WorldPaintableInfo createImage()  throws Exception{
		WorldImageInfo image = new WorldImageInfo();
		image.setBbox(new Bbox(10, 20, 100, 100));
		image.setUrl("org/geomajas/plugin/rasterizing/layer-raster.png");
		image.setGeometrySymbolizerInfo(toStyle("" +
				"          <PolygonSymbolizer>\n" + 
				"          </PolygonSymbolizer>\n" 
				));
		return image;
	}

	private WorldPaintableInfo createText()  throws Exception {
		WorldTextInfo t = new WorldTextInfo();
		t.setAnchor(new Coordinate(40, 50));
		t.setLabel("text");
		t.setLabelSymbolizerInfo((TextSymbolizerInfo)toStyle("" +
				"          <TextSymbolizer>\n" + 
				"            <Font />\n" + 
				"            <Fill>\n" + 
				"              <CssParameter name=\"fill\">#000000</CssParameter>\n" + 
				"            </Fill>\n" + 
				"          </TextSymbolizer>"
				));
		return t;
	}

	private WorldPaintableInfo createPolygon()  throws Exception {
		WKTReader2 reader = new WKTReader2();
		WorldGeometryInfo p = new WorldGeometryInfo();
		p.setGeometry(converterService.toDto(reader.read("POLYGON ((160 310, 180 360, 140 340, 160 310))")));
		p.setGeometrySymbolizerInfo(toStyle("" +
				"          <PolygonSymbolizer>\n" + 
				"            <Fill>\n" + 
				"              <CssParameter name=\"fill\">#40FF40</CssParameter>\n" + 
				"            </Fill>\n" + 
				"            <Stroke>\n" + 
				"              <CssParameter name=\"stroke\">#000000</CssParameter>\n" + 
				"              <CssParameter name=\"stroke-width\">2</CssParameter>    \n" + 
				"            </Stroke>\n" + 
				"          </PolygonSymbolizer>\n" 
				));
		p.setLabel("polygon");
		p.setLabelSymbolizerInfo((TextSymbolizerInfo)toStyle("" +
				"          <TextSymbolizer>\n" + 
				"            <Font />\n" + 
				"            <Fill>\n" + 
				"              <CssParameter name=\"fill\">#40FF40</CssParameter>\n" + 
				"            </Fill>\n" + 
				"          </TextSymbolizer>"
				));
		return p;
	}

	private WorldPaintableInfo createLine() throws Exception {
		WKTReader2 reader = new WKTReader2();
		WorldGeometryInfo p = new WorldGeometryInfo();
		p.setGeometry(converterService.toDto(reader.read("LINESTRING (200 10, 280 260)")));
		p.setGeometrySymbolizerInfo(toStyle("" +
				"          <LineSymbolizer>\n" + 
				"            <Stroke>\n" + 
				"              <CssParameter name=\"stroke\">#364440</CssParameter>\n" + 
				"              <CssParameter name=\"stroke-width\">3</CssParameter>    \n" + 
				"            </Stroke>\n" + 
				"          </LineSymbolizer>\n" 
				));
		p.setLabel("line");
		p.setLabelSymbolizerInfo((TextSymbolizerInfo)toStyle("" +
				"          <TextSymbolizer>\n" + 
				"            <Font />\n" + 
				"            <Fill>\n" + 
				"              <CssParameter name=\"fill\">#000000</CssParameter>\n" + 
				"            </Fill>\n" + 
				"          </TextSymbolizer>"
				));
		return p;
	}

	private WorldPaintableInfo createPoint() throws Exception {
		WKTReader2 reader = new WKTReader2();
		WorldGeometryInfo p = new WorldGeometryInfo();
		p.setGeometry(converterService.toDto(reader.read("POINT(300 100)")));
		p.setGeometrySymbolizerInfo(toStyle("" +
				"          <PointSymbolizer>\n" + 
				"            <Graphic>\n" + 
				"              <Mark>\n" + 
				"                <WellKnownName>circle</WellKnownName>\n" + 
				"                <Fill>\n" + 
				"                  <CssParameter name=\"fill\">#0033CC</CssParameter>\n" + 
				"                </Fill>\n" + 
				"              </Mark>\n" + 
				"              <Size>20</Size>\n" + 
				"            </Graphic>\n" + 
				"          </PointSymbolizer>\n"
				));
		p.setLabel("point");
		p.setLabelSymbolizerInfo((TextSymbolizerInfo)toStyle("" +
				"          <TextSymbolizer>\n" + 
				"            <Font />\n" + 
				"            <Fill>\n" + 
				"              <CssParameter name=\"fill\">#000000</CssParameter>\n" + 
				"            </Fill>\n" + 
				"          </TextSymbolizer>"
				));
		return p;
	}
	
	private SymbolizerTypeInfo toStyle(String sld) throws Exception {
		String style = "<StyledLayerDescriptor version=\"1.0.0\"\n" + 
				"    xsi:schemaLocation=\"http://www.opengis.net/sld StyledLayerDescriptor.xsd\" \n" + 
				"    xmlns=\"http://www.opengis.net/sld\" \n" + 
				"    xmlns:ogc=\"http://www.opengis.net/ogc\" \n" + 
				"    xmlns:xlink=\"http://www.w3.org/1999/xlink\" \n" + 
				"    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
				"  <NamedLayer>\n" + 
				"    <Name>test</Name>\n" + 
				"    <UserStyle>\n" + 
				"      <Title>test</Title>\n" + 
				"      <FeatureTypeStyle>\n" + 
				"        <Rule>\n" + 
				""+sld+"       </Rule>\n" + 
						"      </FeatureTypeStyle>\n" + 
						"    </UserStyle>\n" + 
						"  </NamedLayer>\n" + 
						"</StyledLayerDescriptor>";
		IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		Object object = uctx.unmarshalDocument(new StringReader(style), null);
		return ((StyledLayerDescriptorInfo) object).getChoiceList().get(0).getNamedLayer().getChoiceList().get(0)
				.getUserStyle().getFeatureTypeStyleList().get(0).getRuleList().get(0).getSymbolizerList().get(0);
	}

	class DirectLayerAssert extends TestPathBinaryStreamAssert {

		private DirectLayer layer;

		private MapContext mapContext;

		public DirectLayerAssert(DirectLayer layer, MapContext mapContext) {
			super(imagePath);
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
