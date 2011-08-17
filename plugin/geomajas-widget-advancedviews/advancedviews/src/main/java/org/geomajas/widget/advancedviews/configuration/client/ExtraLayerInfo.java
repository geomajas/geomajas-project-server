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
package org.geomajas.widget.advancedviews.configuration.client;

import org.geomajas.configuration.client.ClientWidgetInfo;

/**
 * Configuration properties used by LayerTreeWithLegend and LayerInfo.
 * 
 * @author Kristof Heirwegh
 */
public class ExtraLayerInfo implements ClientWidgetInfo {

	private static final long serialVersionUID = 180L;

	/**
	 * Use this identifier in your configuration files (beans).
	 */
	public static final String IDENTIFIER = "ExtraLayerInfo";

	/**
	 * Used in LayerTreeWithLegend (RasterLayer only).
	 */
	private String smallLayerIconUrl;

	/**
	 * Used in LayerInfo widget.
	 */
	private String largeLayerIconUrl;

	/**
	 * Used by LayerInfo widget (RasterLayer only).
	 */
	private String legendImageUrl;

	public String getSmallLayerIconUrl() {
		return smallLayerIconUrl;
	}

	public void setSmallLayerIconUrl(String smallLayerIconUrl) {
		this.smallLayerIconUrl = smallLayerIconUrl;
	}

	public String getLargeLayerIconUrl() {
		return largeLayerIconUrl;
	}

	public void setLargeLayerIconUrl(String largeLayerIconUrl) {
		this.largeLayerIconUrl = largeLayerIconUrl;
	}

	public String getLegendImageUrl() {
		return legendImageUrl;
	}

	public void setLegendImageUrl(String legendImageUrl) {
		this.legendImageUrl = legendImageUrl;
	}
}
