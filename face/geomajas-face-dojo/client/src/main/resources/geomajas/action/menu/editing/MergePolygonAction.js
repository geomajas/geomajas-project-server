dojo.provide("geomajas.action.menu.editing.MergePolygonAction");
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

			var command = new JsonCommand("command.geometry.MergePolygon","org.geomajas.command.dto.MergePolygonRequest", null, false);
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
		if (result.geometry != null) {
			var multiPolygon = deserializer.createGeometryFromJSON(result.geometry);
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
