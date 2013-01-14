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

package org.geomajas.plugin.editing.client.service;

import junit.framework.Assert;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;
import org.geomajas.plugin.editing.client.service.GeometryIndexService;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;
import org.junit.Test;

/**
 * Test cases for the parsing and formatting options of the {@link GeometryIndexService}.
 * 
 * @author Pieter De Graef
 */
public class GeometryIndexServiceParseFormatTest {

	private GeometryIndexService service = new GeometryIndexService();

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

	public GeometryIndexServiceParseFormatTest() {
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
	public void testFormat() {
		GeometryIndex index = service.create(GeometryIndexType.TYPE_VERTEX, 1, 2, 3);
		Assert.assertEquals("geometry1.geometry2.vertex3", service.format(index));

		index = service.create(GeometryIndexType.TYPE_EDGE, 1, 2, 3);
		Assert.assertEquals("geometry1.geometry2.edge3", service.format(index));

		index = service.create(GeometryIndexType.TYPE_GEOMETRY, 1, 2, 3);
		Assert.assertEquals("geometry1.geometry2.geometry3", service.format(index));

		index = service.create(GeometryIndexType.TYPE_GEOMETRY, 0);
		Assert.assertEquals("geometry0", service.format(index));
	}

	@Test
	public void testParse() throws GeometryIndexNotFoundException {
		try {
			service.parse(null);
			Assert.fail();
		} catch (NullPointerException e) {
			// We expect this exception.
		}

		GeometryIndex index = service.parse("geometry1.geometry2.vertex3");
		Assert.assertNotNull(index);
		Assert.assertTrue(index.hasChild());
		Assert.assertEquals(GeometryIndexType.TYPE_GEOMETRY, index.getType());
		Assert.assertEquals(1, index.getValue());
		Assert.assertTrue(index.getChild().hasChild());
		Assert.assertEquals(GeometryIndexType.TYPE_GEOMETRY, index.getChild().getType());
		Assert.assertEquals(2, index.getChild().getValue());
		Assert.assertFalse(index.getChild().getChild().hasChild());
		Assert.assertEquals(GeometryIndexType.TYPE_VERTEX, index.getChild().getChild().getType());
		Assert.assertEquals(3, index.getChild().getChild().getValue());

		index = service.parse("edge3");
		Assert.assertNotNull(index);
		Assert.assertFalse(index.hasChild());
		Assert.assertEquals(GeometryIndexType.TYPE_EDGE, index.getType());
		Assert.assertEquals(3, index.getValue());

		index = service.parse("foobar.geometry1.geometry2");
		Assert.assertNotNull(index);
		Assert.assertTrue(index.hasChild());
		Assert.assertEquals(GeometryIndexType.TYPE_GEOMETRY, index.getType());
		Assert.assertEquals(1, index.getValue());
		Assert.assertFalse(index.getChild().hasChild());
		Assert.assertEquals(GeometryIndexType.TYPE_GEOMETRY, index.getChild().getType());
		Assert.assertEquals(2, index.getChild().getValue());

		try {
			service.parse("foobar.geometry1a");
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect this exception.
		}

		index = service.parse("GEOMETRY1.VERTEX2");
		Assert.assertNotNull(index);
		Assert.assertTrue(index.hasChild());
		Assert.assertEquals(GeometryIndexType.TYPE_GEOMETRY, index.getType());
		Assert.assertEquals(1, index.getValue());
		Assert.assertFalse(index.getChild().hasChild());
		Assert.assertEquals(GeometryIndexType.TYPE_VERTEX, index.getChild().getType());
		Assert.assertEquals(2, index.getChild().getValue());

		service.parse("geometry1.vertex2.edge3");
		Assert.assertNotNull(index);
		Assert.assertTrue(index.hasChild());
		Assert.assertEquals(GeometryIndexType.TYPE_GEOMETRY, index.getType());
		Assert.assertEquals(1, index.getValue());
		Assert.assertFalse(index.getChild().hasChild());
		Assert.assertEquals(GeometryIndexType.TYPE_VERTEX, index.getChild().getType());
		Assert.assertEquals(2, index.getChild().getValue());
	}
}
