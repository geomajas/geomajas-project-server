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

package org.geomajas.plugin.caching.step;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.caching.service.CacheContext;

import java.io.Serializable;

/**
 * Base container for the objects which need to be stored in the cache, assure the context is included.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public class CacheContainer implements Serializable {

	private static final long serialVersionUID = 100L;

	private CacheContext context;

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
}
