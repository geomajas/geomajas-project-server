dojo.provide("geomajas.action.menu.editing.StartEditingAction");
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
dojo.require("geomajas.action.Action");

dojo.declare("StartEditingAction", Action, {

	/**
	 * @fileoverview Start editing a feature (rightmouse menu).
	 * @class Start editing a feature.
	 * @author Pieter De Graef
	 *
	 * @constructor 
	 * @extends Action
	 * @param id This action's unique identifier.
	 * @param mapWidget Reference to a MapWidget object.
	 */
	constructor : function (id, mapWidget) {
		/** @private */
		this.mapWidget = mapWidget;

		/** Unique identifier */
		this.id = id;

		/** The action can be displayed as text only. */
		this.text = "Start editing";
	},

	/**
	 * If there is exactly one feature selected, it can be edited. This
	 * function calls {@link FeatureEditor#startEditing} to do that.
	 * @param event The {@link HtmlMouseEvent} from clicking this action.
	 */
	actionPerformed : function (event) {
		var selection = this.mapWidget.getMapModel().getSelection();
		if (selection.count == 1) {
			var feature = selection.item(0).clone();
			if(feature.isClipped()){
				alert("Clipped features cannot be edited, try zooming out first");
			} else {
				var ft = this.mapWidget.getMapModel().getFeatureEditor().startEditing([feature], [feature]);
				dojo.publish(this.mapWidget.getRenderTopic(), [ft, "all"]);

				curCon = this.mapWidget.getCurrentController();
				if (curCon == null || !(curCon instanceof  EditingController)) {
					curCon = new EditingController (this.mapWidget)
					this.mapWidget.setController(curCon);
				}
				curCon.setMode(curCon.statics.DRAG_MODE);
				// seems to work for all geom types ???
				curCon.setGeometryIndex([0]);
			}
		} else {
			alert("Er moet exact 1 feature geselecteerd zijn.");
		}
	}
});
