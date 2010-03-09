dojo.provide("geomajas.controller.PanController");
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
dojo.require("geomajas.event.MouseListener");
dojo.require("geomajas.controller.ZoomToRectangleController");

dojo.declare("PanController", MouseListener, {

	/**
	 * @fileoverview MouseListener for panning.
	 * @class MouseListener implementation for panning by dragging.
	 * @author Pieter De Graef
	 * 
	 * @constructor
	 * @extends MouseListener
	 * @param mapWidget
	 *            Reference to the MapWidget.
	 */
	constructor : function(mapWidget, hideLabelsOnDrag) {
		/** Reference to the MapWidget. */
		this.mapWidget = mapWidget;

		/** @private Is the user busy dragging? */
		this.dragging = false;

		/** @private Keep track of the original mouse-down position. */
		this.begin = null;

		this.hideLabelsOnDrag = hideLabelsOnDrag;

		this.zoomToRect = new ZoomToRectangleController(this.mapWidget);
	},

	/**
	 * Return a unique name.
	 */
	getName : function() {
		return "PanController";
	},

	/**
	 * Called onMouseDown. Saves the position, and sets dragging to true.
	 * 
	 * @param event
	 *            A HtmlMouseEvent object.
	 */
	mousePressed : function(/* HtmlMouseEvent */event) {
		log.info("Pancontroller mousePressed");
		if (event.isCtrlDown() || event.isShiftDown()) {
			this.zoomToRect.mousePressed(event);
		} else if (event.getButton() != event.statics.RIGHT_MOUSE_BUTTON) {
			this.dragging = true;
			this.begin = event.getPosition();
			this.mapWidget.setCursor("move");

			if (this.hideLabelsOnDrag == "true") {
				this.mapWidget.getGraphics().hide("featureLabels");
			}
		}
	},

	/**
	 * Called onMouseUp. If the user was dragging, update the view!
	 * 
	 * @param event
	 *            A HtmlMouseEvent object.
	 */
	mouseReleased : function(/* HtmlMouseEvent */event) {
		try {
			if (this.zoomToRect.dragging) {
				this.zoomToRect.mouseReleased(event);
			} else if (this.dragging) {
				this.dragging = false;
				this.mapWidget.setCursor("default");
			}
		} catch (e) {
			log.error("PanController - mouseReleased : exception !!! ");
			for ( var i in e)
				log.error(e[i]);
		}
	},

	/**
	 * If the user is dragging, update the view.
	 * 
	 * @param event
	 *            A HtmlMouseEvent object.
	 */
	mouseMoved : function(/* HtmlMouseEvent */event) {
		try {
			if (this.zoomToRect.dragging) {
				this.zoomToRect.mouseMoved(event);
			} else if (this.dragging) {
				this._updateView(event);
			}
		} catch (e) {
			log.error("PanController - mouseMoved : exception !!! ");
			for ( var i in e)
				log.error(e[i]);
		}
	},

	mouseExited : function(/* HtmlMouseEvent */event) {
		if (this.dragging) {
			var coords = dojo.coords(dojo.byId(this.mapWidget.id));
			var mouseX = event.originalMouseEvent.clientX - coords.x;
			var mouseY = event.originalMouseEvent.clientY - coords.y;

			if (mouseX <= 0 || mouseY <= 0 || mouseX >= coords.w
					|| mouseY >= coords.h) {
				log.debug("stopping pan");
				// event.setPosition(this.begin); // coordinates of outside
				// element are prolly not the same as in view
				this.dragging = false;
				this.mouseMoved(event);
			}
		}
	},

	/**
	 * @private
	 */
	_updateView : function(event, status) {
		var view = this.mapWidget.getMapView();
		var end = event.getPosition();
		var trans = new WorldViewTransformation(view);

		var c1 = trans.viewPointToWorld(this.begin);
		var c2 = trans.viewPointToWorld(end);
		this.begin = end;
		log.debug("PanController : _updateView " + c1 + " to " + c2);
		log.debug("PanController : _updateView dx " + c2.getX() - c1.getX()
				+ " dy " + c2.getY() - c1.getY());
		view.translate(c1.getX() - c2.getX(), c1.getY() - c2.getY(), status);
	}
});
