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
package org.geomajas.plugin.jmeter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.geomajas.command.CommandRequest;
import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.GetConfigurationRequest;
import org.geomajas.command.dto.GetConfigurationResponse;
import org.geomajas.command.dto.GetRasterTilesRequest;
import org.geomajas.command.dto.GetRasterTilesResponse;
import org.geomajas.command.dto.GetVectorTileRequest;
import org.geomajas.command.dto.GetVectorTileResponse;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientPreferredPixelsPerTile;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.configuration.client.ScaleConfigurationInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.layer.tile.VectorTile.VectorTileContentType;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

/**
 * Geomajas sampler that randomly fetches tiles.
 * 
 * @author Jan De Moerloose
 * 
 */
public class GeomajasSamplerClient extends AbstractJavaSamplerClient implements Serializable {

	private static final Logger LOG = LoggingManager.getLoggerForClass();

	private static final long serialVersionUID = 1L;

	private PerformanceCommandService commandDispatcher;

	private ClientApplicationInfo applicationInfo;

	private ClientMapInfo mapInfo;

	private static TileQueue TILE_QUEUE = new TileQueue();

	private Random random = new Random();

	private URL baseUrl;

	private String errorLabel;

	private File tileFolder;

	private String userToken;

	private static DefaultHttpClient HTTP_CLIENT;

	// set up default arguments for the JMeter GUI
	@Override
	public Arguments getDefaultParameters() {
		Arguments defaultParameters = new Arguments();
		defaultParameters.addArgument("baseUrl", "http://localhost:8888/");
		defaultParameters.addArgument("appId", "sampleApp");
		defaultParameters.addArgument("mapId", "sampleMap");
		defaultParameters.addArgument("layerIds", "");
		defaultParameters.addArgument("runMode", "random");
		defaultParameters.addArgument("tileCount", "100");
		defaultParameters.addArgument("saveTiles", "false");
		defaultParameters.addArgument("username", null);
		defaultParameters.addArgument("password", null);
		defaultParameters.addArgument("userToken", null);
		defaultParameters.addArgument("tileFolder", "/tmp/jmeter/tiles");
		return defaultParameters;
	}

