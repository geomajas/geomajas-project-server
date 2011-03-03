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

package org.geomajas.plugin.rasterizing.step;

import java.util.ArrayList;

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.pipeline.GetTileContainer;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;

/**
 * Step which removes feature and dependent node information. Rasterized tiles should not need this information.
 * 
 * @author Jan De Moerloose
 */
public class RemoveTileFeaturesStep implements PipelineStep<GetTileContainer> {

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(PipelineContext context, GetTileContainer response) throws GeomajasException {
		InternalTile tile = response.getTile();
		tile.setFeatures(new ArrayList<InternalFeature>());
		tile.setCodes(new ArrayList<TileCode>());
	}
}
