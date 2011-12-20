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

package org.geomajas.plugin.editing.jsapi.client.merging;

import org.geomajas.plugin.editing.client.merge.GeometryMergeService;
import org.timepedia.exporter.client.ExporterUtil;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.junit.client.GWTTestCase;

/**
 * Tests to see if the JavaScript for the {@link GeometryMergeService} actually works.
 * 
 * @author Pieter De Graef
 */
public class GwtTestJsGeometryMergingServce extends GWTTestCase {

	@Override
	public String getModuleName() {
		// return "org.geomajas.plugin.editing.jsapi.EditingJavascriptApi";
		return "org.geomajas.plugin.editing.jsapi.EditingJsApiTest";
	}

	public static <T> void jsAssertEquals(T a, T b) {
		assertEquals(a.toString(), b.toString());
	}

	public void testIsBusy() {
		ExporterUtil.exportAll();
		runIsBusy();
	}

	public void testWorkflowHandlers() {
		ExporterUtil.exportAll();
		runWorkflowHandlers();
	}

	public void testMerge() {
		ExporterUtil.exportAll();
		runMerge();
	}

	public native JavaScriptObject runIsBusy()
	/*-{
		// Some initialization:
		assertEquals = function(a, b) {
			@org.geomajas.plugin.editing.jsapi.client.merging.GwtTestJsGeometryMergingServce::jsAssertEquals(Ljava/lang/Object;Ljava/lang/Object;)(a, b);
		}
		var service = new $wnd.org.geomajas.plugin.editing.merging.GeometryMergingService();

		assertEquals("false", service.isBusy() + "");
		service.start();
		assertEquals("true", service.isBusy() + "");
		service.cancel();
		assertEquals("false", service.isBusy() + "");
	}-*/;

	public native JavaScriptObject runWorkflowHandlers()
	/*-{
		// Some initialization:
		assertEquals = function(a, b) {
			@org.geomajas.plugin.editing.jsapi.client.merging.GwtTestJsGeometryMergingServce::jsAssertEquals(Ljava/lang/Object;Ljava/lang/Object;)(a, b);
		}
		var service = new $wnd.org.geomajas.plugin.editing.merging.GeometryMergingService();
		var active = false;

		service.addGeometryMergingStartHandler(function(event) {
			active = true;
		});
		service.addGeometryMergingStopHandler(function(event) {
			active = false;
		});

		service.start();
		assertEquals("true", active + "");
		service.cancel();
		assertEquals("false", active + "");
		service.start();
		assertEquals("true", active + "");
		service.stop();
		assertEquals("false", active + "");
	}-*/;

	public native JavaScriptObject runMerge()
	/*-{
		// Some initialization:
		assertEquals = function(a, b) {
			@org.geomajas.plugin.editing.jsapi.client.merging.GwtTestJsGeometryMergingServce::jsAssertEquals(Ljava/lang/Object;Ljava/lang/Object;)(a, b);
		}
		var service = new $wnd.org.geomajas.plugin.editing.merging.GeometryMergingService();
		var ring = new $wnd.org.geomajas.jsapi.spatial.Geometry("LineString",
				0, 0);
		ring.setCoordinates([
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(10, 10),
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(20, 10),
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(20, 20),
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(10, 10) ]);

		var point = new $wnd.org.geomajas.jsapi.spatial.Geometry("Point", 0, 0);

		var count = 0;

		service.addGeometryMergingAddedHandler(function(event) {
			count++;
		});
		service.addGeometryMergingRemovedHandler(function(event) {
			count--;
		});

		service.start();
		assertEquals("0", count + "");

		service.addGeometry(ring);
		assertEquals("1", count + "");

		service.addGeometry(point);
		assertEquals("2", count + "");

		service.removeGeometry(point);
		assertEquals("1", count + "");

		service.clearGeometries();
		assertEquals("0", count + "");

		service.addGeometry(ring);
		assertEquals("1", count + "");

		service.cancel();
		assertEquals("0", count + "");
	}-*/;
}