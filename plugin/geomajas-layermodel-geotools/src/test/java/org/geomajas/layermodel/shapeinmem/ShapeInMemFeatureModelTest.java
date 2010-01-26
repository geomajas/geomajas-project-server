package org.geomajas.layermodel.shapeinmem;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTReader;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureIterator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Testcase testing all methods of the Shape-In-Memory FeatureModel.
 * </p>
 *
 * @author Mathias Versichele
 */
public class ShapeInMemFeatureModelTest {

	private static final String SHAPE_FILE = "org/geomajas/testdata/shapes/cities_world/cities.shp";

	private static final String LAYER_NAME = "cities";

	private ShapeInMemFeatureModel featureModel;

	private SimpleFeature feature;

	@Before
	public void setUp() throws Exception {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		URL url = classloader.getResource(SHAPE_FILE);
		DataStore dataStore = new ShapefileDataStore(url);
		featureModel = new ShapeInMemFeatureModel(dataStore, LAYER_NAME, 4326);

		FeatureSource<SimpleFeatureType, SimpleFeature> fs = featureModel.getFeatureSource();
		FeatureIterator<SimpleFeature> fi = fs.getFeatures().features();
		feature = fi.next();
		feature = fi.next();
		feature = fi.next();
		feature = fi.next();
		fi.close();
	}

	@Test
	public void getId() throws Exception {
		Assert.assertEquals("Id = 4", featureModel.getId(feature), "4");
	}

	@Test
	public void newInstance() throws Exception {
		Object newInstance = null;
		try {
			newInstance = featureModel.newInstance();
		} catch (Exception e) {
		}
		Assert.assertNotNull(newInstance);

		try {
			newInstance = featureModel.newInstance("1000000");
		} catch (Exception e) {
		}
		Assert.assertNotNull(newInstance);

		String id = null;
		try {
			id = featureModel.getId(newInstance);
		} catch (Exception e) {
		}
		Assert.assertNotNull(id);
	}

	@Test
	public void canHandle() {
		Assert.assertTrue(featureModel.canHandle(feature));
	}

	@Test
	public void getAttribute() throws Exception {
		Assert.assertEquals("City = Heusweiler", featureModel.getAttribute(feature, "City"), "Heusweiler");
	}

	@Test
	public void getAttributes() throws Exception {
		Assert.assertEquals("City = Heusweiler", featureModel.getAttributes(feature).get("City"), "Heusweiler");
	}

	@Test
	public void getGeometry() throws Exception {
		Assert.assertEquals(featureModel.getGeometry(feature).getCoordinate().x, 6.93, 0.01);
	}

	@Test
	public void getGeometryAttributeName() throws Exception {
		Assert.assertEquals(featureModel.getGeometryAttributeName(), "the_geom");
	}

	@Test
	public void getSrid() throws Exception {
		Assert.assertEquals(4326, featureModel.getSrid());
	}

	@Test
	public void setAttributes() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("City", "Heikant");
		map.put("the_geom", featureModel.getGeometry(feature));
		map.put("Population", 100);
		featureModel.setAttributes(feature, map);
		Assert.assertEquals(featureModel.getAttribute(feature, "City"), "Heikant");
	}

	@Test
	public void setGeometry() throws Exception {
		WKTReader wktReader = new WKTReader();
		Point pt = (Point) wktReader.read("POINT (5 5)");
		featureModel.setGeometry(feature, pt);
		Assert.assertEquals(5, featureModel.getGeometry(feature).getCoordinate().x, 0);
	}
}