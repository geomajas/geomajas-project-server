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

import org.geomajas.configuration.LayerExtraInfo;
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
		Map<String, LayerExtraInfo> extra = new HashMap<String, LayerExtraInfo>();
		extra.put("one", new ExtraString1("one"));
		extra.put(ExtraString2.class.getName(), new ExtraString2("two"));
		layerInfo.setExtraInfo(extra);

		Assert.assertNotNull(configurationService.getLayerExtraInfo(layerInfo, ExtraString2.class.getName(),
				ExtraString2.class));
		Assert.assertEquals("one",
				configurationService.getLayerExtraInfo(layerInfo, "one", ExtraString1.class).getString());
		Assert.assertNull(configurationService.getLayerExtraInfo(layerInfo, "one", ExtraString2.class));
		Assert.assertNotNull(configurationService.getLayerExtraInfo(layerInfo, ExtraString2.class));
		Assert.assertNotNull(configurationService.getLayerExtraInfo(layerInfo, "one", ExtraString.class));
	}

	private class ExtraString implements LayerExtraInfo {
		private String str;

		public ExtraString(String str) {
			this.str = str;
		}

		public String getString() {
			return str;
		}
	}

	private class ExtraString1 extends ExtraString {
		public ExtraString1(String str) {
			super(str);
		}
	}

	private class ExtraString2 extends ExtraString {
		public ExtraString2(String str) {
			super(str);
		}
	}
}
