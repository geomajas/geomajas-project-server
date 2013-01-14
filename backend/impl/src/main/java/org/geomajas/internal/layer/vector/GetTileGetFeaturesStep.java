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

package org.geomajas.internal.layer.vector;

import java.util.List;

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.pipeline.GetTileContainer;
import org.geomajas.layer.tile.TileMetadata;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Get the features for the tile.
 *
 * @author Joachim Van der Auwera
 */
public class GetTileGetFeaturesStep implements PipelineStep<GetTileContainer> {

	private String id;

	@Autowired
	private VectorLayerService layerService;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(PipelineContext context, GetTileContainer response) throws GeomajasException {
		VectorLayer layer = context.get(PipelineCode.LAYER_KEY, VectorLayer.class);
		TileMetadata metadata = context.get(PipelineCode.TILE_METADATA_KEY, TileMetadata.class);
		Filter filter = context.get(PipelineCode.FILTER_KEY, Filter.class);

		// Get the features:
		List<InternalFeature> features = layerService
				.getFeatures(metadata.getLayerId(), layer.getCrs(), filter, metadata.getStyleInfo(),
						VectorLayerService.FEATURE_INCLUDE_ALL);
		// Put them all in the tile to make them available to the next step
		response.getTile().setFeatures(features);
	}
}