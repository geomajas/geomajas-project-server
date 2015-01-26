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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.attribute.IntegerAttribute;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.service.FilterService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Test for shape-in-mem layer.
 * 
 * @author Pieter De Graef
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/layerCountries.xml", "/org/geomajas/testdata/layerPopulatedPlaces110m.xml",
		"/org/geomajas/testdata/simplevectorsContext.xml", "/org/geomajas/layer/shapeinmem/test.xml" })
public class ShapeInMemLayerTest {

	protected static final String LAYER_NAME = "110m_populated_places_simple";

	@Autowired
	private FilterService filterService;

	@Autowired()
	@Qualifier("test")
	private ShapeInMemLayer layer;

	private Filter filter;

	@Before
	public void setUp() throws Exception {
		filter = filterService.createCompareFilter("POP_OTHER", ">", "1000000");
	}

	@Test
	public void read() throws Exception {
		SimpleFeature f = (SimpleFeature) layer.read(LAYER_NAME + ".2"); // id always starts with layer id
		Assert.assertNotNull(f);
		Assert.assertEquals("Vatican City", f.getAttribute("NAME"));
	}

	@Test
	public void create() throws ParseException, LayerException {
		Object created = null;
		WKTReader wktReader = new WKTReader();
		Point geometry = null;
		geometry = (Point) wktReader.read("POINT (0 0)");

		Object feature = (SimpleFeature)layer.getFeatureModel().newInstance("500");
		Map<String, Attribute> map = new HashMap<String, Attribute>();
		map.put("NAME", new StringAttribute("Tsjakamaka"));
		map.put("POP_OTHER", new IntegerAttribute(342));
		layer.getFeatureModel().setAttributes(feature, map);
		layer.getFeatureModel().setGeometry(feature, geometry);
		
		created = layer.create(feature);
		Assert.assertNotNull(created);
	}

	@Test
	public void delete() {
		try {
			SimpleFeature f = (SimpleFeature) layer.read(LAYER_NAME + ".3");
			Assert.assertNotNull(f);
			layer.delete(LAYER_NAME + ".3");
			Assert.assertTrue(true);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	@Test
	public void getBounds() throws Exception {
		Envelope bbox = layer.getBounds();
		Assert.assertEquals(-175.22, bbox.getMinX(), .01);
		Assert.assertEquals(179.21, bbox.getMaxX(), .01);
		Assert.assertEquals(-41.29, bbox.getMinY(), .01);
		Assert.assertEquals(64.15, bbox.getMaxY(), .01);
	}

	@Test
	public void getBoundsFilter() throws Exception {
		Envelope bbox = layer.getBounds(filter);
		Assert.assertEquals(-122.34, bbox.getMinX(), .01);
		Assert.assertEquals(151.18, bbox.getMaxX(), .01);
		Assert.assertEquals(-37.81, bbox.getMinY(), .01);
		Assert.assertEquals(55.75, bbox.getMaxY(), .01);
	}

	@Test
	public void getElements() throws LayerException {
		// Checked in QGis!
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int counter = 0;
		while (it.hasNext()) {
			it.next();
			counter++;
		}
		Assert.assertEquals(198, counter);
	}

	@After
	public void refreshContext() throws LayerException {
		layer.initFeatures();
	}

}