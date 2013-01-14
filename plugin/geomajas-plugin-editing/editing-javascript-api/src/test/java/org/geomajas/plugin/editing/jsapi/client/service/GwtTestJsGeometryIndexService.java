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
 * Tests to see if the JavaScript for the GeometryIndexService actually works.
 * 
 * @author Pieter De Graef
 */
public class GwtTestJsGeometryIndexService extends GWTTestCase {

	@Override
	public String getModuleName() {
		// return "org.geomajas.plugin.editing.jsapi.EditingJavascriptApi";
		return "org.geomajas.plugin.editing.jsapi.EditingJsApiTest";
	}

	public static <T> void jsAssertEquals(T a, T b) {
		assertEquals(a.toString(), b.toString());
	}

	public void testIndexHelperMethods() {
		ExporterUtil.exportAll();
		runIndexHelperMethods();
	}

	public void testParseFormat() {
		ExporterUtil.exportAll();
		runParseFormat();
	}

	public void testGeometryRetrieval() {
		ExporterUtil.exportAll();
		runGeometryRetrieval();
	}

	public void testIndexTypes() {
		ExporterUtil.exportAll();
		runIndexTypes();
	}

	public void testGeometryType() {
		ExporterUtil.exportAll();
		runGeometryType();
	}

	public void testSiblingCount() {
		ExporterUtil.exportAll();
		runSiblingCount();
	}

	public void testNextPreviousIndex() {
		ExporterUtil.exportAll();
		runNextPreviousIndex();
	}

	public void testAdjacency() {
		ExporterUtil.exportAll();
		runAdjacency();
	}

	// ------------------------------------------------------------------------
	// The actual JavaScript test methods:
	// ------------------------------------------------------------------------

	public native JavaScriptObject runIndexHelperMethods()
	/*-{
		// Some initialization:
		assertEquals = function(a, b) {
			@org.geomajas.plugin.editing.jsapi.client.service.GwtTestJsGeometryIndexService::jsAssertEquals(Ljava/lang/Object;Ljava/lang/Object;)(a, b);
		}
		var service = new $wnd.org.geomajas.plugin.editing.service.GeometryIndexService();

		// Test normal parsing:
		var index = service.create("vertex", 3, 4, 2);
		assertEquals("2", service.getValue(index) + "");

		var index2 = service.create("geometry", 3, 4);
		assertEquals("true", service.isChildOf(index2, index));
	}-*/;

	public native JavaScriptObject runParseFormat()
	/*-{
		// Some initialization:
		assertEquals = function(a, b) {
			@org.geomajas.plugin.editing.jsapi.client.service.GwtTestJsGeometryIndexService::jsAssertEquals(Ljava/lang/Object;Ljava/lang/Object;)(a, b);
		}
		var service = new $wnd.org.geomajas.plugin.editing.service.GeometryIndexService();

		// Test normal parsing:
		var identifier = "geometry3.geometry4.edge2";
		var index = service.parse(identifier);
		assertEquals("3", index.getValue() + "");
		assertEquals("true", index.hasChild() + "");
		assertEquals("geometry", index.getType());
		assertEquals("4", index.getChild().getValue() + "");
		assertEquals("true", index.getChild().hasChild() + "");
		assertEquals("geometry", index.getChild().getType());
		assertEquals("2", index.getChild().getChild().getValue() + "");
		assertEquals("false", index.getChild().getChild().hasChild() + "");
		assertEquals("edge", index.getChild().getChild().getType());

		var formatted = service.format(index);
		assertEquals(identifier, formatted);

		// Test corner cases:
		index = service.parse("foo0.bar0." + identifier);
		assertEquals(identifier, service.format(index));
	}-*/;

	public native JavaScriptObject runGeometryRetrieval()
	/*-{
		// Some initialization:
		assertEquals = function(a, b) {
			@org.geomajas.plugin.editing.jsapi.client.service.GwtTestJsGeometryIndexService::jsAssertEquals(Ljava/lang/Object;Ljava/lang/Object;)(a, b);
		}
		var service = new $wnd.org.geomajas.plugin.editing.service.GeometryIndexService();
		var ring = new $wnd.org.geomajas.jsapi.spatial.Geometry("LinearRing",
				31300, 5);
		ring.setCoordinates([
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(10, 10),
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(20, 20),
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(20, 10),
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(10, 10) ]);
		var polygon = new $wnd.org.geomajas.jsapi.spatial.Geometry("Polygon",
				31300, 5);
		polygon.setGeometries([ ring ]);

		// Test the getGeometry method:
		var geometry = service.getGeometry(polygon, service.create("geometry",
				0));
		assertEquals("LinearRing", geometry.getGeometryType());

		// Test the getVertex method:
		var vertex = service.getVertex(polygon, service.create("vertex", 0, 1));
		assertEquals("20", vertex.getX() + "");
		assertEquals("20", vertex.getY() + "");

		// Test the getEdge method:
		// var edge = service.getEdge(polygon, service.create("edge", 0, 0));
		// assertEquals("20", edge[1].getX() + "");
		// assertEquals("20", edge[1].getY() + "");
	}-*/;

