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
package org.geomajas.servlet.mvc.legend;

import org.geomajas.service.legend.LegendGraphicMetadata;
import org.geomajas.sld.NamedStyleInfo;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.UserStyleInfo;

/**
 * Default JavaBeans implementation of {@link LegendGraphicMetadata} metadata object.
 * 
 * @author Jan De Moerloose
 * 
 */
public class LegendGraphicMetadataImpl implements LegendGraphicMetadata {

	private String layerId;

	private UserStyleInfo userStyle;

	private NamedStyleInfo namedStyle;

	private RuleInfo rule;

	private double scale;

	private int width;

	private int height;

	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	public UserStyleInfo getUserStyle() {
		return userStyle;
	}

	public void setUserStyle(UserStyleInfo userStyle) {
		this.userStyle = userStyle;
	}

	public NamedStyleInfo getNamedStyle() {
		return namedStyle;
	}

	public void setNamedStyle(NamedStyleInfo namedStyle) {
		this.namedStyle = namedStyle;
	}

	public RuleInfo getRule() {
		return rule;
	}

	public void setRuleInfo(RuleInfo rule) {
		this.rule = rule;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
