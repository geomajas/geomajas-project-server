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

package org.geomajas.puregwt.client.map.render;

import org.geomajas.annotation.Api;
import org.geomajas.puregwt.client.event.LayerOrderChangedHandler;
import org.geomajas.puregwt.client.event.LayerRefreshedHandler;
import org.geomajas.puregwt.client.event.LayerStyleChangedHandler;
import org.geomajas.puregwt.client.event.LayerVisibilityHandler;
import org.geomajas.puregwt.client.event.MapCompositionHandler;
import org.geomajas.puregwt.client.event.MapResizedHandler;
import org.geomajas.puregwt.client.event.ViewPortChangedHandler;

/**
 * General definition of an object that is responsible for making sure the map is always rendered correctly. How exactly
 * the implementation do this, is up to them. As a base they must react correctly to all ViewPort events. Therefore this
 * interface is an extension of the {@link ViewPortChangedHandler}.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface MapRenderer extends ViewPortChangedHandler, LayerOrderChangedHandler, LayerVisibilityHandler,
		LayerStyleChangedHandler, LayerRefreshedHandler, MapResizedHandler, MapCompositionHandler {

	/**
	 * Determine whether or not to use a bigger bounds when rendering the map. By using a bigger scale, more tiles will
	 * be rendered, which can be prettier while panning. Default value is 2, which means twice the map bounds.
	 * 
	 * @param scale
	 *            The scale factor to apply on the rendered map extent.
	 */
	void setMapExtentScaleAtFetch(double scale);

	/**
	 * Set the number of milliseconds an animated navigation sequence should last.
	 * 
	 * @param animationMillis
	 *            The number of milliseconds an animated navigation sequence should last.
	 */
	void setAnimationMillis(int animationMillis);

	/**
	 * Determine the number of layers (starting at the back) that should be animated while navigating. The other layers
	 * should be temporarily removed.
	 * 
	 * @param nrAnimatedLayers
	 */
	void setNrAnimatedLayers(int nrAnimatedLayers);
}