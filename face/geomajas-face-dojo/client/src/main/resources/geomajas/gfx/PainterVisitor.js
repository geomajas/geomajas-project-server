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

dojo.provide("geomajas.gfx.PainterVisitor");
dojo.require("dojox.collections.Dictionary");
dojo.require("dojox.collections.ArrayList");

dojo.declare("PainterVisitor", null, {

	/**
	 * @class The visitor for painter.
	 * @author Pieter De Graef & Jan De Moerloose
	 *
	 * @constructor
	 * @param graphicsContext Implementation of the GraphicsContext interface.
	 * @param renderTopic Name of the rendering topic we're publishing on.
	 */
	constructor : function (graphicsContext) {
		this.graphicsContext = graphicsContext;
		this.painters = new dojox.collections.Dictionary();		
	},

	/**
	 * Register a new painter to this visitor.
	 * @param clazz Each painter is designed to paint a specific type ('clazz')
	 *              of object.
	 * @param painter The painter itself.
	 */
	registerPainter : function (clazz, painter) {
		if (this.painters.contains(clazz)) {
			var array = this.painters.item(clazz);
			array.add (painter);
			this.painters.add (clazz, array);
		} else {
			var array = new dojox.collections.ArrayList();
			array.add (painter);
			this.painters.add (clazz, array);
		}
	},

	/**
	 * Unregister an existing painter from this visitor.
	 * @param clazz Each painter is designed to paint a specific type ('clazz')
	 *              of object.
	 * @param painter The painter itself.
	 */
	unregisterPainter : function (/*Class*/clazz, /*Painter*/painter) {
		if (this.painters.contains(clazz)) {
			var array = this.painters.item(clazz);
			if (array.count == 1) {
				this.painters.remove (clazz);
			} else {
				array.remove (painter);
				this.painters.add (clazz, array);
			}
		}
	},

	/**
	 * Unregisters all painter at once.
	 */
	unregisterAllPainters : function () {
		if (this.painters != null) {
			this.painters.clear();
		}
	},

	visit : function (object) {
		var keys = this.painters.getKeyList();
		for (var i=0; i<keys.length; i++) {
			if (object.declaredClass == keys[i]) {
				var array = this.painters.item (keys[i]);
				for (var j=0; j<array.count; j++) {
					array.item(j).paint (object, this.graphicsContext);
				}
			}
		}
	},

	remove : function (object) {
		var keys = this.painters.getKeyList();
		for (var i=0; i<keys.length; i++) {
			if (object.declaredClass == keys[i]) {
				var array = this.painters.item (keys[i]);
				for (var j=0; j<array.count; j++) {
					array.item(j).deleteShape (object, this.graphicsContext);
				}
			}
		}
	},

	getPaintersForObject : function (object) {
		var keys = this.painters.getKeyList();
		for (var i=0; i<keys.length; i++) {
			if (object.declaredClass == keys[i]) {
				return this.painters.item (keys[i]);
			}
		}
		// empty array
		return new dojox.collections.ArrayList();
	}
});