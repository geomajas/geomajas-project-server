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
package org.geomajas.puregwt.client.service;

import org.geomajas.gwt.client.command.GwtCommandDispatcher;

import com.google.gwt.core.client.GWT;

/**
 * Default implementation of {@link EndPointService}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class EndPointServiceImpl implements EndPointService {

	private static final String GEOMAJAS_SERVICE_PATH = "geomajasService";

	private static final String LEGEND_SERVICE_PATH = "legendgraphic/";

	private String commandServiceUrl;

	private String legendServiceUrl;

	private String dispatcherUrl;

	public String getCommandServiceUrl() {
		if (commandServiceUrl != null) {
			return commandServiceUrl;
		}
		return getDispatcherUrl() + GEOMAJAS_SERVICE_PATH;
	}

	public void setCommandServiceUrl(String commandServiceUrl) {
		this.commandServiceUrl = commandServiceUrl;
		GwtCommandDispatcher.getInstance().setServiceEndPointUrl(commandServiceUrl);
	}

	public String getLegendServiceUrl() {
		if (legendServiceUrl != null) {
			return legendServiceUrl;
		}
		return getDispatcherUrl() + LEGEND_SERVICE_PATH;
	}

	public void setLegendServiceUrl(String legendServiceUrl) {
		this.legendServiceUrl = legendServiceUrl;
	}

	public String getDispatcherUrl() {
		if (null != dispatcherUrl) {
			return dispatcherUrl;
		}
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

	public void setDispatcherUrl(String dispatcherUrl) {
		this.dispatcherUrl = dispatcherUrl;
	}

}
