/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.runtimeconfig.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.geomajas.plugin.runtimeconfig.RuntimeConfigException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:org/geomajas/spring/geomajasContext.xml",
		"classpath*:META-INF/geomajasContext.xml",
		"/org/geomajas/plugin/runtimeconfig/service/converter/ContextA.xml" })
public class BeanDefinitionWriterServiceTest {

	@Autowired
	ConfigurableApplicationContext context;

	@Test
	public void testWriteAndBack() throws NoSuchBeanDefinitionException, RuntimeConfigException, IllegalStateException,
			IOException {
		BeanDefinitionWriterServiceImpl service = new BeanDefinitionWriterServiceImpl();
		String basePath = File.createTempFile("tmp", "tmp").getParentFile().getAbsolutePath() + File.separator;
		service.setBaseResource(new FileSystemResource(basePath));
		List<BeanDefinitionHolder> beans = new ArrayList<BeanDefinitionHolder>();
		beans.add(new BeanDefinitionHolder(context.getBeanFactory().getBeanDefinition("beanA"), "beanA"));
		beans.add(new BeanDefinitionHolder(context.getBeanFactory().getBeanDefinition("beanB"), "beanB"));
		service.persist("test", beans);
		GenericXmlApplicationContext generic = new GenericXmlApplicationContext(service.getBaseResource().createRelative("test.xml"));
		Assert.assertEquals(context.getBean("beanA"), generic.getBean("beanA"));
		Assert.assertEquals(context.getBean("beanB"), generic.getBean("beanB"));
	}
}
