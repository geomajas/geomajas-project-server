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
package org.geomajas.smartgwt.client.action;

import org.geomajas.annotation.Api;

/**
 * Marker which indicates that a toolbar action is configurable, and process the configuration.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public interface ConfigurableAction {

	/**
	 * Add configuration key/value pair.
	 *
	 * @param key parameter key
	 * @param value parameter value
	 */
	void configure(String key, String value);
}
