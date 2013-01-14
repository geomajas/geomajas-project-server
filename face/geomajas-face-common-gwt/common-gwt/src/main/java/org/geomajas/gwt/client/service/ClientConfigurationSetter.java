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
import org.geomajas.configuration.client.ClientApplicationInfo;

/**
 * Class which can be used by the {@link ClientConfigurationLoader} to actually set the data in the
 * {@link ClientConfigurationService}. This allows us to not have a public setter in that class.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface ClientConfigurationSetter {

	/**
	 * Set the full configuration object for the application with given id.
	 *
	 * @param applicationId application id
	 * @param applicationInfo application configuration
	 */
	void set(String applicationId, ClientApplicationInfo applicationInfo);

}