	@Override
	public void setupTest(JavaSamplerContext context) {
		LOG.info("Set up GeomajasSamplerClient");
		if (HTTP_CLIENT == null) {
			PoolingClientConnectionManager connman = new PoolingClientConnectionManager();
			connman.setMaxTotal(20);
			connman.setDefaultMaxPerRoute(20);
			HTTP_CLIENT = new DefaultHttpClient(connman);
			if (context.getParameter("username") != null && !context.getParameter("username").isEmpty()) {
				HTTP_CLIENT.getCredentialsProvider().setCredentials(
						new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
						new UsernamePasswordCredentials(context.getParameter("username"), context
								.getParameter("password")));
			}
		}

		userToken = context.getParameter("userToken");
		try {
			super.setupTest(context);
			initCommandDispatcher(context);
			loadApplication(context);
			prepareTileRequests(context);
			String saveTiles = context.getParameter("saveTiles");
			if (saveTiles.equalsIgnoreCase("true")) {
				tileFolder = new File(context.getParameter("tileFolder"));
				if (tileFolder.mkdirs()) {
					LOG.info("Saving to tile directory " + tileFolder.getAbsolutePath());
				}
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("bad base URL", e);
		}
	}

	@Override
	public SampleResult runTest(JavaSamplerContext context) {
		LOG.info("Starting GeomajasSamplerClient 2");
		SampleResult result = new SampleResult();
		result.sampleStart(); // start stopwatch
		if (errorLabel != null) {
			result.setSampleLabel(errorLabel);
			result.setSuccessful(false);
			result.sampleEnd();
			return result;
		}
		int successCount = 0;
		int failurecount = 0;
		Map<String, Integer> layerCount = new HashMap<String, Integer>();
		CommandRequest request;
		while ((request = TILE_QUEUE.getNextRequest()) != null) {
			if (request instanceof GetVectorTileRequest) {
				GetVectorTileRequest vectorRequest = (GetVectorTileRequest) request;
				LOG.info("Executing vector request for layer " + vectorRequest.getLayerId());
				GetVectorTileResponse response = (GetVectorTileResponse) commandDispatcher.execute(
						GetVectorTileRequest.COMMAND, vectorRequest, userToken, null);
				try {
					if (consumeTile(vectorRequest.getLayerId(), response)) {
						LOG.info("Succesfully consumed vector tile ");
						String layerId = vectorRequest.getLayerId();
						if (!layerCount.containsKey(layerId)) {
							layerCount.put(layerId, 0);
						}
						layerCount.put(layerId, layerCount.get(layerId) + 1);
					} else {
						LOG.info("Vector tile not consumed ");
					}
					successCount++;
				} catch (Exception e) {
					failurecount++;
				}
			} else if (request instanceof GetRasterTilesRequest) {
				GetRasterTilesRequest rasterRequest = (GetRasterTilesRequest) request;
				LOG.info("Executing raster request for layer " + rasterRequest.getLayerId());
				GetRasterTilesResponse response = (GetRasterTilesResponse) commandDispatcher.execute(
						GetRasterTilesRequest.COMMAND, rasterRequest, userToken, null);
				try {
					if (consumeTile(rasterRequest.getLayerId(), response)) {
						LOG.info("Succesfully consumed raster tile ");
						String layerId = rasterRequest.getLayerId();
						if (!layerCount.containsKey(layerId)) {
							layerCount.put(layerId, 0);
						}
						layerCount.put(layerId, layerCount.get(layerId) + 1);
					} else {
						LOG.info("Raster tile not consumed ");
					}
					successCount++;
				} catch (Exception e) {
					failurecount++;
				}
			}
		}
		if (successCount > 0) {
			result.sampleEnd();
			result.setSuccessful(true);
			LOG.info("Found results for " + layerCount.size() + " layers");
			result.setSampleLabel(successCount + "/" + (failurecount + successCount) + " tiles successfull "
					+ getLayerStatistics(layerCount));
		} else {
			result.sampleEnd();
			result.setSuccessful(false);
		}
		return result;
	}

	protected String getLayerStatistics(Map<String, Integer> layerCount) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (String layerId : layerCount.keySet()) {
			if (sb.length() > 1) {
				sb.append(",");
			}
			sb.append(layerId);
			sb.append("(").append(layerCount.get(layerId)).append(")");
		}
		sb.append("]");
		return sb.toString();
	}

	protected boolean consumeTile(String layerId, GetVectorTileResponse response) throws Exception {
		if (response.getTile().getContentType() == VectorTileContentType.URL_CONTENT) {
			String url = response.getTile().getFeatureContent();
			url = url.contains("?") ? (url + "&userToken=" + userToken) : (url + "?userToken=" + userToken);
			url = url.replace(", magdageo-test.inside.vlaanderen.be:8000:8180", "");
			TileCode code = response.getTile().getCode();
			return saveUrlToFile(url, layerId + "-" + code.getCacheId());
		}
		LOG.info("Vector content is string");
		return false;
	}

	protected boolean consumeTile(String layerId, GetRasterTilesResponse response) throws Exception {
		boolean ok = false;
		for (RasterTile tile : response.getRasterData()) {
			String url = tile.getUrl();
			url = url.contains("?") ? (url + "&userToken=" + userToken) : (url + "?userToken=" + userToken);
			url = url.replace(", magdageo-test.inside.vlaanderen.be:8000:8180", "");
			URL tileUrl = new URL(url);
			TileCode code = tile.getCode();
			// only fetch the tiles from our own server
			if (tileUrl.getHost().equals(baseUrl.getHost())) {
				ok = saveUrlToFile(url, layerId + "-" + code.getCacheId());
			}
		}
		return ok;
	}

