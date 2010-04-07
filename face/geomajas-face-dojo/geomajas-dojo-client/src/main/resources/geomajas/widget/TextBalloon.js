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