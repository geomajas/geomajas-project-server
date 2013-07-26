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

package org.geomajas.gwt.client.map.layer;

import org.geomajas.annotation.Api;

/**
 * Extension for the layer interface which signifies that this particular layer can change in opacity.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface OpacitySupported {

	/**
	 * Apply a new opacity on the entire layer.
	 * 
	 * @param opacity
	 *            The new opacity value. Must be a value between 0 and 1, where 0 means invisible and 1 is totally
	 *            visible.
	 */
	void setOpacity(double opacity);

	/**
	 * Get the current opacity value for this layer. Must be a value between 0 and 1, where 0 means invisible and 1 is
	 * totally visible.
	 * 
	 * @return The current opacity value for this layer.
	 */
	double getOpacity();
}