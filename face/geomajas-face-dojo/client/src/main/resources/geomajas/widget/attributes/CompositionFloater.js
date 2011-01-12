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