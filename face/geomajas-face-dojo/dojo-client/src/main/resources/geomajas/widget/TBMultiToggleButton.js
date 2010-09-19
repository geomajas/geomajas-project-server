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