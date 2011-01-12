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