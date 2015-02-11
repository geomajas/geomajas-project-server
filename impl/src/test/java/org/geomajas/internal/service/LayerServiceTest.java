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

package org.geomajas.internal.service;

import org.geomajas.geometry.Crs;
import org.geomajas.layer.Layer;
import org.geomajas.layer.LayerService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test for {@link LayerService}.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/beanContext.xml", "/org/geomajas/testdata/layerBeans.xml"})
public class LayerServiceTest {

	@Autowired
	@Qualifier("layer.LayerService")
	private LayerService layerService;

	@Autowired
	@Qualifier("beans")
	Layer beansLayer;

	@Test
	public void getLayerCrsTest() throws Exception {
		Crs crs = layerService.getCrs(beansLayer);
		Assert.assertNotNull(crs);
		Assert.assertEquals(beansLayer.getLayerInfo().getCrs(), crs.getId());
	}

}
