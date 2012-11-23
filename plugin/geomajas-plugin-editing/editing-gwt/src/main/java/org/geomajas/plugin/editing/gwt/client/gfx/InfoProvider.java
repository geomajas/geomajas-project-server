/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.editing.gwt.client.gfx;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;

/**
 * Callback interface for the panel that shows geometry information.
 * 
 * 
 * @author Jan De Moerloose
 * 
 */
public interface InfoProvider {

	/**
	 * Get the title of the informational panel.
	 * 
	 * @return the title
	 */
	String getTitle();

	/**
	 * Get the HTML content of the informational panel.
	 * 
	 * @param geometry the edited geometry
	 * @param dragPoint the drag point (or null if not dragging/inserting)
	 * @param startA the start point of the first drag line (or null if not dragging/inserting)
	 * @param startB the start point of the second drag line (or null if not dragging/inserting)
	 * @return the HTML content
	 */
	String getHtml(Geometry geometry, Coordinate dragPoint, Coordinate startA, Coordinate startB);

}
