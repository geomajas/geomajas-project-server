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

package org.geomajas.configuration;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Test for {@link LayerInfo}.
 *
 * @author Joachim Van der Auwera
 */
public class LayerInfoTest {

	@Test
	public void testgetExtraInfo() {
		LayerInfo layerInfo = new LayerInfo();
		Map<String, Object> extra = new HashMap<String, Object>();
		extra.put("one", Integer.valueOf(1));
		extra.put(ImageInfo.class.getName(), new ImageInfo());
		layerInfo.setExtraInfo(extra);

		Assert.assertNotNull(layerInfo.getExtraInfo(ImageInfo.class.getName(), ImageInfo.class));
		Assert.assertEquals(Integer.valueOf(1), layerInfo.getExtraInfo("one", Integer.class));
		Assert.assertNull(layerInfo.getExtraInfo("one", ImageInfo.class));
		Assert.assertNotNull(layerInfo.getExtraInfo(ImageInfo.class));
	}
}
