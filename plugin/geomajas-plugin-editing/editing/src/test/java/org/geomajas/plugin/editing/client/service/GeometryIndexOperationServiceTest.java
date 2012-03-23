/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.editing.client.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.event.GeometryEditInsertEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditInsertHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditMoveEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditMoveHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditRemoveEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditRemoveHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditShapeChangedEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditShapeChangedHandler;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;
import org.junit.Test;

/**
 * Test cases testing the methods in the {@link GeometryIndexOperationService}.
 * 
 * @author Pieter De Graef
 */
public class GeometryIndexOperationServiceTest {

	private GeometryEditService service = new GeometryEditServiceImpl();

	private GeometryIndexService indexService;

	private Geometry polygon = new Geometry(Geometry.POLYGON, 0, 0);

	private Coordinate coord = new Coordinate(342.0, 342.0);

	private GeometryIndex index;

	private int insertCount;

	private int moveCount;

	private int deleteCount;

	private int shapeChangedCount;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------

	public GeometryIndexOperationServiceTest() {
		Geometry shell = new Geometry(Geometry.LINEAR_RING, 0, 0);
		shell.setCoordinates(new Coordinate[] { new Coordinate(0, 0), new Coordinate(10, 0), new Coordinate(10, 10),
				new Coordinate(0, 10), new Coordinate(0, 0) });
		Geometry hole = new Geometry(Geometry.LINEAR_RING, 0, 0);
		hole.setCoordinates(new Coordinate[] { new Coordinate(4, 4), new Coordinate(6, 4), new Coordinate(6, 6),
				new Coordinate(4, 6), new Coordinate(4, 4) });
		polygon.setGeometries(new Geometry[] { shell, hole });

		indexService = service.getIndexService();
		index = indexService.create(GeometryIndexType.TYPE_VERTEX, 0, 0);

		service.addGeometryEditInsertHandler(new GeometryEditInsertHandler() {

			public void onGeometryEditInsert(GeometryEditInsertEvent event) {
				insertCount++;
			}
		});
		service.addGeometryEditMoveHandler(new GeometryEditMoveHandler() {

			public void onGeometryEditMove(GeometryEditMoveEvent event) {
				moveCount++;
			}
		});
		service.addGeometryEditRemoveHandler(new GeometryEditRemoveHandler() {

			public void onGeometryEditRemove(GeometryEditRemoveEvent event) {
				deleteCount++;
			}
		});
		service.addGeometryEditShapeChangedHandler(new GeometryEditShapeChangedHandler() {

			public void onGeometryShapeChanged(GeometryEditShapeChangedEvent event) {
				shapeChangedCount++;
			}
		});
	}

	// ------------------------------------------------------------------------
	// Test the GeometryIndexService.
	// ------------------------------------------------------------------------

	@Test
	public void testInsert() throws GeometryOperationFailedException {
		service.start(polygon);
		Assert.assertEquals(5, indexService.getSiblingCount(polygon, index));

		service.insert(Collections.singletonList(index), Collections.singletonList(Collections.singletonList(coord)));
		Assert.assertEquals(6, indexService.getSiblingCount(polygon, index));
		service.stop();
	}

	@Test
	public void testInsertIllegalArguments() {
		service.start(polygon);

		List<List<Coordinate>> coordinates = Collections.singletonList(Collections.singletonList(coord));
		try {
			service.insert(null, coordinates);
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// As expected...
		}
		try {
			service.insert(new ArrayList<GeometryIndex>(), coordinates);
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// As expected...
		}
		try {
			service.insert(Collections.singletonList(index), null);
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// As expected...
		}
		try {
			service.insert(Collections.singletonList(index), new ArrayList<List<Coordinate>>());
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// As expected...
		}

		service.stop();
	}

	@Test
	public void testInsertEvents() throws GeometryOperationFailedException {
		insertCount = 0;
		service.start(polygon);
		service.insert(Collections.singletonList(index), Collections.singletonList(Collections.singletonList(coord)));
		Assert.assertEquals(1, insertCount);
		service.stop();
	}

