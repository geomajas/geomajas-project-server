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

package org.geomajas.plugin.caching.step;

import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.plugin.caching.service.CacheContext;

/**
 * Container for the objects which need to be stored in the bounds cache.
 *
 * @author Joachim Van der Auwera
 */
public class BoundsCacheContainer {

	private CacheContext context;
	private Envelope bounds;

	/**
	 * Get the context for this cached object to allow verifying key uniqueness.
	 *
	 * @return cache context
	 */
	public CacheContext getContext() {
		return context;
	}

	/**
	 * Set the context for this cached object to allow verifying key uniqueness.
	 *
	 * @param context cache context
	 */
	public void setContext(CacheContext context) {
		this.context = context;
	}

	/**
	 * Get the cached bounds.
	 *
	 * @return bounds
	 */
	public Envelope getBounds() {
		return bounds;
	}

	/**
	 * Set the cached bounds.
	 *
	 * @param bounds bounds
	 */
	public void setBounds(Envelope bounds) {
		this.bounds = bounds;
	}
}
