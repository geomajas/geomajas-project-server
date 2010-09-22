dojo.provide("geomajas.action.toolbar.ZoomNextAction");
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