	@Test
	public void testInsertSequence() throws GeometryOperationFailedException {
		insertCount = 0;
		shapeChangedCount = 0;

		service.start(polygon);
		service.insert(Collections.singletonList(index), Collections.singletonList(Collections.singletonList(coord)));
		Assert.assertEquals(1, insertCount);
		Assert.assertEquals(1, shapeChangedCount);

		service.startOperationSequence();
		service.insert(Collections.singletonList(index), Collections.singletonList(Collections.singletonList(coord)));
		service.insert(Collections.singletonList(index), Collections.singletonList(Collections.singletonList(coord)));
		Assert.assertEquals(3, insertCount);
		Assert.assertEquals(1, shapeChangedCount);

		service.stopOperationSequence();
		Assert.assertEquals(2, shapeChangedCount);
		service.stop();
	}

	@Test
	public void testMove() throws GeometryOperationFailedException, GeometryIndexNotFoundException {
		service.start(polygon);
		Assert.assertEquals(5, indexService.getSiblingCount(polygon, index));

		service.move(Collections.singletonList(index), Collections.singletonList(Collections.singletonList(coord)));
		Coordinate moved = indexService.getVertex(polygon, index);
		Assert.assertEquals(342.0, moved.getX());
		Assert.assertEquals(342.0, moved.getY());
		service.stop();
	}

	@Test
	public void testMoveIllegalArguments() {
		service.start(polygon);

		List<List<Coordinate>> coordinates = Collections.singletonList(Collections.singletonList(coord));
		try {
			service.move(null, coordinates);
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// As expected...
		}
		try {
			service.move(new ArrayList<GeometryIndex>(), coordinates);
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// As expected...
		}
		try {
			service.move(Collections.singletonList(index), null);
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// As expected...
		}
		try {
			service.move(Collections.singletonList(index), new ArrayList<List<Coordinate>>());
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// As expected...
		}

		service.stop();
	}

	@Test
	public void testMoveEvents() throws GeometryOperationFailedException {
		moveCount = 0;
		service.start(polygon);
		service.move(Collections.singletonList(index), Collections.singletonList(Collections.singletonList(coord)));
		Assert.assertEquals(1, moveCount);
		service.stop();
	}

	@Test
	public void testMoveSequence() throws GeometryOperationFailedException {
		moveCount = 0;
		shapeChangedCount = 0;

		service.start(polygon);
		service.move(Collections.singletonList(index), Collections.singletonList(Collections.singletonList(coord)));
		Assert.assertEquals(1, moveCount);
		Assert.assertEquals(1, shapeChangedCount);

		service.startOperationSequence();
		service.move(Collections.singletonList(index), Collections.singletonList(Collections.singletonList(coord)));
		service.move(Collections.singletonList(index), Collections.singletonList(Collections.singletonList(coord)));
		Assert.assertEquals(3, moveCount);
		Assert.assertEquals(1, shapeChangedCount);

		service.stopOperationSequence();
		Assert.assertEquals(2, shapeChangedCount);
		service.stop();
	}

	@Test
	public void testRemove() throws GeometryOperationFailedException, GeometryIndexNotFoundException {
		service.start(polygon);
		Assert.assertEquals(5, indexService.getSiblingCount(polygon, index));

		service.remove(Collections.singletonList(index));
		Coordinate moved = indexService.getVertex(polygon, index);
		Assert.assertEquals(10.0, moved.getX());
		Assert.assertEquals(0.0, moved.getY());
		service.stop();
	}

	@Test
	public void testRemoveIllegalArguments() {
		service.start(polygon);

		try {
			service.remove(null);
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// As expected...
		}
		try {
			service.remove(new ArrayList<GeometryIndex>());
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// As expected...
		}

		service.stop();
	}

	@Test
	public void testRemoveEvents() throws GeometryOperationFailedException {
		deleteCount = 0;
		service.start(polygon);
		service.remove(Collections.singletonList(index));
		Assert.assertEquals(1, deleteCount);
		service.stop();
	}

