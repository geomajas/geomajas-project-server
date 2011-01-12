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
