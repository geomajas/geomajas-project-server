package org.geomajas.plugin.rasterizing;

import java.io.OutputStream;

import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.layer.LayerType;
import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.VectorLayer;
import org.geomajas.plugin.rasterizing.api.ImageService;
import org.geomajas.plugin.rasterizing.command.dto.ClientGeometryLayerInfo;
import org.geomajas.plugin.rasterizing.command.dto.MapRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.RasterLayerRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.VectorLayerRasterizingInfo;
import org.geomajas.plugin.rasterizing.layer.GeometryDirectLayer;
import org.geomajas.security.SecurityManager;
import org.geomajas.service.StyleConverterService;
import org.geomajas.spring.ThreadScopeContextHolder;
import org.geomajas.testdata.TestPathBinaryStreamAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/rasterizing/rasterizing-service.xml", "/org/geomajas/testdata/beanContext.xml",
		"/org/geomajas/testdata/layerBeans.xml", "/org/geomajas/testdata/layerBeansMultiLine.xml",
		"/org/geomajas/testdata/layerBeansMultiPolygon.xml", "/org/geomajas/testdata/layerBeansPoint.xml",
		"/org/geomajas/testdata/layerBluemarble.xml" })
public class ImageServiceMapTest {

	@Autowired
	@Qualifier("layerBeansPointStyleInfo")
	private NamedStyleInfo layerBeansPointStyleInfo;

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
	@Qualifier("bluemarble")
	private RasterLayer layerBluemarble;

	@Autowired
	private ImageService imageService;

	@Autowired
	private SecurityManager securityManager;

	@Autowired
	private StyleConverterService styleConverterService;

	@Qualifier("ImageServiceMapTest.path")
	@Autowired
	private String imagePath;

	private static final double DELTA = 0.07;

	// changing this to true and running the test from the base directory will generate the images !
	private boolean writeImages = true;

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
	public void testOneVectorLayer() throws Exception {
		ClientMapInfo mapInfo = new ClientMapInfo();
		MapRasterizingInfo mapRasterizingInfo = new MapRasterizingInfo();
		mapRasterizingInfo.setBounds(new Bbox(-80, -50, 100, 100));
		mapInfo.setCrs("EPSG:4326");
		mapRasterizingInfo.setScale(1);
		mapRasterizingInfo.setTransparent(true);
		mapInfo.getWidgetInfo().put(MapRasterizingInfo.WIDGET_KEY, mapRasterizingInfo);
		ClientVectorLayerInfo clientBeansPointLayerInfo = new ClientVectorLayerInfo();
		clientBeansPointLayerInfo.setServerLayerId(layerBeansPoint.getId());
		VectorLayerRasterizingInfo layerRasterizingInfo = new VectorLayerRasterizingInfo();
		layerRasterizingInfo.setStyle(layerBeansPointStyleInfo);
		clientBeansPointLayerInfo.getWidgetInfo().put(VectorLayerRasterizingInfo.WIDGET_KEY, layerRasterizingInfo);
		mapInfo.getLayers().add(clientBeansPointLayerInfo);
		new MapAssert(mapInfo).assertEqualImage("onevector.png", writeImages, DELTA);
	}

	@Test
	public void testTwoVectorLayers() throws Exception {
		ClientMapInfo mapInfo = new ClientMapInfo();
		MapRasterizingInfo mapRasterizingInfo = new MapRasterizingInfo();
		mapRasterizingInfo.setBounds(new Bbox(-80, -50, 100, 100));
		mapInfo.setCrs("EPSG:4326");
		mapRasterizingInfo.setScale(1);
		mapRasterizingInfo.setTransparent(true);
		mapInfo.getWidgetInfo().put(MapRasterizingInfo.WIDGET_KEY, mapRasterizingInfo);

		ClientVectorLayerInfo cl1 = new ClientVectorLayerInfo();
		cl1.setServerLayerId(layerBeansPoint.getId());
		VectorLayerRasterizingInfo lr1 = new VectorLayerRasterizingInfo();
		lr1.setStyle(layerBeansPointStyleInfo);
		cl1.getWidgetInfo().put(VectorLayerRasterizingInfo.WIDGET_KEY, lr1);
		mapInfo.getLayers().add(cl1);

		ClientVectorLayerInfo cl2 = new ClientVectorLayerInfo();
		cl2.setServerLayerId(layerBeansMultiLine.getId());
		VectorLayerRasterizingInfo lr2 = new VectorLayerRasterizingInfo();
		lr2.setStyle(layerBeansMultiLineStyleInfo);
		cl2.getWidgetInfo().put(VectorLayerRasterizingInfo.WIDGET_KEY, lr2);
		mapInfo.getLayers().add(cl2);
		new MapAssert(mapInfo).assertEqualImage("twovector.png", writeImages, DELTA);
	}

	@Test
	public void testGeometry() throws Exception {
		ClientMapInfo mapInfo = new ClientMapInfo();
		MapRasterizingInfo mapRasterizingInfo = new MapRasterizingInfo();
		mapRasterizingInfo.setBounds(new Bbox(-80, -50, 100, 100));
		mapInfo.setCrs("EPSG:4326");
		mapRasterizingInfo.setScale(1);
		mapRasterizingInfo.setTransparent(true);
		mapInfo.getWidgetInfo().put(MapRasterizingInfo.WIDGET_KEY, mapRasterizingInfo);

		ClientGeometryLayerInfo geo = new ClientGeometryLayerInfo();
		Geometry point = new Geometry(Geometry.POINT, 4326, 5);
		point.setCoordinates(new Coordinate[] { new Coordinate(20, 50) });
		geo.getGeometries().add(point);
		geo.setStyle(styleConverterService.convert(layerBeansPointStyleInfo, GeometryDirectLayer.DEFAULT_GEOMETRY_NAME));
		geo.setLayerType(LayerType.POINT);
		mapInfo.getLayers().add(geo);
		new MapAssert(mapInfo).assertEqualImage("geometry.png", writeImages, DELTA);
	}

	@Test
	public void testOneRasterLayer() throws Exception {
		ClientMapInfo mapInfo = new ClientMapInfo();
		MapRasterizingInfo mapRasterizingInfo = new MapRasterizingInfo();
		mapRasterizingInfo.setBounds(new Bbox(-180, -90, 360, 180));
		mapInfo.setCrs("EPSG:4326");
		mapRasterizingInfo.setScale(2);
		mapRasterizingInfo.setTransparent(true);
		mapInfo.getWidgetInfo().put(MapRasterizingInfo.WIDGET_KEY, mapRasterizingInfo);

		ClientRasterLayerInfo cl1 = new ClientRasterLayerInfo();
		cl1.setServerLayerId(layerBluemarble.getId());
		RasterLayerRasterizingInfo rr1 = new RasterLayerRasterizingInfo();
		rr1.setCssStyle("opacity:0.5");
		cl1.getWidgetInfo().put(RasterLayerRasterizingInfo.WIDGET_KEY, rr1);
		mapInfo.getLayers().add(cl1);
		new MapAssert(mapInfo).assertEqualImage("oneraster.png", writeImages, DELTA);
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
