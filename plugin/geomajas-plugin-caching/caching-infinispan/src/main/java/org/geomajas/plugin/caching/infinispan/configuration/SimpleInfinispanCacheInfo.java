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

package org.geomajas.plugin.caching.infinispan.configuration;

import org.geomajas.annotation.Api;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.cache.ConfigurationChildBuilder;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.loaders.jdbm.JdbmCacheStore;
import org.infinispan.util.concurrent.IsolationLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Easy Infinispan cache configuration based on some sensible default.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public class SimpleInfinispanCacheInfo extends AbstractInfinispanConfiguration {

	private final Logger log = LoggerFactory.getLogger(SimpleInfinispanCacheInfo.class);

	private static final long MILLISECONDS_PER_MINUTE = 60000L;
	
	private static final long EVICTION_NOT_SET = -2; 
	private static final int EXPIRATION_NOT_SET = -2;

	private long evictionWakeUpInterval = EVICTION_NOT_SET;
	private EvictionStrategy evictionStrategy;
	private IsolationLevel isolationLevel;
	private String location;
	private int maxEntries;
	private int expiration = EXPIRATION_NOT_SET;

	/**
	 * Create a {@link SimpleInfinispanCacheInfo}.
	 */
	public SimpleInfinispanCacheInfo() {
		super();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 2.0.0
	 */
	public Configuration getInfinispanConfiguration(ConfigurationBuilder baseBuilder) {
		ConfigurationChildBuilder builder = baseBuilder;
		if (null == builder) {
			builder = new ConfigurationBuilder();
		}
		builder = builder.locking().useLockStriping(false);
		if (EVICTION_NOT_SET != evictionWakeUpInterval) {
			builder = builder.expiration().wakeUpInterval(evictionWakeUpInterval);
		}
		if (null != evictionStrategy) {
			builder = builder.eviction().strategy(evictionStrategy);
		}
		if (null != isolationLevel) {
			builder = builder.locking().isolationLevel(isolationLevel);
		}
		if (null != location) {
			builder = builder.loaders()
					.passivation(true)
					.addCacheLoader()
					.cacheLoader(new JdbmCacheStore())
					.addProperty("location", location);
		}
		if (maxEntries > 0) {
			builder = builder.eviction().maxEntries(maxEntries);
		}
		if (EXPIRATION_NOT_SET != expiration) {
			builder = builder.expiration().maxIdle(expiration < 0 ? -1L : expiration * MILLISECONDS_PER_MINUTE);
		}
		return builder.build();
	}

	/**
	 * Interval between subsequent eviction runs, in milliseconds. If you wish to disable the periodic eviction
	 * process altogether, set wakeUpInterval to -1.
	 *
	 * @param evictionWakeUpInterval eviction wake-up interval in ms
	 */
	public void setEvictionWakeUpInterval(long evictionWakeUpInterval) {
		this.evictionWakeUpInterval = evictionWakeUpInterval;
	}

	/**
	 * Set the eviction strategy to use. Can be UNORDERED, FIFO, LRU and NONE.
	 * <p/>
	 * This indicates the which items need to be removed from cache to make space for more new entries. The cached item
	 * may still be available after eviction if there is a secondary cache (disk).
	 *
	 * @param evictionStrategy eviction strategy
	 */
	public void setEvictionStrategy(EvictionStrategy evictionStrategy) {
		this.evictionStrategy = evictionStrategy;
	}

	/**
	 * Set the transaction isolation level.
	 *
	 * @param isolationLevel tx isolation level
	 */
	public void setIsolationLevel(IsolationLevel isolationLevel) {
		this.isolationLevel = isolationLevel;
	}

	/**
	 * Set the location for the second level cache. This needs to be a directory which is created if needed.
	 * <p/>
	 * The location can point to a system property by using the "${propertyName}" notation. If the property does not
	 * exist it is replaced by a temporary directory. The property needs to be at the start of the string (so
	 * "${cache.dir}/rebuild" is ok but "/tmp/${hostname}" is not. Note that the temporary directory is not
	 * automatically removed!
	 *
	 * @param locationExpr directory location for second level cache of items
	 */
	public void setLevel2CacheLocation(String locationExpr) {
		if (null != locationExpr) {
			// property name handling in location
			String location = propertyReplace(locationExpr);

			// create caching directory
			File dir = new File(location);
			if (!dir.isDirectory()) {
				if (dir.exists()) {
					log.error("Location {} for 2nd level cache should be a directory.", location);
					throw new RuntimeException("Invalid location for setLevel2CacheLocation, " + location + //NOPMD
							" has to be a directory."); //NOPMD
				} else {
					if (!dir.mkdirs()) {
						log.warn("Directory {} for 2nd level cache could not be created.", location);
					}
				}
			}
			this.location = location;
		}
	}

	private String propertyReplace(String location) {
		String result = location;
		if (location.startsWith("${")) {
			int pos = location.indexOf('}');
			if (pos > 0) {
				String property = location.substring(2, pos);
				String rest = location.substring(pos + 1);
				String value = System.getProperty(property);
				if (null == value) {
					value = System.getProperty("java.io.tmpdir") + File.separator + "geomajas" + File.separator +
							"cache";
					log.warn("Trying to create cache location using property {} which is undefined, using {} instead.",
							property, value);
				}
				result = value + rest;
			} else {
				log.warn("Cache location {} looks like a property reference but closing } is missing.", location);
			}
		}
		return result;
	}

	/**
	 * Set the expiration time in minutes. Items are expired when idle for this amount of time.
	 * <p/>
	 * Expiration can be disabled by setting the time to -1.
	 *
	 * @param minutes maximum number of minutes that cached item may be idle before being expired. -1 to disable.
	 */
	public void setExpiration(int minutes) {
		expiration = minutes;
	}

	/**
	 * Set the number of items which should be kept in memory by the cache. This will be rounded up to the next bigger
	 * power of two.
	 * <p/>
	 * When there are more items to be stored, they can be moved to the second level cache if
	 * {@link #setLevel2CacheLocation(String)} is set.
	 *
	 * @param maxEntries maximum entries for the in-memory cache
	 */
	public void setMaxEntries(int maxEntries) {
		this.maxEntries = maxEntries;
	}

}
