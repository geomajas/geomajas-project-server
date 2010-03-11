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
package org.geomajas.gwt.client.map.cache.tile;

import org.geomajas.gwt.client.gfx.Paintable;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.gfx.style.PictureStyle;
import org.geomajas.gwt.client.map.store.RasterLayerStore;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.layer.tile.TileCode;

/**
 * A paintable tile for raster layers.
 *
 * @author Jan De Moerloose
 */
public class RasterTile implements Tile, Paintable {

	/** The unique tile code. */
	protected TileCode code;
	/** The bounds of the tile; */
	protected Bbox bbox;

	/** The store where this tile is kept. */
	protected RasterLayerStore store;

	/** the image url */
	protected String url;

	/** style */
	protected PictureStyle style;

	protected String id;

	/**
	 * @param code
	 * @param bbox
	 * @param store
	 */
	public RasterTile(TileCode code, Bbox bbox, String url, RasterLayerStore store) {
		this.code = code;
		this.bbox = bbox;
		this.url = url;
		this.store = store;
		this.id = store.getLayer().getMapModel().getId() + "." + store.getLayer().getId() + "." + code.toString();
		String styleStr = store.getLayer().getLayerInfo().getStyle();
		style = new PictureStyle(Double.valueOf(styleStr).doubleValue());
	}

	public TileCode getCode() {
		return code;
	}

	public Bbox getBounds() {
		return bbox;
	}

	public boolean isComplete() {
		return true;
	}

	/** Return the unique value of the spatial code for this node. */
	public String getId() {
		return id;
	}

	public void accept(PainterVisitor visitor, Bbox bounds, boolean recursive) {
		visitor.visit(this);
	}

	public PictureStyle getStyle() {
		return style;
	}

	public String getUrl() {
		return url;
	}
	
	public RasterLayerStore getStore() {
		return store;
	}
	
}
