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

package org.geomajas.cache.algorithm;

import org.geomajas.cache.CacheException;
import org.geomajas.cache.store.ContentStore;
import org.geomajas.cache.store.RenderContent;

/**
 * <p>
 * General interface for a caching algorithm. It's basic operating principle is that is has a maximum size in which to
 * fit the required content as good as it can. This process of determining what goes in or out, all happens in the
 * "addToCache" function. This function will only be called when a new arrival presents itself. It is then up to the
 * algorithm to decide what to do with it.
 * </p>
 * <p>
 * A caching algorithm has to maintain it's own meta information about the content. It must keep track of what it has in
 * store (can be asked through the "isInCache" function).
 * </p>
 *
 * @author Pieter De Graef
 */
public interface CachingAlgorithm {

	/**
	 * Is a certain rendering content already in the cache or not?
	 *
	 * @param renderContent
	 *            The rendering content to check for in the cache.
	 * @return Returns true if it is here.
	 */
	boolean isInCache(RenderContent renderContent);

	/**
	 * Given a <code>ContentStore</code> and a <code>RenderContent</code>. Decide if the content should be added to the
	 * cache.
	 *
	 * @param contentStore
	 *            The cache!
	 * @param renderContent
	 * @return Returns true if the content has been successfully added to the <code>ContentStore</code>, false
	 *         otherwise.
	 * @throws org.geomajas.cache.CacheException
	 *             Thrown if something went wrong during the writing/deleting from the <code>ContentStore</code>.
	 */
	boolean addToCache(ContentStore contentStore, RenderContent renderContent) throws CacheException;

	/**
	 * Set the maximum size for the cache.
	 *
	 * @param maximumSize
	 * @throws org.geomajas.cache.CacheException
	 *             Usually this exception is thrown if one tries to change the maximum size, while caching has already
	 *             begun.
	 */
	void setMaximumSize(int maximumSize) throws CacheException;
}
