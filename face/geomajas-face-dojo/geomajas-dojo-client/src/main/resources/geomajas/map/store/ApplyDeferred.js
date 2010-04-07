dojo.provide("geomajas.map.store.ApplyDeferred");
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