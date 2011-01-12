dojo.provide("geomajas.action.toolbar.util.MapViewQueueHandler");
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
dojo.declare("MapViewQueueHandler", null, {

	/**
	 * @fileoverview Manages a queue of map extents, and allows to zoom to
	 *               previous and next extent.
	 * @class Manages a queue of map extents, and allows to zoom to previous
	 *        and next extent.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @param mapWidget The map we need to keep an eye on.
	 * @param maxQueueSize The maximum size of the queue, in which we store
	 *                     the map extents (Bbox).
	 */
	constructor : function (mapWidget, maxQueueSize) {
		/** MapWidget to check. */
		this.mapWidget = mapWidget;

		/** The queue of map extents. */
		this.queue = [];

		/** Maximum size of the queue. */
		this.maxQueueSize = maxQueueSize;

		/** Where are we now in the queue? */
		this.currentQueuePosition = 0;

		/** Rendering topic to listen to. */
		dojo.subscribe(this.mapWidget.getRenderTopic(), this, "_onMapRender");

		var box = this.mapWidget.getMapView().getCurrentBbox();
		if (box) {
			this.queue.push(box);
		}

		/** @private */
		this.active = true;
	},



	//-------------------------------------------------------------------------
	// Class specific functions:
	//-------------------------------------------------------------------------

	/**
	 * Zoom to the previous bbox in the queue, starting from the current
	 * queue position.
	 * @return Returns true if the zooming was successful. If there was no
	 *         previous bbox, this functions will return false.
	 */
	zoomToPrevious : function () {
		if (this.currentQueuePosition > 0) {
			this.currentQueuePosition--;
			this.onQueueChange();
			var bbox = this.queue[this.currentQueuePosition];
			if (bbox) {
				this.active = false; // To prevent addition to the queue when going through the queue.
				this.mapWidget.getMapView().applyBbox(bbox, true, geomajas.ZoomOption.ZOOM_OPTION_LEVEL_CLOSEST);
				return true;
			}
		}
		return false;
	},

	/**
	 * Zoom to the next bbox in the queue, starting from the current
	 * queue position.
	 * @return Returns true if the zooming was successful. If there was no
	 *         next bbox, this functions will return false.
	 */
	zoomToNext : function () {
		if (this.currentQueuePosition < (this.maxQueueSize-1)) {
			this.currentQueuePosition++;
			this.onQueueChange();
			var bbox = this.queue[this.currentQueuePosition];
			if (bbox) {
				this.active = false; // To prevent addition to the queue when going through the queue.
				this.mapWidget.getMapView().applyBbox(bbox, true, geomajas.ZoomOption.ZOOM_OPTION_LEVEL_CLOSEST);
				return true;
			}
		}
		return false;
	},

	/**
	 * Connection point for changes in the queue. This function is called every
	 * time there the current position in the queue changes. In other words,
	 * zooming to previous or next and when registering a new map extent.
	 */
	onQueueChange : function () {
	},

	/**
	 * Reset the entire queue, without changing the map's extents.
	 */
	reset : function () {
		this.currentQueuePosition = 0;
		this.queue = [];
		this.queue.push(this.mapWidget.getMapView().getCurrentBbox());
		this.onQueueChange();
	},


	//-------------------------------------------------------------------------
	// Getters and setters:
	//-------------------------------------------------------------------------

	/**
	 * Returns the maximum size of the queue.
	 */
	getMaxQueueSize : function () {
		return this.maxQueueSize;
	},

	/**
	 * Returns the current position in the queue.
	 */
	getCurrentQueuePosition : function () {
		return this.currentQueuePosition;
	},

	/**
	 * Returns the current size of the queue.
	 */
	getCurrentQueueSize : function () {
		return this.queue.length;
	},



	//-------------------------------------------------------------------------
	// Private functions:
	//-------------------------------------------------------------------------

	/**
	 * Manages the queue when the map is rendered.
	 * @private
	 */
	_onMapRender : function (object, status) {
		if ((object == null || object instanceof MapModel) && status == "all") {
			// If the entire map is rendered:
			if (this.active) {
				// First: remove everything past the current position:
				this.queue.splice(this.currentQueuePosition+1, this.maxQueueSize);

				// Then add the new bbox:
				this.queue.push(this.mapWidget.getMapView().getCurrentBbox());

				// If there is one too many delete the first:
				if (this.queue.length > this.maxQueueSize) {
					this.queue.shift();
				}

				// When the map is re-rendered, we reset the current position in
				// the queue, to the last bbox.
				this.currentQueuePosition = this.queue.length - 1;
				this.onQueueChange();
			} else {
				this.active = true;
			}
		}
	}
});
