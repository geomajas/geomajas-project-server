package org.geomajas.plugin.runtimeconfig.service;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.layer.VectorLayer;
import org.geomajas.plugin.runtimeconfig.RuntimeConfigException;
import org.geomajas.plugin.runtimeconfig.service.BeanDefinitionDtoConverterService.NamedObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/beanContext.xml", "/org/geomajas/testdata/layerBeans.xml" })
public class TestContextConversionTest {

	@Autowired
	@Qualifier("beans")
	private VectorLayer beansLayer;

	@Autowired
	@Qualifier("beansInfo")
	private VectorLayerInfo beansLayerInfo;

	@Autowired
	BeanDefinitionDtoConverterService converterService;

	@Autowired
	ContextConfiguratorService configuratorService;

	@Test
	public void testIntrospectionAndConversion() throws RuntimeConfigException {
		NamedObject info1 = new NamedObjectInfo(beansLayerInfo, "beansInfo");
		NamedObject info2 = new NamedObjectInfo(beansLayer, "beans");
		List<BeanDefinitionHolder> defs = converterService.createBeanDefinitionsByIntrospection(Arrays.asList(info1,
				info2));
		Assert.assertEquals(2, defs.size());
		configuratorService.configureBeanDefinition(defs.get(0).getBeanName(), defs.get(0).getBeanDefinition());
		try {
			configuratorService.configureBeanDefinition(defs.get(1).getBeanName(), defs.get(1).getBeanDefinition());
			Assert.fail("Expected exception for bean layer introspection (not a javabean)");
		} catch (RuntimeConfigException e) {
		}
	}

	class NamedObjectInfo implements NamedObject {

		private Object o;

		private String name;

		public NamedObjectInfo(Object o, String name) {
			this.o = o;
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public Object getObject() {
			return o;
		}

	}
}
