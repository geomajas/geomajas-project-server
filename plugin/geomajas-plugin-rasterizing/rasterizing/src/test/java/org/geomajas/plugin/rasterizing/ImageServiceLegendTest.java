package org.geomajas.plugin.rasterizing;

import java.io.OutputStream;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.VectorLayer;
import org.geomajas.plugin.rasterizing.api.ImageService;
import org.geomajas.plugin.rasterizing.command.dto.LegendRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.MapRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.RasterLayerRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.VectorLayerRasterizingInfo;
import org.geomajas.security.SecurityManager;
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
public class ImageServiceLegendTest {

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
	@Qualifier("layerBeansPoint")
	private VectorLayer layerBeansPoint;

	@Autowired
	@Qualifier("bluemarble")
	private RasterLayer layerBluemarble;

	@Autowired
	private ImageService imageService;

	@Autowired
	private SecurityManager securityManager;

	// changing this to true and running the test from the base directory will generate the images !
	private boolean writeImages = false;

	private static final double DELTA = 1E-6;
	
	@Qualifier("ImageServiceLegendTest.path")
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
	public void testLegend() throws Exception {
		ClientMapInfo mapInfo = new ClientMapInfo();
		MapRasterizingInfo mapRasterizingInfo = new MapRasterizingInfo();
		mapRasterizingInfo.setBounds(new Bbox(-180, -90, 360, 180));
		mapInfo.setCrs("EPSG:4326");
		mapRasterizingInfo.setScale(2);
		mapRasterizingInfo.setTransparent(true);
		mapInfo.getWidgetInfo().put(MapRasterizingInfo.WIDGET_KEY, mapRasterizingInfo);

		ClientRasterLayerInfo cl1 = new ClientRasterLayerInfo();
		cl1.setServerLayerId(layerBluemarble.getId());
		cl1.setLabel("Blue Marble");
		RasterLayerRasterizingInfo rr1 = new RasterLayerRasterizingInfo();
		rr1.setCssStyle("opacity:0.5");
		cl1.getWidgetInfo().put(RasterLayerRasterizingInfo.WIDGET_KEY, rr1);
		mapInfo.getLayers().add(cl1);

		ClientVectorLayerInfo cl2 = new ClientVectorLayerInfo();
		cl2.setServerLayerId(layerBeansPoint.getId());
		cl2.setLayerInfo(layerBeansPoint.getLayerInfo());
		cl2.setLabel(layerBeansPoint.getId());
		VectorLayerRasterizingInfo lr2 = new VectorLayerRasterizingInfo();
		lr2.setStyle(layerBeansPointStyleInfo);
		cl2.getWidgetInfo().put(VectorLayerRasterizingInfo.WIDGET_KEY, lr2);
		mapInfo.getLayers().add(cl2);

		ClientVectorLayerInfo cl3 = new ClientVectorLayerInfo();
		cl3.setServerLayerId(layerBeansMultiLine.getId());
		cl3.setLayerInfo(layerBeansMultiLine.getLayerInfo());
		cl3.setLabel(layerBeansMultiLine.getId());
		VectorLayerRasterizingInfo lr3 = new VectorLayerRasterizingInfo();
		lr3.setStyle(layerBeansMultiLineStyleInfo);
		cl3.getWidgetInfo().put(VectorLayerRasterizingInfo.WIDGET_KEY, lr3);
		mapInfo.getLayers().add(cl3);

		LegendRasterizingInfo legendRasterizingInfo = new LegendRasterizingInfo();
		legendRasterizingInfo.setFont(new FontStyleInfo());
		legendRasterizingInfo.getFont().applyDefaults();
		legendRasterizingInfo.getFont().setSize(12);
		legendRasterizingInfo.getFont().setFamily("Arial");
		legendRasterizingInfo.setTitle("legend");
		mapRasterizingInfo.setLegendRasterizingInfo(legendRasterizingInfo);
		new LegendAssert(mapInfo).assertEqualImage("legend.png", writeImages, DELTA);
	}

	@Test
	public void testLegendDynamic() throws Exception {
		ClientMapInfo mapInfo = new ClientMapInfo();
		MapRasterizingInfo mapRasterizingInfo = new MapRasterizingInfo();
		// set the bounds so no features are visible
		mapRasterizingInfo.setBounds(new Bbox(-180, -90, -170, -80));
		mapInfo.setCrs("EPSG:4326");
		mapRasterizingInfo.setScale(2);
		mapRasterizingInfo.setTransparent(true);
		mapInfo.getWidgetInfo().put(MapRasterizingInfo.WIDGET_KEY, mapRasterizingInfo);

		ClientRasterLayerInfo cl1 = new ClientRasterLayerInfo();
		cl1.setServerLayerId(layerBluemarble.getId());
		cl1.setLabel("Blue Marble");
		RasterLayerRasterizingInfo rr1 = new RasterLayerRasterizingInfo();
		rr1.setCssStyle("opacity:0.5");
		cl1.getWidgetInfo().put(RasterLayerRasterizingInfo.WIDGET_KEY, rr1);
		mapInfo.getLayers().add(cl1);

		ClientVectorLayerInfo cl2 = new ClientVectorLayerInfo();
		cl2.setServerLayerId(layerBeansPoint.getId());
		cl2.setLayerInfo(layerBeansPoint.getLayerInfo());
		cl2.setLabel(layerBeansPoint.getId());
		VectorLayerRasterizingInfo lr2 = new VectorLayerRasterizingInfo();
		lr2.setStyle(layerBeansPointStyleInfo);
		cl2.getWidgetInfo().put(VectorLayerRasterizingInfo.WIDGET_KEY, lr2);
		mapInfo.getLayers().add(cl2);

		ClientVectorLayerInfo cl3 = new ClientVectorLayerInfo();
		cl3.setServerLayerId(layerBeansMultiLine.getId());
		cl3.setLayerInfo(layerBeansMultiLine.getLayerInfo());
		cl3.setLabel(layerBeansMultiLine.getId());
		VectorLayerRasterizingInfo lr3 = new VectorLayerRasterizingInfo();
		lr3.setStyle(layerBeansMultiLineStyleInfo);
		cl3.getWidgetInfo().put(VectorLayerRasterizingInfo.WIDGET_KEY, lr3);
		mapInfo.getLayers().add(cl3);

		LegendRasterizingInfo legendRasterizingInfo = new LegendRasterizingInfo();
		legendRasterizingInfo.setFont(new FontStyleInfo());
		legendRasterizingInfo.getFont().applyDefaults();
		legendRasterizingInfo.getFont().setSize(12);
		legendRasterizingInfo.getFont().setFamily("Arial");
		legendRasterizingInfo.setTitle("legend");
		mapRasterizingInfo.setLegendRasterizingInfo(legendRasterizingInfo);
		new LegendAssert(mapInfo).assertEqualImage("legend_dynamic.png", writeImages, DELTA);
	}
	
	private FeatureStyleInfo getPointStyle() {
		return layerBeansPointStyleInfo.getFeatureStyles().get(0);
	}

	class LegendAssert extends TestPathBinaryStreamAssert {

		private ClientMapInfo map;

		public LegendAssert(ClientMapInfo map) {
			super(imagePath);
			this.map = map;
		}

		public void generateActual(OutputStream out) throws Exception {
			imageService.writeLegend(out, map);
		}

	}

}
