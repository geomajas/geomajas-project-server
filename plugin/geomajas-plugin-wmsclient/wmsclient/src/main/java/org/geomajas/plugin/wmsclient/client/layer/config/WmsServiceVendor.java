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

/**
 * Type of service that provides the URL. If specified, this allows for extra service/vendor options. E.g.
 * legend_options=fontName:... in case of Geoserver WMS getLegend request
 * 
 * @author An Buyle
 */
public enum WmsServiceVendor {
	/** Wms service provided by GeoServer. */
	GEOSERVER_WMS("geoserver"),

	/** Unspecified WMS. This means we can only use the bare-boned WMS defaults. */
	UNSPECIFIED("unspecified");

	private String type;

	private WmsServiceVendor(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return type;
	}
}