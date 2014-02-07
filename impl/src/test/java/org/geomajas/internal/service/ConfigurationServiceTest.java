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

package org.geomajas.internal.service;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.configuration.LayerExtraInfo;
import org.geomajas.configuration.LayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.geometry.Crs;
import org.geomajas.layer.Layer;
import org.geomajas.layer.VectorLayer;
import org.geomajas.service.ConfigurationService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test for {@link ConfigurationServiceImpl}.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/beanContext.xml", "/org/geomajas/testdata/layerBeans.xml"})
public class ConfigurationServiceTest {

	private static final String APPLICATION_ID = "bean";
	private static final String MAP_ID = "beanMap";
	private static final String VECTOR_LAYER_ID = "beans";

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	@Qualifier("beans")
	Layer beansLayer;

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

	@Test
	public void getVectorLayerTest() throws Exception {
		Layer layer;
		Assert.assertNull(configurationService.getRasterLayer(VECTOR_LAYER_ID));
		layer = configurationService.getLayer(VECTOR_LAYER_ID);
		Assert.assertNotNull(layer);
		Assert.assertTrue(layer instanceof VectorLayer);
		layer = configurationService.getVectorLayer(VECTOR_LAYER_ID);
		Assert.assertNotNull(layer);
		Assert.assertTrue(layer instanceof VectorLayer);
	}

	@Test
	public void getMapTest() throws Exception {
		ClientMapInfo map = configurationService.getMap(MAP_ID, APPLICATION_ID);
		Assert.assertNotNull(map);
		Assert.assertEquals(MAP_ID, map.getId());
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
