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

package org.geomajas.plugin.caching.configuration;

import org.apache.commons.lang.SerializationUtils;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Test for {@link org.geomajas.plugin.caching.configuration.CacheInfo}.
 *
 * @author Jan Venstermans
 */
public class CacheInfoTest {

	@Test
	public void testCacheInfoSerializable() {
		CacheInfo cacheInfo = new CacheInfo();
		cacheInfo.setId("id");
		Map<CacheCategory, CacheConfiguration> configurationMap = new HashMap<CacheCategory, CacheConfiguration>();
		configurationMap.put(CacheCategory.BOUNDS, new CacheConfigurationDummy());
		cacheInfo.setConfiguration(configurationMap);
		SerializationUtils.serialize(cacheInfo);
	}
}
