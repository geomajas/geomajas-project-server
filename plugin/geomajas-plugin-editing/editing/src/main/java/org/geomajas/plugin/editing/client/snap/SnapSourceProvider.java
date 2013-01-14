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

package org.geomajas.plugin.editing.client.snap;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;
import org.geomajas.geometry.Bbox;
import org.geomajas.plugin.editing.client.GeometryArrayFunction;

/**
 * <p>
 * Definition of a source geometry provider for the snapping algorithms. In order to be able to snap, we need targets to
 * snap to. The <code>SnapSourceProvider</code> does exactly that. It provides geometries for the snapping algorithms to
 * snap to. Note that the list of target geometries can often be updated as the user navigates around the map. In order
 * to deal with this the <code>update</code> method is called to provide these <code>SnapSourceProvider</code> with the
 * current location on the map, so they can adjust their target geometry list.
 * </p>
 * <p>
 * Possible implementations may provide the geometries of the features of a vector layer, or provide a grid in world
 * space, or ...
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
@UserImplemented
public interface SnapSourceProvider {

	/**
	 * Fetch the target snapping geometries for the bounding box given through the <code>update</code> method.
	 * Implementations must give the geometries to the call-back. A call-back is used because the geometries might have
	 * to come from the server.
	 * 
	 * @param callback
	 *            The call-back method that actually does something with the target snapping geometries.
	 */
	void getSnappingSources(GeometryArrayFunction callback);

	/**
	 * Called by the snapping service whenever it feels the snapping providers need to update their list of target
	 * geometries.
	 * 
	 * @param mapBounds
	 *            The current view on the map.
	 */
	void update(Bbox mapBounds);
}