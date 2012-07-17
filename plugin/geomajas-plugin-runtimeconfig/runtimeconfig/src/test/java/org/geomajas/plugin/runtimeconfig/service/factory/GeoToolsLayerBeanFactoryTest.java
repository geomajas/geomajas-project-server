package org.geomajas.plugin.runtimeconfig.service.factory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.geotools.GeoToolsLayer;
import org.geomajas.plugin.runtimeconfig.RuntimeConfigException;
import org.geomajas.plugin.runtimeconfig.service.BeanDefinitionWriterServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:org/geomajas/spring/geomajasContext.xml",
		"classpath*:META-INF/geomajasContext.xml", "/org/geomajas/testdata/layerCountries.xml" })
public class GeoToolsLayerBeanFactoryTest {

	@Autowired
	@Qualifier("countries")
	private VectorLayer countries;

	@Autowired
	@Qualifier("countriesInfo")
	private VectorLayerInfo countriesInfo;
	
	@Autowired
	private GeoToolsLayerBeanFactory beanFactory;

	@Test
	public void test() throws RuntimeConfigException, IOException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(GeoToolsLayerBeanFactory.BEAN_NAME, "testLayer");
		params.put(GeoToolsLayerBeanFactory.CLASS_NAME, GeoToolsLayer.class);
		params.put(GeoToolsLayerBeanFactory.LAYER_INFO, countriesInfo);
		params.put(GeoToolsLayerBeanFactory.STYLE_INFO, countriesInfo.getNamedStyleInfos());
		List<BeanDefinitionHolder> beans = beanFactory.createBeans(params);
		System.out.println(new BeanDefinitionWriterServiceImpl().toString(beans));
		Assert.assertEquals(2, beans.size());
	}
}
