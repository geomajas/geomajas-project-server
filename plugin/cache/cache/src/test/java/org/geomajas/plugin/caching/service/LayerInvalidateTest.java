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

package org.geomajas.plugin.caching.service;

import junit.framework.Assert;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.TestRecorder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Check that layer invalidation test is properly called.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/dummySecurity.xml", "/org/geomajas/testdata/layerBeans.xml", "/org/geomajas/spring/testRecorder.xml"})
public class LayerInvalidateTest {

	private static final String LAYER_BEANS = "beans";

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private TestRecorder recorder;

	@Test
	public void invalidateTest() throws Exception {
		recorder.clear();
		configurationService.invalidateLayer(LAYER_BEANS);
		Assert.assertEquals("", recorder.matches(LAYER_BEANS, "Layer invalidated"));
	}

}
