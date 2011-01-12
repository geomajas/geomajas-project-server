dojo.provide("geomajas.config.LabelStyleInfo");
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
