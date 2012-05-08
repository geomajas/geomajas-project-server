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
package org.geomajas.plugin.printing.component.dto;

import java.io.Serializable;

import org.geomajas.annotation.Api;

/**
 * DTO object for BaseLayerComponent.
 * 
 * @author Jan De Moerloose
 * @see org.geomajas.plugin.printing.component.BaseLayerComponent
 * @since 2.0.0
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

	/** Constructor. */
	public BaseLayerComponentInfo() {
		getLayoutConstraint().setAlignmentX(LayoutConstraintInfo.JUSTIFIED);
		getLayoutConstraint().setAlignmentY(LayoutConstraintInfo.JUSTIFIED);
	}

	/**
	 * Is this component visible?
	 *
	 * @return true when component is visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Set whether this component is visible.
	 *
	 * @param visible visible status
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * Is this component selected.
	 *
	 * @return true when this component is selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * Set selected status for this component.
	 *
	 * @param selected true when selected
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * Get (server) layer id.
	 *
	 * @return layer id
	 */
	public String getLayerId() {
		return layerId;
	}

	/**
	 * Set (server) layer id.
	 *
	 * @param layerId layer id
	 */
	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}
 
}
