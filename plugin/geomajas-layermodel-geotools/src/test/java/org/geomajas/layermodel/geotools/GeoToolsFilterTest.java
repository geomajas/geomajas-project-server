package org.geomajas.layermodel.geotools;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.GeometricAttributeInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.PrimitiveType;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.service.FilterService;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

public class GeoToolsFilterTest extends TestCase {

	private static final String SHAPE_FILE = "org/geomajas/testdata/shapes/filtertest/filtertest.shp";

	private GeotoolsLayerModel layerModel;

	private FilterService filterCreator;

	public void setUp() throws Exception {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		URL url = classloader.getResource(SHAPE_FILE);
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] {
				"org/geomajas/spring/geomajasContext.xml", "org/geomajas/testdata/layerCountries.xml",
				"org/geomajas/testdata/simplevectorsContext.xml" });
		filterCreator = applicationContext.getBean("service.FilterService", FilterService.class);
		layerModel = applicationContext.getBean("layermodel.geotools.GeotoolsLayerModel", GeotoolsLayerModel.class);
		layerModel.setUrl(url);

		FeatureInfo ft = new FeatureInfo();
		ft.setDataSourceName("filtertest");

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
		pa.setLabel("textAttr");
		pa.setName("textAttr");
		pa.setEditable(false);
		pa.setIdentifying(true);
		pa.setType(PrimitiveType.STRING);

		attr.add(pa);

		PrimitiveAttributeInfo pa2 = new PrimitiveAttributeInfo();
		pa2.setLabel("numberAttr");
		pa2.setName("numberAttr");
		pa2.setEditable(false);
		pa2.setIdentifying(true);
		pa2.setType(PrimitiveType.INTEGER);

		attr.add(pa2);
		ft.setAttributes(attr);

		VectorLayerInfo layerInfo = new VectorLayerInfo();
		layerInfo.setFeatureInfo(ft);
		layerInfo.setCrs("EPSG:4326");

		layerModel.setLayerInfo(layerInfo);
	}

	public void testBetweenFilter() throws Exception {
		Filter filter = filterCreator.createBetweenFilter("numberAttr", "2", "8");
		Iterator<?> it = layerModel.getElements(filter);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		assertEquals(2, t);
	}

	public void testCompareFilter() throws Exception {
		Filter filter = filterCreator.createCompareFilter("numberAttr", "<", "15");
		Iterator<?> it = layerModel.getElements(filter);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		assertEquals(4, t);
	}

	public void testLikeFilter() throws Exception {
		Filter filter = filterCreator.createLikeFilter("textAttr", "*sid*");
		Iterator<?> it = layerModel.getElements(filter);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		assertEquals(2, t);
	}

	public void testLogicFilter() throws Exception {
		Filter filter1 = filterCreator.createCompareFilter("numberAttr", "<", "15");
		Filter filter2 = filterCreator.createLikeFilter("textAttr", "over*");
		Filter filter = filterCreator.createLogicFilter(filter1, "and", filter2);

		Iterator<?> it = layerModel.getElements(filter);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		assertEquals(1, t);
	}

	public void testFIDFilter() throws Exception {
		Filter filter = filterCreator.createFidFilter(new String[] { "1" });
		Iterator<?> it = layerModel.getElements(filter);
		SimpleFeature f = (SimpleFeature) it.next();
		assertEquals("centraal", f.getAttribute("textAttr"));
	}

	public void testContainsFilter() throws Exception {
		Geometry geom = (Geometry) ((SimpleFeature) layerModel.read("3")).getDefaultGeometry();
		Filter filter = filterCreator.createContainsFilter(geom, "the_geom");
		Iterator<?> it = layerModel.getElements(filter);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		assertEquals(3, t);
	}

	public void testWithinFilter() throws Exception {
		Geometry geom = (Geometry) ((SimpleFeature) layerModel.read("1")).getDefaultGeometry();
		Filter filter = filterCreator.createWithinFilter(geom, "the_geom");
		Iterator<?> it = layerModel.getElements(filter);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		assertEquals(4, t);
	}

	public void testIntersectsFilter() throws Exception {
		Geometry geom = (Geometry) ((SimpleFeature) layerModel.read("1")).getDefaultGeometry();
		Filter filter = filterCreator.createIntersectsFilter(geom, "the_geom");
		Iterator<?> it = layerModel.getElements(filter);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		assertEquals(6, t);
	}

	public void testTouchesFilter() throws Exception {
		Geometry geom = (Geometry) ((SimpleFeature) layerModel.read("1")).getDefaultGeometry();
		Filter filter = filterCreator.createTouchesFilter(geom, "the_geom");
		Iterator<?> it = layerModel.getElements(filter);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		assertEquals(1, t);
	}

	public void testcreateBBoxFilter() throws Exception {
		Envelope bbox = new Envelope(-0.4d, -0.3d, -0.2d, 0.1d);
		Filter filter = filterCreator.createBboxFilter("EPSG:4326", bbox, "the_geom");
		Iterator<?> it = layerModel.getElements(filter);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		assertEquals(3, t);
	}

	public void testOverlapsFilter() throws Exception {
		Geometry geom = (Geometry) ((SimpleFeature) layerModel.read("4")).getDefaultGeometry();
		Filter filter = filterCreator.createOverlapsFilter(geom, "the_geom");
		Iterator<?> it = layerModel.getElements(filter);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		assertEquals(1, t);
	}
}