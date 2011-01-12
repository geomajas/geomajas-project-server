dojo.provide("geomajas.spatial.cache.TileCode");
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
			javaClass : "org.geomajas.layer.tile.TileCode",
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
