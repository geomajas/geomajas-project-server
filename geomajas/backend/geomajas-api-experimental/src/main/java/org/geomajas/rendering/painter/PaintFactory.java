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

package org.geomajas.rendering.painter;

import org.geomajas.layer.VectorLayer;
import org.geomajas.rendering.image.TileImageCreator;
import org.geomajas.rendering.painter.tile.TilePainter;
import org.geomajas.rendering.tile.InternalTile;
import org.geomajas.rendering.tile.TileCode;
import org.geomajas.rendering.tile.UrlTile;

/**
 * Allows creation of painter related objects.
 *
 * @author Joachim Van der Auwera
 */
public interface PaintFactory {

	/**
	 * Create a {@link LayerPaintContext} for a layer.
	 *
	 * @param layer layer to create context for
	 * @return layer paint context
	 */
	LayerPaintContext createLayerPaintContext(VectorLayer layer);

	/**
	 * Create a {@link TileImageCreator} object.
	 *
	 * @param tile tile to create object for
	 * @param transparent transparent status
	 * @return {@link TileImageCreator} object
	 */
	TileImageCreator createTileImageCreator(InternalTile tile, boolean transparent);

	/**
	 * Create a {@link TilePainter} for painting raster tiles.
	 *
	 * @param layerId layer id
	 * @return {@link TilePainter}
	 */
	TilePainter createRasterTilePainter(String layerId);

	/**
	 * Create a vector tile which contains the url to the rendered image.
	 *
	 * @param code tile code
	 * @param layer (vector) layer
	 * @param scale scale
	 * @return tile which contains the image url
	 */
	UrlTile createRasterTile(TileCode code, VectorLayer layer, double scale);
}
