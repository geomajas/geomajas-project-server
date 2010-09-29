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

package org.geomajas.plugin.caching.service;

import org.geomajas.global.Api;

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
