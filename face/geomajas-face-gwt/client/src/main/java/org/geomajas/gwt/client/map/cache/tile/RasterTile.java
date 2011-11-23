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
	
	/** The bounds of the tile. */
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

	public void accept(PainterVisitor visitor, Object group, Bbox bounds, boolean recursive) {
		visitor.visit(this, group);
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

	public void setStyle(PictureStyle style) {
		this.style = style;
	}
	
	public void setBounds(Bbox bbox) {
		this.bbox = bbox;
	}	
	
}
