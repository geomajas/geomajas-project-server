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

dojo.provide("geomajas.controller.OverviewRectController");
dojo.require("geomajas.event.MouseListener");

dojo.declare("OverviewRectController", MouseListener, {

	/**
	 * @fileoverview MouseListener for panning to the rectangle on an overview map.
	 * @class MouseListener implementation for panning by dragging the
	 * rectangle on an overview map.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends MouseListener
	 * @param mapWidget Reference to the MapWidget.
	 * @param targetMapId The target map that has to move with the recangle of
	 *                    this (overview) map (mapWidget).
	 *                    
	 */
	constructor : function (mapWidget, targetMapId) {
		/** Reference to the MapWidget. */
		this.mapWidget = mapWidget;
		
		/** Target MapWidget */
		this.targetMap = dijit.byId(targetMapId);
		
		/** @private */
		this.dragging = false;
		
		/** @private */
		this.begin = null;
	},

	/**
	 * Return a unique name.
	 */
	getName : function () {
		return "OverviewRectController";
	},

	/**
	 * Start dragging the rectangle.
	 */
	mousePressed : function (/*HtmlMouseEvent*/event) {
		if (event.getButton() != event.statics.RIGHT_MOUSE_BUTTON) {
			log.debug("OverviewRectController : mousePressed "+event.getPosition());
			this.dragging = true;
			this.begin = event.getPosition();
			this.previous = event.getPosition();
		}
	},

	/**
	 * Stop dragging the rectangle and update the view.
	 */
	mouseReleased : function (/*HtmlMouseEvent*/event) {
		if (this.dragging) { // It is possible to MouseDown somewhere outside the SVG, and MouseUp here...
			log.debug("OverviewRectController : mouseReleased "+event.getPosition());
			this.dragging = false;
			this._updateView (event);
		}
	},

	/**
	 * Move the rectangle while dragging, do not update the view yet, because
	 * that would be too slow.
	 */
	mouseMoved : function (/*HtmlMouseEvent*/event) {
		if (this.dragging) {
			log.debug("OverviewRectController : mouseMoved "+event.getPosition());
			var pos = this.mapWidget.getTargetRect().getPosition();
			log.debug("OverviewRectController : mouseMoved old position "+pos);
			var end = event.getPosition();
			var deltaX = end.getX() - this.previous.getX();
			var deltaY = end.getY() - this.previous.getY();
			this.previous = end;

			this.mapWidget.getTargetRect().setPosition(new Coordinate(pos.getX() + deltaX, pos.getY() + deltaY));
			log.debug("OverviewRectController : mouseMoved new position "+this.mapWidget.getTargetRect().getPosition());
			dojo.publish(this.mapWidget.getRenderTopic(), [this.mapWidget.getTargetRect(), "all"]);
		}
	},

	/**
	 * @private
	 */
	_updateView : function (event) {
		var end = event.getPosition();
		var trans = new WorldViewTransformation(this.mapWidget.getMapView());

		var c1 = trans.viewPointToWorld(this.begin);
		var c2 = trans.viewPointToWorld(end);
		this.begin = end;

		var view = this.targetMap.getMapView();
		log.debug("OverviewRectController : _updateView "+c1+" to "+c2);
		log.debug("OverviewRectController : _updateView dx "+c2.getX() - c1.getX()+" dy "+c2.getY() - c1.getY());
		view.translate (c2.getX() - c1.getX(), c2.getY() - c1.getY());
	}
});