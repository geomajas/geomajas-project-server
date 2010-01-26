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

dojo.provide("geomajas.util.System");
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