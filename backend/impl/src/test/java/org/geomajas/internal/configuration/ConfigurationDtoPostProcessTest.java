package org.geomajas.internal.configuration;

import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.global.ConfigurationDtoPostProcess;
import org.geomajas.global.ConfigurationHelper;
import org.geomajas.layer.LayerException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ConfigurationDtoPostProcessTest {

	@Test
	public void testCall() throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
		context.setId("test");
		context.setDisplayName("test");
		context.setConfigLocation("/org/geomajas/spring/geomajasContext.xml,"
				+ "/org/geomajas/testdata/beanContext.xml,"
				+ "/org/geomajas/testdata/layerBeans.xml,"
				+ "/org/geomajas/internal/configuration/postProcess.xml");
		context.refresh();
		PostProcess pp = (PostProcess) context.getBean("postProcess");
		pp.validate();
	}

	public static class PostProcess implements ConfigurationDtoPostProcess {
		
		ConfigurationHelper configurationHelper;

		@Override
		public void processConfiguration(ConfigurationHelper configurationHelper) {
			if(this.configurationHelper == null) {
				this.configurationHelper = configurationHelper;
			} else {
				Assert.fail("ConfigurationDtoPostProcess called twice !");
			}
		}
		
		public void validate() throws LayerException {
			Assert.assertNotNull(configurationHelper);
			ClientVectorLayerInfo layer = new ClientVectorLayerInfo();
			layer.setServerLayerId("beans");
			layer.setId("clientLayerBeans");
			layer.setZoomToPointScale(layer.getMaximumScale());
			
			ScaleInfo scale = new ScaleInfo();
			scale.setDenominator(1000);
			scale.setNumerator(1);
			
			configurationHelper.completeScale(scale, 5);
			configurationHelper.postProcess(layer, "EPSG:4326", 5);
			// some basic tests to see if post-processing is done
			Assert.assertEquals(0.005, scale.getPixelPerUnit(), 0.00001);
			Assert.assertNotNull(layer.getLayerInfo());
		}

	}
}
