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

dojo.provide("geomajas.widget.attributes.CompositionFloater");
dojo.require("dijit._Widget");
dojo.require("dijit._Templated");
dojo.require("dijit.TitlePane");

dojo.declare("geomajas.widget.attributes.CompositionFloater", dijit.form.Button, {

	atDef : null, 
	feature : null,
	value : null,

	connectionPoint : null,
	floaterId : null,
	disableEditor : false,

	postMixInProperties : function () {
		this.iconClass = "showTableIcon";
		this.label = "Open in a separate table";
		this.floaterId = this.id + ":floater";
		this.inherited(arguments);
	},

	postCreate : function () {
		this.inherited(arguments);
		
		if (this.atDef == null || this.feature == null) {
			return;
		}
	},

	onClick : function () {
		if (dijit.byId(this.floaterId) != null) {
			dijit.byId(this.floaterId).destroy();
		}

		var floater = new geomajas.widget.FloatingPane({
			id:this.floaterId,
			title: this.atDef.getLabel() + " - " + this.feature.getLabel(),
			dockable: false,
			maxable: false,
			closable: true,
			resizable: true
		},null);

		floater.setContent("<div id=\""+this.id+":composition\" dojoType=\"geomajas.widget.attributes.CompositionEditor\" style=\"overflow:hidden; margin:0px; padding:0px; border:0px;\"></div>");
		floater.startup();
		floater.domNode.style.overflow = "hidden";

		var div = dojo.body();
		if (geomajasConfig.connectionPoint) {
			var div = dojo.byId(geomajasConfig.connectionPoint);
		}
		div.appendChild (floater.domNode);

		var compositionEditor = dijit.byId(this.id + ":composition");
		compositionEditor.atDef = this.atDef;
		compositionEditor.feature = this.feature;
		compositionEditor.value = this.value;
		compositionEditor.setDisabled(this.disableEditor);

		floater.show();
		compositionEditor._createTable();
		floater.resize({ w:350, h:300, l:40, t:40 });
		floater.bringToTop();
	},

	destroy : function (finalize) {
		if (dijit.byId(this.floaterId) != null) {
			dijit.byId(this.floaterId).close();
		}
		this.inherited(arguments);
	},
	
	setDisabled : function (disabled) {
		this.disableEditor = disabled;
	}
});