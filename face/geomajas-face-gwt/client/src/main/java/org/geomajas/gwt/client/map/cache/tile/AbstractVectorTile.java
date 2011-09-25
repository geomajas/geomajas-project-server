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

import java.util.List;

import com.google.gwt.core.client.GWT;
import org.geomajas.global.GeomajasConstant;
import org.geomajas.gwt.client.gfx.PaintableGroup;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.map.cache.SpatialCache;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.feature.LazyLoadCallback;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.layer.tile.TileCode;

/**
 * <p>
 * A paintable tile for vector layers.
 * <p/>
 * 
 * @author Pieter De Graef
 */
public abstract class AbstractVectorTile implements Tile, PaintableGroup {

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

	/** Reference to the {@link SpatialCache}. */
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

	// -------------------------------------------------------------------------
	// PainterVisitable implementation:
	// -------------------------------------------------------------------------

	public void accept(final PainterVisitor visitor, final Object group, final Bbox bounds, final boolean recursive) {
		// Draw the tile and therefore all it's features:
		visitor.visit(this, group);

		// Draw all selected features:
		if (recursive) {
			getFeatures(GeomajasConstant.FEATURE_INCLUDE_NONE, new LazyLoadCallback() {
				public void execute(List<Feature> response) {
					GWT.log("          draw vector tile " + code);
					for (Feature feature : response) {
						if (feature != null && feature.isSelected()) {
							feature.accept(visitor, group, bounds, recursive);
						}
					}
				}
			});
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

	/**
	 * Return all features in this node.
	 *
	 * @param featureIncludes what data should be available in the features
	 * @param callback callback which gets the features
	 */
	public abstract void getFeatures(int featureIncludes, LazyLoadCallback callback);

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