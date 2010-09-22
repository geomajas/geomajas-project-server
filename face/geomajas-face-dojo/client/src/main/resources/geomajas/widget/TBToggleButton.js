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

dojo.provide("geomajas.widget.TBToggleButton");
dojo.require("dijit.form.Button");

dojo.declare("geomajas.widget.TBToggleButton", dijit.form.Button, {

	tool : null,
	toolbar : null,
	checked : false,

	baseClass: "dijitToggleButton",

	focusHandle : null,

	init : function (tool, toolbar) {
		this.tool = tool;
		this.toolbar = toolbar;
		this.focusHandle = dojo.subscribe("focusNode", this, "onFocusNode");
	},

	destroy : function (finalize) {
		dojo.unsubscribe(this.focusHandle);
		this.inherited(arguments);
	},

	getTool : function () {
		return this.tool;
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
	},

	onClick: function(/*Event*/ evt){
		this.setChecked(!this.checked);
	},

	setChecked: function(/*Boolean*/ checked){
		this.checked = checked;
		this._setStateClass();
		this.onChange(checked);

		if (this.tool != null) {
			if (checked) {
				this.toolbar.onSelect(this.tool.getId());
				this.tool.onSelect(null);
			} else {
				this.toolbar.onDeSelect(this.tool.getId());
				this.tool.onDeSelect(null);
			}
		}
	}
});