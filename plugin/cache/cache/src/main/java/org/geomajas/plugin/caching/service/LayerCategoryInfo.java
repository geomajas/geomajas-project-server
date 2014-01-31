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

package org.geomajas.plugin.caching.service;

import org.geomajas.annotation.Api;

/**
 * Definition of layer and cacheCategory for use in application context searches.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public class LayerCategoryInfo {
	private String layerId;
	private CacheCategory category;

	/**
	 * Get layer if for this configuration.
	 *
	 * @return layer id
	 */
	public String getLayerId() {
		return layerId;
	}

	/**
	 * Set layer id for this configuration.
	 *
	 * @param layerId layer id
	 */
	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	/**
	 * Get cache category for this configuration.
	 *
	 * @return cache category
	 */
	public CacheCategory getCategory() {
		return category;
	}

	/**
	 * Set cache category for this configuration.
	 *
	 * @param category cache category
	 */
	public void setCategory(CacheCategory category) {
		this.category = category;
	}
}
