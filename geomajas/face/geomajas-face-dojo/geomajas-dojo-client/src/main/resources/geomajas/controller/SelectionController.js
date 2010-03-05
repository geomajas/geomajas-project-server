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

dojo.provide("geomajas.controller.SelectionController");
dojo.require("geomajas.event.MouseListener");
dojo.require("geomajas.spatial.transform.WorldViewTransformation");
dojo.require("geomajas.action.menu.ToggleSelectionAction");
dojo.require("geomajas.action.menu.SelectionAction");
dojo.require("geomajas.action.menu.editing.StartEditingAction");
dojo.require("geomajas.action.menu.editing.DeleteFeatureAction");
dojo.require("geomajas.action.toolbar.DeselectAllAction");

dojo.require("geomajas.gfx.menu.Menu");

dojo.declare("SelectionController", MouseListener, {

	/**
	 * @fileoverview MouseListener for selection.
	 * @class MouseListener implementation that selects/deselects the
	 * object clicked upon. Selection when clicking happens through the 
	 * SelectionAction, via the menu the selection can be toggled via the ToggleSelectionAction.
	 * @author An Buyle
	 *
	 * @constructor
	 * @extends MouseListener
	 * @param mapWidget Reference to the MapWidget on which we can select.
	 * @param supportEditing Should the rightmouse menu support editing?
	 * @param selectFromActiveLayerOnly Should  only features from the active layer be selectable?
	 * @param minInsideFactor	minimum part of feature geometry (area) that has to be inside the
	 * 							selection rectangle to select/deselect it (default 0.7 ==> 70%)  
	 * @paran pixelTolerance Number of pixels that describes the tolerance allowed when trying to select features
	 */
	constructor : function (mapWidget, supportEditing, selectFromActiveLayerOnly, minInsideFactor, pixelTolerance) {
		/** Reference to the MapWidget on which we can select. */
		this.mapWidget = mapWidget; // toch beter mapmodel???

		this.menuId = "selectionMenu";
		this._menu = this._getOrCreateMenu();

		if (supportEditing) {
			this.supportEditing = supportEditing;
		} else {
			this.supportEditing = false;
		}
		if (selectFromActiveLayerOnly) {
			this.selectFromActiveLayerOnly = selectFromActiveLayerOnly;
		}
		else {	
			this.selectFromActiveLayerOnly = false;
		}
		if (minInsideFactor) {
			this.minInsideFactor = minInsideFactor;  
		} else {
			this.minInsideFactor = 0.7;  
		}

		if (pixelTolerance) {
			this.pixelTolerance = pixelTolerance;
		} else {
			this.pixelTolerance = 5;
		}

		/** @private Rectangle paintable object that draws a rectangle. */
		this.rectangle = null;

		/** @private Holds the dragging status. */
		this.dragging = false;

		/** @private */
		this.begin = null;

		/** @private */
		this.bounds = null;

		/** @private */
		this.shift = false;
		this.ctrl = false;

		/** @private */
		this.style = new ShapeStyle("#66AA22", "0.3", "#668822", "0.8", "2", null, null);
	},

	/**
	 * Return a unique name.
	 */
	getName : function () {
		return "SelectionController";
	},

	/**
	 * Initialize this controller on activation.
	 */
	onActivate : function () {
		log.info(this.getName() + ".onActivate()");
		//bind the context menu to the map
		this._menu.bindDomNode(this.mapWidget.id);
	},
	
	/**
	 * cleanup this controller on deactivation.
	 */
	onDeactivate : function () {
		log.info(this.getName() + ".onDeactivate()");
		//remove the context menu from the map
		this._menu.unBindDomNode(this.mapWidget.id);
	},
	
	/**
	 * First mouse button: publish a toggle event on the selection topic for
	 * the referenced MapWidget. Second mouse button opens a menu.
	 * @param event A HtmlMouseEvent object.
	 */
	mouseClicked : function (/*HtmlMouseEvent*/event) {
		log.debug(this.getName() + ".mouseClicked()");
		if (event.getButton() != event.statics.RIGHT_MOUSE_BUTTON) {
			if (this._menu.isShowingNow) {
				dijit.popup.close(this._menu);
			}
			var action = new SelectionAction("selectionController.selection", this.mapWidget,
								this.selectFromActiveLayerOnly, this.pixelTolerance);
			action.actionPerformed(event);
		}
	},

	mousePressed : function (/*HtmlMouseEvent*/event) {
		if (event.getButton() != event.statics.RIGHT_MOUSE_BUTTON) {
			this.dragging = true;
			this.begin = event.getPosition();
			this.bounds = null;
			this.shift = event.isShiftDown();
			this.ctrl = event.isCtrlDown();

			this.rectangle = new Rectangle ("selectionRectangle");
			this.rectangle.setStyle (this.style);
			this.rectangle.setPosition (this.begin);

			dojo.publish (this.mapWidget.getRenderTopic(), [this.rectangle, "all"]);
		}
	},

	mouseReleased : function (/*HtmlMouseEvent*/event) {
		if (this.dragging) { // It is possible to MouseDown somewhere outside the SVG, and MouseUp here...
			this.dragging = false;
			this._updateRectangle(event);

			if (!this.begin.equals(event.getPosition())) {
				var layer = this.mapWidget.getMapModel().getSelectedLayer();

				if (layer != null) {
					if (!this.ctrl && !this.shift) {
						dojo.publish(this.mapWidget.getMapModel().getSelectionTopic(), [ "deselectAll", null ]);
					}
					var viewBounds = new Bbox(this.rectangle.getPosition().getX(), this.rectangle.getPosition().getY(), this.rectangle.getWidth(), this.rectangle.getHeight());
					var trans = new WorldViewTransformation(this.mapWidget.getMapView());
					this.bounds = trans.viewBoundsToWorld(viewBounds);
					this._searchFeatures ();
				}
			}

			if (this.rectangle != null) {
				dojo.publish (this.mapWidget.getRenderTopic(), [this.rectangle, "delete"]);
			}
			this.rectangle = null;
		}
	},

	_searchFeatures : function () {
		var factory = new GeometryFactory(31300, null);
		var linearRing = factory.createLinearRing (this.bounds.getCoordinates());
		var polygon = factory.createPolygon (linearRing, null);

		var command = new JsonCommand("command.feature.SearchByLocation",
                "org.geomajas.command.dto.SearchByLocationRequest", null, false);
		command.addParam ("layerIds", [this.mapWidget.getMapModel().getSelectedLayer().getLayerId()]);
		command.addParam ("searchType", 1);
		command.addParam ("location", polygon);
		command.addParam ("queryType", 1);
		command.addParam ("ratio", this.minInsideFactor);
        command.addParam ("crs", this.mapWidget.getMapModel().crs);
		command.addParam("featureIncludes", geomajasConfig.lazyFeatureIncludesSelect);
		var deferred = geomajasConfig["dispatcher"].execute(command);
		deferred.addCallback(this, "_onGetByLocation");
	},

	_onGetByLocation : function (result) {
		if (result != null && result.featureMap != null && result.featureMap.map != null) {
			var layer = this.mapWidget.getMapModel().getSelectedLayer();
			for (var key in result.featureMap.map) { // For every layer....should be only one.
				var featureArray = result.featureMap.map[key].list;
				for (var i = 0; i< featureArray.length; i++) {
					var feature = new Feature();
					feature.setLayer(layer);
					feature.fromJSON(featureArray[i]);

		
					if (!this.ctrl && !this.shift) { /* Select feature */
						if (!feature.isSelected()) {
							dojo.publish(this.mapWidget.getMapModel().getSelectionTopic(), [ "select", feature ]);
						}
					}
					else { /* Toggle selection of  feature */
						dojo.publish(this.mapWidget.getMapModel().getSelectionTopic(), [ "toggle", feature ]);
					}
				}
			}
		}
	},

	/**
	 * If we're dragging, update the rectangle.
	 * @param event A HtmlMouseEvent object.
	 */
	mouseMoved : function (/*HtmlMouseEvent*/event) {
		if (this.dragging) {
			this._updateRectangle(event);

			dojo.publish (this.mapWidget.getRenderTopic(), [this.rectangle, "all"]);
		}
	},

	contextMenu : function (/*HtmlMouseEvent*/event) {
		this._configureMenu(event);
		// Override the stop-propagation! We need a right mouse menu in this controller.
	},

	/**
	 * @private
	 */
	_configureMenu : function (event) {
		log.info(this.getName() + "._configureMenu(event) for targetId: " + event.getTargetId());
		
		//NOTE: pass the HtmlMouseEvent we get here to each of the menuitems.
		dojo.forEach(this._menu.getChildren(), function(child){ if(child.setOriginalEvent) {child.setOriginalEvent(event);} });
			
		//NOTE: this.supportediting is set after menu construction!!!
		//we need to find a way to hide/show menuitems WITHOUT removing them from the menu.	
		if (this.supportEditing) 
		{
			this._menu.getChildren()[0].style.height = 0;
			this._menu.getChildren()[1].style.visibility = "visible";
			
	      	var target = event.getTargetId();

			//if (this.mapWidget.getMapModel().isFeatureSelected(target) && editable) 
			// Feature doesn't need to be selected, just selectable i.e. the feature must exist!
			if (this.mapWidget.getMapModel().getFeatureById(target) != null)
			{
				this._menu.getChildren()[0].setDisabled(false);   
				this._menu.getChildren()[1].setDisabled(false);    
	      	}
	      	else
	      	{
	      		this._menu.getChildren()[0].setDisabled(true);   
				this._menu.getChildren()[1].setDisabled(false);
	      	}
      	
		}
		else
		{
			this._menu.getChildren()[0].style.visibility = "hidden";
			this._menu.getChildren()[1].style.visibility = "hidden";
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

		var action1 = new ToggleSelectionAction(this.menuId+".toggle", this.mapWidget, false /*singleSelection*/, 
					this.selectFromActiveLayerOnly);
		var item1 = new geomajas.gfx.menu.MenuItem({label:action1.getText(),action:action1});
		pMenu.addChild(item1);

		var action4 = new DeselectAllAction(this.menuId+".deselectall", this.mapWidget.getMapModel().getSelectionTopic());
        var item4 = new geomajas.gfx.menu.MenuItem({label:action4.getText(),action:action4});
       	pMenu.addChild(item4);
		
		return pMenu;
	},

	/**
	 * @private
	 */
	_updateRectangle : function (event) {
		var end = event.getPosition();

		var x = this.begin.getX();
		var y = this.begin.getY();
		var width = end.getX() - x;
		var height = end.getY() - y;
		if (x > end.getX()) {
			x = end.getX();
			width = -width;
		}
		if (y > end.getY()) {
			y = end.getY();
			height = -height;
		}

		this.rectangle.setPosition (new Coordinate(x, y));
		this.rectangle.setWidth (width);
		this.rectangle.setHeight (height);
	}
});
