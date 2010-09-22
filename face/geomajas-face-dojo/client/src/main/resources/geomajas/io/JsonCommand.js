dojo.provide("geomajas.io.JsonCommand");
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
dojo.declare("JsonCommand", null, {

	/**
	 * @fileoverview A command object for communication to the server through JSON.
	 * @class A command object for communication to the server through JSON.
	 * @author Pieter De Graef
	 *
	 * @constructor
     * @param cmdName Name of the command to invoke
	 * @param javaRequestClass The Java class name, that is to contain the command parameters.
	 * @param handler Javascript function that is supposed to handle the result
	 *                of the executed command.
	 * @param sync Boolean that determines whether or not this command should
	 *             be executed synchronously.
	 */
	constructor : function (cmdName, javaRequestClass, handler, sync) {
		this.cmdName = cmdName;
		this.javaRequestClass = javaRequestClass;
		this.handler = handler;
		this.sync = sync;

		this.params = {};
	},
	
	/**
	 * Return the command in a form that can be serialized through JSON.
	 */
	forSerialization : function () {
		var command = this.params;
		command["javaClass"] = this.javaRequestClass;
		return command;
	},
	
	/**
	 * TODO
	 */
	abort : function () {
		// TODO
	},
	
	// Command parameter functions:
	
	addParam : function (name, value) {
		this.params[name] = value;
	},
	
	getParam : function (name) {
		return this.params[name];
	},
	
	// Getters and setters:
	
	getJavaClass : function () {
		return this.javaClass;
	},
	
	setJavaClass : function (javaClass) {
		this.javaClass = javaClass;
	},

	getHandler : function () {
		return this.handler;
	},
	
	setHandler : function (handler) {
		this.handler = handler;
	},
	
	isSync : function () {
		return this.sync;
	},
	
	setSync : function (sync) {
		this.sync = sync;
	}

});
