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

package org.geomajas.plugin.wmsclient.client.layer.config;

import java.io.Serializable;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.wmsclient.client.service.WmsService.WmsVersion;
import org.geomajas.puregwt.client.map.layer.LegendConfig;

/**
 * General WMS configuration object. The values herein will be translated into parameters for the WMS service. Note that
 * this configuration object has fields that are not directly supported through WMS. Some WMS vendors have added such
 * extra options though, so be sure to specify the {@link WmsServiceVendor} if possible.
 * 
 * @author Pieter De Graef
 * @author An Buyle
 * @since 1.0.0
 */
@Api(allMethods = true)
public class WmsLayerConfiguration implements Serializable {

	private static final long serialVersionUID = 100L;

	private String baseUrl;

	private String format = "image/png";

	private String layers = "";

	private String styles = "";

	private String filter; // CQL in case the WMS server supports it.

	private boolean transparent = true;

	private WmsVersion version = WmsVersion.V1_3_0;

	private LegendConfig legendConfig = new LegendConfig();

	private WmsServiceVendor wmsServiceVendor = WmsServiceVendor.UNSPECIFIED;

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	/**
	 * Get the type of service that provides the WMS. This can be a specific brand, such as GeoServer.
	 * 
	 * @return Type of service that provides the WMS service.
	 */
	public WmsServiceVendor getWmsServiceVendor() {
		return wmsServiceVendor;
	}

	/**
	 * Set the WMS service type to used. This may trigger vendor specific options to be used. E.g. If the WMS service is
	 * provided by a Geoserver, there is the possibility to configure the legend_options when performing a
	 * GetLegendGraphic request.
	 * 
	 * @param wmsServiceVendor
	 */
	public void setWmsServiceVendor(WmsServiceVendor wmsServiceVendor) {
		this.wmsServiceVendor = wmsServiceVendor;
	}

	/**
	 * Get the GetMap image format. The default value is "image/png".
	 * 
	 * @return The GetMap image format.
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * Set the GetMap image format. The default value is "image/png".
	 * 
	 * @param format
	 *            The GetMap image format.
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * Get the layers parameter used in the GetMap requests.
	 * 
	 * @return The GetMap layers parameter.
	 */
	public String getLayers() {
		return layers;
	}

	/**
	 * Set the layers parameter used in the GetMap requests.
	 * 
	 * @param layers
	 *            The GetMap layers parameter.
	 */
	public void setLayers(String layers) {
		this.layers = layers;
	}

	/**
	 * Get the styles parameter to be used in the GetMap requests.
	 * 
	 * @return The styles parameter to be used in the GetMap requests.
	 */
	public String getStyles() {
		return styles;
	}

	/**
	 * Set the styles parameter to be used in the GetMap requests.
	 * 
	 * @param styles
	 *            The styles parameter to be used in the GetMap requests.
	 */
	public void setStyles(String styles) {
		this.styles = styles;
	}

	/**
	 * Get the filter parameter used in GetMap requests. Note this parameter is not a default WMS parameter, and not all
	 * WMS servers may support this.
	 * 
	 * @return The GetMap filter parameter.
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * Set the filter parameter used in GetMap requests. Note this parameter is not a default WMS parameter, and not all
	 * WMS servers may support this.
	 * 
	 * @param filter
	 *            The GetMap filter parameter.
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * Get the GetMap transparent parameter. Default value is 'true'.
	 * 
	 * @return The GetMap transparent parameter.
	 */
	public boolean isTransparent() {
		return transparent;
	}

	/**
	 * Set the transparent parameter used in the GetMap requests. Default value is 'true'.
	 * 
	 * @param transparent
	 *            The GetMap transparent parameter.
	 */
	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}

	/**
	 * Get the WMS version used. Default value is '1.3.0'.
	 * 
	 * @return The WMS version.
	 */
	public WmsVersion getVersion() {
		return version;
	}

	/**
	 * Set the WMS version. Default value is '1.3.0'.
	 * 
	 * @param version
	 *            The WMS version.
	 */
	public void setVersion(WmsVersion version) {
		this.version = version;
	}

	/**
	 * Get the base URL to the WMS service. This URL should not contain any WMS parameters.
	 * 
	 * @return The base URL to the WMS service.
	 */
	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * Set the base URL to the WMS service. This URL should not contain any WMS parameters.
	 * 
	 * @param baseUrl
	 *            The base URL to the WMS service.
	 */
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	/**
	 * Get the default legend configuration for this layer. By default WMS does not support any of the options herein,
	 * but some WMS vendors have added extra options to allow for these.
	 * 
	 * @return The default legend creation configuration for this layer.
	 */
	public LegendConfig getLegendConfig() {
		return legendConfig;
	}

	/**
	 * Set the default legend configuration for this layer. By default WMS does not support any of the options herein,
	 * but some WMS vendors have added extra options to allow for these.
	 * 
	 * @param legendConfig
	 *            The default legend creation configuration for this layer.
	 */
	public void setLegendConfig(LegendConfig legendConfig) {
		this.legendConfig = legendConfig;
	}
}