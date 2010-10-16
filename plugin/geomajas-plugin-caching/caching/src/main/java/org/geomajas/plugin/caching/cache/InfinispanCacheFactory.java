/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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

	private Logger log = LoggerFactory.getLogger(InfinispanCacheFactory.class);

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
