package org.geomajas.layermodel.geotools;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTReader;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureIterator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Testcase testing all methods of the Geotools FeatureModel.
 * </p>
 *
 * @author Mathias Versichele
 */
public class GeoToolsFeatureModelTest {

	private static final String SHAPE_FILE =
			"org/geomajas/testdata/shapes/cities_world/cities.shp";

	private static final String LAYER_NAME = "cities";

	private static GeotoolsFeatureModel featureModel;

	private static SimpleFeature feature;

	@BeforeClass
	public static void init() throws Exception {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		URL url = classloader.getResource(SHAPE_FILE);
		DataStore dataStore = new ShapefileDataStore(url);
		featureModel = new GeotoolsFeatureModel(dataStore, LAYER_NAME, 4326);

		FeatureSource<SimpleFeatureType, SimpleFeature> fs = featureModel.getFeatureSource();
		FeatureIterator<SimpleFeature> fi = fs.getFeatures().features();
		feature = fi.next();
		feature = fi.next();
		feature = fi.next();
		feature = fi.next();
	}

	@Test
	public void testGetId() throws Exception {
		Assert.assertEquals(featureModel.getId(feature), "4");
	}

	@Test
	public void testGetAttribute() throws Exception {
		Assert.assertEquals(featureModel.getAttribute(feature, "City"), "Heusweiler");
	}

	@Test
	public void testGetAttributes() throws Exception {
		Assert.assertEquals(featureModel.getAttributes(feature).get("City"), "Heusweiler");
	}

	@Test
	public void testGetGeometry() throws Exception {
		Assert.assertEquals(featureModel.getGeometry(feature).getCoordinate().x, 6.93, 0.01);
	}

	@Test
	public void testGetGeometryAttributeName() throws Exception {
		Assert.assertEquals(featureModel.getGeometryAttributeName(), "the_geom");
	}

	@Test
	public void testGetSrid() throws Exception {
		Assert.assertEquals(4326, featureModel.getSrid());
	}

	@Test
	public void testSetAttributes() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("City", "Heikant");
		map.put("the_geom", featureModel.getGeometry(feature));
		map.put("Population", 100);
		featureModel.setAttributes(feature, map);
		Assert.assertEquals(featureModel.getAttribute(feature, "City"), "Heikant");
	}

	@Test
	public void testSetGeometry() throws Exception {
		WKTReader wktReader = new WKTReader();
		Point pt = (Point) wktReader.read("POINT (5 5)");
		featureModel.setGeometry(feature, pt);
		Assert.assertEquals(5, featureModel.getGeometry(feature).getCoordinate().x, 0);
	}
}