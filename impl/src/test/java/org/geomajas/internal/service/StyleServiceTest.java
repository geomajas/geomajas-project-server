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

import junit.framework.Assert;

import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.service.StyleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * @author Oliver May
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/beanContext.xml", "/org/geomajas/testdata/layerBeans.xml"})
public class StyleServiceTest {

	@Autowired
	private StyleService styleService;
	
	@Autowired
	@Qualifier("beans")
	VectorLayer beansLayer;

	@Test
	public void testRegisterStyle() throws GeomajasException {
		NamedStyleInfo nsi = new NamedStyleInfo();
		nsi.setName("test");
		String uuid = styleService.registerStyle("beans", nsi);
		NamedStyleInfo nsi2 = styleService.retrieveStyle("beans", uuid);
		Assert.assertEquals(nsi, nsi2);
	}

	public void testRetrieveNonExistingStyle() throws GeomajasException {
		NamedStyleInfo nsi = styleService.retrieveStyle("beans", "veryunlikelystylename");
		Assert.assertNull(nsi);
	}
	
	@Test
	public void testRetrieveExistingStyle() throws GeomajasException {
		NamedStyleInfo nsi = styleService.retrieveStyle("beans", "beansStyleInfo");
		Assert.assertEquals(beansLayer.getLayerInfo().getNamedStyleInfo("beansStyleInfo"), nsi);
	}
	
}
