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
import org.geomajas.gwt.client.map.cache.SpatialCache;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.rendering.tile.TileCode;

import java.util.Collection;

/**
 * <p>
 * A paintable tile for vector layers.
 * <p/>
 * 
 * @author Pieter De Graef
 */
public abstract class AbstractVectorTile implements Tile, Paintable {

	/**
	 * ???
	 */
	static enum STATUS {

		EMPTY, LOADING, LOADED
	}

	/**
	 * Some implementation of the SpatialCode interface. Each Node in a spatial cache needs a unique code.
	 */
	protected TileCode code;

	/** Reference to the <code>SpatialCache</code>. */
	protected SpatialCache cache;

	/**
	 * Each spatial node has bounds attached to it. Usually the spatial cache will know how to calculate bounds for a
	 * specific spatial code.
	 */
	protected Bbox bbox;

	protected String id;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	public AbstractVectorTile(TileCode code, Bbox bbox, SpatialCache cache) {
		this.code = code;
		this.bbox = bbox;
		this.cache = cache;
		id = cache.getLayer().getMapModel().getId() + "." + cache.getLayer().getId() + ".features." + code.toString();
	}

	// -------------------------------------------------------------------------
	// PainterVisitable implementation:
	// -------------------------------------------------------------------------

	public void accept(PainterVisitor visitor, Bbox bounds, boolean recursive) {
		// Draw the tile and therefore all it's features:
		visitor.visit(this);

		// Draw all selected features:
		if (recursive) {
			Collection<Feature> features = getFeatures();
			for (Feature feature : features) {
				if (feature != null && feature.isSelected()) {
					feature.accept(visitor, bounds, recursive);
				}
			}
		}
	}

	/**
	 * Return the unique value of the spatial code for this node.
	 */
	public String getId() {
		return id;
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

	/**
	 * Return all features in this node.
	 */
	public abstract Collection<Feature> getFeatures();

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