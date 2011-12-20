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

package org.geomajas.plugin.editing.jsapi.gwt.client;

import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.editing.gwt.client.GeometryEditor;
import org.geomajas.plugin.editing.jsapi.client.service.JsGeometryEditService;
import org.geomajas.plugin.jsapi.client.map.Map;
import org.geomajas.plugin.jsapi.smartgwt.client.exporter.map.MapImpl;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportConstructor;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;
import org.timepedia.exporter.client.NoExport;

/**
 * Central geometry editor for the JavaScript API on top of the GWT face.
 * 
 * @author Pieter De Graef
 */
@Export("GeometryEditor")
@ExportPackage("org.geomajas.plugin.editing")
public class JsGeometryEditor implements Exportable {

	private MapImpl map;

	private GeometryEditor delegate;

	private JsGeometryEditService editingService;

	// Needed for GWT exporter...
	public JsGeometryEditor() {
	}

	@NoExport
	public JsGeometryEditor(Map map) {
		this.map = (MapImpl) map;
		MapWidget mapWidget = this.map.getMapWidget();
		delegate = new GeometryEditor(mapWidget);
		editingService = new JsGeometryEditService(delegate.getService());
	}

	@ExportConstructor
	public static JsGeometryEditor createEditor(Map map) {
		return new JsGeometryEditor(map);
	}

	public MapImpl getMap() {
		return map;
	}

	public JsGeometryEditService getService() {
		return editingService;
	}

	public boolean isZoomOnStart() {
		return delegate.isZoomOnStart();
	}

	public void setZoomOnStart(boolean zoomOnStart) {
		delegate.setZoomOnStart(zoomOnStart);
	}

	public boolean isSnapOnDrag() {
		return delegate.isSnapOnDrag();
	}

	public void setSnapOnDrag(boolean snapOnDrag) {
		delegate.setSnapOnDrag(snapOnDrag);
	}

	public boolean isSnapOnInsert() {
		return delegate.isSnapOnInsert();
	}

	public void setSnapOnInsert(boolean snapOnInsert) {
		delegate.setSnapOnInsert(snapOnInsert);
	}
	
	public boolean isBusyEditing() {
		return delegate.isBusyEditing();
	}
}