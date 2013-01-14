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

package org.geomajas.puregwt.client;

import org.geomajas.gwt.client.command.GwtCommandDispatcher;

import com.google.gwt.core.client.GWT;

/**
 * General static values for use within Geomajas.
 * 
 * @author Pieter De Graef
 */
public final class Geomajas {

	private static final String GEOMAJAS_SERVICE_PATH = "geomajasService";

	private static final String LEGEND_SERVICE_PATH = "legendgraphic/";

	private static String geomajasServiceUrl;

	private static String legendServiceUrl;
	
	private Geomajas() {
		// Utility class should not have a public constructor.
	}

	/**
	 * Get the URL to the Geomajas RPC service.
	 * 
	 * @return The URL to the Geomajas RPC service.
	 */
	public static String getGeomajasServiceUrl() {
		if (geomajasServiceUrl != null) {
			return geomajasServiceUrl;
		}
		return getGeomajasControllerUrl() + GEOMAJAS_SERVICE_PATH;
	}

	/**
	 * Get the URL to the Geomajas Legend service.
	 * 
	 * @return The URL to the Geomajas Legend service.
	 */
	public static String getLegendServiceUrl() {
		if (legendServiceUrl != null) {
			return legendServiceUrl;
		}
		return getGeomajasControllerUrl() + LEGEND_SERVICE_PATH;
	}

	public static void setGeomajasServiceUrl(String geomajasServiceUrl) {
		Geomajas.geomajasServiceUrl = geomajasServiceUrl;
		GwtCommandDispatcher.getInstance().setServiceEndPointUrl(geomajasServiceUrl);
	}

	public static void setLegendServiceUrl(String legendServiceUrl) {
		Geomajas.legendServiceUrl = legendServiceUrl;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private static String getGeomajasControllerUrl() {
		String moduleBaseUrl = GWT.getModuleBaseURL();
		// remove last slash
		moduleBaseUrl = moduleBaseUrl.substring(0, moduleBaseUrl.length() - 1);
		// replace module part by /d
		int contextEndIndex = moduleBaseUrl.lastIndexOf("/");
		if (contextEndIndex > 6) {
			return moduleBaseUrl.substring(0, contextEndIndex) + "/d/";
		} else {
			// fall back to module base URL
			return GWT.getModuleBaseURL();
		}
	}
}