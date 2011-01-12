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

dojo.provide("geomajas.map.editing.FeatureTransaction");
dojo.require("dojox.collections.Stack");
dojo.require("geomajas.gfx.PainterVisitable");

dojo.declare("FeatureTransaction", PainterVisitable, {

	/**
	 * @fileoverview Representation of edited features.
	 * @class This class represents the editing of one or more features.
	 * For example when the oldFeatures are null, and there is one empty
	 * feature in the newFeatures, the FeatureEditor will know that the user
	 * wants to create a new feature.
	 *
	 * @constructor
	 * @param oldFeatures Array of features as they currently are.
	 * @param newFeatures Array of features that are to be edited.
	 */
	constructor : function (oldFeatures, newFeatures, layer, crs) {
		this.commandStack = new dojox.collections.Stack();
		this.newFeatures = newFeatures;
		this.oldFeatures = oldFeatures;
		this.setLayer(layer);
        this.crs = crs
		
		this.geometryChanged = true; // can speed things up.
	},

	/**
	 * Execute a new {@link EditCommand} on this featuretransaction. This
	 * command will also be added to a stack, so it is possible to undo it
	 * again.
	 * @param editCommand The EditCommand object.
	 */
	execute : function (editCommand) {
		this.commandStack.push(editCommand);
		editCommand.execute(this.newFeatures);
	},

	/**
	 * This class is visitable by painter. The {@link FeatureTransActionPainter}
	 * should be able to paint edited features after all...
	 * @param visitor PainterVisitor.
	 */
	accept : function (visitor, bbox, recursive) {
		visitor.visit(this);
	},

	getId : function () {
		return "featureTransaction";
	},

	/**
	 * Convert this object to something that can be sent through JSON to the server.
	 */
	toJSON : function () {
		// old features:
		oldFeatures = null;
		if (this.oldFeatures != null) {
			oldFeatures = [];
			for (var i=0; i<this.oldFeatures.length; i++) {
				oldFeatures[i] = this.oldFeatures[i].toJSON();
			}
		}

		// new features:
		newFeatures = null;
		if (this.newFeatures != null) {
			newFeatures = [];
			for (var i=0; i<this.newFeatures.length; i++) {
				newFeatures[i] = this.newFeatures[i].toJSON();
			}
		}

        return {
			javaClass : "org.geomajas.layer.feature.FeatureTransaction",
			oldFeatures : oldFeatures,
			newFeatures : newFeatures,
			layerId : this.layerId
		};
	},

	/**
	 * Accept a JSON object and interpret it.
	 */
	fromJSON : function (json, layer) {
		this.setLayer(layer);

		// old features:
		this.oldFeatures = null;
		if (json.oldFeatures != null) {
			this.oldFeatures = [];
			for (var i=0; i<json.oldFeatures.length; i++) {
				this.oldFeatures[i] = new Feature();
				this.oldFeatures[i].setLayer(this.layer);
				this.oldFeatures[i].fromJSON(json.oldFeatures[i]);
			}
		}

		// new features:
		this.newFeatures = null;
		if (json.newFeatures != null) {
			this.newFeatures = [];
			for (var i=0; i<json.newFeatures.length; i++) {
				this.newFeatures[i] = new Feature();
				this.newFeatures[i].setLayer(this.layer);
				this.newFeatures[i].fromJSON(json.newFeatures[i]);
			}
		}
	},

	isDragTarget : function (identifier) {
		var index = this.getPointIndexById(identifier);
		if (index == null || index[1] == null || index[1].length == 0) {
			return false;
		}
		return true;
	},

	/**
	 * Returns null if the identifier doesn't match a FeatureTransaction DOM
	 * id, or an array with 2 values in it. The first value in this array is
	 * the feature's index in the "newFeatures" array, to determine which
	 * feature this identifier belongs to. The second value is an array of
	 * values which can be used to get the correct coordinate through the
	 * geometry.getCoordinateN function. Unless ofcourse, the array has length
	 * 0, in which case the identifier did not belong to a point (but probably
	 * of the interior of the geometry)
	 * @param identifier DOM element point id.
	 */
	getPointIndexById : function (identifier) {
		if (identifier == null || identifier.indexOf("featureTransaction") < 0) {
			return null;
		}
		var pos = "featureTransaction.feature".length;
		var temp = identifier.substring (pos);
		var featureIndex = parseInt(temp.substring(0,1));
		if (isNaN(featureIndex)) {
			return null;
		}
		var geometry = this.newFeatures[featureIndex].getGeometry();
		var index = [];
		this._getGeometryIndex(index, geometry, temp, true);
		return [featureIndex, index];
	},

	getLineStringIndexById : function (identifier) {
		if (identifier == null || identifier.indexOf("featureTransaction") < 0) {
			return null;
		}
		var pos = "featureTransaction.feature".length;
		var temp = identifier.substring (pos);
		var featureIndex = parseInt(temp.substring(0,1));
		if (isNaN(featureIndex)) {
			return null;
		}
		var geometry = this.newFeatures[featureIndex].getGeometry();
		var index = [];
		this._getGeometryIndex(index, geometry, temp, false);
		return [featureIndex, index];
	},
	
	/**
	 * Is the element with given identifier part of a hole?
	 * @param identifier The ID attribute from the element.
	 * @param areaOnly Determines whether only the internal area may count,
	 *                 or the borders too.
	 * @return Returns true if it is a polygon's hole.
	 */
	isHole : function (identifier, areaOnly) {
		if(identifier == null || !identifier.match("featureTransaction.feature")){
			return false;
		}
		var pos = "featureTransaction.feature".length;
		var temp = identifier.substring (pos);
		var featureIndex = parseInt(temp.substring(0,1));
		if (isNaN(featureIndex)) {
			return false;
		}
		var geometry = this.newFeatures[featureIndex].getGeometry();
		return this._isGeometryHole(geometry, temp, areaOnly);
	},
	
	isEdge : function (identifier) {
		if(identifier == null || !identifier.match("featureTransaction.feature")){
			return false;
		}
		var pos = "featureTransaction.feature".length;
		var temp = identifier.substring (pos);
		var featureIndex = parseInt(temp.substring(0,1));
		if (isNaN(featureIndex)) {
			return false;
		}
		pos = temp.indexOf("edge");
		if (pos > 0) {
			return true;
		}
		return false;
	},
	
	isVertex : function (identifier) {
		if(identifier == null || !identifier.match("featureTransaction.feature")){
			return false;
		}
		var pos = "featureTransaction.feature".length;
		var temp = identifier.substring (pos);
		var featureIndex = parseInt(temp.substring(0,1));
		if (isNaN(featureIndex)) {
			return false;
		}
		pos = temp.indexOf("coordinate");
		if (pos > 0) {
			return true;
		}
		return false;
	},

	/**
	 * CommandStack is not yet cloned!!!
	 */
	clone : function () {
		var newFeatures = null;
		if (this.newFeatures != null) {
			var newFeatures = [];
			for (var i=0; i<this.newFeatures.length; i++) {
				newFeatures[i] = this.newFeatures[i].clone();
			}
		}

		var oldFeatures = null;
		if (this.oldFeatures != null) {
			var oldFeatures = [];
			for (var i=0; i<this.oldFeatures.length; i++) {
				oldFeatures[i] = this.oldFeatures[i].clone();
			}
		}

		var ft = new FeatureTransaction(oldFeatures, newFeatures, this.layer, this.crs);
		return ft;
	},

	// Getters and setters:

	getCommandStack : function () {
		return this.commandStack;
	},

	setCommandStack : function (commandStack) {
		this.commandStack = commandStack;
	},

	getNewFeatures : function () {
		return this.newFeatures;
	},

	getOldFeatures : function () {
		return this.oldFeatures;
	},

	setLayer : function (layer) {
		this.layer = layer;
		this.layerId = null;
		if (layer != null) {
			this.layerId = layer.layerId;
		}
	},
	
	getLayer : function () {
		return this.layer;
	},
	
	getCrs : function () {
		return this.crs;
	},

	setGeometryChanged : function (geometryChanged) {
		this.geometryChanged = geometryChanged;
	},
	
	isGeometryChanged : function () {
		return this.geometryChanged;
	},
	
	/**
	 * @private
	 */
	_isGeometryHole : function (geometry, identifier, areaOnly) {
		if (geometry instanceof MultiPolygon) {
			return this._isMultiPolygonHole(geometry, identifier, areaOnly);
		} else if (geometry instanceof Polygon) {
			return this._isPolygonHole(geometry, identifier, areaOnly);
		}
		return false;
	},
	
	/**
	 * @private
	 */
	_isMultiPolygonHole : function (geometry, identifier, areaOnly) {
		var pos = identifier.indexOf("polygon");
		if (pos > 0) {
			var temp = identifier.substring(pos + ("polygon".length));
			var polygonIndex = parseInt(temp.substring(0,1));
			var polygon = geometry.getGeometryN(polygonIndex);
			return this._isPolygonHole(polygon, temp, areaOnly);
		}
		return false;
	},

	/**
	 * @private
	 */
	_isPolygonHole : function (geometry, identifier, areaOnly) {
		var pos = identifier.indexOf("hole");
		if (areaOnly) {
			var pos2 = identifier.indexOf("area");
			if (pos > 0 && pos2 > 0) {
				return true;
			}
		} else if (pos > 0) {
			return true;
		}
		return false;
	},

	/**
	 * @private
	 */
	_getGeometryIndex : function (index, geometry, identifier, pointOnly) {
		if (pointOnly && identifier.indexOf("coordinate") < 0 && identifier.indexOf("edge") >= 0) {
			return;
		}

		if (geometry instanceof MultiPolygon) {
			this._getMultiPolygonIndex(index, geometry, identifier, pointOnly);
		} else if (geometry instanceof MultiLineString) {
			this._getMultiLineStringIndex(index, geometry, identifier, pointOnly);
		} else if (geometry instanceof LineString) {
			this._getLineStringIndex(index, geometry, identifier, pointOnly);
		} else if (geometry instanceof Polygon) {
			this._getPolygonIndex(index, geometry, identifier, pointOnly);
		} else if (geometry instanceof LinearRing) {
			this._getLinearRingIndex(index, geometry, identifier, pointOnly);
		} else if (geometry instanceof Point) {
			this._getPointIndex(index, geometry, identifier, pointOnly);
		}
	},

	/**
	 * @private
	 */
	_getMultiPolygonIndex : function (index, geometry, identifier, pointOnly) {
		var pos = identifier.indexOf("polygon");
		if (pos > 0) {
			var temp = identifier.substring(pos + ("polygon".length));
			var dotPos = temp.indexOf(".");
			var polygonIndex = parseInt(temp.substring(0, dotPos));
			index.push(polygonIndex);
			var polygon = geometry.getGeometryN(polygonIndex);
			this._getPolygonIndex(index, polygon, temp, pointOnly);
		}
	},

	/**
	 * @private
	 */
	_getMultiLineStringIndex : function (index, geometry, identifier, pointOnly) {
		var pos = identifier.indexOf("lineString");
		if (pos > 0) {
			var temp = identifier.substring(pos + ("lineString".length));
			var dotPos = temp.indexOf(".");
			var lineStringIndex = parseInt(temp.substring(0, dotPos)); // TODO: multilinestrings with more then 10 linestrings will fail here!
			index.push(lineStringIndex);
			var lineString = geometry.getGeometryN(lineStringIndex);
			this._getLineStringIndex(index, lineString, temp, pointOnly);
		}
	},

	/**
	 * @private
	 */
	_getPolygonIndex : function (index, geometry, identifier, pointOnly) {
		var pos = identifier.indexOf("shell");
		if (pos > 0) {
			var temp = identifier.substring(pos + ("shell".length));
			index.push(0);
			var shell = geometry.getExteriorRing();
			this._getLinearRingIndex(index, shell, temp, pointOnly);
		} else {
			pos = identifier.indexOf("hole");
			if (pos > 0) {
				var temp = identifier.substring(pos + ("hole".length));
				var dotPos = temp.indexOf(".");
				var holeIndex = parseInt(temp.substring(0, dotPos));
				index.push(holeIndex+1);
				var hole = geometry.getInteriorRingN(holeIndex);
				this._getLinearRingIndex(index, hole, temp, pointOnly);
			}
		}
	},

	/**
	 * @private
	 */
	_getLinearRingIndex : function (index, geometry, identifier, pointOnly) {
		/*if (!pointOnly) {
			return;
		}*/
		var pos = identifier.indexOf("coordinate");
		if (pos > 0) {
			var temp = identifier.substring(pos + ("coordinate".length));
			var coordinateIndex = parseInt(temp.substring(0));
			index.push(coordinateIndex);
		} else {
			pos = identifier.indexOf("edge");
			if (pos > 0) {
				var temp = identifier.substring(pos + ("edge".length));
				var coordinateIndex = parseInt(temp.substring(0));
				index.push(coordinateIndex);
				log.error ("Point through edge !!!! "+index);
			}
		}
	},

	/**
	 * @private
	 */
	_getLineStringIndex : function (index, geometry, identifier, pointOnly) {
		/*if (!pointOnly) {
			return;
		}*/
		var pos = identifier.indexOf("coordinate");
		if (pos > 0) {
			var temp = identifier.substring(pos + ("coordinate".length));
			var coordinateIndex = parseInt(temp.substring(0));
			index.push(coordinateIndex);
		} else {
			pos = identifier.indexOf("edge");
			if (pos > 0) {
				var temp = identifier.substring(pos + ("edge".length));
				var coordinateIndex = parseInt(temp.substring(0));
				index.push(coordinateIndex);
				log.error ("Point through edge !!!! "+index);
			}
		}
	},

	/**
	 * @private
	 */
	_getPointIndex : function (index, geometry, identifier, pointOnly) {
		if (!pointOnly) {
			return;
		}
		var keyword = "coordinate";
		var pos = identifier.indexOf("coordinate");
		if(pos < 0){
			keyword = "edge";
			pos = identifier.indexOf("edge");
		}
		if (pos > 0) {
			var temp = identifier.substring(pos + (keyword.length));
			var coordinateIndex = parseInt(temp.substring(0));
			index.push(coordinateIndex);
		}
	}
});