dojo.provide("geomajas.io.JsonCommand");
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
