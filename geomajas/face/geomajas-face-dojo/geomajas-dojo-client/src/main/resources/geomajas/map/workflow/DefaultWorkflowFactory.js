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