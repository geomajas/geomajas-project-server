/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.caching.configuration;

import org.geomajas.annotation.Api;

import java.io.Serializable;

/**
 * Marker interface which indicates a cache configuration.
 * Serializable for it's use in {@link org.geomajas.plugin.caching.configuration.CacheInfo}.
 * Since should be: 1.14.0.
 *
 * @author Joachim Van der Auwera
 * @author Jan Venstermans
 * @since 2.0.0
 */
@Api(allMethods = true)
public interface CacheConfiguration extends Serializable {

}
