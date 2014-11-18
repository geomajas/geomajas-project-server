package org.geomajas.command.geometry;

import java.util.List;

import junit.framework.Assert;

import org.geotools.geometry.jts.WKTReader2;
import org.junit.Test;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

public class GeometrySplitCommandTest {

	private WKTReader2 reader = new WKTReader2();

	@Test
	public void testPolygon() throws Exception {
		Geometry polygon = reader.read("POLYGON((0 0,1 0,1 1,0 1,0 0),(0.1 0.1,0.9 0.1,0.9 0.9,0.1 0.9,0.1 0.1))");
		Geometry line = reader.read("LINESTRING(0 0,1 1)");
		List<Geometry> result = new GeometrySplitCommand().split(polygon, line);
		Assert.assertEquals(2, result.size());
		MultiPolygon m = polygon.getFactory().createMultiPolygon(result.toArray(new Polygon[0]));
		Assert.assertEquals(m.norm().toText(), "MULTIPOLYGON (((0 0, 0 1, 1 1, 0.9 0.9, 0.1 0.9, 0.1 0.1, 0 0)), "
				+ "((0 0, 0.1 0.1, 0.9 0.1, 0.9 0.9, 1 1, 1 0, 0 0)))");
	}

	@Test
	public void testMultiLineString() throws Exception {
		Geometry multiline = reader.read("LINESTRING (0 0,1 0,1 0,0 1,0 1,1 1)");
		Geometry line = reader.read("LINESTRING(0 0,1 1)");
		List<Geometry> result = new GeometrySplitCommand().split(multiline, line);
		Assert.assertEquals(2, result.size());
		MultiLineString m = multiline.getFactory().createMultiLineString(result.toArray(new LineString[0]));
		Assert.assertEquals(m.norm().toText(), "MULTILINESTRING ((0 0, 1 0, 0.5 0.5), (0.5 0.5, 0 1, 1 1))");
	}
}
