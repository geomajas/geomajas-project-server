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

package org.geomajas.plugin.editing.jsapi.client.service;

import org.timepedia.exporter.client.ExporterUtil;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.junit.client.GWTTestCase;

/**
 * Tests to see if the JavaScript for the GeometryEditingService actually works.
 * 
 * @author Pieter De Graef
 */
public class GwtTestJsGeometryEditService extends GWTTestCase {

	@Override
	public String getModuleName() {
		return "org.geomajas.plugin.editing.jsapi.EditingJsApiTest";
	}

	public static <T> void jsAssertEquals(T a, T b) {
		assertEquals(a.toString(), b.toString());
	}

	public void testStartStop() {
		ExporterUtil.exportAll();
		runStartStop();
	}

	public void testEditingState() {
		ExporterUtil.exportAll();
		runEditingState();
	}

	public void testInsertIndex() {
		ExporterUtil.exportAll();
		runInsertIndex();
	}

	public void testAddEmptyChild() {
		ExporterUtil.exportAll();
		runAddEmptyChild();
	}

	public void testTentativeMoveLocation() {
		ExporterUtil.exportAll();
		runTentativeMoveLocation();
	}

	public void testInsert() {
		ExporterUtil.exportAll();
		runInsert();
	}

	public void testMove() {
		ExporterUtil.exportAll();
		runMove();
	}

	public void testRemove() {
		ExporterUtil.exportAll();
		runRemove();
	}

	public void testUndoRedo() {
		ExporterUtil.exportAll();
		runUndoRedo();
	}

	public void testCanUndoRedo() {
		ExporterUtil.exportAll();
		runCanUndoRedo();
	}

	public void testSequence() {
		ExporterUtil.exportAll();
		runSequence();
	}

	// ------------------------------------------------------------------------
	// Actual JavaScript test methods:
	// ------------------------------------------------------------------------

	public native JavaScriptObject runStartStop()
	/*-{
		// Some initialization:
		assertEquals = function(a, b) {
			@org.geomajas.plugin.editing.jsapi.client.service.GwtTestJsGeometryIndex::jsAssertEquals(Ljava/lang/Object;Ljava/lang/Object;)(a, b);
		}
		var service = new $wnd.org.geomajas.plugin.editing.service.GeometryEditService();
		var geometry = new $wnd.org.geomajas.jsapi.spatial.Geometry(
				"LineString", 31300, 0);

		var startCount = 0;
		var stopCount = 0;

		service.addGeometryEditStartHandler(function(event) {
			startCount++;
		});
		service.addGeometryEditStopHandler(function(event) {
			stopCount++;
		});

		service.start(geometry);
		service.stop();

		assertEquals("1", startCount + "");
		assertEquals("1", stopCount + "");
	}-*/;

	public native JavaScriptObject runEditingState()
	/*-{
		// Some initialization:
		assertEquals = function(a, b) {
			@org.geomajas.plugin.editing.jsapi.client.service.GwtTestJsGeometryIndex::jsAssertEquals(Ljava/lang/Object;Ljava/lang/Object;)(a, b);
		}
		var service = new $wnd.org.geomajas.plugin.editing.service.GeometryEditService();
		var geometry = new $wnd.org.geomajas.jsapi.spatial.Geometry("Point", 0,
				0);
		geometry.setCoordinates([
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(10, 10),
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(20, 20) ]);

		var currentState = "";

		service.addGeometryEditChangeStateHandler(function(event) {
			currentState = event.getEditingState();
		});

		service.start(geometry);
		service.setEditingState("inserting");
		assertEquals("inserting", service.getEditingState());
		assertEquals("inserting", currentState);

		service.setEditingState("dragging");
		assertEquals("dragging", service.getEditingState());
		assertEquals("dragging", currentState);

		service.setEditingState("idle");
		assertEquals("idle", service.getEditingState());
		assertEquals("idle", currentState);

		service.stop();
	}-*/;

	public native JavaScriptObject runInsertIndex()
	/*-{
		// Some initialization:
		assertEquals = function(a, b) {
			@org.geomajas.plugin.editing.jsapi.client.service.GwtTestJsGeometryIndex::jsAssertEquals(Ljava/lang/Object;Ljava/lang/Object;)(a, b);
		}
		var service = new $wnd.org.geomajas.plugin.editing.service.GeometryEditService();
		var geometry = new $wnd.org.geomajas.jsapi.spatial.Geometry("Polygon",
				0, 0);

		service.start(geometry);

		var index = service.getInsertIndex();
		assertEquals("null", index + "");

		var index2 = service.getIndexService().create("vertex", 0, 0);
		service.setInsertIndex(index2);
		index = service.getInsertIndex();
		assertEquals("0", index.getChild().getValue() + "");
		assertEquals("false", index.getChild().hasChild() + "");
		assertEquals("vertex", index.getChild().getType() + "");

		service.stop();
	}-*/;

