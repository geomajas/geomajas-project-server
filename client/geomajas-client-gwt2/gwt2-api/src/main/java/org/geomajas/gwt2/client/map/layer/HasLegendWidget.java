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

package org.geomajas.gwt2.client.map.layer;

import org.geomajas.annotation.Api;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * <p>
 * Layer extension that provides a way of building legend widgets. Often a legend will be represented by a single Image
 * widget, but some layers might be very creative in building these widgets.
 * </p>
 * <p>
 * The reason why this has be defined as a separate interface is that not all layers might provide a legend. Raster
 * based layer often do not have a legend, so no point in creating a widget for them.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface HasLegendWidget {

	/**
	 * Build a new widget that represents the legend.
	 * 
	 * @return The legend widget.
	 */
	IsWidget buildLegendWidget();
}