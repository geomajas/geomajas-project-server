package org.geomajas.plugin.admin.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.geomajas.plugin.admin.AdminException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/admin/service/TestBeanFactoryService.xml" })
public class BeanFactoryServiceTest {

	@Autowired
	private BeanFactoryService beanFactoryService;

	@Test
	public void testCreateBeans() throws AdminException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("test1", "value1");
		params.put("test2", "value2");
		params.put("test3", "value3");
		List<BeanDefinitionHolder> holders = beanFactoryService.createBeans(params);
		Assert.assertEquals(1, holders.size());
		Assert.assertEquals("value3", holders.get(0).getBeanName());
		
		params.clear();
		params.put("test1", "value1");
		params.put("test2", "value2");
		holders = beanFactoryService.createBeans(params);
		Assert.assertEquals(1, holders.size());
		Assert.assertEquals("value2", holders.get(0).getBeanName());

		params.clear();
		params.put("test1", "value1");
		params.put("test3", "value3");
		holders = beanFactoryService.createBeans(params);
		Assert.assertEquals(1, holders.size());
		Assert.assertEquals("value3", holders.get(0).getBeanName());

		params.clear();
		params.put("test1", "value1");
		holders = beanFactoryService.createBeans(params);
		Assert.assertEquals(0, holders.size());

	}

	public static class TestBeanFactory implements BeanFactory {

		private String name;

		private Priority priority;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Priority getPriority() {
			return priority;
		}

		public void setPriority(Priority priority) {
			this.priority = priority;
		}

		public Priority getPriority(Map<String, Object> parameters) {
			if (parameters.containsKey(name)) {
				return priority;
			} else {
				return Priority.NONE;
			}
		}

		public List<BeanDefinitionHolder> createBeans(Map<String, Object> parameters) {
			return Collections.singletonList(new BeanDefinitionHolder(new GenericBeanDefinition(), (String) parameters.get(name)));
		}

	}

}