	public native JavaScriptObject runAddEmptyChild()
	/*-{
		// Some initialization:
		assertEquals = function(a, b) {
			@org.geomajas.plugin.editing.jsapi.client.service.GwtTestJsGeometryIndex::jsAssertEquals(Ljava/lang/Object;Ljava/lang/Object;)(a, b);
		}
		var service = new $wnd.org.geomajas.plugin.editing.service.GeometryEditService();
		var geometry = new $wnd.org.geomajas.jsapi.spatial.Geometry("Polygon",
				0, 0);

		service.start(geometry);

		service.addEmptyChild();
		var index = service.getIndexService().create("geometry", 0);
		var emptyChild = service.getIndexService().getGeometry(
				service.getGeometry(), index);
		assertEquals("LinearRing", emptyChild.getGeometryType());

		service.stop();
	}-*/;

	public native JavaScriptObject runTentativeMoveLocation()
	/*-{
		// Some initialization:
		assertEquals = function(a, b) {
			@org.geomajas.plugin.editing.jsapi.client.service.GwtTestJsGeometryIndex::jsAssertEquals(Ljava/lang/Object;Ljava/lang/Object;)(a, b);
		}
		var service = new $wnd.org.geomajas.plugin.editing.service.GeometryEditService();
		var geometry = new $wnd.org.geomajas.jsapi.spatial.Geometry("Polygon",
				0, 0);

		var location = null;

		service.addGeometryEditTentativeMoveHandler(function(event) {
			location = service.getTentativeMoveLocation();
		});

		service.start(geometry);

		var c = new $wnd.org.geomajas.jsapi.spatial.Coordinate(342, 342);
		service.setTentativeMoveLocation(c);
		assertEquals("342", location.getX() + "");
		assertEquals("342", location.getY() + "");

		service.stop();
	}-*/;

	public native JavaScriptObject runInsert()
	/*-{
		// Some initialization:
		assertEquals = function(a, b) {
			@org.geomajas.plugin.editing.jsapi.client.service.GwtTestJsGeometryIndex::jsAssertEquals(Ljava/lang/Object;Ljava/lang/Object;)(a, b);
		}
		var service = new $wnd.org.geomajas.plugin.editing.service.GeometryEditService();
		var geometry = new $wnd.org.geomajas.jsapi.spatial.Geometry(
				"LineString", 0, 0);

		var insertCount = 0;

		service.addGeometryEditInsertHandler(function(event) {
			insertCount++;
		});

		service.start(geometry);
		var index = service.getIndexService().create("vertex", 0);
		service.setInsertIndex(index);
		service.setEditingState("inserting");

		var location = new $wnd.org.geomajas.jsapi.spatial.Coordinate(342, 342);
		service.insert([ index ], [ [ location ] ]);

		assertEquals("1", insertCount + "");

		var vertex = service.getIndexService().getVertex(service.getGeometry(),
				index);
		assertEquals("342", vertex.getX() + "");
		assertEquals("342", vertex.getY() + "");

		service.stop();
	}-*/;

	public native JavaScriptObject runMove()
	/*-{
		// Some initialization:
		assertEquals = function(a, b) {
			@org.geomajas.plugin.editing.jsapi.client.service.GwtTestJsGeometryIndex::jsAssertEquals(Ljava/lang/Object;Ljava/lang/Object;)(a, b);
		}
		var service = new $wnd.org.geomajas.plugin.editing.service.GeometryEditService();
		var geometry = new $wnd.org.geomajas.jsapi.spatial.Geometry(
				"LineString", 0, 0);
		geometry.setCoordinates([
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(10, 0),
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(20, 0),
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(30, 0) ]);

		var moveCount = 0;

		service.addGeometryEditMoveHandler(function(event) {
			moveCount++;
		});

		service.start(geometry);
		var index1 = service.getIndexService().create("vertex", 1);
		var index2 = service.getIndexService().create("vertex", 2);
		var pos1 = new $wnd.org.geomajas.jsapi.spatial.Coordinate(0, 40);
		var pos2 = new $wnd.org.geomajas.jsapi.spatial.Coordinate(0, 50);
		service.move([ index1, index2 ], [ [ pos1 ], [ pos2 ] ]);

		assertEquals("1", moveCount + "");

		var vertex = service.getIndexService().getVertex(service.getGeometry(),
				index1);
		assertEquals("0", vertex.getX() + "");
		assertEquals("40", vertex.getY() + "");
		vertex = service.getIndexService().getVertex(service.getGeometry(),
				index2);
		assertEquals("0", vertex.getX() + "");
		assertEquals("50", vertex.getY() + "");

		service.stop();
	}-*/;

