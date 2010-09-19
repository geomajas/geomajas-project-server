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
