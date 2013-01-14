/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.service.legend;

import org.geomajas.annotation.Api;
import org.geomajas.sld.NamedStyleInfo;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.UserStyleInfo;

/**
 * Default implementation of {@link LegendGraphicMetadata} metadata object.
 * 
 * @author Jan De Moerloose
 * @since 1.10.0
 */
@Api(allMethods = true)
public class DefaultLegendGraphicMetadata implements LegendGraphicMetadata {

	private String layerId;

	private UserStyleInfo userStyle;

	private NamedStyleInfo namedStyle;

	private RuleInfo rule;

	private double scale;

	private int width;

	private int height;

	/** {@inheritDoc} */
	public String getLayerId() {
		return layerId;
	}

	/**
	 * Set the layer for the legend graphic.
	 *
	 * @param layerId layer id
	 */
	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	/** {@inheritDoc} */
	public UserStyleInfo getUserStyle() {
		return userStyle;
	}

	/**
	 * Set the user style for the legend graphic.
	 *
	 * @param userStyle user style or null
	 */
	public void setUserStyle(UserStyleInfo userStyle) {
		this.userStyle = userStyle;
	}

	/** {@inheritDoc} */
	public NamedStyleInfo getNamedStyle() {
		return namedStyle;
	}

	/**
	 * Set the named style for the legend graphic.
	 *
	 * @param namedStyle named style or null
	 */
	public void setNamedStyle(NamedStyleInfo namedStyle) {
		this.namedStyle = namedStyle;
	}

	/** {@inheritDoc} */
	public RuleInfo getRule() {
		return rule;
	}

	/**
	 * Set the SLD rule for the legend graphic.
	 *
	 * @param rule SLD rule
	 */
	public void setRuleInfo(RuleInfo rule) {
		this.rule = rule;
	}

	/** {@inheritDoc} */
	public double getScale() {
		return scale;
	}

	/**
	 * Set the scale for the legend graphic (can be used to determine the rule).
	 *
	 * @param scale scale
	 */
	public void setScale(double scale) {
		this.scale = scale;
	}

	/** {@inheritDoc} */
	public int getWidth() {
		return width;
	}

	/**
	 * Set the legend graphic width.
	 *
	 * @param width width in pixels
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/** {@inheritDoc} */
	public int getHeight() {
		return height;
	}

	/**
	 * Set the legend graphic height.
	 *
	 * @param height height in pixels
	 */
	public void setHeight(int height) {
		this.height = height;
	}

}
