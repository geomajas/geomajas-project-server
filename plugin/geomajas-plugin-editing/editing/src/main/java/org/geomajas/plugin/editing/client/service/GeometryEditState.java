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

import org.geomajas.annotation.Api;

/**
 * General editing state for the {@link GeometryEditService}. This state determines which controller is currently active
 * on the map, and thus changes the behavior.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public enum GeometryEditState {

	/** The idle state. This is the default state while editing. */
	IDLE,

	/**
	 * The dragging state is activated when the user is actually dragging one or more vertices/edges/sub-geometries
	 * during editing.
	 */
	DRAGGING,

	/**
	 * The inserting state is used when the user is inserting new vertices into a geometry. The default behavior works
	 * so that inserting vertices is done by clicking on the map. Each click will insert a new vertex.
	 */
	INSERTING
}