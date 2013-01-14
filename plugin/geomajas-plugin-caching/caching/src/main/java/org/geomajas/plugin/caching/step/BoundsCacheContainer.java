/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.caching.step;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Container for the objects which need to be stored in the bounds cache.
 *
 * @author Joachim Van der Auwera
 */
public class BoundsCacheContainer extends CacheContainer {

	private static final long serialVersionUID = 100L;

	private final Envelope bounds;

	/**
	 * Create object for specific bounds.
	 *
	 * @param bounds bounds
	 */
	public BoundsCacheContainer(Envelope bounds) {
		super();
		this.bounds = bounds;
	}

	/**
	 * Get the cached bounds.
	 *
	 * @return bounds
	 */
	public Envelope getBounds() {
		return bounds;
	}
}
