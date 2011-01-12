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

dojo.provide("geomajas.widget.TextBalloon");
dojo.require("dijit._Widget");
dojo.require("dijit._Templated");

dojo.require("geomajas.spatial.Coordinate");

dojo.declare("geomajas.widget.TextBalloon",[dijit._Widget, dijit._Templated],{

	templatePath: dojo.moduleUrl("geomajas.widget", "html/TextBalloon.html"),

	position : null,
	opacity:   null,	
	text : "",

	postCreate : function () {
		this.handle = dojo.connect(this.domNode, "onclick", dojo.hitch(this, "_onClick"));
	},

	destroy : function (/*Boolean*/ finalize) {
		log.error ("TextBalloon.destroy");
		dojo.disconnect(this.handle);
		this.inherited(arguments);
	},

	setPosition : function (position) {
		this.position = position;
		this.domNode.style.left = position.getX() + "px";
		this.domNode.style.top = position.getY() + "px";
	},

	getPosition : function () {
		return this.position;
	},

	/**
	 * Set a new text in the balloon.
	 */
	setText : function (innerHTML) {
		this.text = innerHTML;
		var container = dojo.byId(this.id+":container");
		if (container != null) {
			container.innerHTML = innerHTML;
		}
	},

	setOpacity : function(opacityValue) { 
		this.opacity = opacityValue;
		var container = dojo.byId(this.id+":background");
		if (container != null) {
			container.style.opacity = ""+opacityValue; /* convert opacity value into a string */
			container.style.filter = "alpha(opacity="+opacityValue+")";
		}		
	},
	
	getOpacity : function () {
		return this.opacity;
	},	
	/**
	 * Render the widget.
	 */
	render : function (node) {
		if (node) {
			node.appendChild(this.domNode);
		} else {
			document.body.appendChild(this.domNode);
		}
		if (this.position != null) {
			this.domNode.style.left = this.position.getX() + "px";
			this.domNode.style.top = this.position.getY() + "px";
		}
		if (this.opacity != null) {
			var container = dojo.byId(this.id+":background");
			if (container != null) {
				container.style.opacity = ""+this.opacity; /* convert opacity value into a string */
			}		
		}
		var container = dojo.byId(this.id+":container");
		if (container != null) {
			container.innerHTML = this.text;
		}
	},

	/**
	 * Returns the textballoon's width in pixels.
	 */
	getWidth : function () {
		return this.domNode.clientWidth;
	},

	/**
	 * Returns the textballoon's height in pixels.
	 */
	getHeight : function () {
		return this.domNode.clientHeight;
	},

	/**
	 * @private
	 */
	_onClick : function (event) {
		this.destroy();
	}
});