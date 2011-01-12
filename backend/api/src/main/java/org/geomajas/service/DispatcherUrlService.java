/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.service;

import org.geomajas.global.Api;

/**
 * Service which allows you to get the URL of the dispatcher service, allowing you to build absolute URLs to
 * services linked in as dispatched controllers.
 *
 * @author Joachim Van der Auwera
 * @since 1.8.0
 */
@Api(allMethods = true)
public interface DispatcherUrlService {

	/**
	 * Get the base URL for the dispatched serviced.
	 *
	 * @return base URL
	 */
	String getDispatcherUrl();
}
