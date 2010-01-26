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

dojo.provide("geomajas.gfx.paintables.Line");
dojo.require("geomajas.gfx.PainterVisitable");

dojo.declare("Line", PainterVisitable, {

	/**
	 * @class An object that is supposed to represent a Line. This
	 * object is drawn by the MeasureDistanceController.
	 * @author Pieter De Graef
	 * 
	 * @constructor
	 */
	constructor : function (id) {
		/** A unique identifier. */
		this.id = id;

		/** The geometry object representing the Line. */
		this.geometry = null;

		/** A style object that determines the look. */
		this.style = null;
	},
	
	/**
	 * Everything that can be drawn on the map, must be accessible by a
	 * PainterVisitor!
	 * @param visitor A PainterVisitor object. Comes from a MapWidget.
	 */
	accept : function (/*PainterVisitor*/visitor, /*Bbox*/ bbox, recursive) {
		visitor.visit(this);
	},
	
	// Getters and setters:

	getId : function () {
		return this.id;
	},
	
	setId : function (id) {
		this.id = id;
	},
	
	getGeometry : function () {
		return this.geom;
	},
	
	setGeometry : function (geom) {
		this.geom = geom;
	},
	
	getStyle : function () {
		return this.style;
	},
	
	setStyle : function (style) {
		this.style = style;
	}

});
