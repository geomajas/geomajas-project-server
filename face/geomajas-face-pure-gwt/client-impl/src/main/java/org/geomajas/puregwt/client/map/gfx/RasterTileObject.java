/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client.map.gfx;

import org.geomajas.puregwt.client.map.render.VectorTilePresenter.TileView;

import com.google.gwt.user.client.DOM;

/**
 * TileView implementation for rasterized tiles. Basically this class is a simple widget that represents an HTML IMG tag
 * wherein a rasterized tile can be displayed. Note that this class is really a HtmlObject extension and as such can be
 * added to a HtmlContainer.
 * 
 * @author Pieter De Graef
 */
public class RasterTileObject extends HtmlImageImpl implements TileView {

	public RasterTileObject(String src, int width, int height, int top, int left) {
		super(src, width, height, top, left);
	}

	public void setContent(String content) {
		DOM.setElementProperty(getElement(), "src", content);
	}
}