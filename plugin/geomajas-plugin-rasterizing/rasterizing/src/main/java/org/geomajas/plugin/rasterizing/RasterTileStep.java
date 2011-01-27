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

package org.geomajas.plugin.rasterizing;

import java.io.ByteArrayOutputStream;

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
import org.geomajas.plugin.rasterizing.api.RasterizingContainer;
import org.geomajas.plugin.rasterizing.api.RasterizingInfo;
import org.geomajas.plugin.rasterizing.api.RasterizingMoment;
import org.geomajas.plugin.rasterizing.api.RasterizingPipelineCode;
import org.geomajas.plugin.rasterizing.api.RasterizingService;
import org.geomajas.security.SecurityContext;
import org.geomajas.service.DispatcherUrlService;
import org.geomajas.service.TestRecorder;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Pipeline step which rasterizes vector tiles.
 * 
 * @author Joachim Van der Auwera
 */
public class RasterTileStep implements PipelineStep<GetTileContainer> {

	private static final String[] KEYS = { PipelineCode.LAYER_ID_KEY, PipelineCode.TILE_METADATA_KEY };

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

	@Autowired
	private TestRecorder recorder;

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

		RebuildCacheContainer cc = cacheManager
				.get(layer, CacheCategory.REBUILD, cacheKey, RebuildCacheContainer.class);
		
		// loop until we have a valid key for the current context
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
		// cc may have gotten null here !
		if (cc == null) {
			cc = new RebuildCacheContainer();
		}
		cc.setContext(cacheContext);
		cc.setMetadata(tileMetadata);
		recorder.record(CacheCategory.REBUILD, cacheKey);
		cacheManager.put(layer, CacheCategory.REBUILD, cacheKey, cc, tileContainer.getTile().getBounds());
		StringBuilder url = new StringBuilder(200);
		url.append(dispatcherUrlService.getDispatcherUrl());
		url.append("/rasterizing/");
		url.append(layerId);
		url.append("/");
		url.append(cacheKey);
		url.append(".png");
		tile.setFeatureContent(url.toString());

		// see if we need to do the actual rasterizing now
		RasterizingContainer rasterizingContainer = context.getOptional(RasterizingPipelineCode.CONTAINER_KEY,
				RasterizingContainer.class);
		if (RasterizingMoment.TILE_REQUEST == rasterizingInfo.getRasterizingMoment() || null != rasterizingContainer) {
			NamedStyleInfo style = tileMetadata.getStyleInfo();
			if (style == null) {
				// no style specified, take the first
				style = layer.getLayerInfo().getNamedStyleInfos().get(0);
			} else if (style.getFeatureStyles().isEmpty()) {
				// only name specified, find it
				style = layer.getLayerInfo().getNamedStyleInfo(style.getName());
			}
			ByteArrayOutputStream imageStream = new ByteArrayOutputStream(50000);
			try {
				rasterizingService.rasterize(imageStream, layer, style, tileMetadata, tile);
				recorder.record(CacheCategory.REBUILD, "Rasterization success");
			} catch (Exception ex) {
				recorder.record(CacheCategory.REBUILD, "Rasterization failed");
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
			cacheManager
					.put(layer, CacheCategory.RASTER, cacheKey, cacheContainer, tileContainer.getTile().getBounds());
		}
	}
}
