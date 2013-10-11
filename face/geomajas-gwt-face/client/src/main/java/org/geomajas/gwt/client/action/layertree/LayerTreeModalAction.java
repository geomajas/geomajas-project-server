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

package org.geomajas.gwt.client.action.layertree;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.gwt.client.map.layer.Layer;

// @extract-start LayerTreeModalAction, LayerTreeModalAction
/**
 * Base template for modal actions in the layer tree toolbar.
 *
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
public abstract class LayerTreeModalAction extends ToolbarBaseAction {

	private String selectedIcon;

	private String disabledIcon;

	private String selectedTooltip;

	/**
	 * Constructor.
	 *
	 * @param selectedIcon selected icon
	 * @param deselectedIcon deselected icon
	 * @param disabledIcon disabled icon
	 * @param selectedTooltip selected tooltip
	 * @param deselectedTooltip deselected tooltip
	 */
	public LayerTreeModalAction(String selectedIcon, String deselectedIcon, String disabledIcon,
			String selectedTooltip, String deselectedTooltip) {
		super(deselectedIcon, deselectedTooltip);
		this.selectedIcon = selectedIcon;
		this.disabledIcon = disabledIcon;
		this.selectedTooltip = selectedTooltip;
	}

	/**
	 * When the toolbar button is selected, this method will be called.
	 */
	public abstract boolean isEnabled(Layer<?> layer);

	/**
	 * When the toolbar button is deselected, this method will be called.
	 */
	public abstract boolean isSelected(Layer<?> layer);

	/** @todo javadoc unknown. */
	public abstract void onDeselect(Layer<?> layer);

	/** @todo javadoc unknown. */
	public abstract void onSelect(Layer<?> layer);

	/**
	 * Get selected icon.
	 *
	 * @return selected icon
	 */
	public String getSelectedIcon() {
		return selectedIcon;
	}

	/**
	 * Set selected icon.
	 *
	 * @param selectedIcon selected icon
	 */
	public void setSelectedIcon(String selectedIcon) {
		this.selectedIcon = selectedIcon;
	}

	/**
	 * Get deselected icon.
	 *
	 * @return deselected icon
	 */
	public String getDeselectedIcon() {
		return getIcon();
	}

	/**
	 * Set deselected icon.
	 *
	 * @param deselectedIcon deselected icon
	 */
	public void setDeselectedIcon(String deselectedIcon) {
		setIcon(deselectedIcon);
	}

	/**
	 * Get selected tooltip.
	 *
	 * @return selected tooltip
	 */
	public String getSelectedTooltip() {
		return selectedTooltip;
	}

	/**
	 * Set selected tooltip.
	 *
	 * @param selectedTooltip selected tooltip
	 */
	public void setSelectedTooltip(String selectedTooltip) {
		this.selectedTooltip = selectedTooltip;
	}

	/**
	 * Get deselected tooltip.
	 *
	 * @return deselected tooltip
	 */
	public String getDeselectedTooltip() {
		return getTooltip();
	}

	/**
	 * Set deselected tooltip.
	 *
	 * @param deselectedTooltip deselected tooltip
	 */
	public void setDeselectedTooltip(String deselectedTooltip) {
		setTooltip(deselectedTooltip);
	}

	/**
	 * Get disabled icon.
	 *
	 * @return disabled icon
	 */
	public String getDisabledIcon() {
		return disabledIcon;
	}

	/**
	 * Set disabled icon.
	 *
	 * @param disabledIcon disabled icon
	 */
	public void setDisabledIcon(String disabledIcon) {
		this.disabledIcon = disabledIcon;
	}
}
// @extract-end
