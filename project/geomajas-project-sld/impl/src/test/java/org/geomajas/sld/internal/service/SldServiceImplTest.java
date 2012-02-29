/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.sld.internal.service;

import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.geomajas.sld.StyledLayerDescriptorInfo;
import org.geomajas.sld.filter.FilterTypeInfo;
import org.geomajas.sld.service.SldException;
import org.jibx.runtime.JiBXException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/sld/internal/service/testContext.xml" })
public class SldServiceImplTest {

	@Autowired
	private SldServiceImpl sldService;

	@Test
	public void testDirectory() throws IOException {
		Assert.assertTrue(sldService.getDirectory() != null);
		Assert.assertTrue(sldService.getDirectory().getFile().isDirectory());
	}

	@Test
	public void testFindAll() throws SldException {
		List<StyledLayerDescriptorInfo> slds = sldService.findAll();
		Assert.assertEquals(1, slds.size());
	}

	@Test
	public void testFindByName() throws SldException {
		StyledLayerDescriptorInfo sld = sldService.findByName("point_attribute");
		Assert.assertNotNull(sld);
	}

	@Test
	public void testSaveOrUpdate() throws SldException, JiBXException {
		StyledLayerDescriptorInfo sld = new StyledLayerDescriptorInfo();
		sld.setName("test");
		try {
			sldService.saveOrUpdate(sld);
			Assert.fail("invalid sld saved");
		} catch (SldException e) {
		}
		sld.setVersion("1.0.0");
		sldService.saveOrUpdate(sld);
		Assert.assertEquals(2, sldService.findAll().size());
	}

	@Test
	public void testCreate() throws SldException, JiBXException {
		// make sure the an SLD with name 'test' is created in case the previous test hasn't been run
		testSaveOrUpdate(); 
		
		StyledLayerDescriptorInfo sld = new StyledLayerDescriptorInfo();
		sld.setVersion("1.0.0");
		sld.setName("test");
		try {
			sldService.create(sld);
			Assert.fail("Creating an already existing sld should fail");
		} catch (SldException e) {
		}

		int prevNumOfSlds = sldService.findAll().size();
		sld.setName("testCreate");
		try {
			sldService.create(sld);
		} catch (SldException e) {
			Assert.fail("Creating failed");
		}
		Assert.assertEquals(sldService.findAll().size(), prevNumOfSlds+1);
	}

	@Test
	public void testValidate() throws SldException {
		FilterTypeInfo filter = new FilterTypeInfo();
		sldService.validate(filter);
	}

}
