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

dojo.provide("geomajas.controller.editing.EditingController");
dojo.require("geomajas._base");
dojo.require("geomajas.event.MouseListener");
dojo.require("geomajas.action.menu.editing.StartEditingAction");
dojo.require("geomajas.action.menu.editing.DeleteFeatureAction");
dojo.require("geomajas.action.menu.editing.NewFeatureAction");

dojo.require("geomajas.controller.editing.PointEditController");
dojo.require("geomajas.controller.editing.LineStringEditController");
dojo.require("geomajas.controller.editing.MultiLineStringEditController");
dojo.require("geomajas.controller.editing.PolygonEditController");
dojo.require("geomajas.controller.editing.MultiPolygonEditController");

dojo.require("geomajas.gfx.menu.Menu");

dojo.declare("EditingController", MouseListener, {

	statics : {
		INSERT_MODE : 1,
		DRAG_MODE : 2
	},

	/**
	 * @fileoverview Base editing mouselistener.
	 * @class Controller responsible for interpreting user events as edit
	 * commands. On changes in the {@link FeatureTransaction} object in the {@link FeatureEditor},
	 * this controller should update the actual controller it uses to do the editing.
	 * What controller that actually is, depends on the geometry type.
	 * @author Jan De Moerloose & Pieter De Graef
	 *
	 * @constructor
	 * @extends MouseListener
	 * @param mapWidget The MapWidget object we editing on.
	 */
	constructor : function (mapWidget, useSnapHelp, allowHoles) {
		/** Can be either insert-mode or drag-mode. */
		this.mode = this.statics.DRAG_MODE;
		
		/** Reference to the MapWidget object. */
		this.mapWidget = mapWidget;
		
		/** This base editing controller makes use of other controller's to do it's bidding. */
		this.controller = null;
		
		this.featureIndex = 0;
		this.geometryIndex = null;
		
		/** @private Used in the DOM. */
		this.menuId = "editingMenu";
		this._menu = this._getOrCreateMenu();	
		
		/** @private */
		this.transform = new WorldViewTransformation(this.mapWidget.getMapView());
			
		dojo.connect(this.mapWidget.getMapModel().getFeatureEditor(),"onFeatureTransactionChanged",this,"_updateController");
		this._updateController();

		if (useSnapHelp == "true") {
			this.snapController = new SnappingHelpController(mapWidget);
		} else {
			this.snapController = null;
		}
		this.allowHoles = allowHoles;
		
		this.geomInfo = null;
		this.editCursor = "crosshair";
	},

	/**
	 * Return a unique name.
	 */
	getName : function () {
		return "EditingController";
	},

	/**
	 * Initialize this controller on activation.
	 */
	onActivate : function () {
		log.info(this.getName() + ".onActivate()");
		this._menu.bindDomNode(this.mapWidget.id);
	},

	/**
	 * cleanup this controller on deactivation.
	 */
	onDeactivate : function () {
		log.info(this.getName() + ".onDeactivate()");
		this._menu.unBindDomNode(this.mapWidget.id);
		if (this.snapController != null) {
			this.snapController.removeGraphicalContent();
		}
	},

	isAllowHoles : function () {
		return (this.allowHoles == "true");
	},

	getGeomInfo : function () {
		return this.geomInfo;
	},

	setGeomInfo : function (geomInfo) {
		this.geomInfo = geomInfo;
	},

	getMode : function () {
		return this.mode;
	},

	setMode : function (mode) {
		this.mode = mode;
	},

	getController : function () {
		return this.controller;
	},

	setController : function (controller) {
		this.controller = controller;
	},

	getEditCursor : function () {
		return this.editCursor;
	},

	setEditCursor : function (editCursor) {
		this.editCursor = editCursor;
	},

	setFeatureIndex : function (featureIndex) {
		this.featureIndex = featureIndex;
	},

	getFeatureIndex : function () {
		return this.featureIndex;
	},

	setGeometryIndex : function (geometryIndex) {
		this.geometryIndex = geometryIndex;
	},

	getGeometryIndex : function () {
		return this.geometryIndex;
	},

	getTransform : function () {
		return this.transform;
	},

	isInserting : function () {
		return this.mode == this.statics.INSERT_MODE;
	},

	isDragging : function () {
		return this.mode == this.statics.DRAG_MODE;
	},

	/**
	 * If there is a controller, delegate. Otherwise see if it's possible to select a feature.
	 */
	mouseClicked : function (/*HtmlMouseEvent*/event) {
		if (this.controller != null) {
			this.controller.mouseClicked(event);
		} else if(event.getButton() == event.statics.LEFT_MOUSE_BUTTON) {
			var action = new ToggleSelectionAction(this.menuId+".toggle", this.mapWidget, true);
			action.actionPerformed(event);
		}
	},

	/**
	 * If there is a controller, delegate. Otherwise in case of a rightmouse click, show a menu.
	 */
	mouseReleased : function (/*HtmlMouseEvent*/event) {
		if (this.controller != null) {
			this.controller.mouseReleased(event);
		} else {
			
			if (event.getButton() != event.statics.RIGHT_MOUSE_BUTTON) {
				if (this._menu.isShowingNow) {
					dijit.popup.close(this._menu);
				}
			}
		}
	},

	/**
	 * If there is a controller, delegate.
	 */
	mousePressed : function (/*HtmlMouseEvent*/event) {
		if (this.controller != null) {
			this.controller.mousePressed(event);
		}
	},

	/**
	 * If there is a controller, delegate.
	 */
	mouseMoved : function (/*HtmlMouseEvent*/event) {
		if (this.snapController) {
			if (this.geometryIndex != null && this.mode == this.statics.INSERT_MODE) {
				this.snapController.mouseMoved(event);
			} else {
				this.snapController.removeGraphicalContent();
			}
		}

		if (this.controller != null) {
			this.controller.mouseMoved(event);
		}
	},

	contextMenu : function (/*HtmlMouseEvent*/event) {
		this._configureMenu(event);		
		if(this.controller) {
			this.controller.contextMenu(event);
		}
		 // Override the stop-propagation! We need a right mouse menu in this controller.
	},

	/**
	 * While in insertmode, dragging lines are drawn that follow the cursor.
	 * This function removes threm from the map.
	 */
	removeDragLines : function () {
		if (this.controller != null) {
			this.controller.removeDragLines();
		}
	},

	createDragLines : function (featureTransaction, event) {
		if (this.controller != null) {
			this.controller.createDragLines(featureTransaction, event);
		}
	},

	/**
	 * Checks whether or not the currently edited LinearRing is valid and has
	 * at least 3 different points.
	 * @return Returns true or false.
	 */
	checkEditedRingValidity : function () {
		var trans = this.mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		var geometry = trans.getNewFeatures()[this.featureIndex].getGeometry().getGeometryN(this.geometryIndex);
		if (geometry instanceof LinearRing) {
			return (geometry != null && geometry.isValid() && geometry.getNumPoints() >= 4);
		} else {
			return (geometry != null && geometry.isValid());
		}
	},

	/**
	 * @private
	 */
	_updateController : function () {
		log.info("_updateController()");
		
		//DVB: only react to this event if the current mapcontroller is an editcontroller
		// 	   otherwise this will steal the focus from other controllers and deactivate them 
		var curCon = this.mapWidget.getCurrentController();
		if (curCon == null || (!(curCon instanceof  EditingController))) 
		{
			log.info("--- current mapcontroller is not an editingcontroller - aborting");
			return;
		}
		if (this.controller != null) {
			log.debug("---  specific controller was:" + this.controller.getName());
		} else {
			log.debug("---  specific controller was : null");
		}
	
		this.setMode(this.statics.INSERT_MODE);
		var featureTransaction = this.mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		if (featureTransaction != null) {
			this.mapWidget.setCursor(this.editCursor);
			var layer = featureTransaction.getLayer();
			if (layer != null) {
				var type = layer.getLayerType();
				if (type == geomajas.LayerTypes.POINT) {
					this.onDeactivate();
					this.controller = new PointEditController(this, this.mapWidget);
					this.controller.onActivate();
				} 
				else if (type == geomajas.LayerTypes.LINESTRING) {
					this.onDeactivate();
					this.controller = new LineStringEditController(this, this.mapWidget);
					this.controller.onActivate();
				} 
				else if (type == geomajas.LayerTypes.MULTILINESTRING) {
					this.onDeactivate();
					this.controller = new MultiLineStringEditController(this, this.mapWidget);
					this.controller.onActivate();
				} 
				else if (type == geomajas.LayerTypes.POLYGON) {
					this.onDeactivate();
					this.controller = new PolygonEditController(this, this.mapWidget);
					this.controller.onActivate();
				}
				else if (type == geomajas.LayerTypes.MULTIPOLYGON) {
					this.onDeactivate();
					this.controller = new MultiPolygonEditController(this, this.mapWidget);
					this.controller.onActivate();
				}
			}
		}	
		else {
			this.mapWidget.setCursor("default");
			if (this.controller != null)
			{
				this.controller.onDeactivate();
				this.controller = null;
				this.onActivate();
			}
		}
	},

	refreshGeomInfo : function () {
		if (this.geomInfo != null) {
			var action = new ToggleGeomInfoAction(this.menuId+".geomInfo", this.mapWidget, this);
			action.actionPerformed(null);
			action.actionPerformed(null);
		}
	},

	/**
	 * @private
	 */
	_getOrCreateMenu : function () {
		log.info(this.getName() + "._getOrCreateMenu()");
		
		var pMenu = dijit.byId(this.menuId);
		if (pMenu != null){
			return pMenu;
		}
	 
		pMenu = new Menu({id:this.menuId});
	   
       	var action_new = new NewFeatureAction(this.menuId+".new", this.mapWidget, this);
 		var mitem_new = new geomajas.gfx.menu.MenuItem({label:action_new.getText(),action:action_new});
        pMenu.addChild(mitem_new);
        
		var action_edit =  new StartEditingAction(this.menuId+".edit", this.mapWidget);
        var mitem_edit = new geomajas.gfx.menu.MenuItem({label:action_edit.getText(), action:action_edit});
        pMenu.addChild(mitem_edit);
        
		var action_delete = new DeleteFeatureAction(this.menuId+".delete", this.mapWidget);
        var mitem_delete = new geomajas.gfx.menu.MenuItem({label:action_delete.getText(),action:action_delete});
        pMenu.addChild(mitem_delete);

		var action_toggle = new ToggleSelectionAction(this.menuId+".toggle", this.mapWidget, true);
        var mitem_toggle = new geomajas.gfx.menu.MenuItem({label:action_toggle.getText(),action:action_toggle});
        pMenu.addChild(mitem_toggle);
	
		var action_deselall = new DeselectAllAction(this.menuId+".deselectall", this.mapWidget.getMapModel().getSelectionTopic());
        var mitem_deselall = new geomajas.gfx.menu.MenuItem({label:action_deselall.getText(),action:action_deselall});
        pMenu.addChild(mitem_deselall);

		return pMenu;
	},
	
	/**
	 * @private
	 */
	_configureMenu : function (event) {
		log.info(this.getName() + "._configureMenu(event) for targetId: " + event.getTargetId());

		//NOTE: pass the HtmlMouseEvent we get here to each of the menuitems.
		dojo.forEach(this._menu.getChildren(), function(child){ if(child.setOriginalEvent) {child.setOriginalEvent(event);} });
		
		// Create button:
		var layer = this.mapWidget.getMapModel().getSelectedLayer();
		if (layer != null && layer instanceof VectorLayer && layer.getEditPermissions().isCreatingAllowed()) {
			this._menu.getChildren()[0].setDisabled(false);
		} else {
			this._menu.getChildren()[0].setDisabled (true);
		}
		
		// Edit and delete buttons:
		var selection = this.mapWidget.getMapModel().getSelection();
		if (selection.count == 1) { // 1 element must be selected...
			var feature = selection.item(0);
			var layer = feature.getLayer();
			if (layer.getEditPermissions().isUpdatingAllowed() && layer.getFeatureType().getGeometryType().isEditable()) {
				this._menu.getChildren()[1].setDisabled (false);
			} else {
				this._menu.getChildren()[1].setDisabled (true);
			}
			if (layer.getEditPermissions().isDeletingAllowed()) {
				this._menu.getChildren()[2].setDisabled (false);
			} else {
				this._menu.getChildren()[2].setDisabled (true);
			}
		} else { // no selection: no edit, no delete
			this._menu.getChildren()[1].setDisabled (true);
			this._menu.getChildren()[2].setDisabled (true);
		}
	}
});