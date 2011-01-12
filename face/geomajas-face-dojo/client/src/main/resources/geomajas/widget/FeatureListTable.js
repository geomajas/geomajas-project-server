dojo.provide("geomajas.widget.FeatureListTable");
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
dojo.require("dijit._Container");
dojo.require("dijit.Dialog");
dojo.require("dojox.grid.Grid");
dojo.require("dojox.grid._data.model");
dojo.require("dojo.parser");

dojo.require("geomajas.widget.FeatureEditDialog");

dojo.declare("geomajas.widget.FeatureListTable", 
		[dijit._Widget, dijit._Templated], {

	widgetsInTemplate : true,
	templatePath : dojo.moduleUrl("geomajas.widget", "html/FeatureListTable.html"),

	useIdInTable : true,
	grid : null,
	layer : null,
	data : null,
	editingTopic : null,
	selectionTopic : null,
	singleSelection : false,
	supportEditing : false,  // Allows editing of a feature, by double clicking on it's row.
	dialog : null,
	mapWidget : null,

	features : null,

	// Handles:
	editTopicHandle : null,
	selectionTopicHandle : null,
	selectHandle : null,
	deselectHandle : null,
	sortHandle : null,
	renderHandle : null,
	executeHandle : null,

	postCreate : function () {
		this.grid = dijit.byId(this.id+":grid");
		if (this.grid == null) {
			log.error ("FeatureListTable : The grid could not be found!");
		} else {
			this.grid.onCellDblClick = dojo.hitch(this, "_onCellDblClick");
			this.grid.selection.multiSelect = !this.singleSelection;
		}
		if (this.mapWidget != null) {
			this.editingTopic = this.mapWidget.getMapModel().getEditingTopic();
			this.editTopicHandle = dojo.subscribe(this.editingTopic, dojo.hitch(this, "_onEditingChange"));
		}
	},

	destroy : function (finalize) {
		this.disableSelection();
		dojo.unsubscribe(this.editTopicHandle);
		dojo.unsubscribe(this.selectionTopicHandle);
		dojo.disconnect(this.selectHandle);
		dojo.disconnect(this.deselectHandle);
		dojo.disconnect(this.sortHandle);
		dojo.disconnect(this.renderHandle);
		dojo.disconnect(this.executeHandle);
		this.mapWidget = null;
		if(this.grid){
			this.grid = null;
		}
		this.inherited(arguments);
	},

	/**
	 * When this widget resizes, simply render the grid again.
	 */
	resize : function (size) {
		if (this.grid) {
			this.grid.domNode.style.height = size.h + "px";
			this.grid.domNode.style.width = size.w + "px";
		}
		this.render();
	},

	/**
	 * Set a new layer on the table. This means creating the head for the
	 * table, using the identifying attributes of the layer's FeatureInfo.
	 *
	 * @param layer Should be a VectorLayer.
	 */
	setLayer : function (layer) {
		if (this.mapWidget != null && this.editingTopic == null) { // Should not be here!!!
			this.editingTopic = this.mapWidget.getMapModel().getEditingTopic();
			if (this.editTopicHandle != null) {
				dojo.unsubscribe(this.editTopicHandle);
			}
			this.editTopicHandle = dojo.subscribe(this.editingTopic, dojo.hitch(this, "_onEditingChange"));
		}
		if (layer instanceof VectorLayer) {
			this.features = new dojox.collections.Dictionary();
			var formatter = new AttributeFormatterFactory();
			this.layer = layer;
			if (this.grid) {
				var attr = this.layer.getFeatureType().getVisibleIdentifyingAttributes(); //dictionary
				var keys = attr.getKeyList();
				var thead = [];
				if (this.useIdInTable) {
					thead.push({ name:"id", width: "50px" });
				} else {
					thead.push({ name:"id", width: "0px", styles: "display:none;" });
				}
				var w = parseInt(100/keys.length) + "%";
				for (var i=0; i<keys.length; i++) {
					var attrDef = attr.item(keys[i]);
					var column = { name:attrDef.getLabel(), width: w, formatter:formatter.create(attrDef), styles: formatter.getStyle(attrDef) };
					thead.push(column);
				}
				
				this.grid.setModel(null);
				this.reset();

				var view = { cells: [ thead ]};
				this.grid.setStructure([ view ]);
			}
		}
	},
	
	getLayer : function () {
		return this.layer;
	},

	/**
	 * Needed for edit-topic listening.
	 */
	setMapWidget : function (mapWidget) {
		if (mapWidget != null) {
			this.mapWidget = mapWidget;
			this.editingTopic = this.mapWidget.id + "/editing";
			if (this.editTopicHandle != null) {
				dojo.unsubscribe(this.editTopicHandle);
			}
			this.editTopicHandle = dojo.subscribe(this.editingTopic, dojo.hitch(this, "_onEditingChange"));
		}	
	},

	/**
	 * Adds a new row to the table in which the given feature is displayed by
	 * it's identifying attributes.
	 *
	 * @param feature A feature object. Must be of the layer that was set
	 *                earlier on this FeatureListTable.
	 */
	addFeature : function (feature) {
		if (this.layer != null && this.layer instanceof VectorLayer) {
			this.features.add(feature.getId(), feature);
			var row = [];
			row.push (feature.getId());
			var attr = this.layer.getFeatureType().getVisibleIdentifyingAttributes();
			var keys = attr.getKeyList();
			for (var i=0; i<keys.length; i++) {
				row.push(feature.getAttributeValue(keys[i]));
			}
			this.data.push(row);
		}
	},

	/**
	 * Render the table. This must be called in order for changes to become
	 * visible.
	 */
	render : function () {
		if (this.grid && this.layer) {
			var model = new dojox.grid.data.Table(null, this.data);
			this.grid.setModel(model);
			this.grid.render();
		}
	},

	/**
	 * Subscribe this widget to the selection topic, so that changes in
	 * selection will become visible.
	 */
	enableSelection : function (selectionTopic) {
		if (this.selectionTopic != null) {
			this.disableSelection();
		}			
		this.selectionTopic = selectionTopic;
		this.selectionTopicHandle = dojo.subscribe(this.selectionTopic, this, "_onSelectionChange");

		this.selectHandle = dojo.connect (this.grid, "onSelected", this, "_onSelect");
		this.deselectHandle = dojo.connect (this.grid, "onDeselected", this, "_onDeselect");

		this.sortHandle = dojo.connect (this.grid, "sort", this, "_requestSelection");
		this.renderHandle = dojo.connect (this, "render", this, "_requestSelection");
	},

	/**
	 * Unsubscribe this widget from the selection topic.
	 */
	disableSelection : function () {
		dojo.unsubscribe(this.selectionTopicHandle);

		dojo.disconnect (this.selectHandle);
		dojo.disconnect (this.deselectHandle);
		dojo.disconnect (this.sortHandle);
		dojo.disconnect (this.renderHandle);

		this.selectionTopic = null;
	},

	/**
	 * Reset the table, making it empty. Note the the head stays!
	 */
	reset : function () {
		if (this.grid) {
			this.data = [];
			this.render();
		}	
	},

	/**
	 * Show only selected features. Filter out the rest (but do not remove them!).
	 */
	showSelectedOnly : function () {
		if (this.selectionTopic && this.grid && this.layer) {
			var filteredData = [];
			for (var i=0; i<this.data.length; i++){
				var row = this.data[i];
				var feature = this.features.item(row[0]);

				if (feature.isSelected()) {
					filteredData.push(this.data[i]);
				}
			}
			var model = new dojox.grid.data.Table(null, filteredData);
			this.grid.setModel(model);
			for (var i=0; i<filteredData.length; i++) {
				this.grid.selection.addToSelection(i);
			}
			this.grid.render();
		}
	},
	
	showAll : function () {
		if (this.grid) {
			this.render();
		}	
	},

	/**
	 * @private
	 */
	_requestSelection : function () {
		if (this.grid && this.layer) {
			this.grid.selection.unselectAll();
			var feature = new Feature();
			feature.setLayer(this.layer);
			feature.setId(this.layer.getId()+".1.1");
			dojo.publish (this.selectionTopic, ["requestSelection", feature]);
		}
	},

	/**
	 * @private
	 */
	_onSelect : function (rowIndex) {
		if (this.grid) {
			var row = this.grid.model.getRow(rowIndex);
			if (row) {
				var feature = this.features.item(row[0]);
				this.stopListening = true;
				dojo.publish (this.selectionTopic, ["select", feature]);
			}
		}
	},

	/**
	 * @private
	 */
	_onDeselect : function (rowIndex) {
		if (this.grid) {
			var row = this.grid.model.getRow(rowIndex);
			if (row) {
				var feature = this.features.item(row[0]);
				this.stopListening = true;
				dojo.publish (this.selectionTopic, ["deselect", feature]);
			}
		}
	},

	/**
	 * @private
	 */
	_onSelectionChange : function (event, feature) {
		if (this.stopListening) {
			this.stopListening = false;
			return;
		}
		if (event == "addElement") {
			var rowIndex = this._searchFeature(feature);
			if (rowIndex != null) {
				this.grid.selection.addToSelection(rowIndex);
				this.grid.render();
			}
		} else if (event == "removeElement") {
			var rowIndex = this._searchFeature(feature);
			if (rowIndex != null) {
				this.grid.selection.deselect(rowIndex);
				this.grid.render();
			}
		}
	},

	/**
	 * @private
	 */
	_searchFeature : function (feature) {
		if (feature != null && feature.getLayer() != null && this.layer != null && this.layer.getId() == feature.getLayer().getId()) {
			var localId = feature.getId();
			var length = this.grid.model.getRowCount();
			for (var i=0; i<length; i++) {
				var row = this.grid.model.getRow(i);
				if (row[0] == localId) {
					return i;
				}
			}
		}
		return null;
	},

	/**
	 * @private
	 */
	_onCellDblClick : function (e) {
		var rowIndex = this.grid.focus.rowIndex;
		var row = this.grid.model.getRow(rowIndex);
		var tmpFeature = this.features.item(row[0]);
		if (tmpFeature == null) {
			return;
		}
		var feature = tmpFeature.clone();

		var floaterId = this.id+":"+feature.getId();
		if (dijit.byId(floaterId) != null) {
			return;
		}

		var height = 65; // Height of the toobar etc
		var attr = this.layer.getFeatureType().getAttributes(); //dictionary
		var keys = attr.getKeyList();
		for (attr in keys) {
			height += 25;
		}
		if (height > 600) {
			height = 600;
		}

		var floater = new geomajas.widget.FloatingPane({
			id:floaterId,
			title: "Feature detail - " + feature.getLabel(),
			dockable: false,
			maxable: false,
			closable: true,
			resizable: false
		},null);

		floater.setContent("<div id=\""+floaterId+":featureEditor\" dojoType=\"geomajas.widget.FeatureDetailEditor\" style=\"width:100%; height: 100%; overflow:hidden;\"></div>");
		floater.domNode.style.overflow = "hidden";
		floater.startup();
		floater.resize({ w:420, h:height, l:20, t:20 });

		var div = dojo.body();
		if (geomajasConfig.connectionPoint) {
			var div = dojo.byId(geomajasConfig.connectionPoint);
		}
		div.appendChild (floater.domNode);

		var editor = dijit.byId(floaterId+":featureEditor");
		editor.setFeature(feature);
		editor.setMapWidget (this.mapWidget);

		floater.show();
		floater.bringToTop();
	},

	/**
	 * @private
	 */
	_updateRow : function (row, feature) {
		var count = 1;
		var attr = this.layer.getFeatureType().getVisibleIdentifyingAttributes();
		var keys = attr.getKeyList();
		for (var i=0; i<keys.length; i++) {
			row[count++] = feature.getAttributeValue(keys[i]);
		}

		this.render();
	},

	/**
	 * @private
	 */
	_deleteRow : function (rowIndex) {
		this.data.splice(rowIndex, 1);
		this.render();
	},

	/**
	 * @private
	 */
	_onEditingChange : function (event, ft) {
		if (event== "commit") {
			// NEW FEATURES ARE OMITTED !!!! This because the table can also be used for search-results. It does not necessarily reflect the MapModel.

			// UPDATES:
			if (ft.getOldFeatures() != null && ft.getNewFeatures() != null && ft.getOldFeatures().length == ft.getNewFeatures().length) {
				for (var i=0; i<ft.getNewFeatures().length; i++) {
					var feature = ft.getNewFeatures()[i];
					feature.setLayer(this.layer);
					var rowIndex = this._searchFeature(feature);
					if (rowIndex != null) {
						this._updateRow(this.data[rowIndex], feature);
					}
					if (this.features.contains(feature.getId())) {
						this.features.remove(feature.getId());
						this.features.add(feature.getId(), feature);
					}
				}
			}

			// DELETES:
			if (ft.getOldFeatures() != null && ft.getNewFeatures() == null) {
				for (var i=0; i<ft.getOldFeatures().length; i++) {
					var feature = ft.getOldFeatures()[i];
					var rowIndex = this._searchFeature(feature);
					if (rowIndex != null) {
						this._deleteRow(rowIndex);
					}
					if (this.features.contains(feature.getId())) {
						this.features.remove(feature.getId());
					}
				}				
			}

			// MERGE:
			if (ft.getOldFeatures() != null && ft.getNewFeatures() != null && ft.getOldFeatures().length > ft.getNewFeatures().length) {
				for (var i=0; i<ft.getOldFeatures().length; i++) {
					var feature = ft.getOldFeatures()[i];
					var rowIndex = this._searchFeature(feature);
					if (rowIndex != null) {
						this._deleteRow(rowIndex);
					}
					if (this.features.contains(feature.getId())) {
						this.features.remove(feature.getId());
					}
				}				
			}
		}
	},

	// Getters and setters:

	isIdInTable : function () {
		return this.idInTable;
	},

	setIdInTable : function (idInTable) {
		this.idInTable = idInTable;
	},

	getSingleSelection : function () {
		return this.singleSelection;
	},

	setSingleSelection : function (singleSelection) {
		this.singleSelection = singleSelection;
	},

	getSupportEditing : function () {
		return this.supportEditing;
	},

	setSupportEditing : function (supportEditing) {
		this.supportEditing = supportEditing;
	}
});
