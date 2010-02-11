/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.layer.geotools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.GeometricAttributeInfo;
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

public class GeoToolsLayerTest extends AbstractGeoToolsTest {

	private static final String SHAPE_FILE = "classpath:org/geomajas/testdata/shapes/cities_world/cities.shp";

	private GeoToolsLayer layer;

	private Filter filter;

	@Before
	public void init() throws Exception {
		layer = (GeoToolsLayer) applicationContext.getBean("test", VectorLayer.class);
		layer.setUrl(SHAPE_FILE);

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

		layer.setLayerInfo(layerInfo);
		filter = filterCreator.createCompareFilter("Population", ">", "49900");
	}

	@Test
	public void testRead() throws Exception {
		SimpleFeature f = (SimpleFeature) layer.read("9703");
		Assert.assertEquals("Elmhurst", f.getAttribute("City"));
		Assert.assertEquals(45060, f.getAttribute("Population"));
	}

	@Test
	public void testUpdate() throws Exception {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleFeature f = (SimpleFeature) layer.read("10");
		f.setAttribute("City", sdf.format(cal.getTime()));
		layer.update(f);
		Assert.assertEquals(sdf.format(cal.getTime()), f.getAttribute("City"));
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
		SimpleFeature f = (SimpleFeature) layer.read("10580");
		Assert.assertNotNull(f);
		layer.delete("10580");
		Assert.assertTrue(true);
	}

	@Test
	public void testGetBounds() throws Exception {
		// Checked in QGis!
		Envelope bbox = layer.getBounds();
		Assert.assertEquals(-175.22, bbox.getMinX(), .0001);
		Assert.assertEquals(179.38, bbox.getMaxX(), .0001);
		Assert.assertEquals(-46.41, bbox.getMinY(), .0001);
		Assert.assertEquals(69.41, bbox.getMaxY(), .0001);
	}

	@Test
	public void testGetBoundsFilter() throws Exception {
		// Checked in QGis!
		Envelope bbox = layer.getBounds(filter);
		Assert.assertEquals(-118.01, bbox.getMinX(), .0001);
		Assert.assertEquals(120.86, bbox.getMaxX(), .0001);
		Assert.assertEquals(-19.99, bbox.getMinY(), .0001);
		Assert.assertEquals(53.09, bbox.getMaxY(), .0001);
	}

	@Test
	public void testGetElements() throws Exception {
		// Checked in QGis!
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int counter = 0;
		while (it.hasNext()) {
			it.next();
			counter++;
		}
		Assert.assertEquals(16, counter);
	}
}