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

package org.geomajas.internal.service;

import org.geomajas.configuration.ImageInfo;
import org.geomajas.configuration.LayerInfo;
import org.geomajas.service.ConfigurationService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Test for {@link ConfigurationServiceImpl}.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/layer/bean/beanContext.xml", "/org/geomajas/layer/bean/layerBeans.xml"})
public class ConfigurationServiceTest {

	@Autowired
	private ConfigurationService configurationService;

	@Test
	public void testGetLayerExtraInfo() {
		LayerInfo layerInfo = new LayerInfo();
		Map<String, Object> extra = new HashMap<String, Object>();
		extra.put("one", Integer.valueOf(1));
		extra.put(ImageInfo.class.getName(), new ImageInfo());
		layerInfo.setExtraInfo(extra);

		Assert.assertNotNull(configurationService.getLayerExtraInfo(layerInfo, ImageInfo.class.getName(),
				ImageInfo.class));
		Assert.assertEquals(Integer.valueOf(1), configurationService.getLayerExtraInfo(layerInfo, "one", Integer.class));
		Assert.assertNull(configurationService.getLayerExtraInfo(layerInfo, "one", ImageInfo.class));
		Assert.assertNotNull(configurationService.getLayerExtraInfo(layerInfo, ImageInfo.class));
	}

}
