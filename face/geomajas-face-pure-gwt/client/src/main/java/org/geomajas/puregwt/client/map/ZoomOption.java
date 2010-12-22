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

package org.geomajas.puregwt.client.map;

/** 
 * Zoom options. These express the different ways to zoom in and out on a map.
 *
 * @author Jan De Moerloose
 */
public enum ZoomOption {

	/** Zoom exactly to the new scale. This is only possible if no resolutions have been defined on the map. */
	EXACT,

	/**
	 * Zoom to a scale level that is different from the current (lower or higher according to the new scale, only if
	 * allowed of course).
	 */
	LEVEL_CHANGE,

	/** Zoom to a scale level that is as close as possible to the new scale. */
	LEVEL_CLOSEST,

	/** Zoom to a scale level that makes the bounds fit inside our view. */
	LEVEL_FIT
}