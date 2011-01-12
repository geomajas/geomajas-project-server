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