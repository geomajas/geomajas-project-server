package org.geomajas.internal.service;

import junit.framework.Assert;

import org.geomajas.layer.LayerType;
import org.geomajas.service.DtoConverterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Test for attribute converter.
 * 
 * @author Jan De Moerloose
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "/org/geomajas/spring/moreContext.xml" })
public class LayerTypeConverterTest {

	@Autowired
	private DtoConverterService converterService;

	@Test
	public void testConversion() {
		// dto -> internal
		for (LayerType layerType : LayerType.values()) {
			if (layerType != LayerType.RASTER) {
				Class<? extends Geometry> c = converterService.toInternal(layerType);
				Assert.assertEquals(layerType.name(), c.getSimpleName().toUpperCase());
			} else {
				Assert.assertNull(converterService.toInternal(layerType));
			}
		}
		// internal -> dto
		Assert.assertEquals(LayerType.POINT, converterService.toDto(Point.class));
		Assert.assertEquals(LayerType.MULTIPOINT, converterService.toDto(MultiPoint.class));
		Assert.assertEquals(LayerType.LINESTRING, converterService.toDto(LineString.class));
		Assert.assertEquals(LayerType.MULTILINESTRING, converterService.toDto(MultiLineString.class));
		Assert.assertEquals(LayerType.POLYGON, converterService.toDto(Polygon.class));
		Assert.assertEquals(LayerType.MULTIPOLYGON, converterService.toDto(MultiPolygon.class));
		Assert.assertEquals(LayerType.GEOMETRY, converterService.toDto(Geometry.class));
	}
}
