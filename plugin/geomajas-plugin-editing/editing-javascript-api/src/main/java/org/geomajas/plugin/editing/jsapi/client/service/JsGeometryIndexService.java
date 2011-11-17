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
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;
import org.geomajas.plugin.editing.client.service.GeometryIndexService;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;

/**
 * Service for managing sub-parts of geometries through special geometry indices.
 * <p>
 * TODO All methods that return an array or a list have been excluded. We still need to get these to work...
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Export("GeometryIndexService")
@ExportPackage("org.geomajas.plugin.editing.service")
@Api(allMethods = true)
public class JsGeometryIndexService implements ExportOverlay<GeometryIndexService> {

	@ExportInstanceMethod
	public static GeometryIndex create(GeometryIndexService instance, String type, int... values) {
		if ("geometry".equalsIgnoreCase(type)) {
			return instance.create(GeometryIndexType.TYPE_GEOMETRY, values);
		} else if ("vertex".equalsIgnoreCase(type)) {
			return instance.create(GeometryIndexType.TYPE_VERTEX, values);
		} else if ("edge".equalsIgnoreCase(type)) {
			return instance.create(GeometryIndexType.TYPE_EDGE, values);
		}
		return null;
	}

	@ExportInstanceMethod
	public static GeometryIndex addChildren(GeometryIndexService instance, GeometryIndex index, String type,
			int... values) {
		if ("geometry".equalsIgnoreCase(type)) {
			return instance.addChildren(index, GeometryIndexType.TYPE_GEOMETRY, values);
		} else if ("vertex".equalsIgnoreCase(type)) {
			return instance.addChildren(index, GeometryIndexType.TYPE_VERTEX, values);
		} else if ("edge".equalsIgnoreCase(type)) {
			return instance.addChildren(index, GeometryIndexType.TYPE_EDGE, values);
		}
		return null;
	}

	public GeometryIndex create(GeometryIndexType type, int... values) {
		return null;
	}

	public GeometryIndex addChildren(GeometryIndex index, GeometryIndexType type, int... values) {
		return null;
	}

	public String format(GeometryIndex index) {
		return null;
	}

	public GeometryIndex parse(String identifier) throws GeometryIndexNotFoundException {
		return null;
	}

	public Geometry getGeometry(Geometry geometry, GeometryIndex index) throws GeometryIndexNotFoundException {
		return null;
	}

	public Coordinate getVertex(Geometry geometry, GeometryIndex index) throws GeometryIndexNotFoundException {
		return null;
	}

	//
	// public Coordinate[] getEdge(Geometry geometry, GeometryIndex index) throws GeometryIndexNotFoundException {
	// return null;
	// }

	public boolean isVertex(GeometryIndex index) {
		return false;
	}

	public boolean isEdge(GeometryIndex index) {
		return false;
	}

	public boolean isGeometry(GeometryIndex index) {
		return false;
	}

	@ExportInstanceMethod
	public static String getType(GeometryIndexService instance, GeometryIndex index) {
		switch (instance.getType(index)) {
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

	//
	// public GeometryIndexType getType(GeometryIndex index) {
	// return null;
	// }

	public String getGeometryType(Geometry geometry, GeometryIndex index) throws GeometryIndexNotFoundException {
		return null;
	}

	public boolean isChildOf(GeometryIndex parentIndex, GeometryIndex childIndex) {
		return false;
	}

	public int getValue(GeometryIndex index) {
		return 0;
	}

	//
	// public List<GeometryIndex> getAdjacentVertices(Geometry geometry, GeometryIndex index)
	// throws GeometryIndexNotFoundException {
	// return null;
	// }
	//
	// public List<GeometryIndex> getAdjacentEdges(Geometry geometry, GeometryIndex index)
	// throws GeometryIndexNotFoundException {
	// return null;
	// }

	public boolean isAdjacent(Geometry geometry, GeometryIndex one, GeometryIndex two) {
		return false;
	}

	public GeometryIndex getNextVertex(GeometryIndex index) {
		return null;
	}

	public GeometryIndex getPreviousVertex(GeometryIndex index) {
		return null;
	}

	public int getSiblingCount(Geometry geometry, GeometryIndex index) {
		return 0;
	}
	//
	// public Coordinate[] getSiblingVertices(Geometry geometry, GeometryIndex index)
	// throws GeometryIndexNotFoundException {
	// return null;
	// }
}