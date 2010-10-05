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

package org.geomajas.internal.layer.vector;

import org.geomajas.global.GeomajasException;
import org.geomajas.internal.rendering.strategy.TiledFeatureService;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.pipeline.GetTileContainer;
import org.geomajas.layer.tile.TileMetadata;
import org.geomajas.service.GeoService;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Get the features for the tile.
 *
 * @author Joachim Van der Auwera
 */
public class GetTileGetFeaturesStep implements PipelineStep<GetTileContainer> {

	private String id;

	@Autowired
	private VectorLayerService layerService;

	@Autowired
	private GeoService geoService;

	@Autowired
	private TiledFeatureService tiledFeatureService;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(PipelineContext context, GetTileContainer response) throws GeomajasException {
		if (null == response.getTile().getFeatures()) {
			VectorLayer layer = context.get(PipelineCode.LAYER_KEY, VectorLayer.class);
			TileMetadata metadata = context.get(PipelineCode.TILE_METADATA_KEY, TileMetadata.class);
			Filter filter = context.get(PipelineCode.FILTER_KEY, Filter.class);

			// Get the features:
			List<InternalFeature> features = layerService
					.getFeatures(metadata.getLayerId(), layer.getCrs(), filter, metadata.getStyleInfo(),
							metadata.getFeatureIncludes() | VectorLayerService.FEATURE_INCLUDE_GEOMETRY |
									VectorLayerService.FEATURE_INCLUDE_STYLE |
									VectorLayerService.FEATURE_INCLUDE_LABEL);
			// Put them all in the tile to make them available to the next step
			response.getTile().setFeatures(features);
		}
	}
}