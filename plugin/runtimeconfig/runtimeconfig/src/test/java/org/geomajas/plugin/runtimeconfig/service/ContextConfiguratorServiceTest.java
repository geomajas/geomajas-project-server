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
package org.geomajas.plugin.runtimeconfig.service;

import junit.framework.Assert;

import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.plugin.runtimeconfig.RuntimeConfigException;
import org.geomajas.plugin.runtimeconfig.service.MultiRefreshContextLoader.MultiRefreshContext;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = MultiRefreshContextLoader.class, locations = {
		"classpath:org/geomajas/spring/geomajasContext.xml", "classpath*:META-INF/geomajasContext.xml",
		"/org/geomajas/plugin/runtimeconfig/service/ContextA.xml" })
public class ContextConfiguratorServiceTest {

	@Autowired
	ContextConfiguratorService service;

	@Autowired
	MultiRefreshContext context;

	@Test
	public void testBeanLoading() throws RuntimeConfigException {
		LifeCycleBean a = (LifeCycleBean) context.getBean("beanA");
		Assert.assertEquals(LifeCycleBean.LifeCycle.INITIALIZED, a.getLifeCycle());
		Assert.assertFalse(context.containsBean("beanB"));
		Assert.assertFalse(context.containsBean("beanC"));
		service.configureBeanDefinitions("/org/geomajas/plugin/runtimeconfig/service/ContextB.xml");
		service.configureBeanDefinitions("/org/geomajas/plugin/runtimeconfig/service/ContextC.xml");
		LifeCycleBean b = (LifeCycleBean) context.getBean("beanB");
		LifeCycleBean c = (LifeCycleBean) context.getBean("beanC");
		Assert.assertEquals(LifeCycleBean.LifeCycle.INITIALIZED, a.getLifeCycle());
		Assert.assertEquals(LifeCycleBean.LifeCycle.INITIALIZED, b.getLifeCycle());
		Assert.assertEquals(LifeCycleBean.LifeCycle.INITIALIZED, c.getLifeCycle());
	}

	@Test
	public void testBeanLoadingBadDefinition() {
		LifeCycleBean a = (LifeCycleBean) context.getBean("beanA");
		Assert.assertEquals(LifeCycleBean.LifeCycle.INITIALIZED, a.getLifeCycle());
		try {
			service.configureBeanDefinitions("/org/geomajas/plugin/runtimeconfig/service/ContextBadDefinition.xml");
			Assert.fail("Expected admin exception for bad bean definition");
		} catch (RuntimeConfigException e) {
			Assert.assertEquals(RuntimeConfigException.INVALID_BEAN_DEFINITION_LOCATION, e.getExceptionCode());
		}
		// check restore
		Assert.assertEquals(LifeCycleBean.LifeCycle.INITIALIZED, a.getLifeCycle());
		Assert.assertSame(a, context.getBean("beanA"));
	}

	@Test
	public void testBeanLoadingBadBean() {
		LifeCycleBean a = (LifeCycleBean) context.getBean("beanA");
		Assert.assertEquals(LifeCycleBean.LifeCycle.INITIALIZED, a.getLifeCycle());
		try {
			service.configureBeanDefinitions("/org/geomajas/plugin/runtimeconfig/service/ContextBadBean.xml");
			Assert.fail("Expected admin exception for bad bean definition");
		} catch (RuntimeConfigException e) {
			Assert.assertEquals(RuntimeConfigException.BEAN_CREATION_FAILED_LOCATION, e.getExceptionCode());
		}
		// check old a destroyed
		Assert.assertEquals(LifeCycleBean.LifeCycle.DESTROYED, a.getLifeCycle());
		// check new a
		a = (LifeCycleBean) context.getBean("beanA");
		Assert.assertEquals(LifeCycleBean.LifeCycle.INITIALIZED, a.getLifeCycle());
	}

	@Test
	public void testBeanDestruction() throws RuntimeConfigException {
		service.configureBeanDefinitions("/org/geomajas/plugin/runtimeconfig/service/ContextB.xml");
		service.configureBeanDefinitions("/org/geomajas/plugin/runtimeconfig/service/ContextC.xml");
		LifeCycleBean a = (LifeCycleBean) context.getBean("beanA");
		LifeCycleBean b = (LifeCycleBean) context.getBean("beanB");
		LifeCycleBean c = (LifeCycleBean) context.getBean("beanC");
		Assert.assertEquals(LifeCycleBean.LifeCycle.INITIALIZED, a.getLifeCycle());
		Assert.assertEquals(LifeCycleBean.LifeCycle.INITIALIZED, b.getLifeCycle());
		Assert.assertEquals(LifeCycleBean.LifeCycle.INITIALIZED, c.getLifeCycle());
		service.removeBeanDefinition("beanB");
		LifeCycleBean a2 = (LifeCycleBean) context.getBean("beanA");
		LifeCycleBean c2 = (LifeCycleBean) context.getBean("beanC");
		Assert.assertSame(a, a2);
		Assert.assertSame(c, c2);
		Assert.assertFalse(context.containsBean("beanB"));
		Assert.assertEquals(LifeCycleBean.LifeCycle.DESTROYED, b.getLifeCycle());
	}

