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

package org.geomajas.plugin.caching.configuration;

import org.geomajas.configuration.ServerSideOnlyInfo;
import org.infinispan.config.Configuration;

/**
 * Interface for marking configurations which can be used by
 * {@link org.geomajas.plugin.caching.cache.InfinispanCacheFactory}.
 *
 * @author Joachim Van der Auwera
 */
public interface InfinispanConfiguration extends ServerSideOnlyInfo {

	/**
	 * Indicates whether caching should be enabled.
	 *
	 * @return true to enable the cache, false to disable it
	 */
	boolean isCacheEnabled();

	/**
	 * Get the name of the infinispan configuration which should be used. This is the name from the XML configuration
	 * file. When this is null, then {@link #getInfinispanConfiguration()} should be not null.
	 *
	 * @return base configuration name or null
	 */
	String getConfigurationName();

	/**
	 * Get the Infinispan configuration object.
	 * <p/>
	 * When {#link getConfigurationName} is null, this is used. It extends the default configuration which is created
	 * using the XML configuration file if any.
	 *
	 * @return configuration object
	 */
	Configuration getInfinispanConfiguration();
}
