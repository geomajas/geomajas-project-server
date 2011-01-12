dojo.provide("geomajas.map.store.ApplyDeferred");
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

dojo.declare("ApplyDeferred", null , {
	constructor : function (/*RemoteStore*/ store, /*Bbox*/bounds, /*String*/filter, /*Function*/callback) {
		this.store = store;
		this.bounds = bounds;
		this.filter = filter;
		this.callback = callback;
	},
	
	apply : function (data) {
		this.store.applyOnBounds(this.bounds, this.filter, this.callback);
	}
});