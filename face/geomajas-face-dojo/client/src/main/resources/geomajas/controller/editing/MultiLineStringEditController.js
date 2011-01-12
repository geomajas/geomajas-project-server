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

dojo.provide("geomajas.controller.editing.MultiLineStringEditController");
dojo.require("geomajas.event.MouseListener");
dojo.require("geomajas.action.menu.editing.CancelEditingAction");
dojo.require("geomajas.action.menu.editing.UndoEditingAction");
dojo.require("geomajas.action.menu.editing.SaveEditingAction");
dojo.require("geomajas.action.menu.editing.ToggleEditingModeAction");
dojo.require("geomajas.action.menu.editing.InsertPointAction");
dojo.require("geomajas.action.menu.editing.RemovePointAction");
dojo.require("geomajas.action.menu.editing.EditAttributesAction");

dojo.declare("MultiLineStringEditController", MouseListener, {

	/**
	 * @fileoverview Mouselistener for editing LineStrings.
	 * @class Editing controller designed for linestring geometries.
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
		this.menuId = "multiLineStringEditMenu";
		this._menu = this._getOrCreateMenu();
		this.factory = new GeometryFactory(
			mapWidget.getMapModel().getSRID(),
			mapWidget.getMapModel().getPrecision());
		this.editor = new GeometryEditor();
	},

	getParent : function() {
		return this.parent;
	},

	getName : function () {
		return "MultiLineStringEditController";
	},
	
	onActivate : function () {
		log.info(this.getName() + ".onActivate()");
		this._menu.bindDomNode(this.mapWidget.id);
	},
	
	onDeactivate : function () {
		log.info(this.getName() + ".onDeactivate()");
		this._menu.unBindDomNode(this.mapWidget.id);
	},
	
	getStartIndex : function (multiLineString) {
		return [0];
	},

	getNewLineStringIndex : function () {
		alert("MultiLineStringEditController.getNewLineStringIndex => implement me!!");
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

				// When we pass all of the previous tests, we can add a new point to some linearring:
				this.createDragLines(trans, event);

				var command = new AddCoordinateCommand (index, position);
				trans.execute (command);

				this.parent.refreshGeomInfo();
				dojo.publish(this.mapWidget.getRenderTopic(), [trans, "delete"]);
				dojo.publish(this.mapWidget.getRenderTopic(), [trans, "all"]);
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

		if (this.dragLine == null) {
			var style = new ShapeStyle("#FFFFFF", "0", "#FF3322", "1", "1", "8,4", null);
			var lineString = this.factory.createLineString([position, position]);
			this.dragLine = new Line ();
			this.dragLine.setId("editController.dragLine1");
			this.dragLine.setGeometry(lineString);
			this.dragLine.setStyle(style);
			dojo.publish (this.mapWidget.getRenderTopic(), [this.dragLine, "all"]);
		}
	},

	updateDragLines : function (featureTransaction, event) {
		if(featureTransaction.getNewFeatures() == null){
			return;
		}
		var coords = featureTransaction.getNewFeatures()[0].getGeometry().getCoordinates();
		if (coords == null || coords.length == 0) {
			return;
		}

		if (this.dragLine == null) {
			this.createDragLines(featureTransaction, event);
		}

		this._getSnapper();
		var temp = this.parent.getTransform().viewPointToWorld(event.getPosition());
		if (this.snapper != null) {
			temp = this.snapper.snap(temp);
		}
		var position = this.parent.getTransform().worldPointToView(temp);

		if (coords.length > 0){
			var c1 = this.parent.getTransform().worldPointToView(coords[coords.length - 1]);
			var lineString = this.factory.createLineString([c1, position]);
			this.dragLine.setGeometry(lineString);
			dojo.publish (this.mapWidget.getRenderTopic(), [this.dragLine, "all"]);
		}
	},

	removeDragLines : function () {
		if (this.dragLine != null) {
			dojo.publish (this.mapWidget.getRenderTopic(), [this.dragLine, "delete"]);
			this.dragLine = null;
		}
	},

	/**
	 * @private
	 */
	_checkAppendGeometryValidity : function (featureTransaction, index, coordinate) {
		var multiLineString = featureTransaction.getNewFeatures()[index[0]].getGeometry().clone();
		var geometry = multiLineString.getGeometryN(index[1]);
		geometry.appendCoordinate(coordinate);
		return multiLineString.isValid();
	},

	/**
	 * @private
	 */
	_checkMoveGeometryValidity : function (featureTransaction, index, coordinate) {
		var multiLineString = featureTransaction.getNewFeatures()[index[0]].getGeometry().clone();
		var operation = new SetCoordinateOperation(index[1], coordinate);
		this.editor.edit(multiLineString, operation)
		return multiLineString.isValid();
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
		
		var action3 = new ToggleEditingModeAction(this.menuId+".toggleMode", this.mapWidget, this.parent);
		var item3 = new geomajas.gfx.menu.MenuItem({label:action3.getText(),action:action3});
		pMenu.addChild(item3);
		
		pMenu.addChild(new dijit.MenuSeparator()); //item4

		var action5 = new InsertPointAction(this.menuId+".insert", this.mapWidget, null,null);
		var item5 = new geomajas.gfx.menu.MenuItem({label:action5.getText(),action:action5});
		pMenu.addChild(item5);
		
		pMenu.addChild(new dijit.MenuSeparator()); //item6

		var action7 = new RemovePointAction(this.menuId+".remove", this.mapWidget, null);
		var item7 = new geomajas.gfx.menu.MenuItem({label:action7.getText(),action:action7});
		pMenu.addChild(item7);

		pMenu.addChild(new dijit.MenuSeparator()); //item8
	
		var action9 = new ToggleGeomInfoAction(this.menuId+".geomInfo", this.mapWidget, this.parent);
		var item9 = new geomajas.gfx.menu.MenuItem({label:action9.getText(),action:action9});
		pMenu.addChild(item9);

		var action10 = new EditAttributesAction(this.menuId+".editAttributes", this.mapWidget, this.parent);
		var item10 = new geomajas.gfx.menu.MenuItem({label:action10.getText(),action:action10});
		pMenu.addChild(item10);

		var action11 = new ZoomToSelectionAction(this.menuId+".zoomToFit", this.mapWidget);
		action11.setText("Zoom to fit");
		var item11 = new geomajas.gfx.menu.MenuItem({label:action11.getText(),action:action11});
		pMenu.addChild(item11);

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
		this._menu.getChildren()[5].setDisabled (true);
		this._menu.getChildren()[7].setDisabled (true);
		
		if (this.parent.isDrawOnlyMode()) {
			this._menu.getChildren()[1].setDisabled(true); /* Save Feature action */
			this._menu.getChildren()[10].setDisabled(true); /* Edit attributes */
		}
		else {
			this._menu.getChildren()[1].setDisabled(false);  /* Save Feature action */
			this._menu.getChildren()[10].setDisabled(false); /* Edit attributes */
		}
		
		if (trans.getCommandStack().count > 0) {
			this._menu.getChildren()[0].setDisabled(false);
		}
		
		if (this.parent.isDragging()) { // DRAG-MODE
			var pointIndex = trans.getPointIndexById(target);

			// item 5 - Check for insert point:
			var isEdge = trans.isEdge(target);
			if (isEdge) {
				var lineIndex = trans.getLineStringIndexById(target);
				var position = this.parent.getTransform().viewPointToWorld(event.getPosition());
				var action5 = new InsertPointAction(this.menuId+".insert", this.mapWidget, lineIndex, position);
				this._menu.getChildren()[5].action = action5;
				this._menu.getChildren()[5].setDisabled(false);
			}
			
			// item 6 - Check for remove point:
			if (pointIndex != null && pointIndex[1].length > 0 && !isEdge) {
				// Do not allow removal of points when less then 2 points in geometry.
				var ringId = pointIndex[1].slice(0, pointIndex[1].length-1);
				var ls = trans.getNewFeatures()[pointIndex[0]].getGeometry().getGeometryN(ringId);
				if (ls.getNumPoints() > 2 && trans.isVertex(target)) {
					var action7 = new RemovePointAction(this.menuId+".remove", this.mapWidget, pointIndex);
					this._menu.getChildren()[7].action = action7;
					this._menu.getChildren()[7].setDisabled(false);
				}
			}
		}	
		if (this.mapWidget.getMapModel().getSelectionCount() == 0) {
			var action11 = new ZoomToFeatureAction(this.menuId+".zoomToFit", this.mapWidget, trans.getNewFeatures()[0]);
			this._menu.getChildren()[11].action = action11;
		}
	}
});