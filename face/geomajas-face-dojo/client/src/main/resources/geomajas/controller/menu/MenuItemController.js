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