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

/**
 * Base definition of an action for the LayerTree toolbar.
 *
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
// @extract-start LayerTreeAction, LayerTreeAction
public abstract class LayerTreeAction extends ToolbarBaseAction {

	private String disabledIcon;

	/**
	 * Constructor setting all values.
	 *
	 * @param icon The default icon for the button.
	 * @param tooltip The default tooltip for the button.
	 * @param disabledIcon The icon used when the button is disabled.
	 */
	public LayerTreeAction(String icon, String tooltip, String disabledIcon) {
		super(icon, tooltip);
		this.disabledIcon = disabledIcon;
	}

	/**
	 * This method will be called when the user clicks on the button.
	 *
	 * @param layer The currently selected layer.
	 */
	public abstract void onClick(Layer<?> layer);

	/**
	 * Is the this action enabled for the layer?
	 *
	 * @param layer layer to test
	 * @return enabled status of action for layer
	 */
	public abstract boolean isEnabled(Layer<?> layer);

	/**
	 * Set icon to display when button is disabled.
	 *
	 * @return icon shown when the button is disabled
	 */
	public String getDisabledIcon() {
		return disabledIcon;
	}

	/**
	 * Set icon for disabled state.
	 *
	 * @param disabledIcon icon for disabled state
	 */
	public void setDisabledIcon(String disabledIcon) {
		this.disabledIcon = disabledIcon;
	}
}
// @extract-end
