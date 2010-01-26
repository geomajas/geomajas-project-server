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

package org.geomajas.internal.cache;

import org.geomajas.cache.CacheException;
import org.geomajas.cache.TileCacheService;
import org.geomajas.cache.broker.Broker;
import org.geomajas.cache.store.RenderContent;
import org.geomajas.service.ApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p>
 * General service object for retrieving raster data. It uses
 * <code>Broker</code> objects to do so. To be more precise, there are 2 broker
 * objects available in this class that take care of the retrieving task.
 * </p>
 * <p>
 * On one hand there is a <code>CacheBroker</code>, who gradually builds a cache
 * to retrieve the raster data. On the other hand there is a
 * <code>RealTimeBroker</code>, who can always return the request data, because
 * it builds it in real-time. Basically the real-time broker acts as a backup
 * for the cache broker, in case the requested data is not found in the cache.
 * </p>
 * <p>
 * This class also makes sure that the cache broker gets the opportunity to
 * actually build up it's cache (using the broker's update method). How exactly
 * the broker builds it's cache, is up to the broker itself. We don't
 * meddle in it.
 * </p>
 * <p>
 * This object should be used in all active threads, by all users. Having a
 * separate cache per user would be quite ineffective :-)
 * </p>
 *
 * @author Pieter De Graef
 */
@Component()
public class DefaultTileCacheService implements TileCacheService {

	private final Logger log = LoggerFactory.getLogger(DefaultTileCacheService.class);

	/**
	 * The <code>CacheBroker</code> object.
	 */
	@Resource(name = "internal.cache.broker.CacheBroker")
	private Broker cache;

	/**
	 * The <code>RealTimeBroker</code> object.
	 */
	@Resource(name = "internal.cache.broker.RealTimeBroker")
	private Broker realTime;

	@Autowired
	private ApplicationService runtimeParameters;

	// -------------------------------------------------------------------------
	// Class specific functions:
	// -------------------------------------------------------------------------

	/**
	 * Get a <code>RenderContent</code> object from a
	 * <code>HttpServletRequest</code> object. This will retrieve the requested
	 * raster data using broker objects. Meanwhile a cache is being build.
	 *
	 * @param renderContent description to render, rendering is done in the object
	 * @return the passed @see RenderObject with rendered data in it
	 * @throws CacheException caching problem 
	 */
	public RenderContent get(RenderContent renderContent) throws CacheException {
		log.info("Getting tile for ID=" + renderContent.getId());

		// Try to get the content from the cache:
		if (runtimeParameters.isTileCacheEnabled()) {
			try {
				if (cache.read(renderContent)) {
					return renderContent;
				}
			} catch (CacheException e) {
				log.error("Error occurred fetching tile content: " + e.getMessage());
			}
		}

		// If it was not in the cache, build the content:
		try {
			if (realTime.read(renderContent)) {
				// When the content is built, try and update the cache.
				if (cache != null) {
					cache.update(renderContent);
				}
				return renderContent;
			}
		} catch (CacheException e) {
			log.error("Error occurred fetching tile content: " + e.getMessage(), e);
		}

		return null;
	}
}