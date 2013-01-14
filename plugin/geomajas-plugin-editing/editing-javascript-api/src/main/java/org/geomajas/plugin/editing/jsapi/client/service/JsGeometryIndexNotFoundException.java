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

import org.geomajas.annotation.Api;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportConstructor;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;

/**
 * Exception that is thrown when a certain {@link JsGeometryIndex} could not be found within a certain
 * {@link org.geomajas.geometry.Geometry}. Used mainly in the {@link JsGeometryIndexService}.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Export("GeometryIndexNotFoundException")
@ExportPackage("org.geomajas.plugin.editing.service")
@Api(allMethods = true)
public class JsGeometryIndexNotFoundException implements ExportOverlay<GeometryIndexNotFoundException> {

	/**
	 * Create a new exception with the given message.
	 * 
	 * @param message
	 *            The exception message.
	 * @return The exception.
	 */
	@ExportConstructor
	public static GeometryIndexNotFoundException create(String message) {
		return new GeometryIndexNotFoundException(message);
	}

	/**
	 * Return the exception message.
	 * 
	 * @return The exception message.
	 */
	public String getMessage() {
		return "";
	}
}