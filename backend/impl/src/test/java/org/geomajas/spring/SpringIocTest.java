/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.spring;

import junit.framework.TestCase;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * SImple test/demonstration of spring dependency injection and the usages needed for Geomajas.
 *
 * @author Joachim Van der Auwera
 */
public class SpringIocTest extends TestCase {

	private static final String EXAMPLE_INTERFACE = "ExampleInterface";
	private static final String DATA = "Data";
	private static final String DATA_FACTORY = "DataFactory";
	private static final String EXAMPLE_CONFIG = "configuredExample";

	private BeanFactory factory;

	protected void setUp() throws Exception {
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
				new String[] {"org/geomajas/spring/testContext.xml", "org/geomajas/spring/moreContext.xml"});
		// of course, an ApplicationContext is just a BeanFactory
		factory = appContext;
	}

	public void testGetExample() {
		ExampleInterface ex = (ExampleInterface) factory.getBean(EXAMPLE_INTERFACE);
		assertNotNull(ex);
		assertEquals(ExampleImplementation.HELLO_WORLD, ex.helloWorld());

		ExampleInterface ex2 = factory.getBean(EXAMPLE_INTERFACE, ExampleInterface.class);
		assertNotNull(ex2);
		assertEquals(ExampleImplementation.HELLO_WORLD, ex2.helloWorld());

		assertTrue(factory.isSingleton(EXAMPLE_INTERFACE));
		assertFalse(factory.isPrototype(EXAMPLE_INTERFACE));
	}

	public void testGetDataBean() {
		DataFactory dataFactory = factory.getBean(DATA_FACTORY, DataFactory.class);
		assertNotNull(dataFactory);

		assertTrue(factory.isSingleton(DATA_FACTORY));
		assertFalse(factory.isPrototype(DATA_FACTORY));

		assertFalse(factory.isSingleton(DATA));
		assertTrue(factory.isPrototype(DATA));

		Data data1 = dataFactory.createData();
		Data data2 = dataFactory.createData();
		assertTrue(data1 != data2);
	}

	public void testExtraConfig() {
		ExampleInterface ex = factory.getBean(EXAMPLE_CONFIG, ExampleInterface.class);
		assertEquals(ConfiguredExample.HELLO_WORLD, ex.helloWorld());
	}
}
