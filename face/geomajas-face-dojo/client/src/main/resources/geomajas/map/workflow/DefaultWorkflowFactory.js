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

dojo.provide("geomajas.map.workflow.DefaultWorkflowFactory");
dojo.require("geomajas.map.workflow.WorkflowFactory");

dojo.declare("DefaultWorkflowFactory", WorkflowFactory, {

	/**
	 * @fileoverview Default implementation for creating workflow objects.
	 * @class Default implementation of the {@link WorkflowFactory} interface.
	 * This factory creates {@link Workflow} object with 2 activities. These
	 * are a validation step, followed by a persistence step.
	 * @author Pieter De Graef
	 * @constructor
	 * @extends WorkflowFactory
	 * @param dispatcher Reference to the {@link CommandDispatcher}.
	 */
	constructor : function (dispatcher) {
		this.dispatcher = dispatcher;
	},

	/**
	 * Creates a {@link Workflow} object with 2 activities. These are a
	 * validation step, followed by a persistence step.
	 * @param id Not used.
	 */
	create : function (id) {
		var workflow = new Workflow();

		var activity1 = new LayerValidationActivity (this.editingTopic);
		activity1.setParent(workflow);
//		var activity2 = new EditAttributesActivity (this.dispatcher);
//		activity2.setParent(workflow);
		var activity3 = new CommitActivity (this.editingTopic, this.dispatcher);
		activity3.setParent(workflow);
		workflow.setSubActivities([activity1, activity3]);

		return workflow;
	}
});