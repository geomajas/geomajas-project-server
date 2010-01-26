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

dojo.provide("geomajas.controller.SimpleMeasureDistanceController");
dojo.require("geomajas.event.MouseListener");
dojo.require("geomajas.spatial.geometry.LineString");
dojo.require("geomajas.gfx.paintables.Line");
dojo.require("geomajas.map.common");

dojo.declare("SimpleMeasureDistanceController", MouseListener, {

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
		/** The mapwidget we're measuring on. */
		this.mapWidget = mapWidget;
		
		/** While dragging the mouse, a Line object is drawn. */
		this.measuringRod = null;
		
		/** We have to know whether or not the user is measuring. This happens
		 *  throught dragging.
		 */
		this.dragging = false;
		
		/** The result message, in your preferred language. */
		this.message = message;

		this.snapper = null;
		this.factory = new GeometryFactory(
			mapWidget.getMapModel().getSRID(),
			mapWidget.getMapModel().getPrecision());
		this.editor = new GeometryEditor();

		if (useSnapHelp == "true") {
			this.snapController = new SnappingHelpController(mapWidget);
		} else {
			this.snapController = null;
		}
	},

	/**
	 * Get the name of this controller.
	 * @returns A unique name.
	 */
	getName : function () {
		return "SimpleMeasureDistanceController";
	},

	/**
	 * When the mouse is pressed (on the map) we have to keep track of the
	 * position. Also a Line paintable is initiated, and dragging is
	 * set to true.
	 * @param event A HtmlMouseEvent object.
	 */
	mousePressed : function (/*HtmlMouseEvent*/event) {
		if (event.getButton() != event.statics.RIGHT_MOUSE_BUTTON) {
			var layer = this.mapWidget.getMapModel().getSelectedLayer();
			if (layer != null && layer instanceof VectorLayer) {
				this.snapper = layer.getSnapper();
			}

			this.dragging = true;

			var trans = new WorldViewTransformation(this.mapWidget.getMapView());
			var newPos = trans.viewPointToWorld(event.getPosition());
			if (this.snapper) {
				newPos = this.snapper.snap(newPos);
			}
			var begin = trans.worldPointToView(newPos);

			this.measuringRod = new Line ("measureDistanceLine");
			this.measuringRod.setStyle ("stroke:#0000FF; stroke-opacity:0.7; stroke-width: 5;");

			var lineString = this.factory.createLineString([begin, begin]);
			this.measuringRod.setGeometry (lineString);

			dojo.publish (this.mapWidget.getRenderTopic(), [this.measuringRod, "all"]);
		}
	},

	/**
	 * If the mouse is released and we were dragging, then measuring should
	 * take place. It then calculates the dragged distance, and displays it.
	 * @param event A HtmlMouseEvent object.
	 */
	mouseReleased : function (/*HtmlMouseEvent*/event) {
		if (this.dragging) { // It is possible to MouseDown somewhere outside the SVG, and MouseUp here...
			this.dragging = false;
			this.snapper = null;
			var trans = new WorldViewTransformation(this.mapWidget.getMapView());
			var newPos = trans.viewPointToWorld(event.getPosition());
			if (this.snapper) {
				newPos = this.snapper.snap(newPos);
			}
			var operation = new SetCoordinateOperation(1, newPos);
			this.editor.edit(this.measuringRod.getGeometry(), operation)

			var v1 = new Vector2D();
			var v2 = new Vector2D();
			v1.fromCoordinate(trans.viewPointToWorld(coords[0]));
			v2.fromCoordinate(newPos);

			var distance = v1.distance (v2);
			alert(dojo.string.substituteParams(this.message, ""+distance));

			dojo.publish (this.mapWidget.getRenderTopic(), [this.measuringRod, "all"]);
		}
	},

	/**
	 * When the user is dragging, this will update the Line.
	 * @param event A HtmlMouseEvent object.
	 */
	mouseMoved : function (/*HtmlMouseEvent*/event) {
		if (this.snapController) {
			this.snapController.mouseMoved(event);
		}

		if (this.dragging) {
			var trans = new WorldViewTransformation(this.mapWidget.getMapView());
			var newPos = trans.viewPointToWorld(event.getPosition());
			if (this.snapper) {
				newPos = this.snapper.snap(newPos);
			}
			var operation = new SetCoordinateOperation(1, newPos);
			this.editor.edit(this.measuringRod.getGeometry(), operation)

			dojo.publish (this.mapWidget.getRenderTopic(), [this.measuringRod, "all"]);
		}
	},

	removeGraphicalContent : function () {
	}
});