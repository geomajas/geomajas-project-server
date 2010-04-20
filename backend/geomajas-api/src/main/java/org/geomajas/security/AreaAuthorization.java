/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.security;

import com.vividsolutions.jts.geom.Geometry;
import org.geomajas.global.Api;
import org.geomajas.global.UserImplemented;

/**
 * Authorizations based on area.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
@UserImplemented
public interface AreaAuthorization extends BaseAuthorization {

	/**
	 * Get the visible area for a layer.
	 * <p/>
	 * Note that returning null means that the entire layer is invisible.
	 * <p/>
	 * The return value should be consistent with the isLayerVisible() method. If isLayerVisible() is false,
	 * this method should return null. If isLayerVisible() is true, a Geometry should be returned.
	 * <p/>
	 * Geometry coordinates need to be in layer coordinate space.
	 *
	 * @param layerId layer id for which the visible area should be returned
	 * @return geometry which indicates the visible area or null when nothing is visible
	 */
	Geometry getVisibleArea(String layerId);

	/**
	 * Indicates whether features which only partly fall inside the visible area are still considered visible.
	 *
	 * @param layerId layer id for which the status needs to be checked
	 * @return true when any feature which intersects the visible area is considered visible, false when a feature
	 * 		needs to be inside the visible area to be considered visible.
	 */
	boolean isPartlyVisibleSufficient(String layerId);

	/**
	 * Get the area of the layer where updating features is allowed.
	 * <p/>
	 * Note that returning null means that the entire layer prohibits updates.
	 * <p/>
	 * The return value should be consistent with the isLayerUpdateAuthorized() method. If isLayerUpdateAuthorized() is
	 * false, this method should return null. If isLayerUpdateAuthorized() is true, a Geometry should be returned.
	 * <p/>
	 * The return value should also be consistent with getVisibleArea(). The returned area should be contained in the
	 * visible area.
	 * <p/>
	 * Geometry coordinates need to be in layer coordinate space.
	 *
	 * @param layerId layer id for which the area where updates are allowed should be returned
	 * @return geometry which indicates the updatable area or null when nothing is updatable
	 */
	Geometry getUpdateAuthorizedArea(String layerId);

	/**
	 * Indicates whether features which only partly fall inside the updatable area are still considered updatable.
	 *
	 * @param layerId layer id for which the status needs to be checked
	 * @return true when any feature which intersects the visible area is considered updatable, false when a feature
	 * 		needs to be inside the updatable area to be considered updatable.
	 */
	boolean isPartlyUpdateAuthorizedSufficient(String layerId);

	/**
	 * Get the area of the layer where creating features is allowed.
	 * <p/>
	 * Note that returning null means that the entire layer prohibits creation.
	 * <p/>
	 * The return value should be consistent with the isLayerCreateAuthorized() method. If isLayerCreateAuthorized() is
	 * false, this method should return null. If isLayerCreateAuthorized() is true, a Geometry should be returned.
	 * <p/>
	 * The return value should also be consistent with getVisibleArea(). The returned area should be contained in the
	 * visible area.
	 * <p/>
	 * Geometry coordinates need to be in layer coordinate space.
	 *
	 * @param layerId layer id for which the area where creating features is allowed should be returned
	 * @return geometry which indicates the area where creation is allowed or null when nothing can be created
	 */
	Geometry getCreateAuthorizedArea(String layerId);

	/**
	 * Indicates whether features which only partly fall inside the area where creation is allowed are still allowed
	 * to be created.
	 *
	 * @param layerId layer id for which the status needs to be checked
	 * @return true when any feature which intersects the createAuthorized area is allowed to be created, false when a
	 *      feature needs to be inside the creatable area to be created.
	 */
	boolean isPartlyCreateAuthorizedSufficient(String layerId);

	/**
	 * Get the area of the layer where deleting features is allowed.
	 * <p/>
	 * Note that returning null means that the entire layer prohibits deletion.
	 * <p/>
	 * The return value should be consistent with the isLayerDeleteAuthorized() method. If isLayerDeleteAuthorized() is
	 * false, this method should return null. If isLayerDeleteAuthorized() is true, a Geometry should be returned.
	 * <p/>
	 * The return value should also be consistent with getVisibleArea(). The returned area should be contained in the
	 * visible area.
	 * <p/>
	 * Geometry coordinates need to be in layer coordinate space.
	 *
	 * @param layerId layer id for which the area where creating features is allowed should be returned
	 * @return geometry which indicates the area in which deletion is allowed or null when nothing can be deleted
	 */
	Geometry getDeleteAuthorizedArea(String layerId);

	/**
	 * Indicates whether features which only partly fall inside the deletable area are still considered deletable.
	 *
	 * @param layerId layer id for which the status needs to be checked
	 * @return true when any feature which intersects the deletable area is considered deletable, false when a feature
	 * 		needs to be inside the deletable area to be considered deletable.
	 */
	boolean isPartlyDeleteAuthorizedSufficient(String layerId);
}
