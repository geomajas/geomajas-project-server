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

dojo.provide("geomajas.map.RenderTopicDistributor");
dojo.declare("RenderTopicDistributor", null, {

	/**
	 * @fileoverview Distribution class for the external rendering topic.
	 * @class Distribution class for the external rendering topic. Anyone is able to
	 * publish new events on this topic, and this class is able to interprete
	 * those events.<br/>
	 * Since it's a field in the MapView class, it is automatically instantiated
	 * when using a MapWidget. The general name of this external rendering topic
	 * is: "<MapWidget.id>/render/ext".
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @param topic The name of the external topic. This class will subscribe
	 *              to it, and from then on, interprete the arguments on it.
	 * @param mapView Reference to the MapView object that is to execute the
	 *                rendering changes.
	 */
	constructor : function (/*String*/topic, /*MapView*/mapView) {
		this.topic = topic;
		this.mapView = mapView;
		dojo.subscribe (this.topic, this, "distribute");
	},
	
	/**
	 * Delegates the given arguments to the right function. At this time 4
	 * different events are supported: setBounds, translate, rotate, scale.
	 * Each of these need different arguments:
	 * <ul>
	 *     <li>event:setBounds => x, y, width, height</li>
	 *     <li>event:translate => x, y</li>
	 *     <li>event:rotate    => angle</li>
	 *     <li>event:scale     => delta</li>
	 * </ul>
	 * @param args The arguments on the external rendering topic.
	 */
	distribute : function (args) {
		if (args != null && args.event != null) {
			var evtName = args.event;

			// Needed arguments: x, y, width, height
			if (evtName == "setBounds") {
				this._applyExternalBbOX(args);
			}

			// Needed arguments: x, y
			else if (evtName == "translate") {
				this._applyTranslation(args);
			}

			// Needed arguments: angle
			else if (evtName == "rotate") {
				this._applyRotation(args);
			}

			// Needed arguments: delta
			else if (evtName == "zoom") {
				this._applyZooming(args);
			}

			// Needed arguments: scale
			else if (evtName == "scale") {
				this._applyScale(args);
			}
		}
	},

	/**
	 * This private function responds to an external publishing of a new bounds.
	 * It will get the x, y, width and height from the arguments, and then sets
	 * these new bounds on the map.
	 * @private
	 */
	_applyExternalBbOX : function (args) {
		var x = args.x;
		var y = args.y;
		var width = args.width;
		var height = args.height;
		var option = args.option;
		if (x != null && y != null && width != null && height != null) {
			var bounds = new Bbox (x, y, width, height);
			this.mapView.applyBbox (bounds, false, option);
		}
	},
	
	/**
	 * @private
	 */
	_applyTranslation : function (args) {
		var x = args.x;
		var y = args.y;
		if (x != null && y != null) {
			this.mapView.translate (x, y, "all");
		}
	},
	
	/**
	 * @private
	 */
	_applyRotation : function (args) {
		var angle = args.angle;
		if (angle != null) {
			this.mapView.rotate (angle);
		}
	},
	
	/**
	 * @private
	 */
	_applyZooming : function (args) {
		var delta = args.delta;
		var option = args.option;
		if (delta != null) {
			this.mapView.scale (delta, option);
		}
	},

	/**
	 * @private
	 */
	_applyScale : function (args) {
		var scale = args.scale;
		var option = args.option;
		if (scale != null) {
			this.mapView.setCurrentScale (scale, option);
		}
	}
});
