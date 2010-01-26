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

dojo.provide("geomajas.map.editing.EditPermissions");
dojo.declare ("EditPermissions", null, {

	/**
	 * @fileoverview Config object determinging CRUD permissions.
	 * @class Config object determinging CRUD permissions.
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