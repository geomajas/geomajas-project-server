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

import java.util.List;

import junit.framework.Assert;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;
import org.geomajas.plugin.editing.client.service.GeometryIndexService;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;
import org.junit.Test;

/**
 * Test cases testing the adjacency methods in the {@link GeometryIndexService}.
 * 
 * @author Pieter De Graef
 */
public class GeometryIndexServiceAdjacencyTest {

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

	public GeometryIndexServiceAdjacencyTest() {
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
	public void testCreateIndex() {
		final GeometryIndexType type = GeometryIndexType.TYPE_VERTEX;
		final int value1 = 1;
		final int value2 = 2;
		GeometryIndex index = service.create(type, value1, value2);

		Assert.assertEquals(index.getValue(), value1);
		Assert.assertEquals(index.getType(), GeometryIndexType.TYPE_GEOMETRY);
		Assert.assertEquals(index.hasChild(), true);
		Assert.assertEquals(index.getChild().getValue(), value2);
		Assert.assertEquals(index.getChild().getType(), type);
		Assert.assertEquals(index.getChild().hasChild(), false);

		index = service.create(GeometryIndexType.TYPE_EDGE, value1, value2);
		Assert.assertNotNull(index);
		Assert.assertTrue(index.hasChild());
		Assert.assertEquals(GeometryIndexType.TYPE_GEOMETRY, index.getType());
		Assert.assertEquals(value1, index.getValue());
		Assert.assertFalse(index.getChild().hasChild());
		Assert.assertEquals(GeometryIndexType.TYPE_EDGE, index.getChild().getType());
		Assert.assertEquals(value2, index.getChild().getValue());

		index = service.create(GeometryIndexType.TYPE_GEOMETRY, value1, value2);
		Assert.assertNotNull(index);
		Assert.assertTrue(index.hasChild());
		Assert.assertEquals(GeometryIndexType.TYPE_GEOMETRY, index.getType());
		Assert.assertEquals(value1, index.getValue());
		Assert.assertFalse(index.getChild().hasChild());
		Assert.assertEquals(GeometryIndexType.TYPE_GEOMETRY, index.getChild().getType());
		Assert.assertEquals(value2, index.getChild().getValue());
	}

	@Test
	public void testAdjacentVertices() throws GeometryIndexNotFoundException {
		GeometryIndex index = service.create(GeometryIndexType.TYPE_VERTEX, 0);
		try {
			service.getAdjacentVertices(null, index);
			Assert.fail();
		} catch (Exception e) {
			// We expect this exception.
		}
		try {
			service.getAdjacentVertices(point, null);
			Assert.fail();
		} catch (Exception e) {
			// We expect this exception.
		}
		try {
			service.getAdjacentVertices(point, service.create(GeometryIndexType.TYPE_VERTEX, 0, 1));
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect this exception.
		}
		try {
			service.getAdjacentVertices(polygon, service.create(GeometryIndexType.TYPE_VERTEX, 1));
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect this exception.
		}
	}

	@Test
	public void testAdjacentVerticesForPoint() throws GeometryIndexNotFoundException {
		GeometryIndex index = service.create(GeometryIndexType.TYPE_VERTEX, 0);
		try {
			service.getAdjacentVertices(point, index);
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect this exception.
		}
	}

	@Test
	public void testAdjacentVerticesForLineString() throws GeometryIndexNotFoundException {
		// Index = -1:
		GeometryIndex index = service.create(GeometryIndexType.TYPE_VERTEX, -1);
		try {
			service.getAdjacentVertices(lineString, index);
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect this exception.
		}

		// Index = 0:
		index = service.create(GeometryIndexType.TYPE_VERTEX, 0);
		List<GeometryIndex> neighbours = service.getAdjacentVertices(lineString, index);
		Assert.assertNotNull(neighbours);
		Assert.assertEquals(1, neighbours.size());
		Assert.assertFalse(neighbours.get(0).hasChild());
		Assert.assertEquals(1, neighbours.get(0).getValue());

		// Index = 1:
		index = service.create(GeometryIndexType.TYPE_VERTEX, 1);
		neighbours = service.getAdjacentVertices(lineString, index);
		Assert.assertNotNull(neighbours);
		Assert.assertEquals(2, neighbours.size());
		Assert.assertFalse(neighbours.get(0).hasChild());
		Assert.assertEquals(0, neighbours.get(0).getValue());
		Assert.assertEquals(2, neighbours.get(1).getValue());

		// Index = 2:
		index = service.create(GeometryIndexType.TYPE_VERTEX, 2);
		neighbours = service.getAdjacentVertices(lineString, index);
		Assert.assertNotNull(neighbours);
		Assert.assertEquals(1, neighbours.size());
		Assert.assertFalse(neighbours.get(0).hasChild());
		Assert.assertEquals(1, neighbours.get(0).getValue());

		// Index = 3:
		index = service.create(GeometryIndexType.TYPE_VERTEX, 3);
		try {
			service.getAdjacentVertices(lineString, index);
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect this exception.
		}
	}

	@Test
	public void testAdjacentVerticesForLinearRing() throws GeometryIndexNotFoundException {
		// Index = -1:
		GeometryIndex index = service.create(GeometryIndexType.TYPE_VERTEX, -1);
		try {
			service.getAdjacentVertices(linearRing, index);
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect this exception.
		}

		// Index = 0:
		index = service.create(GeometryIndexType.TYPE_VERTEX, 0);
		List<GeometryIndex> neighbours = service.getAdjacentVertices(linearRing, index);
		Assert.assertNotNull(neighbours);
		Assert.assertEquals(2, neighbours.size());
		Assert.assertFalse(neighbours.get(0).hasChild());
		Assert.assertEquals(2, neighbours.get(0).getValue());
		Assert.assertEquals(1, neighbours.get(1).getValue());

		// Index = 1:
		index = service.create(GeometryIndexType.TYPE_VERTEX, 1);
		neighbours = service.getAdjacentVertices(linearRing, index);
		Assert.assertNotNull(neighbours);
		Assert.assertEquals(2, neighbours.size());
		Assert.assertFalse(neighbours.get(0).hasChild());
		Assert.assertEquals(0, neighbours.get(0).getValue());
		Assert.assertEquals(2, neighbours.get(1).getValue());

		// Index = 2:
		index = service.create(GeometryIndexType.TYPE_VERTEX, 2);
		neighbours = service.getAdjacentVertices(linearRing, index);
		Assert.assertNotNull(neighbours);
		Assert.assertEquals(2, neighbours.size());
		Assert.assertFalse(neighbours.get(0).hasChild());
		Assert.assertEquals(1, neighbours.get(0).getValue());
		Assert.assertEquals(0, neighbours.get(1).getValue());

		// Index = 3:
		index = service.create(GeometryIndexType.TYPE_VERTEX, 3);
		try {
			service.getAdjacentVertices(linearRing, index);
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect this exception.
		}
	}

	@Test
	public void testAdjacentVerticesForPolygon() throws GeometryIndexNotFoundException {
		// Index = -1:
		GeometryIndex index = service.create(GeometryIndexType.TYPE_VERTEX, -1);
		try {
			service.getAdjacentVertices(polygon, index);
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect this exception.
		}

		// Index = 1,0:
		index = service.create(GeometryIndexType.TYPE_VERTEX, 1, 0);
		List<GeometryIndex> neighbours = service.getAdjacentVertices(polygon, index);
		Assert.assertNotNull(neighbours);
		Assert.assertEquals(2, neighbours.size());
		Assert.assertTrue(neighbours.get(0).hasChild());
		Assert.assertFalse(neighbours.get(0).getChild().hasChild());
		Assert.assertEquals(1, neighbours.get(0).getValue());
		Assert.assertEquals(1, neighbours.get(1).getValue());
		Assert.assertEquals(3, neighbours.get(0).getChild().getValue());
		Assert.assertEquals(1, neighbours.get(1).getChild().getValue());

		// Index = 1,1:
		index = service.create(GeometryIndexType.TYPE_VERTEX, 1, 1);
		neighbours = service.getAdjacentVertices(polygon, index);
		Assert.assertNotNull(neighbours);
		Assert.assertEquals(2, neighbours.size());
		Assert.assertTrue(neighbours.get(0).hasChild());
		Assert.assertFalse(neighbours.get(0).getChild().hasChild());
		Assert.assertEquals(1, neighbours.get(0).getValue());
		Assert.assertEquals(1, neighbours.get(1).getValue());
		Assert.assertEquals(0, neighbours.get(0).getChild().getValue());
		Assert.assertEquals(2, neighbours.get(1).getChild().getValue());

		// Index = 1,2:
		index = service.create(GeometryIndexType.TYPE_VERTEX, 1, 2);
		neighbours = service.getAdjacentVertices(polygon, index);
		Assert.assertNotNull(neighbours);
		Assert.assertEquals(2, neighbours.size());
		Assert.assertTrue(neighbours.get(0).hasChild());
		Assert.assertFalse(neighbours.get(0).getChild().hasChild());
		Assert.assertEquals(1, neighbours.get(0).getValue());
		Assert.assertEquals(1, neighbours.get(1).getValue());
		Assert.assertEquals(1, neighbours.get(0).getChild().getValue());
		Assert.assertEquals(3, neighbours.get(1).getChild().getValue());

		// Index = 1,3:
		index = service.create(GeometryIndexType.TYPE_VERTEX, 1, 3);
		neighbours = service.getAdjacentVertices(polygon, index);
		Assert.assertNotNull(neighbours);
		Assert.assertEquals(2, neighbours.size());
		Assert.assertTrue(neighbours.get(0).hasChild());
		Assert.assertFalse(neighbours.get(0).getChild().hasChild());
		Assert.assertEquals(1, neighbours.get(0).getValue());
		Assert.assertEquals(1, neighbours.get(1).getValue());
		Assert.assertEquals(2, neighbours.get(0).getChild().getValue());
		Assert.assertEquals(0, neighbours.get(1).getChild().getValue());
	}

	@Test
	public void testAdjacentEdges() throws GeometryIndexNotFoundException {
		GeometryIndex index = service.create(GeometryIndexType.TYPE_VERTEX, 0);
		try {
			service.getAdjacentEdges(null, index);
			Assert.fail();
		} catch (Exception e) {
			// We expect this exception.
		}
		try {
			service.getAdjacentEdges(point, null);
			Assert.fail();
		} catch (Exception e) {
			// We expect this exception.
		}
		try {
			service.getAdjacentEdges(point, service.create(GeometryIndexType.TYPE_VERTEX, 0, 1));
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect this exception.
		}
		try {
			service.getAdjacentEdges(polygon, service.create(GeometryIndexType.TYPE_VERTEX, 1));
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect this exception.
		}
	}

	@Test
	public void testAdjacentEdgesForPoint() throws GeometryIndexNotFoundException {
		GeometryIndex index = service.create(GeometryIndexType.TYPE_VERTEX, 0);
		try {
			service.getAdjacentEdges(point, index);
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect this exception.
		}
	}

	@Test
	public void testAdjacentEdgesForLineString() throws GeometryIndexNotFoundException {
		// Index = -1:
		GeometryIndex index = service.create(GeometryIndexType.TYPE_VERTEX, -1);
		try {
			service.getAdjacentEdges(lineString, index);
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect this exception.
		}

		// Index = 0:
		index = service.create(GeometryIndexType.TYPE_VERTEX, 0);
		List<GeometryIndex> neighbours = service.getAdjacentEdges(lineString, index);
		Assert.assertNotNull(neighbours);
		Assert.assertEquals(1, neighbours.size());
		Assert.assertFalse(neighbours.get(0).hasChild());
		Assert.assertEquals(0, neighbours.get(0).getValue());

		// Index = 1:
		index = service.create(GeometryIndexType.TYPE_VERTEX, 1);
		neighbours = service.getAdjacentEdges(lineString, index);
		Assert.assertNotNull(neighbours);
		Assert.assertEquals(2, neighbours.size());
		Assert.assertFalse(neighbours.get(0).hasChild());
		Assert.assertEquals(0, neighbours.get(0).getValue());
		Assert.assertEquals(1, neighbours.get(1).getValue());

		// Index = 2:
		index = service.create(GeometryIndexType.TYPE_VERTEX, 2);
		neighbours = service.getAdjacentEdges(lineString, index);
		Assert.assertNotNull(neighbours);
		Assert.assertEquals(1, neighbours.size());
		Assert.assertFalse(neighbours.get(0).hasChild());
		Assert.assertEquals(1, neighbours.get(0).getValue());

		// Index = 3:
		index = service.create(GeometryIndexType.TYPE_VERTEX, 3);
		try {
			service.getAdjacentEdges(lineString, index);
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect this exception.
		}
	}

	@Test
	public void testAdjacentEdgesForLinearRing() throws GeometryIndexNotFoundException {
		// Index = -1:
		GeometryIndex index = service.create(GeometryIndexType.TYPE_VERTEX, -1);
		try {
			service.getAdjacentEdges(linearRing, index);
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect this exception.
		}

		// Index = 0:
		index = service.create(GeometryIndexType.TYPE_VERTEX, 0);
		List<GeometryIndex> neighbours = service.getAdjacentEdges(linearRing, index);
		Assert.assertNotNull(neighbours);
		Assert.assertEquals(2, neighbours.size());
		Assert.assertFalse(neighbours.get(0).hasChild());
		Assert.assertEquals(2, neighbours.get(0).getValue());
		Assert.assertEquals(0, neighbours.get(1).getValue());

		// Index = 1:
		index = service.create(GeometryIndexType.TYPE_VERTEX, 1);
		neighbours = service.getAdjacentEdges(linearRing, index);
		Assert.assertNotNull(neighbours);
		Assert.assertEquals(2, neighbours.size());
		Assert.assertFalse(neighbours.get(0).hasChild());
		Assert.assertEquals(0, neighbours.get(0).getValue());
		Assert.assertEquals(1, neighbours.get(1).getValue());

		// Index = 2:
		index = service.create(GeometryIndexType.TYPE_VERTEX, 2);
		neighbours = service.getAdjacentEdges(linearRing, index);
		Assert.assertNotNull(neighbours);
		Assert.assertEquals(2, neighbours.size());
		Assert.assertFalse(neighbours.get(0).hasChild());
		Assert.assertEquals(1, neighbours.get(0).getValue());
		Assert.assertEquals(2, neighbours.get(1).getValue());

		// Index = 3:
		index = service.create(GeometryIndexType.TYPE_VERTEX, 3);
		neighbours = service.getAdjacentEdges(linearRing, index);
		// TODO should this be the case? We could throw an exception as well....
		Assert.assertNotNull(neighbours);
		Assert.assertEquals(2, neighbours.size());
		Assert.assertFalse(neighbours.get(0).hasChild());
		Assert.assertEquals(2, neighbours.get(0).getValue());
		Assert.assertEquals(0, neighbours.get(1).getValue());
	}

	@Test
	public void testAdjacentVerticesForPointEdges() throws GeometryIndexNotFoundException {
		GeometryIndex index = service.create(GeometryIndexType.TYPE_EDGE, 0);
		try {
			service.getAdjacentVertices(point, index);
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect this exception.
		}
	}

	/** Given an edge in a LineString, search neighboring vertices. */
	@Test
	public void testAdjacentVerticesForLineStringEdge() throws GeometryIndexNotFoundException {
		// Index = -1:
		GeometryIndex index = service.create(GeometryIndexType.TYPE_EDGE, -1);
		try {
			service.getAdjacentVertices(lineString, index);
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect this exception.
		}

		// Index = 0:
		index = service.create(GeometryIndexType.TYPE_EDGE, 0);
		List<GeometryIndex> neighbours = service.getAdjacentVertices(lineString, index);
		Assert.assertNotNull(neighbours);
		Assert.assertEquals(2, neighbours.size());
		Assert.assertFalse(neighbours.get(0).hasChild());
		Assert.assertEquals(0, neighbours.get(0).getValue());
		Assert.assertEquals(1, neighbours.get(1).getValue());

		// Index = 1:
		index = service.create(GeometryIndexType.TYPE_EDGE, 1);
		neighbours = service.getAdjacentVertices(lineString, index);
		Assert.assertNotNull(neighbours);
		Assert.assertEquals(2, neighbours.size());
		Assert.assertFalse(neighbours.get(0).hasChild());
		Assert.assertEquals(1, neighbours.get(0).getValue());
		Assert.assertEquals(2, neighbours.get(1).getValue());

		// Index = 2:
		index = service.create(GeometryIndexType.TYPE_EDGE, 2);
		try {
			service.getAdjacentVertices(lineString, index);
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect this exception.
		}
	}

	/** Given an edge in a LinearRing, search neighboring vertices. */
	@Test
	public void testAdjacentVerticesForLinearRingEdge() throws GeometryIndexNotFoundException {
		// Index = -1:
		GeometryIndex index = service.create(GeometryIndexType.TYPE_EDGE, -1);
		try {
			service.getAdjacentVertices(linearRing, index);
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect this exception.
		}

		// Index = 0:
		index = service.create(GeometryIndexType.TYPE_EDGE, 0);
		List<GeometryIndex> neighbours = service.getAdjacentVertices(linearRing, index);
		Assert.assertNotNull(neighbours);
		Assert.assertEquals(2, neighbours.size());
		Assert.assertFalse(neighbours.get(0).hasChild());
		Assert.assertEquals(0, neighbours.get(0).getValue());
		Assert.assertEquals(1, neighbours.get(1).getValue());

		// Index = 1:
		index = service.create(GeometryIndexType.TYPE_EDGE, 1);
		neighbours = service.getAdjacentVertices(linearRing, index);
		Assert.assertNotNull(neighbours);
		Assert.assertEquals(2, neighbours.size());
		Assert.assertFalse(neighbours.get(0).hasChild());
		Assert.assertEquals(1, neighbours.get(0).getValue());
		Assert.assertEquals(2, neighbours.get(1).getValue());

		// Index = 2:
		index = service.create(GeometryIndexType.TYPE_EDGE, 2);
		neighbours = service.getAdjacentVertices(linearRing, index);
		Assert.assertNotNull(neighbours);
		Assert.assertEquals(2, neighbours.size());
		Assert.assertFalse(neighbours.get(0).hasChild());
		Assert.assertEquals(2, neighbours.get(0).getValue());
		Assert.assertEquals(0, neighbours.get(1).getValue());

		// Index = 3:
		index = service.create(GeometryIndexType.TYPE_EDGE, 3);
		try {
			service.getAdjacentVertices(linearRing, index);
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect this exception.
		}
	}

	/** Given an edge in a LineString, search neighboring edges. */
	@Test
	public void testAdjacentEdgesForLineStringEdge() throws GeometryIndexNotFoundException {
		// Index = -1:
		GeometryIndex index = service.create(GeometryIndexType.TYPE_EDGE, -1);
		try {
			service.getAdjacentEdges(lineString, index);
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect this exception.
		}

		// Index = 0:
		index = service.create(GeometryIndexType.TYPE_EDGE, 0);
		List<GeometryIndex> neighbours = service.getAdjacentEdges(multiLineString.getGeometries()[1], index);
		Assert.assertNotNull(neighbours);
		Assert.assertEquals(1, neighbours.size());
		Assert.assertFalse(neighbours.get(0).hasChild());
		Assert.assertEquals(1, neighbours.get(0).getValue());

		// Index = 1:
		index = service.create(GeometryIndexType.TYPE_EDGE, 1);
		neighbours = service.getAdjacentEdges(multiLineString.getGeometries()[1], index);
		Assert.assertNotNull(neighbours);
		Assert.assertEquals(2, neighbours.size());
		Assert.assertFalse(neighbours.get(0).hasChild());
		Assert.assertEquals(0, neighbours.get(0).getValue());
		Assert.assertEquals(2, neighbours.get(1).getValue());

		// Index = 2:
		index = service.create(GeometryIndexType.TYPE_EDGE, 2);
		neighbours = service.getAdjacentEdges(multiLineString.getGeometries()[1], index);
		Assert.assertNotNull(neighbours);
		Assert.assertEquals(1, neighbours.size());
		Assert.assertFalse(neighbours.get(0).hasChild());
		Assert.assertEquals(1, neighbours.get(0).getValue());

		// Index = 3:
		index = service.create(GeometryIndexType.TYPE_EDGE, 3);
		try {
			service.getAdjacentEdges(multiLineString.getGeometries()[1], index);
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect this exception.
		}
	}

	/** Given an edge in a LinearRing, search neighboring edges. */
	@Test
	public void testAdjacentEdgesForLinearRingEdge() throws GeometryIndexNotFoundException {
		// Index = -1:
		GeometryIndex index = service.create(GeometryIndexType.TYPE_EDGE, -1);
		try {
			service.getAdjacentEdges(linearRing, index);
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect this exception.
		}

		// Index = 0:
		index = service.create(GeometryIndexType.TYPE_EDGE, 0);
		List<GeometryIndex> neighbours = service.getAdjacentEdges(linearRing, index);
		Assert.assertNotNull(neighbours);
		Assert.assertEquals(2, neighbours.size());
		Assert.assertFalse(neighbours.get(0).hasChild());
		Assert.assertEquals(2, neighbours.get(0).getValue());
		Assert.assertEquals(1, neighbours.get(1).getValue());

		// Index = 1:
		index = service.create(GeometryIndexType.TYPE_EDGE, 1);
		neighbours = service.getAdjacentEdges(linearRing, index);
		Assert.assertNotNull(neighbours);
		Assert.assertEquals(2, neighbours.size());
		Assert.assertFalse(neighbours.get(0).hasChild());
		Assert.assertEquals(0, neighbours.get(0).getValue());
		Assert.assertEquals(2, neighbours.get(1).getValue());

		// Index = 2:
		index = service.create(GeometryIndexType.TYPE_EDGE, 2);
		neighbours = service.getAdjacentEdges(linearRing, index);
		Assert.assertNotNull(neighbours);
		Assert.assertEquals(2, neighbours.size());
		Assert.assertFalse(neighbours.get(0).hasChild());
		Assert.assertEquals(1, neighbours.get(0).getValue());
		Assert.assertEquals(0, neighbours.get(1).getValue());

		// Index = 3:
		index = service.create(GeometryIndexType.TYPE_EDGE, 3);
		try {
			service.getAdjacentEdges(linearRing, index);
			Assert.fail();
		} catch (GeometryIndexNotFoundException e) {
			// We expect this exception.
		}
	}

	@Test
	public void testIsAdjacentForLineString() {
		Geometry geometry = multiLineString.getGeometries()[1];

		// Vertex and vertex:
		GeometryIndex one = service.create(GeometryIndexType.TYPE_VERTEX, 1);
		Assert.assertTrue(service.isAdjacent(geometry, one, service.create(GeometryIndexType.TYPE_VERTEX, 0)));
		Assert.assertTrue(service.isAdjacent(geometry, one, service.create(GeometryIndexType.TYPE_VERTEX, 2)));
		Assert.assertFalse(service.isAdjacent(geometry, one, service.create(GeometryIndexType.TYPE_VERTEX, 3)));

		// Vertex and edge:
		one = service.create(GeometryIndexType.TYPE_VERTEX, 1);
		Assert.assertTrue(service.isAdjacent(geometry, one, service.create(GeometryIndexType.TYPE_EDGE, 0)));
		Assert.assertTrue(service.isAdjacent(geometry, one, service.create(GeometryIndexType.TYPE_EDGE, 1)));
		Assert.assertFalse(service.isAdjacent(geometry, one, service.create(GeometryIndexType.TYPE_EDGE, 2)));

		// Edge and vertex:
		one = service.create(GeometryIndexType.TYPE_EDGE, 1);
		Assert.assertTrue(service.isAdjacent(geometry, one, service.create(GeometryIndexType.TYPE_VERTEX, 1)));
		Assert.assertTrue(service.isAdjacent(geometry, one, service.create(GeometryIndexType.TYPE_VERTEX, 2)));
		Assert.assertFalse(service.isAdjacent(geometry, one, service.create(GeometryIndexType.TYPE_VERTEX, 0)));

		// Edge and edge:
		one = service.create(GeometryIndexType.TYPE_EDGE, 1);
		Assert.assertTrue(service.isAdjacent(geometry, one, service.create(GeometryIndexType.TYPE_EDGE, 0)));
		Assert.assertTrue(service.isAdjacent(geometry, one, service.create(GeometryIndexType.TYPE_EDGE, 2)));
		Assert.assertFalse(service.isAdjacent(geometry, one, service.create(GeometryIndexType.TYPE_EDGE, 3)));
	}

	@Test
	public void testIsAdjacentForLineString2() {
		// Separate test for a LineString with only 2 points:
		Geometry geometry = new Geometry(Geometry.LINE_STRING, 0, 0);
		geometry.setCoordinates(new Coordinate[] { new Coordinate(1, 1), new Coordinate(2, 2) });

		// Vertex and vertex:
		GeometryIndex one = service.create(GeometryIndexType.TYPE_VERTEX, 0);
		Assert.assertFalse(service.isAdjacent(geometry, one, service.create(GeometryIndexType.TYPE_VERTEX, 0)));
		Assert.assertTrue(service.isAdjacent(geometry, one, service.create(GeometryIndexType.TYPE_VERTEX, 1)));

		// Vertex and edge:
		Assert.assertTrue(service.isAdjacent(geometry, one, service.create(GeometryIndexType.TYPE_EDGE, 0)));
		Assert.assertFalse(service.isAdjacent(geometry, one, service.create(GeometryIndexType.TYPE_EDGE, 1)));

		// Edge and vertex:
		one = service.create(GeometryIndexType.TYPE_EDGE, 0);
		Assert.assertTrue(service.isAdjacent(geometry, one, service.create(GeometryIndexType.TYPE_VERTEX, 0)));
		Assert.assertTrue(service.isAdjacent(geometry, one, service.create(GeometryIndexType.TYPE_VERTEX, 1)));

		// Edge and edge:
		Assert.assertFalse(service.isAdjacent(geometry, one, service.create(GeometryIndexType.TYPE_EDGE, 0)));
		Assert.assertFalse(service.isAdjacent(geometry, one, service.create(GeometryIndexType.TYPE_EDGE, 1)));
	}

	@Test
	public void testIsAdjacentForLinearRing() {
		// Vertex and vertex:
		GeometryIndex one = service.create(GeometryIndexType.TYPE_VERTEX, 0);
		Assert.assertTrue(service.isAdjacent(linearRing, one, service.create(GeometryIndexType.TYPE_VERTEX, 1)));
		Assert.assertTrue(service.isAdjacent(linearRing, one, service.create(GeometryIndexType.TYPE_VERTEX, 2)));
		Assert.assertFalse(service.isAdjacent(linearRing, one, service.create(GeometryIndexType.TYPE_VERTEX, 3)));

		// Vertex and edge:
		one = service.create(GeometryIndexType.TYPE_VERTEX, 0);
		Assert.assertTrue(service.isAdjacent(linearRing, one, service.create(GeometryIndexType.TYPE_EDGE, 0)));
		Assert.assertTrue(service.isAdjacent(linearRing, one, service.create(GeometryIndexType.TYPE_EDGE, 2)));
		Assert.assertFalse(service.isAdjacent(linearRing, one, service.create(GeometryIndexType.TYPE_EDGE, 1)));

		// Edge and vertex:
		one = service.create(GeometryIndexType.TYPE_EDGE, 0);
		Assert.assertTrue(service.isAdjacent(linearRing, one, service.create(GeometryIndexType.TYPE_VERTEX, 0)));
		Assert.assertTrue(service.isAdjacent(linearRing, one, service.create(GeometryIndexType.TYPE_VERTEX, 1)));
		Assert.assertFalse(service.isAdjacent(linearRing, one, service.create(GeometryIndexType.TYPE_VERTEX, 2)));

		// Edge and edge:
		one = service.create(GeometryIndexType.TYPE_EDGE, 0);
		Assert.assertTrue(service.isAdjacent(linearRing, one, service.create(GeometryIndexType.TYPE_EDGE, 1)));
		Assert.assertTrue(service.isAdjacent(linearRing, one, service.create(GeometryIndexType.TYPE_EDGE, 2)));
		Assert.assertFalse(service.isAdjacent(linearRing, one, service.create(GeometryIndexType.TYPE_EDGE, 3)));
	}

	@Test
	public void testIsAdjacentForMultiPolygon() {
		Geometry geom = multiPolygon;

		// Vertex and vertex:
		GeometryIndex one = service.create(GeometryIndexType.TYPE_VERTEX, 0, 0, 0);
		Assert.assertTrue(service.isAdjacent(geom, one, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0, 1)));
		Assert.assertTrue(service.isAdjacent(geom, one, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0, 3)));
		Assert.assertFalse(service.isAdjacent(geom, one, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0, 2)));

		// Vertex and edge:
		one = service.create(GeometryIndexType.TYPE_VERTEX, 0, 0, 0);
		Assert.assertTrue(service.isAdjacent(geom, one, service.create(GeometryIndexType.TYPE_EDGE, 0, 0, 0)));
		Assert.assertTrue(service.isAdjacent(geom, one, service.create(GeometryIndexType.TYPE_EDGE, 0, 0, 3)));
		Assert.assertFalse(service.isAdjacent(geom, one, service.create(GeometryIndexType.TYPE_EDGE, 0, 0, 1)));

		// Edge and vertex:
		one = service.create(GeometryIndexType.TYPE_EDGE, 0, 0, 0);
		Assert.assertTrue(service.isAdjacent(geom, one, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0, 0)));
		Assert.assertTrue(service.isAdjacent(geom, one, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0, 1)));
		Assert.assertFalse(service.isAdjacent(geom, one, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0, 2)));

		// Edge and edge:
		one = service.create(GeometryIndexType.TYPE_EDGE, 0, 0, 0);
		Assert.assertTrue(service.isAdjacent(geom, one, service.create(GeometryIndexType.TYPE_EDGE, 0, 0, 1)));
		Assert.assertTrue(service.isAdjacent(geom, one, service.create(GeometryIndexType.TYPE_EDGE, 0, 0, 3)));
		Assert.assertFalse(service.isAdjacent(geom, one, service.create(GeometryIndexType.TYPE_EDGE, 0, 0, 2)));
	}
}
