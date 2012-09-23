/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.widget.advancedviews.configuration.client;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.client.ClientWidgetInfo;
import org.geomajas.widget.advancedviews.configuration.client.themes.ViewConfig;

/**
 * Main entrypoint for configuring theme's. You can configure some basic information for the theme widget and provide a
 * list of views.
 * 
 * @author Oliver May
 * 
 */
public class ThemesInfo implements ClientWidgetInfo {

	/**
	 * Use this identifier in your configuration files (beans).
	 */
	public static final String IDENTIFIER = "ThemesInfo";

	private static final long serialVersionUID = 100L;

	private List<ViewConfig> themeConfigs = new ArrayList<ViewConfig>();

	private boolean hideOtherlayers; // false

	private boolean showDescription; // false

	private String descriptionWidth = "300px";

	/**
	 * Hide available layers that are not in the configuration when true.
	 * 
	 * @return if other layers should be hidden
	 */
	public boolean isHideOtherlayers() {
		return hideOtherlayers;
	}

	/**
	 * Hide available layers that are not in the configuration when true.
	 * 
	 * @param hideOtherlayers
	 *            should other layers be hidden
	 */
	public void setHideOtherlayers(boolean hideOtherlayers) {
		this.hideOtherlayers = hideOtherlayers;
	}

	/**
	 * Set the list of theme configurations.
	 * 
	 * @param themeConfigs
	 *            the themeConfigs to set
	 */
	public void setThemeConfigs(List<ViewConfig> themeConfigs) {
		this.themeConfigs = themeConfigs;
	}

	/**
	 * Get the list of theme configurations.
	 * 
	 * @return the themeConfigs
	 */
	public List<ViewConfig> getThemeConfigs() {
		return themeConfigs;
	}

	/**
	 * If true show description next to the button, if false show the tooltip.
	 * 
	 * @return true if description should be shown
	 */
	public boolean isShowDescription() {
		return showDescription;
	}

	/**
	 * If true show description next to the button, if false show the tooltip.
	 * 
	 * @param showDescription
	 *            should the description be shown
	 */
	public void setShowDescription(boolean showDescription) {
		this.showDescription = showDescription;
	}

	/**
	 * If true show description next to the button (false == tooltip).
	 * 
	 * @return the description.
	 */
	public String getDescriptionWidth() {
		return descriptionWidth;
	}

	/**
	 * Width of the label showing the description. Can be any css accepted width formulation (100px, 50%, 10em).
	 * 
	 * @param descriptionWidth
	 *            width of the description
	 */
	public void setDescriptionWidth(String descriptionWidth) {
		this.descriptionWidth = descriptionWidth;
	}

}
