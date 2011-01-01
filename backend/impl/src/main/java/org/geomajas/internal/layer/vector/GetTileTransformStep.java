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

import org.geomajas.geometry.CrsTransform;
import org.geomajas.global.GeomajasException;
import org.geomajas.internal.layer.feature.InternalFeatureImpl;
import org.geomajas.internal.rendering.strategy.TiledFeatureService;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.pipeline.GetTileContainer;
import org.geomajas.layer.tile.TileMetadata;
import org.geomajas.service.GeoService;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.springframework.beans.factory.annotation.Autowired;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

import java.util.ArrayList;
import java.util.List;

/**
 * Transform the features in a tile to map coordinates and determines if they are part of the tile or should be fetched
 * through other tile codes.
 * 
 * @author Joachim Van der Auwera
 * @author Jan De Moerloose
 */
public class GetTileTransformStep implements PipelineStep<GetTileContainer> {

	private String id;

	@Autowired
	private TiledFeatureService tiledFeatureService;

	@Autowired
	private GeoService geoService;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(PipelineContext context, GetTileContainer response) throws GeomajasException {
		TileMetadata metadata = context.get(PipelineCode.TILE_METADATA_KEY, TileMetadata.class);

		// Determine transformation to apply
		CrsTransform transform = context.get(PipelineCode.CRS_TRANSFORM_KEY, CrsTransform.class);

		// convert feature geometries to layer, need to copy to assure cache is not affected
		List<InternalFeature> orgFeatures = response.getTile().getFeatures();
		List<InternalFeature> features = new ArrayList<InternalFeature>();
		response.getTile().setFeatures(features);
		for (InternalFeature feature : orgFeatures) {
			if (null != feature.getGeometry()) {
				InternalFeature newFeature = new InternalFeatureImpl(feature);
				newFeature.setGeometry(geoService.transform(feature.getGeometry(), transform));
				features.add(newFeature);
			}
		}
		
		// Determine the maximum tile extent
		Envelope maxTileExtent = context.get(PipelineCode.TILE_MAX_EXTENT_KEY, Envelope.class);
		// fill the tiles
		tiledFeatureService.fillTile(response.getTile(), maxTileExtent);

		// clipping of features in tile
		Coordinate panOrigin = new Coordinate(metadata.getPanOrigin().getX(), metadata.getPanOrigin().getY());
		tiledFeatureService.clipTile(response.getTile(), metadata.getScale(), panOrigin);
	}
}
