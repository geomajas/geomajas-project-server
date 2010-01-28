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

package org.geomajas.internal.rendering.painter;

import com.vividsolutions.jts.geom.Coordinate;
import org.geomajas.internal.application.tile.RasterTileJG;
import org.geomajas.internal.application.tile.VectorTileJG;
import org.geomajas.internal.rendering.DefaultLayerPaintContext;
import org.geomajas.internal.rendering.image.TileImageCreatorImpl;
import org.geomajas.internal.rendering.painter.feature.DefaultFeaturePainter;
import org.geomajas.internal.rendering.painter.feature.TiledFeaturePainter;
import org.geomajas.internal.rendering.painter.tile.RasterTilePainter;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.FeatureFactory;
import org.geomajas.rendering.image.TileImageCreator;
import org.geomajas.rendering.painter.LayerPaintContext;
import org.geomajas.rendering.painter.PaintFactory;
import org.geomajas.rendering.painter.feature.FeaturePainter;
import org.geomajas.rendering.painter.tile.TilePainter;
import org.geomajas.rendering.tile.RenderedTile;
import org.geomajas.rendering.tile.TileCode;
import org.geomajas.rendering.tile.UrlTile;
import org.geomajas.service.BboxService;
import org.geomajas.service.FilterCreator;
import org.geomajas.service.VectorLayerModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Allows creation of painter related objects.
 *
 * @author Joachim Van der Auwera
 */
@Component
public class PaintFactoryImpl implements PaintFactory {

	@Autowired
	private BboxService bboxService;

	@Autowired
	private FilterCreator filterCreator;

	@Autowired
	private FeatureFactory featureFactory;

	@Autowired
	private VectorLayerModelService layerModelService;

	public FeaturePainter createTiledFeaturePainter(RenderedTile tile, VectorLayer layer, TileCode code, double scale,
			Coordinate panOrigin) {
		return new TiledFeaturePainter(tile, layer, code, scale, panOrigin, bboxService);
	}

	public FeaturePainter createFeaturePainter() {
		return new DefaultFeaturePainter(featureFactory);
	}

	public LayerPaintContext createLayerPaintContext(VectorLayer layer) {
		return new DefaultLayerPaintContext(layer);
	}

	public TileImageCreator createTileImageCreator(RenderedTile tile, boolean transparent) {
		return new TileImageCreatorImpl(tile, transparent, filterCreator, layerModelService);
	}

	public TilePainter createRasterTilePainter(String layerId) {
		return new RasterTilePainter(layerId);
	}

	public UrlTile createRasterTile(TileCode code, VectorLayer layer, double scale) {
		return new RasterTileJG(code, layer, scale);
	}

	public RenderedTile createVectorTile(TileCode code, VectorLayer layer, double scale) {
		return new VectorTileJG(code, layer, scale);
	}
}
