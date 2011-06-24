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

import org.geomajas.geometry.Crs;
import org.geomajas.geometry.CrsTransform;
import org.geomajas.global.GeomajasException;
import org.geomajas.internal.layer.tile.InternalTileImpl;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.pipeline.GetTileContainer;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.TileMetadata;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.step.AbstractSecurityContextCachingInterceptor;
import org.geomajas.plugin.rasterizing.api.RasterizingPipelineCode;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.geomajas.service.TestRecorder;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Interceptor that caches the rebuild context.
 * 
 * @author Jan De Moerloose
 */
public class RebuildCacheCachingInterceptor extends AbstractSecurityContextCachingInterceptor<GetTileContainer> {

	private static final String[] KEYS = { PipelineCode.LAYER_ID_KEY, PipelineCode.TILE_METADATA_KEY };

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private GeoService geoService;

	@Autowired
	private DtoConverterService dtoConverterService;

	@Autowired
	private TestRecorder recorder;
	
	public ExecutionMode beforeSteps(PipelineContext context, GetTileContainer response) throws GeomajasException {
		if (context.containsKey(PipelineCode.TILE_METADATA_KEY)) {
		RebuildCacheContainer rcc = getContainer(RasterizingPipelineCode.IMAGE_ID_KEY,
				RasterizingPipelineCode.IMAGE_ID_CONTEXT, KEYS, CacheCategory.REBUILD, context,
				RebuildCacheContainer.class);
		} else {
			recorder.record(CacheCategory.REBUILD, "Got item from cache");
			rcc.getContext();
			TileMetadata tileMetadata = rcc.getMetadata();
			context.put(PipelineCode.TILE_METADATA_KEY, tileMetadata);
			context.put(PipelineCode.LAYER_ID_KEY, tileMetadata.getLayerId());
			VectorLayer layer = configurationService.getVectorLayer(tileMetadata.getLayerId());
			context.put(PipelineCode.LAYER_KEY, layer);
			context.put(PipelineCode.TILE_METADATA_KEY, tileMetadata);
			Crs crs = geoService.getCrs2(tileMetadata.getCrs());
			context.put(PipelineCode.CRS_KEY, crs);
			CrsTransform layerToMap = geoService.getCrsTransform(layer.getCrs(), crs);
			context.put(PipelineCode.CRS_TRANSFORM_KEY, layerToMap);
			context.put(PipelineCode.FEATURE_INCLUDES_KEY, tileMetadata.getFeatureIncludes());
			Envelope layerExtent = dtoConverterService.toInternal(layer.getLayerInfo().getMaxExtent());
			Envelope tileExtent = geoService.transform(layerExtent, layerToMap);
			context.put(PipelineCode.TILE_MAX_EXTENT_KEY, tileExtent);
			// can't stop here, we have only prepared the context, not built the tile !
			InternalTile tile = new InternalTileImpl(tileMetadata.getCode(), tileExtent, tileMetadata.getScale());
			response.setTile(tile);
			restoreSecurityContext(rcc.getContext());
			return ExecutionMode.EXECUTE_ALL;
		}
		return ExecutionMode.EXECUTE_ALL;
	}

	public void afterSteps(PipelineContext context, GetTileContainer response) throws GeomajasException {
		recorder.record(CacheCategory.REBUILD, "Put item in cache");
		RebuildCacheContainer rcc = new RebuildCacheContainer();
		TileMetadata tileMetadata = context.get(PipelineCode.TILE_METADATA_KEY, TileMetadata.class);
		rcc.setMetadata(tileMetadata);
		putContainer(context, CacheCategory.REBUILD, KEYS, RasterizingPipelineCode.IMAGE_ID_KEY,
				RasterizingPipelineCode.IMAGE_ID_CONTEXT, rcc, response.getTile().getBounds());
	}

}
