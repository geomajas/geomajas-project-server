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

package org.geomajas.internal.service.vector;

import com.vividsolutions.jts.geom.Coordinate;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.internal.rendering.strategy.TileService;
import org.geomajas.internal.rendering.strategy.TiledFeatureService;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.TileMetadata;
import org.geomajas.service.GeoService;
import org.geomajas.service.VectorLayerService;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.geotools.geometry.jts.JTS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Transform the features in a tile to map coordinates.
 *
 * @author Joachim Van der Auwera
 */
public class GetTileTransformStep implements PipelineStep<InternalTile> {

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

	public void execute(PipelineContext context, InternalTile response) throws GeomajasException {
		VectorLayer layer = context.get(PipelineCode.LAYER_KEY, VectorLayer.class);
		TileMetadata metadata = context.get(PipelineCode.TILE_METADATA_KEY, TileMetadata.class);
		CoordinateReferenceSystem crs = context.get(PipelineCode.CRS_KEY, CoordinateReferenceSystem.class);

		// Determine transformation to apply
		MathTransform transform = context.get(PipelineCode.CRS_TRANSFORM_KEY, MathTransform.class);

		// convert tile data to layer
		System.out.println("transform tile sizes");
		TileService.transformTileSizes(response, transform, metadata.getScale());

		// convert feature geometries to layer
		for (InternalFeature feature : response.getFeatures()) {
			if (null != feature.getGeometry()) {
				try {
					System.out.println("transform a feature");
					feature.setGeometry(JTS.transform(feature.getGeometry(), transform));
				} catch (TransformException te) {
					throw new GeomajasException(te, ExceptionCode.GEOMETRY_TRANSFORMATION_FAILED);
				}
			}
		}

		// clipping of features in tile
		System.out.println("clip tile");
		Coordinate panOrigin = new Coordinate(metadata.getPanOrigin().getX(), metadata.getPanOrigin().getY());
		tiledFeatureService.clipTile(response, layer, metadata.getCode(), metadata.getScale(), panOrigin);
	}
}
