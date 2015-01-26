/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.layer.shapeinmem;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.attribute.DoubleAttribute;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.service.DtoConverterService;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureIterator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTReader;

/**
 * <p>
 * Testcase testing all methods of the Shape-In-Memory FeatureModel.
 * </p>
 * 
 * @author Mathias Versichele
 * @author Pieter De Graef
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/layerCountries.xml",
		"/org/geomajas/layer/geotools/layerPopulatedPlaces110m.xml",
		"/org/geomajas/testdata/simplevectorsContext.xml" })
public class ShapeInMemFeatureModelTest {

	private static final String SHAPE_FILE = 
		"org/geomajas/testdata/shapes/natural_earth/110m_populated_places_simple.shp";

	private static final String LAYER_NAME = "110m_populated_places_simple";
	
	protected static final String ATTRIBUTE_NAME = "NAME";

	protected static final String ATTRIBUTE_POPULATION = "POP_OTHER";

	private ShapeInMemFeatureModel featureModel;

	private SimpleFeature feature;

	@Autowired
	private DtoConverterService converterService;

	@Autowired
	@Qualifier("populatedPlaces110mInfo")
	private VectorLayerInfo layerInfo;

	@Before
	public void setUp() throws Exception {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		URL url = classloader.getResource(SHAPE_FILE);
		DataStore dataStore = new ShapefileDataStore(url);
		featureModel = new ShapeInMemFeatureModel(dataStore, LAYER_NAME, 4326, converterService);
		featureModel.setLayerInfo(layerInfo);

		FeatureSource<SimpleFeatureType, SimpleFeature> fs = featureModel.getFeatureSource();
		FeatureIterator<SimpleFeature> fi = fs.getFeatures().features();
		feature = fi.next();
		feature = fi.next();
		feature = fi.next();
		feature = fi.next();
		feature = fi.next();
		fi.close();
	}

	@Test
	public void getId() throws Exception {
		Assert.assertEquals(LAYER_NAME + ".5", featureModel.getId(feature));
	}

	@Test
	public void newInstance() throws Exception {
		Object newInstance = null;
			newInstance = featureModel.newInstance();
		Assert.assertNotNull(newInstance);

			newInstance = featureModel.newInstance("1000000");
		Assert.assertNotNull(newInstance);

		String id = null;
			id = featureModel.getId(newInstance);
		Assert.assertNotNull(id);
	}

	@Test
	public void canHandle() {
		Assert.assertTrue(featureModel.canHandle(feature));
	}

	@Test
	public void getAttribute() throws Exception {
		Assert.assertEquals("Pasay City", featureModel.getAttribute(feature, ATTRIBUTE_NAME).getValue());
	}

	@Test
	public void getAttributes() throws Exception {
		Assert.assertEquals("Pasay City", featureModel.getAttributes(feature).get(ATTRIBUTE_NAME).getValue());
	}

	@Test
	public void getGeometry() throws Exception {
		Assert.assertEquals(120.99, featureModel.getGeometry(feature).getCoordinate().x, 0.01);
	}

	@Test
	public void getGeometryAttributeName() throws Exception {
		Assert.assertEquals("the_geom", featureModel.getGeometryAttributeName());
	}

	@Test
	public void getSrid() throws Exception {
		Assert.assertEquals(4326, featureModel.getSrid());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void setAttributes() throws Exception {
		Map<String, Attribute> map = new HashMap<String, Attribute>();
		map.put(ATTRIBUTE_NAME, new StringAttribute("Heikant"));
		map.put(ATTRIBUTE_POPULATION, new DoubleAttribute(100.0));
		featureModel.setAttributes(feature, map);
		Assert.assertEquals("Heikant", featureModel.getAttribute(feature, ATTRIBUTE_NAME).getValue());
	}

	@Test
	public void setGeometry() throws Exception {
		WKTReader wktReader = new WKTReader();
		Point pt = (Point) wktReader.read("POINT (5 5)");
		featureModel.setGeometry(feature, pt);
		Assert.assertEquals(5, featureModel.getGeometry(feature).getCoordinate().x, 0.00001);
	}
}