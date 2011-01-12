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

dojo.provide("geomajas.controller.LocationInfoController");
dojo.require("geomajas.event.MouseListener");

dojo.declare("LocationInfoController", MouseListener, {

	/**
	 * @fileoverview Mouselistener for getting location info.
	 * @class MouseListener implementation for getting location info.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends MouseListener
	 * @param mapWidget Reference to the MapWidget.
	 */
	constructor : function (mapWidget) {
		/** Reference to the MapWidget. */
		this.mapWidget = mapWidget;

		/** The current textballoon, or null if none is shown. */
		this.balloon = null;

		/** The minimum distance between a textballoon and the map's borders. */
		this.borderMargin = 10;
		
		/** For transforming! */
		this.trans = new WorldViewTransformation(this.mapWidget.getMapView());
		
	},

	/**
	 * Return a unique name.
	 */
	getName : function () {
		return "FeatureInfoController";
	},

	/**
	 * Show a {@link geomajas.widget.TextBalloon} object that describes the clicked feature's identifying attributes.
	 */
	mouseClicked : function (event) {
		if (event.getButton() != event.statics.RIGHT_MOUSE_BUTTON) {
			log.info("LocationInfoController : info of "+event);
			
			this.killBalloon();

			this.balloon = new geomajas.widget.TextBalloon({id:"featureInfoBalloon"}, document.createElement("div"));
			
			var text = "Position info: <br/>";
			text += "Client X: " + event.position.x + "<br/>";
			text += "Client Y: " + event.position.y + "<br/>";
			var worldPos = this.trans.viewPointToWorld(event.getPosition());

			text += "World X: " + Math.round(worldPos.x*100)/100 + "<br/>";
			text += "World Y: " + Math.round(worldPos.y*100)/100 + "<br/>";

			
			text += "viewScale: " + this.mapWidget.getMapView().getCurrentScale() + "<br/>";
			
			
			this.balloon.setText(text);
			this.balloon.render(this.mapWidget.domNode);
			this._positionBalloon(event);

			this.positionSelected(worldPos);
		}
	},
	
	/**
	 * To be fired, gets world position.
	 */
	positionSelected : function (position) {
	
	},

	/**
	 * Extra function for killing the balloon.
	 */
	killBalloon : function () {
		if (this.balloon != null) {
			this.balloon.destroy();
			this.balloon = null;
		}
	},

	/**
	 * Sets a new value for the borderMargin.
	 */
	setBorderMargin : function () {
		this.borderMargin = borderMargin;
	},

	/** @private */
	_positionBalloon : function (event) {
		if (this.balloon == null) {
			return;
		}

		var position = event.getPosition().clone();
		var maxWidth = this.mapWidget.getMapView().getMapWidth() - this.borderMargin;
		var maxHeight = this.mapWidget.getMapView().getMapHeight() - this.borderMargin;

		var balloonWidth = this.balloon.getWidth();
		var balloonHeight = this.balloon.getHeight();

		if ((position.getX() + balloonWidth) > maxWidth) {
			position.setX(maxWidth - balloonWidth);
		}
		if ((position.getY() + balloonHeight) > maxHeight) {
			position.setY(maxHeight - balloonHeight);
		}
		if (position.getX() < this.borderMargin) {
			position.setX(this.borderMargin);
		}
		if (position.getY() < this.borderMargin) {
			position.setY(this.borderMargin);
		}

		this.balloon.setPosition(position);
	}

});