dojo.provide("geomajas.action.toolbar.EditTool");
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
dojo.require("geomajas.action.ToolbarTool");
dojo.require("geomajas.controller.editing.EditingController");
dojo.require("geomajas.controller.editing.EditMarkingController");

dojo.declare("EditTool", ToolbarTool, {

	/**
	 * @fileoverview Activate/Deactivate editing mode (DynamicToolbar).
	 * @class Tool that activates or deactivates editing mode.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends ToolbarTool
	 * @param id Unique identifier
	 * @param mapWidget Reference to the MapWidget object on which editing
	 *                  mode should be activated/deactivated.
	 * @param useMarkings Boolean that determines whether or not to use the
	 *                    orange border and question mark with the manual.
	 */
	constructor : function (id, mapWidget, useMarkings, useSnapHelp, allowHoles) {
		/** Unique identifier */
		this.id = id;

		/** This tool's image representation. A pencil. */
		this.image = "editIcon";

		/** Reference to the tooltip. */
		this.tooltip = this.tooltipLocale.EditTool;

		/** The controller used during editing mode. */
		this.controller = new EditingController(mapWidget, useSnapHelp, allowHoles);

		/** Should we use marking for help or not. */
		this.useMarkings = useMarkings;

		/** @private */
		this.mapWidget = mapWidget;

		/** @private */
		this.question = null;

		/** @private */
		this.line1 = null;

		/** @private */
		this.line2 = null;

		/** @private */
		this.line3 = null;

		/** @private */
		this.line4 = null;
	},

	/**
	 * When this tool is selected, activate editing mode. And perhaps also
	 * draw helping markings.
	 * @param event The activation event.
	 */
	onSelect : function (event) {
		this.selected = true;
		this.mapWidget.setController(this.controller);

		// check for existing FeatureTransaction:
		var ft = this.mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		if (ft != null) {
			dojo.publish(this.mapWidget.getRenderTopic(), [ft, "delete"]);
			dojo.publish(this.mapWidget.getRenderTopic(), [ft, "all"]);
		}

		if (this.useMarkings == "true") {
			this._makeVisible();
		}
	},

	/**
	 * When this tool is deselected, deactivate editing mode. Also remove the
	 * markings (if they were visible in the first place).
	 * @param event Deselection event.
	 */
	onDeSelect : function (event) {
		this.selected = false;

		// Cancel editing.
		var cancelAction = new CancelEditingAction ("editMenu.cancel", this.mapWidget, this.controller);
		cancelAction.actionPerformed(event);		

		this.mapWidget.setController(null);

		if (this.useMarkings == "true") {
			this._makeInvisible();
		}
	},

	/**
	 * Make the markings visible.
	 * @private
	 */
	_makeVisible : function () {
		var width = this.mapWidget.getMapView().getMapWidth();
		var height = this.mapWidget.getMapView().getMapHeight();
		var style = new ShapeStyle("#FFFFFF", "0", "#FF9900", "1", "2", null, null);

		this.question = new Picture("editMarking.Picture");
		this.question.setPosition(new Coordinate(1,1));
		this.question.setWidth(18);
		this.question.setHeight(18);
		this.question.setHref(dojo.moduleUrl("geomajas.widget", "html/images/question.gif"));
		dojo.publish(this.mapWidget.getRenderTopic(), [this.question, "all"]);

		var lineString1 = new LineString("editMarking.editLineString1");
		lineString1.appendCoordinate(new Coordinate(0, 0));
		lineString1.appendCoordinate(new Coordinate(width, 0));
		this.line1 = new Line("editMarking.editLine1");
		this.line1.setStyle(style);
		this.line1.setGeometry(lineString1);
		dojo.publish(this.mapWidget.getRenderTopic(), [this.line1, "all"]);

		var lineString2 = new LineString("editMarking.editLineString2");
		lineString2.appendCoordinate(new Coordinate(0, 0));
		lineString2.appendCoordinate(new Coordinate(0, height));
		this.line2 = new Line("editMarking.editLine2");
		this.line2.setStyle(style);
		this.line2.setGeometry(lineString2);
		dojo.publish(this.mapWidget.getRenderTopic(), [this.line2, "all"]);

		var lineString3 = new LineString("editMarking.editLineString3");
		lineString3.appendCoordinate(new Coordinate(width, height));
		lineString3.appendCoordinate(new Coordinate(width, 0));
		this.line3 = new Line("editMarking.editLine3");
		this.line3.setStyle(style);
		this.line3.setGeometry(lineString3);
		dojo.publish(this.mapWidget.getRenderTopic(), [this.line3, "all"]);

		var lineString4 = new LineString("editMarking.editLineString4");
		lineString4.appendCoordinate(new Coordinate(width, height));
		lineString4.appendCoordinate(new Coordinate(0, height));
		this.line4 = new Line("editMarking.editLine4");
		this.line4.setStyle(style);
		this.line4.setGeometry(lineString4);
		dojo.publish(this.mapWidget.getRenderTopic(), [this.line4, "all"]);

		this.mapWidget.attachListenerToElement("editMarking.Picture", new EditMarkingController(), true);
	},
	
	/**
	 * Make the markings invisible.
	 * @private
	 */
	_makeInvisible : function () {
		dojo.publish(this.mapWidget.getRenderTopic(), [this.question, "delete"]);
		dojo.publish(this.mapWidget.getRenderTopic(), [this.line1, "delete"]);
		dojo.publish(this.mapWidget.getRenderTopic(), [this.line2, "delete"]);
		dojo.publish(this.mapWidget.getRenderTopic(), [this.line3, "delete"]);
		dojo.publish(this.mapWidget.getRenderTopic(), [this.line4, "delete"]);
	},
	
	/**
	 * Setter for the markings boolean.
	 * @param useMarkings Should we use visible marking for help on
	 * activation of the editing mode?
	 */
	setUseMarkings : function (useMarkings) {
		this.useMarkings = useMarkings;
	}
});