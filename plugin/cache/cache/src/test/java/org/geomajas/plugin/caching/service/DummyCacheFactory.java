/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.caching.service;

import org.geomajas.layer.Layer;

/**
 * Dummy cache factory for testing.
 *
 * @author Joachim Van der Auwera
 */
public class DummyCacheFactory implements CacheFactory {
	private String test;

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public CacheService create(Layer layer, CacheCategory category) {
		return new DummyCacheService();
	}
}
