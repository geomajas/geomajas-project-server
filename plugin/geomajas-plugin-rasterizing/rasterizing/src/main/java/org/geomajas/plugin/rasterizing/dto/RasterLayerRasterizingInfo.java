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
package org.geomajas.plugin.rasterizing.dto;

import org.geomajas.configuration.client.ClientWidgetInfo;

/**
 * Metadata DTO class that carries sufficient information to render a raster layer.
 * 
 * @author Jan De Moerloose
 * 
 */
public class RasterLayerRasterizingInfo implements ClientWidgetInfo, RasterizingConstants {

	private String cssStyle;

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

}
