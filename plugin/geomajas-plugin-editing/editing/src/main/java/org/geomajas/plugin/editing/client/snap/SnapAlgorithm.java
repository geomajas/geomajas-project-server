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
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;

/**
 * <p>
 * Definition of a snapping algorithm. Implementations determine how snapping is done. For example, one could snap only
 * to end-points of a geometry, or snapping could include any point on the edges as well.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
@UserImplemented
public interface SnapAlgorithm {

	/**
	 * Set the full list of target geometries. These are the geometries where to this snapping algorithm can snap.
	 * 
	 * @param geometries
	 *            The list of target geometries.
	 */
	void setGeometries(Geometry[] geometries);

	/**
	 * Execute the snap operation.
	 * 
	 * @param coordinate
	 *            The original location.
	 * @param distance
	 *            The maximum distance allowed for snapping.
	 * @return The new location. If no snapping target was found, this may return the original location.
	 */
	Coordinate snap(Coordinate coordinate, double distance);

	/**
	 * Get the effective distance that was bridged during the snap operation. In case snapping occurred, this distance
	 * will be smaller than the given "distance" value during the last call to snap.
	 * 
	 * @return The effective snapping distance. Only valid if snapping actually occurred.
	 */
	double getCalculatedDistance();

	/**
	 * Has snapping actually occurred during the last call to the <code>snap</code> method? If so the returned snap
	 * location was different from the original location.
	 * 
	 * @return Returns if the returned location from the snap method differs from the original location.
	 */
	boolean hasSnapped();
}