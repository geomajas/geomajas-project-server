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

package org.geomajas.puregwt.widget.client.map;

import org.geomajas.annotation.Api;

import com.google.gwt.resources.client.CssResource;

/**
 * CSS resource bundle that contains styles for map related widgets.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface WidgetMapCssResource extends CssResource {

	/** Style used in the {@link LayerLegendPanel} for the outer DIV. */
	@ClassName("gm-LayerLegendPanel")
	String layerLegendPanel();

	/** Style used in the {@link LayerLegendPanel} for the title text. */
	@ClassName("gm-LayerLegendPanel-title")
	String layerLegendPanelTitle();
}