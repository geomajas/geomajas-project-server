/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.wmsclient.client.service;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.plugin.wmsclient.client.capabilities.WmsGetCapabilitiesInfo;
import org.geomajas.plugin.wmsclient.client.capabilities.v1_1_1.WmsGetCapabilitiesInfo111;
import org.geomajas.plugin.wmsclient.client.capabilities.v1_3_0.WmsGetCapabilitiesInfo130;
import org.geomajas.plugin.wmsclient.client.layer.FeaturesSupportedWmsLayer;
import org.geomajas.plugin.wmsclient.client.layer.WmsLayer;
import org.geomajas.plugin.wmsclient.client.layer.WmsLayerConfiguration;
import org.geomajas.plugin.wmsclient.server.command.dto.GetFeatureInfoRequest;
import org.geomajas.plugin.wmsclient.server.command.dto.GetFeatureInfoResponse;
import org.geomajas.puregwt.client.map.feature.Feature;
import org.geomajas.puregwt.client.map.feature.FeatureFactory;

import com.google.gwt.core.client.Callback;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;
import com.google.inject.Inject;

/**
 * Default implementation of the {@link WmsService}.
 * 
 * @author Pieter De Graef
 */
public class WmsServiceImpl implements WmsService {

	private static final NumberFormat NUMBERFORMAT = NumberFormat.getFormat("#0.0#");

	@Inject
	private WmsTileService tileService;

	@Inject
	private FeatureFactory featureFactory;

	private WmsUrlTransformer urlTransformer;

	// ------------------------------------------------------------------------
	// WMS GetCapabilities methods:
	// ------------------------------------------------------------------------

