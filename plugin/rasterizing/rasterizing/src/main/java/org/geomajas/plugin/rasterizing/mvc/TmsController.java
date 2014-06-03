/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.rasterizing.mvc;

import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Crs;
import org.geomajas.geometry.CrsTransform;
import org.geomajas.global.GeomajasException;
import org.geomajas.internal.layer.tile.InternalTileImpl;
import org.geomajas.internal.layer.tile.TileMetadataImpl;
import org.geomajas.internal.rendering.strategy.TileUtil;
import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.RasterLayerService;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.pipeline.GetTileContainer;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.layer.tile.TileMetadata;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheManagerService;
import org.geomajas.plugin.caching.service.CachingSupportService;
import org.geomajas.plugin.caching.service.CachingSupportServiceSecurityContextAdder;
import org.geomajas.plugin.rasterizing.api.RasterizingContainer;
import org.geomajas.plugin.rasterizing.api.RasterizingPipelineCode;
import org.geomajas.plugin.rasterizing.layer.tile.TmsTileMetadata;
import org.geomajas.plugin.rasterizing.step.RebuildCacheContainer;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.geomajas.service.StyleService;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Controller which serves tiles in a TMS-compliant way.
 * 
 * @author Jan De Moerloose
 */
@Controller(TmsController.MAPPING + "**")
public class TmsController {

	private final Logger log = LoggerFactory.getLogger(TmsController.class);

	public static final String MAPPING = "/tms/";

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

	@Autowired
	private StyleService styleService;

	@Autowired
	private VectorLayerService layerService;

	@Autowired
	private RasterLayerService rasterLayerService;

	@Autowired
	private CachingSupportService cachingSupportService;

	private final HttpClient httpClient;

	private static final String TMS_TILE_RENDERER = "TmsTileRenderer";

	private static final String[] KEYS = { PipelineCode.LAYER_ID_KEY, PipelineCode.TILE_METADATA_KEY };

	public TmsController() {
		PoolingClientConnectionManager manager = new PoolingClientConnectionManager();
		manager.setDefaultMaxPerRoute(10);
		httpClient = new DefaultHttpClient(manager);

	}

	/**
	 * Get a vector tile from the TMS server.
	 * 
	 * @param layerId layer id
	 * @param styleKey style key
	 * @param crs crs, e.g. "EPSG:4326"
	 * @param tileLevel tile level, 0 is highest level
	 * @param xIndex x-index of tile 
	 * @param yIndex y-index of tile
	 * @param resolution resolution (m/pixel)
	 * @param tileOrigin origin of the tile configuration "&lt;x-coordinate>,&lt;y-coordinate>"
	 * @param tileWidth tile width in pixels
	 * @param tileHeight tile height in pixels
	 * @param response servlet response
	 * @throws Exception
	 */
	@RequestMapping(value = MAPPING + "{layerId}@{crs}/{styleKey}/{tileLevel}/{xIndex}/{yIndex}.png", method = RequestMethod.GET)
	public void getVectorTile(@PathVariable String layerId, @PathVariable String styleKey, @PathVariable String crs,
			@PathVariable Integer tileLevel, @PathVariable Integer xIndex, @PathVariable Integer yIndex,
			@RequestParam Double resolution, @RequestParam String tileOrigin,
			@RequestParam(required = false, defaultValue = "512") int tileWidth,
			@RequestParam(required = false, defaultValue = "512") int tileHeight, HttpServletResponse response)
			throws Exception {
		try {
			Crs tileCrs = geoService.getCrs2(crs);
			TmsTileMetadata tileMetadata = new TmsTileMetadata();
			tileMetadata.setCode(new TileCode(tileLevel, xIndex, yIndex));
			tileMetadata.setCrs(geoService.getCodeFromCrs(tileCrs));
			tileMetadata.setLayerId(layerId);
			tileMetadata.setPaintGeometries(true);
			tileMetadata.setPaintLabels(false);
			tileMetadata.setRenderer(TMS_TILE_RENDERER);
			// TmsTileMetadata specific
			tileMetadata.setResolution(resolution);
			tileMetadata.setTileOrigin(parseOrigin(tileOrigin));
			tileMetadata.setTileWidth(tileWidth);
			tileMetadata.setTileHeight(tileHeight);
			tileMetadata.setStyleInfo(styleService.retrieveStyle(layerId, styleKey));

			RebuildCacheContainer rcc = new RebuildCacheContainer();
			rcc.setMetadata(tileMetadata);
			PipelineContext context = pipelineService.createContext();
			context.put(PipelineCode.TILE_METADATA_KEY, tileMetadata);
			context.put(PipelineCode.LAYER_ID_KEY, layerId);
			// store container to recover the key
			cachingSupportService.putContainer(context, securityContextAdder, CacheCategory.REBUILD, KEYS,
					RasterizingPipelineCode.IMAGE_ID_KEY, RasterizingPipelineCode.IMAGE_ID_CONTEXT, rcc, null);
			String key = context.get(RasterizingPipelineCode.IMAGE_ID_KEY, String.class);
			renderImage(layerId, key, response);
		} catch (Throwable e) { // NOSONAR need to log all problems
			log.error("Could not rasterize tile " + layerId + "/" + styleKey + "/" + tileLevel + "-" + xIndex + "-"
					+ yIndex + ".png", e);
			response.sendError(HttpServletResponse.SC_NO_CONTENT);
		}
	}