	@Test
	public void testAutowiringParentToChild() throws RuntimeConfigException {
		LifeCycleBean a = (LifeCycleBean) context.getBean("beanA");
		Assert.assertEquals(LifeCycleBean.LifeCycle.INITIALIZED, a.getLifeCycle());
		service.configureBeanDefinitions("/org/geomajas/plugin/runtimeconfig/service/ContextB.xml");
		service.configureBeanDefinitions("/org/geomajas/plugin/runtimeconfig/service/ContextC.xml");
		LifeCycleBean b = (LifeCycleBean) context.getBean("beanB");
		LifeCycleBean c = (LifeCycleBean) context.getBean("beanC");
		Assert.assertEquals(3, a.getBeans().size());
		Assert.assertEquals(3, b.getBeans().size());
		Assert.assertEquals(3, c.getBeans().size());
	}

	@Test
	public void testRewiringChildToParent() throws RuntimeConfigException {
		service.configureBeanDefinitions("/org/geomajas/plugin/runtimeconfig/service/ContextB.xml");
		service.configureBeanDefinitions("/org/geomajas/plugin/runtimeconfig/service/ContextC.xml");
		LifeCycleBean a = (LifeCycleBean) context.getBean("beanA");
		LifeCycleBean b = (LifeCycleBean) context.getBean("beanB");
		LifeCycleBean c = (LifeCycleBean) context.getBean("beanC");
		Assert.assertEquals(3, a.getBeans().size());
		Assert.assertEquals(3, b.getBeans().size());
		Assert.assertEquals(3, c.getBeans().size());
		// and removal
		service.removeBeanDefinition("beanB");
		service.removeBeanDefinition("autoB");
		LifeCycleBean c3 = (LifeCycleBean) context.getBean("beanC");
		Assert.assertSame(c, c3);
		Assert.assertEquals(2, a.getBeans().size());
		Assert.assertEquals(2, c3.getBeans().size());
	}

	@Test
	public void testNoRewiringForNestedBeans() throws RuntimeConfigException {
		service.configureBeanDefinitions("/org/geomajas/plugin/runtimeconfig/service/ContextB.xml");
		service.configureBeanDefinitions("/org/geomajas/plugin/runtimeconfig/service/ContextC.xml");
		LifeCycleBean a = (LifeCycleBean) context.getBean("beanA");
		LifeCycleBean b = (LifeCycleBean) context.getBean("beanB");
		LifeCycleBean c = (LifeCycleBean) context.getBean("beanC");
		Assert.assertNull(a.getInnerBean().getC());
		Assert.assertNull(b.getInnerBean().getC());
		Assert.assertNull(c.getInnerBean().getC());
	}

	@Test
	public void testAutowiringForNestedBeans() {
		ClassPathXmlApplicationContext contextABC = new ClassPathXmlApplicationContext(
				"/org/geomajas/plugin/runtimeconfig/service/ContextABC.xml");
		LifeCycleBean a = (LifeCycleBean) contextABC.getBean("beanA");
		LifeCycleBean b = (LifeCycleBean) contextABC.getBean("beanB");
		LifeCycleBean c = (LifeCycleBean) contextABC.getBean("beanC");
		Assert.assertNotNull(a.getInnerBean().getC());
		Assert.assertNotNull(b.getInnerBean().getC());
		Assert.assertNotNull(c.getInnerBean().getC());
	}

	@Test
	public void testSetters() throws BeansException, RuntimeConfigException {
		GenericApplicationContext context = new GenericApplicationContext(new ClassPathXmlApplicationContext(
				"/org/geomajas/plugin/runtimeconfig/service/ContextConfiguratorService.xml"));
		ContextConfiguratorService service = (ContextConfiguratorService) context.getBean("contextConfiguratorService");
		// configure some beans
		service.configureBeanDefinitions("/org/geomajas/plugin/runtimeconfig/service/ContextA.xml");
		service.configureBeanDefinitions("/org/geomajas/plugin/runtimeconfig/service/ContextB.xml");
		service.configureBeanDefinitions("/org/geomajas/plugin/runtimeconfig/service/ContextC.xml");
		// get test rewirable
		TestRewirable testRewirable = (TestRewirable) context.getBean("testRewirable");
		// check rewiring
		Assert.assertEquals(3, testRewirable.getBeans().size());
		// check post-processing
		Assert.assertTrue(testRewirable.isProcessed());
	}
		
	@After
	public void after() {
		context.refresh();
	}

}
