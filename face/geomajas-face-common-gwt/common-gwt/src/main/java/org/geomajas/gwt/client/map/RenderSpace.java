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

package org.geomajas.gwt.client.map;

import org.geomajas.annotation.Api;

/**
 * <p>
 * Listing of all 'spaces' wherein the map presenter can render objects. This render space definition is mainly used in
 * the {@link ViewPort} which has method for transforming geometries between the different spaces.
 * </p>
 * <p>
 * Use cases where the render spaces are used often require the transformation from one space to another. For example,
 * when the location of a mouse event is captured on the map, it will be expressed in screen space (pixels). In order
 * know it's real world location, one would have to transform this coordinate to world space. This position could than
 * be applied on the {@link ViewPort} to have the map zoom or translate to it.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public enum RenderSpace {

	/**
	 * <p>
	 * World space describes a rendering space where all objects are expressed in the coordinate reference system of the
	 * map they are drawn in. As a result, all objects within world space move about with the view on the map.
	 * </p>
	 * <p>
	 * Let's say for example that a rectangle is rendered on a map with CRS lon-lat. The rectangle has origin (118,34)
	 * and width and height both equal to 1. Than this rectangle will cover the city of Los Angeles.
	 * </p>
	 */
	WORLD,

	/**
	 * <p>
	 * Screen space describes a rendering space where all objects are expressed in pixels with the origin in the top
	 * left corner of the map. Objects rendered in screen will always occupy a fixed position on the map. They are
	 * immobile and are not affected by changes in the map view.
	 * </p>
	 */
	SCREEN
}