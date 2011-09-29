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

package org.geomajas.plugin.editing.client.service;

import junit.framework.Assert;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexService;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;
import org.geomajas.plugin.editing.client.service.impl.GeometryIndexServiceImpl;
import org.junit.Test;

/**
 * Test cases for the parsing and formatting options of the {@link GeometryIndexService}.
 * 
 * @author Pieter De Graef
 */
public class GeometryIndexServiceGeneralTest {

	private GeometryIndexService service = new GeometryIndexServiceImpl();

	private Geometry point = new Geometry(Geometry.POINT, 0, 0);

	private Geometry lineString = new Geometry(Geometry.LINE_STRING, 0, 0);

	private Geometry linearRing = new Geometry(Geometry.LINEAR_RING, 0, 0);

	private Geometry polygon = new Geometry(Geometry.POLYGON, 0, 0);

	private Geometry multiPoint = new Geometry(Geometry.MULTI_POINT, 0, 0);

	private Geometry multiLineString = new Geometry(Geometry.MULTI_LINE_STRING, 0, 0);

	private Geometry multiPolygon = new Geometry(Geometry.MULTI_POLYGON, 0, 0);

	// ------------------------------------------------------------------------
	// Constructor: initialize geometries.
	// ------------------------------------------------------------------------

	public GeometryIndexServiceGeneralTest() {
		point.setCoordinates(new Coordinate[] { new Coordinate(1, 1) });
		lineString
				.setCoordinates(new Coordinate[] { new Coordinate(1, 1), new Coordinate(2, 2), new Coordinate(3, 3) });
		linearRing.setCoordinates(new Coordinate[] { new Coordinate(1, 1), new Coordinate(2, 2), new Coordinate(3, 3),
				new Coordinate(1, 1) });

		Geometry shell = new Geometry(Geometry.LINEAR_RING, 0, 0);
		shell.setCoordinates(new Coordinate[] { new Coordinate(0, 0), new Coordinate(10, 0), new Coordinate(10, 10),
				new Coordinate(0, 10), new Coordinate(0, 0) });
		Geometry hole = new Geometry(Geometry.LINEAR_RING, 0, 0);
		hole.setCoordinates(new Coordinate[] { new Coordinate(4, 4), new Coordinate(6, 4), new Coordinate(6, 6),
				new Coordinate(4, 6), new Coordinate(4, 4) });
		polygon.setGeometries(new Geometry[] { shell, hole });

		Geometry point2 = new Geometry(Geometry.POINT, 0, 0);
		point2.setCoordinates(new Coordinate[] { new Coordinate(2, 2) });
		multiPoint.setGeometries(new Geometry[] { point, point2 });

		Geometry lineString2 = new Geometry(Geometry.LINE_STRING, 0, 0);
		lineString2.setCoordinates(new Coordinate[] { new Coordinate(4, 4), new Coordinate(5, 5), new Coordinate(6, 6),
				new Coordinate(7, 7) });
		multiLineString.setGeometries(new Geometry[] { lineString, lineString2 });

		Geometry shell2 = new Geometry(Geometry.LINEAR_RING, 0, 0);
		shell.setCoordinates(new Coordinate[] { new Coordinate(0, 0), new Coordinate(10, 0), new Coordinate(10, 10),
				new Coordinate(0, 10), new Coordinate(0, 0) });
		Geometry hole2 = new Geometry(Geometry.LINEAR_RING, 0, 0);
		hole.setCoordinates(new Coordinate[] { new Coordinate(3, 3), new Coordinate(7, 3), new Coordinate(7, 7),
				new Coordinate(3, 7), new Coordinate(3, 3) });
		Geometry polygon2 = new Geometry(Geometry.POLYGON, 0, 0);
		polygon2.setGeometries(new Geometry[] { shell2, hole2 });
		multiPolygon.setGeometries(new Geometry[] { polygon, polygon2 });
	}

	// ------------------------------------------------------------------------
	// Test the GeometryIndexService.
	// ------------------------------------------------------------------------

	@Test
	public void testSiblingCount() {
		GeometryIndex index = service.create(GeometryIndexType.TYPE_VERTEX, 0);
		Assert.assertEquals(lineString.getCoordinates().length, service.getSiblingCount(lineString, index));
		index = service.create(GeometryIndexType.TYPE_EDGE, 0);
		Assert.assertEquals(lineString.getCoordinates().length - 1, service.getSiblingCount(lineString, index));
		index = service.create(GeometryIndexType.TYPE_GEOMETRY, 0);
		Assert.assertEquals(0, service.getSiblingCount(lineString, index));
	}
}