dojo.provide("geomajas.config.NamedStyleInfo");
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
dojo.declare("NamedStyleInfo", null, {

	/**
	 * @class Configuration object that defines styles for a layer object.
	 * @author Pieter De Graef
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 * @param id Unique identifier.
	 * @param label The style's label.
	 * @param style A Style object.
	 */
	constructor : function (id, name, featureStyles, labelStyle) {
		/** @private */
		this.javaClass = "org.geomajas.configuration.NamedStyleInfo";
				
		/** The style's name. */
		this.name = name;
		
		/** The feature style list. */
		this.featureStyles = featureStyles;

		/** The label style. */
		this.labelStyle = labelStyle;
	},
	
	// Serialization methods:
	
	toJSON : function () {
		// prepare list for json
		var fs = this.featureStyles.toArray();	
		for(var i = 0; i < fs.length; i++){
			fs[i] = fs[i].toJSON();
		}
		var json = {
			javaClass : this.javaClass,
			name : this.name,
			featureStyles : 
				{
					javaClass : "java.util.List",
					list : fs
				},
			labelStyle : this.labelStyle.toJSON()
		};
		return json;
	},
	
	fromJSON : function (json) {
		this.name = json.name;
		this.labelStyle = new LabelStyleInfo();
		this.labelStyle.fromJSON(json.labelStyle);
		this.featureStyles = new dojox.collections.ArrayList();
		var tmp = json.featureStyles.list;
		for(var i = 0; i < tmp.length; i++){
			var style = new FeatureStyleInfo();
			style.fromJSON(tmp[i]);
			this.featureStyles.add(style);
		}
	},
	
	addStyle : function (/*FeatureStyleInfo*/ style) {
		this.featureStyles.add(style);
	},

	replaceStyle : function (/*FeatureStyleInfo*/ style) {
		var styleArray = this.featureStyles.toArray();
		for(var i = 0; i < styleArray.length; i++){
			if(styleArray[i].getName() == style.getName()){
				this.featureStyles.removeAt(i);
				this.featureStyles.insert(i,style);
				return;
			}
		}
		this.featureStyles.insert(0,style);
	},
	
	getStyleById : function (styleId) {
		for(var i = 0; i < this.featureStyles.count; i++){
			if(this.featureStyles.item(i).getStyleId() == styleId){
				return this.featureStyles.item(i);
			}
		}
		return null;
	},
	
	getStyles : function () {
		return this.featureStyles;
	},
	
	getLabelStyle : function () {
		return this.labelStyle;
	},

	setLabelStyle : function (labelStyle) {
		this.labelStyle = labelStyle;
	}
	
	
});
