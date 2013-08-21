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

package org.geomajas.plugin.editing.gwt.client.gfx;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;

/**
 * Factory definition that creates styles for geometry indices.
 * 
 * @author Pieter De Graef
 */
public interface GeometryIndexStyleFactory {

	/**
	 * Create a style for the given geometry index. The returned style must be associated with the state of the given
	 * geometry index.
	 * 
	 * @param editService
	 *            The geometry editing service.
	 * @param index
	 *            The index for which to return a style. This index will have a certain state (which will be fetched
	 *            from the edit service), and it is this state that will determine the style for the index. Also
	 *            different types of indices (vertex, edge, geometry) will receive different styles.
	 * @return The appropriate style.
	 * @throws GeometryIndexNotFoundException
	 *             Thrown in case the index can't be found or if the editing process hasn't started.
	 */
	FeatureStyleInfo create(GeometryEditService editService, GeometryIndex index) throws GeometryIndexNotFoundException;
}