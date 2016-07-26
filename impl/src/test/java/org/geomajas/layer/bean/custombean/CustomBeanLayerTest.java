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

package org.geomajas.layer.bean.custombean;

import junit.framework.Assert;

import org.geomajas.layer.LayerException;
import org.geomajas.layer.VectorLayer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * Test for the bean layer with a custom bean, proof for fix of MAJ-968
 * 
 * @author Balder Van Camp
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/layer/bean/customBeanContext.xml", "/org/geomajas/layer/bean/customLayerBeans.xml" })
public class CustomBeanLayerTest {

	@Autowired
	@Qualifier("beans")
	private VectorLayer layer;

	@Before
	public void setupBeans() throws LayerException {
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4329);
		LinearRing shell = factory.createLinearRing(new Coordinate[] { new Coordinate(0, 0), new Coordinate(1, 0),
				new Coordinate(1, 1), new Coordinate(0, 1), new Coordinate(0, 0), });
		Polygon p = factory.createPolygon(shell, null);
		MultiPolygon expected = factory.createMultiPolygon(new Polygon[] { p });
		CustomBean cb = new CustomBean();
		cb.setId(1);
		cb.setGeometry(expected);
		cb.setName("testbean");
		layer.saveOrUpdate(cb);
	}

	@Test
	public void readGeometry() throws LayerException {
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);
		LinearRing shell = factory.createLinearRing(new Coordinate[] { new Coordinate(0, 0), new Coordinate(1, 0),
				new Coordinate(1, 1), new Coordinate(0, 1), new Coordinate(0, 0), });
		Polygon p = factory.createPolygon(shell, null);
		MultiPolygon expected = factory.createMultiPolygon(new Polygon[] { p });
		Assert.assertTrue(((CustomBean) layer.read("1")).getGeometry().equalsExact(expected));
	}

	public void readAttributes() throws LayerException {
		CustomBean cb = ((CustomBean) layer.read("1"));
		Assert.assertEquals(cb.getName(), "testbean");
	}
}
