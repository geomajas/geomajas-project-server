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

import org.geomajas.annotation.Api;
import org.vaadin.gwtgraphics.client.VectorObjectContainer;

/**
 * Base container definition for vector object types. This interface is used for rendering vector layers, screen space
 * objects or world space objects.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api
public interface VectorContainer extends VectorObjectContainer {
	
	void setTranslation(double deltaX, double deltaY);
	
	void setScale(double scaleX, double scaleY);

	/**
	 * Determine container visibility.
	 * 
	 * @param visible
	 *            Set whether or not this container and all it's children should be visible or not.
	 */
	void setVisible(boolean visible);

	/**
	 * Get whether or not this container and all it's children are visible.
	 * 
	 * @return Whether or not this container and all it's children are visible.
	 */
	boolean isVisible();
}