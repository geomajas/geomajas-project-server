/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.layer.geotools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.GeometryAttributeInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.PrimitiveType;
import org.geomajas.configuration.VectorLayerInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Test for GeoTools layer.
 *
 * @author Pieter De Graef
 */
public class GeoToolsFilterTest extends AbstractGeoToolsTest {

	private static final String SHAPE_FILE = "org/geomajas/testdata/shapes/filtertest/filtertest.shp";

	@Autowired
	@Qualifier("filterTest")
	private GeoToolsLayer layer;

	@Before
	public void setUp() throws Exception {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		classloader.getResource(SHAPE_FILE);

		FeatureInfo ft = new FeatureInfo();
		ft.setDataSourceName("filtertest");

		PrimitiveAttributeInfo ia = new PrimitiveAttributeInfo();
		ia.setLabel("id");
		ia.setName("Id");
		ia.setType(PrimitiveType.STRING);
		ft.setIdentifier(ia);

		GeometryAttributeInfo ga = new GeometryAttributeInfo();
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

		layer.setLayerInfo(layerInfo);
		layer.initFeatures();
	}

	@Test
	public void testBetweenFilter() throws Exception {
		Filter filter = filterCreator.createBetweenFilter("numberAttr", "2", "8");
		Iterator<?> it = layer.getElements(filter, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(2, t);
	}

	@Test
	public void testCompareFilter() throws Exception {
		Filter filter = filterCreator.createCompareFilter("numberAttr", "<", "15");
		Iterator<?> it = layer.getElements(filter, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(4, t);
	}

	@Test
	public void testLikeFilter() throws Exception {
		Filter filter = filterCreator.createLikeFilter("textAttr", "*sid*");
		Iterator<?> it = layer.getElements(filter, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(2, t);
	}

	@Test
	public void testLogicFilter() throws Exception {
		Filter filter1 = filterCreator.createCompareFilter("numberAttr", "<", "15");
		Filter filter2 = filterCreator.createLikeFilter("textAttr", "over*");
		Filter filter = filterCreator.createLogicFilter(filter1, "and", filter2);

		Iterator<?> it = layer.getElements(filter, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(1, t);
	}

	@Test
	public void testFIDFilter() throws Exception {
		Filter filter = filterCreator.createFidFilter(new String[] { "filtertest.1" });
		Iterator<?> it = layer.getElements(filter, 0, 0);
		SimpleFeature f = (SimpleFeature) it.next();
		Assert.assertEquals("centraal", f.getAttribute("textAttr"));
	}

	@Test
	public void testContainsFilter() throws Exception {
		Geometry geom = (Geometry) ((SimpleFeature) layer.read("filtertest.3")).getDefaultGeometry();
		Filter filter = filterCreator.createContainsFilter(geom, "the_geom");
		Iterator<?> it = layer.getElements(filter, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void testWithinFilter() throws Exception {
		Geometry geom = (Geometry) ((SimpleFeature) layer.read("filtertest.1")).getDefaultGeometry();
		Filter filter = filterCreator.createWithinFilter(geom, "the_geom");
		Iterator<?> it = layer.getElements(filter, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(4, t);
	}

	@Test
	public void testIntersectsFilter() throws Exception {
		Geometry geom = (Geometry) ((SimpleFeature) layer.read("filtertest.1")).getDefaultGeometry();
		Filter filter = filterCreator.createIntersectsFilter(geom, "the_geom");
		Iterator<?> it = layer.getElements(filter, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(6, t);
	}

	@Test
	public void testTouchesFilter() throws Exception {
		Geometry geom = (Geometry) ((SimpleFeature) layer.read("filtertest.1")).getDefaultGeometry();
		Filter filter = filterCreator.createTouchesFilter(geom, "the_geom");
		Iterator<?> it = layer.getElements(filter, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(1, t);
	}

	@Test
	public void testcreateBBoxFilter() throws Exception {
		Envelope bbox = new Envelope(-0.4d, -0.2d, -0.3d, 0.1d);
		Filter filter = filterCreator.createBboxFilter("EPSG:900913", bbox, "the_geom");
		Iterator<?> it = layer.getElements(filter, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void testOverlapsFilter() throws Exception {
		Geometry geom = (Geometry) ((SimpleFeature) layer.read("filtertest.4")).getDefaultGeometry();
		Filter filter = filterCreator.createOverlapsFilter(geom, "the_geom");
		Iterator<?> it = layer.getElements(filter, 0, 0);

		int t = 0;
		while (it.hasNext()) {
			it.next();
			t++;
		}
		Assert.assertEquals(1, t);
	}
}