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