dojo.provide("geomajas.widget.attributes.CompositionEditor");
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
dojo.require("dijit._Widget");
dojo.require("dijit._Templated");
dojo.require("dijit.TitlePane");

dojo.declare("geomajas.widget.attributes.CompositionEditor", [dijit.layout.LayoutContainer, dijit._Templated], {

	templatePath : dojo.moduleUrl("geomajas.widget", "attributes/html/CompositionEditor.html"),
	templateCssPath : dojo.moduleUrl("geomajas.widget", "attributes/html/CompositionEditor.css"),

	widgetsInTemplate : true,

	grid : null,

	postCreate : function () {
		this.inherited(arguments);

		var addButton = dijit.byId(this.id+":add");
		if (addButton) {
			addButton.onClick = dojo.hitch(this, "addRow");
		}
		var removeButton = dijit.byId(this.id+":remove");
		if (removeButton) {
			removeButton.onClick = dojo.hitch(this, "removeRows");
		}
		var resetButton = dijit.byId(this.id+":reset");
		if (resetButton) {
			resetButton.onClick = dojo.hitch(this, "reset");
		}

		this._createTable();
	},

	/**
	 * Grid in a floater gives problems with height, so we try and compensate.
	 */
	resize : function (size) {
		var cs = dojo.getComputedStyle(this.domNode);
		var toolbar = dijit.byId(this.id + ":toolbar");
		
		if (dojo.isIE && (cs.height == "auto" || cs.height == "100%")) {
			var height = 100;
			if (size != null && size.h > 0) {
				height = size.h;
			}
			var tbHeight = getCompleteNodeHeight(toolbar.domNode);
			this.grid.domNode.style.height = (height-tbHeight) + "px";
		} else {
			var height = parseInt(cs.height);
			var tbHeight = getCompleteNodeHeight(toolbar.domNode);
			this.grid.domNode.style.height = (height-tbHeight) + "px";
		}
		this.grid.render();
	},

	destroy : function (finalize) {
		this.destroyDescendants();
		this.uninitialize();
		this.destroyRendering(finalize);
		dijit.registry.remove(this.id);
	},

	/**
	 * Return the value of this widget as an array of dictionaries. Each entry
	 * in the array has name-value pairs as represented in the grid.
	 */
	getValue : function () {
		var value = [];
		if (this.grid != null && this.grid.model != null) {
			for (var i=0; i<this.grid.model.data.length; i++) {
				var dataRow = this.grid.model.data[i];
				var keys = this._getAttributesFromDefinition();
				var row = { id: dataRow[0], javaClass:this.atDef.object.name };
				for (var j=0; j<keys.length; j++){
					var name = keys[j].name;
					row[name] = dataRow[j+1];
				}
				value.push(row);
			}
		}
		return value;
	},

	/**
	 * Add a new empty row to the grid.
	 */
	addRow : function () {
		if (this.grid != null) {
			var keys = this._getAttributesFromDefinition();
			if (keys != null) {
				var row = [];
				row.push("");
				for (var i=0; i<keys.length; i++) {
					if (keys[i].type.value == "string" || keys[i].type.value == "url" || keys[i].type.value == "imgurl") {
						row[i+1] = "?";
					} else if (keys[i].type.value == "integer" || keys[i].type.value == "long" ||
							keys[i].type.value == "short" || keys[i].type.value == "float" || keys[i].type.value == "double" || keys[i].type.value == "currency") {
						row[i+1] = 0;
					} else if (keys[i].type.value == "date") {
						row[i+1] = new Date();
					} else if (keys[i].type.value == "boolean") {
						row[i+1] = false;
					}
				}
				this.grid.addRow(row);
			}
		}
	},

	/**
	 * Remove the currently selected rows from the grid.
	 */
	removeRows : function () {
		if (this.grid != null) {
			this.grid.removeSelectedRows();
		}
	},

	/**
	 * Reset the grid to it's original values.
	 */
	reset : function () {
		if (this.grid != null) {
			this.grid.edit.cancel();

			var keys = this._getAttributesFromDefinition();
			var data = this._createGridDataFromValue(keys);
			var model = new dojox.grid.data.Table(null, data);
			this.grid.setModel(model);
			this.grid.render();
		}
	},

	isValid : function () {
		return true;
	},

	/**
	 * Disable this widget. Make sure it's contents cannot be adjusted.
	 */
	setDisabled : function (disabled) {
		var addButton = dijit.byId(this.id+":add");
		if (addButton) {
			addButton.setAttribute("disabled", true);
		}
		var removeButton = dijit.byId(this.id+":remove");
		if (removeButton) {
			removeButton.setAttribute("disabled", true);
		}
		var resetButton = dijit.byId(this.id+":reset");
		if (resetButton) {
			resetButton.setAttribute("disabled", true);
		}
		
		this.grid.canEdit = function(inCell, inRowIndex) {return false;}
	},

	/**
	 * Not being used atm...
	 */
	onChange : function () {
	},

	addAction : function (action) {
		var toolbar = dijit.byId(this.id + ":toolbar");
		var button = new geomajas.widget.TBButton({name:action.getId(), id:action.getId(), iconClass:action.getImage(), label:action.getTooltip(), showLabel:false}, document.createElement('div'));
		button.init(action);
		toolbar.addChild(button);
	},



	//-------------------------------------------------------------------------
	// Private functions.
	//-------------------------------------------------------------------------

	/**
	 * @private
	 */
	_createTable : function () {
		this.grid = dijit.byId(this.id+":grid");
		if (this.atDef != null && this.atDef.getType() == "one-to-many" && this.grid != null) {
			var thead = [];
			var keys = this._getAttributesFromDefinition();
			if (keys == null) {
				log.error ("Error creating CompositionEditor widget.");
				return;
			}

			// Creating the GRID structure:
			var columnWidth = parseInt(100 / keys.length+1) + "%";
			thead.push({ name:"id" });
			for (var i=0; i<keys.length; i++) {
				thead.push(this._createGridColumnDefinition(keys[i], columnWidth));
			}

			var view = { 
				defaultCell: { editor: dojox.grid.editors.Input, styles: 'text-align: right;' },
				cells: [ thead ]
			};
			this.grid.setStructure([ view ]);

			// Setting the GRID data:
			if (this.value == null) {
				this.grid.setModel(null);
			} else {
				var data = this._createGridDataFromValue(keys);
				var model = new dojox.grid.data.Table(null, data);
				this.grid.setModel(model);
			}
			this.resize();
		}
	},

	/**
	 * @private
	 * Retrieves the list of attributes of the one-to-many objects.
	 */
	_getAttributesFromDefinition : function () {
		if (this.atDef != null) {
			return this.atDef.object.attributes.list;
		}
		return null;
	},

	/**
	 * @private
	 * Create the GRID definition for a column, based on the given attribute definition.
	 */
	_createGridColumnDefinition : function (atDef, columnWidth) {
		var column = { name:atDef.label, width:columnWidth };
		var formatter = new AttributeFormatterFactory();

		if (atDef.type == "string" || atDef.type == "url" || atDef.type == "imgurl") {
			column["editor"] = dojox.grid.editors.Dijit;
			column["editorClass"] = "dijit.form.TextBox";
		} else if (atDef.type == "integer" || atDef.type == "long" || atDef.type == "short") {
			column["editor"] = dojox.grid.editors.Dijit;
			column["editorClass"] = "dijit.form.NumberTextBox";
			column["formatter"] = function(inDatum){
				return isNaN(inDatum) ? '0' : parseInt(inDatum);
			}
		} else if (atDef.type == "float" || atDef.type == "double") {
			column["editor"] = dojox.grid.editors.Dijit;
			column["editorClass"] = "dijit.form.NumberTextBox";
			column["formatter"] = function(inDatum){
				return isNaN(inDatum) ? '0' : parseFloat(inDatum).toFixed(2);
			}
		} else if (atDef.type == "currency") {
			column["editor"] = dojox.grid.editors.Dijit;
			column["editorClass"] = "dijit.form.CurrencyTextBox";
			column["formatter"] = function(inDatum){
				return isNaN(inDatum) ? '0' : parseFloat(inDatum).toFixed(2);
			}
		} else if (atDef.type == "date") {
			column["editor"] = dojox.grid.editors.Dijit;
			column["editorClass"] = "dijit.form.DateTextBox";
			column["formatter"] = function(inDatum){
				return dojo.date.locale.format(new Date(inDatum), this.constraint);
			};
		} else if (atDef.type == "boolean") {
			column["editor"] = dojox.grid.editors.CheckBox;
		}
		column["formatter"] = formatter.create(atDef);

		return column;
	},

	/**
	 * @private
	 * Create a 2-dimensional array of grid-data from the feature's value.
	 */
	_createGridDataFromValue : function (keys) {
		data = [];
		for (var i=0; i<this.value.list.length; i++) {
			var row = [];
			var val = this.value.list[i];
			row.push(val.id.value); // First column = ID
			for (var j=0; j<keys.length; j++) {
				var key = keys[j].name;
				row.push(val.attributes.map[key].value);
			}
			data.push(row);
		}
		return data;
	}
});
