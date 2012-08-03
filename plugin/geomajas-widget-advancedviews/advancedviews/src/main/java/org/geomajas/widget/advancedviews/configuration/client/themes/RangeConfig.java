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

import org.geomajas.configuration.client.ScaleInfo;


/**
 * Configuration of a scale range in map theming, this allows the visibility configuration of a set of layers.
 * 
 * @author Oliver May
 *
 */
public class RangeConfig implements Serializable {

	private static final long serialVersionUID = 100L;
	/**
	 * Icon that represents the viewconfig within this range.
	 */
	private String icon;
	
	/**
	 * The scale from which this range becomes active (zoomed out).
	 */
	private ScaleInfo minimumScale = new ScaleInfo(ScaleInfo.MINIMUM_PIXEL_PER_UNIT);
	
	/**
	 * The scale to which this range is active (zoomed in).
	 */
	private ScaleInfo maximumScale = new ScaleInfo(ScaleInfo.MAXIMUM_PIXEL_PER_UNIT);;
	
	/**
	 * Configuration of layers within this range.
	 */
	private List<LayerConfig> layerConfigs = new ArrayList<LayerConfig>(0);

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * @return the layerConfigs
	 */
	public List<LayerConfig> getLayerConfigs() {
		return layerConfigs;
	}

	/**
	 * @param layerConfigs the layerConfigs to set
	 */
	public void setLayerConfigs(List<LayerConfig> layerConfigs) {
		this.layerConfigs = layerConfigs;
	}

	
	/**
	 * @return the minimumScale
	 */
	public ScaleInfo getMinimumScale() {
		return minimumScale;
	}

	
	/**
	 * @param minimumScale the minimumScale to set
	 */
	public void setMinimumScale(ScaleInfo minimumScale) {
		this.minimumScale = minimumScale;
	}

	
	/**
	 * @return the maximumScale
	 */
	public ScaleInfo getMaximumScale() {
		return maximumScale;
	}

	
	/**
	 * @param maximumScale the maximumScale to set
	 */
	public void setMaximumScale(ScaleInfo maximumScale) {
		this.maximumScale = maximumScale;
	}
	
	
}