	public native JavaScriptObject runRemove()
	/*-{
		// Some initialization:
		assertEquals = function(a, b) {
			@org.geomajas.plugin.editing.jsapi.client.service.GwtTestJsGeometryIndex::jsAssertEquals(Ljava/lang/Object;Ljava/lang/Object;)(a, b);
		}
		var service = new $wnd.org.geomajas.plugin.editing.service.GeometryEditService();
		var geometry = new $wnd.org.geomajas.jsapi.spatial.Geometry(
				"LineString", 0, 0);
		geometry.setCoordinates([
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(10, 10),
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(20, 20),
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(30, 30) ]);

		var deleteCount = 0;

		service.addGeometryEditRemoveHandler(function(event) {
			deleteCount++;
		});

		service.start(geometry);
		var index = service.getIndexService().create("vertex", 1);
		service.remove([ index ]);
		assertEquals("1", deleteCount + "");

		var vertex = service.getIndexService().getVertex(service.getGeometry(),
				index);
		assertEquals("30", vertex.getX() + "");
		assertEquals("30", vertex.getY() + "");

		service.stop();
	}-*/;

	public native JavaScriptObject runUndoRedo()
	/*-{
		// Some initialization:
		assertEquals = function(a, b) {
			@org.geomajas.plugin.editing.jsapi.client.service.GwtTestJsGeometryIndex::jsAssertEquals(Ljava/lang/Object;Ljava/lang/Object;)(a, b);
		}
		var service = new $wnd.org.geomajas.plugin.editing.service.GeometryEditService();
		var geometry = new $wnd.org.geomajas.jsapi.spatial.Geometry(
				"LineString", 0, 0);

		var changeCount = 0;
		service.addGeometryEditShapeChangedHandler(function(event) {
			changeCount++;
		});

		service.start(geometry);
		var index = service.getIndexService().create("vertex", 0);
		service.setInsertIndex(index);
		service.setEditingState("inserting");

		var coordinate1 = new $wnd.org.geomajas.jsapi.spatial.Coordinate(1, 1);
		service.insert([ index ], [ [ coordinate1 ] ]);
		var nr = service.getIndexService().getSiblingCount(geometry, index);
		assertEquals("1", nr + "");
		assertEquals("1", changeCount + "");

		service.undo();
		nr = service.getIndexService().getSiblingCount(geometry, index);
		assertEquals("0", nr + "");
		assertEquals("2", changeCount + "");

		service.redo();
		nr = service.getIndexService().getSiblingCount(geometry, index);
		assertEquals("1", nr + "");
		assertEquals("3", changeCount + "");

		service.stop();
	}-*/;

	public native JavaScriptObject runCanUndoRedo()
	/*-{
		// Some initialization:
		assertEquals = function(a, b) {
			@org.geomajas.plugin.editing.jsapi.client.service.GwtTestJsGeometryIndex::jsAssertEquals(Ljava/lang/Object;Ljava/lang/Object;)(a, b);
		}
		var service = new $wnd.org.geomajas.plugin.editing.service.GeometryEditService();
		var geometry = new $wnd.org.geomajas.jsapi.spatial.Geometry(
				"LineString", 0, 0);

		service.start(geometry);
		var index = service.getIndexService().create("vertex", 0);
		service.setInsertIndex(index);
		service.setEditingState("inserting");

		var coordinate1 = new $wnd.org.geomajas.jsapi.spatial.Coordinate(1, 1);
		service.insert([ index ], [ [ coordinate1 ] ]);
		assertEquals("true", service.canUndo() + "");
		assertEquals("false", service.canRedo() + "");

		service.undo();
		assertEquals("false", service.canUndo() + "");
		assertEquals("true", service.canRedo() + "");

		service.redo();
		assertEquals("true", service.canUndo() + "");
		assertEquals("false", service.canRedo() + "");

		service.stop();
	}-*/;	

	public native JavaScriptObject runSequence()
	/*-{
		// Some initialization:
		assertEquals = function(a, b) {
			@org.geomajas.plugin.editing.jsapi.client.service.GwtTestJsGeometryIndex::jsAssertEquals(Ljava/lang/Object;Ljava/lang/Object;)(a, b);
		}
		var service = new $wnd.org.geomajas.plugin.editing.service.GeometryEditService();
		var geometry = new $wnd.org.geomajas.jsapi.spatial.Geometry(
				"LineString", 0, 0);
		geometry.setCoordinates([
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(10, 0),
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(20, 0) ]);

		var changeCount = 0;
		service.addGeometryEditShapeChangedHandler(function(event) {
			changeCount++;
		});

		service.start(geometry);
		service.startOperationSequence();
		var index = service.getIndexService().create("vertex", 1);

		var location = new $wnd.org.geomajas.jsapi.spatial.Coordinate(0, 30);
		service.move([ index ], [ [ location ] ]);
		assertEquals("0", changeCount + "");

		location = new $wnd.org.geomajas.jsapi.spatial.Coordinate(0, 40);
		service.move([ index ], [ [ location ] ]);
		assertEquals("0", changeCount + "");

		service.stopOperationSequence();
		assertEquals("1", changeCount + "");

		service.stop();
	}-*/;	
}