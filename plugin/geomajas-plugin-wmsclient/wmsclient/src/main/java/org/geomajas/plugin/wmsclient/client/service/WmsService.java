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

import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.plugin.wmsclient.client.capabilities.WmsGetCapabilitiesInfo;
import org.geomajas.plugin.wmsclient.client.layer.FeaturesSupportedWmsLayer;
import org.geomajas.plugin.wmsclient.client.layer.WmsLayerConfiguration;
import org.geomajas.puregwt.client.map.feature.Feature;

import com.google.gwt.core.client.Callback;

/**
 * Client service that assists in performing requests to the WMS server.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface WmsService {

	/**
	 * Supported format for the WMS GetFeatureInfo request.
	 * 
	 * @author Pieter De Graef
	 */
	public enum GetFeatureInfoFormat {
		GML2("application/vnd.ogc.gml"), GML3("application/vnd.ogc.gml/3.1.1"), HTML("text/html");

		private String format;

		private GetFeatureInfoFormat(String format) {
			this.format = format;
		}

		public String toString() {
			return format;
		}
	}

	/**
	 * WMS version enumeration.
	 * 
	 * @author Pieter De Graef
	 */
	public enum WmsVersion {
		v1_1_1("1.1.1"), v1_3_0("1.3.0");

		private String version;

		private WmsVersion(String version) {
			this.version = version;
		}

		public String toString() {
			return version;
		}
	}

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
	void getCapabilities(String baseUrl, WmsVersion version, Callback<WmsGetCapabilitiesInfo, String> callback);

	// ------------------------------------------------------------------------
	// WMS GetMap methods:
	// ------------------------------------------------------------------------

	/**
	 * Get the URL that retrieves the requested bounds for the requested layer through a GetMap request.
	 * 
	 * @param wmsConfig
	 *            The configuration object that points to some WMS layer.
	 * @param crs
	 *            The preferred coordinate reference system.
	 * @param worldBounds
	 *            The bounds to retrieve through the GetMap request.
	 * @param imageWidth
	 *            The image width.
	 * @param imageHeight
	 *            The image height.
	 * @return URL to the image.
	 */
	String getMapUrl(WmsLayerConfiguration wmsConfig, String crs, Bbox worldBounds, int imageWidth, int imageHeight);

	// ------------------------------------------------------------------------
	// WMS GetFeatureInfo methods:
	// ------------------------------------------------------------------------

	/**
	 * Execute a WMS GetFeatureInfo request. This request will use the format 'application/vnd.ogc.gml', so that the
	 * response can be parsed.
	 * 
	 * @param layer
	 *            The {@link FeaturesSupportedWmsLayer} to search features for. Note that a normal
	 *            {@link org.geomajas.plugin.wmsclient.client.layer.WmsLayer} is not enough. It must support the
	 *            GetFeatureInfo request.
	 * @param location
	 *            The location to search at. Must be in the map CRS.
	 * @param cb
	 *            The callback that will return a list of {@link Feature}s that have been found at the location. This
	 *            can be an empty list.
	 */
	void getFeatureInfo(FeaturesSupportedWmsLayer layer, Coordinate location, Callback<List<Feature>, String> cb);

	/**
	 * Execute a WMS GetFeatureInfo request.
	 * 
	 * @param layer
	 *            The {@link FeaturesSupportedWmsLayer} to search features for. Note that a normal
	 *            {@link org.geomajas.plugin.wmsclient.client.layer.WmsLayer} is not enough. It must support the
	 *            GetFeatureInfo request.
	 * @param location
	 *            The location to search at. Must be in the map CRS.
	 * @param format
	 *            The requested format for the response.
	 * @param cb
	 *            The callback that will return the response from the WMS server directly as a string. It is up to you
	 *            to parse this response.
	 */
	void getFeatureInfo(FeaturesSupportedWmsLayer layer, Coordinate location, GetFeatureInfoFormat format,
			Callback<String, String> cb);

	// ------------------------------------------------------------------------
	// WMS GetLegendGraphic methods:
	// ------------------------------------------------------------------------

	/**
	 * Get the URL that points to the legend graphic of a WMS layer through a WMS GetLegendGraphic request.
	 * 
	 * @param wmsConfig
	 *            The configuration object that points to some WMS layer.
	 * @return Returns the URL that points to the legend image.
	 */
	String getLegendGraphicUrl(WmsLayerConfiguration wmsConfig);
}