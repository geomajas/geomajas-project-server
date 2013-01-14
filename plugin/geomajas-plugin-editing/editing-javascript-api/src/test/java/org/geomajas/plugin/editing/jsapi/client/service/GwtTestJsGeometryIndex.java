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
 * Tests to see if the JavaScript for the GeometryIndex actually works.
 * 
 * @author Pieter De Graef
 */
public class GwtTestJsGeometryIndex extends GWTTestCase {

	@Override
	public String getModuleName() {
		// return "org.geomajas.plugin.editing.jsapi.EditingJavascriptApi";
		return "org.geomajas.plugin.editing.jsapi.EditingJsApiTest";
	}

	public static <T> void jsAssertEquals(T a, T b) {
		assertEquals(a.toString(), b.toString());
	}

	public void testCreate() {
		ExporterUtil.exportAll();
		runCreate();
	}

	public void testAddChildren() {
		ExporterUtil.exportAll();
		runAddChildren();
	}

	public native JavaScriptObject runCreate()
	/*-{
		// Some initialization:
		assertEquals = function(a, b) {
			@org.geomajas.plugin.editing.jsapi.client.service.GwtTestJsGeometryIndex::jsAssertEquals(Ljava/lang/Object;Ljava/lang/Object;)(a, b);
		}
		var service = new $wnd.org.geomajas.plugin.editing.service.GeometryIndexService();

		// Test the create method and the indices that come out of it:
		var index = service.create("vertex", 1, 2);
		assertEquals("1", index.getValue() + "");
		assertEquals("true", index.hasChild() + "");
		assertEquals("geometry", index.getType());
		assertEquals("2", index.getChild().getValue() + "");
		assertEquals("false", index.getChild().hasChild() + "");
		assertEquals("vertex", index.getChild().getType());

		index = service.create("geometry", 0);
		assertEquals("0", index.getValue() + "");
		assertEquals("false", index.hasChild() + "");
		assertEquals("geometry", index.getType());

		index = service.create("edge", 3, 4, 2);
		assertEquals("3", index.getValue() + "");
		assertEquals("true", index.hasChild() + "");
		assertEquals("geometry", index.getType());
		assertEquals("4", index.getChild().getValue() + "");
		assertEquals("true", index.getChild().hasChild() + "");
		assertEquals("geometry", index.getChild().getType());
		assertEquals("2", index.getChild().getChild().getValue() + "");
		assertEquals("false", index.getChild().getChild().hasChild() + "");
		assertEquals("edge", index.getChild().getChild().getType());
	}-*/;

	public native JavaScriptObject runAddChildren()
	/*-{
		// Some initialization:
		assertEquals = function(a, b) {
			@org.geomajas.plugin.editing.jsapi.client.service.GwtTestJsGeometryIndex::jsAssertEquals(Ljava/lang/Object;Ljava/lang/Object;)(a, b);
		}
		var service = new $wnd.org.geomajas.plugin.editing.service.GeometryIndexService();

		// Test the addChildren method:
		var index = service.addChildren(null, "vertex", 1, 2);
		assertEquals("1", index.getValue() + "");
		assertEquals("true", index.hasChild() + "");
		assertEquals("geometry", index.getType());
		assertEquals("2", index.getChild().getValue() + "");
		assertEquals("false", index.getChild().hasChild() + "");
		assertEquals("vertex", index.getChild().getType());

		index = service.create("geometry", 3);
		assertEquals("3", index.getValue() + "");
		assertEquals("false", index.hasChild() + "");
		assertEquals("geometry", index.getType());

		index = service.addChildren(index, "edge", 4, 2);
		assertEquals("3", index.getValue() + "");
		assertEquals("true", index.hasChild() + "");
		assertEquals("geometry", index.getType());
		assertEquals("4", index.getChild().getValue() + "");
		assertEquals("true", index.getChild().hasChild() + "");
		assertEquals("geometry", index.getChild().getType());
		assertEquals("2", index.getChild().getChild().getValue() + "");
		assertEquals("false", index.getChild().getChild().hasChild() + "");
		assertEquals("edge", index.getChild().getChild().getType());
	}-*/;
}