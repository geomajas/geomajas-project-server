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
 * Configuration properties for the LayerTreeWithLegend.
 *
 * @author Kristof Heirwegh
 */
public class LayerTreeWithLegendInfo implements ClientWidgetInfo {

	private static final long serialVersionUID = 180L;

	/**
	 * Use this identifier in your configuration files (beans).
	 */
	public static final String IDENTIFIER = "LayerTreeWithLegendInfo";
	
	/**
	 * The size of the legend icons in pixels, this is always square (so only
	 * one parameter).
	 * <p>
	 * The default value is 18
	 * </p>
	 */
	private int iconSize = 18;

	public int getIconSize() {
		return iconSize;
	}

	public void setIconSize(int iconSize) {
		this.iconSize = iconSize;
	}
}
