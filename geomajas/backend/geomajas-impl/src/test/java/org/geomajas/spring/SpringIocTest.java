/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.spring;

import junit.framework.TestCase;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * SImple test/demonstration of spring dependency injection and the usages needed for Geomajas.
 *
 * @author <a href="mailto:joachim@geosparc.com">Joachim Van der Auwera</a>
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

	/* this fails
	public void testAddSettings() {
		ApplicationService grp = factory.getBean("service.ApplicationService",
				ApplicationServiceImpl.class);
		assertEquals("second does not overwrite first", 61, grp.getTileCacheMaximumSize());
		assertEquals("second does not add to first", "testloc", grp.getApplicationLocation());
	}
	*/
}
