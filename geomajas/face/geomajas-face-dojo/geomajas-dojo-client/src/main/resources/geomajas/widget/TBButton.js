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

dojo.provide("geomajas.widget.TBButton");
dojo.require("dijit.form.Button");

dojo.declare("geomajas.widget.TBButton", dijit.form.Button, {

	action : null,

	baseClass: "dijitButton",
	
	clickHandle : null,
	focusHandle : null,

	init : function (action) {
		this.action = action;

		this.clickHandle = dojo.connect (this, "onClick", action, "actionPerformed");
		this.focusHandle = dojo.subscribe("focusNode", this, "onFocusNode");
	},

	destroy : function (finalize) {
		dojo.disconnect(this.clickHandle);
		dojo.unsubscribe(this.focusHandle);
		this.inherited(arguments);
	},

	getAction : function () {
		return this.action;
	},

	/**
	 * Function subscribed to the focus topic. It purpose is to make sure that
	 * this LayerTreeButton cannot be focused.
	 * @param node The node being focused. If that node equals this widget's
	 *             focusNode, execute a blur, to defocus.
	 */
	onFocusNode : function(node){
		// Remove the focus:
		if(node == this.focusNode) {
			node.blur();
		}
	}
});