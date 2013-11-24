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

package org.geomajas.plugin.caching.configuration;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.LayerExtraInfo;
import org.geomajas.configuration.ServerSideOnlyInfo;
import org.geomajas.plugin.caching.service.CacheCategory;

import java.util.Map;

/**
 * Information about a cache.
 * <p/>
 * Each cache should contain information for all cache categories. When a category is not available, it is not cached.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public class CacheInfo implements LayerExtraInfo, ServerSideOnlyInfo {

	private static final long serialVersionUID = 100L;

	/**
	 * Key which can be used to store this information in
	 * {@link org.geomajas.configuration.LayerInfo#setExtraInfo(java.util.Map)}.
	 */
	public static final String KEY = CacheInfo.class.getName();

	private Map<CacheCategory, CacheConfiguration> configuration;
	private String id;

	/**
	 * Set the cache id. This is assigned automatically by setting the bean id.
	 *
	 * @param id id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get the id for this cache configuration.
	 *
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the cache category specific configuration to use.
	 * <p/>
	 * This extends the default configuration. If a cache category specific configuration was specified in the
	 * configuration file and that same cache category is also defined in the map, then the configuration from the map
	 * will be used (extending the default configuration!).
	 *
	 * @param configuration cache configuration
	 * @since 2.0.0
	 */
	public void setConfiguration(Map<CacheCategory, CacheConfiguration> configuration) {
		this.configuration = configuration;
	}

	/**
	 * Get the cache category specific configuration to use.
	 * <p/>
	 * This extends the default configuration. If a cache category specific configuration was specified in the
	 * configuration file and that same cache category is also defined in the map, then the configuration from the map
	 * will be used (extending the default configuration!).
	 *
	 * @return cache configuration
	 * @since 2.0.0
	 */
	public Map<CacheCategory, CacheConfiguration> getConfiguration() {
		return configuration;
	}
}
