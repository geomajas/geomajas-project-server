package org.geomajas.internal.service;
/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */


import org.geomajas.global.GeomajasException;
import org.geomajas.service.ResourceService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests for the DtoConverterService, specifically testing the feature conversions.
 * 
 * @author Jan De Moerloose
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/internal/service/resourceServiceContext.xml" })

public class ResourceServiceTest {

	@Autowired
	private ResourceService resourceService;

	@Test
	public void testClasspath() throws GeomajasException {
		Resource res = resourceService.find("/org/geomajas/internal/service/resourceServiceContext.xml");
		Assert.assertTrue(res instanceof ClassPathResource);
	}

	@Test
	public void testAppContext() throws GeomajasException {
		Resource res = resourceService.find("http://www.geomajas.com");
		Assert.assertTrue(res instanceof UrlResource);
	}

	@Test
	public void testRootFile() throws GeomajasException {
		Resource res = resourceService.find("someResource.txt");
		Assert.assertTrue(res instanceof ClassPathResource);
	}

	@Test
	public void testResourceInfo() throws GeomajasException {
		// check if ResourceInfo is registered
		Assert.assertEquals(2, resourceService.getRootPaths().size());
		// find from extra path (see ResourceInfo definition in resourceServiceContext.xml)
		Resource res = resourceService.find("internal/service/someResource.txt");
		Assert.assertTrue(res instanceof ClassPathResource);
	}

}
