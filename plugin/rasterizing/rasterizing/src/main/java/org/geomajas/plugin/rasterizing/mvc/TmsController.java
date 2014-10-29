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

import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Crs;
import org.geomajas.geometry.CrsTransform;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.global.GeomajasException;
import org.geomajas.internal.layer.tile.InternalTileImpl;
import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.RasterLayerService;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.common.proxy.LayerHttpService;
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

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

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
	private LayerHttpService httpService;

	@Autowired
	private CachingSupportService cachingSupportService;
	
	private boolean redirectRasterLayers = true;

	private static final int ERROR_MESSAGE_X = 10;

	private static final String TMS_TILE_RENDERER = "TmsTileRenderer";

	private static final String[] KEYS = { PipelineCode.LAYER_ID_KEY, PipelineCode.TILE_METADATA_KEY };

	public TmsController() {
	}
	
	public boolean isRedirectRasterLayers() {
		return redirectRasterLayers;
	}
	
	public void setRedirectRasterLayers(boolean redirectRasterLayers) {
		this.redirectRasterLayers = redirectRasterLayers;
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
	@RequestMapping(value = MAPPING + "{layerId}@{crs}/{styleKey}/{tileLevel}/{xIndex}/{yIndex}.{imageFormat}",
			method = RequestMethod.GET)
	public void getVectorTile(@PathVariable String layerId, @PathVariable String styleKey, @PathVariable String crs,
			@PathVariable Integer tileLevel, @PathVariable Integer xIndex, @PathVariable Integer yIndex,
			@RequestParam(required = false) Double resolution,
			@RequestParam(required = false) String tileOrigin,
			@RequestParam(required = false, defaultValue = "512") int tileWidth,
			@RequestParam(required = false, defaultValue = "512") int tileHeight, HttpServletResponse response)
			throws Exception {
		try {
			Crs tileCrs = geoService.getCrs2(crs);
			VectorLayer layer = configurationService.getVectorLayer(layerId);
			TmsTileMetadata tileMetadata = new TmsTileMetadata();
			tileMetadata.setCode(new TileCode(tileLevel, xIndex, yIndex));
			tileMetadata.setCrs(geoService.getCodeFromCrs(tileCrs));
			tileMetadata.setLayerId(layerId);
			tileMetadata.setPaintGeometries(true);
			tileMetadata.setPaintLabels(false);
			tileMetadata.setRenderer(TMS_TILE_RENDERER);
			// TmsTileMetadata specific
			if (resolution == null) {
				resolution = 1 / getScale(tileWidth, tileLevel, layer.getLayerInfo().getMaxExtent().getWidth());
			}
			tileMetadata.setResolution(resolution);
			if (tileOrigin == null) {
				tileMetadata.setTileOrigin(BboxService.getOrigin(layer.getLayerInfo().getMaxExtent()));
			} else {
				tileMetadata.setTileOrigin(parseOrigin(tileOrigin));
			}
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
	@RequestMapping(value = MAPPING + "{layerId}@{crs}/{tileLevel}/{xIndex}/{yIndex}.{imageFormat}",
			method = RequestMethod.GET)
	public void getRasterTile(@PathVariable String layerId, @PathVariable String crs, @PathVariable Integer tileLevel,
			@PathVariable Integer xIndex, @PathVariable Integer yIndex, @PathVariable String imageFormat,
			HttpServletRequest request,	HttpServletResponse response) throws Exception {
		Crs tileCrs = geoService.getCrs2(crs);
		// calculate the tile extent
		Envelope maxExtent = getRasterLayerExtent(layerId, crs);
		RasterLayer layer = configurationService.getRasterLayer(layerId);
		double resolution = 0;
		if (layer.getLayerInfo().getResolutions().size() > 0) {
			// use resolutions of server for raster layer (as yet not reprojectable)
			resolution = layer.getLayerInfo().getResolutions().get(tileLevel);
		} else {
			// if no resolutions, fall back to quad tree numbers
			resolution = 1 / getScale(layer.getLayerInfo().getTileWidth(), tileLevel, layer.getLayerInfo()
					.getMaxExtent().getWidth());
		}
		double centerX = maxExtent.getMinX() + (xIndex + 0.5) * resolution * layer.getLayerInfo().getTileWidth();
		double centerY = maxExtent.getMinY() + (yIndex + 0.5) * resolution * layer.getLayerInfo().getTileHeight();
		Envelope tileBounds = new Envelope(centerX, centerX, centerY, centerY);
		List<RasterTile> tiles = rasterLayerService.getTiles(layerId, tileCrs, tileBounds, 1 / resolution);
		if (tiles.size() == 1) {
			log.debug("Rendering raster layer tile " + layerId + "/" + tileLevel + "-" + xIndex + "-" + yIndex);
			log.debug("Url = " + tiles.get(0).getUrl());
			log.debug("Tile = " + tiles.get(0));
			String url = tiles.get(0).getUrl();
			if (isRedirectRasterLayers()) {
				response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
				response.setHeader("Location", url);
			} else {
				writeToResponse(layer, url.replace(".jpeg", ".png"), request, response);
			}
		}
	}

	/**
	 * Renders the image by fetching it from the cache or, if that fails, using the rebuild container.
	 *
	 * @param layerId
	 * @param key
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

	private void writeToResponse(RasterLayer layer, String url, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		InputStream stream = null;
		try {
			response.setContentType("image/png");
			ServletOutputStream out = response.getOutputStream();
			stream = httpService.getStream(url, layer);
			int b;
			while ((b = stream.read()) >= 0) {
				out.write(b);
			}
		} catch (Exception e) { // NOSONAR
			log.error("Cannot get original TMS image", e);
			// Create an error image to make the reason for the error visible:
			RasterLayerInfo layerInfo = layer.getLayerInfo();
			byte[] b = createErrorImage(layerInfo.getTileWidth(), layerInfo.getTileHeight(), e);
			response.setContentType("image/png");
			response.getOutputStream().write(b);
		} finally {
			if (null != stream) {
				try {
					stream.close();
				} catch (IOException ioe) {
					// ignore, closing anyway
				}
			}
		}
	}

	/**
	 * Create an error image should an error occur while fetching a TMS map.
	 *
	 * @param width image width
	 * @param height image height
	 * @param e exception
	 * @return error image
	 * @throws java.io.IOException oops
	 */
	private byte[] createErrorImage(int width, int height, Exception e) throws IOException {
		String error = e.getMessage();
		if (null == error) {
			Writer result = new StringWriter();
			PrintWriter printWriter = new PrintWriter(result);
			e.printStackTrace(printWriter);
			error = result.toString();
		}

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D) image.getGraphics();

		g.setColor(Color.RED);
		g.drawString(error, ERROR_MESSAGE_X, height / 2);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(image, "PNG", out);
		out.flush();
		byte[] result = out.toByteArray();
		out.close();

		return result;
	}
}
