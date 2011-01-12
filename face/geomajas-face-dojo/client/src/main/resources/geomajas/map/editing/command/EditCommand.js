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

dojo.provide("geomajas.map.editing.command.EditCommand");
dojo.declare("EditCommand", null, {

	/**
	 * @fileoverview Interface for possible editing actions.
	 * @class Features are edited through a command pattern. This interface
	 * defines the individual commands.
	 * @author Jan De Moerloose & Pieter De Graef
	 *
	 * @constructor
	 */
	constructor : function () {
	},
	
	/**
	 * General execution function of a single editing command. Needs to be
	 * implemented!
	 * @param feature Reference to the newFeature from the FeatureTransaction.
	 */
	execute : function (feature) {
	},
	
	/**
	 * Each editing command is held in a stack in the FeatureTransaction class.
	 * With this function it is always possible to undo this one command. Needs
	 * to be implemented! 
	 * @param feature Reference to the newFeature from the FeatureTransaction.
	 */
	undo : function (feature) {
	}

});
