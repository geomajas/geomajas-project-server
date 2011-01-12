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
package org.geomajas.plugin.printing.component.dto;

import java.io.Serializable;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.global.Api;
import org.geomajas.layer.LayerType;

/**
 * DTO object for LegendIconComponent.
 * 
 * @author Jan De Moerloose
 * @see org.geomajas.plugin.printing.component.LegendIconComponent
 * @since 2.0.0
 * 
 */
@Api(allMethods = true)
public class LegendIconComponentInfo extends PrintComponentInfo implements Serializable {

	private static final long serialVersionUID = 200L;

	private String label;

	private LayerType layerType;

	private FeatureStyleInfo styleInfo;

	
	public String getLabel() {
		return label;
	}

	
	public void setLabel(String label) {
		this.label = label;
	}

	
	public LayerType getLayerType() {
		return layerType;
	}

	
	public void setLayerType(LayerType layerType) {
		this.layerType = layerType;
	}

	
	public FeatureStyleInfo getStyleInfo() {
		return styleInfo;
	}

	
	public void setStyleInfo(FeatureStyleInfo styleInfo) {
		this.styleInfo = styleInfo;
	}

}
