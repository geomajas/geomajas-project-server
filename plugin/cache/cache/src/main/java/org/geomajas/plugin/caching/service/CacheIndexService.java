/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.caching.service;

import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.annotation.FutureApi;

import java.util.ArrayList;
import java.util.List;

/**
 * Spatial index for handling invalidation of entries in a {@link org.geomajas.plugin.caching.service.CacheService}.
 * This service is used by the {@link org.geomajas.plugin.caching.service.CacheManagerService}. 
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@FutureApi(allMethods = true)
public interface CacheIndexService {

	/**
	 * Special return value which can be used to indicate that the entire cache needs to be cleared.
	 * <p/>
	 * No items should ever be added in this object. Should be tested for using object equality (==).
	 */
	List<String> ALL_KEYS = new ArrayList<String>();

	/**
	 * Add a key/geometry pair to the spatial index.
	 * <p/>
	 * This will overwrite any pre-existing object for the key.
	 *
	 * @param key key for the spatial object
	 * @param envelope envelope for the object
	 */
	void put(String key, Envelope envelope);

	/**
	 * Remove a spatial object from the index.
	 *
	 * @param key key to remove
	 */
	void remove(String key);

	/**
	 * Clear the entire index. This is only useful when clearing the cache which is indexed.
	 */
	void clear();

	/**
	 * Drop the entire index. This is only useful when dropping the cache which is indexed.
	 * <p/>
	 * This should on y be called when the cache will no longer be used, it frees all necessary resources.
	 */
	void drop();

	/**
	 * Get the keys for the objects which (may) overlap with the given geometry.
	 *
	 * @param envelope envelope to test
	 * @return list of keys of spatial objects which my overlap with the geometry
	 */
	List<String> getOverlappingKeys(Envelope envelope);
}
