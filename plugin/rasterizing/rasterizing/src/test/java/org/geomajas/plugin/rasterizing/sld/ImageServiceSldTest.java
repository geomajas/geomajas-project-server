package org.geomajas.plugin.rasterizing.sld;

import java.io.OutputStream;

import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.layer.VectorLayer;
import org.geomajas.plugin.rasterizing.api.ImageService;
import org.geomajas.plugin.rasterizing.command.dto.MapRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.VectorLayerRasterizingInfo;
import org.geomajas.security.SecurityManager;
import org.geomajas.spring.ThreadScopeContextHolder;
import org.geomajas.testdata.ReloadContext;
import org.geomajas.testdata.ReloadContextTestExecutionListener;
import org.geomajas.testdata.TestPathBinaryStreamAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(listeners = { ReloadContextTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/rasterizing/DefaultCachedAndRasterizedPipelines.xml",
		"/org/geomajas/plugin/rasterizing/rasterizing-service.xml", "/org/geomajas/testdata/beanContext.xml",
		"/org/geomajas/testdata/layerBeans.xml", "/org/geomajas/testdata/layerBeansMultiLine.xml",
		"/org/geomajas/testdata/layerBeansMultiPolygon.xml", "/org/geomajas/testdata/layerBeansPoint.xml",
		"/org/geomajas/testdata/layerBeansMixedGeometry.xml" })
public class ImageServiceSldTest {

	private boolean writeImages = false;

	private static final double DELTA = 1E-6;

	@Qualifier("ImageServiceSldTest.path")
	@Autowired
	private String imagePath;

	@Autowired
	private ImageService imageService;

	@Autowired
	@Qualifier("layerBeansMultiPolygon")
	private VectorLayer layerBeansMultiPolygon;

	@Autowired
	@Qualifier("layerBeansMultiPolygonSldStyleInfo")
	private NamedStyleInfo layerBeansMultiPolygonStyleInfo;

	@Autowired
	@Qualifier("layerBeansPoint")
	private VectorLayer layerBeansPoint;

	@Autowired
	@Qualifier("layerBeansPointSldStyleInfo")
	private NamedStyleInfo layerBeansPointStyleInfo;

	@Autowired
	private SecurityManager securityManager;

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
	public void testMultiPolygonStyle() throws Exception {
		// default
		checkMultiPolygon("multipolygon_default.png", false, true);
	}

	@Test
	@ReloadContext
	public void testPointStyle() throws Exception {
		// default
		checkPoint("point_default.png", false, true);
		// save circle state
	}

	protected void checkPoint(String fileName, boolean paintLabels, boolean paintGeometries, Bbox box) throws Exception {
		checkOrRender(fileName, paintLabels, paintGeometries, layerBeansPoint, layerBeansPointStyleInfo, box);
	}

	protected void checkPoint(String fileName, boolean paintLabels, boolean paintGeometries) throws Exception {
		checkOrRender(fileName, paintLabels, paintGeometries, layerBeansPoint, layerBeansPointStyleInfo);
	}

	protected void checkOrRender(String fileName, boolean paintLabels, boolean paintGeometries, VectorLayer layer,
			NamedStyleInfo styleInfo) throws Exception {
		checkOrRender(fileName, paintLabels, paintGeometries, layer, styleInfo, new Bbox(-50, -50, 100, 100));
	}

	protected void checkOrRender(String fileName, boolean paintLabels, boolean paintGeometries, VectorLayer layer,
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

	protected void checkMultiPolygon(String fileName, boolean paintLabels, boolean paintGeometries) throws Exception {
		checkOrRender(fileName, paintLabels, paintGeometries, layerBeansMultiPolygon, layerBeansMultiPolygonStyleInfo);
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
