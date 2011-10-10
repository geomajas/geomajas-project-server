package org.geomajas.plugin.rasterizing.layer;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.layer.RasterLayer;
import org.geomajas.plugin.rasterizing.command.dto.RasterLayerRasterizingInfo;
import org.geomajas.security.SecurityManager;
import org.geomajas.spring.ThreadScopeContextHolder;
import org.geomajas.testdata.TestPathBinaryStreamAssert;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.DirectLayer;
import org.geotools.map.MapContext;
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
		"/org/geomajas/plugin/rasterizing/rasterizing-service.xml", "/org/geomajas/testdata/layerBluemarble.xml" })
public class RasterDirectLayerTest {

	@Autowired
	private RasterLayerFactory layerFactory;

	@Autowired
	@Qualifier("bluemarble")
	private RasterLayer layerBluemarble;

	// changing this to true and running the test from the base directory will generate the images !
	private boolean writeImages = false;

	private static final double DELTA = 0.04;

	private static final String IMAGE_CLASS_PATH = "org/geomajas/plugin/rasterizing/images/rasterlayer";

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
	public void testWMS() throws Exception {

		ClientRasterLayerInfo cl1 = new ClientRasterLayerInfo();
		cl1.setServerLayerId(layerBluemarble.getId());
		RasterLayerRasterizingInfo rr1 = new RasterLayerRasterizingInfo();
		rr1.setCssStyle("opacity:0.75");
		cl1.getWidgetInfo().put(RasterLayerRasterizingInfo.WIDGET_KEY, rr1);

		DefaultMapContext mapContext = new DefaultMapContext();
		mapContext.setCoordinateReferenceSystem(layerBluemarble.getCrs());
		mapContext.getViewport().setCoordinateReferenceSystem(mapContext.getCoordinateReferenceSystem());
		mapContext.getViewport().setScreenArea(new Rectangle(1024, 512));
		mapContext.getViewport().setBounds(new ReferencedEnvelope(-180, 180, -90, 90, layerBluemarble.getCrs()));
		RasterDirectLayer layer = (RasterDirectLayer) layerFactory.createLayer(mapContext, cl1);
		new DirectLayerAssert(layer, mapContext).assertEqualImage("wms.png", writeImages, DELTA);
		mapContext.dispose();
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
