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

dojo.provide("geomajas.map.workflow.WorkflowHandler");
dojo.require("geomajas.map.workflow.WorkflowFactory");
dojo.require("dojox.collections.Dictionary");

dojo.declare("WorkflowHandler", null, {

	/**
	 * @fileoverview Entity overseeing the workflow.
	 * @class Entity overseeing the workflow. Should simply be initialized with
	 * a {@link WorkflowFactory}. It is the factory that creates the workflow
	 * objects, and so determines what activities it consists of. What this 
	 * class does, is to start the workflow. Once it is busy, it can handle
	 * itself.
	 * 
	 * @author Pieter De Graef
	 * @constructor
	 * @param factory A {@link WorkflowFactory} object.
	 */
	constructor : function (factory) {
		this.factory = factory;
		this.workflowMap = new dojox.collections.Dictionary();
	},

	/**
	 * Start the workflow with a new {@link FeatureTransaction} object. It is
	 * this featureTransaction that determines what changes have been made, and
	 * the workflow will then handle these changes. Usualy this means
	 * persisting them to a database.
	 * @param featureTransaction A {@link FeatureTransaction} object.
	 */
	startWorkflow : function (featureTransaction) {
		var workflow = this.factory.create("someId");
		workflow.setFeatureTransaction(featureTransaction);
		return workflow;
	},

	/**
	 * Return the {@link WorkflowFactory}.
	 */
	getFactory : function () {
		return this.factory;
	},

	/**
	 * Set a new {@link WorkflowFactory}.
	 * @param factory A {@link WorkflowFactory} object.
	 */
	setFactory : function (factory) {
		this.factory = factory;
	}
});