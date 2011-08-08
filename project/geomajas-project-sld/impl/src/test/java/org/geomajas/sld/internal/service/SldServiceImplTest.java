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
		} catch (Exception e) {
		}
		sld.setVersion("1.0.0");
		sldService.saveOrUpdate(sld);
		Assert.assertEquals(2, sldService.findAll().size());
	}

	@Test
	public void testValidate() throws SldException {
		FilterTypeInfo filter = new FilterTypeInfo();
		sldService.validate(filter);
	}

}
