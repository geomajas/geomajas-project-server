/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.printing.component.dto;

import java.io.Serializable;

import org.geomajas.annotation.Api;

/**
 * DTO object for RasterLayerComponent.
 * 
 * @author Jan De Moerloose
 * @see org.geomajas.plugin.printing.component.RasterLayerComponent
 * @since 2.0.0
 *
 */
@Api(allMethods = true)
public class RasterLayerComponentInfo extends BaseLayerComponentInfo implements Serializable {

	private static final long serialVersionUID = 200L;

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
