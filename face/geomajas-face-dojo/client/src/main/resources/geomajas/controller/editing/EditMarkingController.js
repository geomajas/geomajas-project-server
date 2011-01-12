dojo.provide("geomajas.controller.editing.EditMarkingController");
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
