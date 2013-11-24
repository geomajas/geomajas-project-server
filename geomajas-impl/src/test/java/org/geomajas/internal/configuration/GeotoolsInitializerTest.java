package org.geomajas.internal.configuration;

import junit.framework.Assert;

import org.geotools.util.logging.Logging;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class GeotoolsInitializerTest {

	@Test
	public void testLogging() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
		context.setId("test");
		context.setDisplayName("test");
		context.setConfigLocation("/org/geomajas/spring/geomajasContext.xml ");
		context.refresh();
		Assert.assertSame(Logging.GEOTOOLS.getLoggerFactory(), Slf4jLoggerFactory.getInstance());
	}

}
