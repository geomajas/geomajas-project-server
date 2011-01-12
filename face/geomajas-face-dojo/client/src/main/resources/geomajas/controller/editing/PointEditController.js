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

dojo.provide("geomajas.controller.editing.PointEditController");
dojo.require("geomajas.event.MouseListener");
dojo.require("geomajas.action.menu.editing.CancelEditingAction");
dojo.require("geomajas.action.menu.editing.UndoEditingAction");
dojo.require("geomajas.action.menu.editing.SaveEditingAction");
dojo.require("geomajas.action.menu.editing.ToggleEditingModeAction");
dojo.require("geomajas.action.menu.editing.RemovePointAction");
dojo.require("geomajas.action.menu.editing.EditAttributesAction");

dojo.declare("PointEditController", MouseListener, {

	/**
	 * @fileoverview Mouselistener for editing Points.
	 * @class Editing controller designed for point geometries.
	 * @author Pieter De Graef
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
		this.menuId = "PointEditMenu";
		this._menu = this._getOrCreateMenu();
		log.info("PointEditController "+mapWidget.getMapModel());
		log.info("PointEditController " + mapWidget.getMapModel().getSRID());
		this.factory = new GeometryFactory(
			mapWidget.getMapModel().getSRID(),
			mapWidget.getMapModel().getPrecision());
		this.editor = new GeometryEditor();
	},

	getParent : function() {
		return this.parent;
	},

	getName : function () {
		return "PointEditController";
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
	 * new geometry or a new sub-geometry. Not much choice of course in point
	 * objects: this simply returns an empty array, so that the
	 * {@link Point#getGeometryN} would return the point itself.
	 */
	getStartIndex : function (point) {
		return [];
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
				var command = new AddCoordinateCommand (index, position);
				trans.execute (command);
				this.parent.setMode(this.parent.statics.DRAG_MODE);
				trans.getCommandStack().pop(); // since we immediatly change to DRAGMODE, it should not be possible to undo the creation of the point, because that would leave us with a deadlock.

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
			if (this.parent.isDragging() && this.draggingStarted) {
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

			var trans = this.mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();

			if (this.parent.isDragging() && this.draggingStarted) {
				this.draggingStarted = false;

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

					var command = new MoveCoordinateCommand (index[1], position);
					trans.execute (command);
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
	},

	updateDragLines : function (featureTransaction, event) {
	},

	removeDragLines : function () {
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

		pMenu.addChild(new dijit.MenuSeparator()); //item 3

		var action4 = new EditAttributesAction(this.menuId+".editAttributes", this.mapWidget, this.parent);
		var item4 = new geomajas.gfx.menu.MenuItem({label:action4.getText(),action:action4});
		pMenu.addChild(item4);

		return pMenu;
	},

	/**
	 * @private
	 */
	_configureMenu : function (event) {
		log.info(this.getName() + "._configureMenu(event) for targetId: " + event.getTargetId());
		
		var trans = this.mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		
		//NOTE: pass the HtmlMouseEvent we get here to each of the menuitems.
		dojo.forEach(this._menu.getChildren(), function(child){ if(child.setOriginalEvent) {child.setOriginalEvent(event);} });
		
		//disable these unless specified otherwise later on
		this._menu.getChildren()[0].setDisabled (true);
		if (this.parent.isDrawOnlyMode()) {
			this._menu.getChildren()[1].setDisabled(true); /* Save Feature action */
			this._menu.getChildren()[4].setDisabled(true); /* Edit attributes */
		}
		else {
			this._menu.getChildren()[1].setDisabled(false); /* Save Feature action */
			this._menu.getChildren()[4].setDisabled(false);	/* Edit attributes */
		}		
		
		if (trans.getCommandStack().count > 0) {
			this._menu.getChildren()[0].setDisabled(false);
		}
	}
});