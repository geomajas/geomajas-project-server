/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.rasterizing.command.dto;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.client.ClientWidgetInfo;

/**
 * Metadata DTO class that carries extra metadata information to render a raster layer.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public class RasterLayerRasterizingInfo implements ClientWidgetInfo, RasterizingConstants {

	private static final long serialVersionUID = 100L;

	private String cssStyle;

	// default showing
	private boolean showing = true;

	/**
	 * Returns the css style to be applied to this layer.
	 * 
	 * @return css style string
	 */
	public String getCssStyle() {
		return cssStyle;
	}

	/**
	 * Sets the css style to be applied to this layer.
	 * 
	 * @param cssStyle
	 *            the css style string
	 */
	public void setCssStyle(String cssStyle) {
		this.cssStyle = cssStyle;
	}

	/**
	 * Get the showing status of this layer. If true, the layer will be rendered, if false not.
	 * 
	 * @return The showing status of this layer
	 */
	public boolean isShowing() {
		return showing;
	}

	/**
	 * Sets the showing status of this layer. If true, the layer will be rendered, if false not.
	 * 
	 * @param showing
	 *            showing status of this layer
	 */
	public void setShowing(boolean showing) {
		this.showing = showing;
	}

}
