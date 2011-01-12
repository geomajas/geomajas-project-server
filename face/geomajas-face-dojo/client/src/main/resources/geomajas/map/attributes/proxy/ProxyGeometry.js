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

dojo.provide("geomajas.map.attributes.proxy.ProxyGeometry");
dojo.declare("ProxyGeometry", null, {

	constructor : function (layerId, featureId, crs) {
		this.layerId = layerId;
		this.featureId = featureId;
		this.crs = crs;
		
		this.result = null;
	},
	
	clone : function () {
		return new ProxyGeometry(this.layerId, this.featureId, this.crs);
	},

	getValue : function () {
		var command = new JsonCommand("command.feature.Search",
				"org.geomajas.command.dto.SearchFeatureRequest", null, true);
		command.addParam("layerId", this.layerId);
		command.addParam("crs", this.crs);
		command.addParam("criteria", [{
			javaClass : "org.geomajas.layer.feature.SearchCriterion",
			attributeName : "$id",
			operator : "=",
			value : this.featureId
		}]);
		command.addParam("featureIncludes", 2); // 1=attributes, 2=geometry
		var deferred = geomajasConfig.dispatcher.execute(command);
		deferred.addCallback(this, "_callback");
		return this.result;
	},

	_callback : function (result){
		if (result.features && result.features[0]) {
			var deserializer = new GeometryDeserializer();
			this.result = deserializer.createGeometryFromJSON(result.features[0].geometry);
		}
	}
});