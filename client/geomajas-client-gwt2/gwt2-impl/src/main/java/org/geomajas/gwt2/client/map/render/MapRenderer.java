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

package org.geomajas.gwt2.client.map.render;

import org.geomajas.gwt2.client.event.LayerOrderChangedHandler;
import org.geomajas.gwt2.client.event.LayerRefreshedHandler;
import org.geomajas.gwt2.client.event.LayerStyleChangedHandler;
import org.geomajas.gwt2.client.event.LayerVisibilityHandler;
import org.geomajas.gwt2.client.event.MapCompositionHandler;
import org.geomajas.gwt2.client.event.MapResizedHandler;
import org.geomajas.gwt2.client.event.ViewPortChangedHandler;
import org.geomajas.gwt2.client.event.ViewPortChangingHandler;

/**
 * General definition of an object that is responsible for making sure the map is always rendered correctly. How exactly
 * the implementation do this, is up to them. As a base they must react correctly to all ViewPort events. Therefore this
 * interface is an extension of the {@link ViewPortChangedHandler}.
 * 
 * @author Pieter De Graef
 */
public interface MapRenderer extends ViewPortChangedHandler, ViewPortChangingHandler, LayerOrderChangedHandler,
		LayerVisibilityHandler, LayerStyleChangedHandler, LayerRefreshedHandler, MapResizedHandler,
		MapCompositionHandler {
}