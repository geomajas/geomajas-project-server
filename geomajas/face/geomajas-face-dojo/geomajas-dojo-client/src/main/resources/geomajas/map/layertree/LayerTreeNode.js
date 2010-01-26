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

dojo.provide("geomajas.map.layertree.LayerTreeNode");
dojo.require("dojox.collections.ArrayList");

dojo.declare("LayerTreeNode", PainterVisitable, {

	/**
	 * @fileoverview Basic node implementation for a MapModel's layer-tree structure.
	 * @class The layers of a MapModel object are ordered as a tree, with
	 * internal nodes represented by this class, and leafs represented by one
	 * of the layer classes. In other words, the layers are also nodes in the
	 * layer tree, and therefore extend this class.
	 * 
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends PainterVisitable
	 * @param id Unique identifier for the node.
	 * @param label A label by which this node can be represented (for example
	 *              in the LayerTree widget).
	 */
	constructor : function (id, label, expanded) {
		this.id = id;
		this.label = label;
		this.children = new dojox.collections.ArrayList();
		this.mapModel = null;	
		this.selected = false;
		this.expanded = expanded;
		if (this.expanded == null) {
			this.expanded = false;
		}
		if (!id) {
			this.id = "LayerTreeRootNode";
		}
	},

	/**
	 * PainterVisitable implementation. Needed to be able to draw layers
	 * through the tree.
	 */
	accept : function (/*PainterVisitor*/visitor, /*Bbox*/ bbox, recursive) {
		visitor.visit (this);
		if(recursive) {
			for (var i=0; i<this.children.count; i++) {
				this.children.item(i).accept(visitor, bbox, true);
			}
		}
	},

	/**
	 * Add a new child to this node.
	 * @param layerTreeNode Can be a LayerTreeNode like this class, but can
	 *                      also be a layer (VectorLayer, RasterLayer).
	 */
	addChild : function (layerTreeNode) {
		this.children.add (layerTreeNode);
	},

	// Getters and setters:

	getId : function () {
		return this.id;
	},

	setId : function (/*String*/id) {
		this.id = id;
	},
	
	getMapModel : function () {
		return this.mapModel;
	},

	setMapModel : function (/*MapModel*/mapModel) {
		this.mapModel = mapModel;
	},

	getLabel : function () {
		return this.label;
	},

	setLabel : function (/*String*/label) {
		this.label = label;
	},

	isSelected : function () {
		return this.selected;
	},

	setSelected : function (/*Boolean*/selected) {
		this.selected = selected;
	},

	isExpanded : function () {
		return this.expanded;
	},

	setExpanded : function (/*Boolean*/expanded) {
		this.expanded = expanded;
	},

	isVisible : function () {
		return this.visible;
	},

	setVisible : function (visible) {
		log.debug("layer: setVisible: " + visible);
		this.visible = visible;
	},

	getChildren : function () {
		return this.children;
	},

	setChildren : function (children) {
		this.children = children;
	},

	toString : function() {
		return "node["+this.id+","+this.label+"]";
	}
});
