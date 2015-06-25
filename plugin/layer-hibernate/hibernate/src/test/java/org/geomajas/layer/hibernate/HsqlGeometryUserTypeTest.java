package org.geomajas.layer.hibernate;

import junit.framework.Assert;

import org.junit.Test;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class HsqlGeometryUserTypeTest {

	private WKTReader reader = new WKTReader();

	@Test
	public void testConversion() throws ParseException {

		Geometry geom = reader.read("POLYGON ((0 0, 1 0, 1 1, 0 1, 0 0))");
		HsqlGeometryUserType type = new HsqlGeometryUserType();
		geom.setSRID(12345);
		String wkt = (String) type.conv2DBGeometry(geom, null);
		Geometry copy = type.convert2JTS(wkt);
		Assert.assertEquals(geom, copy);

	}
}
