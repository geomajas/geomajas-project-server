dojo.provide("geomajas.controller.MeasureDistanceController");
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
dojo.require("geomajas.event.MouseListener");
dojo.require("geomajas.spatial.geometry.LineString");
dojo.require("geomajas.gfx.paintables.Line");
dojo.require("geomajas.map.common");

dojo.requireLocalization("geomajas.controller", "measureDistanceController");
dojo.declare("MeasureDistanceController", MouseListener, {

	// i18n
	totalDistance: "",
	radius: "",

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
	constructor : function (mapWidget, message, useSnapHelp) {
		// i18n
		var widgetLocale = dojo.i18n.getLocalization("geomajas.controller", "measureDistanceController");
		this.totalDistance = widgetLocale.totalDistance;
		this.radius = widgetLocale.radius;
		
		/** The mapwidget we're measuring on. */
		this.mapWidget = mapWidget;

		/** While dragging the mouse, a Line object is drawn. */
		this.measuringRod = null;

		this.dragLine = null;
		this.textBalloon = null;
		this.circle = null;

		/** The result message, in your preferred language. */
		this.message = message;

		this.snapper = null;
		this.factory = new GeometryFactory(
			mapWidget.getMapModel().getSRID(),
			mapWidget.getMapModel().getPrecision());
		this.trans = new WorldViewTransformation(this.mapWidget.getMapView());

		if (useSnapHelp == "true") {
			this.snapController = new SnappingHelpController(mapWidget);
		} else {
			this.snapController = null;
		}
		
		this.lineStyle1 = new ShapeStyle("#FFFFFF", "0", "#FF9900", "1", "2", null, null);
		this.lineStyle2 = new ShapeStyle("#FFFFFF", "0", "#FF5500", "1", "2", null, null);
		this.circleStyle = new ShapeStyle("#FFEE00", "0.4", "#FFEE00", "1", "1", null, null);
	},

	/**
	 * Get the name of this controller.
	 * @returns A unique name.
	 */
	getName : function () {
		return "MeasureDistanceController";
	},

	mouseReleased : function (event) {
		if (this.measuringRod == null) { // Just beginning
			var viewPos = this._getViewPosition(event);

			this.measuringRod = new Line ("measureDistance.Line");
			this.measuringRod.setStyle (this.lineStyle1);

			var lineString = this.factory.createLineString([viewPos, viewPos]);
			this.measuringRod.setGeometry (lineString);

			dojo.publish (this.mapWidget.getRenderTopic(), [this.measuringRod, "all"]);
		} else { // We're already busy measuring
			var viewPos = this._getViewPosition(event);
			this.measuringRod.getGeometry().appendCoordinate(viewPos);
			dojo.publish (this.mapWidget.getRenderTopic(), [this.measuringRod, "all"]);
		}
	},

	doubleClick : function (event) {
		this.removeGraphicalContent();
	},

	/**
	 * When th user is dragging, this will update the Line.
	 * @param event A HtmlMouseEvent object.
	 */
	mouseMoved : function (/*HtmlMouseEvent*/event) {
		if (this.snapController) {
			this.snapController.mouseMoved(event);
		}

		if (this.measuringRod != null) {
			var viewPos = this._getViewPosition(event); // Click position in viewspace (snapped or not)
			var coords = this.measuringRod.getGeometry().getCoordinates();
			var lastCoordinate = coords[coords.length-1]; // Last coordinate of the measuring rod.
			var lineString = this.factory.createLineString([lastCoordinate, viewPos]); // dragLine linestring
			var r = lineString.getLength(); // circle r
			var len = r + this.measuringRod.getGeometry().getLength(); // total length

			// DRAGLINE:
			if (this.dragLine == null) { // Draw dragLine
				this.dragLine = new Line("measureDistance.DragLine");
				this.dragLine.setStyle (this.lineStyle2);
				this.dragLine.setGeometry (lineString);
				dojo.publish (this.mapWidget.getRenderTopic(), [this.dragLine, "all"]);
			} else { // Update dragline
				this.dragLine.setGeometry (lineString);
				dojo.publish (this.mapWidget.getRenderTopic(), [this.dragLine, "all"]);
			}

			// TEXTBALLOON:
			if (this.textBalloon == null && dijit.byId("measureDistance.Balloon") == null) { // Draw textballoon
				var dist = this.mapWidget.scaleUtil.readableScaleFormat(this.mapWidget.getMapView().getCurrentScale(), len);
				this.textBalloon = new geomajas.widget.TextBalloon({id:"measureDistance.Balloon"}, document.createElement("div"));
				this.textBalloon.setPosition(new Coordinate (event.getPosition().getX()+25, event.getPosition().getY()+25));
				this.textBalloon.setText(this.totalDistance + ": " + dist);
				this.textBalloon.render(this.mapWidget.domNode);
			} else if (this.textBalloon != null){ // Update textballoon
				var dist = this.mapWidget.scaleUtil.readableScaleFormat(this.mapWidget.getMapView().getCurrentScale(), len);
				var rad = this.mapWidget.scaleUtil.readableScaleFormat(this.mapWidget.getMapView().getCurrentScale(), r);
				var c = new Coordinate (event.getPosition().getX()+25, event.getPosition().getY()+25);
				this.textBalloon.setPosition(c);
				var text = this.totalDistance + ": "+dist;
				if (event.isCtrlDown()) {
					text += "<br/>" + this.radius + ": "+rad;
				}
				this.textBalloon.setText(text);
			}

			if (event.isCtrlDown()) {
				if (this.circle == null) {
					this.circle = new Circle ("measureDistance.Circle");
					this.circle.setStyle(this.circleStyle);
					this.circle.setPosition(lastCoordinate);
					this.circle.setR(r);
					dojo.publish (this.mapWidget.getRenderTopic(), [this.circle, "all"]);
				} else {
					this.circle.setPosition(lastCoordinate);
					this.circle.setR(r);
					dojo.publish (this.mapWidget.getRenderTopic(), [this.circle, "all"]);
				}
			} else if (this.circle != null) {
				dojo.publish (this.mapWidget.getRenderTopic(), [this.circle, "delete"]);
				this.circle = null;
			}
		}
	},

	/**
	 * @private
	 */
	removeGraphicalContent : function () {
		this.tempLen = 0;
		if (this.measuringRod != null) {
			dojo.publish (this.mapWidget.getRenderTopic(), [this.measuringRod, "delete"]);
			this.measuringRod = null;
		}
		if (this.dragLine != null) {
			dojo.publish (this.mapWidget.getRenderTopic(), [this.dragLine, "delete"]);
			this.dragLine = null;
		}
		if (this.circle != null) {
			dojo.publish (this.mapWidget.getRenderTopic(), [this.circle, "delete"]);
			this.circle = null;
		}
		if (this.textBalloon != null) {
			this.textBalloon.destroy();
			this.textBalloon = null;
		}
	},

	/**
	 * Return the position of the event in view-coordinates. Use a snapper if
	 * there is one active!
	 * @private
	 */
	_getViewPosition : function (event) {
		if (this.snapper == null) {
			var layer = this.mapWidget.getMapModel().getSelectedLayer();
			if (layer != null && layer instanceof VectorLayer) {
				this.snapper = layer.getSnapper();
			}
		}
		if (this.snapper) {
			var worldPos = this.trans.viewPointToWorld(event.getPosition());
			worldPos = this.snapper.snap(worldPos);
			return this.trans.worldPointToView(worldPos);
		}
		return event.getPosition();
	}
});
