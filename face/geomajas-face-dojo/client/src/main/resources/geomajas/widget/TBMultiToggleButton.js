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
dojo.provide("geomajas.widget.TBMultiToggleButton");

dojo.require("geomajas.widget.TBToggleButton");

dojo.declare("geomajas.widget.TBMultiToggleButton", [ geomajas.widget.TBToggleButton ], {
	
	postCreate: function() {
		this.inherited(arguments);
		//dojo.addClass(this.iconNode, "multiToggleButton");
		// little js/css hack to override dijitButton border settings
		this.focusNode.setAttribute("style","border-color:#C0C0C0 #9B9B9B #9B9B9B #C0C0C0; border-style: dotted; border-width:1px;");
	},
	setChecked : function(/* Boolean */checked) {
		this.checked = checked;
		this._setStateClass();
		this.onChange(checked);

		if (this.tool != null) {
			if (checked) {
				this.tool.onSelect(null);
			} else {
				this.tool.onDeSelect(null);
			}
		}
	}
});