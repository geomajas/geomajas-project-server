/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.smartgwt.client.spatial.geometry;

import junit.framework.Assert;

import org.geomajas.geometry.Coordinate;
import org.geomajas.smartgwt.client.spatial.geometry.GeometryFactory;
import org.geomajas.smartgwt.client.spatial.geometry.Point;
import org.junit.Test;

/**
 * Tests WKT generation.
 * 
 * @author Jan De Moerloose
 * 
 */
public class GeometryToWktTest {

	@Test
	public void testPoint() {
		GeometryFactory factory = new GeometryFactory(4326, 5);
		Point point = factory.createPoint(new Coordinate(30, 10));
		Assert.assertEquals("POINT (30.0 10.0)", point.toWkt());
	}
	
	

}
