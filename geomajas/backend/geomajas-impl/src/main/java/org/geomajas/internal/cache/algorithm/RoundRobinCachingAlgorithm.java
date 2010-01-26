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

package org.geomajas.internal.cache.algorithm;

import org.geomajas.cache.CacheException;
import org.geomajas.cache.algorithm.CachingAlgorithm;
import org.geomajas.cache.store.ContentStore;
import org.geomajas.cache.store.RenderContent;
import org.geomajas.global.ExceptionCode;
import org.geomajas.internal.cache.store.DefaultRenderContent;
import org.geomajas.internal.cache.store.MetaRenderContent;

import java.util.LinkedList;

/**
 * <p>
 * A very simple caching algorithm that adds the latest
 * <code>RenderContent</code> to the back of the list. If the maximum size has
 * been reached, the first element will be popped out.
 * </p>
 * <p>
 * If the maximumSize equals 0, then this algorithm assumes there is no limit.
 * In this case, elements will never be deleted from the cache!
 * </p>
 *
 * @author Pieter De Graef
 */
public class RoundRobinCachingAlgorithm implements CachingAlgorithm {

	/**
	 * The cache's maximum size.
	 */
	private int maximumSize;

	/**
	 * The meta information storage. Each <code>RenderContent</code> object has
	 * a unique ID which is used in identifying it's meta information as well.
	 * TODO: this is stored in memory, which means that a server's reboot will
	 * wipe it. The contents of it's delegate are probably not wiped at server
	 * reboot. This may cause some problems.
	 */
	private LinkedList<MetaRenderContent> metaCache;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Default constructor that set no limit on the maximum size.
	 */
	public RoundRobinCachingAlgorithm() {
		this(0);
	}

	/**
	 * Constructor that immediately sets a limit on the maximum cache size.
	 *
	 * @param maximumSize
	 */
	public RoundRobinCachingAlgorithm(int maximumSize) {
		this.maximumSize = maximumSize;
		metaCache = new LinkedList<MetaRenderContent>();
	}

	// -------------------------------------------------------------------------
	// CachingAlgorithm implementation:
	// -------------------------------------------------------------------------

	/**
	 * Set the maximum size for the cache.
	 *
	 * @param maximumSize
	 * @throws org.geomajas.cache.CacheException
	 *             This exception is thrown if one tries to change the maximum
	 *             size, while caching has already begun.
	 */
	public void setMaximumSize(int maximumSize) throws CacheException {
		if (metaCache.size() > 0) {
			throw new CacheException(ExceptionCode.CACHE_SIZE_IMMUTABLE_WHEN_IN_USE);
		}
		this.maximumSize = maximumSize;
	}

	/**
	 * Add a new <code>RenderContent</code> object to the cache. If the maximum
	 * size has been reached, the oldest entry will be deleted.
	 *
	 * @param contentStore
	 *            The content store on which this algorithm applies. Creating
	 *            and deleting happens on this store.
	 * @param renderContent
	 *            The <code>RenderContent</code> object we wish to persist.
	 * @return Returns true of the render content has been successfully added.
	 *         If the maximum size has been reached, the oldest entry is to be
	 *         deleted. If this deletion results in an exception, false is
	 *         returned.
	 * @exception org.geomajas.cache.CacheException
	 *                Thrown if something goes wrong during the creation of the
	 *                new entry.
	 */
	public synchronized boolean addToCache(ContentStore contentStore, RenderContent renderContent)
			throws CacheException {
		if (maximumSize > 0 && metaCache.size() >= maximumSize) {
			try {
				contentStore.delete(new DefaultRenderContent(metaCache.removeFirst()));
			} catch (CacheException e) {
				return false;
			}
		}
		contentStore.create(renderContent);
		metaCache.add(new MetaRenderContent(renderContent));
		return true;
	}

	/**
	 * Is a certain rendering content already in the cache or not?
	 *
	 * @param renderContent
	 *            The rendering content to check for in the cache.
	 * @return Returns true if it is here.
	 */
	public synchronized boolean isInCache(RenderContent renderContent) {
		return metaCache.contains(new MetaRenderContent(renderContent.getId()));
	}
}