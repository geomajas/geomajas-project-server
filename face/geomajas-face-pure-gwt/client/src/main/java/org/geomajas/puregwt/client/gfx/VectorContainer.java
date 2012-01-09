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

package org.geomajas.puregwt.client.gfx;

import org.geomajas.annotation.FutureApi;
import org.vaadin.gwtgraphics.client.VectorObjectContainer;

/**
 * Base container definition for vector object types. This interface is used for rendering vector layers, screen space
 * objects or world space objects.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@FutureApi(allMethods = true)
public interface VectorContainer extends VectorObjectContainer {

	/**
	 * Apply a translation onto the container. This will also translate everything in it.
	 * 
	 * @param deltaX
	 *            The translation factor along the X-axis.
	 * @param deltaY
	 *            The translation factor along the X-axis.
	 */
	void setTranslation(double deltaX, double deltaY);

	/**
	 * Apply a scaling factor onto the container. This scaling factor will apply on all children.
	 * 
	 * @param scaleX
	 *            The scaling factor along the X-axis.
	 * @param scaleY
	 *            The scaling factor along the X-axis.
	 */
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