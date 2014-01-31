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

package org.geomajas.service;

import org.geomajas.annotation.Api;

/**
 * Service which allows you to get the URL of the dispatcher service, allowing you to build absolute URLs to
 * services linked in as dispatched controllers.
 *
 * @author Joachim Van der Auwera
 * @author Jan De Moerloose
 * @since 1.8.0
 */
@Api(allMethods = true)
public interface DispatcherUrlService {

	/**
	 * Get the base URL for the dispatcher service.
	 * 
	 * @return base URL. This URL is guaranteed to end with a slash (/).
	 */
	String getDispatcherUrl();

	/**
	 * Get the local base URL for the dispatcher service. A local URL can be resolved from the web application host, as
	 * opposed to the web client. This is useful for processing resources such as images on the server side, e.g. for
	 * printing or reporting.
	 * 
	 * @return local base URL or null. If not null, this URL is guaranteed to end with a slash (/), e.g.
	 *         'http://localhost:8080/myApp/d/'
	 * @since 1.10.0
	 */
	String getLocalDispatcherUrl();

	/**
	 * Convert an external URL to a locally accessible URL. Just a utility method that replaces
	 * {@link #getDispatcherUrl()} by {@link #getLocalDispatcherUrl()} although implementations may perform more
	 * sophisticated localization.
	 * 
	 * @param externalUrl external URL that maps to the dispatcher service
	 * @return URL that provides local access to the same resource
	 * @since 1.10.0
	 */
	String localize(String externalUrl);
}
