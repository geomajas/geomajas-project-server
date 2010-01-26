dojo.provide("geomajas.action.menu.editing.MergePolygonAction");
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
dojo.require("geomajas._base");
dojo.require("geomajas.action.Action");

dojo.declare("MergePolygonAction", Action, {

	constructor : function (id, mapWidget) {
		/** @private */
		this.mapWidget = mapWidget;

		/** Unique identifier */
		this.id = id;

		/** The action can be displayed as text only. */
		this.text = "Merge polygons";

		this.features = null;
	},

	actionPerformed : function (event) {
		var target = event.getTargetId();

		var selection = this.mapWidget.getMapModel().getSelection();
		if (selection.count > 1) {
			this.features = [];
			var polygons = [];
			var check = true;
			for (var i=0; i<selection.count; i++) {
				var geom = selection.item(i).getGeometry();
				if (geom instanceof MultiPolygon && geom.getNumGeometries() == 1) {
					geom = geom.getGeometryN(0);
				}
				if (!(geom instanceof Polygon)) {
					return;
				}
				this.features.push (selection.item(i));
				polygons.push(geom);
			}

			var command = new JsonCommand("command.geometry.MergePolygon","org.geomajas.extension.command.dto.MergePolygonRequest", null, false);
			command.addParam("polygons", polygons);
			command.addParam("allowMultiPolygon", true);
			var deferred = geomajasConfig.dispatcher.execute(command);
			deferred.addCallback(this, "_callback");
		}
	},

	/**
	 * @private
	 */
	_callback : function (result) {
		if (result.errorMessages.list.length > 0) {
			log.info("_callback : got an error !!!!");
			return;
		}
		var deserializer = new GeometryDeserializer();
		var feature = this.features[0].clone();
		// make it a new feature !!!
		feature.setId(null);
		var layer = this._getLayer();
		if (result.multiPolygon != null) {
			var multiPolygon = deserializer.createGeometryFromJSON(result.multiPolygon);
			if (layer.getLayerType() == geomajas.LayerTypes.POLYGON) {
				feature.setGeometry(polygon.getGeometryN(0));
			} else if (layer.getLayerType() == geomajas.LayerTypes.MULTIPOLYGON) {
				//var factory = polygon.getGeometryFactory();
				//var multi = factory.createMultiPolygon([polygon]);
				feature.setGeometry(multiPolygon);
			} else {
				return;
			}

			// APPLY
			var featureTransaction = this.mapWidget.getMapModel().getFeatureEditor().startEditing(this.features, [feature]);
			var handler = layer.getWorkflowHandler();
			var workflow = handler.startWorkflow(featureTransaction);
			workflow.start();

			dojo.publish (this.mapWidget.getRenderTopic(), [featureTransaction, "delete"]);
			this.mapWidget.getMapModel().getFeatureEditor().stopEditing(featureTransaction);
			dojo.publish(this.mapWidget.getMapModel().getSelectionTopic(), [ "deselectAll", null ]);
		}
		return result;
	},
	
	_getLayer : function () {
		var f = this.features[0];
		return this.mapWidget.getMapModel().getLayerById(f.getLayer().getLayerInfo().getId());
	}
});
