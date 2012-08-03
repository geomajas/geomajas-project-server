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
package org.geomajas.widget.advancedviews.configuration.client.themes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Configuration of the global viewconfig of theming on a map, this allows definition of different scale ranges.
 *
 * @author Oliver May
 */
public class ViewConfig implements Serializable {

	private static final long serialVersionUID = 100L;

	/**
	 * Icon that represents the global viewconfig (overridden by RangeConfig.icon)
	 */
	private String icon;

	/**
	 * Name of this viewconfig
	 */
	private String title;
	
	/**
	 * Different ranges defined in this Viewconfig.
	 */
	private List<RangeConfig> rangeConfigs = new ArrayList<RangeConfig>(0);

	/**
	 * Human readable description of this range.
	 */
	private String description;

	/**
	 * Is the theme is active by default. Note that the last theme with this property set will be active.
	 */
	private boolean activeByDefault;
	
	/**
	 * Different ranges defined in this Viewconfig. Note that the list of ranges is not checked against overlaps, so the
	 * first occurence within a specific viewscale is a hit.
	 *
	 * @param rangeConfigs the rangeConfigs to set
	 */
	public void setRangeConfigs(List<RangeConfig> rangeConfigs) {
		this.rangeConfigs = rangeConfigs;
	}

	/**
	 * @return the rangeConfigs
	 */
	public List<RangeConfig> getRangeConfigs() {
		return rangeConfigs;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param name the name to set
	 */
	public void setTitle(String name) {
		this.title = name;
	}

	/**
	 * @return the name
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param activeByDefault the activeByDefault to set
	 */
	public void setActiveByDefault(boolean activeByDefault) {
		this.activeByDefault = activeByDefault;
	}

	/**
	 * @return the activeByDefault
	 */
	public boolean isActiveByDefault() {
		return activeByDefault;
	}
}
