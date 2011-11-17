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
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;

/**
 * JavaScript exportable version of a {@link GeometryIndex}. It is the definition of an index in a geometry. This index
 * will point to a specific sub-part of a geometry. Depending on the "type", this sub-part can be a vertex, an edge or a
 * sub-geometry.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Export("GeometryIndex")
@ExportPackage("org.geomajas.plugin.editing.service")
@Api(allMethods = true)
public class JsGeometryIndex implements ExportOverlay<GeometryIndex> {

	@ExportInstanceMethod
	public static String getType(GeometryIndex instance) {
		switch (instance.getType()) {
			case TYPE_GEOMETRY:
				return "geometry";
			case TYPE_VERTEX:
				return "vertex";
			case TYPE_EDGE:
				return "edge";
			default:
				return "unknown";
		}
	}

	public boolean hasChild() {
		return false;
	}

	public GeometryIndex getChild() {
		return null;
	}

	public int getValue() {
		return 0;
	}

	public String toString() {
		return "";
	}
}