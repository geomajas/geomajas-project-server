package org.geomajas.layer.geotools;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.Assert;

import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.service.FilterService;
import org.geomajas.spring.ThreadScopeContextHolder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "/org/geomajas/testdata/allowAll.xml",
		"/org/geomajas/layer/geotools/layerPoint.xml" })
@Transactional
public class GeoDbTest {

	@Autowired
	private GeoToolsLayer pointLayer;

	@Autowired
	private FilterService filterService;
	
	@Before
	public void before() {
		ThreadScopeContextHolder.clear();
	}

	@Test
	public void testRead() throws GeomajasException {
		Iterator<?> it = pointLayer.getElements(Filter.INCLUDE, 0, 0);
		Map<String, SimpleFeature> all = new HashMap<String, SimpleFeature>();
		while (it.hasNext()) {
			SimpleFeature f = (SimpleFeature) it.next();
			all.put(f.getID(), f);
		}
		Assert.assertEquals(4, all.size());
		for (int i = 1; i <= 4; i++) {
			Assert.assertEquals("point" + i, all.get("POINT." + i).getAttribute("NAME"));
		}
	}

	@Test
	public void testCreate() throws GeomajasException, ParseException {
		// test creating a 5th geometry, expect auto-generated id
		WKTReader wktReader = new WKTReader();
		Point geometry = (Point) wktReader.read("POINT (0 0)");
		SimpleFeatureBuilder build = new SimpleFeatureBuilder(pointLayer.getSchema());
		SimpleFeature feature = build.buildFeature(null, new Object[] { "point5", geometry });
		SimpleFeature created = (SimpleFeature) pointLayer.create(feature);
		Assert.assertNotNull(created);
		Assert.assertEquals("POINT.5", created.getID());
	}

	@Test
	public void testDelete() throws GeomajasException, ParseException {
		pointLayer.delete("POINT.4");
		try {
			pointLayer.read("POINT.4");
		} catch (GeomajasException e) {
			Assert.assertEquals(ExceptionCode.LAYER_MODEL_FEATURE_NOT_FOUND, e.getExceptionCode());
		}
	}

	@Test
	public void testUpdate() throws GeomajasException, ParseException {
		// test updating geometry and name
		WKTReader wktReader = new WKTReader();
		Point geometry = (Point) wktReader.read("POINT (100 0)");
		SimpleFeatureBuilder build = new SimpleFeatureBuilder(pointLayer.getSchema());
		SimpleFeature feature = build.buildFeature("POINT.4", new Object[] { "point44", geometry });
		pointLayer.update(feature);
		SimpleFeature point4 = (SimpleFeature) pointLayer.read("POINT.4");
		Assert.assertEquals("point44", point4.getAttribute("NAME"));
		Assert.assertNotSame(geometry, point4.getDefaultGeometry());
		Assert.assertTrue(geometry.equalsTopo((Geometry) point4.getDefaultGeometry()));
	}

	@Test
	public void testBbox() throws LayerException {
		Iterator<?> it = pointLayer.getElements(
				filterService.createBboxFilter("EPSG:4326", new Envelope(-0.5, 1.5, -0.5, 0.5), "GEOM"), 0, 0);
		Map<String, SimpleFeature> filtered = new HashMap<String, SimpleFeature>();
		while (it.hasNext()) {
			SimpleFeature f = (SimpleFeature) it.next();
			filtered.put(f.getID(), f);
		}
		Assert.assertEquals(2, filtered.size());
		Assert.assertTrue(filtered.containsKey("POINT.1"));		
		Assert.assertTrue(filtered.containsKey("POINT.2"));		
	}
	

}
