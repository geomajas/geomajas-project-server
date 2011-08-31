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

package org.geomajas.puregwt.client.map.gfx;

import org.geomajas.global.FutureApi;
import org.vaadin.gwtgraphics.client.VectorObject;

/**
 * A {@link VectorObject} holder that knows how to apply world-to-screen scaling to the object being held.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@FutureApi
public interface WorldObject {

	/**
	 * Get the actual vector object.
	 * 
	 * @return the vector object
	 */
	VectorObject getVectorObject();

	/**
	 * 
	 * Scale the actual vector object to screen space. Implementations should at least scale their position to appear on
	 * the map.
	 * 
	 * @param scaleX scale in pixels per world unit
	 * @param scaleY scale in pixels per world unit (negative, also contains y-inversion)
	 */
	void scaleToScreen(double scaleX, double scaleY);

}
