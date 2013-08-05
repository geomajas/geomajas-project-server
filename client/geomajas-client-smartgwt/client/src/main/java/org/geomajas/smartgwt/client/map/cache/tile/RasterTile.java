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
package org.geomajas.smartgwt.client.map.cache.tile;

import org.geomajas.smartgwt.client.gfx.Paintable;
import org.geomajas.smartgwt.client.gfx.PainterVisitor;
import org.geomajas.smartgwt.client.gfx.style.PictureStyle;
import org.geomajas.smartgwt.client.map.store.RasterLayerStore;
import org.geomajas.smartgwt.client.spatial.Bbox;
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

	/** The image url. */
	protected String url;

	/** Style. */
	protected PictureStyle style;

	protected String id;

	/**
	 * Constructor for a raster tile.
	 *
	 * @param code tile code
	 * @param bbox bounding box
	 * @param url tile URL
	 * @param store raster layer store
	 */
	public RasterTile(TileCode code, Bbox bbox, String url, RasterLayerStore store) {
		this.code = code;
		this.bbox = bbox;
		this.url = url;
		this.store = store;
		this.id = store.getLayer().getMapModel().getId() + "." + store.getLayer().getId() + "." + code.toString();
		String styleStr = store.getLayer().getLayerInfo().getStyle();
		try {
			style = new PictureStyle(Double.parseDouble(styleStr));
		} catch (NumberFormatException e) {
			style = new PictureStyle(1.0);
		}
	}

	@Override
	public TileCode getCode() {
		return code;
	}

	@Override
	public Bbox getBounds() {
		return bbox;
	}

	/**
	 * Return the unique value of the spatial code for this node.
	 *
	 * @return id
	 */
	public String getId() {
		return id;
	}

	@Override
	public void accept(PainterVisitor visitor, Object group, Bbox bounds, boolean recursive) {
		visitor.visit(this, group);
	}

	/**
	 * Get style for this tile.
	 *
	 * @return tile style
	 */
	public PictureStyle getStyle() {
		return style;
	}

	/**
	 * Get URL for this tile.
	 *
	 * @return tile URL
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Get raster layer store.
	 *
	 * @return raster layer store
	 */
	public RasterLayerStore getStore() {
		return store;
	}

	/**
	 * Set style.
	 *
	 * @param style style
	 */
	public void setStyle(PictureStyle style) {
		this.style = style;
	}

	/**
	 * Set bounding box.
	 *
	 * @param bbox bounding box
	 */
	public void setBounds(Bbox bbox) {
		this.bbox = bbox;
	}	
	
}
