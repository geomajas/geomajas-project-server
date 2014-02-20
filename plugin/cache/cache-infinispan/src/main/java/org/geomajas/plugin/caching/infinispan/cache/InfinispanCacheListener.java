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

import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntriesEvicted;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryLoaded;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryRemoved;
import org.infinispan.notifications.cachelistener.event.CacheEntriesEvictedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryLoadedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryRemovedEvent;
import org.infinispan.notifications.cachemanagerlistener.annotation.CacheStarted;
import org.infinispan.notifications.cachemanagerlistener.annotation.CacheStopped;
import org.infinispan.notifications.cachemanagerlistener.event.CacheStartedEvent;
import org.infinispan.notifications.cachemanagerlistener.event.CacheStoppedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logging listener, can be useful for debugging.
 * 
 * @author Jan De Moerloose
 * 
 */
@Listener
public class InfinispanCacheListener {

	private final Logger log = LoggerFactory.getLogger(InfinispanCacheFactory.class);

	@CacheEntriesEvicted
	public void dataEvicted(CacheEntriesEvictedEvent event) {
		if (log.isTraceEnabled()) {
			if (event.isPre()) {
				log.trace("Going to evict " + event.getEntries().size() + " entries from the cache");
			} else {
				log.trace("Evicted " + event.getEntries().size() + " entries from the cache");
			}
		}
	}

	@CacheEntryCreated
	public void dataAdded(CacheEntryCreatedEvent event) {
		if (log.isTraceEnabled()) {
			if (event.isPre()) {
				log.trace("Going to add new entry " + event.getKey() + " to the cache");
			} else {
				log.trace("Added new entry " + event.getKey() + " to the cache" + event.getCache().getName());
			}
		}
	}

	@CacheEntryRemoved
	public void dataRemoved(CacheEntryRemovedEvent event) {
		if (log.isTraceEnabled()) {
			if (event.isPre()) {
				log.trace("Going to remove entry " + event.getKey() + " from the cache");
			} else {
				log.trace("Removed entry " + event.getKey() + " from the cache");
			}
		}
	}

	@CacheEntryLoaded
	public void dataLoaded(CacheEntryLoadedEvent event) {
		if (log.isTraceEnabled()) {
			if (event.isPre()) {
				log.trace("Going to load entry " + event.getKey() + " from the cache");
			} else {
				log.trace("Loaded entry " + event.getKey() + " from the cache");
			}
		}
	}

	@CacheStarted
	public void cacheStarted(CacheStartedEvent event) {
		if (log.isTraceEnabled()) {
			log.trace("Cache Started");
		}
	}

	@CacheStopped
	public void cacheStopped(CacheStoppedEvent event) {
		if (log.isTraceEnabled()) {
			log.trace("Cache Started");
		}
	}
}