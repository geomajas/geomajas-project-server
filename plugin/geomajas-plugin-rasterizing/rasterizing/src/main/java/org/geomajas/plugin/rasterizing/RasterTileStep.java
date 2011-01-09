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

package org.geomajas.plugin.rasterizing;

import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.pipeline.GetTileContainer;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.TileMetadata;
import org.geomajas.layer.tile.VectorTile;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheContext;
import org.geomajas.plugin.caching.service.CacheKeyService;
import org.geomajas.plugin.caching.service.CacheManagerService;
import org.geomajas.security.SecurityContext;
import org.geomajas.service.DispatcherUrlService;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayOutputStream;

/**
 * Pipeline step which rasterizes vector tiles.
 *
 * @author Joachim Van der Auwera
 */
public class RasterTileStep implements PipelineStep<GetTileContainer> {

	private static final String[] KEYS = {PipelineCode.LAYER_ID_KEY, PipelineCode.TILE_METADATA_KEY};

	private final Logger log = LoggerFactory.getLogger(RasterTileStep.class);

	@Autowired
	private DispatcherUrlService dispatcherUrlService;

	@Autowired
	private CacheManagerService cacheManager;

	@Autowired
	private CacheKeyService cacheKeyService;

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private RasterizingInfo rasterizingInfo;

	@Autowired
	private RasterizingService rasterizingService;

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(PipelineContext context, GetTileContainer tileContainer) throws GeomajasException {
		InternalTile tile = tileContainer.getTile();
		tile.setContentType(VectorTile.VectorTileContentType.URL_CONTENT);

		String layerId = context.get(PipelineCode.LAYER_ID_KEY, String.class);
		VectorLayer layer = context.get(PipelineCode.LAYER_KEY, VectorLayer.class);
		TileMetadata tileMetadata = context.get(PipelineCode.TILE_METADATA_KEY, TileMetadata.class);

		CacheContext cacheContext = cacheKeyService.getCacheContext(context, KEYS);
		cacheContext.put("securityContext", securityContext.getId());
		String cacheKey = cacheKeyService.getCacheKey(layer, CacheCategory.TILE, cacheContext);

		RebuildCacheContainer cc = cacheManager.get(layer, CacheCategory.REBUILD, cacheKey,
				RebuildCacheContainer.class);
		cc.setContext(cacheContext);
		cc.setMetadata(tileMetadata);

		while (null != cc) {
			if (cacheContext.equals(cc.getContext())) {
				// found item in cache, nothing special to do
				break;
			} else {
				cacheKey = cacheKeyService.makeUnique(cacheKey);
				log.debug("Cache context did not match, new key {}", cacheKey);
				cc = cacheManager.get(layer, CacheCategory.REBUILD, cacheKey, RebuildCacheContainer.class);
			}
		}
		cacheManager.put(layer, CacheCategory.REBUILD, cacheKey, cc, tileContainer.getTile().getBounds());

		StringBuilder url = new StringBuilder(200);
		url.append(dispatcherUrlService.getDispatcherUrl());
		url.append(layerId);
		url.append("/");
		url.append(cacheKey);
		url.append(".png");
		tile.setFeatureContent(url.toString());

		// see if we need to do the actual rasterizing now
		RasterizingContainer rasterizingContainer = context.getOptional(RasterizingPipelineCode.CONTAINER_KEY,
				RasterizingContainer.class);
		if (RasterizingMoment.TILE_REQUEST == rasterizingInfo.getRasterizingMoment() || null != rasterizingContainer) {
			NamedStyleInfo style = context.get(PipelineCode.STYLE_KEY, NamedStyleInfo.class);
			ByteArrayOutputStream imageStream = new ByteArrayOutputStream(50000);
			try {
				rasterizingService.rasterize(imageStream, layer, style, tileMetadata, tile);
			} catch (Exception ex) {
				log.error("Problem while rasterizing tile, image will be zero-length.", ex);
			}

			byte[] image = imageStream.toByteArray();

			if (null != rasterizingContainer) {
				rasterizingContainer.setImage(image);
			}

			// put in cache, as same context as for rebuild cache
			RasterizingContainer cacheContainer = new RasterizingContainer();
			cacheContainer.setContext(cacheContext);
			cacheContainer.setImage(image);
			cacheManager.put(layer, CacheCategory.RASTER, cacheKey, cacheContainer,
					tileContainer.getTile().getBounds());
		}
	}
}
