package org.geomajas.layermodel.bean;

import java.util.Calendar;

import org.geomajas.layer.LayerException;
import org.geomajas.layer.LayerModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/beanContext.xml", "/org/geomajas/testdata/layerBeans.xml" })
public class BeanLayerModelTest {

	@Autowired
	@Qualifier("beansModel")
	private LayerModel model;

	@Test
	public void readPrimitives() throws LayerException {
		Object bean = model.read("1");
		Assert.assertEquals(true, model.getFeatureModel().getAttribute(bean, "booleanAttr"));
		Assert.assertEquals("100,23", model.getFeatureModel().getAttribute(bean, "currencyAttr"));
		Calendar c = Calendar.getInstance();
		c.set(2010, 1, 23, 0, 0, 0);
		c.set(Calendar.MILLISECOND, 0);
		Assert.assertEquals(c.getTime(), model.getFeatureModel().getAttribute(bean, "dateAttr"));
		Assert.assertEquals(123.456, model.getFeatureModel().getAttribute(bean, "doubleAttr"));
		Assert.assertEquals(456.789F, model.getFeatureModel().getAttribute(bean, "floatAttr"));
		Assert.assertEquals("http://www.geomajas.org/image1", model.getFeatureModel()
				.getAttribute(bean, "imageUrlAttr"));
		Assert.assertEquals(789, model.getFeatureModel().getAttribute(bean, "integerAttr"));
		Assert.assertEquals(123456789L, model.getFeatureModel().getAttribute(bean, "longAttr"));
		Assert.assertEquals((short) 123, model.getFeatureModel().getAttribute(bean, "shortAttr"));
		Assert.assertEquals("bean1", model.getFeatureModel().getAttribute(bean, "stringAttr"));
		Assert.assertEquals("http://www.geomajas.org/url1", model.getFeatureModel().getAttribute(bean, "urlAttr"));
	}

	@Test
	public void readGeometry() throws LayerException {
		Object bean = model.read("1");
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);
		LinearRing shell = factory.createLinearRing(new Coordinate[] { new Coordinate(0, 0), new Coordinate(1, 0),
				new Coordinate(1, 1), new Coordinate(0, 1), new Coordinate(0, 0), });
		Polygon p = factory.createPolygon(shell, null);
		MultiPolygon expected =factory.createMultiPolygon(new Polygon[]{p});
		Geometry g = model.getFeatureModel().getGeometry(bean);
		Assert.assertTrue(expected.equalsExact(g, 0.00001));

	}
}
