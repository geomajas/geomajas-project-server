/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.command.dto;

import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.command.CommandResponse;
import org.geomajas.layer.tile.RasterTile;

/**
 * Response object for {@link org.geomajas.command.render.GetRasterTilesCommand}.
 *
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
public class GetRasterTilesResponse extends CommandResponse {

	private static final long serialVersionUID = 151L;

	private List<RasterTile> rasterData;

	private String nodeId;

	/**
	 * Get list of raster image metadata.
	 *
	 * @return list of raster data
	 */
	public List<RasterTile> getRasterData() {
		return rasterData;
	}

	/**
	 * Set list of raster tiles.
	 *
	 * @param rasterData new list with raster tiles
	 */
	public void setRasterData(List<RasterTile> rasterData) {
		this.rasterData = rasterData;
	}

	/**
	 * Get node id, which is (layer id) + "." + level.
	 *
	 * @return node id
	 */
	public String getNodeId() {
		return nodeId;
	}

	/**
	 * Set node id, which is (layer id) + "." + level.
	 *
	 * @param nodeId node id
	 */
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	@Override
	public String toString() {
		return "GetRasterTilesResponse{" +
				"rasterData=" + rasterData +
				", nodeId='" + nodeId + '\'' +
				'}';
	}
}
