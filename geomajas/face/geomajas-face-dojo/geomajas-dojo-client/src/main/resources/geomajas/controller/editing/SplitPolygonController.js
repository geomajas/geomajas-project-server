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

dojo.provide("geomajas.controller.editing.SplitPolygonController");
dojo.require("geomajas.event.MouseListener");
dojo.require("geomajas.action.menu.editing.StartSplittingAction");
dojo.require("geomajas.action.menu.editing.StopSplittingAction");
dojo.require("geomajas.action.menu.editing.SaveSplittingAction");
dojo.require("geomajas.action.menu.editing.CancelSplittingAction");
dojo.require("geomajas.action.menu.editing.ToggleSplitInfoAction");

dojo.declare("SplitPolygonController", MouseListener, {

	constructor : function (mapWidget) {
		/** @private Reference to the MapWidget object. */
		this.mapWidget = mapWidget;

		/** @private */
		this.transform = new WorldViewTransformation(this.mapWidget.getMapView());

		/** @private */
		this.splitting = false;

		/** @private */
		this.inserting = false;

		/** @private */
		this.selected = null;

		/** @private Used in the DOM. */
		this.menuId = "splittingMenu";
		this._menu = this._getOrCreateMenu();

		this.factory = new GeometryFactory(
			mapWidget.getMapModel().getSRID(),
			mapWidget.getMapModel().getPrecision());
		this.editor = new GeometryEditor();

		this.snapper = null;
		this.geomInfo = null;
		this.editCursor = "crosshair";
	},

	/**
	 * Return a unique name.
	 */
	getName : function () {
		return "SplitPolygonController";
	},
		
	onActivate : function () {
		log.info(this.getName() + ".onActivate()");
		this._menu.bindDomNode(this.mapWidget.id);
	},
	
	onDeactivate : function () {
		log.info(this.getName() + ".onDeactivate()");
		this._menu.unBindDomNode(this.mapWidget.id);
	},
	
	//DVB: why would we need this? - look like a real good way to shoot yourself in the foot
	getMenuId : function () {
		return this.menuId;
	},

	isSplitting : function () {
		return this.splitting;
	},

	setSplitting : function (splitting) {
		this.splitting = splitting;
		if (splitting) {
			this.mapWidget.setCursor(this.editCursor);
		} else {
			this.mapWidget.setCursor("default");
		}
	},

	isInserting : function () {
		return this.inserting;
	},

	setInserting : function (inserting) {
		this.inserting = inserting;
	},

	getSelected : function () {
		return this.selected;
	},

	setSelected : function (selected) {
		this.selected = selected;
	},

	getTransform : function () {
		return this.transform;
	},

	getGeomInfo : function () {
		return this.geomInfo;
	},

	setGeomInfo : function (geomInfo) {
		this.geomInfo = geomInfo;
	},

	getEditCursor : function () {
		return this.editCursor;
	},

	setEditCursor : function (editCursor) {
		this.editCursor = editCursor;
	},

	mouseClicked : function (/*HtmlMouseEvent*/event) {
		if (event.getButton() == event.statics.LEFT_MOUSE_BUTTON) {
			if (this.splitting && this.inserting) {
				var trans = this.mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();

				// When we pass all of the previous tests, we can add a new point to some linearring:
				this.createDragLine(event);

				var command = new AddCoordinateCommand ([0, []], this.transform.viewPointToWorld(this._getPositionFromEvent(event)));
				trans.execute (command);

				this.refreshGeomInfo();
				dojo.publish(this.mapWidget.getRenderTopic(), [trans, "delete"]);
				dojo.publish(this.mapWidget.getRenderTopic(), [trans, "update"]);
			} else if (!this.splitting){
				var action = new ToggleSelectionAction(this.menuId+".toggle", this.mapWidget, true);
				action.actionPerformed(event);
			}
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
					var position = this.transform.viewPointToWorld(event.getPosition());
					if (this.snapper != null) {
						position = this.snapper.snap(position);
					}

					var index = trans.getPointIndexById(this.dragTargetId);
					if (index == null) {
						return;
					}
					var command = new MoveCoordinateCommand (index[1], position);
					trans.execute (command);
					this.refreshGeomInfo();

					if (this.dragFeatureTransaction != null) {
						dojo.publish(this.mapWidget.getRenderTopic(), [this.dragFeatureTransaction, "delete"]);
					}
					dojo.publish(this.mapWidget.getRenderTopic(), [trans, "update"]);
				}
				this.dragTargetId = null;
				this.dragPosition = null;
				this.dragFeatureTransaction = null;
			} 
		}
	},

	mousePressed : function (/*HtmlMouseEvent*/event) {
		var trans = this.mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		if (trans == null) {
			return;
		}

		if (!this.inserting && event.getButton() != event.statics.RIGHT_MOUSE_BUTTON && trans.isDragTarget(event.getTargetId())){
			this.draggingStarted = true;
			this.dragPosition = this.transform.viewPointToWorld(event.getPosition());
			this.dragTargetId = event.getTargetId();
			this.dragFeatureTransaction = trans.clone(); // While dragging, a clone is painted

			dojo.publish(this.mapWidget.getRenderTopic(), [trans, "delete"]);
			dojo.publish(this.mapWidget.getRenderTopic(), [this.dragFeatureTransaction, "all"]);
		}
	},

	mouseMoved : function (/*HtmlMouseEvent*/event) {
		var trans = this.mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		if (trans != null) {
			if (this.inserting) {
				var geometry = trans.getNewFeatures()[0].getGeometry();
				this.updateDragLine(event, geometry);
			} else if (this.draggingStarted) {
				var index = this.dragFeatureTransaction.getPointIndexById(this.dragTargetId);
				var geometry = this.dragFeatureTransaction.getNewFeatures()[index[0]].getGeometry();
				this._getSnapper();
				var position = this.transform.viewPointToWorld(event.getPosition());
				if (this.snapper != null) {
					position = this.snapper.snap(position);
				}
				this.editor.edit(geometry, new SetCoordinateOperation(index[1], position));

				dojo.publish(this.mapWidget.getRenderTopic(), [this.dragFeatureTransaction, "delete"]);
				dojo.publish(this.mapWidget.getRenderTopic(), [this.dragFeatureTransaction, "all"]);
			}
		}
	},

	contextMenu : function (/*HtmlMouseEvent*/event) {
		this._configureMenu(event);
		 // Override the stop-propagation! We need a right mouse menu in this controller.
	},

	createDragLine : function (event) {
		if (this.dragLine == null) {
			var position = this._getPositionFromEvent(event);
			var lineString = this.factory.createLineString([position, position]);
			this.dragLine = new Line ();
			this.dragLine.setId("splitController.dragLine");
			this.dragLine.setGeometry(lineString);
			this.dragLine.setStyle(new ShapeStyle("#FFFFFF", "0", "#FF3322", "1", "1", "8,4", null));
			dojo.publish (this.mapWidget.getRenderTopic(), [this.dragLine, "all"]);
		}
	},

	updateDragLine : function (event, geometry) {
		if (this.dragLine == null) {
			this.createDragLine(event);
		}
		var position = this._getPositionFromEvent(event);
		var coords = geometry.getCoordinates();
		if (coords.length > 0){
			var c1 = this.transform.worldPointToView(coords[coords.length - 1]);
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

	refreshGeomInfo : function () {
		if (this.geomInfo != null) {
			var action = new ToggleSplitInfoAction(this.menuId+".info", this.mapWidget, this);
			action.actionPerformed(null);
			action.actionPerformed(null);
		}
	},

	/**
	 * @private
	 */
	_getPositionFromEvent : function (event) {
		this._getSnapper();
		if (this.snapper != null) {
			var temp = this.transform.viewPointToWorld(event.getPosition());
			temp = this.snapper.snap(temp);
			return this.transform.worldPointToView(temp);
		}
		return event.getPosition();
	},

	/**
	 * @private
	 */
	_getSnapper : function () {
		if (this.snapper == null) {
			var layer = this.mapWidget.getMapModel().getSelectedLayer();
			if (layer != null) {
				this.snapper = layer.getSnapper();
			} else if (this.selected != null){
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
		
		var action0 = new StartSplittingAction(this.menuId+".start", this.mapWidget);
		var item0 = new geomajas.gfx.menu.MenuItem({label:action0.getText(),action:action0});
		pMenu.addChild(item0);
   
		pMenu.addChild(new dijit.MenuSeparator()); //item 1

		var action2 = new UndoEditingAction(this.menuId+".undo", this.mapWidget, this);
		var item2 = new geomajas.gfx.menu.MenuItem({label:action2.getText(),action:action2});
		pMenu.addChild(item2);
		
		var action3 = new CancelSplittingAction(this.menuId+".cancel", this.mapWidget);
		var item3 = new geomajas.gfx.menu.MenuItem({label:action3.getText(),action:action3});
		pMenu.addChild(item3);

		var action4 = new SaveSplittingAction(this.menuId+".save", this.mapWidget);
		var item4 = new geomajas.gfx.menu.MenuItem({label:action4.getText(),action:action4});
		pMenu.addChild(item4);

		pMenu.addChild(new dijit.MenuSeparator()); //item 5	
		
		var action6 = new StopSplittingAction(this.menuId+".stop", this.mapWidget);
		var item6 = new geomajas.gfx.menu.MenuItem({label:action6.getText(),action:action6});
		pMenu.addChild(item6);
		
		pMenu.addChild(new dijit.MenuSeparator()); //item 7
		
		var action8 = new InsertPointAction(this.menuId+".insert", this.mapWidget, null, null);
		var item8 = new geomajas.gfx.menu.MenuItem({label:action8.getText(),action:action8});
		pMenu.addChild(item8);
		
		var action9 = new RemovePointAction(this.menuId+".remove", this.mapWidget, null);
		var item9 = new geomajas.gfx.menu.MenuItem({label:action9.getText(),action:action9});
		pMenu.addChild(item9);
					
		pMenu.addChild(new dijit.MenuSeparator()); //item 10

		var action11 = new ToggleSplitInfoAction(this.menuId+".info", this.mapWidget, this);
		var item11 = new geomajas.gfx.menu.MenuItem({label:action11.getText(),action:action11});
		pMenu.addChild(item11);
		
		return pMenu;
	},

	/**
	 * @private
	 */
	_configureMenu : function (event) {
		log.info(this.getName() + "._configureMenu(event) for targetId: " + event.getTargetId());

		//NOTE: pass the HtmlMouseEvent we get here to each of the menuitems.
		dojo.forEach(this._menu.getChildren(), function(child){ if(child.setOriginalEvent) {child.setOriginalEvent(event);} });

		//disable these unless specified otherwise later on
		this._menu.getChildren()[0].setDisabled(true);
		this._menu.getChildren()[2].setDisabled(true);
		this._menu.getChildren()[3].setDisabled(true);
		this._menu.getChildren()[4].setDisabled(true);
		this._menu.getChildren()[6].setDisabled(true);
		this._menu.getChildren()[8].setDisabled(true);
		this._menu.getChildren()[9].setDisabled(true);
		this._menu.getChildren()[11].setDisabled(true);

		var target = event.getTargetId();
		var editable = false;
		var selection = this.mapWidget.getMapModel().getSelection();
		if (selection.count == 1) {
			var layer = selection.item(0).getLayer();
			if (layer != null){
				editable = (layer.getEditPermissions().isCreatingAllowed() && layer.getEditPermissions().isDeletingAllowed());
			}
		}

		if (!this.splitting) { //enable toggle when not splitting
			if (editable) {
				this._menu.getChildren()[0].setDisabled(false); // start splitting
			}
		} else {
			this._menu.getChildren()[3].setDisabled(false);  // cancel
			this._menu.getChildren()[4].setDisabled(false);  // save
			this._menu.getChildren()[11].setDisabled(false); // toggle info

			if (this.inserting) {
				this._menu.getChildren()[6].setDisabled(false); // stop inserting
			}
		}
		
		var toggleInfo = new ToggleSplitInfoAction(this.menuId+".info", this.mapWidget, this);
		this._menu.getChildren()[11].label = toggleInfo.getText(); // toggle info

		var trans = this.mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		if (trans != null) {
			if (trans.getCommandStack().count > 0) {
				this._menu.getChildren()[2].setDisabled(false); // undo
			}
		}

		if (!this.inserting && trans != null) {
			var pointIndex = trans.getPointIndexById(target);

			// Check for insert point
			var isEdge = trans.isEdge(target);
			if (isEdge) {
				var position = this.transform.viewPointToWorld(event.getPosition());
				var action8 = new InsertPointAction(this.menuId+".insert", this.mapWidget, pointIndex, position);
				this._menu.getChildren()[8].action = action8; // insert point
				this._menu.getChildren()[8].setDisabled(false); // insert point
			}

			// Check for remove point:
			var lsIndex = trans.getLineStringIndexById(target);
			if (pointIndex != null && pointIndex[1].length > 0 && !isEdge) {

				// Do not allow removal of points when less then 4 points in geometry.
				var ls = trans.getNewFeatures()[lsIndex[0]].getGeometry().getGeometryN(lsIndex[1]);
				if (ls.getNumPoints() > 0 && trans.isVertex(target)) {
					var action9 = new RemovePointAction(this.menuId+".remove", this.mapWidget, pointIndex);
					this._menu.getChildren()[9].action = action9; // remove point
					this._menu.getChildren()[9].setDisabled(false);// remove point
				}
			}
		}
	}
});