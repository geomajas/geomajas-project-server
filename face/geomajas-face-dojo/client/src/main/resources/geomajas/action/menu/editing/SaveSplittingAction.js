dojo.provide("geomajas.action.menu.editing.SaveSplittingAction");
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

dojo.declare("SaveSplittingAction", Action, {

	constructor : function (id, mapWidget) {
		/** @private */
		this.mapWidget = mapWidget;

		/** Unique identifier */
		this.id = id;

		/** The action can be displayed as text only. */
		this.text = "Save splitting";
		
		this.selected = null;
	},

	actionPerformed : function (event) {
		var curCon = this.mapWidget.getCurrentController();
		curCon.setSplitting(false);
		curCon.removeDragLines();

		// + actually save
		var trans = this.mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		dojo.publish(this.mapWidget.getRenderTopic(), [trans, "delete"]);
		var lineString = trans.getNewFeatures()[0].getGeometry();
		lineString.precision = 5;
		this.selected = curCon.getSelected();
		var geometry = this.selected.getGeometry();
		var polygon = null;
		if (geometry instanceof Polygon) {
			polygon = geometry;
		} else if (geometry instanceof MultiPolygon) {
			polygon = geometry.getGeometryN(0);
		} else {
			return;
		}
		var command = new JsonCommand("command.geometry.SplitPolygon","org.geomajas.command.dto.SplitPolygonRequest", null, false);
		command.addParam("splitter", lineString);
		command.addParam("geometry", polygon);
		var deferred = geomajasConfig.dispatcher.execute(command);
		deferred.addCallback(this, "_callback");
	},

	/**
	 * @private
	 */
	_callback : function (result) {
		var layerId = this.selected.getLayer().getId();
		var layer = this.mapWidget.getMapModel().getLayerById(layerId);

		var deserializer = new GeometryDeserializer();
		var features = [];
		if (result.geometries.length > 1) {
			for (var i=0; i<result.geometries.length; i++) {
				var feature = this.selected.clone();
				feature.setId(null);
				var polygon = deserializer.createGeometryFromJSON(result.geometries[i]);
				if (layer.getLayerType() == geomajas.LayerTypes.MULTIPOLYGON) {
					var factory = polygon.getGeometryFactory();
					var multiPolygon = factory.createMultiPolygon([polygon]);
					feature.setGeometry(multiPolygon);
				} else {
					feature.setGeometry(polygon);
				}
				features.push(feature);
			}

			// APPLY
			var featureTransaction = this.mapWidget.getMapModel().getFeatureEditor().startEditing([this.selected], features);
			var handler = layer.getWorkflowHandler();
			var workflow = handler.startWorkflow(featureTransaction);
			workflow.start();
		}

		// CANCEL
		var cancel = new CancelSplittingAction(this.id+".cancel", this.mapWidget);
		cancel.actionPerformed(null);
		return result;
	}
});