	protected boolean saveUrlToFile(String url, String id) throws IOException, ClientProtocolException,
			FileNotFoundException {
		HttpGet httpget = new HttpGet(url);
		HttpResponse r = HTTP_CLIENT.execute(httpget);
		HttpEntity entity = r.getEntity();
		String mimeType = entity.getContentType().getValue();
		LOG.info("Tile has mime type " + mimeType);
		if (tileFolder != null && tileFolder.exists() && tileFolder.isDirectory()) {
			File tmp = new File(tileFolder, id + "-" + getExtensionForMimeType(mimeType));
			FileOutputStream fos = new FileOutputStream(tmp);
			LOG.info("Saving to " + tmp.getAbsolutePath());
			entity.writeTo(fos);
			fos.flush();
			fos.close();
		} else {
			LOG.info("Saving to memory");
			ByteArrayOutputStream outstream = new ByteArrayOutputStream();
			entity.writeTo(outstream);
			outstream.flush();
			outstream.close();
		}
		return true;
	}

	protected String getExtensionForMimeType(String type) {
		MimeType mimeType = null;
		try {
			mimeType = MimeTypes.getDefaultMimeTypes().forName(type);
			return mimeType.getExtension();
		} catch (MimeTypeException e) {
			return ".png";
		}

	}

