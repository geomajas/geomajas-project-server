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
import org.geomajas.plugin.caching.configuration.InfinispanConfiguration;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheFactory;
import org.geomajas.plugin.caching.service.CacheService;
import org.infinispan.config.Configuration;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

/**
 * Cache factory for creating infinispan caches.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api
public class InfinispanCacheFactory implements CacheFactory {

	private final Logger log = LoggerFactory.getLogger(InfinispanCacheFactory.class);

	private EmbeddedCacheManager manager;
	private String configurationFile;
	private InfinispanConfiguration allCategoriesConfiguration;
	private Map<CacheCategory, InfinispanConfiguration> configuration;

	/**
	 * Set the location of the Infinispan configuration file.
	 * <p/>
	 * When present, this is the base configuration which can be overwritten by the other settings.
	 * <p/>
	 * This location is first searched on the classpath and if that fails, as a absolute path.
	 *
	 * @param configurationFile configuration file
	 */
	public void setConfigurationFile(String configurationFile) {
		this.configurationFile = configurationFile;
	}

	/**
	 * Set the base configuration which should apply to all cache categories.
	 * <p/>
	 * This is read after the configuration file, and will extend the default configuration from there. It does not
	 * affect specific cache category configuration which is specified in the configuration file.
	 *
	 * @param allCategoriesConfiguration extension to default configuration
	 */
	public void setAllCategoriesConfiguration(InfinispanConfiguration allCategoriesConfiguration) {
		this.allCategoriesConfiguration = allCategoriesConfiguration;
	}

	/**
	 * Set the cache category specific configuration to use.
	 * <p/>
	 * This extends the default configuration. If a cache category specific configuration was specified in the
	 * configuration file and that same cache category is also defined in the map, then the configuration from the map
	 * will be used (extending the default configuration!).
	 *
	 * @param configuration cache manager configuration
	 * @since 1.0.0
	 */
	@Api
	public void setConfiguration(Map<CacheCategory, InfinispanConfiguration> configuration) {
		this.configuration = configuration;
	}

	@PostConstruct
	public void init() throws IOException {
		// base configuration from XML file
		if (null != configurationFile) {
			log.debug("Get base configuration from {}", configurationFile);
			manager = new DefaultCacheManager(configurationFile);
		} else {
			manager = new DefaultCacheManager();
		}

		// extend default configuration
		if (null != allCategoriesConfiguration) {
			Configuration conf = manager.getDefaultConfiguration();
			conf.applyOverrides(allCategoriesConfiguration.getInfinispanConfiguration());
		}

		// extended specific configuration
		if (null != configuration) {
			for (Map.Entry<CacheCategory, InfinispanConfiguration> entry : configuration.entrySet()) {
				log.debug("Override configuration for category {}", entry.getKey().getName());
				Configuration conf = manager.getDefaultConfiguration().clone();
				conf.applyOverrides(entry.getValue().getInfinispanConfiguration());
				manager.defineConfiguration(entry.getKey().getName(), conf);
			}
		}
	}

	public CacheService create(Layer layer, CacheCategory category) {
		return new InfinispanCacheService(manager.<String, Object>getCache(category.getName()));
	}
}
