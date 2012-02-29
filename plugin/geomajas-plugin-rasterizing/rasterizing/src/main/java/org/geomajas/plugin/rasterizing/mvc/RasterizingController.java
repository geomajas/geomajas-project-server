/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.rasterizing.mvc;

import javax.servlet.http.HttpServletResponse;

import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.geometry.Crs;
import org.geomajas.geometry.CrsTransform;
import org.geomajas.internal.layer.tile.InternalTileImpl;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.pipeline.GetTileContainer;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.TileMetadata;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheManagerService;
import org.geomajas.plugin.caching.service.CachingSupportServiceSecurityContextAdder;
import org.geomajas.plugin.rasterizing.api.RasterizingContainer;
import org.geomajas.plugin.rasterizing.api.RasterizingPipelineCode;
import org.geomajas.plugin.rasterizing.step.RebuildCacheContainer;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.geomajas.service.TestRecorder;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineService;
import org.geomajas.servlet.CacheFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller which serves the actual rasterized images.
 * 
 * @author Joachim Van der Auwera
 * @author Jan De Moerloose
 */
@Controller(RasterizingController.MAPPING + "**")
public class RasterizingController {

	private final Logger log = LoggerFactory.getLogger(RasterizingController.class);

	public static final String MAPPING = "/rasterizing/";

	public static final String LAYER_MAPPING = MAPPING + "layer/";

	public static final String IMAGE_MAPPING = MAPPING + "image/";
	
	@Autowired
	private PipelineService<GetTileContainer> pipelineService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private CacheManagerService cacheManagerService;

	@Autowired
	private DtoConverterService dtoConverterService;

	@Autowired
	private GeoService geoService;

	@Autowired
	private TestRecorder recorder;

	@Autowired
	private CachingSupportServiceSecurityContextAdder securityContextAdder;

	@RequestMapping(value = LAYER_MAPPING + "{layerId}/{key}.png", method = RequestMethod.GET)
	public void getImage(@PathVariable String layerId, @PathVariable String key, HttpServletResponse response)
			throws Exception {

		try {
			VectorLayer layer = configurationService.getVectorLayer(layerId);
			RasterizingContainer rasterizeContainer = cacheManagerService.get(layer, CacheCategory.RASTER, key,
					RasterizingContainer.class);
			// if not in cache, try the rebuild cache and invoke the pipeline directly
			if (rasterizeContainer == null) {
				log.debug("Item not in cache, rebuilding: {}", key);
				GetTileContainer tileContainer = new GetTileContainer();
				PipelineContext context = pipelineService.createContext();
				context.put(RasterizingPipelineCode.IMAGE_ID_KEY, key);
				context.put(PipelineCode.LAYER_ID_KEY, layerId);
				context.put(PipelineCode.LAYER_KEY, layer);

				// get data from rebuild cache
				RebuildCacheContainer rebuildCacheContainer = cacheManagerService.get(layer, CacheCategory.REBUILD,
						key, RebuildCacheContainer.class);
				if (null == rebuildCacheContainer) {
					log.error("Data to rebuild the raster image is no longer available for key " + key);
					response.sendError(HttpServletResponse.SC_NO_CONTENT);
					return;
				}
				recorder.record(CacheCategory.REBUILD, "Got rebuild info from cache");
				TileMetadata tileMetadata = rebuildCacheContainer.getMetadata();
				context.put(PipelineCode.TILE_METADATA_KEY, tileMetadata);
				Crs crs = geoService.getCrs2(tileMetadata.getCrs());
				context.put(PipelineCode.CRS_KEY, crs);
				CrsTransform layerToMap = geoService.getCrsTransform(layer.getCrs(), crs);
				context.put(PipelineCode.CRS_TRANSFORM_KEY, layerToMap);
				Envelope layerExtent = dtoConverterService.toInternal(layer.getLayerInfo().getMaxExtent());
				Envelope tileExtent = geoService.transform(layerExtent, layerToMap);
				context.put(PipelineCode.TILE_MAX_EXTENT_KEY, tileExtent);
				// can't stop here, we have only prepared the context, not built the tile !
				InternalTile tile = new InternalTileImpl(tileMetadata.getCode(), tileExtent, tileMetadata.getScale());
				tileContainer.setTile(tile);
				securityContextAdder.restoreSecurityContext(rebuildCacheContainer.getContext());

				pipelineService.execute(RasterizingPipelineCode.PIPELINE_GET_VECTOR_TILE_RASTERIZING, layerId, context,
						tileContainer);
				rasterizeContainer = context.get(RasterizingPipelineCode.CONTAINER_KEY, RasterizingContainer.class);
			} else {
				recorder.record(CacheCategory.RASTER, "Got item from cache");
					log.debug("Got item from cache: {}", key);
			}
			// Prepare the response:
			CacheFilter.configureNoCaching(response);
			response.setContentType("image/png");
			response.getOutputStream().write(rasterizeContainer.getImage());
		} catch (Throwable e) { // NOSONAR need to log all problems
			log.error("Could not rasterize image " + key, e);
			response.sendError(HttpServletResponse.SC_NO_CONTENT);
		}
	}

	@RequestMapping(value = IMAGE_MAPPING + "{key}.png", method = RequestMethod.GET)
	public void getMap(@PathVariable String key, HttpServletResponse response) throws Exception {
		try {
			RasterizingContainer rasterizeContainer = (RasterizingContainer) cacheManagerService.get(null,
					CacheCategory.RASTER, key);
			// Prepare the response:
			CacheFilter.configureNoCaching(response);
			response.setContentType("image/png");
			response.getOutputStream().write(rasterizeContainer.getImage());
		} catch (Throwable e) { // NOSONAR need to log all problems
			log.error("Could not rasterize image " + key, e);
			response.sendError(HttpServletResponse.SC_NO_CONTENT);
		}
	}

}
