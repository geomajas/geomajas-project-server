/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.rendering.painter.tile;

import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.VectorTile.VectorTileContentType;
import org.geomajas.rendering.RenderException;
import org.geomajas.rendering.image.RasterUrlBuilder;
import org.geomajas.rendering.painter.tile.TilePainter;

/**
 * <p>
 * TilePainter implementation for painting raster tiles. This painter is actually very simple. It will create a
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
					tile.setFeatureContent(urlBuilder.getImageUrl());
				}
				if (paintLabels) {
					urlBuilder.paintGeometries(false);
					urlBuilder.paintLabels(paintLabels);
					tile.setLabelContent(urlBuilder.getImageUrl());
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