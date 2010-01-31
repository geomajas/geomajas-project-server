package org.geomajas.layermodel.shapeinmem;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.GeometricAttributeInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.PrimitiveType;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.service.FilterService;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShapeInMemLayerModelTest {

	private static final String SHAPE_FILE = "classpath:org/geomajas/testdata/shapes/cities_world/cities.shp";

	private ShapeInMemLayerModel layerModel;

	private Filter filter;

	@Before
	public void setUp() throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				new String[] {"org/geomajas/spring/geomajasContext.xml",
						"org/geomajas/testdata/layerCountries.xml",
						"org/geomajas/testdata/simplevectorsContext.xml"});
		FilterService filterCreator = applicationContext.getBean("service.FilterCreator", FilterService.class);
		layerModel = applicationContext.getBean(
				"layermodel.shapeinmem.ShapeInMemLayerModel", ShapeInMemLayerModel.class);
		layerModel.setUrl(SHAPE_FILE);

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
	public void read() {
		SimpleFeature f = null;
		try {
			f = (SimpleFeature) layerModel.read("9703");
		} catch (Exception e) {
		}

		Assert.assertNotNull(f);
		Assert.assertEquals("Elmhurst", f.getAttribute("City"));
	}

	@Test
	public void create() {
		Object created = null;
		try {
			WKTReader wktReader = new WKTReader();
			Point geometry = null;
			try {
				geometry = (Point) wktReader.read("POINT (0 0)");
			} catch (ParseException e) {
			}

			SimpleFeature feature = CommonFactoryFinder.getFeatureFactory(null).createSimpleFeature(
					new Object[] {geometry, "Tsjakamaka", 342}, layerModel.getSchema(), "100000");

			created = layerModel.create(feature);
		} catch (Exception e) {
		}
		Assert.assertNotNull(created);
	}

	@Test
	public void delete() {
		try {
			SimpleFeature f = (SimpleFeature) layerModel.read("10580");
			Assert.assertNotNull(f);
			layerModel.delete("10580");
			Assert.assertTrue(true);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	@Test
	public void getBounds() throws Exception {
		// Checked in QGis!
		Bbox bbox = layerModel.getBounds();
		Assert.assertEquals(-175.22, bbox.getX(), .0001);
		Assert.assertEquals(179.38, bbox.getMaxX(), .0001);
		Assert.assertEquals(-46.41, bbox.getY(), .0001);
		Assert.assertEquals(69.41, bbox.getMaxY(), .0001);
	}

	@Test
	public void getBoundsFilter() throws Exception {
		// Checked in QGis!
		Bbox bbox = layerModel.getBounds(filter);
		Assert.assertEquals(-118.01, bbox.getX(), .0001);
		Assert.assertEquals(120.86, bbox.getMaxX(), .0001);
		Assert.assertEquals(-19.99, bbox.getY(), .0001);
		Assert.assertEquals(53.09, bbox.getMaxY(), .0001);
	}

	@Test
	public void getElements() {
		try {
			// Checked in QGis!
			Iterator<?> it = layerModel.getElements(filter);
			int counter = 0;
			while (it.hasNext()) {
				it.next();
				counter++;
			}
			Assert.assertEquals(16, counter);
		} catch (Exception e) {
		}
	}
}