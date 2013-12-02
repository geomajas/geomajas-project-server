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

package org.geomajas.layer.common.proxy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpRequestInterceptor;

/**
 * A map of HttpRequestInterceptors which will be used by the LayerHttpService.
 * <p>
 * The key of the map decides to which requests the interceptors will be added.
 * <p>
 * They are used as layernames or as prefixes: eg. if the key is for example:
 * "http:\\myTmsServer.com\" then all requests (urls) beginning with this string
 * will be enriched.
 * <p>
 * You can add generic interceptors to be used around all request by using an
 * empty string as key.
 * 
 * @author Kristof Heirwegh
 * @since 1.0.0
 */
public class LayerHttpServiceInterceptors {

	private static final long serialVersionUID = 1L;

	private Map<String, List<HttpRequestInterceptor>> map;

	public Map<String, List<HttpRequestInterceptor>> getMap() {
		if (map == null) {
			map = new HashMap<String, List<HttpRequestInterceptor>>();
		}
		return map;
	}

	public void setMap(Map<String, List<HttpRequestInterceptor>> map) {
		this.map = map;
	}
}
