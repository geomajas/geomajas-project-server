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

package org.geomajas.plugin.editing.client.snap;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.snap.algorithm.NearestVertexSnapAlgorithm;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the {@link NearestVertexSnapAlgorithm}.
 * 
 * @author Pieter De Graef
 */
public class NearestVertexAlgorithmTest {

	private static final double DELTA = 0.0001;

	private NearestVertexSnapAlgorithm algorithm = new NearestVertexSnapAlgorithm();

	@Test
	public void testPoints() {
		Geometry point1 = new Geometry(Geometry.POINT, 0, 0);
		point1.setCoordinates(new Coordinate[] { new Coordinate(0, 0) });
		Geometry point2 = new Geometry(Geometry.POINT, 0, 0);
		point2.setCoordinates(new Coordinate[] { new Coordinate(10, 10) });
		algorithm.setGeometries(new Geometry[] { point1, point2 });

		Coordinate result = algorithm.snap(new Coordinate(1, 1), 1);
		Assert.assertEquals(1.0, result.getX(), DELTA);
		Assert.assertEquals(1.0, result.getY(), DELTA);

		result = algorithm.snap(new Coordinate(1, 1), 2);
		Assert.assertEquals(0.0, result.getX(), DELTA);
		Assert.assertEquals(0.0, result.getY(), DELTA);

		result = algorithm.snap(new Coordinate(9, 9), 2);
		Assert.assertEquals(10.0, result.getX(), DELTA);
		Assert.assertEquals(10.0, result.getY(), DELTA);

		result = algorithm.snap(new Coordinate(5, 5), 8);
		Assert.assertEquals(0.0, result.getX(), DELTA);
		Assert.assertEquals(0.0, result.getY(), DELTA);
	}

	@Test
	public void testLineString() {
		Geometry line1 = new Geometry(Geometry.LINE_STRING, 0, 0);
		line1.setCoordinates(new Coordinate[] { new Coordinate(0, 0), new Coordinate(10, 0) });
		Geometry line2 = new Geometry(Geometry.LINE_STRING, 0, 0);
		line2.setCoordinates(new Coordinate[] { new Coordinate(0, 10), new Coordinate(10, 10) });
		algorithm.setGeometries(new Geometry[] { line1, line2 });

		Coordinate result = algorithm.snap(new Coordinate(1, 1), 1);
		Assert.assertEquals(1.0, result.getX(), DELTA);
		Assert.assertEquals(1.0, result.getY(), DELTA);

		result = algorithm.snap(new Coordinate(1, 1), 2);
		Assert.assertEquals(0.0, result.getX(), DELTA);
		Assert.assertEquals(0.0, result.getY(), DELTA);

		result = algorithm.snap(new Coordinate(9, 9), 2);
		Assert.assertEquals(10.0, result.getX(), DELTA);
		Assert.assertEquals(10.0, result.getY(), DELTA);

		result = algorithm.snap(new Coordinate(5, 5), 8);
		Assert.assertEquals(0.0, result.getX(), DELTA);
		Assert.assertEquals(0.0, result.getY(), DELTA);
	}
}