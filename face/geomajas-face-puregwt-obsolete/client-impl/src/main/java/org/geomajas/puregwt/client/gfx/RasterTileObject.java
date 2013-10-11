/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client.gfx;

import org.geomajas.puregwt.client.map.render.VectorTilePresenter.TileView;

import com.google.gwt.core.client.Callback;

/**
 * TileView implementation for rasterized tiles. Basically this class is a simple widget that represents an HTML IMG tag
 * wherein a rasterized tile can be displayed. Note that this class is really a HtmlObject extension and as such can be
 * added to a HtmlContainer.
 * 
 * @author Pieter De Graef
 */
public class RasterTileObject extends HtmlImageImpl implements TileView {

	/**
	 * Create an RasterTileObject widget that represents an tile in a layer by means of an HTML IMG tag.
	 * 
	 * @param src
	 *            Pointer to the actual image.
	 * @param width
	 *            The width for this image, expressed in pixels.
	 * @param height
	 *            The height for this image, expressed in pixels.
	 * @param top
	 *            How many pixels should this image be placed from the top (relative to the parent origin).
	 * @param left
	 *            How many pixels should this image be placed from the left (relative to the parent origin).
	 * @param onLoadingDone
	 *            Call-back to be executed when the image finished loading, or when an error occurs while loading.
	 */
	public RasterTileObject(String src, int width, int height, int top, int left, 
			Callback<String, String> onLoadingDone) {
		super(src, width, height, top, left, onLoadingDone);
	}

	/**
	 * Sets the SRC property of the underlying IMG tag.
	 * 
	 * @param content
	 *            The URL to the actual image representing the tile.
	 */
	public void setContent(String content) {
		setSrc(content);
	}
}