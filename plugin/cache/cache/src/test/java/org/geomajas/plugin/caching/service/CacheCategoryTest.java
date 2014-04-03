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

import org.apache.commons.lang.SerializationUtils;
import org.junit.Test;

/**
 * Test for {@link org.geomajas.plugin.caching.service.CacheCategory}.
 *
 * @author Jan Venstermans
 */
public class CacheCategoryTest {

	@Test
	public void testCacheCategorySerializable() {
		CacheCategory cacheCategory = new CacheCategory("nieuw");
		CacheCategory clone = (CacheCategory) SerializationUtils.clone(cacheCategory);
		SerializationUtils.serialize(cacheCategory);
	}

}
