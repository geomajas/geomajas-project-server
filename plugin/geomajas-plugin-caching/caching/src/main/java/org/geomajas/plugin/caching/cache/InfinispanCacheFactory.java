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

package org.geomajas.plugin.caching.cache;

import org.geomajas.global.Api;
import org.geomajas.layer.Layer;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheFactory;
import org.geomajas.plugin.caching.service.CacheService;
import org.infinispan.config.Configuration;
import org.infinispan.manager.CacheContainer;
import org.infinispan.manager.DefaultCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Cache factory for creating infinispan caches.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api
public class InfinispanCacheFactory implements CacheFactory {

	private final Logger log = LoggerFactory.getLogger(InfinispanCacheFactory.class);

	private CacheContainer manager;
	private String configurationFile;
	private Configuration configuration;

	/**
	 * Set the location of the infinispan.
	 * <p/>
	 * This location is first searched on the classpath and if that fails, as a absolute path.
	 *
	 * @param configurationFile configuration file
	 */
	public void setConfigurationFile(String configurationFile) {
		this.configurationFile = configurationFile;
	}

	/**
	 * Set the cache configuration to use.
	 *
	 * @param configuration cache manager configuration
	 * @since 1.0.0
	 */
	@Api
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	@PostConstruct
	public void init() throws IOException {
		if (null != configuration) {
			manager = new DefaultCacheManager(configuration);
		} else if (null != configurationFile) {
			manager = new DefaultCacheManager(configurationFile);
		} else {
			manager = new DefaultCacheManager();
		}
	}

	public CacheService create(Layer layer, CacheCategory category) {
		return new InfinispanCacheService(manager.<String, Object>getCache(category.getName()));
	}
}
