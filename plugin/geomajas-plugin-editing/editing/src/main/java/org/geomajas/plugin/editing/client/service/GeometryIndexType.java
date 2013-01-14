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

package org.geomajas.plugin.editing.client.service;

/**
 * The type of sub-part of a geometry that a {@link GeometryIndex} can point to. Supported parts are sub-geometries,
 * vertices and edges.
 * 
 * @author Pieter De Graef
 */
public enum GeometryIndexType {

	/** Points to the geometry itself or one of it's sub-geometries. */
	TYPE_GEOMETRY,

	/** Points to a vertex (coordinate) within a geometry. */
	TYPE_VERTEX,

	/** Points to an edge within a geometry. Actually an edge is defined by it's 2 end-vertices. */
	TYPE_EDGE
}