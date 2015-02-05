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
package org.geomajas.configuration.client;

import org.geomajas.annotation.Api;

/**
 * Client side raster layer metadata.
 * 
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
public class ClientRasterLayerInfo extends ClientLayerInfo {

	private static final long serialVersionUID = 160L;

	private String style = "";

	/**
	 * Returns the opacity of the raster layer.
	 * 
	 * @return the opacity of the raster layer.
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * Sets the opacity of the raster layer.
	 * 
	 * @param style the opacity as a string (eg '0.5')
	 */
	public void setStyle(String style) {
		this.style = style;
	}

}
