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

import org.geomajas.configuration.AttributeInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Geometry;
import org.geomajas.global.Api;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.Feature;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.VectorTile;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Converts between DTOs and server-side objects.
 * 
 * @author Jan De Moerloose
 * @author Joachim Van der Auwera
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
public interface DtoConverterService {

	// -------------------------------------------------------------------------
	// Attribute conversion:
	// -------------------------------------------------------------------------

	/**
	 * Converts a DTO attribute into a generic attribute object.
	 * 
	 * @param attribute
	 *            The DTO attribute.
	 * @return The server side attribute representation. As we don't know at this point what kind of object the
	 *         attribute is (that's a problem for the <code>FeatureModel</code>), we return an <code>Object</code>.
	 * @throws GeomajasException conversion failed
	 */
	Object toInternal(Attribute<?> attribute) throws GeomajasException;

	/**
	 * Converts a server-side attribute object into a DTO attribute.
	 * 
	 * @param object
	 *            The attribute value.
	 * @param info
	 *            The attribute definition from the configuration.
	 * @return Returns a DTO attribute.
	 * @throws GeomajasException conversion failed
	 */
	Attribute<?> toDto(Object object, AttributeInfo info) throws GeomajasException;

	// -------------------------------------------------------------------------
	// Feature conversion:
	// -------------------------------------------------------------------------

	/**
	 * Convert the server side feature to a DTO feature that can be sent to the client.
	 * 
	 * @param feature
	 *            The server-side feature representation.
	 * @param featureIncludes
	 *            Indicate which aspects of the should be included @see {@link org.geomajas.layer.VectorLayerService}
	 * @return Returns the DTO feature.
	 * @throws GeomajasException conversion failed
	 */
	Feature toDto(InternalFeature feature, int featureIncludes) throws GeomajasException;

	/**
	 * Convert the server side feature to a DTO feature that can be sent to the client.
	 * <p/>
	 * All data which is contained in the InternalFeature will be included.
	 *
	 * @param feature
	 *            The server-side feature representation.
	 * @return Returns the DTO feature.
	 * @throws GeomajasException conversion failed
	 */
	Feature toDto(InternalFeature feature) throws GeomajasException;

	/**
	 * Convert a DTO feature to a server-side feature.
	 * 
	 * @param dto
	 *            The DTO feature that comes from the client.
	 * @return Returns a server-side feature object.
	 * @throws GeomajasException conversion failed
	 */
	InternalFeature toInternal(Feature dto) throws GeomajasException;

	// -------------------------------------------------------------------------
	// Geometry conversion:
	// -------------------------------------------------------------------------

	/**
	 * Takes in a JTS geometry, and creates a new DTO geometry from it.
	 * 
	 * @param geometry
	 *            The geometry to convert into a DTO geometry.
	 * @return Returns a DTO type geometry, that is serializable.
	 * @throws GeomajasException conversion failed
	 */
	Geometry toDto(com.vividsolutions.jts.geom.Geometry geometry) throws GeomajasException;

	/**
	 * Takes in a DTO geometry, and converts it into a JTS geometry.
	 * 
	 * @param geometry
	 *            The DTO geometry to convert into a JTS geometry.
	 * @return Returns a JTS geometry.
	 * @throws GeomajasException conversion failed
	 */
	com.vividsolutions.jts.geom.Geometry toInternal(Geometry geometry) throws GeomajasException;

	// -------------------------------------------------------------------------
	// Tile conversion:
	// -------------------------------------------------------------------------

	/**
	 * Convert a server-side tile representations into a DTO tile.
	 * 
	 * @param tile
	 *            The server-side representation of a tile.
	 * @param crs
	 *            crs code to include in the features (can be null)
	 * @param featureIncludes
	 *            Indicate which aspects of the should be included @see {@link org.geomajas.layer.VectorLayerService}
	 * @return Returns the DTO version that can be sent to the client.
	 * @throws GeomajasException conversion failed
	 */
	VectorTile toDto(InternalTile tile, String crs, int featureIncludes) throws GeomajasException;

	/**
	 * Convert a server-side tile representations into a DTO tile.
	 * <p/>
	 * All data which is contained in the InternalFeature will be included.
	 *
	 * @param tile
	 *            The server-side representation of a tile.
	 * @return Returns the DTO version that can be sent to the client.
	 * @throws GeomajasException conversion failed
	 */
	VectorTile toDto(InternalTile tile) throws GeomajasException;

	// -------------------------------------------------------------------------
	// Bounding box conversion:
	// -------------------------------------------------------------------------

	/**
	 * Convert a bbox to a JTS envelope.
	 * 
	 * @param bbox
	 *            Geomajas bbox
	 * @return JTS envelope
	 */
	Envelope toInternal(Bbox bbox);

	/**
	 * Convert JTS envelope into a bbox.
	 * 
	 * @param envelope
	 *            JTS envelope
	 * @return Geomajas bbox
	 */
	Bbox toDto(Envelope envelope);
}
