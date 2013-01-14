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
 * Tests to see if the JavaScript for the GeometryIndexStateService actually works.
 * 
 * @author Pieter De Graef
 */
public class GwtTestJsGeometryIndexStateService extends GWTTestCase {

	@Override
	public String getModuleName() {
		return "org.geomajas.plugin.editing.jsapi.EditingJsApiTest";
	}

	public static <T> void jsAssertEquals(T a, T b) {
		assertEquals(a.toString(), b.toString());
	}

	public void testSelectionHandlers() {
		ExporterUtil.exportAll();
		runSelectionHandlers();
	}

	public void testSelection() {
		ExporterUtil.exportAll();
		runSelection();
	}

	public void testDisablingHandlers() {
		ExporterUtil.exportAll();
		runDisablingHandlers();
	}

	public void testEnableDisable() {
		ExporterUtil.exportAll();
		runEnableDisable();
	}

	public void testHighlightingHandlers() {
		ExporterUtil.exportAll();
		runHighlightingHandlers();
	}

	public void testHighlighting() {
		ExporterUtil.exportAll();
		runHighlighting();
	}

	public void testMarkForDeletionHandlers() {
		ExporterUtil.exportAll();
		runMarkForDeletionHandlers();
	}

	public void testMarkForDeletion() {
		ExporterUtil.exportAll();
		runMarkForDeletion();
	}

	// ------------------------------------------------------------------------
	// The actual JavaScript test methods:
	// ------------------------------------------------------------------------

	public native JavaScriptObject runSelectionHandlers()
	/*-{
		// Some initialization:
		assertEquals = function(a, b) {
			@org.geomajas.plugin.editing.jsapi.client.service.GwtTestJsGeometryIndexStateService::jsAssertEquals(Ljava/lang/Object;Ljava/lang/Object;)(a, b);
		}
		var service = new $wnd.org.geomajas.plugin.editing.service.GeometryEditService();
		var stateService = service.getIndexStateService();
		var indexService = service.getIndexService();

		var count = 0;

		stateService.addGeometryIndexSelectedHandler(function(event) {
			count++;
		});
		stateService.addGeometryIndexDeselectedHandler(function(event) {
			count--;
		});

		var index1 = indexService.create("vertex", 0);
		var index2 = indexService.create("vertex", 1);

		stateService.select([ index1, index2 ]);
		assertEquals("1", count + "");

		stateService.deselect([ index1 ]);
		assertEquals("0", count + "");
	}-*/;

	public native JavaScriptObject runSelection()
	/*-{
		// Some initialization:
		assertEquals = function(a, b) {
			@org.geomajas.plugin.editing.jsapi.client.service.GwtTestJsGeometryIndexStateService::jsAssertEquals(Ljava/lang/Object;Ljava/lang/Object;)(a, b);
		}
		var service = new $wnd.org.geomajas.plugin.editing.service.GeometryEditService();
		var stateService = service.getIndexStateService();
		var indexService = service.getIndexService();

		var index1 = indexService.create("vertex", 0);
		var index2 = indexService.create("vertex", 1);

		stateService.select([ index1, index2 ]);
		assertEquals("true", stateService.isSelected(index1));
		assertEquals("true", stateService.isSelected(index2));

		stateService.deselect([ index1 ]);
		assertEquals("false", stateService.isSelected(index1));
		assertEquals("true", stateService.isSelected(index2));

		stateService.deselectAll();
		assertEquals("false", stateService.isSelected(index1));
		assertEquals("false", stateService.isSelected(index2));
	}-*/;

	public native JavaScriptObject runDisablingHandlers()
	/*-{
		// Some initialization:
		assertEquals = function(a, b) {
			@org.geomajas.plugin.editing.jsapi.client.service.GwtTestJsGeometryIndexStateService::jsAssertEquals(Ljava/lang/Object;Ljava/lang/Object;)(a, b);
		}
		var service = new $wnd.org.geomajas.plugin.editing.service.GeometryEditService();
		var stateService = service.getIndexStateService();
		var indexService = service.getIndexService();

		var count = 0;

		stateService.addGeometryIndexEnabledHandler(function(event) {
			count--;
		});
		stateService.addGeometryIndexDisabledHandler(function(event) {
			count++;
		});

		var index1 = indexService.create("vertex", 0);
		var index2 = indexService.create("vertex", 1);

		stateService.disable([ index1 ]);
		assertEquals("1", count + "");

		stateService.enable([ index1, index2 ]);
		assertEquals("0", count + "");
	}-*/;

