/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.layer.geotools;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.internal.layer.feature.FeatureModelRegistry;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.attribute.IntegerAttribute;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureIterator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * <p>
 * Test case testing all methods of the GeoTools FeatureModel.
 * </p>
 * 
 * @author Mathias Versichele
 * @author Pieter De Graef
 */
public class GeoToolsFeatureModelTest extends AbstractGeoToolsTest {

	private static final double DELTA = 0.00001;

	private GeoToolsFeatureModel readOnlyFeatureModel;

	private GeoToolsFeatureModel featureModel;

	private SimpleFeature feature;

	private SimpleFeature readOnlyFeature;

	@Autowired
	@Qualifier("populatedPlaces110mInfo")
	private VectorLayerInfo layerInfo;

	@Autowired
	@Qualifier("populatedPlaces110mInfoEditable")
	private VectorLayerInfo layerInfoEditable;

	@Before
	public void init() throws Exception {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		URL url = classloader.getResource(SHAPE_FILE);
		DataStore dataStore = new ShapefileDataStore(url);
		readOnlyFeatureModel = new GeoToolsFeatureModel(dataStore, LAYER_NAME, 4326, converterService);
		readOnlyFeatureModel.setLayerInfo(layerInfo);

		featureModel = new GeoToolsFeatureModel(dataStore, LAYER_NAME, 4326, converterService);
		featureModel.setLayerInfo(layerInfoEditable);

		FeatureSource<SimpleFeatureType, SimpleFeature> fs = featureModel.getFeatureSource();
		FeatureIterator<SimpleFeature> fi = fs.getFeatures().features();
		feature = fi.next();
		feature = fi.next();
		feature = fi.next();
		feature = fi.next();
		feature = fi.next();

		FeatureSource<SimpleFeatureType, SimpleFeature> fs2 = readOnlyFeatureModel.getFeatureSource();
		FeatureIterator<SimpleFeature> fi2 = fs.getFeatures().features();
		readOnlyFeature = fi2.next();
		readOnlyFeature = fi2.next();
		readOnlyFeature = fi2.next();
		readOnlyFeature = fi2.next();
		readOnlyFeature = fi2.next();
}

	@Test
	public void testGetId() throws Exception {
		Assert.assertEquals(LAYER_NAME + ".5", featureModel.getId(feature));
	}

	@Test
	public void testGetAttribute() throws Exception {
		Assert.assertEquals("Pasay City", featureModel.getAttribute(feature, ATTRIBUTE_NAME).getValue());
	}

	@Test
	public void testGetAttributes() throws Exception {
		Assert.assertEquals("Pasay City", featureModel.getAttributes(feature).get(ATTRIBUTE_NAME).getValue());
	}

	@Test
	public void testGetGeometry() throws Exception {
		Assert.assertEquals(120.99, featureModel.getGeometry(feature).getCoordinate().x, 0.01);
	}

	@Test
	public void testGetGeometryAttributeName() throws Exception {
		Assert.assertEquals("the_geom", featureModel.getGeometryAttributeName());
	}

	@Test
	public void testGetSrid() throws Exception {
		Assert.assertEquals(4326, featureModel.getSrid());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSetAttributes() throws Exception {
		Map<String, Attribute> map = new HashMap<String, Attribute>();
		map.put(ATTRIBUTE_NAME, new StringAttribute("Heikant"));
		map.put(ATTRIBUTE_POPULATION, new IntegerAttribute(100));
		featureModel.setAttributes(feature, map);
		Assert.assertEquals("Heikant", featureModel.getAttribute(feature, ATTRIBUTE_NAME).getValue());
	}

	@Test
	public void testSetGeometry() throws Exception {
		WKTReader wktReader = new WKTReader();
		Point pt = (Point) wktReader.read("POINT (5 5)");
		featureModel.setGeometry(feature, pt);
		Assert.assertEquals(5, featureModel.getGeometry(feature).getCoordinate().x, 0);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testSetAttributesReadOnly() throws Exception {
		Map<String, Attribute> map = new HashMap<String, Attribute>();
		map.put(ATTRIBUTE_NAME, new StringAttribute("Heikant"));
		map.put(ATTRIBUTE_POPULATION, new IntegerAttribute(100));
		readOnlyFeatureModel.setAttributes(readOnlyFeature, map);
		Assert.assertEquals("Pasay City", featureModel.getAttribute(feature, ATTRIBUTE_NAME).getValue());
	}

	@Test
	public void testSetGeometryReadOnly() throws Exception {
		WKTReader wktReader = new WKTReader();
		Point pt = (Point) wktReader.read("POINT (5 5)");
		readOnlyFeatureModel.setGeometry(readOnlyFeature, pt);
		Assert.assertEquals(121, featureModel.getGeometry(feature).getCoordinate().x, DELTA);
	}
}