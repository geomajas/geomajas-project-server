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

dojo.provide("geomajas.controller.ZoomOnClickController");
dojo.require("geomajas.event.MouseListener");
dojo.require("geomajas.spatial.transform.WorldViewTransformation");

dojo.declare("ZoomOnClickController", MouseListener, {

	/**
	 * @fileoverview MouseListener for zooming by clicking.
	 * @class MouseListener implementation for zooming on a click event.
	 * Whether it zooms in or out, depends on the given scaleFactor.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends MouseListener
	 * @param mapView Reference to a MapWidget's viewing object.
	 * @param scaleFactor Determines the difference in scale.
	 */
	constructor : function (mapView, scaleFactor, name) {
		/** Reference to a MapWidget's MapView object. */
		this.mapView = mapView;
		
		/** The scale-delta. Bigger then 1 zooms in, smaller then 1 zooms out. */
		this.scaleFactor = scaleFactor;
		
		this.name = name;
	},

	/**
	 * Return a unique name for this MouseListener.
	 */
	getName : function () {
		if (this.name) {
			return this.name;
		}
		return "ZoomOnClickController";
	},

	/**
	 * Zooms in or out around the position where the user clicked.
	 * @param event A HtmlMouseEvent object.
	 */
	mouseClicked : function (/*HtmlMouseEvent*/event) {
		var transform = new WorldViewTransformation(this.mapView);

		var viewPosition = event.getPosition();
		var worldPosition = transform.viewPointToWorld(viewPosition);
		
		this.mapView.getCamera().setPosition (worldPosition.getX(), worldPosition.getY());
		this.mapView.scale(this.scaleFactor, geomajas.ZoomOption.ZOOM_OPTION_LEVEL_CHANGE);
	}

});