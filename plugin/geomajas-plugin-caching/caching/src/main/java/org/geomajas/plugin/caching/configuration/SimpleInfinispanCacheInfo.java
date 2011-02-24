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

import org.infinispan.config.Configuration;

/**
 * Easy Infinispan cache configuration based on some sensible default.
 *
 * @author Joachim Van der Auwera
 */
public class SimpleInfinispanCacheInfo implements InfinispanConfiguration {

	// expiration
	// directory location for cache store
	// cache size in memory (# or size?)
	// active
	//private String

	private Configuration configuration = new Configuration();

	public Configuration getInfinispanConfiguration() {
		return configuration;
	}

	/**
	 * Interval between subsequent eviction runs, in milliseconds. If you wish to disable the periodic eviction
	 * process altogether, set wakeupInterval to -1.
	 *
	 * @param evictionWakeUpInterval eviction wake-up interval in ms
	 */
	public void setEvictionWakeUpInterval(long evictionWakeUpInterval) {
		configuration.setEvictionWakeUpInterval(evictionWakeUpInterval);
	}

	/**
	 * Set the eviction strategy to use. Can be UNORDERED, FIFO, LRU and NONE.
	 * <p/>
	 *This indicates the which items need to be removed from cache to make space for more new entries. The cached item
	 * may still be available after eviction if there is a secondary cache (disk).
	 *
	 * @param evictionStrategy eviction strategy
	 */
	public void setEvictionStrategy(org.infinispan.eviction.EvictionStrategy evictionStrategy) {
		configuration.setEvictionStrategy(evictionStrategy);
	}


}