	public native JavaScriptObject runIndexTypes()
	/*-{
		// Some initialization:
		assertEquals = function(a, b) {
			@org.geomajas.plugin.editing.jsapi.client.service.GwtTestJsGeometryIndexService::jsAssertEquals(Ljava/lang/Object;Ljava/lang/Object;)(a, b);
		}
		var service = new $wnd.org.geomajas.plugin.editing.service.GeometryIndexService();

		var index = service.create("geometry", 0, 1);
		assertEquals("geometry", service.getType(index));
		assertEquals("true", service.isGeometry(index) + "");
		assertEquals("false", service.isVertex(index) + "");
		assertEquals("false", service.isEdge(index) + "");

		index = service.addChildren(index, "vertex", 2);
		assertEquals("vertex", service.getType(index));
		assertEquals("false", service.isGeometry(index) + "");
		assertEquals("true", service.isVertex(index) + "");
		assertEquals("false", service.isEdge(index) + "");

		index = service.create("edge", 0, 1);
		assertEquals("edge", service.getType(index));
		assertEquals("false", service.isGeometry(index) + "");
		assertEquals("false", service.isVertex(index) + "");
		assertEquals("true", service.isEdge(index) + "");
	}-*/;

	public native JavaScriptObject runGeometryType()
	/*-{
		// Some initialization:
		assertEquals = function(a, b) {
			@org.geomajas.plugin.editing.jsapi.client.service.GwtTestJsGeometryIndexService::jsAssertEquals(Ljava/lang/Object;Ljava/lang/Object;)(a, b);
		}
		var service = new $wnd.org.geomajas.plugin.editing.service.GeometryIndexService();
		var ring = new $wnd.org.geomajas.jsapi.spatial.Geometry("LinearRing",
				31300, 5);
		ring.setCoordinates([
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(10, 10),
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(20, 20),
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(20, 10),
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(10, 10) ]);
		var polygon = new $wnd.org.geomajas.jsapi.spatial.Geometry("Polygon",
				31300, 5);
		polygon.setGeometries([ ring ]);

		// Test the getGeometry method:
		var type = service.getGeometryType(polygon, null);
		assertEquals("Polygon", type);

		var index = service.create("geometry", 0);
		var type = service.getGeometryType(polygon, index);
		assertEquals("LinearRing", type);
	}-*/;

	public native JavaScriptObject runSiblingCount()
	/*-{
		// Some initialization:
		assertEquals = function(a, b) {
			@org.geomajas.plugin.editing.jsapi.client.service.GwtTestJsGeometryIndexService::jsAssertEquals(Ljava/lang/Object;Ljava/lang/Object;)(a, b);
		}
		var service = new $wnd.org.geomajas.plugin.editing.service.GeometryIndexService();
		var ring = new $wnd.org.geomajas.jsapi.spatial.Geometry("LinearRing",
				31300, 5);
		ring.setCoordinates([
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(10, 10),
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(20, 20),
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(20, 10),
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(10, 10) ]);
		var polygon = new $wnd.org.geomajas.jsapi.spatial.Geometry("Polygon",
				31300, 5);
		polygon.setGeometries([ ring ]);

		// Test the getGeometry method:
		var index = service.create("vertex", 0, 0);
		assertEquals("4", service.getSiblingCount(polygon, index));
	}-*/;

	public native JavaScriptObject runNextPreviousIndex()
	/*-{
		// Some initialization:
		assertEquals = function(a, b) {
			@org.geomajas.plugin.editing.jsapi.client.service.GwtTestJsGeometryIndexService::jsAssertEquals(Ljava/lang/Object;Ljava/lang/Object;)(a, b);
		}
		var service = new $wnd.org.geomajas.plugin.editing.service.GeometryIndexService();

		// Test the getGeometry method:
		var index = service.create("vertex", 0, 5);

		var previous = service.getPreviousVertex(index);
		assertEquals("4", service.getValue(previous) + "");

		var next = service.getNextVertex(index);
		assertEquals("6", service.getValue(next) + "");
	}-*/;

	public native JavaScriptObject runAdjacency()
	/*-{
		// Some initialization:
		assertEquals = function(a, b) {
			@org.geomajas.plugin.editing.jsapi.client.service.GwtTestJsGeometryIndexService::jsAssertEquals(Ljava/lang/Object;Ljava/lang/Object;)(a, b);
		}
		var service = new $wnd.org.geomajas.plugin.editing.service.GeometryIndexService();
		var ring = new $wnd.org.geomajas.jsapi.spatial.Geometry("LinearRing",
				31300, 5);
		ring.setCoordinates([
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(10, 10),
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(20, 20),
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(20, 10),
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(10, 10) ]);
		var polygon = new $wnd.org.geomajas.jsapi.spatial.Geometry("Polygon",
				31300, 5);
		polygon.setGeometries([ ring ]);

		var index1 = service.create("vertex", 0, 1);
		var index2 = service.create("vertex", 0, 2);
		assertEquals("true", service.isAdjacent(polygon, index1, index2));
	}-*/;
}