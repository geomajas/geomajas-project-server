/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.dojo.server.json;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

import org.junit.Assert;
import org.junit.Test;

/**
 * Centroid test.
 *
 * @author Pieter De Graef
 */
public class CentroidTest {

	@Test
	public void testSinglePointString() {
		GeometryFactory factory = new GeometryFactory();
		LineString ls = factory.createLineString(new Coordinate[] {new Coordinate(0, 0),
				new Coordinate(0, 0)});
		com.vividsolutions.jts.geom.Point p = ls.getCentroid();
		Assert.assertTrue(Double.isNaN(p.getX()));
	}
}
 