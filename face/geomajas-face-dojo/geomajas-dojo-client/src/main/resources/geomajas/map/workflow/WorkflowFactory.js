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

dojo.provide("geomajas.map.workflow.WorkflowFactory");
dojo.declare("WorkflowFactory", null, {

	/**
	 * @fileoverview Interface for workflow factories.
	 * @class Interface for factories that are capable of creating
	 * {@link Workflow} objects. These workflow objects determines what happens
	 * to changes in the data, represented by a {@link FeatureTransaction}.
	 * @author Pieter De Graef
	 * @constructor
	 */
	constructor : function () {
		this.editingTopic = null;
	},

	/**
	 * The creation function. Creates a certain {@link Workflow} object.
	 * @param id Optional identifier.
	 */
	create : function (id) {
	},

	/**
	 * Set a topic on which the activities of the workflow can publish their
	 * state/progress. There might be other instances interested in this.
	 * @param editingTopic Name of the topic to publish progress on.
	 */
	setEditingTopic : function (editingTopic) {
		this.editingTopic = editingTopic;
	},

	/**
	 * Return the name of the editing topic.
	 */
	getEditingTopic : function () {
		return this.editingTopic;
	}
});