/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.command.dto;

import org.geomajas.annotation.Api;
import org.geomajas.command.CommandResponse;
import org.geomajas.layer.tile.VectorTile;

/**
 * Response object for {@link org.geomajas.command.render.GetVectorTileCommand}.
 *
 * @author Jan De Moerloose
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class GetVectorTileResponse extends CommandResponse {

	private static final long serialVersionUID = 151L;

	private VectorTile tile;

	/**
	 * Get response tile.
	 *
	 * @return tile
	 */
	public VectorTile getTile() {
		return tile;
	}

	/**
	 * Set response tile.
	 *
	 * @param tile tile
	 */
	public void setTile(VectorTile tile) {
		this.tile = tile;
	}

	/**
	 * String representation of object.
	 *
	 * @return string representation of object
	 * @since 1.8.0
	 */
	@Override
	public String toString() {
		return "GetVectorTileResponse{" +
				"tile=" + tile +
				'}';
	}
}