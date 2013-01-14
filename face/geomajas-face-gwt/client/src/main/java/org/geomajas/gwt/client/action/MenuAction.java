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

import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;

// @extract-start MenuAction, MenuAction
/**
 * General definition of a <code>MenuAction</code>. All Geomajas actions in toolbars or context menus should build upon
 * this class.
 *
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
public abstract class MenuAction extends MenuItem implements ClickHandler {

	/**
	 * Constructor that expects you to immediately fill in the title and the icon.
	 *
	 * @param title
	 *            The textual title of the menu item.
	 * @param icon
	 *            A picture to be used as icon for the menu item.
	 */
	protected MenuAction(String title, String icon) {
		super(title, icon);
		addClickHandler(this);
	}
}
// @extract-end
