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

package org.geomajas.plugin.editing.jsapi.gwt.client;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.SnappingRuleInfo;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.editing.gwt.client.GeometryEditor;
import org.geomajas.plugin.editing.gwt.client.GeometryEditorImpl;
import org.geomajas.plugin.editing.gwt.client.snap.SnapRuleUtil;
import org.geomajas.plugin.editing.jsapi.client.gfx.JsGeometryRenderer;
import org.geomajas.plugin.editing.jsapi.client.merge.JsGeometryMergeService;
import org.geomajas.plugin.editing.jsapi.client.service.JsGeometryEditService;
import org.geomajas.plugin.editing.jsapi.client.split.JsGeometrySplitService;
import org.geomajas.plugin.editing.jsapi.gwt.client.gfx.JsStyleService;
import org.geomajas.plugin.jsapi.client.map.Map;
import org.geomajas.plugin.jsapi.gwt.client.exporter.map.MapImpl;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;
import org.timepedia.exporter.client.NoExport;

/**
 * Central geometry editor for the JavaScript API on top of the GWT face.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Export("GeometryEditor")
@ExportPackage("org.geomajas.plugin.editing")
@Api(allMethods = true)
public class JsGeometryEditor implements Exportable {

	private MapWidget mapWidget;

	private MapImpl map;

	private GeometryEditorImpl delegate;

	private JsGeometryEditService editingService;

	private JsGeometrySplitService splitService;

	private JsGeometryMergeService mergeService;

	private JsStyleService styleService;
	
	private JsGeometryRenderer renderer;

	/**
	 * Needed for Gwt exporter.
	 */
	public JsGeometryEditor() {
	}

	/**
	 * Set the map.
	 * 
	 * @param map map
	 */
	public void setMap(Map map) {
		this.map = (MapImpl) map;
		mapWidget = this.map.getMapWidget();
		delegate = new GeometryEditorImpl(mapWidget);
		editingService = new JsGeometryEditService(delegate.getEditService());
		splitService = new JsGeometrySplitService(delegate.getEditService());
		mergeService = new JsGeometryMergeService();
		renderer = new JsGeometryRenderer(delegate.getRenderer());
		styleService = new JsStyleService(delegate.getStyleService());
	}

	/**
	 * Add the list of snapping rules as they are configured for a specific layer within the XML configuration.
	 * 
	 * @param layerId
	 *            The vector layer to use the configuration from.
	 */
	public void addLayerSnappingRules(String layerId) {
		Layer<?> layer = mapWidget.getMapModel().getLayer(layerId);
		if (layer != null && layer instanceof VectorLayer) {
			VectorLayer vLayer = (VectorLayer) layer;
			for (SnappingRuleInfo snappingRuleInfo : vLayer.getLayerInfo().getSnappingRules()) {
				SnapRuleUtil.addRule(delegate.getSnappingService(), mapWidget, snappingRuleInfo);
			}
		}
	}

	/**
	 * Get the map.
	 * 
	 * @return the map
	 */
	public MapImpl getMap() {
		return map;
	}
	
	/**
	 * Get wrapper for the geometry renderer.
	 * 
	 * @return the renderer
	 */
	public JsGeometryRenderer getRenderer() {
		return renderer;
	}

	/**
	 * Get wrapper for the edit service.
	 * 
	 * @return the renderer
	 */
	public JsGeometryEditService getService() {
		return editingService;
	}

	/**
	 * Get wrapper for the splitting service.
	 * 
	 * @return the renderer
	 */
	public JsGeometrySplitService getSplitService() {
		return splitService;
	}

	/**
	 * Get wrapper for the merging service.
	 * 
	 * @return the renderer
	 */
	public JsGeometryMergeService getMergeService() {
		return mergeService;
	}

	/**
	 * Get wrapper for the style service.
	 * 
	 * @return the renderer
	 */
	public JsStyleService getStyleService() {
		return styleService;
	}

	/**
	 * Should we zoom to the geometry when editing starts ?
	 * 
	 * @return true when zooming, false otherwise
	 */
	public boolean isZoomOnStart() {
		return delegate.isZoomOnStart();
	}

	/**
	 * @see #isZoomOnStart()
	 * @param zoomOnStart
	 */
	public void setZoomOnStart(boolean zoomOnStart) {
		delegate.setZoomOnStart(zoomOnStart);
	}

	/**
	 * Should snapping be enabled while dragging ?
	 * 
	 * @return true when enabled, false otherwise
	 */
	public boolean isSnapOnDrag() {
		return delegate.isSnapOnDrag();
	}

	/**
	 * @see #isSnapOnDrag()
	 * @param snapOnDrag
	 */
	public void setSnapOnDrag(boolean snapOnDrag) {
		delegate.setSnapOnDrag(snapOnDrag);
	}

	/**
	 * Should snapping be enabled while inserting ?
	 * 
	 * @return true when enabled, false otherwise
	 */
	public boolean isSnapOnInsert() {
		return delegate.isSnapOnInsert();
	}

	/**
	 * @see #isSnapOnInsert()
	 * @param snapOnInsert
	 */
	public void setSnapOnInsert(boolean snapOnInsert) {
		delegate.setSnapOnInsert(snapOnInsert);
	}

	/**
	 * Is editing busy ?
	 * 
	 * @return true when busy, false otherwise
	 */
	public boolean isBusyEditing() {
		return delegate.isBusyEditing();
	}
	
	@NoExport
	public GeometryEditor getDelegate() {
		return delegate;
	}

}