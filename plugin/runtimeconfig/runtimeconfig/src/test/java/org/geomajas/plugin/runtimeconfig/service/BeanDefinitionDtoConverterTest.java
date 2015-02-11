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
package org.geomajas.plugin.runtimeconfig.service;

import junit.framework.Assert;
import org.geomajas.plugin.runtimeconfig.RuntimeConfigException;
import org.geomajas.plugin.runtimeconfig.dto.bean.BeanDefinitionInfo;
import org.geomajas.plugin.runtimeconfig.service.BeanDefinitionDtoConverterService.NamedObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/runtimeconfig/service/converter/ContextA.xml" })
public class BeanDefinitionDtoConverterTest {

	@Autowired
	ConfigurableApplicationContext context;

	@Autowired
	BeanDefinitionDtoConverterService service;
	
	@Autowired
	ContextConfiguratorService configuratorService;

	@Test
	public void testToDtoAndBack() {
		BeanDefinition def = context.getBeanFactory().getBeanDefinition("beanA");
		BeanDefinitionInfo info = service.toDto(def);
		BeanDefinition copy = service.toInternal(info);
		((BeanDefinitionRegistry) context.getBeanFactory()).registerBeanDefinition("beanACopy", copy);
		ConvertTestBean bean = (ConvertTestBean) context.getBean("beanA");
		ConvertTestBean beanACopy = (ConvertTestBean) context.getBean("beanACopy");
		Assert.assertEquals(bean, beanACopy);
	}

	@Test
	public void testIntrospection() throws RuntimeConfigException {
		final ConvertTestBean bean = new ConvertTestBean();
		bean.setList(Arrays.asList(new ConvertTestBean()));
		BeanDefinition definition = service.createBeanDefinitionByIntrospection(bean);
		((BeanDefinitionRegistry) context.getBeanFactory()).registerBeanDefinition("beanACopy", definition);
		NamedObject o1 = new NamedObject() {

			public String getName() {
				return "A";
			}

			public Object getObject() {
				return bean;
			}

		};
		List<BeanDefinitionHolder> holders = service.createBeanDefinitionsByIntrospection(Arrays.asList(o1));
		for (BeanDefinitionHolder holder : holders) {
			configuratorService.configureBeanDefinition(holder.getBeanName(), holder.getBeanDefinition());
		}

	}

}