	protected void prepareTileRequests(JavaSamplerContext context) {
		String appId = context.getParameter("appId");
		if (applicationInfo == null) {
			errorLabel = "Application " + appId + " not found on server";
			return;
		}
		String mapId = context.getParameter("mapId");
		for (ClientMapInfo map : applicationInfo.getMaps()) {
			if (mapId.equals(map.getId())) {
				mapInfo = map;
				break;
			}
		}
		if (mapInfo == null) {
			errorLabel = "Map " + mapId + " not found in application " + appId;
			return;
		}
		if (mapInfo != null && TILE_QUEUE.isEmpty()) {
			String layerIds = context.getParameter("layerIds");
			if (!layerIds.isEmpty()) {
				String[] allowed = layerIds.split(",");
				Set<String> all = new HashSet<String>(Arrays.asList(allowed));
				for (ListIterator<ClientLayerInfo> it = mapInfo.getLayers().listIterator(); it.hasNext();) {
					String layerId = it.next().getId();
					if (!all.contains(layerId)) {
						it.remove();
						LOG.info("Layer " + layerId + " skipped");
					} else {
						LOG.info("Layer " + layerId + " included");
					}
				}
			}
			ScaleConfigurationInfo scaleInfo = mapInfo.getScaleConfiguration();
			int scaleCount = scaleInfo.getZoomLevels().size();
			int tileCount = context.getIntParameter("tileCount");
			String runMode = context.getParameter("runMode");
			if (runMode.equalsIgnoreCase("random")) {
				for (int i = 0; i < tileCount; i++) {
					// pick a random scale
					double scale = scaleInfo.getZoomLevels().get(random.nextInt(scaleCount)).getPixelPerUnit();
					// find possible layers
					List<ClientLayerInfo> layers = new ArrayList<ClientLayerInfo>();
					for (ClientLayerInfo layerInfo : mapInfo.getLayers()) {
						if (scale > layerInfo.getMinimumScale().getPixelPerUnit()
								&& scale < layerInfo.getMaximumScale().getPixelPerUnit()) {
							layers.add(layerInfo);
						}
					}
					if (layers.size() > 0) {
						// pick a random layer
						ClientLayerInfo layer = layers.get(random.nextInt(layers.size()));
						// create the request
						if (layer instanceof ClientVectorLayerInfo) {
							GetVectorTileRequest vectorTileRequest = new GetVectorTileRequest();
							// find best level for scale
							int level = calculateTileLevel(layer, scale);
							// pick a random x and y
							int x = random.nextInt((int) Math.pow(2, level));
							int y = random.nextInt((int) Math.pow(2, level));
							vectorTileRequest.setCode(new TileCode(level, x, y));
							vectorTileRequest.setCrs(mapInfo.getCrs());
							vectorTileRequest.setLayerId(layer.getServerLayerId());
							vectorTileRequest.setPaintGeometries(true);
							vectorTileRequest.setPaintLabels(true);
							vectorTileRequest.setPanOrigin(new Coordinate(0, 0));
							vectorTileRequest.setScale(scale);
							vectorTileRequest.setStyleInfo(((ClientVectorLayerInfo) layer).getNamedStyleInfo());
							vectorTileRequest.setRenderer("SVG");
							TILE_QUEUE.addRequest(vectorTileRequest);
						} else {
							GetRasterTilesRequest rasterTilesRequest = new GetRasterTilesRequest();
							// find best level for scale
							int level = calculateTileLevel(layer, scale);
							// pick a random x and y
							int x = random.nextInt((int) Math.pow(2, level));
							int y = random.nextInt((int) Math.pow(2, level));
							TileCode tilecode = new TileCode(level, x, y);
							rasterTilesRequest.setBbox(calcBoundsForTileCode(layer, tilecode, scale));
							rasterTilesRequest.setCrs(mapInfo.getCrs());
							rasterTilesRequest.setLayerId(layer.getServerLayerId());
							rasterTilesRequest.setScale(scale);
							TILE_QUEUE.addRequest(rasterTilesRequest);
						}
					}
				}
			} else {
				int i = 0;
				while (i < tileCount) {
					for (int scaleIndex = 0; scaleIndex < scaleInfo.getZoomLevels().size(); scaleIndex++) {
						// pick next scale
						double scale = scaleInfo.getZoomLevels().get(scaleIndex).getPixelPerUnit();
						// find possible layers
						List<ClientLayerInfo> layers = new ArrayList<ClientLayerInfo>();
						for (ClientLayerInfo layerInfo : mapInfo.getLayers()) {
							if (scale > layerInfo.getMinimumScale().getPixelPerUnit()
									&& scale < layerInfo.getMaximumScale().getPixelPerUnit()) {
								layers.add(layerInfo);
							}
						}
						for (int layerIndex = 0; layerIndex < layers.size(); layerIndex++) {
							ClientLayerInfo layer = layers.get(layerIndex);
							// create the request
							if (layer instanceof ClientVectorLayerInfo) {
								// find best level for scale
								int level = calculateTileLevel(layer, scale);
								// pick a random x and y
								for (int x = 0; x < (int) Math.pow(2, level); x++) {
									for (int y = 0; y < (int) Math.pow(2, level); y++) {
										GetVectorTileRequest vectorTileRequest = new GetVectorTileRequest();
										vectorTileRequest.setCode(new TileCode(level, x, y));
										vectorTileRequest.setCrs(mapInfo.getCrs());
										vectorTileRequest.setLayerId(layer.getServerLayerId());
										vectorTileRequest.setPaintGeometries(true);
										vectorTileRequest.setPaintLabels(true);
										vectorTileRequest.setPanOrigin(new Coordinate(0, 0));
										vectorTileRequest.setScale(scale);
										vectorTileRequest.setStyleInfo(((ClientVectorLayerInfo) layer)
												.getNamedStyleInfo());
										vectorTileRequest.setRenderer("SVG");
										TILE_QUEUE.addRequest(vectorTileRequest);
										LOG.info("added tilecode " + vectorTileRequest.getCode());
										i++;
										if (i >= tileCount) {
											return;
										}
									}
								}
							} else {
								// find best level for scale
								int level = calculateTileLevel(layer, scale);
								// pick a random x and y
								for (int x = 0; x < (int) Math.pow(2, level); x++) {
									for (int y = 0; y < (int) Math.pow(2, level); y++) {
										GetRasterTilesRequest rasterTilesRequest = new GetRasterTilesRequest();
										TileCode tilecode = new TileCode(level, x, y);
										rasterTilesRequest.setBbox(calcBoundsForTileCode(layer, tilecode, scale));
										rasterTilesRequest.setCrs(mapInfo.getCrs());
										rasterTilesRequest.setLayerId(layer.getServerLayerId());
										rasterTilesRequest.setScale(scale);
										TILE_QUEUE.addRequest(rasterTilesRequest);
										i++;
										if (i >= tileCount) {
											return;
										}
									}
								}
							}
						}
					}
					break;
				}
			}
		}
		LOG.info("Prepared " + TILE_QUEUE.getSize() + " tiles");
	}

