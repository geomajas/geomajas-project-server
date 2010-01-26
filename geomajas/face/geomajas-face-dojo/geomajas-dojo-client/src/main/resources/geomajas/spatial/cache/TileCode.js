dojo.provide("geomajas.spatial.cache.TileCode");
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
dojo.declare("TileCode", SpatialCode, {

	/**
	 * @fileoverview Definition of a Tile code.
	 * @class Definition of a Tile code.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends SpatialCode
	 * @param tileLevel The depth of the tile.
	 * @param x The horizontal tile number. 
	 * @param y The vertical tile number. 
	 */
	constructor : function (tileLevel, x, y) {
		this.tileLevel = tileLevel;
		this.x = x;
		this.y = y;
	},



	//-------------------------------------------------------------------------
	// SpatialCode implementation:
	//-------------------------------------------------------------------------

	/**
	 * String representation of a tilecode. Often used as key in dictionaries.
	 */
	toString : function () {
		return this.tileLevel + "-" + this.x + "-" + this.y;
	},

	/**
	 * Standard equals function. No surprises here.
	 */
	equals : function (tileCode) {
		return (
			this.tileLevel == tileCode.tileLevel && 
			this.x == tileCode.x && 
			this.y == tileCode.y
		);
	},

	/**
	 * Parses a JSON object.
	 */
	fromJSON : function (json) {
		this.tileLevel = json.tileLevel;
		this.x = json.x;
		this.y = json.y;
	},

	toJSON : function () {
		return {
			javaClass : "org.geomajas.rendering.tile.TileCode",
			tileLevel : this.tileLevel,
			x : this.x,
			y : this.y
		};
	},


	//-------------------------------------------------------------------------
	// Getters and setters.
	//-------------------------------------------------------------------------

	getTileLevel : function () {
		return this.tileLevel;
	},

	setTileLevel : function (tileLevel) {
		this.tileLevel = tileLevel;
	},

	getX : function () {
		return this.x;
	},

	setX : function (x) {
		this.x = x;
	},

	getY : function () {
		return this.y;
	},

	setY : function (y) {
		this.y = y;
	}
	
}); 
