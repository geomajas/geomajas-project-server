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

package org.geomajas.smartgwt.client.map.store;

import java.util.Collection;

import org.geomajas.smartgwt.client.map.cache.tile.Tile;

/**
 * A store is a collection of tiles for a particular layer.
 *
 * @param <T>
 *
 * @author Pieter De Graef
 */
public interface LayerStore<T extends Tile> {

	/** Empty the entire <code>LayerStore</code> unconditionally. */
	void clear();

	/**
	 * Is the <code>LayerStore</code> out of sync ?
	 *
	 * @return true if dirty
	 */
	boolean isDirty();

	/** Return an array of all the <code>SpatialNode</code>s currently present in the cache. */
	Collection<T> getTiles();
}
