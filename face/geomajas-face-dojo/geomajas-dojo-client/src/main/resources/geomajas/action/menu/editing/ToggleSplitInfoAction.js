dojo.provide("geomajas.action.menu.editing.ToggleSplitInfoAction");
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
