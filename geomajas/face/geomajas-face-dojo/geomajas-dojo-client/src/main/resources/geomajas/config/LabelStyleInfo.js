dojo.provide("geomajas.config.LabelStyleInfo");
/*
 * This file is part of Geomajas, a component framework for building rich
 * Internet applications (RIA) with sophisticated capabilities for the display,
 * analysis and management of geographic information. It is a building block
 * that allows developers to add maps and other geographic data capabilities to
 * their web applications.
 * 
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
dojo.declare("LabelStyleInfo", null, {

	/**
	 * @class Configuration object that defines label style for a layer object.
	 * @author Jan De Moerloose
	 * 
	 * @constructor
	 * @param labelAttributeName
	 *            The name of the label attribute.
	 * @param fontStyle
	 *            The style of the label's font.
	 * @param backgroundStyle
	 *            The style of the label's background.
	 */
	constructor : function(labelAttributeName, fontStyle, backgroundStyle) {
		/** @private */
		this.javaClass = "org.geomajas.configuration.LabelStyleInfo";

		/** The name of the label attribute */
		this.labelAttributeName = labelAttributeName;

		/** The style of the label's font. */
		this.fontStyle = fontStyle;

		/** The style of the label's background. */
		this.backgroundStyle = backgroundStyle;

	},

	// Serialization methods:

	toJSON : function() {
		var json = {
			javaClass : this.javaClass,
			labelAttributeName : this.labelAttributeName,
			fontStyle : this.fontStyle.toJSON(),
			backgroundStyle : this.backgroundStyle.toJSON()
		};
		return json;
	},

	fromJSON : function(json) {
		this.labelAttributeName = json.labelAttributeName;
		this.fontStyle = new FeatureStyleInfo();
		this.fontStyle.fromJSON(json.fontStyle);
		this.backgroundStyle = new FeatureStyleInfo();
		this.backgroundStyle.fromJSON(json.backgroundStyle);
	},

	// Getters and setters:

	getLabelAttributeName : function() {
		return this.labelAttributeName;
	},


	setLabelAttributeName : function(labelAttributeName) {
		this.labelAttributeName = labelAttributeName;
	},

	getbackgroundStyle : function() {
		return this.backgroundStyle.getStyle();
	},
	
	getFontStyle : function() {
		return this.fontStyle.getStyle();
	}

});