	public native JavaScriptObject runEnableDisable()
	/*-{
		// Some initialization:
		assertEquals = function(a, b) {
			@org.geomajas.plugin.editing.jsapi.client.service.GwtTestJsGeometryIndexStateService::jsAssertEquals(Ljava/lang/Object;Ljava/lang/Object;)(a, b);
		}
		var service = new $wnd.org.geomajas.plugin.editing.service.GeometryEditService();
		var stateService = service.getIndexStateService();
		var indexService = service.getIndexService();

		var index1 = indexService.create("vertex", 0);
		var index2 = indexService.create("vertex", 1);

		stateService.disable([ index1, index2 ]);
		assertEquals("false", stateService.isEnabled(index1));
		assertEquals("false", stateService.isEnabled(index2));

		stateService.enable([ index1 ]);
		assertEquals("true", stateService.isEnabled(index1));
		assertEquals("false", stateService.isEnabled(index2));

		stateService.enableAll();
		assertEquals("true", stateService.isEnabled(index1));
		assertEquals("true", stateService.isEnabled(index2));
	}-*/;

	public native JavaScriptObject runHighlightingHandlers()
	/*-{
		// Some initialization:
		assertEquals = function(a, b) {
			@org.geomajas.plugin.editing.jsapi.client.service.GwtTestJsGeometryIndexStateService::jsAssertEquals(Ljava/lang/Object;Ljava/lang/Object;)(a, b);
		}
		var service = new $wnd.org.geomajas.plugin.editing.service.GeometryEditService();
		var stateService = service.getIndexStateService();
		var indexService = service.getIndexService();

		var count = 0;

		stateService.addGeometryIndexHighlightBeginHandler(function(event) {
			count++;
		});
		stateService.addGeometryIndexHighlightEndHandler(function(event) {
			count--;
		});

		var index1 = indexService.create("vertex", 0);
		var index2 = indexService.create("vertex", 1);

		stateService.highlightBegin([ index1, index2 ]);
		assertEquals("1", count + "");

		stateService.highlightEnd([ index1 ]);
		assertEquals("0", count + "");
	}-*/;

	public native JavaScriptObject runHighlighting()
	/*-{
		// Some initialization:
		assertEquals = function(a, b) {
			@org.geomajas.plugin.editing.jsapi.client.service.GwtTestJsGeometryIndexStateService::jsAssertEquals(Ljava/lang/Object;Ljava/lang/Object;)(a, b);
		}
		var service = new $wnd.org.geomajas.plugin.editing.service.GeometryEditService();
		var stateService = service.getIndexStateService();
		var indexService = service.getIndexService();

		var index1 = indexService.create("vertex", 0);
		var index2 = indexService.create("vertex", 1);

		stateService.highlightBegin([ index1, index2 ]);
		assertEquals("true", stateService.isHightlighted(index1));
		assertEquals("true", stateService.isHightlighted(index2));

		stateService.highlightEnd([ index1 ]);
		assertEquals("false", stateService.isHightlighted(index1));
		assertEquals("true", stateService.isHightlighted(index2));

		stateService.highlightEndAll();
		assertEquals("false", stateService.isHightlighted(index1));
		assertEquals("false", stateService.isHightlighted(index2));
	}-*/;

	public native JavaScriptObject runMarkForDeletionHandlers()
	/*-{
		// Some initialization:
		assertEquals = function(a, b) {
			@org.geomajas.plugin.editing.jsapi.client.service.GwtTestJsGeometryIndexStateService::jsAssertEquals(Ljava/lang/Object;Ljava/lang/Object;)(a, b);
		}
		var service = new $wnd.org.geomajas.plugin.editing.service.GeometryEditService();
		var stateService = service.getIndexStateService();
		var indexService = service.getIndexService();

		var count = 0;

		stateService
				.addGeometryIndexMarkForDeletionBeginHandler(function(event) {
					count++;
				});
		stateService.addGeometryIndexMarkForDeletionEndHandler(function(event) {
			count--;
		});

		var index1 = indexService.create("vertex", 0);
		var index2 = indexService.create("vertex", 1);

		stateService.markForDeletionBegin([ index1, index2 ]);
		assertEquals("1", count + "");

		stateService.markForDeletionEnd([ index1 ]);
		assertEquals("0", count + "");
	}-*/;

	public native JavaScriptObject runMarkForDeletion()
	/*-{
		// Some initialization:
		assertEquals = function(a, b) {
			@org.geomajas.plugin.editing.jsapi.client.service.GwtTestJsGeometryIndexStateService::jsAssertEquals(Ljava/lang/Object;Ljava/lang/Object;)(a, b);
		}
		var service = new $wnd.org.geomajas.plugin.editing.service.GeometryEditService();
		var stateService = service.getIndexStateService();
		var indexService = service.getIndexService();

		var index1 = indexService.create("vertex", 0);
		var index2 = indexService.create("vertex", 1);

		stateService.markForDeletionBegin([ index1, index2 ]);
		assertEquals("true", stateService.isMarkedForDeletion(index1));
		assertEquals("true", stateService.isMarkedForDeletion(index2));

		stateService.markForDeletionEnd([ index1 ]);
		assertEquals("false", stateService.isMarkedForDeletion(index1));
		assertEquals("true", stateService.isMarkedForDeletion(index2));

		stateService.markForDeletionEndAll();
		assertEquals("false", stateService.isMarkedForDeletion(index1));
		assertEquals("false", stateService.isMarkedForDeletion(index2));
	}-*/;
}