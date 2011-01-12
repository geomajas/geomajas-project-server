dojo.provide("geomajas.action.toolbar.ZoomNextAction");
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
dojo.require("geomajas.action.ToolbarAction");
dojo.require("geomajas.action.toolbar.util.MapViewQueueHandler");

dojo.declare("ZoomNextAction", ToolbarAction, {

	/**
	 * @fileoverview Zoom to the next view on the map.
	 * @class Zoom to the next view on the map.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends ToolbarAction
	 * @param id Unique identifier
	 * @param queueHandler Manages the map extent queue.
	 */
	constructor : function (id, queueHandler) {
		/** Unique identifier. */
		this.id = id;

		/** This tool's image representation. */
		this.image = "zoomNextIcon";

		/** Reference to the tooltip. */
		this.tooltip = this.tooltipLocale.ZoomNextAction;
		
		/** Manager for the map extent queue. */
		this.queueHandler = queueHandler;

		// Initialization:
		dojo.connect(this.queueHandler, "onQueueChange", dojo.hitch(this, "_onMapViewQueueChange"));
		this.setEnabled(false);
	},

	/**
	 * The action's execution function. Calls the MapViewQueueHandler's
	 * zoomToNext function.
	 * @param event Standard browser mouseevent.
	 */
	actionPerformed : function (event) {
		this.queueHandler.zoomToNext();
	},

	getQueueHandler : function () {
		return this.queueHandler;
	},

	/**
	 * @private 
	 */
	_onMapViewQueueChange : function () {
		if (this.queueHandler.getCurrentQueuePosition() == (this.queueHandler.getCurrentQueueSize()-1)) {
			this.setEnabled(false);
		} else {
			this.setEnabled(true);
		}
	}
});
