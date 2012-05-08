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

package org.geomajas.gwt.client.action;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.action.event.ToolbarActionDisabledEvent;
import org.geomajas.gwt.client.action.event.ToolbarActionEnabledEvent;
import org.geomajas.gwt.client.action.event.ToolbarActionHandler;
import org.geomajas.gwt.client.action.toolbar.parameter.ButtonLayoutParameter.Layout;

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

	private String title; // Text that appears in the button under (or to the right of) the icon.

	private String buttonLayout; // Determines to gui of the RibbonButton.

	/** Is the button for this action disabled or not? */
	private boolean disabled;

	private HandlerManager handlerManager;

	private Layout layout;

	/**
	 * Constructor.
	 *
	 * @param icon icon
	 * @param tooltip tooltip
	 */
	public ToolbarBaseAction(String icon, String tooltip) {
		this(icon, tooltip, tooltip);
	}

	/**
	 * Constructor for ToolbarBaseAction.
	 * 
	 * @param icon icon
	 * @param title title
	 * @param tooltip tool tip
	 * @since 1.10.0
	 */
	public ToolbarBaseAction(String icon, String title, String tooltip) {
		this.icon = icon;
		this.title = title;
		this.tooltip = tooltip;
		handlerManager = new HandlerManager(this);
	}

	/**
	 * Add toolbar action handler.
	 *
	 * @param handler action handler
	 * @return handler registration
	 */
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

	/**
	 * Text that appears in the button under (or to the right of) the icon.
	 * 
	 * @return title
	 * @since 1.10.0
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Text that appears in the button under (or to the right of) the icon.
	 * 
	 * @param title
	 *            The new value
	 * @since 1.10.0
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @deprecated Replaced by {@link #getLayout()}
	 * Get the button layout which determines the gui in a RibbonColumn.
	 * 
	 * @return The button layout which determines the gui.
	 * @since 1.10.0
	 */
	@Deprecated
	public String getButtonLayout() {
		return buttonLayout;
	}
	
	/**
	 * <p>Set the button layout which determines the gui in a RibbonColumn.<p>
	 * 
	 * @param buttonLayout
	 *           The button layout which determines the gui.
	 * @since 1.10.0
	 * @deprecated Replaced by {@link #setLayout(Layout)}
	 */
	@Deprecated
	public void setButtonLayout(String buttonLayout) {
		this.buttonLayout = buttonLayout;
	}
	
	/**
	 * Get the button layout which determines the gui in a RibbonColumn.
	 * 
	 * @return The {@link Layout} which determines the gui.
	 * @since 1.11.0
	 */
	public Layout getLayout() {
		return layout;
	}

	/**
	 * Get the button layout which determines the gui in a RibbonColumn. 
	 * 
	 * @param layout The {@link Layout} which determines the gui.
	 * @since 1.11.0
	 */
	public void setLayout(Layout layout) {
		this.layout = layout;
	}
}
// @extract-end
