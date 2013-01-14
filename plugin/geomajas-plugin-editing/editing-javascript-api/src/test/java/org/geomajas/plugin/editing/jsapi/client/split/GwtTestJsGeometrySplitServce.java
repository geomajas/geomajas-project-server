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

package org.geomajas.plugin.editing.jsapi.client.split;

import org.timepedia.exporter.client.ExporterUtil;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.junit.client.GWTTestCase;

/**
 * Tests to see if the JavaScript for the {@link org.geomajas.plugin.editing.client.split.GeometrySplitService} actually
 * works.
 * 
 * @author Pieter De Graef
 */
public class GwtTestJsGeometrySplitServce extends GWTTestCase {

	@Override
	public String getModuleName() {
		// return "org.geomajas.plugin.editing.jsapi.EditingJavascriptApi";
		return "org.geomajas.plugin.editing.jsapi.EditingJsApiTest";
	}

	public static <T> void jsAssertEquals(T a, T b) {
		assertEquals(a.toString(), b.toString());
	}

	public void testWorkflow() {
		ExporterUtil.exportAll();
		runWorkflow();
	}

	public native JavaScriptObject runWorkflow()
	/*-{
		// Some initialization:
		assertEquals = function(a, b) {
			@org.geomajas.plugin.editing.jsapi.client.split.GwtTestJsGeometrySplitServce::jsAssertEquals(Ljava/lang/Object;Ljava/lang/Object;)(a, b);
		}
		var editService = new $wnd.org.geomajas.plugin.editing.service.GeometryEditService();
		var service = new $wnd.org.geomajas.plugin.editing.split.GeometrySplitService(editService);

		var ring = new $wnd.org.geomajas.jsapi.spatial.Geometry("LinearRing",
				0, 0);
		ring.setCoordinates([
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(10, 10),
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(20, 10),
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(20, 20),
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(10, 10) ]);
		var polygon = new $wnd.org.geomajas.jsapi.spatial.Geometry("Polygon",
				0, 0);
		polygon.setGeometries([ ring ]);
		
		var splitLine = new $wnd.org.geomajas.jsapi.spatial.Geometry("LineString",
				0, 0);
		splitLine.setCoordinates([
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(5, 15),
				new $wnd.org.geomajas.jsapi.spatial.Coordinate(25, 15) ]);
				

		service.start(polygon);
		
		service.getGeometryEditService().start(splitLine);
		
		service.stop(null);

		//assertEquals("false", service.isBusy() + "");
	}-*/;
}