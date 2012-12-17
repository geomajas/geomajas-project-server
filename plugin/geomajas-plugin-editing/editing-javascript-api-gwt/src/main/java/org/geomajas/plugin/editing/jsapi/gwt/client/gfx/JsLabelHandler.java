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
package org.geomajas.plugin.editing.jsapi.gwt.client.gfx;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.editing.gwt.client.GeometryEditor;
import org.geomajas.plugin.editing.gwt.client.handler.LabelDragLineHandler;
import org.geomajas.plugin.editing.jsapi.gwt.client.JsGeometryEditor;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;


/**
 * JavaScript wrapper of {@link LabelDragLineHandler}.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 * 
 */
@Export("LabelHandler")
@ExportPackage("org.geomajas.plugin.editing.gfx")
@Api(allMethods = true)
public class JsLabelHandler implements Exportable {

	private LabelDragLineHandler delegate;
	
	private GeometryEditor editor;
	
	/**
	 * Needed for exporter.
	 */
	public JsLabelHandler() {
	}

	@Export
	public JsLabelHandler(JsGeometryEditor jsEditor) {
		editor = jsEditor.getDelegate();
		delegate = new LabelDragLineHandler(editor.getMapWidget(), editor.getEditService()) ;
	}

	public void register() {
		delegate.register();
	}

	public void unregister() {
		delegate.unregister();
	}
	
	public boolean isRegistered() {
		return delegate.isRegistered();
	}

}
