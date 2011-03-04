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

package org.geomajas.puregwt.client.map;

import org.geomajas.puregwt.client.map.event.LayerOrderChangedHandler;
import org.geomajas.puregwt.client.map.event.ViewPortChangedHandler;
import org.geomajas.puregwt.client.map.gfx.HtmlContainer;

/**
 * General definition of an object that is responsible for making sure the map is always rendered correctly. How exactly
 * the implementation do this, is up to them. As a base they must react correctly to all ViewPort events. Therefore this
 * interface is an extension of the {@link ViewPortChangedHandler}.
 * 
 * @author Pieter De Graef
 */
public interface MapRenderer extends ViewPortChangedHandler, LayerOrderChangedHandler {

	/**
	 * Determine whether or not to use a bigger bounds when rendering the map. By using a bigger scale, more tiles will
	 * be rendered, which can be prettier while panning. Default value is 2, which means twice the map bounds.
	 * 
	 * @param scale
	 *            The scale factor to apply on the rendered map extent.
	 */
	void setMapExentScaleAtFetch(double scale);

	void clear();

	void redraw();

	void setHtmlContainer(HtmlContainer htmlContainer);
}