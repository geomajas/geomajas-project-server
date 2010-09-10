package org.geomajas.rest.server.mvc;

import java.util.Calendar;
import java.util.List;

import org.geomajas.layer.feature.Feature;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.rest.server.RestException;
import org.geomajas.security.SecurityManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/beanContext.xml", "/org/geomajas/testdata/layerBeans.xml"})
public class RestControllerTest {

	private final Logger log = LoggerFactory.getLogger(RestControllerTest.class);
	
	@Autowired
	RestContoller restController;

	@Autowired
	private SecurityManager securityManager;

	
	@Before
	public void login() {
		// assure security context is set
		securityManager.createSecurityContext(null);
	}
	
	@Test
	public void readOneFeature() throws RestException {
		ModelAndView mav = restController.readOneFeature("beans", "1");
		Object o = mav.getModel().get(RestContoller.FEATURE_COLLECTION);
		Assert.assertTrue(o instanceof List);
		List<InternalFeature> features = (List<InternalFeature>) o; 
		Assert.assertNotNull(features);
		Assert.assertNotNull(features.get(0));
		Assert.assertTrue(features.get(0) instanceof InternalFeature);
		
		Assert.assertEquals("bean1", features.get(0).getAttributes().get("stringAttr").getValue());
		Assert.assertEquals(true, features.get(0).getAttributes().get("booleanAttr").getValue());
		Assert.assertEquals("100,23", features.get(0).getAttributes().get("currencyAttr").getValue());
		Calendar c = Calendar.getInstance();
		c.set(2010, 1, 23, 0, 0, 0);
		c.set(Calendar.MILLISECOND, 0);
		Assert.assertEquals(c.getTime(), features.get(0).getAttributes().get("dateAttr").getValue());
		Assert.assertEquals(123.456, features.get(0).getAttributes().get("doubleAttr").getValue());
		Assert.assertEquals(456.789F, features.get(0).getAttributes().get("floatAttr").getValue());
		Assert.assertEquals("http://www.geomajas.org/image1", features.get(0).getAttributes().get("imageUrlAttr").getValue());
		Assert.assertEquals(789, features.get(0).getAttributes().get("integerAttr").getValue());
		Assert.assertEquals(123456789L, features.get(0).getAttributes().get("longAttr").getValue());
		Assert.assertEquals((short) 123, features.get(0).getAttributes().get("shortAttr").getValue());
		Assert.assertEquals("http://www.geomajas.org/url1", features.get(0).getAttributes().get("urlAttr").
				getValue());
		
	}
	

}
