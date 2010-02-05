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
package org.geomajas.extension.command.dto;

import org.geomajas.command.CommandResponse;
import org.geomajas.layer.tile.RasterImage;

import java.util.List;

/**
 * Response object for {@link org.geomajas.extension.command.render.GetRasterDataCommand}.
 *
 * @author Jan De Moerloose
 */
public class GetRasterDataResponse extends CommandResponse {

	private static final long serialVersionUID = 151L;

	private List<RasterImage> rasterData;

	private String nodeId;

	public GetRasterDataResponse() {
	}

	/**
	 * Get list of raster image metadata.
	 *
	 * @return list of raster data
	 */
	public List<RasterImage> getRasterData() {
		return rasterData;
	}

	/**
	 * Set list of raster tiles.
	 *
	 * @param rasterData new list with raster tiles
	 */
	public void setRasterData(List<RasterImage> rasterData) {
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
	 * Set node id, which is (layer id) + "." + level
	 *
	 * @param nodeId node id
	 */
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
}
