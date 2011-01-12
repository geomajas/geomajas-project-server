dojo.provide("geomajas.map.Feature");
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
dojo.require("geomajas.gfx.PainterVisitable");
dojo.require("geomajas.gfx.PainterVisitor");

dojo.require("geomajas.map.attributes.proxy.ProxyAttributeMap");
dojo.require("geomajas.map.attributes.proxy.ProxyGeometry");

dojo.declare("Feature", PainterVisitable, {

	/**
	 * @fileoverview Element of a layer (vectorlayer).
	 * @class The objects in layers are called Features. And these features in
	 * turn are a composition of a Geometry (for the geographical representation)
	 * and attributes (for alpha-numeric representation). Since features need
	 * to be painted on the map, this class must implement the PainterVisitable
	 * interface.
	 * @author Pieter De Graef & Jan De Moerloose
	 *
	 * @constructor
	 * @extends PainterVisitable
	 */
	constructor : function () {
		/** Unique identifier, over layers, and even maps. */
		this.id = null;

		/** Reference to the layer. */
		this.layer = null;

		/** Map of this feature's attributes. */
		this.attributes = {};

		/** This feature's geometry. */
		this.geometry = null;
		
		/** This feature's path attribute */
		this.path = null;

		/** The identifier of this feature's style. */
		this.styleId = null;

		/** Coordinate for the label. */
		this.labelPosition = null;
		
		/** is the feature clipped ? */
		this.clipped = false;
		
		this.editable = false;

		this.crs = null;
	},

	/**
	 * Implementation of the PainterVisitable interface.
	 * Accept visitor visiting.
	 */
	accept : function (/*PainterVisitor*/visitor, /*Bbox*/ bbox, recursive) {
		visitor.visit(this);
	},

	/**
	 * Create a clone of this feature.
	 */
	clone : function () {
		var f = new Feature();
		f.setId(this.id);
		f.setLayer(this.getLayer());
		f.setCrs(this.crs);
		f.styleId = this.styleId;
		
		if (this.geometry != null) {
			f.setGeometry(this.geometry.clone()); // proxy or not; works on both
		}
		if (this.attributes != null) {
			if (this.attributes instanceof ProxyAttributeMap || 
					this.attributes.declaredClass == "ProxyAttributeMap") {
				f.setAttributes(this.attributes.clone());
			} else {
				var attributes = {};
				for (attr in this.attributes) {
					attributes[attr] = this.attributes[attr];
				}
				f.setAttributes(attributes);
			}
		}
		if (this.labelPosition != null) {
			f.setLabelPosition(this.labelPosition.clone());
		}
		f.setClipped(this.clipped);
		f.setPath(this.path);
		return f;
	},



	//-------------------------------------------------------------------------
	// JSON serialization:
	//-------------------------------------------------------------------------

	/**
	 * Prepare this object for JSON serialization.
	 * @returns A recursive hashmap of name-value pairs.
	 */
	toJSON : function () {
        var jsonAttr = {};
        var temp = this.getAttributes(); // make sure they are actually fetched!
        for (attr in temp) {
            // special treatment for dates !
            if(temp[attr].value instanceof Date){
            	jsonAttr[attr] = temp[attr];
            	jsonAttr[attr].value = {
                      "javaClass": "java.util.Date",
                      "time": temp[attr].value.getTime()
                };
            } else if(temp[attr] instanceof Array && temp[attr].length == 0){
                jsonAttr[attr]  = {
                      "javaClass": "java.util.ArrayList",
                      "list": []
                }
            }
            else {
                jsonAttr[attr] = temp[attr];
            }
        }
        return {
			javaClass : "org.geomajas.layer.feature.Feature",
			id : this.getId(),
			geometry : this.getGeometry(),
		    attributes : {
                "javaClass": "java.util.HashMap",
                "map": jsonAttr
            }
        };
	},

	/**
	 * Accepts a JSON string, and fills in all fields of this feature object.
	 * @param json A recursive hashmap of name-value pairs.
	 */
	fromJSON : function (json) {
		this.setId(json.id);
		this.setClipped(json.clipped);
		this.setPath(json.path);
		this.setCrs(json.crs);

		if (json.attributes) {
			this.setAttributes(json.attributes.map);
		} else {
			this.attributes = new ProxyAttributeMap(this.getLayer().getLayerId(), this.getId(), this.crs);
		}
		
		this.styleId = json.styleId;
		if (json.geometry) {
			var deser = new GeometryDeserializer();
			var geometry = deser.createGeometryFromJSON (json.geometry);
			this.setGeometry(geometry);
		} else {
			this.setGeometry(new ProxyGeometry(this.getLayer().getLayerId(), this.getId(), this.crs));
		}
		if (json.labelPosition) {
			this.setLabelPosition(new Coordinate(json.labelPosition.x, json.labelPosition.y));
		}
		if (json.editable) {
			this.editable = json.editable;
		}
		// make sure selection store stays up-to-date
		var selStore = this.getLayer().getSelectionStore();
		if (selStore.contains(this.getId())) {
			selStore.remove(this.getId());
			selStore.add(this.getId(),this);
		}
	},



	//-------------------------------------------------------------------------
	// ID fetching
	//-------------------------------------------------------------------------

	getId : function () {
		return this.id;
	},

	setId : function (id) {
		this.id = id;
	},

	/**
	 * Returns the feature's own simple ID.
	 */
	getLocalId : function () {
		if (this.id != null) {
			var pos = this.id.lastIndexOf(".");
			if (pos >= 0) {
				return this.id.substring(pos + 1);
			}
			else{
				return this.id;
			}
		}
		return null;
	},

	/**
	 * Returns the DOM id of the selected feature
 	 */
	getSelectionId : function () {
		return this.getLayer().getId() + ".selection." + this.getLocalId();
	},


	//-------------------------------------------------------------------------
	// Attribute handling:
	//-------------------------------------------------------------------------

	/**
	 * Get the value the attribute corresponding to the given attribute name.
	 * In case of an association, the return object can be quite complex.
	 * @param name The name of the attribute.
	 * @return Returns the primitive value or null.
	 */
	getAttributeValue : function (name) {
		this._checkAttributes();
		return this.attributes[name].value;
	},

	/**
	 * Sets a new value for one of the attributes. In case of an association,
	 * make sure you pass a complex object as value, because this function
	 * tries to set both it's identifier, as it's label value.
	 * @param name The attribute's name.
	 * @param value The value for the attribute. Complex in case of an
	 *              association!
	 */
	setAttributeValue : function (name, value) {
		this._checkAttributes();
		if(this.getLayer()) {
			var attribute = this.getLayer().getFeatureType().getAttributeByName(name);
			if(attribute.isAssociation()) {
				// The attribute may never be null! Only it's value can be null.
				this.attributes[name].value = value;
			}  else {
				this.attributes[name].value = value;
			}
			/**
			if (attribute != null && attribute.isAssociation() && value != null && attribute.getType() == "many-to-one") {
				var association = this.attributes[name];
				if (association == null) { // if no value yet...
					association = {javaClass : attribute.object.name };
				}
				var identifierName = attribute.getAssociationIdentifierName();
				association[identifierName] = value[identifierName];

				var associationName = attribute.getAssociationAttributeName();
				association[associationName] = value[associationName];

				this.attributes[name] = association; // in case association was null...
			} else {
				this.attributes[name] = value;
			}
			*/
		} else {
			this.attributes[name] = value;
		}
	},

	/**
	 * Return this feature's label. It uses the preconfigured label attribute to
	 * do so. If no label is found, an empty string is returned.
	 */
	getLabel : function () {
		if (this.layer) {
			var attrName = this.layer.getLabelStyle().getLabelAttributeName();
			var value = this.getAttributeValue(attrName);
			if (value != null) {
				return value;
			}
		}
		return "";
	},



	//-------------------------------------------------------------------------
	// Getters and setters:
	//-------------------------------------------------------------------------

	getLayer : function () {
		return this.layer;
	},
	
	setLayer : function (layer) {
		this.layer = layer;
	},

	getAttributes : function () {
		this._checkAttributes();
		return this.attributes;
	},

	setAttributes : function (attributes) {
		this.attributes = this._fixDates(attributes);
	},

	getGeometry : function () {
		if (this.geometry instanceof ProxyGeometry) {
			var temp = this.geometry.getValue();
			this.geometry = temp;
		}
		return this.geometry;
	},

	setGeometry : function (geometry) {
		this.geometry = geometry;
	},

	getPath : function () {
		return this.path;
	},

	setPath : function (path) {
		this.path = path;
	},

	getCrs : function () {
		return this.crs;
	},

	setCrs : function (crs) {
		this.crs = crs;
	},

	getStyle : function () {
		return this.getLayer().getStyleById(this.styleId);
	},

	setStyleId : function (styleId) {
		this.styleId = styleId;
	},

	getStyleId : function () {
		return this.styleId;
	},

	getLabelPosition : function () {
		return this.labelPosition;
	},

	setLabelPosition : function (labelPosition) {
		this.labelPosition = labelPosition;
	},

	isSelected : function () {
		if (this.layer && this.layer instanceof VectorLayer) {
			return this.layer.getSelectionStore().contains(this.getId());
		}
		return false;
	},

	isClipped : function () {
		return this.clipped;
	},

	setClipped : function (clipped) {
		this.clipped = clipped;
	},

	isEditable : function () {
		return this.editable;
	},

	setEditable : function (editable) {
		this.editable = editable;
	},

	_simplifyFromJSON : function (json) {
		var result = [];
		for(var key in json){
			if(key != "id" && key !="javaClass"){
				result.push(key+"="+json[key]);
			}
		}
		return "{"+result.join(",")+"}";
	},
	
	_checkAttributes : function () {
		if (this.attributes != null && (this.attributes instanceof ProxyAttributeMap || 
				this.attributes.declaredClass == "ProxyAttributeMap")) {
			var json = this.attributes.getValue();
			this.setAttributes(json);
		}
	},

	_fixDates : function (obj) {
		// Dates aren't parsed by Dojo JSON parser (which uses eval).
		// We recursively replace java dates by javascript dates
		if(obj && (obj instanceof Object || obj instanceof Array)){
			for (var i in obj) {
				var arrVal = obj[i];
				if (this._isDate(arrVal)) {
					obj[i] = this._toDate(arrVal);
				} else if(arrVal){
					obj[i] = this._fixDates(arrVal);
				}
			}
		}
		return obj;
	},

	_toDate : function(obj) {
		return new Date(obj.time);
	},

	_isDate :  function(obj) {
	   return (obj != null && obj.javaClass && (obj.javaClass == "java.sql.Timestamp" || obj.javaClass == "java.util.Date"));
	}

});
