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

import org.geomajas.global.Api;

/**
 * DTO object for BaseLayerComponent.
 * 
 * @author Jan De Moerloose
 * @see org.geomajas.plugin.printing.component.BaseLayerComponent
 * @since 2.0.0
 *
 */
@Api(allMethods = true)
public class BaseLayerComponentInfo extends PrintComponentInfo implements Serializable {

	private static final long serialVersionUID = 200L;
	/**
	 * True if layer is visible.
	 */
	private boolean visible = true;

	/**
	 * True if layer is selected.
	 */
	private boolean selected;

	/**
	 * ID of this layer (client ID).
	 */
	private String layerId;

	public BaseLayerComponentInfo() {
		getLayoutConstraint().setAlignmentX(LayoutConstraintInfo.JUSTIFIED);
		getLayoutConstraint().setAlignmentY(LayoutConstraintInfo.JUSTIFIED);
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}
 

}
