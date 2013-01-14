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

package org.geomajas.project.profiling.jmx;

import junit.framework.Assert;
import org.geomajas.project.profiling.service.OneContainer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * Test to verify that the AOP binding works (building on from BindJMXTest).
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/jmxAopContext.xml" })
public class BindAopTest {

	@Autowired
	private ExampleService service;
	
	@Autowired
	private MBeanServer mBeanServer;

	@Test
	public void testAopJmx() throws Exception {
		ObjectName mbean = new ObjectName("bean:name=profilingAopTest");
		
		mBeanServer.invoke(mbean, "clear", new Object[]{}, new String[]{});
		
		service.doSomething(1);
		service.doSomething(2);
		service.doSomething(3);

		System.out.println("" + mBeanServer.getAttribute(mbean, "Total"));
		Assert.assertEquals(3, ((OneContainer) mBeanServer.getAttribute(mbean, "Total")).getInvocationCount());
		System.out.println("" + mBeanServer.getAttribute(mbean, "GroupData"));
		
		//Thread.sleep(1000000000); // use this to test whether you can connect using JConsole
	}
	
}
