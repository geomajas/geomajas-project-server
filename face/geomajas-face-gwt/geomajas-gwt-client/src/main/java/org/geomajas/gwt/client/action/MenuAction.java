/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client.action;

import org.geomajas.global.Api;

import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;

/**
 * General definition of a <code>MenuAction</code>. All Geomajas actions in toolbars or context menus should build upon
 * this class.
 *
 * @author Pieter De Graef
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