	/**
	 * Calculate the exact bounding box for a tile, given it's tile-code.
	 * 
	 * @param tileCode tile code
	 * @return bbox for tile
	 */
	protected Bbox calcBoundsForTileCode(ClientLayerInfo layer, TileCode tileCode, double scale) {
		Bbox layerBounds = layer.getMaxExtent();
		// Calculate tile width and height for tileLevel=tileCode.getTileLevel()
		double div = Math.pow(2, tileCode.getTileLevel());
		double tileWidth = Math.ceil((scale * layerBounds.getWidth()) / div) / scale;
		double tileHeight = Math.ceil((scale * layerBounds.getHeight()) / div) / scale;

		// Now calculate indices, and return bbox:
		double x = layerBounds.getX() + tileCode.getX() * tileWidth;
		double y = layerBounds.getY() + tileCode.getY() * tileHeight;
		return new Bbox(x, y, tileWidth, tileHeight);
	}

	protected void loadApplication(JavaSamplerContext context) {
		GetConfigurationRequest request = new GetConfigurationRequest();
		request.setApplicationId(context.getParameter("appId"));
		CommandResponse response = commandDispatcher.execute(GetConfigurationRequest.COMMAND, request, userToken, null);
		GetConfigurationResponse configResponse = (GetConfigurationResponse) response;
		applicationInfo = configResponse.getApplication();
	}

	protected void initCommandDispatcher(JavaSamplerContext context) throws MalformedURLException {
		baseUrl = new URL(context.getParameter("baseUrl"));
		HttpInvokerProxyFactoryBean fb = new HttpInvokerProxyFactoryBean();
		fb.setServiceInterface(PerformanceCommandService.class);
		fb.setServiceUrl(context.getParameter("baseUrl"));
		HttpComponentsHttpInvokerRequestExecutor executor = new HttpComponentsHttpInvokerRequestExecutor();
		executor.setHttpClient(HTTP_CLIENT);
		fb.setHttpInvokerRequestExecutor(executor);
		fb.afterPropertiesSet();
		commandDispatcher = (PerformanceCommandService) fb.getObject();
	}

	protected int calculateTileLevel(ClientLayerInfo layer, double scale) {
		Bbox layerBounds = layer.getMaxExtent();
		double baseX = layerBounds.getWidth();
		double baseY = layerBounds.getHeight();
		// choose the tile level so the area is between minimumTileSize and the next level (minimumTileSize * 4)
		double baseArea = baseX * baseY;
		double osmArea = getPreferredPixelsPerTile() / (scale * scale);
		int tileLevel = (int) Math.round(Math.log(baseArea / osmArea) / Math.log(4.0));
		if (tileLevel < 0) {
			tileLevel = 0;
		}
		return tileLevel;
	}

	protected int getPreferredPixelsPerTile() {
		ClientPreferredPixelsPerTile ppt = mapInfo.getPreferredPixelsPerTile();
		switch (ppt.getPreferredPixelsPerTileType()) {
			case MAP:
				return 1024 * 1024;
			case CONFIGURED:
			default:
				return ppt.getWidth() * ppt.getHeight();
		}
	}
}