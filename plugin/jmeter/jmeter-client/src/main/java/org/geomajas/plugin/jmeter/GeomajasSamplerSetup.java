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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
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
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
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
import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientPreferredPixelsPerTile;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.configuration.client.ScaleConfigurationInfo;
import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.layer.tile.VectorTile.VectorTileContentType;
import org.geomajas.plugin.rasterizing.command.dto.LegendRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.MapRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.RasterLayerRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.RasterizeMapRequest;
import org.geomajas.plugin.rasterizing.command.dto.RasterizeMapResponse;
import org.geomajas.plugin.rasterizing.command.dto.VectorLayerRasterizingInfo;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

/**
 * Singleton setup class for all {@link GeomajasSamplerClient} instances. First {@link GeomajasSamplerClient} calling
 * {@link #getInstance(JavaSamplerContext)} will create the singleton.
 * 
 * @author Jan De Moerloose
 * 
 */
public class GeomajasSamplerSetup {

	private static final Logger LOG = LoggingManager.getLoggerForClass();

	private TileQueue tileQueue = new TileQueue();

	private PerformanceCommandService commandDispatcher;

	private ClientApplicationInfo applicationInfo;

	private ClientMapInfo mapInfo;

	private Random random = new Random();

	private URL baseUrl;

	private String errorLabel;

	private File tileFolder;

	private String userToken;

	private DefaultHttpClient httpClient;

	private static volatile GeomajasSamplerSetup instance;

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static GeomajasSamplerSetup getInstance(JavaSamplerContext context) {
		// CHECKSTYLE: OFF
		if (instance == null) {
			synchronized (GeomajasSamplerSetup.class) {
				if (instance == null) {
					instance = new GeomajasSamplerSetup(context);
				}
			}
		}
		return instance;
		// CHECKSTYLE: ON
	}

