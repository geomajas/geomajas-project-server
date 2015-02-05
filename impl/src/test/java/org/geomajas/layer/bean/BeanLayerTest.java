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

package org.geomajas.layer.bean;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.geomajas.global.ExceptionCode;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerAssociationSupport;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.service.FilterService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * Test for the bean layer.
 *
 * @author Jan De Moerloose
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/beanContext.xml", "/org/geomajas/testdata/layerBeans.xml"})
public class BeanLayerTest {

	@Autowired
	@Qualifier("beans")
	private VectorLayer layer;

	@Autowired
	protected FilterService filterService;

	@Test
	public void readPrimitives() throws LayerException {
		Object bean = layer.read("1");
		Assert.assertEquals("bean1", layer.getFeatureModel().getAttribute(bean, "stringAttr").getValue());
		Assert.assertEquals(true, layer.getFeatureModel().getAttribute(bean, "booleanAttr").getValue());
		Assert.assertEquals("100,23", layer.getFeatureModel().getAttribute(bean, "currencyAttr").getValue());
		Calendar c = Calendar.getInstance();
		c.set(2010, 1, 23, 0, 0, 0);
		c.set(Calendar.MILLISECOND, 0);
		Assert.assertEquals(c.getTime(), layer.getFeatureModel().getAttribute(bean, "dateAttr").getValue());
		Assert.assertEquals(123.456, layer.getFeatureModel().getAttribute(bean, "doubleAttr").getValue());
		Assert.assertEquals(456.789F, layer.getFeatureModel().getAttribute(bean, "floatAttr").getValue());
		Assert.assertEquals("http://www.geomajas.org/image1", layer.getFeatureModel()
				.getAttribute(bean, "imageUrlAttr").getValue());
		Assert.assertEquals(789, layer.getFeatureModel().getAttribute(bean, "integerAttr").getValue());
		Assert.assertEquals(123456789L, layer.getFeatureModel().getAttribute(bean, "longAttr").getValue());
		Assert.assertEquals((short) 123, layer.getFeatureModel().getAttribute(bean, "shortAttr").getValue());
		Assert.assertEquals("http://www.geomajas.org/url1", layer.getFeatureModel().getAttribute(bean, "urlAttr").
				getValue());
	}

	@Test
	public void readGeometry() throws LayerException {
		Object bean = layer.read("1");
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);
		LinearRing shell = factory.createLinearRing(new Coordinate[] { new Coordinate(0, 0), new Coordinate(1, 0),
				new Coordinate(1, 1), new Coordinate(0, 1), new Coordinate(0, 0), });
		Polygon p = factory.createPolygon(shell, null);
		MultiPolygon expected =factory.createMultiPolygon(new Polygon[]{p});
		Geometry g = layer.getFeatureModel().getGeometry(bean);
		Assert.assertTrue(expected.equalsExact(g, 0.00001));
	}

	@Test
	public void simpleFilter() throws Exception {
		Filter filter = filterService.createCompareFilter("stringAttr", "=", "bean2");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue(it.next() instanceof FeatureBean);
			t++;
		}
		Assert.assertEquals(1, t);
	}

	@Test
	public void nonExistingAttributeFilter() throws Exception {
		Filter filter = filterService.createCompareFilter("noSuchAttr", "=", "blabla");
		try {
			layer.getElements(filter, 0, 0);
			Assert.fail("Expected filter evaluation exception for non-exisitng attribute");
		} catch (LayerException e) {
			Assert.assertEquals(ExceptionCode.FILTER_EVALUATION_PROBLEM, e.getExceptionCode());
		}
	}

	@Test
	public void manyToOneFilter() throws Exception {
		Filter filter = filterService.parseFilter("manyToOneAttr.stringAttr='manyToOne - bean1'");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue(it.next() instanceof FeatureBean);
			t++;
		}
		Assert.assertEquals(1, t);
	}

	@Test
	public void manyToOneIdFilter() throws Exception {
		Filter filter = filterService.parseFilter("manyToOneAttr.id=1");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue(it.next() instanceof FeatureBean);
			t++;
		}
		Assert.assertEquals(1, t);
	}

	@Test
	public void manyToOneEcqlIdFilter() throws Exception {
		Filter filter = filterService.parseFilter("manyToOneAttr.\"id\"=1");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue(it.next() instanceof FeatureBean);
			t++;
		}
		Assert.assertEquals(1, t);
	}

	@Test
	public void functionFilter() throws Exception {
		Filter filter = filterService.parseFilter("geometryType(geometry)='Point'");
		Iterator<?> it = layer.getElements(filter, 0, 0);
		int t = 0;
		while (it.hasNext()) {
			Assert.assertTrue(it.next() instanceof FeatureBean);
			t++;
		}
		Assert.assertEquals(0, t);
		filter = filterService.parseFilter("geometryType(geometry)='MultiPolygon'");
		it = layer.getElements(filter, 0, 0);
		t = 0;
		while (it.hasNext()) {
			Assert.assertTrue(it.next() instanceof FeatureBean);
			t++;
		}
		Assert.assertEquals(3, t);
	}

	@Test
	public void testGetAttributes() throws Exception {
		// this stuff is very much hardcoded to work only for this specific field
		List<Attribute<?>> attributes = ((VectorLayerAssociationSupport) layer).getAttributes("manyToOneAttr", null);
		Assert.assertNotNull(attributes);
		Assert.assertEquals(2, attributes.size());
	}

	@Test
	@DirtiesContext // adding a bean
	public void testCreateNullId() throws Exception {
		Assert.assertEquals(3, count(layer.getElements(null, 0, 0)));
		layer.saveOrUpdate(new FeatureBean());
		Assert.assertEquals(4, count(layer.getElements(null, 0, 0)));
		Assert.assertNotNull(layer.read("4")); // should be assigned id 4
	}

	private int count(Iterator it) {
		int count = 0;
		while (it.hasNext()) {
			count++;
			it.next();
		}
		return count;
	}
}
