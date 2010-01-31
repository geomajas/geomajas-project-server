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

package org.geomajas.service;

import org.geomajas.geometry.Geometry;
import org.geomajas.layer.feature.Feature;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.rendering.tile.InternalTile;
import org.geomajas.rendering.tile.Tile;

/**
 * Converts between DTOs and server-side objects.
 *
 * @author Jan De Moerloose
 */
public interface DtoConverter {

	/**
	 * Convert the server side feature to a DTO feature that can be sent to the client.
	 *
	 * @param feature
	 *            The server-side feature representation.
	 * @return Returns the DTO feature.
	 */
	Feature toDto(InternalFeature feature);

	/**
	 * Convert a DTO feature to a server-side feature.
	 *
	 * @param dto
	 *            The DTO feature that comes from the client.
	 * @return Returns a server-side feature object.
	 */
	InternalFeature toFeature(Feature dto);

	/**
	 * Takes in a JTS geometry, and creates a new DTO geometry from it.
	 *
	 * @param geometry
	 *            The geometry to convert into a DTO geometry.
	 * @return Returns a DTO type geometry, that is serializable.
	 */
	Geometry toDto(com.vividsolutions.jts.geom.Geometry geometry);

	/**
	 * Takes in a DTO geometry, and converts it into a JTS geometry.
	 *
	 * @param geometry
	 *            The DTO geometry to convert into a JTS geometry.
	 * @return Returns a JTS geometry.
	 */
	com.vividsolutions.jts.geom.Geometry toJts(Geometry geometry);

	/**
	 * Convert a server-side tile representations into a DTO tile.
	 *
	 * @param tile
	 *            The server-side representation of a tile.
	 * @return Returns the DTO version that can be sent to the client.
	 */
	Tile toDto(InternalTile tile);
}
