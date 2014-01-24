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

package org.geomajas.plugin.caching.infinispan.cache;

import org.geomajas.annotation.Api;
import org.geomajas.layer.Layer;
import org.geomajas.plugin.caching.cache.NoCacheCacheFactory;
import org.geomajas.plugin.caching.configuration.CacheConfiguration;
import org.geomajas.plugin.caching.configuration.CacheInfo;
import org.geomajas.plugin.caching.infinispan.configuration.InfinispanConfiguration;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheFactory;
import org.geomajas.plugin.caching.service.CacheService;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.TestRecorder;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Cache factory for creating infinispan caches.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api
public class InfinispanCacheFactory implements CacheFactory {

	private static final int PRIME = 31;

	private final Logger log = LoggerFactory.getLogger(InfinispanCacheFactory.class);

	private EmbeddedCacheManager manager;
	private String configurationFile;
	private CacheInfo defaultConfiguration;
	private final NoCacheCacheFactory noCacheFactory = new NoCacheCacheFactory();

	private final Map<CacheSelector, CacheService> caches = new HashMap<CacheSelector, CacheService>();

	@Autowired(required = false)
	private Map<String, Layer<?>> layerMap = new LinkedHashMap<String, Layer<?>>(); // NOSONAR autowired does change it

	@Autowired
	private TestRecorder recorder;

	@Autowired
	private ConfigurationService configurationService;

	/**
	 * Set the location of the Infinispan configuration file.
	 * <p/>
	 * When present, this is the base configuration which can be overwritten by the other settings.
	 * <p/>
	 * This location is first searched on the classpath and if that fails, as a absolute path.
	 *
	 * @param configurationFile configuration file
	 * @since 1.0.0
	 */
	@Api
	public void setConfigurationFile(String configurationFile) {
		this.configurationFile = configurationFile;
	}

	/**
	 * Set the default cache configuration. Specifies the default settings for the cache categories.
	 * <p/>
	 * Cache configurations can be different per layer (with layers sharing caches).
	 *
	 * @param defaultConfiguration default cache configuration
	 */
	public void setDefaultConfiguration(CacheInfo defaultConfiguration) {
		this.defaultConfiguration = defaultConfiguration;
	}

	/**
	 * Finish initializing service.
	 *
	 * @throws IOException oop
	 */
	@PostConstruct
	protected void init() throws IOException {
		// base configuration from XML file
		if (null != configurationFile) {
			log.debug("Get base configuration from {}", configurationFile);
			manager = new DefaultCacheManager(configurationFile);
		} else {
			manager = new DefaultCacheManager();
		}

		// cache for caching the cache configurations (hmmm, sounds a bit strange)
		Map<String, Map<CacheCategory, CacheService>> cacheCache =
				new HashMap<String, Map<CacheCategory, CacheService>>();

		// build default configuration
		if (null != defaultConfiguration) {
			setCaches(cacheCache, null, defaultConfiguration);
		}

		// build layer specific configurations
		for (Layer layer : layerMap.values()) {
			CacheInfo ci = configurationService.getLayerExtraInfo(layer.getLayerInfo(), CacheInfo.class);
			if (null != ci) {
				setCaches(cacheCache, layer, ci);
			}
		}
	}

	/**
	 * Add layer specific cache configuration in the caches variable. This assures each cache is only built once and
	 * possibly reused (thanks to cacheCache).
	 *
	 * @param cacheCache cache for built caches
	 * @param layer layer (or null)
	 * @param cacheInfo cache configuration info
	 */
	private void setCaches(Map<String, Map<CacheCategory, CacheService>> cacheCache, Layer layer,
			CacheInfo cacheInfo) {
		Map<CacheCategory, CacheService> ciCaches;
		ciCaches = cacheCache.get(cacheInfo.getId());
		if (null == ciCaches) {
			ciCaches = createCaches(cacheInfo);
			cacheCache.put(cacheInfo.getId(), ciCaches);
		}
		for (Map.Entry<CacheCategory, CacheService> ciEntry : ciCaches.entrySet()) {
			CacheSelector cs = new CacheSelector(ciEntry.getKey(), layer);
			caches.put(cs, ciEntry.getValue());
		}
	}

	private Map<CacheCategory, CacheService> createCaches(CacheInfo cacheInfo) {
		Map<CacheCategory, CacheService> ciCaches = new HashMap<CacheCategory, CacheService>();
		for (Map.Entry<CacheCategory, CacheConfiguration> entry : cacheInfo.getConfiguration().entrySet()) {
			if (entry.getValue() instanceof InfinispanConfiguration) {
				CacheService cacheService;
				CacheCategory category = entry.getKey();
				InfinispanConfiguration config = (InfinispanConfiguration) entry.getValue();
				if (config.isCacheEnabled()) {
					String configurationName = config.getConfigurationName();
					if (null == configurationName) {
						Configuration dcc = manager.getDefaultCacheConfiguration();
						Configuration infinispan = config.getInfinispanConfiguration(
								new ConfigurationBuilder().read(dcc));
						configurationName = "$" + category.getName() + "$" + cacheInfo.getId();
						manager.defineConfiguration(configurationName, infinispan);
					}
					recorder.record("infinispan", "configuration name " + configurationName);
					cacheService = new InfinispanCacheService(manager.<String, Object>getCache(configurationName));
				} else {
					cacheService = noCacheFactory.create(null, category);
				}
				ciCaches.put(category, cacheService);
			}
		}
		return ciCaches;
	}

	@Override
	public CacheService create(Layer layer, CacheCategory category) {
		CacheSelector cacheSelector = new CacheSelector(category, layer);
		CacheService cacheService;
		cacheService = caches.get(cacheSelector);
		if (null == cacheService) {
			cacheSelector = new CacheSelector(category, null);
			cacheService = caches.get(cacheSelector);
		}
		if (null == cacheService) {
			cacheService = noCacheFactory.create(layer, category);
		}
		return cacheService;
	}

	/** Cache selector, key for map. */
	private static class CacheSelector {

		private String category = "";
		private String layer = "";

		/**
		 * Create a {@link CacheSelector}.
		 *
		 * @param category category
		 * @param layer layer
		 */
		public CacheSelector(CacheCategory category, Layer layer) {
			if (null != category) {
				this.category = category.toString();
			}
			if (null != layer) {
				this.layer = layer.getId();
			}
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof CacheSelector)) {
				return false;
			}

			CacheSelector that = (CacheSelector) o;

			return !(category != null ? !category.equals(that.category) : that.category != null) &&
					!(layer != null ? !layer.equals(that.layer) : that.layer != null);

		}

		@Override
		public int hashCode() {
			int result = category != null ? category.hashCode() : 0;
			result = PRIME * result + (layer != null ? layer.hashCode() : 0);
			return result;
		}
	}
}
