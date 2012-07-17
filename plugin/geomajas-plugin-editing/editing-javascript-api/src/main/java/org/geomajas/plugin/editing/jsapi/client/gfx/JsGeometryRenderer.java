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
package org.geomajas.plugin.editing.jsapi.client.gfx;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.editing.client.gfx.GeometryRenderer;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * JavaScript wrapper of {@link GeometryRenderer}.
 * 
 * @author Jan De Moerloose
 * 
 */
@Export("GeometryRenderer")
@ExportPackage("org.geomajas.plugin.editing.gfx")
@Api(allMethods = true)
public class JsGeometryRenderer implements Exportable {

	private final GeometryRenderer delegate;

	/**
	 * Constructor with a {@link GeometryRenderer} delegate.
	 * 
	 * @param delegate delegate
	 */
	public JsGeometryRenderer(GeometryRenderer delegate) {
		this.delegate = delegate;
	}

	/**
	 * Redraw the geometry to apply changes in the editor internal state.
	 */
	public void redraw() {
		delegate.redraw();
	}

	/**
	 * Make the edited geometry.visible/invisible.
	 * 
	 * @param visible true to make the geometry visible, false to make the geometry invisible 
	 */
	public void setVisible(boolean visible) {
		delegate.setVisible(visible);
	}

}
