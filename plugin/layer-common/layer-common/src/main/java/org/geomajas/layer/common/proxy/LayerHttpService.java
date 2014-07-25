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

package org.geomajas.layer.common.proxy;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.impl.client.AbstractHttpClient;
import org.geomajas.annotation.Api;
import org.geomajas.layer.RasterLayer;

/**
 * Service which handles the (secured) HTTP communication for raster layer requests.
 *
 * @author Joachim Van der Auwera
 * @since 1.16.0
 */
@Api(allMethods = true)
public interface LayerHttpService {

	/**
	 * Get the contents from the request URL.
	 *
	 * @param url URL to get the response from
	 * @param layer the raster layer
	 * @return {@link InputStream} with the content
	 * @throws IOException cannot get content
	 */
	InputStream getStream(String url, RasterLayer layer) throws IOException;
	
	/**
	 * Sets the HTTP client for this service.
	 * @param client the common HTTP client
	 */
	void setClient(AbstractHttpClient client);
	
	/**
	 * Get the HTTP client for this service.
	 * @return the common HTTP client
	 */
	AbstractHttpClient getClient();

}
