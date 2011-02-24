/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.caching.configuration;

import org.infinispan.config.Configuration;

/**
 * Interface for marking configurations which can be used by
 * {@link org.geomajas.plugin.caching.cache.InfinispanCacheFactory}.
 *
 * @author Joachim Van der Auwera
 */
public interface InfinispanConfiguration {

	/**
	 * Get the Infinispan configuration object.
	 *
	 * @return configuration object
	 */
	Configuration getInfinispanConfiguration();
}
