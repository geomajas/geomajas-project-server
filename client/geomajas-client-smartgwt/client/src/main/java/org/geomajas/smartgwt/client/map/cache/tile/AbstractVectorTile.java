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

import org.geomajas.smartgwt.client.gfx.PaintableGroup;
import org.geomajas.smartgwt.client.gfx.PainterVisitor;
import org.geomajas.smartgwt.client.map.cache.SpatialCache;
import org.geomajas.smartgwt.client.spatial.Bbox;
import org.geomajas.layer.tile.TileCode;

/**
 * <p>
 * A paintable tile for vector layers.
 * <p/>
 * 
 * @author Pieter De Graef
 */
public abstract class AbstractVectorTile implements Tile, PaintableGroup {

	private boolean cancelled;

	/**
	 * Possible statuses.
	 */
	static enum STATUS {

		EMPTY, LOADING, LOADED
	}

	/**
	 * Some implementation of the SpatialCode interface. Each Node in a spatial cache needs a unique code.
	 */
	protected TileCode code;

	/** Reference to the {@link org.geomajas.smartgwt.client.map.cache.SpatialCache}. */
	protected SpatialCache cache;

	/**
	 * Each spatial node has bounds attached to it. Usually the spatial cache will know how to calculate bounds for a
	 * specific spatial code.
	 */
	protected Bbox bbox;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	public AbstractVectorTile(TileCode code, Bbox bbox, SpatialCache cache) {
		this.code = code;
		this.bbox = bbox;
		this.cache = cache;
	}

	/**
	 * Cancel the fetching of this tile. No callback will be executed anymore.
	 */
	public void cancel() {
		cancelled = true;
	}


	// -------------------------------------------------------------------------
	// PainterVisitable implementation:
	// -------------------------------------------------------------------------

	public void accept(final PainterVisitor visitor, final Object group, final Bbox bounds, final boolean recursive) {
		if (!cancelled) {
			// Draw the tile and therefore all it's features:
			visitor.visit(this, group);
		}
	}

	// -------------------------------------------------------------------------
	// SpatialNode interface:
	// -------------------------------------------------------------------------

	/**
	 * Return the unique {@link TileCode} for this node.
	 */
	public TileCode getCode() {
		return code;
	}

	public String getGroupName() {
		return code.toString();
	}

	public STATUS getStatus() {
		return STATUS.EMPTY;
	}

	/**
	 * Return this node's bounding box.
	 */
	public Bbox getBounds() {
		return bbox;
	}

	public SpatialCache getCache() {
		return cache;
	}
}