	@Test
	public void testRemoveSequence() throws GeometryOperationFailedException {
		deleteCount = 0;
		shapeChangedCount = 0;

		service.start(polygon);
		service.remove(Collections.singletonList(index));
		Assert.assertEquals(1, deleteCount);
		Assert.assertEquals(1, shapeChangedCount);

		service.startOperationSequence();
		service.remove(Collections.singletonList(index));
		service.remove(Collections.singletonList(index));
		Assert.assertEquals(3, deleteCount);
		Assert.assertEquals(1, shapeChangedCount);

		service.stopOperationSequence();
		Assert.assertEquals(2, shapeChangedCount);
		service.stop();
	}

	@Test
	public void testCanUndo() throws GeometryOperationFailedException {
		Assert.assertFalse(service.canUndo());
		service.start(polygon);
		Assert.assertFalse(service.canUndo());
		service.remove(Collections.singletonList(index));
		Assert.assertTrue(service.canUndo());
		service.stop();
		Assert.assertFalse(service.canUndo());
	}

	@Test
	public void testCanRedo() throws GeometryOperationFailedException {
		Assert.assertFalse(service.canRedo());
		service.start(polygon);
		Assert.assertFalse(service.canRedo());
		service.remove(Collections.singletonList(index));
		Assert.assertFalse(service.canRedo());
		service.undo();
		Assert.assertTrue(service.canRedo());
		service.stop();
		Assert.assertFalse(service.canRedo());
	}

	@Test
	public void testUndoRedo() throws GeometryOperationFailedException, GeometryIndexNotFoundException {
		service.start(polygon);
		Assert.assertFalse(service.canUndo());
		Coordinate c = indexService.getVertex(polygon, index);
		Assert.assertEquals(0.0, c.getX());
		Assert.assertEquals(0.0, c.getY());

		service.remove(Collections.singletonList(index));
		c = indexService.getVertex(polygon, index);
		Assert.assertEquals(10.0, c.getX());
		Assert.assertEquals(0.0, c.getY());

		service.undo();
		c = indexService.getVertex(polygon, index);
		Assert.assertEquals(0.0, c.getX());
		Assert.assertEquals(0.0, c.getY());

		service.redo();
		c = indexService.getVertex(polygon, index);
		Assert.assertEquals(10.0, c.getX());
		Assert.assertEquals(0.0, c.getY());

		service.stop();
	}

	@Test
	public void testUndoRedoSequence() throws GeometryIndexNotFoundException, GeometryOperationFailedException {
		service.start(polygon);
		Assert.assertFalse(service.canUndo());
		Coordinate c = indexService.getVertex(polygon, index);
		Assert.assertEquals(0.0, c.getX());
		Assert.assertEquals(0.0, c.getY());

		service.startOperationSequence();
		service.move(Collections.singletonList(index),
				Collections.singletonList(Collections.singletonList(new Coordinate(5, 5))));
		service.move(Collections.singletonList(index),
				Collections.singletonList(Collections.singletonList(new Coordinate(7, 7))));
		service.move(Collections.singletonList(index), Collections.singletonList(Collections.singletonList(coord)));
		Assert.assertFalse(service.canUndo());

		service.stopOperationSequence();
		Assert.assertTrue(service.canUndo());
		c = indexService.getVertex(polygon, index);
		Assert.assertEquals(342.0, c.getX());
		Assert.assertEquals(342.0, c.getY());

		service.undo();
		c = indexService.getVertex(polygon, index);
		Assert.assertEquals(0.0, c.getX());
		Assert.assertEquals(0.0, c.getY());

		service.redo();
		c = indexService.getVertex(polygon, index);
		Assert.assertEquals(342.0, c.getX());
		Assert.assertEquals(342.0, c.getY());

		service.stop();
	}

	@Test
	public void testAddEmptyChild() throws GeometryOperationFailedException {
		service.start(polygon);
		int originalNumber = polygon.getGeometries().length;
		service.addEmptyChild(new GeometryIndex(GeometryIndexType.TYPE_GEOMETRY, 0, null));
		int afterNumber = polygon.getGeometries().length;
		Assert.assertTrue(originalNumber + 1 == afterNumber);
	}
}