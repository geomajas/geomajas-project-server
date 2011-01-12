dojo.provide("geomajas.controller.FeatureInfoController");
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