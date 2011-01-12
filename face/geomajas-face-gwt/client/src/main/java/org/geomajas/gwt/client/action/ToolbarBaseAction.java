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

package org.geomajas.gwt.client.action;

import org.geomajas.global.Api;
import org.geomajas.gwt.client.action.event.ToolbarActionDisabledEvent;
import org.geomajas.gwt.client.action.event.ToolbarActionEnabledEvent;
import org.geomajas.gwt.client.action.event.ToolbarActionHandler;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Base tool bar action, defines the common bits between {@link ToolbarAction} and {@link ToolbarModalAction}.
 * 
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
// @extract-start ToolbarBaseAction, ToolbarBaseAction
public abstract class ToolbarBaseAction {

	private String icon; // Link to the image icon that should represent the action's button in the tool bar.

	private String tooltip; // Text that appears when hovering over the tool bar button.

	/** Is the button for this action disabled or not? */
	private boolean disabled;

	private HandlerManager handlerManager;

	public ToolbarBaseAction(String icon, String tooltip) {
		this.icon = icon;
		this.tooltip = tooltip;
		handlerManager = new HandlerManager(this);
	}

	public HandlerRegistration addToolbarActionHandler(ToolbarActionHandler handler) {
		return handlerManager.addHandler(ToolbarActionHandler.TYPE, handler);
	}


	/**
	 * Link to the image icon that should represent the action's button in the tool bar.
	 *
	 * @return icon link
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * Link to the image icon that should represent the action's button in the tool bar.
	 * 
	 * @param icon
	 *            The new icon value
	 * */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * Text that appears when hovering over the tool bar button.
	 *
	 * @return tool tip
	 */
	public String getTooltip() {
		return tooltip;
	}

	/**
	 * Text that appears when hovering over the tool bar button.
	 * 
	 * @param tooltip
	 *            The new tooltip value
	 */
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	/**
	 * Is the button for this action disabled or not?
	 *
	 * @return true when disabled 
	 */
	public boolean isDisabled() {
		return disabled;
	}

	/**
	 * Is the button for this action disabled or not?
	 * 
	 * @param disabled
	 *            The new value
	 */
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
		if (disabled) {
			handlerManager.fireEvent(new ToolbarActionDisabledEvent());
		} else {
			handlerManager.fireEvent(new ToolbarActionEnabledEvent());
		}
	}
}
// @extract-end
