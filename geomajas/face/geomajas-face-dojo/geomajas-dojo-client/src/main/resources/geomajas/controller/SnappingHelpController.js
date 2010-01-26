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

dojo.provide("geomajas.controller.SnappingHelpController");
dojo.require("geomajas.event.MouseListener");
dojo.require("geomajas.gfx.paintables.Circle");

dojo.declare("SnappingHelpController", MouseListener, {

	/**
	 * @fileoverview Mouselistener for measuring distances.
	 * @class A MouseListener implementation that measures distances, by
	 * dragging the mouse.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends MouseListener
	 * @param mapWidget A reference to the MapWidget on which we have to
	 *                  measure.
	 * @param message The message to display when the mouse is released.
	 *                (for internationalization support)
	 */
	constructor : function (mapWidget) {
		/** The mapwidget we're measuring on. */
		this.mapWidget = mapWidget;
		
		this.circle = null;
		this.snapper = null;
		this.factory = new GeometryFactory(
			mapWidget.getMapModel().getSRID(),
			mapWidget.getMapModel().getPrecision());
		this.trans = new WorldViewTransformation(this.mapWidget.getMapView());
	},

	/**
	 * Get the name of this controller.
	 * @returns A unique name.
	 */
	getName : function () {
		return "SnappingHelpController";
	},

	mouseMoved : function (/*HtmlMouseEvent*/event) {
		this._retrieveSnapper();
//		if (event.isCtrlDown()) {
		if (this.snapper != null && this.snapper.isActive()) {
			if (this.circle == null) {
				this.circle = new Circle ("snappingHelp.Circle");
				this.circle.setStyle(new ShapeStyle("#FF88EE", "1", "#FF00EE", "1", "1", null, null));
				this.circle.setPosition(this._getViewPosition(event));
				this.circle.setR(4);
				dojo.publish (this.mapWidget.getRenderTopic(), [this.circle,"all"]);
			} else {
				this.circle.setPosition(this._getViewPosition(event));
				dojo.publish (this.mapWidget.getRenderTopic(), [this.circle, "all"]);
			}
		} else if (this.circle != null) {
			dojo.publish (this.mapWidget.getRenderTopic(), [this.circle, "delete"]);
			this.circle = null;
		}
	},

	removeGraphicalContent : function () {
		if (this.circle != null) {
			dojo.publish (this.mapWidget.getRenderTopic(), [this.circle, "delete"]);
			this.circle = null;
		}
	},

	/**
	 * @private
	 */
	_retrieveSnapper : function () {
		var layer = this.mapWidget.getMapModel().getSelectedLayer();
		if (layer != null && layer instanceof VectorLayer) {
			this.snapper = layer.getSnapper();
		}
	},

	/**
	 * @private
	 */
	_getViewPosition : function (event) {
		this._retrieveSnapper();
		if (this.snapper) {
			var worldPos = this.trans.viewPointToWorld(event.getPosition());
			worldPos = this.snapper.snap(worldPos);
			return this.trans.worldPointToView(worldPos);
		}
		return event.getPosition();
	}
});