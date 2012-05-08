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

import com.smartgwt.client.widgets.events.ClickEvent;

// @extract-start ToolbarModalAction, ToolbarModalAction
/**
 * Abstract class which serves as a template for selectable buttons in a tool bar. These selectable buttons can be
 * selected and deselected. With each of these actions a different method is executed. Usually this type of tool bar
 * button is used to set a new controller onto the {@link org.geomajas.gwt.client.widget.MapWidget}. If you are looking
 * for an action that should be executed immediately when clicking on it, have a look at the
 * {@link org.geomajas.gwt.client.action.ToolbarAction} class.
 *
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
public abstract class ToolbarModalAction extends ToolbarBaseAction {

	/**
	 * Constructor.
	 *
	 * @param icon icon
	 * @param tooltip tooltip
	 */
	public ToolbarModalAction(String icon, String tooltip) {
		super(icon, tooltip, tooltip);
	}

	/**
	 * Create a new ToolbarModalAction.
	 *
	 * @param icon icon
	 * @param title title
	 * @param tooltip tooltip
	 * @since 1.10.0
	 */
	public ToolbarModalAction(String icon, String title, String tooltip) {
		super(icon, title, tooltip);
	}

	// Class specific actions:

	/**
	 * When the tool bar button is selected, this method will be called.
	 *
	 * @param event event
	 */
	public abstract void onSelect(ClickEvent event);

	/**
	 * When the tool bar button is deselected, this method will be called.
	 *
	 * @param event event
	 */
	public abstract void onDeselect(ClickEvent event);
}
// @extract-end