	public GeomajasSamplerSetup(JavaSamplerContext context) {
		LOG.info("Set up GeomajasSamplerClient");
		if (httpClient == null) {
			PoolingClientConnectionManager connman = new PoolingClientConnectionManager();
			connman.setMaxTotal(20);
			connman.setDefaultMaxPerRoute(20);
			httpClient = new DefaultHttpClient(connman);
			if (context.getParameter("username") != null && !context.getParameter("username").isEmpty()) {
				httpClient.getCredentialsProvider().setCredentials(
						new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
						new UsernamePasswordCredentials(context.getParameter("username"), context
								.getParameter("password")));
			}
		}

		userToken = context.getParameter("userToken");
		try {
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

	public boolean consumeTile(String layerId, GetRasterTilesResponse response) throws Exception {
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

	public boolean consumeTile(RasterizeMapResponse response) throws Exception {
		boolean ok = false;
		String url = response.getMapUrl();
		url = url.contains("?") ? (url + "&userToken=" + userToken) : (url + "?userToken=" + userToken);
		url = url.replace(", magdageo-test.inside.vlaanderen.be:8000:8180", "");
		URL mapUrl = new URL(url);
		// only fetch the tiles from our own server
		if (mapUrl.getHost().equals(baseUrl.getHost())) {
			ok = saveUrlToFile(url, response.getMapKey() + ".png");
		}
		return ok;
	}

	protected boolean saveUrlToFile(String url, String id) throws IOException, ClientProtocolException,
			FileNotFoundException {
		HttpGet httpget = new HttpGet(url);
		HttpResponse r = httpClient.execute(httpget);
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

	protected void initCommandDispatcher(JavaSamplerContext context) throws MalformedURLException {
		baseUrl = new URL(context.getParameter("baseUrl"));
		HttpInvokerProxyFactoryBean fb = new HttpInvokerProxyFactoryBean();
		fb.setServiceInterface(PerformanceCommandService.class);
		fb.setServiceUrl(context.getParameter("baseUrl"));
		HttpComponentsHttpInvokerRequestExecutor executor = new HttpComponentsHttpInvokerRequestExecutor();
		executor.setHttpClient(httpClient);
		fb.setHttpInvokerRequestExecutor(executor);
		fb.afterPropertiesSet();
		commandDispatcher = (PerformanceCommandService) fb.getObject();
	}

	protected void loadApplication(JavaSamplerContext context) {
		GetConfigurationRequest request = new GetConfigurationRequest();
		request.setApplicationId(context.getParameter("appId"));
		CommandResponse response = commandDispatcher.execute(GetConfigurationRequest.COMMAND, request, userToken, null);
		GetConfigurationResponse configResponse = (GetConfigurationResponse) response;
		applicationInfo = configResponse.getApplication();
		// fix scale bug
		for (ClientMapInfo map : applicationInfo.getMaps()) {
			for (ScaleInfo scale : map.getScaleConfiguration().getZoomLevels()) {
				fix(scale);
			}
			fix(map.getScaleConfiguration().getMaximumScale());
			for (ClientLayerInfo layer : map.getLayers()) {
				fix(layer.getMaximumScale());
				fix(layer.getMinimumScale());
				fix(layer.getZoomToPointScale());
			}
		}
	}

	private void fix(ScaleInfo scale) {
		scale.setConversionFactor(ScaleInfo.PIXEL_PER_METER);
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
		if (mapInfo != null && tileQueue.isEmpty()) {
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
			} else {
				for (ListIterator<ClientLayerInfo> it = mapInfo.getLayers().listIterator(); it.hasNext();) {
					LOG.info("Layer " + it.next().getId() + " found");
				}
			}
			ScaleConfigurationInfo scaleInfo = mapInfo.getScaleConfiguration();
			int scaleCount = scaleInfo.getZoomLevels().size();
			int tileCount = context.getIntParameter("tileCount");
			String runMode = context.getParameter("runMode");

			// print stuff
			boolean print = context.getParameter("printMap").equalsIgnoreCase("true");
			int printWidthInPixels = context.getIntParameter("printWidthInPixels");
			int printHeightInPixels = context.getIntParameter("printHeightInPixels");

			if (runMode.equalsIgnoreCase("random") || print) {
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
						if (print) {
							ClientMapInfo map = createPrintableMap(printWidthInPixels, printHeightInPixels, scale,
									layers);
							RasterizeMapRequest rasterizeMapRequest = new RasterizeMapRequest();
							rasterizeMapRequest.setClientMapInfo(map);
							tileQueue.addRequest(rasterizeMapRequest);
						} else {
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
								tileQueue.addRequest(vectorTileRequest);
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
								tileQueue.addRequest(rasterTilesRequest);
							}
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
										tileQueue.addRequest(vectorTileRequest);
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
										tileQueue.addRequest(rasterTilesRequest);
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
		LOG.info("Prepared " + tileQueue.getSize() + " tiles");
	}

	private ClientMapInfo createPrintableMap(int printWidthInPixels, int printHeightInPixels, double scale,
			List<ClientLayerInfo> layers) {
		ClientMapInfo printMap = new ClientMapInfo();
		printMap.setBackgroundColor(mapInfo.getBackgroundColor());
		printMap.setCrs(mapInfo.getCrs());
		printMap.setDisplayUnitType(mapInfo.getDisplayUnitType());
		printMap.setId(mapInfo.getId());
		printMap.setInitialBounds(mapInfo.getInitialBounds());
		printMap.setLayers(mapInfo.getLayers());
		printMap.setLineSelectStyle(mapInfo.getLineSelectStyle());
		printMap.setPointSelectStyle(mapInfo.getPointSelectStyle());
		printMap.setPolygonSelectStyle(mapInfo.getPolygonSelectStyle());
		printMap.setScaleConfiguration(mapInfo.getScaleConfiguration());
		MapRasterizingInfo mapRasterizingInfo = new MapRasterizingInfo();
		printMap.getWidgetInfo().put(MapRasterizingInfo.WIDGET_KEY, mapRasterizingInfo);
		double width = printWidthInPixels / scale;
		double height = printHeightInPixels / scale;
		Coordinate center = BboxService.getCenterPoint(printMap.getInitialBounds());
		Bbox bounds = new Bbox(center.getX() - 0.5 * width, center.getY() - 0.5 * height, width, height);
		mapRasterizingInfo.setBounds(bounds);
		mapRasterizingInfo.setTransparent(true);
		mapRasterizingInfo.setScale(scale);
		mapRasterizingInfo.setDpi(96);
		for (ClientLayerInfo clientLayerInfo : printMap.getLayers()) {
			if (clientLayerInfo instanceof ClientVectorLayerInfo) {
				ClientVectorLayerInfo vectorLayerInfo = (ClientVectorLayerInfo) clientLayerInfo;
				VectorLayerRasterizingInfo vectorLayerRasterizingInfo = new VectorLayerRasterizingInfo();
				vectorLayerRasterizingInfo.setPaintGeometries(true);
				vectorLayerRasterizingInfo.setStyle(vectorLayerInfo.getNamedStyleInfo());
				vectorLayerRasterizingInfo.setShowing(layers.contains(vectorLayerInfo));
				vectorLayerInfo.getWidgetInfo().put(VectorLayerRasterizingInfo.WIDGET_KEY, vectorLayerRasterizingInfo);
			} else if (clientLayerInfo instanceof ClientRasterLayerInfo) {
				ClientRasterLayerInfo rasterLayerInfo = (ClientRasterLayerInfo) clientLayerInfo;
				RasterLayerRasterizingInfo rasterLayerRasterizingInfo = new RasterLayerRasterizingInfo();
				rasterLayerRasterizingInfo.setCssStyle(rasterLayerInfo.getStyle());
				rasterLayerRasterizingInfo.setShowing(layers.contains(rasterLayerInfo));
				rasterLayerInfo.getWidgetInfo().put(RasterLayerRasterizingInfo.WIDGET_KEY, rasterLayerRasterizingInfo);
			}
		}
		LegendRasterizingInfo legend = new LegendRasterizingInfo();
		FontStyleInfo fontStyleInfo = new FontStyleInfo();
		fontStyleInfo.applyDefaults();
		legend.setFont(fontStyleInfo);
		legend.setTitle("Jmeter");
		legend.setWidth(100);
		legend.setHeight(500);
		mapRasterizingInfo.setLegendRasterizingInfo(legend);
		return printMap;
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

	public String getErrorLabel() {
		return errorLabel;
	}

	public CommandRequest getNextRequest() {
		CommandRequest request = tileQueue.getNextRequest();
		LOG.info(tileQueue.getSize() + " requests left");
		return request;
	}

	public CommandResponse execute(String commandName, CommandRequest request) {
		return commandDispatcher.execute(commandName, request, userToken, null);
	}

	// CHECKSTYLE: OFF
	public static void tearDown() {
		// prepare for next run
		instance = null;
	}
	// CHECKSTYLE: ON

}
