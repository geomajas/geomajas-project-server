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

package org.geomajas.plugin.printing.component.service;

import junit.framework.Assert;

import org.geomajas.plugin.printing.PrintingException;
import org.geomajas.plugin.printing.component.DummyComponent;
import org.geomajas.plugin.printing.component.dto.DummyComponentInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/printing/printing.xml", "/org/geomajas/testdata/layerBluemarble.xml",
		"/org/geomajas/testdata/layerCountries.xml", "/org/geomajas/testdata/simplemixedContext.xml" })
@Transactional(rollbackFor = { org.geomajas.global.GeomajasException.class })
public class PrintDtoConverterServiceTest {

	@Autowired
	private PrintDtoConverterService service;

	@Autowired
	ApplicationContext context;

	@Test
	public void testPrototypeScope() throws PrintingException {
		DummyComponentInfo info = new DummyComponentInfo();
		DummyComponent comp1 = (DummyComponent) service.toInternal(info);
		DummyComponent comp2 = (DummyComponent) service.toInternal(info);
		Assert.assertTrue(comp1 != comp2);
	}
	
	@Test
	public void testRuntimeInjection() throws PrintingException {
		DummyComponentInfo info = new DummyComponentInfo();
		DummyComponent comp = (DummyComponent) service.toInternal(info);
		Assert.assertTrue(comp.isInjected());
	}



}
