/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.layer.geotools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.configuration.AbstractAttributeInfo;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.GeometryAttributeInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.PrimitiveType;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.layer.VectorLayer;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Test for GeoTools layer.
 * 
 * @author Pieter De Graef
 */
public class GeoToolsLayerTest extends AbstractGeoToolsTest {

	private GeoToolsLayer layer;

	private Filter filter;

	@Before
	public void init() throws Exception {
		layer = (GeoToolsLayer) applicationContext.getBean("test", VectorLayer.class);

		FeatureInfo ft = new FeatureInfo();
		ft.setDataSourceName(LAYER_NAME);

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
		Map<String, AbstractAttributeInfo> attrMap = new LinkedHashMap<String, AbstractAttributeInfo>();
		PrimitiveAttributeInfo pa = new PrimitiveAttributeInfo();
		pa.setLabel("Name");
		pa.setName("NAME");
		pa.setEditable(false);
		pa.setIdentifying(true);
		pa.setType(PrimitiveType.STRING);

		attr.add(pa);
		attrMap.put(pa.getName(), pa);
		ft.setAttributes(attr);
		ft.setAttributesMap(attrMap);

		VectorLayerInfo layerInfo = new VectorLayerInfo();
		layerInfo.setFeatureInfo(ft);
		layerInfo.setCrs("EPSG:4326");

		layer.setLayerInfo(layerInfo);
		layer.initFeatures();
		filter = filterCreator.createCompareFilter(ATTRIBUTE_POPULATION, ">", "1000000");
	}

	@Test
	public void testRead() throws Exception {
		SimpleFeature f = (SimpleFeature) layer.read(LAYER_NAME + ".2"); // id always starts with layer id
		Assert.assertEquals("Vatican City", f.getAttribute(ATTRIBUTE_NAME));
		Assert.assertEquals(562430, f.getAttribute(ATTRIBUTE_POPULATION));
	}

	@Test
	public void testUpdate() throws Exception {
		SimpleFeature f = (SimpleFeature) layer.read(LAYER_NAME + ".3"); // id always starts with layer id
		f.setAttribute("NAME", "Luxembourg2");
		layer.update(f);
		Assert.assertEquals("Luxembourg2", f.getAttribute(ATTRIBUTE_NAME));
	}

	@Test
	public void create() throws Exception {
		WKTReader wktReader = new WKTReader();
		Point geometry = (Point) wktReader.read("POINT (0 0)");

		SimpleFeatureBuilder build = new SimpleFeatureBuilder(layer.getSchema());
		SimpleFeature feature = build.buildFeature("100000", new Object[] { geometry, "Tsjakamaka", 342 });

		Object created = layer.create(feature);
		Assert.assertNotNull(created);
	}

	@Test
	public void testDelete() throws Exception {
		SimpleFeature f = (SimpleFeature) layer.read(LAYER_NAME + ".4"); // id always starts with layer id
		Assert.assertNotNull(f);
		layer.delete(LAYER_NAME + ".4"); // id always starts with layer id
		Assert.assertTrue(true);
	}

	@Test
	public void testGetBounds() throws Exception {
		Envelope bbox = layer.getBounds();
		Assert.assertEquals(-175.22, bbox.getMinX(), .01);
		Assert.assertEquals(179.21, bbox.getMaxX(), .01);
		Assert.assertEquals(-41.29, bbox.getMinY(), .01);
		Assert.assertEquals(64.15, bbox.getMaxY(), .01);
	}

	@Test
	public void testGetBoundsFilter() throws Exception {
		Envelope bbox = layer.getBounds(filter);
		Assert.assertEquals(-122.34, bbox.getMinX(), .01);
		Assert.assertEquals(151.18, bbox.getMaxX(), .01);
		Assert.assertEquals(-37.81, bbox.getMinY(), .01);
		Assert.assertEquals(55.75, bbox.getMaxY(), .01);
	}

	@Test
	public void testGetElements() throws Exception {
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int counter = 0;
		while (it.hasNext()) {
			it.next();
			counter++;
		}
		Assert.assertEquals(198, counter);
	}
}