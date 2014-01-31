/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.printing.component.dto;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.annotation.Api;
import org.geomajas.layer.LayerType;

/**
 * DTO object for {@link org.geomajas.plugin.printing.component.impl.LegendIconComponentImpl}.
 * 
 * @author Jan De Moerloose
 * @since 2.0.0
 * 
 */
@Api(allMethods = true)
public class LegendIconComponentInfo extends PrintComponentInfo {

	private static final long serialVersionUID = 200L;

	private String label;

	private LayerType layerType;

	private FeatureStyleInfo styleInfo;

	/**
	 * Get label string.
	 *
	 * @return label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Set label string.
	 *
	 * @param label label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Get layer type.
	 *
	 * @return layer type
	 */
	public LayerType getLayerType() {
		return layerType;
	}

	/**
	 * Set layer type.
	 *
	 * @param layerType layer type
	 */
	public void setLayerType(LayerType layerType) {
		this.layerType = layerType;
	}

	/**
	 * Get feature style info.
	 *
	 * @return feature style info
	 */
	public FeatureStyleInfo getStyleInfo() {
		return styleInfo;
	}

	/**
	 * Set feature style info.
	 *
	 * @param styleInfo style info
	 */
	public void setStyleInfo(FeatureStyleInfo styleInfo) {
		this.styleInfo = styleInfo;
	}

}
