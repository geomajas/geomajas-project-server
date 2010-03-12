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

dojo.provide("geomajas.controller.MouseInfoController");
dojo.require("geomajas.event.MouseListener");
/**
 *  * @author Kristof Heirwegh, Balder Van Camp
 */
dojo.declare("MouseInfoController", MouseListener, {

	constructor : function (mapWidget, widgetLocale, showViewCoords, showWorldCoords, left, right, top, bottom, opacity) {
		this.mapWidget = mapWidget;
		this.widgetLocale = widgetLocale;
		this.showViewCoords = showViewCoords;
		this.showWorldCoords = showWorldCoords;
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
		this.opacity = opacity;
		this.balloonWidth = (showViewCoords == "true" && showWorldCoords == "true" ? 140 : 100);
		this.balloonHeight = (showViewCoords == "true" && showWorldCoords == "true" ? 70 : 37);
		this.textPre = '';//'<div style="width:' + this.balloonWidth + 'px; height:' + this.balloonHeight + 'px; overflow:hidden;">';
		this.textPost = ''; //"</div>";
		
		// i18n
		this.labelX = widgetLocale.x;
		this.labelY = widgetLocale.y;
		this.labelViewX = widgetLocale.viewx;
		this.labelViewY = widgetLocale.viewy;
		this.labelWorldX = widgetLocale.worldx;
		this.labelWorldY = widgetLocale.worldy;

		/** For transforming! */
		this.trans = new WorldViewTransformation(this.mapWidget.getMapView());
	},

	getName : function () {
		return "MouseInfoController";
	},

	mouseMoved : function (event) {
		var text = this.textPre;
		var both = (this.showViewCoords == "true" && this.showWorldCoords == "true");
		if (this.showViewCoords == "true") {
			text += (both == true ? this.widgetLocale.viewx : "X: ") + dojo.number.format(event.position.x) + "<br/>";
			text += (both == true ? this.widgetLocale.viewy : "Y: ") + dojo.number.format(event.position.y);
		}
		if (this.showWorldCoords == "true") {
			if (this.showViewCoords == "true") {
				text += '<br />';
			}
			var worldPos = this.trans.viewPointToWorld(event.getPosition());
			text += (both == true ? this.widgetLocale.worldx : "X: ") + dojo.number.format(worldPos.x) + "<br/>";
			text += (both == true ? this.widgetLocale.worldy : "Y: ") + dojo.number.format(worldPos.y);
		}
		text += this.textPost;
		this.balloon.setText(text);
	},
	
	/**
	 * Extra function for killing the balloon.
	 */
	killBalloon : function () {
		if (this.balloon != null) {
			dojo.disconnect(this.mapHandle);
			this.balloon.destroy();
			this.balloon = null;
		}
	},

	createBalloon : function () {
		this.balloon = new geomajas.widget.TextBalloon({id:"mouseInfoBalloon"}, document.createElement("div"));
		dojo.disconnect(this.balloon.handle);
		this.balloon.opacity = this.opacity;
		this.balloon.setPosition(this._calculatePos());
		this.balloon.setText(this.textPre + "&nbsp;" + this.textPost);
		this.balloon.render(this.mapWidget.domNode);
		var w = this.balloonWidth+'px';
		var h = this.balloonHeight+'px';
		dojo.style('mouseInfoBalloon', {width: w, height: h});
		this.mapHandle = dojo.connect(this.mapWidget, "layout", dojo.hitch(this, "_mapLayoutChange"));
	},
	
	_calculatePos : function () {
		var maxWidth = this.mapWidget.getMapView().getMapWidth();
		var maxHeight = this.mapWidget.getMapView().getMapHeight();
		var x,y;
		if (this.right > this.left)
			x = maxWidth - this.right - this.balloonWidth;
		else
			x = this.left;
		if (this.bottom > this.top)
			y = maxHeight - this.bottom - this.balloonHeight;
		else
			y = this.top;
		
		return new Coordinate (x, y);
	}, 
	
	_mapLayoutChange : function () {
		this.balloon.setPosition(this._calculatePos());
	}
		
});