	/**
	 * Get a raster layer tile from the TMS server.
	 * 
	 * @param layerId layer id
	 * @param crs crs, e.g. EPSG:4326
	 * @param tileLevel tile level, 0 is highest level
	 * @param xIndex x-index of tile 
	 * @param yIndex y-index of tile
	 * @param request servlet request
	 * @param response servlet response
	 * @throws Exception
	 */
	@RequestMapping(value = MAPPING + "{layerId}@{crs}/{tileLevel}/{xIndex}/{yIndex}.png", method = RequestMethod.GET)
	public void getRasterTile(@PathVariable String layerId, @PathVariable String crs, @PathVariable Integer tileLevel,
			@PathVariable Integer xIndex, @PathVariable Integer yIndex, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Crs tileCrs = geoService.getCrs2(crs);
		// calculate the tile extent
		Envelope maxExtent = getRasterLayerExtent(layerId, crs);
		RasterLayer layer = configurationService.getRasterLayer(layerId);
		double scale = getScale(layer.getLayerInfo().getTileWidth(), tileLevel, maxExtent.getWidth());
		Envelope tileBounds = TileUtil.getTileBounds(new TileCode(tileLevel, xIndex, yIndex), maxExtent, scale);
		// take center to avoid overlap with other tiles !!!
		double centerX = 0.5 * (tileBounds.getMinX() + tileBounds.getMaxX());
		double centerY = 0.5 * (tileBounds.getMinY() + tileBounds.getMaxY());
		tileBounds = new Envelope(centerX, centerX, centerY, centerY);
		List<RasterTile> tiles = rasterLayerService.getTiles(layerId, tileCrs, tileBounds, scale);
		if (tiles.size() == 1) {
			log.debug("Rendering raster layer tile " + layerId + "/" + tileLevel + "-" + xIndex + "-" + yIndex);
			log.debug("Url = " + tiles.get(0).getUrl());
			log.debug("Tile = " + tiles.get(0));
			writeToResponse(tiles.get(0).getUrl(), request, response);
		}
	}

	/**
	 * Renders the image by fetching it from the cache or, if that fails, using the rebuild container.
	 * 
	 * @param layerId
	 * @param key
	 * @param tileExtent
	 * @param response
	 * @throws Exception
	 */
	private void renderImage(String layerId, String key, HttpServletResponse response) throws Exception {

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
				// can't stop here, we have only prepared the context, not built the tile !
				InternalTileImpl tile = null;
				if (tileMetadata.getRenderer().equals(TMS_TILE_RENDERER)) {
					tile = new InternalTileImpl(tileMetadata.getCode(),
							((TmsTileMetadata) tileMetadata).getTileOrigin(), tileMetadata.getScale(),
							((TmsTileMetadata) tileMetadata).getTileWidth(),
							((TmsTileMetadata) tileMetadata).getTileHeight());
				} else {
					tile = new InternalTileImpl(tileMetadata.getCode(), getLayerExtent(tileMetadata),
							tileMetadata.getScale());
				}
				tileContainer.setTile(tile);
				securityContextAdder.restoreSecurityContext(rebuildCacheContainer.getContext());
				Crs crs = geoService.getCrs2(tileMetadata.getCrs());
				context.put(PipelineCode.CRS_KEY, crs);
				CrsTransform layerToMap = geoService.getCrsTransform(layer.getCrs(), crs);
				context.put(PipelineCode.CRS_TRANSFORM_KEY, layerToMap);
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

	private Coordinate parseOrigin(String tileOrigin) {
		String[] xy = tileOrigin.split(",");
		return new Coordinate(Double.parseDouble(xy[0]), Double.parseDouble(xy[1]));
	}

	private double getScale(int screenWidth, int level, double width) {
		double scale = (screenWidth * Math.pow(2, level) / width);
		return scale;
	}

	private Envelope getLayerExtent(TileMetadata tileMetadata) throws GeomajasException {
		VectorLayer layer = configurationService.getVectorLayer(tileMetadata.getLayerId());
		Crs layerCrs = layerService.getCrs(layer);
		Crs tileCrs = geoService.getCrs2(tileMetadata.getCrs());
		CrsTransform layerToMap = geoService.getCrsTransform(layerCrs, tileCrs);
		Envelope layerExtent = dtoConverterService.toInternal(layer.getLayerInfo().getMaxExtent());
		Envelope tileExtent = geoService.transform(layerExtent, layerToMap);
		return tileExtent;
	}

	private Envelope getRasterLayerExtent(String layerId, String crs) throws GeomajasException {
		RasterLayer layer = configurationService.getRasterLayer(layerId);
		Crs layerCrs = layerService.getCrs(layer);
		Crs tileCrs = geoService.getCrs2(crs);
		CrsTransform layerToMap = geoService.getCrsTransform(layerCrs, tileCrs);
		Envelope layerExtent = dtoConverterService.toInternal(layer.getLayerInfo().getMaxExtent());
		Envelope tileExtent = geoService.transform(layerExtent, layerToMap);
		return tileExtent;
	}

	private void writeToResponse(String url, HttpServletRequest request, HttpServletResponse response) {
		HttpGet get = new HttpGet(url);
		for (Enumeration<String> names = request.getHeaderNames(); names.hasMoreElements();) {
			String name = names.nextElement();
			get.setHeader(name, request.getHeader(name));
		}
		try {
			HttpResponse r = httpClient.execute(get);
			r.getEntity().writeTo(response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
