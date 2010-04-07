dojo.provide("geomajas.controller.editing.EditMarkingController");
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
dojo.require("geomajas.event.MouseListener");

dojo.declare("EditMarkingController", MouseListener, {

	/**
	 * @fileoverview Mouselistener for popping up the edit manual.
	 * @class Simple controller used during editmode. The EditTool class can
	 * make a questionmark appear in the upperleft corner of a map. By using
	 * this controller, it is possible to open a popup that shows the editing
	 * manual.
	 * @author Pieter De Graef
	 *
	 * @constructor 
	 * @extends MouseListener
	 */
	constructor : function () {
	},

	getName : function () {
		return "EditMarkingController";
	},
	
	onActivate : function () {
		log.info(this.getName() + ".onActivate()");
	},
	
	onDeactivate : function () {
		log.info(this.getName() + ".onDeactivate()");
	},
	
	/**
	 * Open a popup that shows the editing manual.
	 * @param event The HtmlMouseEvent from the mouseclick.
	 */
	mouseClicked : function (event) {
		this.popupWindow = window.open(geomajasConfig.serverBase+"/html/user_manual/index.html", 'Help', 'width=800,height=600,location=no,scrollbars=yes,menubar=no,toolbar=no');
	}
});
