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


/**
 * Implemented by complex paintable objects that will be represented by a group in the map.
 * 
 * @author Jan De Moerloose
 */
public interface PaintableGroup extends Paintable {

	/**
	 * Returns a nice name for the group to use in the DOM, not necessarily unique.
	 * 
	 * @return name
	 */
	String getGroupName();
}
