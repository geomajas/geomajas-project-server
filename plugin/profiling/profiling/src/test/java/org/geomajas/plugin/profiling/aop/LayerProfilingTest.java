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

package org.geomajas.plugin.profiling.aop;

import junit.framework.Assert;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.dto.LogRequest;
import org.geomajas.command.dto.SearchAttributesRequest;
import org.geomajas.command.dto.SearchAttributesResponse;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.project.profiling.service.OneContainer;
import org.geomajas.testdata.ReloadContext;
import org.geomajas.testdata.ReloadContextTestExecutionListener;
import org.geomajas.testdata.ReloadContext.ClassMode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import java.util.List;

/**
 * Test to verify that the AOP binding works (building on from BindJMXTest).
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(listeners = { ReloadContextTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/beanContext.xml", "/org/geomajas/testdata/layerBeans.xml",
		"/org/geomajas/testdata/allowAll.xml" })
@ReloadContext(classMode=ClassMode.BEFORE_EACH_TEST_METHOD)
public class LayerProfilingTest {

	private static final String LAYER_ID = "beans";

	@Autowired
	private CommandDispatcher dispatcher;

	@Autowired
	private MBeanServer mBeanServer;

	@Test
	public void testLayerProfiling() throws Exception {
		ObjectName mbean = new ObjectName("bean:name=org.geomajas.profiling.layers");

		mBeanServer.invoke(mbean, "clear", new Object[]{}, new String[]{});

		SearchAttributesRequest request = new SearchAttributesRequest();
		request.setLayerId(LAYER_ID);
		request.setAttributePath("manyToOneAttr");
		SearchAttributesResponse response = (SearchAttributesResponse) dispatcher.execute(
				SearchAttributesRequest.COMMAND, request, null, "en");
		org.junit.Assert.assertFalse(response.isError());
		List<Attribute<?>> attributes = response.getAttributes();
		org.junit.Assert.assertNotNull(attributes);
		org.junit.Assert.assertEquals(2, attributes.size());

		System.out.println("" + mBeanServer.getAttribute(mbean, "Total"));
		Assert.assertEquals(1, ((OneContainer) mBeanServer.getAttribute(mbean, "Total")).getInvocationCount());
		System.out.println("" + mBeanServer.getAttribute(mbean, "GroupData"));

		//Thread.sleep(1000000000); // use this to test whether you can connect using JConsole
	}

}
