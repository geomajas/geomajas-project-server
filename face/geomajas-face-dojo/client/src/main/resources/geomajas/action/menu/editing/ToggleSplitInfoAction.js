dojo.provide("geomajas.action.menu.editing.ToggleSplitInfoAction");
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
dojo.require("geomajas.action.Action");

dojo.declare("ToggleSplitInfoAction", Action, {

	constructor : function (id, mapWidget, splitController) {
		/** @private */
		this.mapWidget = mapWidget;

		this.splitController = splitController;

		/** Unique identifier */
		this.id = id;

		/** The action can be displayed as text only. */
		this.text = "Toggle info";
//		if (splitController.getGeomInfo() == null) {
//			this.text = "Show info";
//		} else {
//			this.text = "Hide info";
//		}
	},

	/**
	 * Simply changes the {@link EditingController}'s mode field.
	 * @param event The {@link HtmlMouseEvent} from clicking this action.
	 */
	actionPerformed : function (event) {
		var geomInfo = this.splitController.getGeomInfo();
		if (geomInfo == null) {
			this._calcSplit(event);
		} else {
			// Remove from the map:
			for (var i=0; i<geomInfo.length; i++) {
				geomInfo[i].destroy();
			}

			// Then remove from controller:
			this.splitController.setGeomInfo(null);
		}
	},

	/**
	 * @private
	 */
	_calcSplit : function (event) {
		var trans = this.mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		var lineString = trans.getNewFeatures()[0].getGeometry();
		lineString.precision = 5;
		var geometry = this.splitController.getSelected().getGeometry();
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
		var layerId = this.splitController.getSelected().getLayer().getId();
		var layer = this.mapWidget.getMapModel().getLayerById(layerId);

		var deserializer = new GeometryDeserializer();
		var polygons = [];
		for (var i=0; i<result.geometries.length; i++) {
			polygons.push (deserializer.createGeometryFromJSON(result.geometries[i]));
		}
		this._createSplitInfoBalloons(polygons);
		return result;
	},

	/**
	 * @private
	 */
	_createSplitInfoBalloons : function (polygons) {
		var balloons = [];
		for (var i=0; i<polygons.length; i++) {
			var pol = polygons[i];
			var transform = this.splitController.getTransform();
			var centroid = transform.worldPointToView(pol.getCentroid());
			var name = "splitInfoBalloon"+i;
			dijit.registry.remove(name);
			var balloon = new geomajas.widget.TextBalloon({id:name}, document.createElement("div"));
			balloon.setPosition(centroid);
			balloon.setText("Geometric info:<br/>Length: "+pol.getLength().toFixed(2)+"<br/>Area: "+pol.getArea().toFixed(2));
			balloon.render(this.mapWidget.domNode);
			balloons.push(balloon);
		}
		this.splitController.setGeomInfo(balloons);
	}
});
