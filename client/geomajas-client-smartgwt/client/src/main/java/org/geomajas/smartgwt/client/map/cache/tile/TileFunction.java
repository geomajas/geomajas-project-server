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

/**
 * <p>
 * General interface for functions to be executed on a <code>Tile</code> object.
 * </p>
 *
 * @param <T> type of {@link Tile}
 *
 * @author Pieter De Graef
 */
public interface TileFunction<T extends Tile> {

	/**
	 * Execute this function!
	 *
	 * @param tile tile
	 */
	void execute(T tile);
}