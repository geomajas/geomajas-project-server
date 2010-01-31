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

package org.geomajas.internal.service;

import org.geomajas.geometry.Geometry;
import org.geomajas.layer.feature.Feature;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.rendering.tile.InternalTile;
import org.geomajas.rendering.tile.Tile;
import org.geomajas.service.DtoConverter;
import org.geomajas.service.FeatureConverter;
import org.geomajas.service.GeometryConverter;
import org.geomajas.service.TileConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Default implementation of DTO converter.
 *
 * @author Jan De Moerloose
 */
@Component()
public class DtoConverterImpl implements DtoConverter {

	@Autowired
	private FeatureConverter featureConverter;

	@Autowired
	private GeometryConverter geometryConverter;

	@Autowired
	private TileConverter tileConverter;

	public Feature toDto(InternalFeature feature) {
		return featureConverter.toDto(feature);
	}

	public InternalFeature toFeature(Feature dto) {
		return featureConverter.toFeature(dto);
	}

	public Geometry toDto(com.vividsolutions.jts.geom.Geometry geometry) {
		return geometryConverter.toDto(geometry);
	}

	public Tile toDto(InternalTile tile) {
		return tileConverter.toDto(tile);
	}

	public com.vividsolutions.jts.geom.Geometry toJts(Geometry geometry) {
		return geometryConverter.toJts(geometry);
	}

}
