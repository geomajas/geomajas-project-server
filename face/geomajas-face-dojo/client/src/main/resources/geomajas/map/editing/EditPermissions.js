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

dojo.provide("geomajas.map.editing.EditPermissions");
dojo.declare ("EditPermissions", null, {

	/**
	 * @fileoverview Config object determining CRUD permissions.
	 * @class Config object determining CRUD permissions.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 */
	constructor : function () {
		this.creatingAllowed = false;
		this.updatingAllowed = false;
		this.deletingAllowed = false;
	},

	fromJSON : function (json) {
		this.creatingAllowed = json.creatingAllowed;
		this.updatingAllowed = json.updatingAllowed;
		this.deletingAllowed = json.deletingAllowed;
	},

	// Getters and setters:

	isCreatingAllowed : function () {
		return this.creatingAllowed;
	},

	setCreatingAllowed : function (creatingAllowed) {
		this.creatingAllowed = creatingAllowed;
	},

	isUpdatingAllowed : function () {
		return this.updatingAllowed;
	},

	setUpdatingAllowed : function (updatingAllowed) {
		this.updatingAllowed = updatingAllowed;
	},

	isDeletingAllowed : function () {
		return this.deletingAllowed;
	},

	setDeletingAllowed : function (deletingAllowed) {
		this.deletingAllowed = deletingAllowed;
	}
});