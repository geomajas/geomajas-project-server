dojo.provide("geomajas.controller.ZoomToRectangleController");
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
dojo.require("geomajas.event.MouseListener");
dojo.require("geomajas.spatial.geometry.Polygon");
dojo.require("geomajas.gfx.paintables.Rectangle");

dojo.declare("ZoomToRectangleController", MouseListener, {

	/**
	 * @fileoverview MouseListener for zooming to a drawn rectangle.
	 * @class MouseListener implementation for zooming to a drawn rectangle.
	 * This rectangle is created by dragging the mouse on the MapWidget.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends MouseListener
	 * @param mapWidget Reference to the MapWidget on which we want to zoom.
	 */
	constructor : function (mapWidget) {
		/** Reference to the MapWidget on which we want to zoom. */
		this.mapWidget = mapWidget;

		/** Rectangle paintable object that draws a rectangle. */
		this.rectangle = null;

		/** Holds the dragging status. */
		this.dragging = false;

		/** @private */
		this.begin = null;

		/** @private */
		this.style = new ShapeStyle("#CCCC00", "0.5", "#FF6600", "0.7", "2", null, null);
	},

	/**
	 * Returns a unique name.
	 */
	getName : function () {
		return "ZoomToRectangleController";
	},

	/**
	 * OnMouseDown, set dragging to true, and initialize a rectangle for
	 * drawing.
	 * @param event A HtmlMouseEvent object.
	 */
	mousePressed : function (/*HtmlMouseEvent*/event) {
		if (event.getButton() != event.statics.RIGHT_MOUSE_BUTTON) {
			this.mapWidget.setCursor("crosshair");
			this.dragging = true;
			this.begin = event.getPosition();

			this.rectangle = new Rectangle ("zoomRectangle");
			this.rectangle.setStyle (this.style);
			this.rectangle.setPosition (this.begin);

			dojo.publish (this.mapWidget.getRenderTopic(), [this.rectangle, "all"]);
		}
	},

	/**
	 * If we're dragging, apply the drawn rectangle as a bounding box on
	 * the MapWidget's MapView object. Also get rid of the rectangle.
	 * @param event A HtmlMouseEvent object.
	 */
	mouseReleased : function (/*HtmlMouseEvent*/event) {
		if (this.dragging) { // It is possible to MouseDown somewhere outside the SVG, and MouseUp here...
			this.mapWidget.setCursor("default");
			this.dragging = false;
			this._updateRectangle(event);

			if (!this.begin.equals(event.getPosition())) {
				var viewBounds = new Bbox(this.rectangle.getPosition().getX(), this.rectangle.getPosition().getY(), this.rectangle.getWidth(), this.rectangle.getHeight());
				var trans = new WorldViewTransformation(this.mapWidget.getMapView());
				var worldBounds = trans.viewBoundsToWorld(viewBounds);
				this.mapWidget.getMapView().applyBbox(worldBounds, true, geomajas.ZoomOption.ZOOM_OPTION_LEVEL_CHANGE);
			}

			if (this.rectangle != null) {
				dojo.publish (this.mapWidget.getRenderTopic(), [this.rectangle, "delete"]);
			}
			this.rectangle = null;
		}
	},

	/**
	 * If we're dragging, update the rectangle.
	 * @param event A HtmlMouseEvent object.
	 */
	mouseMoved : function (/*HtmlMouseEvent*/event) {
		if (this.dragging) {
			this._updateRectangle(event);

			dojo.publish (this.mapWidget.getRenderTopic(), [this.rectangle, "all"]);
		}
	},

	/**
	 * @private
	 */
	_updateRectangle : function (event) {
		var end = event.getPosition();

		var x = this.begin.getX();
		var y = this.begin.getY();
		var width = end.getX() - x;
		var height = end.getY() - y;
		if (x > end.getX()) {
			x = end.getX();
			width = -width;
		}
		if (y > end.getY()) {
			y = end.getY();
			height = -height;
		}

		this.rectangle.setPosition (new Coordinate(x, y));
		this.rectangle.setWidth (width);
		this.rectangle.setHeight (height);
	}
});
