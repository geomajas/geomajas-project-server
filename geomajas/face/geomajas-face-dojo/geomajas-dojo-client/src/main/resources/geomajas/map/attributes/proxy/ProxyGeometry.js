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

dojo.provide("geomajas.map.attributes.proxy.ProxyGeometry");
dojo.declare("ProxyGeometry", null, {

	constructor : function (layerId, featureId) {
		this.layerId = layerId;
		this.featureId = featureId;
		
		this.result = null;
	},
	
	clone : function () {
		return new ProxyGeometry(this.layerId, this.featureId);
	},

	getValue : function () {
		var command = new JsonCommand("command.geometry.GetGeometry",
                "org.geomajas.extension.command.dto.GetGeometryRequest", null, true);
		command.addParam("layerId", this.layerId);
		command.addParam("featureId", this.featureId);
		var deferred = geomajasConfig.dispatcher.execute(command);
		deferred.addCallback(this, "_callback");
		return this.result;
	},

	_callback : function (result){
		if (result.geometry) {
			var deserializer = new GeometryDeserializer();
			this.result = deserializer.createGeometryFromJSON(result.geometry);
		}
	}
});