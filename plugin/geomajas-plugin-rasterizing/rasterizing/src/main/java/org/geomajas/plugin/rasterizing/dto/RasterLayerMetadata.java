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

/**
 * Metadata DTO class that carries sufficient information to render a raster layer.
 * 
 * @author Jan De Moerloose
 * 
 */
public class RasterLayerMetadata implements LayerMetadata {

	private String layerId;

	private String rasterStyle;

	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	public String getRasterStyle() {
		return rasterStyle;
	}

	public void setRasterStyle(String rasterStyle) {
		this.rasterStyle = rasterStyle;
	}

}
