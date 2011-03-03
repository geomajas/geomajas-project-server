package org.geomajas.plugin.rasterizing;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.layer.LayerType;
import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.VectorLayer;
import org.geomajas.plugin.rasterizing.api.ImageService;
import org.geomajas.plugin.rasterizing.dto.GeometryLayerMetadata;
import org.geomajas.plugin.rasterizing.dto.MapMetadata;
import org.geomajas.plugin.rasterizing.dto.RasterLayerMetadata;
import org.geomajas.plugin.rasterizing.dto.VectorLayerMetadata;
import org.geomajas.security.SecurityManager;
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
		"/org/geomajas/plugin/rasterizing/rasterizing-service.xml", "/org/geomajas/testdata/beanContext.xml",
		"/org/geomajas/testdata/layerBeans.xml", "/org/geomajas/testdata/layerBeansMultiLine.xml",
		"/org/geomajas/testdata/layerBeansMultiPolygon.xml", "/org/geomajas/testdata/layerBeansPoint.xml",
		"/org/geomajas/testdata/layerBluemarble.xml" })
public class ImageServiceTest {

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
	@Qualifier("layerBeansPointStyleInfo")
	private NamedStyleInfo layerBeansPointStyleInfo;

	@Autowired
	private ImageService imageService;

	@Autowired
	private SecurityManager securityManager;

	private boolean writeImages = true;

	private static final String IMAGE_FILE_PATH = "src/test/resources/org/geomajas/plugin/rasterizing/images/imageservice/";

	private static final String IMAGE_CLASS_PATH = "/org/geomajas/plugin/rasterizing/images/";

	@Before
	public void login() {
		// assure security context is set
		securityManager.createSecurityContext(null);
	}

	@Test
	public void testOneVectorLayer() throws Exception {
		MapMetadata metadata = new MapMetadata();
		metadata.setBounds(new Bbox(-80, -50, 100, 100));
		metadata.setCrs("EPSG:4326");
		metadata.setScale(1);
		metadata.setTransparent(true);
		VectorLayerMetadata beansMetadata = new VectorLayerMetadata();
		beansMetadata.setLayerId(layerBeansPoint.getId());
		beansMetadata.setStyle(layerBeansPointStyleInfo);
		metadata.getLayers().add(beansMetadata);
		writeIt("onevector.png", metadata);
	}

	@Test
	public void testTwoVectorLayers() throws Exception {
		MapMetadata metadata = new MapMetadata();
		metadata.setBounds(new Bbox(-80, -50, 100, 100));
		metadata.setCrs("EPSG:4326");
		metadata.setScale(1);
		metadata.setTransparent(true);
		VectorLayerMetadata beansMetadata = new VectorLayerMetadata();
		beansMetadata.setLayerId(layerBeansPoint.getId());
		beansMetadata.setStyle(layerBeansPointStyleInfo);
		metadata.getLayers().add(beansMetadata);
		VectorLayerMetadata beansMultilineMetadata = new VectorLayerMetadata();
		beansMultilineMetadata.setLayerId(layerBeansMultiLine.getId());
		beansMultilineMetadata.setStyle(layerBeansMultiLineStyleInfo);
		metadata.getLayers().add(beansMultilineMetadata);
		writeIt("twovector.png", metadata);
	}

	@Test
	public void testGeometry() throws Exception {
		MapMetadata metadata = new MapMetadata();
		metadata.setBounds(new Bbox(-80, -50, 100, 100));
		metadata.setCrs("EPSG:4326");
		metadata.setScale(1);
		metadata.setTransparent(true);
		GeometryLayerMetadata layerMetadata = new GeometryLayerMetadata();
		Geometry point = new Geometry(Geometry.POINT, 4326, 5);
		point.setCoordinates(new Coordinate[] { new Coordinate(50, 40) });
		layerMetadata.getGeometries().add(point);
		layerMetadata.setStyle(layerBeansPointStyleInfo.getFeatureStyles().get(0));
		layerMetadata.setLayerId("geometry");
		layerMetadata.setLayerType(LayerType.POINT);
		metadata.getLayers().add(layerMetadata);
		writeIt("geometry.png", metadata);
	}

	@Test
	public void testOneRasterLayer() throws Exception {
		MapMetadata metadata = new MapMetadata();
		metadata.setBounds(new Bbox(-180, -90, 360, 180));
		metadata.setCrs("EPSG:4326");
		metadata.setScale(1.42222222);
		metadata.setTransparent(true);
		RasterLayerMetadata layerMetadata = new RasterLayerMetadata();
		layerMetadata.setLayerId(layerBluemarble.getId());
		layerMetadata.setRasterStyle("0.5");
		metadata.getLayers().add(layerMetadata);
		writeIt("oneraster.png", metadata);
	}

	private void writeIt(String fileName, MapMetadata metadata) throws Exception {
		File file = new File(IMAGE_FILE_PATH + fileName);
		if (writeImages) {
			FileOutputStream fos;
			fos = new FileOutputStream(file);
			imageService.writeMap(fos, metadata);
			fos.flush();
			fos.close();
		} else {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			imageService.writeMap(baos, metadata);
			baos.flush();
			baos.close();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			InputStream is = getClass().getResourceAsStream(IMAGE_CLASS_PATH + fileName);
			byte[] buf = new byte[1024];
			int len;
			while ((len = is.read(buf, 0, 1024)) != -1) {
				bos.write(buf, 0, len);
			}
			is.close();
			byte[] expecteds = bos.toByteArray();
			Assert.assertArrayEquals(expecteds, baos.toByteArray());
		}
	}
}
