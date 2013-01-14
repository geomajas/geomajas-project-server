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

package org.geomajas.gwt.client.action;

import org.geomajas.annotation.Api;

import com.smartgwt.client.widgets.events.ClickHandler;

// @extract-start ToolbarAction, ToolbarAction
/**
 * Abstract class that serves as a template for building tool bar actions. A tool bar action is an action that is
 * executed immediately when the tool bar button is clicked. If you want a selectable tool bar button, have a look at
 * the {@link ToolbarModalAction} class.
 *
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
public abstract class ToolbarAction extends ToolbarBaseAction implements ClickHandler {

	/**
	 * Constructor.
	 *
	 * @param icon icon
	 * @param tooltip tooltip
	 */
	public ToolbarAction(String icon, String tooltip) {
		this(icon, tooltip, tooltip);
	}

	/**
	 * Create a new ToolbarAction.
	 *
	 * @param icon icon
	 * @param title title
	 * @param tooltip tooltip
	 * @since 1.10.0
	 */
	public ToolbarAction(String icon, String title, String tooltip) {
		super(icon, title, tooltip);
	}
}
// @extract-end
