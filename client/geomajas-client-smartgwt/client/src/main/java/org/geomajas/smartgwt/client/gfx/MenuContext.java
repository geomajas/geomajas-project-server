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

package org.geomajas.smartgwt.client.gfx;

import org.geomajas.geometry.Coordinate;
import org.geomajas.annotation.Api;

/**
 * Adds support for right-mouse context menu.
 * 
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api
public interface MenuContext {

	/**
	 * Retrieve the element name of the last right mouse event.
	 * 
	 * @return Returns the name of the element or null if no element found.
	 */
	String getRightButtonName();
	
	/**
	 * Retrieve the group object of the last right mouse event's group.
	 * 
	 * @return Returns the group or null if no group found
	 */
	Object getRightButtonObject();

	/**
	 * Retrieve the coordinate of the last right mouse event.
	 * 
	 * @return Returns the event's position.
	 * @since 1.6.0
	 */
	@Api
	Coordinate getRightButtonCoordinate();
}
