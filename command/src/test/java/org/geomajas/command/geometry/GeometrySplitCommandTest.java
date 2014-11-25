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
	
	@Test
	public void testMultiLineString2() throws Exception {
		Geometry multiline = reader.read("LINESTRING (3277730.9995999997 5894141.5677, 3277734.6 5894137.6, 3277743.4 5894125.5,"
				+ " 3277754.17 5894116, 3277765.76 5894108.41, 3277790.26 5894103.5, 3277836.3951 5894109.8266,"
				+ " 3277902.6163999997 5894100.4557, 3277980.51 5894092.78, 3278029.53 5894095.91, 3278066.5 5894107.04,"
				+ " 3278070.5464 5894108.034499999, 3278085.5193 5894111.7144)");
				
		Geometry line = reader.read("LINESTRING (3277910.986003672 5894154.221340268, 3277889.4924864867 5894049.141922917)");
		
		List<Geometry> result = new GeometrySplitCommand().split(multiline, line);
		Assert.assertEquals(2, result.size());
		MultiLineString m = multiline.getFactory().createMultiLineString(result.toArray(new LineString[0]));
		Assert.assertEquals(m.norm().toText(), "MULTILINESTRING ((3277730.9995999997 5894141.5677, 3277734.6 5894137.6,"
				+ " 3277743.4 5894125.5, 3277754.17 5894116, 3277765.76 5894108.41,"
				+ " 3277790.26 5894103.5, 3277836.3951 5894109.8266, 3277900.0624115206 5894100.817111972),"
				+ " (3277900.0624115206 5894100.817111972, 3277902.6163999997 5894100.4557, 3277980.51 5894092.78,"
				+ " 3278029.53 5894095.91, 3278066.5 5894107.04, 3278070.5464 5894108.034499999, 3278085.5193 5894111.7144))");
	}
}
