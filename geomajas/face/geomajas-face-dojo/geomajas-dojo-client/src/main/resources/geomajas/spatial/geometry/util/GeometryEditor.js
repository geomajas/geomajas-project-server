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

dojo.provide("geomajas.spatial.geometry.util.GeometryEditor");
dojo.declare("GeometryEditor", null, {

	/**
	 * @fileoverview General editor for geometries.
	 * @class General editor for geometries. Uses geometry operations to do
	 * it's work. Don't try to adjust geometries without this class and these
	 * operations!
	 * @author Pieter De Graef
	 * @constructor
	 */
	constructor : function () {
	},

	/**
	 * The actual editing of a geometry by a certain operation.
	 * @param geometry A {@link Geometry} object.
	 * @param operation A {@link GeometryOperation} object.
	 */
	edit : function (geometry, operation) {
		if (geometry == null) {
			log.warn("Geometry operation could not start: geometry was null.");
			return;
		}
		try {
			operation.edit(geometry);
		} catch (e) {
			log.error ("Error in geometry operation, "+operation.declaredClass);
		}
	}
});