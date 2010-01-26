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

dojo.provide("geomajas.controller.editing.PolygonEditController");
dojo.require("geomajas.event.MouseListener");
dojo.require("geomajas.action.menu.editing.CancelEditingAction");
dojo.require("geomajas.action.menu.editing.UndoEditingAction");
dojo.require("geomajas.action.menu.editing.SaveEditingAction");
dojo.require("geomajas.action.menu.editing.StopInsertingAction");
dojo.require("geomajas.action.menu.editing.InsertPointAction");
dojo.require("geomajas.action.menu.editing.RemovePointAction");
dojo.require("geomajas.action.menu.editing.StartLinearRingAction");
dojo.require("geomajas.action.menu.editing.DeleteLinearRingAction");
dojo.require("geomajas.action.menu.editing.EditAttributesAction");

dojo.declare("PolygonEditController", MouseListener, {

	/**
	 * @fileoverview Mouselistener for editing Polygons.
	 * @class Editing controller designed for polygon geometries.
	 * @author Jan De Moerloose & Pieter De Graef
	 *
	 * @constructor
	 * @extends MouseListener
	 * @param parent Parent editing controller.
	 * @param mapWidget The MapWidget object we editing on.
	 */
	constructor : function (parent, mapWidget) {
		this.parent = parent;
		this.mapWidget = mapWidget;
		this.draggingStarted = false;
		//this.menuId = this.parent.menuId;
		this.menuId = "PolygonEditMenu";
		this._menu = this._getOrCreateMenu();
		this.snapper = null;
		this.factory = new GeometryFactory(
			mapWidget.getMapModel().getSRID(),
			mapWidget.getMapModel().getPrecision());
		this.editor = new GeometryEditor();
	},

	getParent : function() {
		return this.parent;
	},

	getName : function () {
		return "PolygonEditController";
	},
	
	onActivate : function () {
		log.info(this.getName() + ".onActivate()");
		this._menu.bindDomNode(this.mapWidget.id);
	},
	
	onDeactivate : function () {
		log.info(this.getName() + ".onDeactivate()");
		this._menu.unBindDomNode(this.mapWidget.id);
	},
	
	/**
	 * Returns the index for finding the right insertion point when creating a
	 * new geometry. This simply returns[0], so that the {@link Point#getGeometryN}
	 * would return the shell of the polygon.
	 */
	getStartIndex : function (polygon) {
		return [0];
	},

	/**
	 * Returns the starting index for a new hole.
	 */
	getNewRingIndex : function (polygon) {
		var numHoles = polygon.getNumInteriorRing();
		return [numHoles+1];
	},

	/**
	 * @param event A HtmlMouseEvent object.
	 */
	mouseClicked : function (/*HtmlMouseEvent*/event) {
		var trans = this.mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		if (trans != null && event.getButton() == event.statics.LEFT_MOUSE_BUTTON) {
			if (this.parent.isInserting()) {
				var geomIndex = this.parent.getGeometryIndex();
				if (geomIndex == null) { // If no geometry index, then it's a new feature. Use the startIndex.
					this.parent.setGeometryIndex(this.getStartIndex());
				}

				this._getSnapper();
				var position = this.parent.getTransform().viewPointToWorld(event.getPosition());
				if (this.snapper != null) {
					position = this.snapper.snap(position);
				}

				var index = [this.parent.getFeatureIndex(), this.parent.getGeometryIndex()];
				if (!this._checkAppendGeometryValidity(trans, index, position)) {
					return;
				}

				// When clicking on the first point, stop inserting:
				if (this._isFirstPoint(event.getTargetId())) {
					this.parent.setMode(this.parent.statics.DRAG_MODE);
					this.removeDragLines();
				} else {
					// When we pass all of the previous tests, we can add a new point to some linearring:
					this.createDragLines(trans, event);

					var command = new AddCoordinateCommand (index, position);
					trans.execute (command);

					this.parent.refreshGeomInfo();
					dojo.publish(this.mapWidget.getRenderTopic(), [trans, "delete"]);
					dojo.publish(this.mapWidget.getRenderTopic(), [trans, "all"]);
				}
			}
		}
	},

	/**
	 * @param event A HtmlMouseEvent object.
	 */
	mouseMoved : function (/*HtmlMouseEvent*/event) {
		var trans = this.mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		if (trans != null) {
			if (this.parent.isInserting()) {
				this.updateDragLines(trans, event);
			} else if (this.parent.isDragging() && this.draggingStarted) {
				var index = this.dragFeatureTransaction.getPointIndexById(this.dragTargetId);
				var geometry = this.dragFeatureTransaction.getNewFeatures()[index[0]].getGeometry();

				this._getSnapper();
				var position = this.parent.getTransform().viewPointToWorld(event.getPosition());
				if (this.snapper != null) {
					position = this.snapper.snap(position);
				}
				this.editor.edit(geometry, new SetCoordinateOperation(index[1], position));

				dojo.publish(this.mapWidget.getRenderTopic(), [this.dragFeatureTransaction, "delete"]);
				dojo.publish(this.mapWidget.getRenderTopic(), [this.dragFeatureTransaction, "all"]);
			}
		}
	},

	mousePressed : function (/*HtmlMouseEvent*/event) {
		var trans = this.mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		if (trans == null) {
			return;
		}

		if (this.parent.isDragging() && event.getButton() != event.statics.RIGHT_MOUSE_BUTTON && trans.isDragTarget(event.getTargetId())){
			this.draggingStarted = true;
			this.dragPosition = this.parent.getTransform().viewPointToWorld(event.getPosition());
			this.dragTargetId = event.getTargetId();
			this.dragFeatureTransaction = trans.clone(); // While dragging, a clone is painted

			dojo.publish(this.mapWidget.getRenderTopic(), [trans, "delete"]);
			dojo.publish(this.mapWidget.getRenderTopic(), [this.dragFeatureTransaction, "all"]);
		}
	},

	mouseReleased : function (/*HtmlMouseEvent*/event) {
		if (event.getButton() != event.statics.RIGHT_MOUSE_BUTTON) {
			if (this._menu.isShowingNow) {
				dijit.popup.close(this._menu);
			}
		
			if (this.draggingStarted) {
				this.draggingStarted = false;

				var trans = this.mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
				if (trans != null) {
					this._getSnapper();
					var position = this.parent.getTransform().viewPointToWorld(event.getPosition());
					if (this.snapper != null) {
						position = this.snapper.snap(position);
					}

					var index = trans.getPointIndexById(this.dragTargetId);
					if (index == null) {
						return;
					}
					if (this._checkMoveGeometryValidity(trans, index, position)) {
						var command = new MoveCoordinateCommand (index[1], position);
						trans.execute (command);
						this.parent.refreshGeomInfo();
					}

					if (this.dragFeatureTransaction != null) {
						dojo.publish(this.mapWidget.getRenderTopic(), [this.dragFeatureTransaction, "delete"]);
					}
					dojo.publish(this.mapWidget.getRenderTopic(), [trans, "all"]);
				}
				this.dragTargetId = null;
				this.dragPosition = null;
				this.dragFeatureTransaction = null;
			} 
		}
	},

	contextMenu : function (/*HtmlMouseEvent*/event) {
		this._configureMenu(event);	
		 // Override the stop-propagation! We need a right mouse menu in this controller.
	},

	createDragLines : function (featureTransaction, event) {
		if(featureTransaction.getNewFeatures() == null){
			return;
		}

		this._getSnapper();
		var temp = this.parent.getTransform().viewPointToWorld(event.getPosition());
		if (this.snapper != null) {
			temp = this.snapper.snap(temp);
		}
		var position = this.parent.getTransform().worldPointToView(temp);

		if (this.dragLine1 == null) {
			var lineString = this.factory.createLineString([position, position]);
			this.dragLine1 = new Line ();
			this.dragLine1.setId("editController.dragLine1");
			this.dragLine1.setGeometry(lineString);
			this.dragLine1.setStyle(new ShapeStyle("#FFFFFF", "0", "#FF3322", "1", "1", "8,4", null));
			dojo.publish (this.mapWidget.getRenderTopic(), [this.dragLine1, "all"]);
		}

		if (this.dragLine2 == null) {
			var lineString = this.factory.createLineString([position, position]);
			this.dragLine2 = new Line ();
			this.dragLine2.setId("editController.dragLine2");
			this.dragLine2.setGeometry(lineString);
			this.dragLine2.setStyle(new ShapeStyle("#FFFFFF", "0", "#FF3322", "1", "1", "8,4", null));
			dojo.publish (this.mapWidget.getRenderTopic(), [this.dragLine2, "all"]);
		}
	},

	updateDragLines : function (featureTransaction, event) {
		if(featureTransaction.getNewFeatures() == null){
			return;
		}

		var index = this.parent.getGeometryIndex();
		var geometry = featureTransaction.getNewFeatures()[0].getGeometry().getGeometryN(index);
		if (geometry == null) {
			return;
		}
		var coords = geometry.getCoordinates();
		if (coords == null || coords.length == 0) {
			return;
		}

		if (this.dragLine1 == null) {
			this.createDragLines(featureTransaction, event);
		}

		this._getSnapper();
		var temp = this.parent.getTransform().viewPointToWorld(event.getPosition());
		if (this.snapper != null) {
			temp = this.snapper.snap(temp);
		}
		var position = this.parent.getTransform().worldPointToView(temp);

		// Update the first dragline:
		var c1 = this.parent.getTransform().worldPointToView(coords[0]);
		var lineString1 = this.factory.createLineString([c1, position]);
		this.dragLine1.setGeometry(lineString1);
		dojo.publish (this.mapWidget.getRenderTopic(), [this.dragLine1, "all"]);

		// Update the second dragline:
		var c2 = this.parent.getTransform().worldPointToView(coords[coords.length - 2]);
		var lineString2 = this.factory.createLineString([c2, position]);
		this.dragLine2.setGeometry(lineString2);
		dojo.publish (this.mapWidget.getRenderTopic(), [this.dragLine2, "all"]);
	},

	removeDragLines : function () {
		if (this.dragLine1 != null) {
			dojo.publish (this.mapWidget.getRenderTopic(), [this.dragLine1, "delete"]);
			this.dragLine1 = null;
		}
		if (this.dragLine2 != null) {
			dojo.publish (this.mapWidget.getRenderTopic(), [this.dragLine2, "delete"]);
			this.dragLine2 = null;
		}
	},

	/**
	 * @private
	 */
	_checkAppendGeometryValidity : function (featureTransaction, index, coordinate) {
		var polygon = featureTransaction.getNewFeatures()[index[0]].getGeometry().clone();

		if (index[1].length == 1 && index[1] > 0) {
			var mathLib = new MathLib();
			if (!mathLib.isWithin(polygon, coordinate)) {
				log.warn ("Point lies outside the polygon!");
				return false;
			}

			if (mathLib.touches(polygon, coordinate)) {
				log.warn ("Point touches the polygon!");
				return false;
			}
		}

		var geometry = polygon.getGeometryN(index[1]);
		geometry.appendCoordinate(coordinate);
		return polygon.isValid();
	},

	/**
	 * @private
	 */
	_checkMoveGeometryValidity : function (featureTransaction, index, coordinate) {
		var polygon = featureTransaction.getNewFeatures()[index[0]].getGeometry().clone();

		if (index[1].length == 1 && index[1] > 0) {
			var mathLib = new MathLib();
			if (!mathLib.isWithin(polygon, coordinate)) {
				log.warn ("Point lies outside the polygon!");
				return false;
			}

			if (mathLib.touches(polygon, coordinate)) {
				log.warn ("Point touches the polygon!");
				return false;
			}
		}

		var operation = new SetCoordinateOperation(index[1], coordinate);
		this.editor.edit(polygon, operation)
		return polygon.isValid();
	},

	/**
	 * @private
	 */
	_isFirstPoint : function (target) {
		var trans = this.mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		var geomIndex = this.parent.getGeometryIndex();
		var index = trans.getPointIndexById(target);
		if (index == null || index[1].length == 0) {
			return false;
		}
		var pIndex = index[1];
		for (var i=0; i<geomIndex.length; i++) {
			if (pIndex[i] != geomIndex[i]) {
				return false;
			}
		}
		if (pIndex[pIndex.length-1] == 0) {
			return true;
		}
		return false;
	},
	
	/**
	 * @private
	 */
	_getSnapper : function () {
		if (this.snapper == null) {
			var layer = this.mapWidget.getMapModel().getSelectedLayer();
			if (layer != null) {
				this.snapper = layer.getSnapper();
			} else {
				var trans = this.mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
				var layer = trans.getNewFeatures()[0].getLayer();
				if (layer != null) {
					this.snapper = layer.getSnapper();
				}
			}
		}
	},

	/**
	 * @private
	 */
	_getOrCreateMenu : function () {
		log.info(this.getName() + "._getOrCreateMenu()");
		
		var pMenu = dijit.byId (this.menuId);
        if (pMenu != null){ return pMenu; } 
		
		pMenu = new Menu({id:this.menuId});
		
		var action0 = new UndoEditingAction(this.menuId+".undo", this.mapWidget, this.parent);
		var item0 = new geomajas.gfx.menu.MenuItem({label:action0.getText(),action:action0});
		pMenu.addChild(item0);
		
		var action1 = new SaveEditingAction(this.menuId+".save", this.mapWidget, this.parent);
		var item1 = new geomajas.gfx.menu.MenuItem({label:action1.getText(),action:action1});
		pMenu.addChild(item1);
		
		var action2 = new CancelEditingAction(this.menuId+".cancel", this.mapWidget, this.parent);
		var item2 = new geomajas.gfx.menu.MenuItem({label:action2.getText(),action:action2});
		pMenu.addChild(item2);

		var action3 = new StopInsertingAction(this.menuId+".toggleMode", this.mapWidget, this.parent);
		var item3 = new geomajas.gfx.menu.MenuItem({label:action3.getText(),action:action3});
		pMenu.addChild(item3);
	
		pMenu.addChild(new dijit.MenuSeparator()); //item4
	
		var action5 = new InsertPointAction(this.menuId+".insert", this.mapWidget, null, null);
		var item5 = new geomajas.gfx.menu.MenuItem({label:action5.getText(),action:action5});
		pMenu.addChild(item5);

		pMenu.addChild(new dijit.MenuSeparator()); //item 6

		var action7 = new RemovePointAction(this.menuId+".remove", this.mapWidget, null);
		var item7 = new geomajas.gfx.menu.MenuItem({label:action7.getText(),action:action7});
		pMenu.addChild(item7);

		pMenu.addChild(new dijit.MenuSeparator()); //item 8

		var action9 = new StartLinearRingAction(this.menuId+".startRing", this.mapWidget);
		var item9 = new geomajas.gfx.menu.MenuItem({label:action9.getText(),action:action9});
		pMenu.addChild(item9);

		var action10 = new DeleteLinearRingAction(this.menuId+".deleteRing", this.mapWidget);
		var item10 = new geomajas.gfx.menu.MenuItem({label:action10.getText(),action:action10});
		pMenu.addChild(item10);

		pMenu.addChild(new dijit.MenuSeparator()); //item 11

		var action12 = new ToggleGeomInfoAction(this.menuId+".geomInfo", this.mapWidget, this.parent);
		var item12 = new geomajas.gfx.menu.MenuItem({label:action12.getText(),action:action12});
		pMenu.addChild(item12);

		var action13 = new EditAttributesAction(this.menuId+".editAttributes", this.mapWidget, this.parent);
		var item13 = new geomajas.gfx.menu.MenuItem({label:action13.getText(),action:action13});
		pMenu.addChild(item13);

		var action14 = new ZoomToSelectionAction(this.menuId+".zoomToFit", this.mapWidget);
		action14.setText("Zoom to fit");
		var item14 = new geomajas.gfx.menu.MenuItem({label:action14.getText(),action:action14});
		pMenu.addChild(item14);

		return pMenu;
	},
	
	/**
	 * @private
	 */
	_configureMenu : function (event) {
		log.info(this.getName() + "._configureMenu(event) for targetId: " + event.getTargetId());

		var trans = this.mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		var target = event.getTargetId();

		//NOTE: pass the HtmlMouseEvent we get here to each of the menuitems.
		dojo.forEach(this._menu.getChildren(), function(child){ if(child.setOriginalEvent) {child.setOriginalEvent(event);} });

		//disable these unless specified otherwise later on
		this._menu.getChildren()[0].setDisabled (true);
		this._menu.getChildren()[3].setDisabled (true);
		this._menu.getChildren()[5].setDisabled (true);
		this._menu.getChildren()[7].setDisabled (true);
		this._menu.getChildren()[9].setDisabled (true);
		this._menu.getChildren()[10].setDisabled (true);

		if (trans.getCommandStack().count > 0) {
			this._menu.getChildren()[0].setDisabled(false);
		}

		if (this.parent.isInserting() && this.parent.checkEditedRingValidity()) { // INSERT-MODE
			this._menu.getChildren()[3].setDisabled (false);
		} else { // DRAG-MODE
			var pointIndex = trans.getPointIndexById(target);

			// item5 - Check for insert point:
			var isEdge = trans.isEdge(target);
			if (isEdge) {
				var lineIndex = trans.getLineStringIndexById(target);
				var position = this.parent.getTransform().viewPointToWorld(event.getPosition());
				var action5 = new InsertPointAction(this.menuId+".insert", this.mapWidget, lineIndex, position);
				this._menu.getChildren()[5].action = action5;
				this._menu.getChildren()[5].setDisabled(false);
				}

			// item7 - Check for remove point:
			//var lsIndex = trans.getLineStringIndexById(target);
			if (pointIndex != null && pointIndex[1].length > 0 && !isEdge) {

				// Do not allow removal of points when less then 4 points in geometry.			
				//var ls = trans.getNewFeatures()[lsIndex[0]].getGeometry().getGeometryN(lsIndex[1]);
				var ringId = pointIndex[1].slice(0, pointIndex[1].length-1);
				var ls = trans.getNewFeatures()[pointIndex[0]].getGeometry().getGeometryN(ringId);
				if (ls.getNumPoints() > 4 && trans.isVertex(target)) {
					var action7 = new RemovePointAction(this.menuId+".remove", this.mapWidget, pointIndex);
					this._menu.getChildren()[7].action = action7;
					this._menu.getChildren()[7].setDisabled (false); 
				}
			}

			// item 9/10 - Holes:
			if (this.parent.isAllowHoles()) {
				if (!this.parent.isInserting()) { // When inserting, you cannot immediatly start a new hole. (otherwise you can create empty holes one after another)
					this._menu.getChildren()[9].setDisabled (false);
				}
				if (trans.isHole(target, true)) {
					this._menu.getChildren()[10].setDisabled(false);
				}
			}
		}		
		if (this.mapWidget.getMapModel().getSelectionCount() == 0) {
			var action14 = new ZoomToFeatureAction(this.menuId+".zoomToFit", this.mapWidget, trans.getNewFeatures()[0]);
			this._menu.getChildren()[14].action = action14;
		}
	}
});