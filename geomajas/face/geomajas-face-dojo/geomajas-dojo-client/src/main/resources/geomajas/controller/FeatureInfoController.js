dojo.provide("geomajas.controller.FeatureInfoController");
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

dojo.declare("FeatureInfoController", MouseListener, {

	/**
	 * @fileoverview Mouselistener for getting feature info.
	 * @class MouseListener implementation for getting feature info.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends MouseListener
	 * @param mapWidget Reference to the MapWidget.
	 */
	constructor : function (mapWidget) {
		/** Reference to the MapWidget. */
		this.mapWidget = mapWidget;
	},

	/**
	 * Return a unique name.
	 */
	getName : function () {
		return "FeatureInfoController";
	},

	mouseReleased : function (event) {
		if (event.getButton() != event.statics.RIGHT_MOUSE_BUTTON) {
			var featureReference = new FeatureReference(event);
			var scale = this.mapWidget.getMapModel().getMapView().getCurrentScale();
			featureReference.setBuffer(scale ? (5/scale) : 0);
			this.mapWidget.getMapModel().applyOnFeatureReference(featureReference, dojo.hitch(this, "_showFeature"));
		}
	},

	/**
	 * @private
	 */
	_showFeature : function (feature) {
		var floaterId = this.getName()+":"+feature.getId();
		if (dijit.byId(floaterId) != null) {
			return;
		}

		var height = 65; // Height of the toobar etc
		var attr = feature.layer.getFeatureType().getVisibleAttributes(); //dictionary
		var keys = attr.getKeyList();
		for (attr in keys) {
			height += 25;
		}
		if (height > 600) {
			height = 600;
		}

		var floater = new geomajas.widget.FloatingPane({
			id:floaterId,
			title: "Feature detail - " + feature.getLabel(),
			dockable: false,
			maxable: false,
			closable: true,
			resizable: false
		},null);

		floater.setContent("<div id=\""+floaterId+":featureEditor\" dojoType=\"geomajas.widget.FeatureDetailEditor\" style=\"width:100%; height: 100%; overflow:hidden;\"></div>");
		floater.domNode.style.overflow = "hidden";
		floater.startup();
		floater.resize({ w:420, h:height, l:20, t:20 });

		var div = dojo.body();
		if (geomajasConfig.connectionPoint) {
			var div = dojo.byId(geomajasConfig.connectionPoint);
		}
		div.appendChild (floater.domNode);

		var editor = dijit.byId(floaterId+":featureEditor");
		editor.setFeature(feature);
		editor.setMapWidget (this.mapWidget);

		floater.show();
		floater.bringToTop();
	},

	/**
	 * Extra function for killing the balloon.
	 */
	killBalloon : function () {
	}
});