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

dojo.provide("geomajas.controller.menu.MenuItemController");
dojo.require("geomajas.event.MouseListener");

dojo.declare("MenuItemController", MouseListener, {

	/**
	 * @fileoverview Mouselistener for rightmouse menu items.
	 * @class MouseListener implementation for an item in a right-mouseclick
	 * menu. Has a mouseEntered and mouseExited implementation that changes
	 * it's looks, plus a mouseCLicked that executes the Action behind the
	 * menu-item.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends MouseListener
	 * @param topic Name of the rendering topic of the mapWidget this
	 *              controller is defined for.
	 * @param svgMenu Reference to the SvgMenu object.
	 * @param svgMenuItem Reference to the SvgMenuItem this controller affects.
	 */
	constructor : function (topic, svgMenu, svgMenuItem) {
		this.topic = topic;
		this.svgMenu = svgMenu;
		this.svgMenuItem = svgMenuItem;
	},

	/**
	 * Controller's name.
	 */
	getName : function () {
		return "MenuItemController";
	},

	/**
	 * Change the style to the svgMenuItem's hoovering style, and redraw.
	 * Doesn't do much is the SvgMenuItem is disabled.
	 * @param event A HtmlMouseEvent.
	 */
	mouseEntered : function (/*HtmlMouseEvent*/event) {
		this.svgMenuItem.setHoovering (true);
		dojo.publish (this.topic, [this.svgMenuItem, "all"]);
	},

	/**
	 * Change the style to the svgMenuItem's normal style, and redraw.
	 * Doesn't do much is the SvgMenuItem is disabled.
	 * @param event A HtmlMouseEvent.
	 */
	mouseExited : function (/*HtmlMouseEvent*/event) {
		this.svgMenuItem.setHoovering (false);
		dojo.publish (this.topic, [this.svgMenuItem, "all"]);
	},

	/**
	 * If this svgMenuItem is enabled, then execute it's action. Also delete's
	 * the menu from the map.
	 * @param event A HtmlMouseEvent.
	 */
	mouseReleased : function (/*HtmlMouseEvent*/event) {
		log.info ("MenuItemController:mouseReleased");
		if (this.svgMenuItem.getAction() != null) {
			var action = this.svgMenuItem.getAction();
			if (action != null && action.isEnabled()) {
				action.actionPerformed(this.svgMenuItem.getEvent());
			}
		}
		dojo.publish (this.topic, [this.svgMenu, "delete"]);
	}
});