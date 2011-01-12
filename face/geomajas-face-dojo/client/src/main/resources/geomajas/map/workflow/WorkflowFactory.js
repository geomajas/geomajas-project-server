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