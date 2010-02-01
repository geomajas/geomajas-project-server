package org.geomajas.layermodel.geotools;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTReader;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.GeometricAttributeInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.PrimitiveType;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.service.FilterService;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class GeoToolsLayerModelTest {

	private static final String SHAPE_FILE =
			"org/geomajas/testdata/shapes/cities_world/cities.shp";

	private static GeotoolsLayerModel layerModel;

	private static Filter filter;

	@BeforeClass
	public static void init() throws Exception {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		URL url = classloader.getResource(SHAPE_FILE);
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				new String[] {"org/geomajas/spring/geomajasContext.xml",
						"org/geomajas/testdata/layerCountries.xml",
						"org/geomajas/testdata/simplevectorsContext.xml"});
		FilterService filterCreator = applicationContext.getBean("service.FilterService", FilterService.class);
		layerModel = applicationContext.getBean("layermodel.geotools.GeotoolsLayerModel", GeotoolsLayerModel.class);
		layerModel.setUrl(url);

		FeatureInfo ft = new FeatureInfo();
		ft.setDataSourceName("cities");

		PrimitiveAttributeInfo ia = new PrimitiveAttributeInfo();
		ia.setLabel("id");
		ia.setName("Id");
		ia.setType(PrimitiveType.STRING);
		ft.setIdentifier(ia);

		GeometricAttributeInfo ga = new GeometricAttributeInfo();
		ga.setName("the_geom");
		ga.setEditable(false);
		ft.setGeometryType(ga);

		List<AttributeInfo> attr = new ArrayList<AttributeInfo>();
		PrimitiveAttributeInfo pa = new PrimitiveAttributeInfo();
		pa.setLabel("City");
		pa.setName("City");
		pa.setEditable(false);
		pa.setIdentifying(true);
		pa.setType(PrimitiveType.STRING);

		attr.add(pa);
		ft.setAttributes(attr);

		VectorLayerInfo layerInfo = new VectorLayerInfo();
		layerInfo.setFeatureInfo(ft);
		layerInfo.setCrs("EPSG:4326");

		layerModel.setLayerInfo(layerInfo);
		filter = filterCreator.createCompareFilter("Population", ">", "49900");
	}

	@Test
	public void testRead() throws Exception {
		SimpleFeature f = (SimpleFeature) layerModel.read("9703");
		Assert.assertEquals("Elmhurst", f.getAttribute("City"));
	}

	@Test
	public void testUpdate() throws Exception {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleFeature f = (SimpleFeature) layerModel.read("10");
		f.setAttribute("City", sdf.format(cal.getTime()));
		layerModel.update(f);
		Assert.assertEquals(sdf.format(cal.getTime()), f.getAttribute("City"));
	}

	@Test
	public void create() throws Exception {
		WKTReader wktReader = new WKTReader();
		Point geometry = (Point) wktReader.read("POINT (0 0)");

		SimpleFeatureBuilder build = new SimpleFeatureBuilder(layerModel.getSchema());
		SimpleFeature feature = build.buildFeature("100000", new Object[] {geometry, "Tsjakamaka", 342});

		Object created = layerModel.create(feature);
		Assert.assertNotNull(created);
	}

	@Test
	public void testDelete() throws Exception {
		SimpleFeature f = (SimpleFeature) layerModel.read("10580");
		Assert.assertNotNull(f);
		layerModel.delete("10580");
		Assert.assertTrue(true);
	}

	@Test
	public void testGetBounds() throws Exception {
		// Checked in QGis!
		Bbox bbox = layerModel.getBounds();
		Assert.assertEquals(-175.22, bbox.getX(), .0001);
		Assert.assertEquals(179.38, bbox.getMaxX(), .0001);
		Assert.assertEquals(-46.41, bbox.getY(), .0001);
		Assert.assertEquals(69.41, bbox.getMaxY(), .0001);
	}

	@Test
	public void testGetBoundsFilter() throws Exception {
		// Checked in QGis!
		Bbox bbox = layerModel.getBounds(filter);
		Assert.assertEquals(-118.01, bbox.getX(), .0001);
		Assert.assertEquals(120.86, bbox.getMaxX(), .0001);
		Assert.assertEquals(-19.99, bbox.getY(), .0001);
		Assert.assertEquals(53.09, bbox.getMaxY(), .0001);
	}

	@Test
	public void testGetElements() throws Exception {
		// Checked in QGis!
		Iterator<?> it = layerModel.getElements(filter);
		int counter = 0;
		while (it.hasNext()) {
			it.next();
			counter++;
		}
		Assert.assertEquals(16, counter);
	}
}