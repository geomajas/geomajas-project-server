/*
 * This file is part of Geomajas, a component framework for building rich
 * Internet applications (RIA) with sophisticated capabilities for the display,
 * analysis and management of geographic information. It is a building block
 * that allows developers to add maps and other geographic data capabilities to
 * their web applications.
 * 
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
dojo.provide("geomajas.controller.PanToolController");

dojo.require("geomajas.event.MouseListener");
dojo.require("geomajas.controller.PanController");
/**
 * PanToolController is a Controller that lets the ZoomSlider widget take control over a PanController.
 * @author An Buyle, Balder Van Camp 
 */
dojo.declare("geomajas.controller.PanToolController", MouseListener, {
	/**
	 * @param mapWidget
	 * @param boolean hideLabelsOnDrag
	 * @param toolbarId widget of the toolbar optional.
	 */
	constructor : function(mapWidget, hideLabelsOnDrag, toolbarId) {
		/** Reference to the MapWidget. */
		this.mapWidget = mapWidget;
		this.selected = false;

		if (toolbarId && toolbarId != null) { 
			this.toolbarid = toolbarid;
			this.controller = new PanController (mapWidget, hideLabelsOnDrag, toolbarId);
		} else {
			this.controller = new PanController (mapWidget, hideLabelsOnDrag);
		}
		/** MouseListener that defines the actions for mouse-events on the map */
		this.handle = dojo.connect(this.controller, "onDeactivate", this, "_onMyPanCtrlDeactivate");
			/* Note that when another controller is activated, this.controller.onDeactivate will be called. 
			 * By connecting to this call, we can deactivate the tool defined here 
			 */
	},

	/**
	 * Return a unique name.
	 */
	getName : function() {
		return "PanToolController";
	},

	/**
	 * Initialize this controller on activation.
	 */
	onActivate : function () {
		log.info(this.getName() + ".onActivate()");
	},
	
	/**
	 * cleanup this controller on deactivation.
	 */
	onDeactivate : function () {
		log.info(this.getName() + ".onDeactivate()");
	},
	
	_onMyPanCtrlDeactivate: function () {
		log.debug(this.getName() + "._onMyPanCtrlDeactivate()");
		this.selected = false;
		this.prevToolId = null;
		this.mapWidget.setCursor("default");
	},
	/**
	 * Called onMouseDown. Saves the position, and sets dragging to true.
	 * 
	 * @param event
	 *            A HtmlMouseEvent object.
	 */
	mousePressed : function(/* HtmlMouseEvent */event) {
		log.debug(this.getName() + ".mousePressed");
	},

	/**
	 * Called onMouseUp. 
	 * Toggle the activeness of the pan controller .
	 * @param event
	 *            A HtmlMouseEvent object.
	 */
	mouseReleased : function(/* HtmlMouseEvent */event) {

	},
	
	mouseClicked : function (/*HtmlMouseEvent*/event) {
		log.debug(this.getName() + ".mouseClicked()");
		if (event.getButton() != event.statics.RIGHT_MOUSE_BUTTON) {
			this.toggleActiveness();
		}
	},
	
	toggleActiveness : function () {
	
		if (this.selected) {
			log.debug(this.getName() + ".toggleActiveness(): deactivate");
			this.selected = false;
			this.mapWidget.setCursor("default");
			this.mapWidget.setController(null);
		}
		else {
			log.debug(this.getName() + ".toggleActiveness(): activate");
			this.selected = true;
			if(this.toolbarid) {
				var mainToolbar = dijit.byId(this.toolbarid);
				if (mainToolbar) {
					mainToolbar.onSelect(null);
					/* deselect the currently active tool (and its controller) 
					on the toolbar, if any. */ 
				}
			}
			this.mapWidget.setController(this.controller);
			//dojo.hitch(this, 'deactivate' , this.mapWidget, 'onSetController');
			this.mapWidget.setCursor("move");

		}
	}
});