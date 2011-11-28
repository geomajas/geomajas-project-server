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

package org.geomajas.plugin.editing.client.snapping;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Geometry;

/**
 * <p>
 * Definition of a source geometry provider for the snapping algorithms. In order to be able to snap, we need targets to
 * snap to. The <code>SnappingSourceProvider</code> does exactly that. It provides geometries for the snapping
 * algorithms to snap to. Note that the list of target geometries can often be updated as the user navigates around the
 * map. In order to deal ith this the <code>update</code> method is called to provide these
 * <code>SnappingSourceProvider</code> with the current location on the map, so they can adjust their target geometry
 * list.
 * </p>
 * <p>
 * Possible implementations may provide the geometries of the features of a vector layer, or provide a grid in world
 * space, or ...
 * </p>
 * 
 * @author Pieter De Graef
 */
public interface SnappingSourceProvider {

	/**
	 * Callback for passing along the geometries. Some implementations of the <code>SnappingSourceProvider</code> may
	 * have to fetch their geometries from the server, and thus a callback is required to support this.
	 * 
	 * @author Pieter De Graef
	 */
	public interface GeometryArrayCallback {

		/**
		 * Something needs to be done with the geometries no? The snapping service will implement this to pass along the
		 * geometries to the snapping algorithms.
		 * 
		 * @param geometries
		 *            The resulting list of geometries for the current bounding box given through the
		 *            <code>update</code> method.
		 */
		void execute(Geometry[] geometries);
	}

	/**
	 * Fetch the target snapping geometries for the bounding box given through the <code>update</code> method.
	 * Implementations must give the geometries to the callback.
	 * 
	 * @param callback
	 *            The callback method that actually does something with the target snapping geometries.
	 */
	void getSnappingSources(GeometryArrayCallback callback);

	/**
	 * Called by the snapping service whenever it feels the snapping providers need to update their list of target
	 * geometries.
	 * 
	 * @param mapBounds
	 *            The current view on the map.
	 */
	void update(Bbox mapBounds);
}