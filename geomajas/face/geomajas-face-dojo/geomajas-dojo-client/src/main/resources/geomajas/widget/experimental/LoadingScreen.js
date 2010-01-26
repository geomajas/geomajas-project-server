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

dojo.provide("geomajas.widget.experimental.LoadingScreen");
dojo.require("dijit._Widget");
dojo.require("dijit._Templated");
dojo.requireLocalization("geomajas.widget", "activityDiv");

dojo.declare("geomajas.widget.experimental.LoadingScreen", [dijit._Widget, dijit._Templated], {

	templatePath : dojo.moduleUrl("geomajas.widget.experimental", "html/LoadingScreen.html"),

	/**
	 * Text that appears in the loading screen. Can be used to announce your
	 * application.
	 */
	loadText: "Loading application...",

	/**
	 * Duration in millis of the loadingscreen fade-out. 
	 */
	fadeOutDuration : 1000,

	/**
	 * Connection handle for the hiding function.
	 */
	hideHandle : null,

	/**
	 * This widget is initialized with the layer to which it must connect to
	 * the onFetchDone event. 
	 */
	init : function (layer) {
		if (layer != null) {
			log.debug("connecting loadingscreen to layer... " + layer.layerId);
			this.hideHandle = dojo.connect(
					layer.featureStore.cache, "onFetchDone", dojo.hitch(this, "hideLoader")
			);
			dojo.publish(layer.getMapModel().getRenderTopic(), [layer, "all"]);
		}
	},

	/**
	 * Destructor function. Cleans up the connections from the
	 * hiding-on-load-handle before destroying itself.
	 */
	destroy : function (finalize) {
		dojo.disconnect(this.hideHandle);
		this.inherited(arguments);
	},

	/**
	 * Make the loadscreen disappear.
	 */
	hideLoader : function () {
		var self = this;
		dojo.fadeOut({ node: self.domNode, duration: this.fadeOutDuration,
			onEnd: function(){ 
				self.domNode.style.display = "none"; 
			}
		}).play();
	}
});