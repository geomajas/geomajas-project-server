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

package org.geomajas.gwt.client.service;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

/**
 * Loader which allows customizing how the {@link ClientConfigurationService} loads the configuration from the server.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
@UserImplemented
public interface ClientConfigurationLoader {

	/**
	 * Load the configuration for an application.
	 *
	 * @param applicationId application to load configuration for
	 * @param setter callback to set the configuration
	 */
	void loadClientApplicationInfo(String applicationId, ClientConfigurationSetter setter);

}
