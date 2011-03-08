package org.geomajas.plugin.rasterizing.layer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.geometry.Geometry;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerType;
import org.geomajas.plugin.rasterizing.dto.GeometryLayerMetadata;
import org.geomajas.service.DtoConverterService;
import org.geotools.geometry.jts.WKTReader2;
import org.geotools.map.DefaultMapContext;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.io.ParseException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml"})
public class GeometryDirectLayerTest {

	@Autowired
	private GeometryLayerFactory layerFactory;
	
	@Autowired
	private DtoConverterService converterService;

	private boolean writeImages = true;

	private static final String IMAGE_FILE_PATH = "src/test/resources/org/geomajas/plugin/rasterizing/images/geometrylayer/";

	private static final String IMAGE_CLASS_PATH = "/org/geomajas/plugin/rasterizing/images/";

	@Test
	public void testPolygon() throws GeomajasException, ParseException, IOException {
		GeometryLayerMetadata metadata = new GeometryLayerMetadata();
		metadata.getGeometries().add(createPolygon());
		metadata.setLayerType(LayerType.POLYGON);
		metadata.setLayerId("polygon");
		metadata.setStyle(createPolygonStyle());
		DefaultMapContext mapContext = new DefaultMapContext();
		GeometryDirectLayer layer = (GeometryDirectLayer) layerFactory.createLayer(mapContext, metadata);
		BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR);
		layer.draw(image.createGraphics(), mapContext, mapContext.getViewport());
		checkOrRender(image, "polygon.png");
	}
	
	private void checkOrRender(BufferedImage image, String fileName) throws IOException{
		if (writeImages) {
			FileOutputStream fos;
			fos = new FileOutputStream(IMAGE_FILE_PATH+fileName);
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

	private FeatureStyleInfo createPolygonStyle() {
		FeatureStyleInfo style = new FeatureStyleInfo();
		style.setFillColor("#D2691E");
		style.setFillOpacity(0.5f);
		style.setStrokeColor("#D2691E");
		style.setStrokeOpacity(1f);
		style.setStrokeWidth(3);
		return style;
	}

	private Geometry createPolygon() throws GeomajasException, ParseException {
		WKTReader2 reader = new WKTReader2();
		return converterService.toDto(reader.read("POLYGON((10 10,90 10,90 90,10 90,10 10 ))"));
	}
}
