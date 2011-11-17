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

package org.geomajas.plugin.editing.jsapi.client.service;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportConstructor;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;

/**
 * ...
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Export("GeometryOperationFailedException")
@ExportPackage("org.geomajas.plugin.editing.service")
@Api(allMethods = true)
public class JsGeometryOperationFailedException implements ExportOverlay<GeometryOperationFailedException> {

	@ExportConstructor
	public static GeometryOperationFailedException create(String message) {
		return new GeometryOperationFailedException(message);
	}

	public String getMessage() {
		return "";
	}
}