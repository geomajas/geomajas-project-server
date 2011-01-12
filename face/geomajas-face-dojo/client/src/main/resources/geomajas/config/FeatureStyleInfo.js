dojo.provide("geomajas.config.FeatureStyleInfo");
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
dojo.declare("FeatureStyleInfo", null, {

	/**
	 * @class Configuration object that defines styles for a layer object.
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 * @param id Unique identifier.
	 * @param label The style's label.
	 * @param style A Style object.
	 */
	constructor : function (index, name, formula, style) {
		/** @private */
		this.javaClass = "org.geomajas.configuration.FeatureStyleInfo";
		
		/** Index in the list. */
		this.index = index;
		
		/** The style's label. */
		this.name = name;
		
		/** The style's label. */
		this.formula = formula;

		/** A Style object. */
		this.style = style;
		
		this.styleId = null;
	},
	
	// Serialization methods:
	
	toJSON : function () {
		var json = {
			javaClass : this.javaClass,
			index : this.index,
			name : this.name,
			formula : this.formula,
			fillColor : this.style.fillColor,
			fillOpacity : this.style.fillOpacity,
			strokeColor : this.style.strokeColor,
			strokeOpacity : this.style.strokeOpacity,
			strokeWidth : this.style.strokeWidth,
			dashArray : this.style.dashArray,
			symbol : this.style.symbol,
			styleId : this.styleId
		};
		return json;
	},
	
	fromJSON : function (json) {
		this.index = json.index;
		this.name = json.name;
		this.formula = json.formula;
		this.styleId = json.styleId;
		this.style = new ShapeStyle();
		// only non-null elements !
		this.style.copyFrom(json);
	},
	
	// Getters and setters:
	
	getIndex : function () {
		return this.index;
	},
	
	setIndex : function (index) {
		this.index = index;
	},

	getName : function () {
		return this.name;
	},
	
	setName : function (name) {
		this.name = name;
	},

	getFormula : function () {
		return this.formula;
	},
	
	setFormula : function (formula) {
		this.formula = formula;
	},

	getStyleId : function () {
		return this.styleId;
	},
	
	setStyleId : function (styleId) {
		this.styleId = styleId;
	},

	getStyle : function () {
		return this.style;
	},
	
	setStyle : function (style) {
		this.style = style;
	}
});
