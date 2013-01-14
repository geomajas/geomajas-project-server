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

package org.geomajas.plugin.profiling.aop;

import junit.framework.Assert;
import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.dto.LogRequest;
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
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/beanContext.xml", "/org/geomajas/testdata/layerBeans.xml",
		"/org/geomajas/testdata/allowAll.xml" })
public class CommandProfilingTest {

	@Autowired
	private CommandDispatcher dispatcher;

	@Autowired
	private MBeanServer mBeanServer;

	@Test
	public void testCommandProfiling() throws Exception {
		ObjectName mbean = new ObjectName("bean:name=org.geomajas.profiling.commandDispatcher");

		mBeanServer.invoke(mbean, "clear", new Object[]{}, new String[]{});

		LogRequest request = new LogRequest();
		request.setLevel(LogRequest.LEVEL_INFO);
		request.setStatement("log something");
		dispatcher.execute(LogRequest.COMMAND, request, null, null);

		System.out.println("" + mBeanServer.getAttribute(mbean, "Total"));
		Assert.assertEquals(1, ((OneContainer) mBeanServer.getAttribute(mbean, "Total")).getInvocationCount());
		System.out.println("" + mBeanServer.getAttribute(mbean, "GroupData"));

		//Thread.sleep(1000000000); // use this to test whether you can connect using JConsole
	}

}
