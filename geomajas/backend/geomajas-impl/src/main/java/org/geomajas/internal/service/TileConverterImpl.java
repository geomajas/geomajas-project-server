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

import org.geomajas.internal.application.tile.RasterTileJG;
import org.geomajas.internal.application.tile.VectorTileJG;
import org.geomajas.layer.feature.Feature;
import org.geomajas.layer.feature.RenderedFeature;
import org.geomajas.rendering.tile.RasterTile;
import org.geomajas.rendering.tile.RenderedTile;
import org.geomajas.rendering.tile.Tile;
import org.geomajas.rendering.tile.VectorTile;
import org.geomajas.service.FeatureConverter;
import org.geomajas.service.TileConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Converter that can transform server-side tile representations into DTO tiles. These DTO versions can be sent to the
 * client.
 *
 * @author Pieter De Graef
 */
@Component
public final class TileConverterImpl implements TileConverter {

	@Autowired
	private FeatureConverter featureConverter;

	/**
	 * Convert a server-side tile representations into a DTO tile.
	 *
	 * @param tile
	 *            The server-side representation of a tile.
	 * @return Returns the DTO version that can be sent to the client.
	 */
	public Tile toDto(RenderedTile tile) {
		if (tile != null) {
			if (tile instanceof VectorTileJG) {
				VectorTileJG vTile = (VectorTileJG) tile;
				VectorTile dto = new VectorTile();
				updateBasicTile(tile, dto);
				dto.setFeatureFragment(vTile.getFeatureFragment());
				dto.setLabelFragment(vTile.getLabelFragment());
				return dto;
			} else if (tile instanceof RasterTileJG) {
				RasterTileJG rTile = (RasterTileJG) tile;
				RasterTile dto = new RasterTile();
				updateBasicTile(tile, dto);
				dto.setFeatureImage(rTile.getFeatureImage());
				dto.setLabelImage(rTile.getLabelImage());
				return dto;
			} else {
				Tile dto = new Tile();
				updateBasicTile(tile, dto);
				return dto;
			}
		}
		return null;
	}

	// Private methods:

	private void updateBasicTile(RenderedTile source, Tile target) {
		target.setClipped(source.isClipped());
		target.setCode(source.getCode());
		target.setCodes(source.getCodes());
		target.setScreenHeight(source.getScreenHeight());
		target.setScreenWidth(source.getScreenWidth());
		target.setTileHeight(source.getTileHeight());
		target.setTileWidth(source.getTileWidth());
		List<Feature> features = new ArrayList<Feature>();
		for (RenderedFeature feature : source.getFeatures()) {
			features.add(featureConverter.toDto(feature));
		}
		target.setFeatures(features);
	}
}
