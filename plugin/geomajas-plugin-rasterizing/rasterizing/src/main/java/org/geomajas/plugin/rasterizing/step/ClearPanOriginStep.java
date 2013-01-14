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

package org.geomajas.plugin.rasterizing.step;

import org.geomajas.geometry.Coordinate;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.pipeline.GetTileContainer;
import org.geomajas.layer.tile.TileMetadata;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;

/**
 * Step which clears the pan origin. Rasterized tiles don't make use of the pan origin, so it should not be part of the
 * metadata.
 * 
 * @author Jan De Moerloose
 * 
 */
public class ClearPanOriginStep implements PipelineStep<GetTileContainer> {

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(PipelineContext context, GetTileContainer response) throws GeomajasException {
		TileMetadata tileMetadata = context.getOptional(PipelineCode.TILE_METADATA_KEY, TileMetadata.class);
		if (tileMetadata != null) {
			// set to origin
			tileMetadata.setPanOrigin(new Coordinate());
		}
	}		
	

}
