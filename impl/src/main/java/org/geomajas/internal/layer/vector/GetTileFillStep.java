/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.layer.vector;

import org.geomajas.global.GeomajasException;
import org.geomajas.internal.rendering.strategy.TiledFeatureService;
import org.geomajas.layer.pipeline.GetTileContainer;
import org.geomajas.layer.tile.TileMetadata;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.springframework.beans.factory.annotation.Autowired;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

/**
 * Determines if the features are part of the tile or should be fetched
 * through other tile codes.
 * 
 * @author Joachim Van der Auwera
 * @author Jan De Moerloose
 */
public class GetTileFillStep implements PipelineStep<GetTileContainer> {

	private String id;

	@Autowired
	private TiledFeatureService tiledFeatureService;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(PipelineContext context, GetTileContainer response) throws GeomajasException {
		TileMetadata metadata = context.get(PipelineCode.TILE_METADATA_KEY, TileMetadata.class);

		// Determine the maximum tile extent
		Envelope maxTileExtent = context.get(PipelineCode.TILE_MAX_EXTENT_KEY, Envelope.class);

		// fill the tiles
		tiledFeatureService.fillTile(response.getTile(), maxTileExtent);

		// clipping of features in tile
		Coordinate panOrigin = new Coordinate(metadata.getPanOrigin().getX(), metadata.getPanOrigin().getY());
		tiledFeatureService.clipTile(response.getTile(), metadata.getScale(), panOrigin);
	}
}
