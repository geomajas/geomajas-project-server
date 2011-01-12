dojo.provide("geomajas.util.System");
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
dojo.declare("System", null, {

	/**
	 * @fileoverview General system property handler.
	 * @class This class can detect certain technologies and properties
	 *        available in the user's browser.
	 * 
	 * @author Pieter De Graef
	 * @constructor
	 */
	constructor : function () {
	},

	hasSVGSupport : function () {
		result = false;
		if ((navigator.userAgent.indexOf('MSIE') != -1) && (navigator.userAgent.indexOf('Win') != -1)) {
			document.writeln('<script language="VBscript">'); 
			document.writeln('\n on error resume next \n result = IsObject(CreateObject("Adobe.SVGCtl"))');
			document.writeln('</scr' + 'ipt>');
		} else {
			result = document.implementation.hasFeature("http://www.w3.org/TR/SVG11/feature#BasicStructure", "1.1");
		}
		return result;
	},
	
	getResolution : function () {
		return screen.width + "x" + screen.height;
	}
});