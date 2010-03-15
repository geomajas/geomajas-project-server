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

package org.geomajas.internal.rendering.painter.tile;

import org.geomajas.internal.layer.tile.InternalTileImpl;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.VectorTile.VectorTileContentType;
import org.geomajas.rendering.RenderException;
import org.geomajas.rendering.image.RasterUrlBuilder;
import org.geomajas.rendering.painter.tile.TilePainter;

/**
 * <p>
 * TilePainter implementation for painting raster tiles. This painter is actually very simple. It will simply create a
 * URL where the actual image can be found. To do this, it uses the {@link RasterUrlBuilder} contained within the tile.
 * One condition for this to work is that the <code>RenderedTile</code> is an instance of {@link InternalTile},
 * and that this raster tile's {@link RasterUrlBuilder} is not null!
 * </p>
 * 
 * @author Pieter De Graef
 */
public class UrlContentTilePainter implements TilePainter {

	/**
	 * Should this painter paint a feature's geometries or not?
	 */
	private boolean paintGeometries = true;

	/**
	 * Should this painter paint a feature's labels or not?
	 */
	private boolean paintLabels;

	private RasterUrlBuilder urlBuilder;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Constructor setting the layer's ID.
	 * 
	 * @param urlBuilder
	 *            Creates URL's to where the actual rendering of tiles can be found.
	 */
	public UrlContentTilePainter(RasterUrlBuilder urlBuilder) {
		this.urlBuilder = urlBuilder;
	}

	// -------------------------------------------------------------------------
	// TilePainter implementation:
	// -------------------------------------------------------------------------

	/**
	 * Painter the tile, by building a URL where the image can be found.
	 * 
	 * @param tile
	 *            Must be an instance of {@link InternalTile}, and must have a non-null {@link RasterUrlBuilder}.
	 * @return Returns a {@link InternalTile}.
	 */
	public InternalTile paint(InternalTile tile) throws RenderException {
		if (tile.getContentType().equals(VectorTileContentType.URL_CONTENT)) {
			if (urlBuilder != null) {
				if (paintGeometries) {
					urlBuilder.paintGeometries(paintGeometries);
					urlBuilder.paintLabels(false);
					((InternalTileImpl) tile).setFeatureContent(urlBuilder.getImageUrl());
				}
				if (paintLabels) {
					urlBuilder.paintGeometries(false);
					urlBuilder.paintLabels(paintLabels);
					((InternalTileImpl) tile).setLabelContent(urlBuilder.getImageUrl());
				}
				return tile;
			}
		}
		return tile;
	}

	/**
	 * Enables or disabled the use of painter that paint the geometries of the features in the tile.
	 * 
	 * @param paintGeometries
	 *            true or false.
	 */
	public void setPaintGeometries(boolean paintGeometries) {
		this.paintGeometries = paintGeometries;
	}

	/**
	 * Enables or disabled the use of painter that paint the labels of the features in the tile.
	 * 
	 * @param paintLabels
	 *            true or false.
	 */
	public void setPaintLabels(boolean paintLabels) {
		this.paintLabels = paintLabels;
	}
}