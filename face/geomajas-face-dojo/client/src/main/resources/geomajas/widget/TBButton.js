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