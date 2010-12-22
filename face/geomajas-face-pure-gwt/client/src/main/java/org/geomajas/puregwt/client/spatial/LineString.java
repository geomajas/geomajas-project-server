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

package org.geomajas.puregwt.client.spatial;

import org.geomajas.geometry.Coordinate;

/**
 * LineString client-side GWT object.
 * 
 * @author Pieter De Graef
 */
public interface LineString extends Geometry {

	/**
	 * Is this line string closed or not? Closed means that the last point equals the first one. Typically
	 * {@link LineString} geometries are not closed, while {@link LinearRing} geometries are. For a {@link LinearRing}
	 * it is a necessity.
	 * 
	 * @return Returns true or false.
	 */
	boolean isClosed();

	/**
	 * Return the coordinate at the given index.
	 * 
	 * @param index
	 *            The index to search.
	 * @return Returns the coordinate, or null if no coordinate could be found at the given index.
	 */
	Coordinate getCoordinateN(int index);
}