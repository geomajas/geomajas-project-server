package org.geomajas.plugin.rasterizing.layer;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.geomajas.geometry.Bbox;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerType;
import org.geomajas.layer.RasterLayer;
import org.geomajas.plugin.rasterizing.dto.MapMetadata;
import org.geomajas.plugin.rasterizing.dto.RasterLayerMetadata;
import org.geomajas.security.SecurityManager;
import org.geomajas.service.DtoConverterService;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.junit.Assert;
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

	private boolean writeImages = true;

	private static final String IMAGE_CLASS_PATH = "/org/geomajas/plugin/rasterizing/images/rasterlayer/";

	private static final String IMAGE_FILE_PATH = "src/test/resources" + IMAGE_CLASS_PATH;

	@Autowired
	private SecurityManager securityManager;

	@Before
	public void login() {
		// assure security context is set
		securityManager.createSecurityContext(null);
	}

	@Test
	public void testWMS() throws GeomajasException, IOException {
		RasterLayerMetadata metadata = new RasterLayerMetadata();
		metadata.setLayerId(layerBluemarble.getId());
		metadata.setRasterStyle("0.5");
		DefaultMapContext mapContext = new DefaultMapContext();
		mapContext.getViewport().setCoordinateReferenceSystem(layerBluemarble.getCrs());
		mapContext.getViewport().setScreenArea(new Rectangle(1024, 512));
		mapContext.getViewport().setBounds(new ReferencedEnvelope(-180, 180, -90, 90, layerBluemarble.getCrs()));
		RasterDirectLayer layer = (RasterDirectLayer) layerFactory.createLayer(mapContext, metadata);
		BufferedImage image = new BufferedImage(1024, 512, BufferedImage.TYPE_4BYTE_ABGR);
		layer.draw(image.createGraphics(), mapContext, mapContext.getViewport());
		checkOrRender(image, "wms.png");
	}

	private void checkOrRender(BufferedImage image, String fileName) throws IOException {
		if (writeImages) {
			FileOutputStream fos;
			fos = new FileOutputStream(IMAGE_FILE_PATH + fileName);
			ImageIO.write(image, "PNG", fos);
			fos.flush();
			fos.close();
		} else {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			InputStream is = getClass().getResourceAsStream(IMAGE_CLASS_PATH + fileName);
			byte[] buf = new byte[1024];
			int len;
			while ((len = is.read(buf, 0, 1024)) != -1) {
				bos.write(buf, 0, len);
			}
			is.close();
			byte[] expecteds = bos.toByteArray();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "PNG", baos);
			Assert.assertArrayEquals(expecteds, baos.toByteArray());
		}
	}

}
