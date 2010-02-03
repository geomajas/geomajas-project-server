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

package org.geomajas.internal.cache.broker;

import org.geomajas.cache.CacheException;
import org.geomajas.cache.CacheService;
import org.geomajas.cache.algorithm.CachingAlgorithm;
import org.geomajas.cache.broker.Broker;
import org.geomajas.cache.store.ContentStore;
import org.geomajas.cache.store.RenderContent;
import org.geomajas.internal.cache.algorithm.RoundRobinCachingAlgorithm;
import org.geomajas.internal.cache.store.FileContentStore;
import org.geomajas.layer.VectorLayer;
import org.geomajas.service.ApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;

/**
 * <p>
 * This broker implements a caching mechanism using <code>ContentStore</code>
 * objects. Also this class contains a caching algorithm that determines what
 * should be in the cache, and what should not.
 * </p>
 *
 * @author Pieter De Graef
 */
@Component()
public class CacheBroker implements Broker {

	private final Logger log = LoggerFactory.getLogger(CacheBroker.class);

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private ApplicationService runtime;

	private boolean initialised;

	/**
	 * The ContentStore that actually stores the <code>ContentStore</code>'s
	 * content. This broker uses a <code>FileContentStore</code> to do the dirty
	 * work.
	 */
	private ContentStore contentStore;

	/**
	 * The caching algorithm used to determine what should be in the cache, and
	 * what should not.
	 */
	private CachingAlgorithm algorithm;

	/**
	 * Initializes the ContentStore with a basePath to store the cache's files.
	 *
	 * @throws org.geomajas.cache.CacheException problems while creating FileContentStore
	 */
	public void init() throws CacheException {
		if (!initialised) {
			contentStore = new FileContentStore(new File(runtime.getTileCacheDirectory()));
			algorithm = new RoundRobinCachingAlgorithm(runtime.getTileCacheMaximumSize());
			initialised = true;
		}
	}

	// -------------------------------------------------------------------------
	// Broker implementation:
	// -------------------------------------------------------------------------

	/**
	 * Try and get the content for a specific empty <code>RenderContent</code>
	 * object. It asks the ContentStore for this content through it's read
	 * function. If successful "true" is returned. Also checks if the required
	 * content is allowed to be cached to begin with.
	 *
	 * @param renderContent
	 *            An empty renderContent. This function should try and fill it
	 *            with the right content.
	 * @return Returns true if the content for the <code>RenderContent</code>
	 *         object was successfully set. If not, false is returned.
	 * @throws org.geomajas.cache.CacheException
	 */
	public boolean read(RenderContent renderContent) throws CacheException {
		if (!initialised) {
			init();
		}
		if (!isCachable(renderContent)) {
			return false;
		}
		if (algorithm.isInCache(renderContent)) {
			byte[] content = contentStore.read(renderContent);
			if (content != null) {
				log.debug("READ ({}): Content was found in the cache.", renderContent.getId());
				renderContent.setContent(content);
				return true;
			}
		}
		log.debug("READ ({}): Could not be found in the cache.", renderContent.getId());
		return false;
	}

	/**
	 * Update the cache! At the moment only a simple implementation is in place:
	 * just add the damn thing. We don't care how big the cache gets.
	 */
	public void update(RenderContent renderContent) throws CacheException {
		if (!initialised) {
			init();
		}
		if (isCachable(renderContent)) {
			algorithm.addToCache(contentStore, renderContent);
		}
	}

	// -------------------------------------------------------------------------
	// Private functions:
	// -------------------------------------------------------------------------

	/**
	 * Checks to see if the content is allowed to be cached at all. To do this,
	 * it checks the layer's editing permissions. Editable layers can not be
	 * cached.
	 *
	 * @param renderContent data to render
	 * @return true when the data is immutable and can safely be cached
	 * @throws CacheException oops
	 */
	private boolean isCachable(RenderContent renderContent) throws CacheException {
		Map<String, Object> params = renderContent.getParameters();
		String layerId = (String) params.get(CacheService.PARAM_LAYER_ID);
		VectorLayer vLayer;
		try {
			vLayer = runtime.getVectorLayer(layerId);
		} catch (NullPointerException e) {
			return false;
		}
		return !(vLayer.isCreateCapable() || vLayer.isDeleteCapable() || vLayer.isUpdateCapable());
	}
}