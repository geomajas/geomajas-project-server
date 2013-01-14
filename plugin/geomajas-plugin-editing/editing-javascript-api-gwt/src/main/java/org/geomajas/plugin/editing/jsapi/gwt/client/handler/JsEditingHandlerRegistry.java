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
package org.geomajas.plugin.editing.jsapi.gwt.client.handler;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.editing.gwt.client.handler.EditingHandlerRegistry;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * JavaScript wrapper for {@link EditingHandlerRegistry}. This registry is exported as a global function by
 * {@link #getInstance()}.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 * 
 */
@Api(allMethods = true)
@Export("EditingHandlerRegistry")
@ExportPackage("org.geomajas.plugin.editing.handler")
public final class JsEditingHandlerRegistry implements Exportable {

	private static final JsEditingHandlerRegistry INSTANCE = new JsEditingHandlerRegistry();

	private JsEditingHandlerRegistry() {
	}

	/**
	 * Get a reference to the {@link EditingHandlerRegistry}.
	 * 
	 * @return the {@link JsEditingHandlerRegistry}.
	 */
	@Export("$wnd.EditingHandlerRegistry")
	public static JsEditingHandlerRegistry getInstance() {
		return INSTANCE;
	}

	/**
	 * Register a geometry handler factory. Make sure to call
	 * {@link org.geomajas.plugin.editing.jsapi.client.gfx.JsGeometryRenderer#redraw()} after all factories have been
	 * registered.
	 * 
	 * @param factory the geometry handler factory
	 */
	@Export
	public void addGeometryHandlerFactory(JsGeometryHandlerFactory factory) {
		EditingHandlerRegistry.addGeometryHandlerFactory(factory);
	}

	/**
	 * Unregister a geometry handler factory. Make sure to call
	 * {@link org.geomajas.plugin.editing.jsapi.client.gfx.JsGeometryRenderer#redraw()} after all factories have been
	 * unregistered.
	 * 
	 * @param factory the geometry handler factory
	 */
	@Export
	public void removeGeometryHandlerFactory(JsGeometryHandlerFactory factory) {
		EditingHandlerRegistry.removeGeometryHandlerFactory(factory);
	}

}