	/**
	 * Get the capabilities information of a WMS service.
	 * 
	 * @param baseUrl
	 *            The WMS base URL (without any WMS parameters).
	 * @param version
	 *            The preferred WMS version.
	 * @param callback
	 *            Callback that returns a {@link WmsGetCapabilitiesInfo} instance on success. From here, you can extract
	 *            all the information or layers defined in the capabilities file.
	 */
	public void getCapabilities(String baseUrl, final WmsVersion version,
			final Callback<WmsGetCapabilitiesInfo, String> callback) {
		String url = getCapabilitiesUrl(baseUrl, version);
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		try {
			builder.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable e) {
					callback.onFailure(e.getMessage());
				}

				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						Document messageDom = XMLParser.parse(response.getText());
						WmsGetCapabilitiesInfo capabilities;
						switch (version) {
							case V1_1_1:
								capabilities = new WmsGetCapabilitiesInfo111(messageDom.getDocumentElement());
								break;
							case V1_3_0:
								capabilities = new WmsGetCapabilitiesInfo130(messageDom.getDocumentElement());
								break;
							default:
								callback.onFailure("Unsupported version");
								return;
						}
						callback.onSuccess(capabilities);
					} else {
						callback.onFailure(response.getText());
					}
				}
			});
		} catch (RequestException e) {
			// Couldn't connect to server
			callback.onFailure(e.getMessage());
		}
	}

	// ------------------------------------------------------------------------
	// WmsService implementation:
	// ------------------------------------------------------------------------

	@Override
	public String getMapUrl(WmsLayerConfiguration wmsConfig, String crs, Bbox worldBounds, int imageWidth,
			int imageHeight) {
		StringBuilder url = getBaseUrlBuilder(wmsConfig);

		// Add the base parameters needed for getMap:
		addBaseParameters(url, wmsConfig, crs, worldBounds, imageWidth, imageHeight);

		// Parameter: request type
		url.append("&request=GetMap");

		return finishUrl(WmsRequest.GETMAP, url);
	}

	@Override
	public void getFeatureInfo(final FeaturesSupportedWmsLayer layer, Coordinate location,
			final Callback<List<Feature>, String> callback) {
		String url = getFeatureInfoUrl(layer, location, GetFeatureInfoFormat.GML2);
		GwtCommand command = new GwtCommand(GetFeatureInfoRequest.COMMAND_NAME);
		command.setCommandRequest(new GetFeatureInfoRequest(url));
		GwtCommandDispatcher.getInstance().execute(command, new CommandCallback<GetFeatureInfoResponse>() {

			public void execute(GetFeatureInfoResponse response) {
				List<Feature> features = new ArrayList<Feature>();
				for (org.geomajas.layer.feature.Feature feature : response.getFeatures()) {
					features.add(featureFactory.create(feature, layer));
				}
				callback.onSuccess(features);
			}
		});
	}

	@Override
	public void getFeatureInfo(FeaturesSupportedWmsLayer layer, Coordinate location, GetFeatureInfoFormat format,
			final Callback<Object, String> callback) {
		String url = getFeatureInfoUrl(layer, location, format);
		GwtCommand command = new GwtCommand(GetFeatureInfoRequest.COMMAND_NAME);
		command.setCommandRequest(new GetFeatureInfoRequest(url));
		GwtCommandDispatcher.getInstance().execute(command, new CommandCallback<GetFeatureInfoResponse>() {

			public void execute(GetFeatureInfoResponse response) {
				if (response.getFeatures() != null) {
					callback.onSuccess(response.getFeatures());
				} else {
					callback.onSuccess(response.getWmsResponse());
				}
			}
		});
	}

	// ------------------------------------------------------------------------
	// WMS GetLegendGraphic methods:
	// ------------------------------------------------------------------------

	@Override
	public String getLegendGraphicUrl(WmsLayerConfiguration wmsConfig) {
		StringBuilder url = getBaseUrlBuilder(wmsConfig);

		// Parameter: service
		int pos = url.lastIndexOf("?");
		if (pos > 0) {
			url.append("&service=WMS");
		} else {
			url.append("?service=WMS");
		}

		// Parameter: layers
		url.append("&layer=");
		url.append(wmsConfig.getLayers()); // No URL.encode here!

		// Parameter: request type
		url.append("&request=GetLegendGraphic");

		// Parameter: format
		url.append("&format=image/png");

		// Parameter: width
		url.append("&width=");
		url.append(Integer.toString(wmsConfig.getLegendWidth()));

		// Parameter: height
		url.append("&height=");
		url.append(Integer.toString(wmsConfig.getLegendHeight()));

		// Parameter: transparent
		url.append("&transparent=true");

		return finishUrl(WmsRequest.GETLEGENDGRAPHIC, url);
	}

	// ------------------------------------------------------------------------
	// Proxy options:
	// ------------------------------------------------------------------------

	@Override
	public void setWmsUrlTransformer(WmsUrlTransformer urlTransformer) {
		this.urlTransformer = urlTransformer;
	}

	@Override
	public WmsUrlTransformer getWmsUrlTransformer() {
		return urlTransformer;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private String getFeatureInfoUrl(WmsLayer layer, Coordinate location, GetFeatureInfoFormat format) {
		StringBuilder url = getBaseUrlBuilder(layer.getConfig());

		TileCode tileCode = tileService.getTileCodeForLocation(layer.getViewPort(), layer.getTileConfig(), location,
				layer.getViewPort().getScale());
		Bbox worldBounds = tileService.getWorldBoundsForTile(layer.getViewPort(), layer.getTileConfig(), tileCode);

		Bbox screenBounds = layer.getViewPort().transform(worldBounds, RenderSpace.WORLD, RenderSpace.SCREEN);
		Coordinate screenLocation = layer.getViewPort().transform(location, RenderSpace.WORLD, RenderSpace.SCREEN);

		// Add the base parameters needed for getMap:
		addBaseParameters(url, layer.getConfig(), layer.getCrs(), worldBounds, layer.getTileConfig().getTileWidth(),
				layer.getTileConfig().getTileHeight());

		url.append("&QUERY_LAYERS=");
		url.append(layer.getConfig().getLayers()); // No URL.encode here!
		url.append("&request=GetFeatureInfo");
		switch (layer.getConfig().getVersion()) {
			case V1_3_0:
				url.append("&I=");
				url.append((int) Math.round(screenLocation.getX() - screenBounds.getX()));
				url.append("&J=");
				url.append((int) Math.round(screenLocation.getY() - screenBounds.getY()));
				break;
			case V1_1_1:
			default:
				url.append("&X=");
				url.append((int) Math.round(screenLocation.getX() - screenBounds.getX()));
				url.append("&Y=");
				url.append((int) Math.round(screenLocation.getY() - screenBounds.getY()));
		}
		url.append("&INFO_FORMAT=");
		url.append(format.toString());

		return finishUrl(WmsRequest.GETFEATUREINFO, url);
	}

	private StringBuilder getBaseUrlBuilder(WmsLayerConfiguration config) {
		return new StringBuilder(config.getBaseUrl());
	}

	private String finishUrl(WmsRequest request, StringBuilder builder) {
		String url = builder.toString();
		if (urlTransformer != null) {
			url = urlTransformer.transform(request, url);
		}
		return URL.encode(url);
	}

	private StringBuilder addBaseParameters(StringBuilder url, WmsLayerConfiguration config, String crs,
			Bbox worldBounds, int imageWidth, int imageHeight) {
		// Parameter: service
		int pos = url.lastIndexOf("?");
		if (pos > 0) {
			url.append("&service=WMS");
		} else {
			url.append("?service=WMS");
		}

		// Parameter: layers
		url.append("&layers=");
		url.append(config.getLayers()); // No URL.encode here, performed in finishUrl

		// Parameter: width
		url.append("&width=");
		url.append(Integer.toString(imageWidth));

		// Parameter: height
		url.append("&height=");
		url.append(Integer.toString(imageHeight));

		// Parameter: bbox
		url.append("&bbox=");
		if (useInvertedAxis(config.getVersion(), crs)) {
			// Replace 
			url.append(floatToStringWithDecimalPoint((worldBounds.getY())));
			url.append(",");
			url.append(floatToStringWithDecimalPoint(worldBounds.getX()));
			url.append(",");
			url.append(floatToStringWithDecimalPoint(worldBounds.getMaxY()));
			url.append(",");
			url.append(floatToStringWithDecimalPoint(worldBounds.getMaxX()));
		} else {
			url.append(floatToStringWithDecimalPoint(worldBounds.getX()));
			url.append(",");
			url.append(floatToStringWithDecimalPoint(worldBounds.getY()));
			url.append(",");
			url.append(floatToStringWithDecimalPoint(worldBounds.getMaxX()));
			url.append(",");
			url.append(floatToStringWithDecimalPoint(worldBounds.getMaxY()));
		}

		// Parameter: format
		url.append("&format=");
		url.append(config.getFormat());

		// Parameter: version
		url.append("&version=");
		url.append(config.getVersion().toString());

		// Parameter: crs/srs
		switch (config.getVersion()) {
			case V1_1_1:
				url.append("&srs=");
				break;
			case V1_3_0:
			default:
				url.append("&crs=");
				break;
		}
		url.append(crs);  // No URL.encode here, performed in finishUrl

		// Parameter: styles
		url.append("&styles=");
		url.append(config.getStyles());

		// Parameter: transparent
		if (config.isTransparent()) {
			url.append("&transparent=");
			url.append(config.isTransparent());
		}

		// Return the URL:
		return url;
	}

	private static String floatToStringWithDecimalPoint(double number) {
		return NUMBERFORMAT.format(number).replace(",", ".");
	}

	private String getCapabilitiesUrl(String baseUrl, WmsVersion version) {
		StringBuilder url = new StringBuilder(baseUrl);

		// Parameter: Service
		int pos = url.lastIndexOf("?");
		if (pos > 0) {
			url.append("&service=WMS");
		} else {
			url.append("?service=WMS");
		}

		// Parameter: Version
		url.append("&version=");
		url.append(version.toString());

		// Parameter: request type
		url.append("&request=GetCapabilities");

		return finishUrl(WmsRequest.GETCAPABILITIES, url);
	}

	private boolean useInvertedAxis(WmsVersion version, String crs) {
		if (WmsVersion.V1_3_0.equals(version) && ("EPSG:4326".equalsIgnoreCase(crs) || 
				"WGS:84".equalsIgnoreCase(crs))) {
			return true;
		}
		return false;
	}
}