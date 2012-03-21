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

/**
 * Container definition for vector objects that should be rendered in world space.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@FutureApi(allMethods = true)
public interface WorldVectorContainer extends VectorContainer {

	/**
	 * Use a fixed size (in pixels) for the objects within this world container. This has no effect on the automatic
	 * positioning of objects in world space.
	 * 
	 * @param useFixedSize
	 *            If true, objects will receive a fixed size. In this case, the size should be expressed in pixels. If
	 *            false, the objects will resize a scale size, based upon the map scale. In this case, the size should
	 *            be expressed in world coordinates.
	 */
	void setUseFixedSize(boolean useFixedSize);

	/**
	 * Does this container use fixed sizing or not?
	 * 
	 * @return Does this container use fixed sizing or not?
	 */
	boolean isUseFixedSize();
}