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